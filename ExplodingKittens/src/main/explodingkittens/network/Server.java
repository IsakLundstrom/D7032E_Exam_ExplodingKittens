package main.explodingkittens.network;

import main.explodingkittens.exception.EKNetworkException;
import main.explodingkittens.io.message.MessageFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements IServer {

    private final List<IClient> clients;
    private final ServerSocket serverSocket;
    private final int port;

    public Server(int port) throws EKNetworkException {
        this.port = port;
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new EKNetworkException("Unable to create server on port: " + port, e);
        }
    }

    public void setupServer(int nrPlayers, int nrBots) {
        //clients.add(new PlayerClient(null, null, true));
        try {
            for (int i = 0; i < nrPlayers; i++) {
                System.out.println("Waiting for " + (nrPlayers - i) + "(more) player(s) to join");
                Socket connectionSocket = serverSocket.accept();
                ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
                ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
                clients.add(new PlayerClient(inFromClient, outToClient, false));
                System.out.println("Player " + i + " connected\n");
                outToClient.writeObject(MessageFactory.createMsg("You connected to the server as player " + i));
            }
        } catch (Exception e) {
            System.out.println("Failed to setup server");
        }
        for (int i = 0; i < nrBots; i++) {
            clients.add(new BotClient());
        }

    }



    @Override
    public List<IClient> getClients() {
        return clients;
    }
}
