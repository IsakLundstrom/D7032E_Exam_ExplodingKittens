package main.explodingkittens.network;

import main.explodingkittens.exception.EKNetworkException;
import main.explodingkittens.util.message.MessageFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A server implementation
 */
public class Server implements IServer {

    private final List<IClient> clients;
    private final ServerSocket serverSocket;
    private final int port;

    /**
     * Create a server
     *
     * @param port the port to open on
     * @throws EKNetworkException
     */
    public Server(int port) throws EKNetworkException {
        this.port = port;
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new EKNetworkException("Unable to create server on port: " + port, e);
        }
    }

    @Override
    public void setupServer(int nrPlayers, int nrBots) throws EKNetworkException {
        try {
            connectHumanPlayers(nrPlayers);
        } catch (IOException e) {
            throw new EKNetworkException("Failed to setup server", e);
        }
        for (int i = 0; i < nrBots; i++) {
            clients.add(new BotClient());
        }

    }

    private void connectHumanPlayers(int nrPlayers) throws IOException {
        for (int i = 0; i < nrPlayers; i++) {
            System.out.println("Waiting for " + (nrPlayers - i) + " player(s) to join");
            Socket connectionSocket = serverSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            clients.add(new HumanClient(inFromClient, outToClient));
            System.out.println("Player " + i + " connected\n");
            outToClient.writeObject(MessageFactory.createMsg("\nYou connected to the server as player " + i));
        }
    }

    @Override
    public List<IClient> getClients() {
        return clients;
    }
}
