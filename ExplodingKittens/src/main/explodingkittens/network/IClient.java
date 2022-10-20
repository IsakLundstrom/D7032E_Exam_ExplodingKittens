package main.explodingkittens.network;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.exception.EKNetworkException;
import main.explodingkittens.io.message.IMessage;

/**
 * A client interface for connecting to a server
 * @param <T> The implementing class
 */
public interface IClient<T> {

    /**
     * Create a socket and return a new IClient object to this socket.
     *
     * @param host hostname (ip)
     * @param port port
     * @return a Client object
     * @throws EKNetworkException when the connection failed
     */
    T connect(String host, int port) throws EKNetworkException;

    /**
     * Try and disconnect this IClient
     * @throws EKIOException when failed to disconnect
     */
    void disconnect() throws EKIOException;

    /**
     * Send a message string to the remote or local
     * @param msg the message to send
     * @throws EKIOException when a message could not be sent
     */
    public void sendMessage(IMessage msg) throws EKIOException;

    /**
     * Receive a message from remote or local
     *
     * @return the message sent
     * @throws EKIOException when the message could not be read
     */
    public IMessage readMessage() throws EKIOException;

    /**
     * Receive an interuptable message from remote or local
     *
     * @return the message sent
     * @throws EKIOException when the message could not be read
     */
    public IMessage readInteruptableMessage(int secondsToInterrupt) throws EKIOException;

}
