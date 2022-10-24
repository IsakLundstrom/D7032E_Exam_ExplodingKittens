package main.explodingkittens.network;

import main.explodingkittens.util.message.IMessage;

/**
 * A client for testing requirements
 */
public class TestClient implements IClient {

    private IMessage readMsg;
    private IMessage sendMsg;

    public TestClient() {
    }

    @Override
    public TestClient connect(String host, int port) {
        return new TestClient();
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void sendMessage(IMessage msg) {
        sendMsg = msg;
    }

    @Override
    public IMessage readMessage() {
        return readMsg;
    }

    public void setReadMsg(IMessage readMsg) {
        this.readMsg = readMsg;
    }

    public IMessage getSendMsg() {
        return sendMsg;
    }
}
