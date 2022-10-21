package main.explodingkittens.game.card.unplayablecard;

import main.explodingkittens.game.card.Card;

/**
 * Inheriting cards are those who can not be played on their own
 */
public abstract class UnplayableCard extends Card {

    public UnplayableCard() {
        super();
        this.playable = false;
    }
}
