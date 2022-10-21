package main.explodingkittens.game;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.game.card.playablecard.targetcard.TargetCard;
import main.explodingkittens.game.card.unplayablecard.NopeCard;
import main.explodingkittens.game.card.kittencard.KittenCard;
import main.explodingkittens.game.cardpack.ECardPacks;
import main.explodingkittens.game.card.Card;
import main.explodingkittens.game.card.unplayablecard.DefuseCard;
import main.explodingkittens.game.card.kittencard.ExplodingKittenCard;
import main.explodingkittens.io.Console;
import main.explodingkittens.util.message.IMessage;
import main.explodingkittens.util.message.MessageFactory;
import main.explodingkittens.util.message.OptionsMessage;
import main.explodingkittens.io.option.Option;
import main.explodingkittens.io.option.Options;
import main.explodingkittens.network.HumanClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;

public class Game {

    private final Console console = Console.getInstance();
    private final GameState gameState;
    private final int secondsToInterruptWithNope = 5;
    private final String TWO = "Two";
    private final String THREE = "Three";
    private final String PASS = "Pass";

    /**
     * Create a game from a gameState
     *
     * @param gameState the GameState
     */
    public Game(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Create a game with no GameState
     */
    public Game() {
        this(null);
    }

    /**
     * Init and run the game loop of a client
     *
     * @param client the IClinet
     * @throws EKIOException
     */
    public void initClientGame(HumanClient client) throws EKIOException {
        while (true) {
            try {
                IMessage msg = client.readMessage();
                console.print(msg);
                if (msg instanceof OptionsMessage) {
                    if (((OptionsMessage) msg).isTimed()) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        int millisecondsWaited = 0;
                        while (true) {
                            if (!(!br.ready() && millisecondsWaited < (secondsToInterruptWithNope * 1000))) break;
                            Thread.sleep(200);
                            millisecondsWaited += 200;
                        }
                        client.sendMessage(MessageFactory.createMsg(br.ready() ? br.readLine() : " "));
                    } else {
                        client.sendMessage(MessageFactory.createMsg(console.getString(msg.getOptions())));
                    }
                }
            } catch (IOException | InterruptedException | EKIOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    /**
     * Start the server game loop
     *
     * @throws EKIOException
     * @throws InterruptedException
     */
    public void serverGameLoop() throws EKIOException, InterruptedException {
        while (true) {
            startTurn();
            endTurn();
            if (checkGameOver()) {
                sendGameOver();
                System.exit(0);
            }
        }
    }

    /**
     * Set up the games init state
     *
     * @throws EKIOException
     * @throws InterruptedException
     */
    public void setupGame() throws EKIOException, InterruptedException {
        //Give 1 defuse
        for (Player p : gameState.getPlayingOrder()) {
            p.getHand().insert(new DefuseCard());
        }
        // Insert rest of cards
        for (ECardPacks pack : gameState.getCardPacksUsed()) {
            gameState.getDrawPile().insert(pack.getCards());
        }
        //Insert # defuse(s) depending on nrPlayers
        gameState.getDrawPile().insert(new DefuseCard());
        if (gameState.getPlayingOrder().size() < 5) {
            gameState.getDrawPile().insert(new DefuseCard());
        }
        //Shuffle draw
        gameState.getDrawPile().shuffle();
        //Give cards to players and sort
        for (Player p : gameState.getPlayingOrder()) {
            for (int i = 0; i < 7; i++) {
                p.getHand().insert(gameState.getDrawPile().extract(0));
                p.getHand().sort();
            }
        }
        //insert number of kitten cards depending on nrPlayers
        for (int i = 0; i < gameState.getMaxAllowedPlayers() - 1; i++) {
            gameState.getDrawPile().insert(new ExplodingKittenCard());
        }
        //Shuffle again
        gameState.getDrawPile().shuffle();

        //Random player goes first
        int random = new Random().nextInt(0, gameState.getMaxAllowedPlayers() + 1);
        random = 0;
        for (int i = 0; i < random; i++) {
            gameState.nextPlayer();
        }
        //Show hand to all players
        for (Player player : gameState.getPlayingOrder()) {
            player.getClient().sendMessage(MessageFactory.createMsg("Your hand: " + player.getHand().getCards()));
        }
    }

    /**
     * Start a turn for the game
     *
     * @throws EKIOException
     * @throws InterruptedException
     */
    public void startTurn() throws EKIOException, InterruptedException {

        //Select currentPlayer
        Player currentPlayer = gameState.getPlayingOrder().get(0);
        notifyNewTurn(currentPlayer);

        //Get options
        Options playOptions = getPlayOptions(currentPlayer);

        //Show options and get choice
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("You have the following options:", playOptions));
        String chosenPlayOption = currentPlayer.getClient().readMessage().toString();


        //Handle Pass option
        if (chosenPlayOption.equals(PASS)) {
            gameState.addNrTurns(-1);
            return;
        }

        String nopeMsg = "Player " + currentPlayer.getId() + " played: [" + chosenPlayOption + "]";

        //Get target of two or three cards or target card
        int chosenTarget = -1;
        if (chosenPlayOption.contains(TWO) || chosenPlayOption.contains(THREE) || currentPlayer.getHand().get(chosenPlayOption) instanceof TargetCard) {
            chosenTarget = getTarget(currentPlayer);
            nopeMsg += " at player " + chosenTarget;
        }

        Card chosenCard = null;
        if (chosenPlayOption.contains(THREE)) {
            chosenCard = getStealCard(currentPlayer);
            nopeMsg += " trying to steal an " + chosenCard.getName();
        }

        //Check if chosen action was noped
        sendNopeMsg(nopeMsg);

        Player target = gameState.getPlayerById(chosenTarget);
        List<String> cardsInHand = currentPlayer.getHand().toStringList();
        //Handle single card option
        if (cardsInHand.contains(chosenPlayOption)) {
            Card playCard = currentPlayer.getHand().extract(cardsInHand.indexOf(chosenPlayOption));
            if (!gameState.wasNoped()) {
                if (playCard instanceof TargetCard) {
                    ((TargetCard) playCard).action(gameState, target);
                } else {
                    playCard.action(gameState);
                }
            }
            gameState.getDiscardPile().insert(playCard);
        }

        //Handle two card option action
        else if (chosenPlayOption.contains(TWO)) {
            Card stolenCard = null;
            stolenCard = handleTwoCombo(currentPlayer, chosenPlayOption, target);
            notifyTwoCombo(currentPlayer, target, stolenCard);
        }

        //Handle three card option action
        else if (chosenPlayOption.contains(THREE)) {
            Card stolenCard = null;
            stolenCard = handleThreeCombo(currentPlayer, chosenPlayOption, chosenCard, target, stolenCard);
            notifyThreeCombo(currentPlayer, target, stolenCard);

        }
    }

    /**
     * Handle the end of a turn
     *
     * @throws EKIOException
     * @throws InterruptedException
     */
    public void endTurn() throws EKIOException, InterruptedException {
        Player currentPlayer = gameState.getPlayingOrder().get(0);
        if (gameState.shouldDrawCard()) {
            Card drawCard = gameState.getDrawPile().extract(0);
            if (drawCard instanceof KittenCard) {
                drawCard.action(gameState);
                checkExploded(currentPlayer);
            } else {
                currentPlayer.getHand().insert(drawCard);
                currentPlayer.getHand().sort();
            }
        }
        if (gameState.getNrTurns() == 0) {
            gameState.nextPlayer();
            gameState.addNrTurns(1);
        }

        gameState.setDrawCard(true);
    }

    /**
     * Get the play options of a player in a turn
     *
     * @param player the player
     * @return the options
     */
    public Options getPlayOptions(Player player) {
        List<Option> playOptList = new ArrayList<>();
        HashMap<Card, Integer> mapCards = player.getHand().toHashMap();

        //Add single card options
        for (Card card : mapCards.keySet()) {
            if (card.isPlayable()) {
                playOptList.add(new Option(card.getName()));
            }
        }

        //Add two or three cards options
        List<Integer> otherPlayerId = new ArrayList<>();
        for (int i = 1; i < gameState.getPlayingOrder().size(); i++) {
            otherPlayerId.add(gameState.getPlayingOrder().get(i).getId());
        }
        //Add two
        for (Map.Entry<Card, Integer> entry : mapCards.entrySet()) {
            Card card = entry.getKey();
            Integer amount = entry.getValue();
            if (amount >= 2) {
                //RangeOption opt = new RangeOption(TWO + " " + card, "steal a random card from other player " + otherPlayerId, "playerID", otherPlayerId);
                Option opt = new Option(TWO + " " + card, "steal a random card from other player " + otherPlayerId);
                playOptList.add(opt);
            }
        }
        //Add three
        for (Map.Entry<Card, Integer> entry : mapCards.entrySet()) {
            Card card = entry.getKey();
            Integer amount = entry.getValue();
            if (amount >= 3) {
                //RangeOption opt = new RangeOption(THREE + " " + card, "name and pick a card from other player " + otherPlayerId, "playerID", otherPlayerId, "Card Type");
                Option opt = new Option(THREE + " " + card, "name and pick a card from other player " + otherPlayerId);
                playOptList.add(opt);
            }
        }

        //Add pass option
        playOptList.add(new Option(PASS, "to draw a card and end your turn"));

        return new Options(playOptList);
    }

    /**
     * Get target options depending on the game state
     *
     * @return the targetable players as options
     */
    public Options getTargetOptions() {
        List<Option> targetList = new ArrayList<>();
        for (int i = 1; i < gameState.getPlayingOrder().size(); i++) {
            targetList.add(new Option(Integer.toString(gameState.getPlayingOrder().get(i).getId())));
        }
        Options options = new Options(targetList);
        return options;
    }

    /**
     * Get steal card options depending on the game state
     *
     * @return the possible cards steal to steal as options
     */
    public Options getStealCardOptions() {
        List<Option> cardList = new ArrayList<>();
        for (ECardPacks pack : gameState.getCardPacksUsed()) {
            for (Card card : pack.getCards()) {
                Option o = new Option(card.getName());
                if (!cardList.contains(o)) cardList.add(o);
            }
        }
        Options options = new Options(cardList);
        return options;
    }

    /**
     * Handle when two cards wants to be played
     *
     * @param currentPlayer    the playing player
     * @param chosenPlayOption the play option
     * @param target           the target player
     * @return the card stolen
     */
    public Card handleTwoCombo(Player currentPlayer, String chosenPlayOption, Player target) {
        Card stolenCard = null;
        Card playCard = currentPlayer.getHand().extract(currentPlayer.getHand().toStringList().indexOf(chosenPlayOption.split(" ")[1]));
        currentPlayer.getHand().extract(playCard);
        if (!gameState.wasNoped()) {
            stolenCard = twoCardsAction(currentPlayer, target);
        }
        gameState.getDiscardPile().insert(playCard);
        gameState.getDiscardPile().insert(playCard);
        return stolenCard;
    }

    /**
     * Handle when three cards wants to be played
     *
     * @param currentPlayer    the playing player
     * @param chosenPlayOption the play option
     * @param target           the target player
     * @param stolenCard       the card to be stolen
     * @return the card stolen or null
     */
    public Card handleThreeCombo(Player currentPlayer, String chosenPlayOption, Card chosenCard, Player target, Card stolenCard) {
        Card playCard = currentPlayer.getHand().extract(currentPlayer.getHand().toStringList().indexOf(chosenPlayOption.split(" ")[1]));
        currentPlayer.getHand().extract(playCard);
        currentPlayer.getHand().extract(playCard);
        if (!gameState.wasNoped()) {
            stolenCard = threeCardsAction(currentPlayer, target, chosenCard);
        }
        gameState.getDiscardPile().insert(playCard);
        gameState.getDiscardPile().insert(playCard);
        gameState.getDiscardPile().insert(playCard);
        return stolenCard;
    }

    /**
     * The action for playing two cards
     *
     * @param current the player playing
     * @param target  the targeted player
     * @return the card stolen
     */
    public Card twoCardsAction(Player current, Player target) {
        Card card = target.getHand().extract(new Random().nextInt(0, target.getHand().getSize()));
        current.getHand().insert(card);
        return card;
    }

    /**
     * The action for playing three cards
     *
     * @param current   the player playing
     * @param target    the targeted player
     * @param stealCard the steal card
     * @return the card stolen or null if missed
     */
    public Card threeCardsAction(Player current, Player target, Card stealCard) {
        if (target.getHand().getCards().contains(stealCard)) {
            Card stolenCard = target.getHand().extract(stealCard);
            current.getHand().insert(stolenCard);
            return stolenCard;
        }
        return null;
    }

    /**
     * Check if the game is over
     */
    public boolean checkGameOver() {
        if (gameState.getPlayingOrder().size() == 1) {
            return true;
        }
        return false;
    }

    private int getTarget(Player currentPlayer) throws EKIOException {
        int chosenTarget;
        Options options = getTargetOptions();
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("You have the following targets:", options));
        chosenTarget = Integer.parseInt(currentPlayer.getClient().readMessage().toString());
        return chosenTarget;
    }

    private Card getStealCard(Player currentPlayer) throws EKIOException {
        Card chosenCard;
        Options options = getStealCardOptions();
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("You have the following cards to possibly steal:", options));
        String chosenCardString = currentPlayer.getClient().readMessage().toString();
        chosenCard = currentPlayer.getHand().getCards().get(currentPlayer.getHand().toStringList().indexOf(chosenCardString));
        return chosenCard;
    }

