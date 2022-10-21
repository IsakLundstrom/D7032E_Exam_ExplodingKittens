package main.explodingkittens.game.card;

import main.explodingkittens.game.GameState;

import java.util.Objects;

/**
 * Inheriting cards are possible cards in the game
 */
public abstract class Card {

    protected String name;
    protected boolean playable;

    public Card() {
        this.name = "Card name placeholder";
    }

    /**
     * The cards action
     *
     * @param gameState current GameState
     */
    public void action(GameState gameState) {

    }

    /**
     * Get the name of the card
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Is the card playable on its own
     *
     * @return true if the card is playable, otherwise false
     */
    public boolean isPlayable() {
        return playable;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return playable == card.playable && Objects.equals(name, card.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, playable);
    }
}
