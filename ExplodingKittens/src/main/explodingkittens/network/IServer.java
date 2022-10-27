package main.explodingkittens.network;

import main.explodingkittens.exception.EKNetworkException;

import java.util.List;

/**
 * A server interface for hosting a game
 */
public interface IServer {

    /**
     * Creates and sets up the server
     *
     * @param numberPlayers number of players in the game
     * @param numberOfBots  number of bots in the game
     */
    void setupServer(int numberPlayers, int numberOfBots) throws EKNetworkException;

    /**
     * Get the list of all clients
     *
     * @return the list
     */
    List<IClient> getClients();
}