    private void notifyTwoCombo(Player currentPlayer, Player target, Card stolenCard) throws EKIOException {
        target.getClient().sendMessage(MessageFactory.createMsg("Player " + currentPlayer.getId() + " randomly took a " + stolenCard));
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("You randomly got a " + stolenCard));
    }

    private void notifyThreeCombo(Player current, Player target, Card stolenCard) throws EKIOException {
        String targetMsg = "Player " + current.getId() + " tried to take a " + stolenCard + " but failed as you did not have one";
        String currentMsg = "You failed to get a " + stolenCard + "as the target did not have one";
        if (stolenCard != null) {
            targetMsg = "Player " + current.getId() + " took a " + stolenCard + "from you";
            currentMsg = "You got a " + stolenCard;
        }
        target.getClient().sendMessage(MessageFactory.createMsg(targetMsg));
        current.getClient().sendMessage(MessageFactory.createMsg(currentMsg));
    }

    private void notifyNewTurn(Player currentPlayer) throws EKIOException {
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("\nYour turn, you have " + gameState.getNrTurns() + " to take"));
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("Your hand: " + currentPlayer.getHand().getCards()));

        //Send not playing players whose turn it is
        for (int i = 1; i < gameState.getPlayingOrder().size(); i++) {
            gameState.getPlayingOrder().get(i).getClient().sendMessage(MessageFactory.createMsg("\nIt is now the turn of player " + currentPlayer.getId() + " with " + gameState.getNrTurns() + " turn(s) to play"));
        }
    }

    private void sendNopeMsg(String msg) throws EKIOException, InterruptedException {
        int nrNopesBefore = gameState.getNrNopes();
        Card nope = new NopeCard();
        List<Option> list = new ArrayList<>();
        list.add(new Option("", "press <<Enter>> to play a Nope"));
        Options nopeOption = new Options(list);
        ExecutorService threadpool = Executors.newFixedThreadPool(gameState.getPlayingOrder().size());
        for (Player player : gameState.getPlayingOrder()) {
            //If no nope
            if (!player.getHand().getCards().contains(nope)) {
                player.getClient().sendMessage(MessageFactory.createMsg(msg + " (you do not have a Nope)"));
                continue;
            }
            //Otherwise send timed msg
            player.getClient().sendMessage(MessageFactory.createMsg(msg, nopeOption, true));
            Runnable task = () -> {
                try {
                    IMessage nextMessage = player.getClient().readMessage(); //Read that is interrupted after secondsToInterruptWithNope
                    if (nextMessage.getMessage().equals("")) {
                        Card nopeCard = player.getHand().extract(nope);
                        gameState.getDiscardPile().insert(nopeCard);
                        gameState.nopePlayed();
                        for (Player p : gameState.getPlayingOrder()) {
                            p.getClient().sendMessage(MessageFactory.createMsg("Player " + player.getId() + " played a Nope"));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("addToDiscardPile: " + e.getMessage());
                }
            };
            threadpool.execute(task);
        }
        //add a delay to avoid concurrency problems with the ObjectInputStream
        threadpool.awaitTermination((secondsToInterruptWithNope * 1000) + 500, TimeUnit.MILLISECONDS);
        for (Player p : gameState.getPlayingOrder()) {
            p.getClient().sendMessage(MessageFactory.createMsg("This timewindow to play Nope passed"));
        }
        //If any nope was played ask players again
        if (gameState.getNrNopes() > nrNopesBefore) {
            sendNopeMsg("Play another Nope? (alternate between Nope and Yup) [Currently] = " + (gameState.wasNoped() ? "Nope" : "Yup"));
        }
        gameState.setNrNopes(0);
    }

    private void checkExploded(Player currentPlayer) throws EKIOException {
        //Current player exploded
        if (!gameState.getPlayingOrder().contains(currentPlayer)) {
            sendExplodedMsg(currentPlayer);
        }
    }

    private void sendExplodedMsg(Player explodedPlayer) throws EKIOException {
        for (Player p : gameState.getAllPlayers()) {
            if (p.equals(explodedPlayer)) {
                explodedPlayer.getClient().sendMessage(MessageFactory.createMsg("You exploded! Better luck next time!"));
            } else {
                p.getClient().sendMessage(MessageFactory.createMsg("Player " + explodedPlayer.getId() + " exploded"));
            }
        }
    }

    private void sendGameOver() throws EKIOException {
        for (Player p : gameState.getAllPlayers()) {
            if (p.equals(gameState.getPlayingOrder().get(0))) {
                gameState.getPlayingOrder().get(0).getClient().sendMessage(MessageFactory.createMsg("You win!"));
            } else {
                p.getClient().sendMessage(MessageFactory.createMsg("Player " + gameState.getPlayingOrder().get(0).getId() + " won the game!"));
            }
        }
    }
}
