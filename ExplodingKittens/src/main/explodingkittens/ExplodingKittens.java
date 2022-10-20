package main.explodingkittens;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.exception.EKNetworkException;
import main.explodingkittens.game.Game;
import main.explodingkittens.game.GameState;
import main.explodingkittens.game.cardpack.ECardPacks;
import main.explodingkittens.io.Console;
import main.explodingkittens.io.message.MessageFactory;
import main.explodingkittens.io.option.Option;
import main.explodingkittens.io.option.Options;
import main.explodingkittens.network.PlayerClient;
import main.explodingkittens.network.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

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
            game.initServerGame();
        } catch (EKIOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getPacksUsed(GameState gameState) {
        List<String> cardPacks = Stream.of(ECardPacks.values()).map(ECardPacks::name).toList();
        List<String> addedPacks = new ArrayList<>();
        while(true) {
            List<Option> packOptList = new ArrayList<>();
            for (int i = 1; i < cardPacks.size(); i++){
                if (!addedPacks.contains(cardPacks.get(i))) {
                    packOptList.add(new Option(cardPacks.get(i)));
                }
            }
            if (packOptList.size() == 0){
                break;
            }
            String continueKey = "";
            packOptList.add(new Option(continueKey, "press <<Enter>> to continue"));
            Options packOptions = new Options(packOptList);
            console.print(MessageFactory.createMsg("Choose what packs you want to add to the base game:", packOptions));
            String chosenPackOption = console.getString(packOptions);
            if (chosenPackOption.equals(continueKey)) {
                break;
            }
            addedPacks.add(chosenPackOption);
            gameState.addCardPackToGame(chosenPackOption);
        }
    }

    private void setupServer(GameState gameState) {
        int maxPlayingEntities = gameState.getMaxNrPlayers();
        console.print(String.format("Choose number of players (max %d, min 1):", maxPlayingEntities));
        int players = console.getIntMaxMin(maxPlayingEntities, 1);
        console.print(String.format("Choose number of bots (max %d, min 0) (Bots not currently available, please choose 0):", maxPlayingEntities - players));
        int bots = console.getIntMaxMin(maxPlayingEntities - players, 0);
        console.print("What port should the server open on? (eg. 2048)");
        try {
            int port = console.getIntMin(0);
            Server server = new Server(port);
            createLocalClient(port);
            server.setupServer(players, bots);
            gameState.initPlayingEntities(server.getClients());
        } catch (EKNetworkException e) {
            throw new RuntimeException(e);
        }
    }

    private void createLocalClient(int port) {
        ExecutorService threadpool = Executors.newFixedThreadPool(1);
        Runnable connectLocalClient = () -> {
            try {
                Thread.sleep(200);
                PlayerClient client = new PlayerClient().connect("127.0.0.1", port);
                Game game = new Game();
                game.initClientGame(client);
            } catch (InterruptedException | EKNetworkException | EKIOException e) {
                throw new RuntimeException(e);
            }
        };
        threadpool.execute(connectLocalClient);
    }

    private void joinServer() {
        console.print("What ip address has the server? (press <<Enter>> to connect to [ip] = 127.0.0.1)");
        String tmp = console.getString();
        String ip = tmp.equals("") ? "127.0.0.1" : tmp;
        console.print("What port has opened on the server? [ip] = " + ip + "?");
        int port = console.getInt();
        try {
            PlayerClient client = new PlayerClient().connect(ip, port);
            Game game = new Game();
            game.initClientGame(client);
        } catch (EKNetworkException | EKIOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] argv) {
        new ExplodingKittens();
    }
}
