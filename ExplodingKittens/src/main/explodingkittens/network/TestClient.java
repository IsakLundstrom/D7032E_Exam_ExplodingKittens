package main.explodingkittens.network;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.exception.EKNetworkException;
import main.explodingkittens.util.message.IMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestClient implements IClient {

    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    private IMessage msg;

    public TestClient(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
    }

    public TestClient() {
        this(null, null);
    }

    @Override
    public TestClient connect(String host, int port) throws EKNetworkException {
        Socket socket;
        try {
            socket = new Socket(host, port);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            return new TestClient(input, output);
        } catch (IOException e) {
            //TODO: Set up properties files
            throw new EKNetworkException("Could not connect to host port", e);
            //throw new KoTNetworkException(ResourceLoader.getString(Resource.Error, "unableToConnectToHostPort", host, port), e);
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
