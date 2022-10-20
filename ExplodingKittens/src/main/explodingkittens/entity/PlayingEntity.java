package main.explodingkittens.entity;

import main.explodingkittens.game.cardpile.CardPile;
import main.explodingkittens.network.IClient;

public class PlayingEntity {

    private final int id;
    private CardPile hand;
    private IClient client;

    public PlayingEntity(int id, IClient client) {
        this.id = id;
        this.client = client;
        hand = new CardPile();
    }

    public int getId() {
        return id;
    }

    public IClient getClient() {
        return client;
    }

    public CardPile getHand() {
        return hand;
    }

}
