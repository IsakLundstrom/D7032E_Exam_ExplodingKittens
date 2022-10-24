package main.explodingkittens.network;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.exception.EKNetworkException;
import main.explodingkittens.util.message.IMessage;

import java.io.*;
import java.net.Socket;

/**
 * A client for a human to connect to a server through a socket
 */
public class HumanClient implements IClient {

    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    /**
     * Create a human client with an input and output stream
     *
     * @param input  the input stream
     * @param output the output steam
     */
    public HumanClient(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Create a human client
     */
    public HumanClient() {
        this(null, null);
    }

    @Override
    public HumanClient connect(String host, int port) throws EKNetworkException {
        Socket socket;
        try {
            socket = new Socket(host, port);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            return new HumanClient(input, output);
        } catch (IOException e) {
            throw new EKNetworkException("Could not connect to host port", e);
        }
    }

    @Override
    public void disconnect() throws EKIOException {
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            throw new EKIOException("Unable to disconnect", e);
        }
    }

    @Override
    public void sendMessage(IMessage msg) throws EKIOException {
        try {
            output.writeObject(msg);
        } catch (IOException e) {
            throw new EKIOException("Could not send the message", e);
        }
    }


    @Override
    public IMessage readMessage() throws EKIOException {
        try {
            return (IMessage) input.readObject();
        } catch (IOException e) {
            throw new EKIOException("Could not read the message", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
