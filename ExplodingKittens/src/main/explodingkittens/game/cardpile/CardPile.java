package main.explodingkittens.game.cardpile;

import main.explodingkittens.game.card.Card;

import java.util.*;

/**
 * Card pile class to keep track of many cards and different methods to support the game flow
 */
public class CardPile {

    private List<Card> cards;

    public CardPile() {
        cards = new ArrayList<>();
    }

    /**
     * Get the cards in the pile
     *
     * @return the cards as a list
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Insert a card in the pile
     *
     * @param card the card to insert
     */
    public void insert(Card card) {
        cards.add(card);
    }

    /**
     * Insert a card in the pile in a specific position
     *
     * @param card the card to insert
     * @param pos  the position to insert at
     */
    public void insert(Card card, int pos) {
        cards.add(pos, card);
    }

    /**
     * Insert a list of cards in the pile
     *
     * @param cards a list of cards
     */
    public void insert(List<Card> cards) {
        this.cards.addAll(cards);
    }

    /**
     * Extract a card from a position in the pile
     *
     * @param pos the postion to extract from
     * @return the extracted card or null if not found
     */
    public Card extract(int pos) {
        Card card = cards.get(pos);
        cards.remove(pos);
        return card;
    }

    /**
     * Extract a specific card from the pile
     *
     * @param card the card to extract
     * @return the extracted card or null if not found
     */
    public Card extract(Card card) {
        if (cards.contains(card)) {
            cards.remove(card);
            return card;
        }
        return null;
    }

    /**
     * Extract a specific card from the pile
     *
     * @param card the card string to extract
     * @return the extracted card or null if not found
     */
    public Card extract(String card) {
        if (toStringList().contains(card)) {
            Card tmp = cards.get(toStringList().indexOf(card));
            cards.remove(tmp);
            return tmp;
        }
        return null;
    }

    /**
     * Get a card from the pile
     *
     * @param card the card to find
     * @return the card or null if not in the pile
     */
    public Card get(Card card) {
        if (cards.contains(card)) {
            return card;
        }
        return null;
    }

    /**
     * Get a card from the pile
     *
     * @param card the card string to find
     * @return the card or null if not in the pile
     */
    public Card get(String card) {
        if (toStringList().contains(card)) {
            return cards.get(toStringList().indexOf(card));
        }
        return null;
    }

    /**
     * Shuffle the pile
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Shuffle the pile with a random to get a predictable shuffle
     *
     * @param random the random
     */
    public void shuffle(Random random) {
        Collections.shuffle(cards, random);
    }

    /**
     * Get the number of cards in the pile
     *
     * @return the number of cards in the pile
     */
    public int getSize() {
        return cards.size();
    }

    /**
     * Create a HashMap of the card pile to know how many duplicate cards there are in the pile
     *
     * @return a hash map of the cards in the pile and how many times the cards appear
     */
    public HashMap<Card, Integer> toHashMap() {
        HashMap<Card, Integer> map = new HashMap<>();
        for (Card card : cards) {
            if (!map.containsKey(card)) {
                map.put(card, 1);
                continue;
            }
            map.put(card, map.get(card) + 1);
        }
        return map;
    }

    /**
     * Create a String list of the card pile
     *
     * @return a string list representation of the card pile
     */
    public List<String> toStringList() {
        List<String> list = new ArrayList<>();
        for (Card card : cards) {
            list.add(card.getName());
        }
        return list;
    }

    /**
     * Sort the card pile based on the names of the cards
     */
    public void sort() {
        cards.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
    }
}
