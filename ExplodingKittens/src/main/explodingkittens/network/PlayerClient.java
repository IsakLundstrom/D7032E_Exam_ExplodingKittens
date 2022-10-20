package main.explodingkittens.network;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.exception.EKNetworkException;
import main.explodingkittens.io.Console;
import main.explodingkittens.io.message.IMessage;
import main.explodingkittens.io.message.MessageFactory;

import java.io.*;
import java.net.Socket;

public class PlayerClient implements IClient {

    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final boolean host;

    public PlayerClient(ObjectInputStream input, ObjectOutputStream output, boolean isHost) {
        this.input = input;
        this.output = output;
        this.host = isHost;
    }

    public PlayerClient() {
        this(null, null, false);
    }

    @Override
    public PlayerClient connect(String host, int port) throws EKNetworkException {
        Socket socket;
        try {
            socket = new Socket(host, port);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            return new PlayerClient(input, output, false);
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
        if (host) {
            Console.getInstance().print(msg.toString());
            return;
        }
        try {
            output.writeObject(msg);
        } catch (IOException e) {
            throw new EKIOException("Could not send the message", e);
        }
    }


    @Override
    public IMessage readMessage() throws EKIOException {
        if (host) {
            return MessageFactory.createMsg(Console.getInstance().getString());
        }
        try {
            return (IMessage) input.readObject();
        } catch (IOException e) {
            throw new EKIOException("Could not read the message", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IMessage readInteruptableMessage(int secondsToInterrupt) throws EKIOException {
        if (!host) {
            return readMessage();
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int millisecondsWaited = 0;
            while (!br.ready() && millisecondsWaited < (secondsToInterrupt * 1000)) {
                Thread.sleep(200);
                millisecondsWaited += 200;
            }
            if (br.ready()) {
                return MessageFactory.createMsg(br.readLine());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return MessageFactory.createMsg(" ");
    }
}
