package main.explodingkittens.game;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.game.card.Card;
import main.explodingkittens.game.card.unplayablecard.kittencard.ExplodingKittenCard;
import main.explodingkittens.game.card.unplayablecard.kittencard.KittenCard;
import main.explodingkittens.game.card.playablecard.targetcard.TargetCard;
import main.explodingkittens.game.card.unplayablecard.DefuseCard;
import main.explodingkittens.game.card.unplayablecard.NopeCard;
import main.explodingkittens.game.cardpack.ECardPacks;
import main.explodingkittens.util.option.Option;
import main.explodingkittens.util.option.Options;
import main.explodingkittens.util.message.IMessage;
import main.explodingkittens.util.message.MessageFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameLogic {

    /**
     * Give 1 defuse per player
     *
     * @param gameState game state
     */
    public void giveOneDefuse(GameState gameState) {
        for (Player p : gameState.getAllPlayers()) {
            p.getHand().insert(new DefuseCard());
        }
    }

    /**
     * Initialize the draw pile before dealing cards
     *
     * @param gameState game state
     */
    public void initDrawPile(GameState gameState) {
        // Insert cards
        for (ECardPacks pack : gameState.getCardPacksUsed()) {
            gameState.getDrawPile().insert(pack.getCards());
        }
        //Insert # defuse(s) depending on nrPlayers
        gameState.getDrawPile().insert(new DefuseCard());
        if (gameState.getAllPlayers().size() < 5) {
            gameState.getDrawPile().insert(new DefuseCard());
        }
    }

    /**
     * Deal cards to all players
     *
     * @param gameState game state
     */
    public void giveHand(GameState gameState) {
        for (Player p : gameState.getAllPlayers()) {
            for (int i = 0; i < 7; i++) {
                p.getHand().insert(gameState.getDrawPile().extract(0));
            }
            p.getHand().sort();
        }
    }

    /**
     * Insert all appropriate Kitten cards in the draw pile
     *
     * @param gameState game state
     */
    public void insertKittensDrawPile(GameState gameState) {
        //insert number of kitten cards depending on nrPlayers
        for (int i = 0; i < gameState.getAllPlayers().size() - 1; i++) {
            gameState.getDrawPile().insert(new ExplodingKittenCard());
        }
    }

    /**
     * Set random player to go first with a random to get predicable results
     *
     * @param gameState game state
     * @param random    a random
     */
    public void randomPlayerGoesFirst(GameState gameState, Random random) {
        int r = random.nextInt(0, gameState.getMaxAllowedPlayers() + 1);
        for (int i = 0; i < r; i++) {
            gameState.nextPlayer();
        }
    }

    /**
     * Play a single card and do its action
     *
     * @param gameState current game state
     * @param player    the player to play the card
     * @param card      the card to play
     * @param target    a possible target
     */
    public void playSingleCard(GameState gameState, Player player, String card, Player target) {
        Card playCard = player.getHand().extract(card);
        if (!gameState.wasNoped()) {
            if (playCard instanceof TargetCard) {
                ((TargetCard) playCard).action(gameState, target);
            } else {
                playCard.action(gameState);
            }
        }
        gameState.getDiscardPile().insert(playCard);
    }

    /**
     * Handle when two cards wants to be played
     *
     * @param gameState        current game state
     * @param currentPlayer    the playing player
     * @param chosenPlayOption the play option
     * @param target           the target player
     * @return the card stolen
     */
    public Card playTwoCombo(GameState gameState, Player currentPlayer, String chosenPlayOption, Player target) {
        Card stolenCard = null;
        Card playCard = currentPlayer.getHand().extract(chosenPlayOption.split(" ")[1]);
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
     * @param gameState        current game state
     * @param currentPlayer    the playing player
     * @param chosenPlayOption the play option
     * @param target           the target player
     * @param stealCard        the card to possibly be stolen
     * @return the card stolen or null
     */
    public Card playThreeCombo(GameState gameState, Player currentPlayer, String chosenPlayOption, Player target, String stealCard) {
        Card stolenCard = null;
        Card playCard = currentPlayer.getHand().extract(chosenPlayOption.split(" ")[1]);
        currentPlayer.getHand().extract(playCard);
        currentPlayer.getHand().extract(playCard);
        if (!gameState.wasNoped()) {
            stolenCard = threeCardsAction(currentPlayer, target, stealCard);
        }
        gameState.getDiscardPile().insert(playCard);
        gameState.getDiscardPile().insert(playCard);
        gameState.getDiscardPile().insert(playCard);
        return stolenCard;
    }

    /**
     * Get the play options of a player in a turn
     *
     * @param gameState current game state
     * @param player    the player
     * @return the options
     */
    public Options getPlayOptions(GameState gameState, Player player) {
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
                Option opt = new Option(GameConstants.TWO + " " + card, "steal a random card from other player " + otherPlayerId);
                playOptList.add(opt);
            }
        }
        //Add three
        for (Map.Entry<Card, Integer> entry : mapCards.entrySet()) {
            Card card = entry.getKey();
            Integer amount = entry.getValue();
            if (amount >= 3) {
                //RangeOption opt = new RangeOption(THREE + " " + card, "name and pick a card from other player " + otherPlayerId, "playerID", otherPlayerId, "Card Type");
                Option opt = new Option(GameConstants.THREE + " " + card, "name and pick a card from other player " + otherPlayerId);
                playOptList.add(opt);
            }
        }

        //Add pass option
        playOptList.add(new Option(GameConstants.PASS, "to draw a card and end your turn"));

        return new Options(playOptList);
    }

    /**
     * Get target options depending on the game state
     *
     * @param gameState current game state
     * @return the targetable players as options
     */
    public Options getTargetOptions(GameState gameState) {
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
     * @param gameState current game state
     * @return the possible cards steal to steal as options
     */
    public Options getStealCardOptions(GameState gameState) {
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
     * The action for playing two cards
     *
     * @param current the player playing
     * @param target  the targeted player
     * @return the card stolen
     */
    public Card twoCardsAction(Player current, Player target) {
        Card card = target.getHand().extract(new Random().nextInt(0, target.getHand().getSize()));
        current.getHand().insert(card);
        current.getHand().sort();
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
    public Card threeCardsAction(Player current, Player target, String stealCard) {
        if (target.getHand().get(stealCard) != null) {
            Card stolenCard = target.getHand().extract(stealCard);
            current.getHand().insert(stolenCard);
            current.getHand().sort();
            return stolenCard;
        }
        return null;
    }

    /**
     * Handle the nopes played
     *
     * @param gameState current game state
     * @param msg       the message to send
     * @throws EKIOException
     * @throws InterruptedException
     */
    public void handleNopes(GameState gameState, String msg) throws EKIOException, InterruptedException {
        int nrNopesBefore = gameState.getNrNopes();
        Card nope = new NopeCard();
        List<Option> list = new ArrayList<>();
        list.add(new Option("", "press <<Enter>> to play a Nope"));
        Options nopeOption = new Options(list);
        ExecutorService threadpool = Executors.newFixedThreadPool(gameState.getPlayingOrder().size());
        for (Player player : gameState.getPlayingOrder()) {
            //If no nope
            if (player.getHand().get(nope) == null) {
                player.getClient().sendMessage(MessageFactory.createMsg(msg + " (you do not have a Nope)"));
                continue;
            }
            //Otherwise sendF timed msg
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
        threadpool.awaitTermination((GameConstants.secondsToInterruptWithNope * 1000) + 500, TimeUnit.MILLISECONDS);
        for (Player p : gameState.getPlayingOrder()) {
            p.getClient().sendMessage(MessageFactory.createMsg("This timewindow to play Nope passed"));
        }
        //If any nope was played ask players again
        if (gameState.getNrNopes() > nrNopesBefore) {
            handleNopes(gameState, "Play another Nope? (alternate between Nope and Yup) [Currently] = " + (gameState.wasNoped() ? "Nope" : "Yup"));
        }
    }

    /**
     * Draw a card for given player and current game state
     *
     * @param gameState current game state
     * @param player    drawing player
     * @throws EKIOException
     */
    public void drawCard(GameState gameState, Player player) throws EKIOException {
        Card drawCard = gameState.getDrawPile().extract(0);
        player.getClient().sendMessage(MessageFactory.createMsg("You drew a: " + drawCard));
        if (drawCard instanceof KittenCard) {
            drawCard.action(gameState);
            sendExplodedMsg(gameState, player, !gameState.getPlayingOrder().contains(player));
        } else {
            player.getHand().insert(drawCard);
        }
        player.getHand().sort();
    }

    /**
     * Check if the game is over
     *
     * @param gameState current game state
     */
    public boolean checkGameOver(GameState gameState) {
        if (gameState.getPlayingOrder().size() == 1) {
            return true;
        }
        return false;
    }

    /**
     * Send game over to all players
     *
     * @param gameState current game state
     * @throws EKIOException
     */
    public void sendGameOver(GameState gameState) throws EKIOException {
        for (Player p : gameState.getAllPlayers()) {
            if (p.equals(gameState.getPlayingOrder().get(0))) {
                gameState.getPlayingOrder().get(0).getClient().sendMessage(MessageFactory.createMsg("You win!"));
            } else {
                p.getClient().sendMessage(MessageFactory.createMsg("Player " + gameState.getPlayingOrder().get(0).getId() + " won the game!"));
            }
        }
    }

    protected int getTarget(GameState gameState, Player currentPlayer) throws EKIOException {
        int chosenTarget;
        Options options = getTargetOptions(gameState);
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("You have the following targets:", options));
        chosenTarget = Integer.parseInt(currentPlayer.getClient().readMessage().toString());
        return chosenTarget;
    }

    protected String getStealStringCard(GameState gameState, Player current) throws EKIOException {
        Options options = getStealCardOptions(gameState);
        current.getClient().sendMessage(MessageFactory.createMsg("You have the following cards to possibly steal:", options));
        String stealCardString = current.getClient().readMessage().toString();
        return stealCardString;
    }

    protected void notifyTwoCombo(Player currentPlayer, Player target, Card stolenCard) throws EKIOException {
        target.getClient().sendMessage(MessageFactory.createMsg("Player " + currentPlayer.getId() + " randomly took a " + stolenCard));
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("You randomly got a " + stolenCard));
    }

    protected void notifyThreeCombo(Player current, Player target, Card stolenCard) throws EKIOException {
        String targetMsg = "Player " + current.getId() + " tried to take a " + stolenCard + " but failed as you did not have one";
        String currentMsg = "You failed to get a " + stolenCard + "as the target did not have one";
        if (stolenCard != null) {
            targetMsg = "Player " + current.getId() + " took a " + stolenCard + "from you";
            currentMsg = "You got a " + stolenCard + " from player " + target.getId();
        }
        target.getClient().sendMessage(MessageFactory.createMsg(targetMsg));
        current.getClient().sendMessage(MessageFactory.createMsg(currentMsg));
    }

    protected void notifyNewTurn(GameState gameState, Player currentPlayer) throws EKIOException {
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("\nYour turn, you have " + gameState.getNrTurns() + " to take"));
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("Your hand: " + currentPlayer.getHand().getCards()));

        //Send not playing players whose turn it is
        for (int i = 1; i < gameState.getPlayingOrder().size(); i++) {
            gameState.getPlayingOrder().get(i).getClient().sendMessage(MessageFactory.createMsg("\nIt is now the turn of player " + currentPlayer.getId() + " with " + gameState.getNrTurns() + " turn(s) to play"));
        }
    }

    protected void sendShowHand(GameState gameState) throws EKIOException {
        //Show hand to all players
        for (Player player : gameState.getPlayingOrder()) {
            player.getClient().sendMessage(MessageFactory.createMsg("Your hand: " + player.getHand().getCards()));
        }
    }

    protected void sendExplodedMsg(GameState gameState, Player player, boolean exploded) throws EKIOException {
        String currentMsg = "You defused the kitten!";
        String otherMsg = "Player " + player.getId() + " defused the kitten!";
        if (exploded) {
            currentMsg = "You exploded! Better luck next time!";
            otherMsg = "Player " + player.getId() + " exploded";
        }
        for (Player p : gameState.getAllPlayers()) {
            if (p.equals(player)) {
                player.getClient().sendMessage(MessageFactory.createMsg(currentMsg));
            } else {
                p.getClient().sendMessage(MessageFactory.createMsg(otherMsg));
            }
        }
    }
}
