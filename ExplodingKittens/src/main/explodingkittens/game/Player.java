package main.explodingkittens.game;

import main.explodingkittens.game.cardpile.CardPile;
import main.explodingkittens.network.IClient;

import java.util.Objects;

public class Player {

    private final int id;
    private final CardPile hand;
    private final IClient client;

    /**
     * Create a player given a id and a client to send and read messages from
     *
     * @param id     the id
     * @param client the client
     */
    public Player(int id, IClient client) {
        this.id = id;
        this.client = client;
        hand = new CardPile();
    }

    /**
     * Get the id
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the client
     *
     * @return the client
     */
    public IClient getClient() {
        return client;
    }

    /**
     * Get the hand (the card pile)
     *
     * @return the hand
     */
    public CardPile getHand() {
        return hand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && Objects.equals(hand, player.hand) && Objects.equals(client, player.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hand, client);
    }
}
