package main.explodingkittens;

import main.explodingkittens.exception.EKException;
import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.exception.EKNetworkException;
import main.explodingkittens.game.Game;
import main.explodingkittens.game.GameLogic;
import main.explodingkittens.game.GameState;
import main.explodingkittens.game.cardpack.ECardPacks;
import main.explodingkittens.io.Console;
import main.explodingkittens.util.message.MessageFactory;
import main.explodingkittens.util.option.Option;
import main.explodingkittens.util.option.Options;
import main.explodingkittens.network.HumanClient;
import main.explodingkittens.network.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * Class for creating and starting a normal game
 */
public class ExplodingKittens {

    private final Console console;

    public ExplodingKittens() {
        this.console = Console.getInstance();
        console.print("\nWelcome to Exploding Kittens!\n");
        mainMenu();
    }

    private void mainMenu() {
        Options menuOptions = new Options("Host: host a server\nJoin: join a server\nQuit: quit the game");
        console.print(MessageFactory.createMsg("Main menu options: ", menuOptions));
        String chosenMenuOption = console.getString(menuOptions);
        switch (chosenMenuOption) {
            case "Host" -> {
                hostServer();
            }
            case "Join" -> {
                joinServer();
            }
            case "Quit" -> {
                System.exit(0);
            }
        }
    }

    private void hostServer() {
        GameState gameState = new GameState();
        getPacksUsed(gameState);
        setupServer(gameState);
        Game game = new Game(gameState);
        try {
            game.setupGame();
            game.serverGameLoop();
        } catch (EKIOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getPacksUsed(GameState gameState) {
        List<String> existingCardPacks = new ArrayList<>(Stream.of(ECardPacks.values()).map(ECardPacks::name).toList());
        existingCardPacks.remove(ECardPacks.ExplodingKittens.name());
        //Add base game to game state
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        while (true) {
            List<Option> packOptList = new ArrayList<>();
            for (String cardPack : existingCardPacks) {
                packOptList.add(new Option(cardPack));
            }
            String continueKey = "";
            packOptList.add(new Option(continueKey, "press <<Enter>> to continue"));
            Options packOptions = new Options(packOptList);
            console.print(MessageFactory.createMsg("Choose what packs you want to add to the base game:", packOptions));
            String chosenPackOption = console.getString(packOptions);
            if (chosenPackOption.equals(continueKey)) {
                break;
            }
            gameState.addCardPackFromString(chosenPackOption);
            existingCardPacks.remove(chosenPackOption);
        }
    }

    private void setupServer(GameState gameState) {
        int maxPlayingEntities = gameState.getMaxAllowedPlayers();
        console.print(String.format("Choose number of players (max %d, min 1):", maxPlayingEntities));
        int players = console.getIntMaxMin(maxPlayingEntities, 1);
        console.print(String.format("Choose number of bots (max %d, min 0) (Bots not currently available, please choose 0):", maxPlayingEntities - players));
        int bots = console.getIntMaxMin(maxPlayingEntities - players, players > 1 ? 0 : 1);
        console.print("What port should the server open on? (eg. 2048)");
        int port = console.getIntMin(0);
        console.print("Create local client? (1 for yes, 0 for no)");
        int createClient = console.getIntMaxMin(1, 0);
        try {
            Server server = new Server(port);
            if (createClient > 0) createLocalClient(port);
            server.setupServer(players, bots);
            gameState.initPlayers(server.getClients());
        } catch (EKException e) {
            throw new RuntimeException(e);
        }
    }

    private void createLocalClient(int port) {
        ExecutorService threadpool = Executors.newFixedThreadPool(1);
        Runnable connectLocalClient = () -> {
            try {
                Thread.sleep(200);
                HumanClient client = new HumanClient().connect("127.0.0.1", port);
                Game game = new Game();
                game.initClientGame(client);
            } catch (InterruptedException | EKNetworkException | EKIOException e) {
                throw new RuntimeException(e);
            }
        };
        threadpool.execute(connectLocalClient);
    }

    private void joinServer() {
        console.print("What ip address has the server? (press <<Enter>> to connect to ip = 127.0.0.1)");
        String tmp = console.getString();
        String ip = tmp.equals("") ? "127.0.0.1" : tmp;
        console.print("What port has opened on the server? [ip = " + ip + "?]");
        int port = console.getInt();
        try {
            HumanClient client = new HumanClient().connect(ip, port);
            Game game = new Game();
            game.initClientGame(client);
        } catch (EKNetworkException | EKIOException e) {
            e.printStackTrace();
        }
    }


    /**
     * The method for starting the game
     *
     * @param argv
     */
    public static void main(String[] argv) {
        new ExplodingKittens();
    }
}
