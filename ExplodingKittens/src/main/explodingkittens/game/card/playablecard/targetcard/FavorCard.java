package main.explodingkittens.game.card.playablecard.targetcard;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.game.GameState;
import main.explodingkittens.game.Player;
import main.explodingkittens.game.card.Card;
import main.explodingkittens.util.option.Option;
import main.explodingkittens.util.option.Options;
import main.explodingkittens.util.message.MessageFactory;

import java.util.ArrayList;
import java.util.List;

public class FavorCard extends TargetCard {

    public FavorCard() {
        super();
        this.name = "Favor";
    }

    @Override
    public void action(GameState gameState, Player target) {
        List<Option> cardList = new ArrayList<>();
        for (Card card : target.getHand().getCards()) {
            Option o = new Option(card.getName());
            if (!cardList.contains(o))
                cardList.add(o);
        }
        try {
            target.getClient().sendMessage(MessageFactory.createMsg("Give a card to player " + gameState.getPlayingOrder().get(0).getId(), new Options(cardList)));
            String card = target.getClient().readMessage().toString();
            gameState.getPlayingOrder().get(0).getClient().sendMessage(MessageFactory.createMsg("You got a " + card));
            gameState.getPlayingOrder().get(0).getHand().insert(target.getHand().get(card));
        } catch (EKIOException e) {
            e.printStackTrace();
        }

    }
}
