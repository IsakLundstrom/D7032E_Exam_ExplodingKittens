package main.explodingkittens.game.cardpack;

import main.explodingkittens.game.card.Card;

import java.util.List;
import java.util.stream.Stream;

/**
 * Enum for the available card packs
 */
public enum ECardPacks {

    /**
     * Base game card pack
     */
    ExplodingKittens(new ExplodingKittensPack().createCardPack(), 5),

    /**
     * Expansion 1 card pack
     */
    ImplodingKittens(new ImplodingKittensPack().createCardPack(), 1);

    private final List<Card> cards;
    private final int nrExtraPlayers;

    ECardPacks(List<Card> cards, int nrExtraPlayers) {
        this.cards = cards;
        this.nrExtraPlayers = nrExtraPlayers;
    }

    /**
     * Get the cards of the card pack
     *
     * @return the list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Get the number of (extra) players for this card pack
     *
     * @return number of players
     */
    public int getNrExtraPlayers() {
        return nrExtraPlayers;
    }

    @Override
    public String toString() {
        String s = "";
        for (String cardpack : Stream.of(ECardPacks.values()).map(ECardPacks::name).toList()) {
            s += cardpack + "\n";
        }
        return s;
    }
}
