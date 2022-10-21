package main.explodingkittens.game.cardpack;

import main.explodingkittens.game.card.Card;

import java.util.List;

/**
 * Interface to create card packs
 */
public interface ICardPack {

    /**
     * Create a card pack of Cards
     *
     * @return the cardPack
     */
    List<Card> createCardPack();
}
