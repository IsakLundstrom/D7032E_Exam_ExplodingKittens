package main.explodingkittens.game.card.kittencard;

import main.explodingkittens.game.card.Card;

/**
 * Inheriting cards are those who can explode i.e. kitten cards
 */
public abstract class KittenCard extends Card {

    public KittenCard() {
        super();
        this.playable = false;
    }
}
