package main.explodingkittens.network;

import java.util.List;

/**
 * A server interface for hosting a game
 */
public interface IServer {

    /**
     * Creates and sets up the server
     * @param numberPlayers number of players in the game
     * @param numberOfBots number of bots in the game
     */
    void setupServer(int numberPlayers, int numberOfBots);

    /**
     * Get the list of all clients
     * @return the list
     */
    List<IClient> getClients();
}
