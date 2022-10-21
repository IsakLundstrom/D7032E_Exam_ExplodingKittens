package main.explodingkittens.game.card.playablecard;

import main.explodingkittens.game.card.Card;

/**
 * Inheriting cards are those who can be played on their own
 */
public abstract class PlayableCard extends Card {

    public PlayableCard() {
        super();
        this.playable = true;
    }
}
