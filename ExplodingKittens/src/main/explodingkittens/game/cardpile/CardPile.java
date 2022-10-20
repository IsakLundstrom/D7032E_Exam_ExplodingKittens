package main.explodingkittens.game.cardpile;

import main.explodingkittens.game.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CardPile {

    List<Card> cards;

    public CardPile() {
        cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void insert(Card card) {
        cards.add(card);
    }

    public void insert(Card card, int pos) {
        cards.add(pos, card);
    }

    public void insert(List<Card> cards) {
        this.cards.addAll(cards);
    }

    public Card extract(int pos) {
        Card card = cards.get(pos);
        cards.remove(pos);
        return card;
    }

    public Card extract(Card card) {
        if (cards.contains(card)){
            cards.remove(card);
            return card;
        }
        return null;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int getSize() {
        return cards.size();
    }

    public List<String> toStringList() {
        List<String> list = new ArrayList<>();
        for (Card card : cards) {
            list.add(card.getName());
        }
        return list;
    }

    public String toStringGrouped() {
        String s = "";
        HashMap<String, Integer> map = new HashMap<>();
        for (Card card : cards) {
            if (!map.containsKey(card.toString())) {
                map.put(card.toString(), 1);
                continue;
            }
            map.put(card.toString(), map.get(card.toString()) + 1);
        }

        for (String card : map.keySet()) {
            s += String.format("%s %s\n", map.get(card), card);
        }
        return s;
    }
}
