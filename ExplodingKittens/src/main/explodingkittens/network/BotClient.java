package main.explodingkittens.network;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.exception.EKNetworkException;
import main.explodingkittens.util.message.IMessage;

/**
 * A client class for the bots TODO: (NOT implemented jet)
 */
public class BotClient implements IClient {

    @Override
    public BotClient connect(String host, int port) throws EKNetworkException {
        return null;
    }

    @Override
    public void disconnect() throws EKIOException {

    }

    @Override
    public void sendMessage(IMessage msg) throws EKIOException {

    }

    @Override
    public IMessage readMessage() throws EKIOException {
        return null;
    }

}
