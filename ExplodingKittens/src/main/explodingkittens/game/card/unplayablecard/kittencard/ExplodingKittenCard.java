package main.explodingkittens.game.card.unplayablecard.kittencard;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.game.Player;
import main.explodingkittens.game.GameState;
import main.explodingkittens.game.card.unplayablecard.DefuseCard;
import main.explodingkittens.util.message.MessageFactory;
import main.explodingkittens.util.option.Option;
import main.explodingkittens.util.option.Options;
import main.explodingkittens.util.option.RangeOption;

import java.util.List;

public class ExplodingKittenCard extends KittenCard {

    public ExplodingKittenCard() {
        super();
        this.name = "ExplodingKitten";
    }

    @Override
    public void action(GameState gameState) {
        Player currentPlayer = gameState.getPlayingOrder().get(0);
        if (currentPlayer.getHand().getCards().contains(new DefuseCard())) {
            gameState.getDrawPile().insert(currentPlayer.getHand().extract(new DefuseCard()));
            Option option = new RangeOption("Place at", "pos", 0, gameState.getDrawPile().getSize());
            Options options = new Options(List.of(option));
            try {
                gameState.getPlayingOrder().get(0).getClient().sendMessage(MessageFactory.createMsg("Place the " + getName() + " in the draw pile range " + 0 + ".." + gameState.getDrawPile().getSize(), options));
                int pos = Integer.parseInt(currentPlayer.getClient().readMessage().toString().split(" ")[2]);
                gameState.getDrawPile().insert(new ExplodingKittenCard(), pos);
            } catch (EKIOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        gameState.removePlayerFromGame(gameState.getPlayingOrder().get(0));
        gameState.setNrTurns(1);
    }
}
