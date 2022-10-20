package main.explodingkittens.game.cardpack;

import main.explodingkittens.game.card.Card;

import java.util.ArrayList;
import java.util.List;

public class ImplodingKittensPack implements ICardPack {

    public List<Card> createCardPack() {
        List<Card> cards = new ArrayList<>();
        //for (int i = 0; i < 4; i++) cards.add(new AttackCard());
        return cards;
    }
}
