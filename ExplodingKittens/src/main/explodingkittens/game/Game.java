package main.explodingkittens.game;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.game.card.playablecard.targetcard.TargetCard;
import main.explodingkittens.game.card.Card;
import main.explodingkittens.io.Console;
import main.explodingkittens.util.message.IMessage;
import main.explodingkittens.util.message.MessageFactory;
import main.explodingkittens.util.message.OptionsMessage;
import main.explodingkittens.util.option.Options;
import main.explodingkittens.network.HumanClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Game {

    private final Console console = Console.getInstance();
    private final GameState gameState;
    private final GameLogic gameLogic;

    /**
     * Create a game from a gameState
     *
     * @param gameState the GameState
     */
    public Game(GameState gameState) {
        this.gameState = gameState;
        this.gameLogic = new GameLogic();
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
                            if (!(!br.ready() && millisecondsWaited < (GameConstants.secondsToInterruptWithNope * 1000))) break;
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
        gameLogic.sendShowHand(gameState);
        while (true) {
            startTurn();
            endTurn();
            if (gameLogic.checkGameOver(gameState)) {
                gameLogic.sendGameOver(gameState);
                System.exit(0);
            }
        }
    }

    public void setupGame() {
        Random random = new Random();
        setupGame(random);
    }

    /**
     * Set up the games init state
     *
     * @throws EKIOException
     * @throws InterruptedException
     */
    public void setupGame(Random random) {
        gameLogic.giveOneDefuse(gameState);
        gameLogic.initDrawPile(gameState);
        //Shuffle draw
        gameState.getDrawPile().shuffle(random);
        gameLogic.giveHand(gameState);
        gameLogic.insertKittensDrawPile(gameState);
        //Shuffle again
        gameState.getDrawPile().shuffle(random);
        //Random player goes first
        gameLogic.randomPlayerGoesFirst(gameState, random);
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
        gameLogic.notifyNewTurn(gameState, currentPlayer);

        //Get options
        Options playOptions = gameLogic.getPlayOptions(gameState, currentPlayer);

        //Show options and get choice
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("You have the following options:", playOptions));
        String chosenPlayOption = currentPlayer.getClient().readMessage().toString();


        //Handle Pass option
        if (chosenPlayOption.equals(GameConstants.PASS)) {
            gameState.addNrTurns(-1);
            gameState.setDrawCard(true);
            return;
        }

        String nopeMsg = "Player " + currentPlayer.getId() + " played: [" + chosenPlayOption + "]";

        //Get target of two or three cards or target card
        int chosenTarget = -1;
        if (chosenPlayOption.contains(GameConstants.TWO) || chosenPlayOption.contains(GameConstants.THREE) || currentPlayer.getHand().get(chosenPlayOption) instanceof TargetCard) {
            chosenTarget = gameLogic.getTarget(gameState, currentPlayer);
            nopeMsg += " at player " + chosenTarget;
        }

        //Get steal card if three cards
        String stealCard = "";
        if (chosenPlayOption.contains(GameConstants.THREE)) {
            stealCard = gameLogic.getStealStringCard(gameState, currentPlayer);
            nopeMsg += " trying to steal an " + stealCard;
        }

        //Check if chosen action was noped
        gameLogic.handleNopes(gameState, nopeMsg);

        Player target = gameState.getPlayerById(chosenTarget);
        List<String> cardsInHand = currentPlayer.getHand().toStringList();
        //Handle single card option
        if (cardsInHand.contains(chosenPlayOption)) {
            gameLogic.playSingleCard(gameState, currentPlayer, chosenPlayOption, target);
        }

        //Handle two card option action
        else if (chosenPlayOption.contains(GameConstants.TWO)) {
            Card stolenCard;
            stolenCard = gameLogic.playTwoCombo(gameState, currentPlayer, chosenPlayOption, target);
            gameLogic.notifyTwoCombo(currentPlayer, target, stolenCard);
        }

        //Handle three card option action
        else if (chosenPlayOption.contains(GameConstants.THREE)) {
            Card stolenCard;
            stolenCard = gameLogic.playThreeCombo(gameState, currentPlayer, chosenPlayOption, target, stealCard);
            gameLogic.notifyThreeCombo(currentPlayer, target, stolenCard);
        }
        gameState.setNrNopes(0);
    }

    /**
     * Handle the end of a turn
     *
     * @throws EKIOException
     * @throws InterruptedException
     */
    public void endTurn() throws EKIOException {
        Player currentPlayer = gameState.getPlayingOrder().get(0);
        if (gameState.shouldDrawCard()) {
            gameLogic.drawCard(gameState, currentPlayer);
        }
        if (gameState.getNrTurns() == 0) {
            gameState.nextPlayer();
            gameState.addNrTurns(1);
        }
        gameState.setDrawCard(false);
    }

}
