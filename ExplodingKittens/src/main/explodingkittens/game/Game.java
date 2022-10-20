package main.explodingkittens.game;

import main.explodingkittens.entity.PlayingEntity;
import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.game.card.effectcard.unplayablecard.NopeCard;
import main.explodingkittens.game.cardpack.ECardPacks;
import main.explodingkittens.game.card.Card;
import main.explodingkittens.game.card.effectcard.unplayablecard.DefuseCard;
import main.explodingkittens.game.card.kittencard.ExplodingKittenCard;
import main.explodingkittens.io.Console;
import main.explodingkittens.io.message.IMessage;
import main.explodingkittens.io.message.MessageFactory;
import main.explodingkittens.io.message.OptionsMessage;
import main.explodingkittens.io.option.Option;
import main.explodingkittens.io.option.Options;
import main.explodingkittens.network.PlayerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Game {

    private final Console console;
    private final GameState gameState;
    private final int secondsToInterruptWithNope = 5;

    public Game(GameState gameState) {
        this.gameState = gameState;
        this.console = Console.getInstance();
    }

    public Game() {
        this(null);
    }

    public void initServerGame() throws EKIOException, InterruptedException {
        for (PlayingEntity p : gameState.getPlayingOrder()) {
            p.getHand().insert(new DefuseCard());
        }
        for (ECardPacks pack : gameState.getCardPacksUsed()) {
            gameState.getDrawPile().insert(pack.getCards());
        }
        gameState.getDrawPile().insert(new DefuseCard());
        if (gameState.getPlayingOrder().size() < 5) {
            gameState.getDrawPile().insert(new DefuseCard());
        }
        gameState.getDrawPile().shuffle();
        for (PlayingEntity p : gameState.getPlayingOrder()) {
            for (int i = 0; i < 7; i++) {
                p.getHand().insert(gameState.getDrawPile().extract(0));
            }
        }
        for (int i = 0; i < gameState.getMaxNrPlayers() - 1; i++) {
            gameState.getDrawPile().insert(new ExplodingKittenCard());
        }
        gameState.getDrawPile().shuffle();

        int random = new Random().nextInt(0, gameState.getMaxNrPlayers() + 1);
        //random = 0;
        for (int i = 0; i < random; i++) {
            gameState.nextPlayer();
        }
        startNewTurn();
    }

    public void startNewTurn() throws EKIOException, InterruptedException {
        PlayingEntity currentPlayer = gameState.getPlayingOrder().get(0);
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("Your turn, you have " + gameState.getNrTurns() + " to take"));
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("Your hand: " + currentPlayer.getHand().getCards()));

        List<Option> playOptList = new ArrayList<>();
        outerLoop: for (Card card : currentPlayer.getHand().getCards()) {
            if (card.isPlayable()) {
                //check if in list
                for (Option opt: playOptList) {
                    if (opt.getKey().equals(card.getName())){
                        break outerLoop;
                    }
                }
                playOptList.add(new Option(card.getName()));
            }
        }
        //TODO check two or three cards add options

        String passKey = "Pass";
        playOptList.add(new Option(passKey, "to draw a card and end your turn"));
        Options playOptions = new Options(playOptList);
        currentPlayer.getClient().sendMessage(MessageFactory.createMsg("You have the following options:", playOptions));
        String chosenPlayOption = currentPlayer.getClient().readMessage().toString();

        //Handle Pass option
        if (chosenPlayOption.equals(passKey)) {
            endTurn(); //Player passed their turn
            return;
        }

        //Handle single card option
        String msg = "";
        List<String> cardsInHand = currentPlayer.getHand().toStringList();
        if (cardsInHand.contains(chosenPlayOption)) {
            Card playCard = currentPlayer.getHand().extract(cardsInHand.indexOf(chosenPlayOption));
            msg = "Player " + currentPlayer.getId() + " played: " + playCard.getName();
            sendNopeMsg(msg);
            if (!gameState.wasNoped()) {
                playCard.playAction(gameState);
            }
        }


        for (int i = 1; i < gameState.getPlayingOrder().size(); i++) {
            gameState.getPlayingOrder().get(i).getClient().sendMessage(MessageFactory.createMsg("It is now the turn of player " + currentPlayer.getId() + " with " + gameState.getNrTurns() + " turn(s) to play"));
        }


        while (true) {
            gameState.getPlayingOrder().get(0).getClient().sendMessage(MessageFactory.createMsg(gameState.getPlayingOrder().get(0).getHand().toString()));
            gameState.getPlayingOrder().get(1).getClient().sendMessage(MessageFactory.createMsg(gameState.getPlayingOrder().get(1).getHand().toString()));
            console.getString();
        }
    }

    private void sendNopeMsg(String msg) throws EKIOException, InterruptedException {
        int nopePlayedBefore = gameState.getNrNopes();
        String nopeName = new NopeCard().getName();
        List<Option> list = new ArrayList<>();
        list.add(new Option("", "press <<Enter>> to play a Nope"));
        Options nopeOption = new Options(list);
        ExecutorService threadpool = Executors.newFixedThreadPool(gameState.getPlayingOrder().size());
        for (PlayingEntity player : gameState.getPlayingOrder()) {
            //If no nope
            if (!player.getHand().toStringList().contains(nopeName)) {
                player.getClient().sendMessage(MessageFactory.createMsg(msg + " (you do not have a Nope)"));
                break;
            }
            //Otherwise send timed msg
            player.getClient().sendMessage(MessageFactory.createMsg(msg, nopeOption, true));
            Runnable task = () -> {
                try {
                    IMessage nextMessage = player.getClient().readMessage(); //Read that is interrupted after secondsToInterruptWithNope
                    if (nextMessage.getMessage().equals("")) {
                        Card nopeCard = player.getHand().extract(player.getHand().toStringList().indexOf(nopeName));
                        gameState.getDiscardPile().insert(nopeCard);
                        gameState.nopePlayed();
                        for (PlayingEntity p : gameState.getPlayingOrder()) {
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
        for (PlayingEntity p : gameState.getPlayingOrder()) {
            p.getClient().sendMessage(MessageFactory.createMsg("This timewindow to play Nope passed"));
        }
        //If any nope was played ask players again
        if (gameState.getNrNopes() > nopePlayedBefore) {
            for (PlayingEntity p : gameState.getPlayingOrder()) {
                sendNopeMsg("Play another Nope? (alternate between Nope and Yup) [Currently] = " + (gameState.wasNoped() ? "Nope" : "Yup"));
            }
        }
    }

    private void endTurn() {

    }

    public void initClientGame(PlayerClient client) throws EKIOException {
        //ExecutorService threadpool = Executors.newFixedThreadPool(1);
        //Runnable receive = () -> {
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
        //};

        //threadpool.execute(receive);
    }
}
