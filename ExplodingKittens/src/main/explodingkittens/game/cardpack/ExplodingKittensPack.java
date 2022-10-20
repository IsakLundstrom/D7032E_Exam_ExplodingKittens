package main.explodingkittens.game.cardpack;

import main.explodingkittens.game.card.catcard.CattermelonCard;
import main.explodingkittens.game.card.catcard.HairyPotatoCatCard;
import main.explodingkittens.game.card.catcard.OverweightBikiniCatCard;
import main.explodingkittens.game.card.catcard.RainbowRalphingCatCard;
import main.explodingkittens.game.card.catcard.TacoCatCard;
import main.explodingkittens.game.card.effectcard.unplayablecard.NopeCard;
import main.explodingkittens.game.card.effectcard.SeeTheFutureCard;
import main.explodingkittens.game.card.effectcard.ShuffleCard;
import main.explodingkittens.game.card.effectcard.SkipCard;
import main.explodingkittens.game.card.effectcard.AttackCard;
import main.explodingkittens.game.card.Card;
import main.explodingkittens.game.card.targetcard.FavorCard;

import java.util.ArrayList;
import java.util.List;

public class ExplodingKittensPack implements ICardPack {

    public List<Card> createCardPack() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 4; i++) cards.add(new AttackCard());
        for (int i = 0; i < 4; i++) cards.add(new FavorCard());
        for (int i = 0; i < 5; i++) cards.add(new NopeCard());
        for (int i = 0; i < 4; i++) cards.add(new ShuffleCard());
        for (int i = 0; i < 4; i++) cards.add(new SkipCard());
        for (int i = 0; i < 5; i++) cards.add(new SeeTheFutureCard());
        for (int i = 0; i < 4; i++) cards.add(new HairyPotatoCatCard());
        for (int i = 0; i < 4; i++) cards.add(new CattermelonCard());
        for (int i = 0; i < 4; i++) cards.add(new RainbowRalphingCatCard());
        for (int i = 0; i < 4; i++) cards.add(new TacoCatCard());
        for (int i = 0; i < 4; i++) cards.add(new OverweightBikiniCatCard());

        return cards;
    }
}
