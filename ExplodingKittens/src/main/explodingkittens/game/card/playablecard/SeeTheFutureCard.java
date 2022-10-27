package main.explodingkittens.game.card.playablecard;

import main.explodingkittens.exception.EKIOException;
import main.explodingkittens.game.GameState;
import main.explodingkittens.util.message.MessageFactory;

public class SeeTheFutureCard extends PlayableCard {

    public SeeTheFutureCard(){
        super();
        this.name = "SeeTheFuture";
    }

    @Override
    public void action(GameState gameState) {
        String s = "";
        for (int i = 0; i < (Math.min(gameState.getDrawPile().getSize(), 3)); i++) {
            s += gameState.getDrawPile().getCards().get(i) + " ";
        }
        try {
            gameState.getPlayingOrder().get(0).getClient().sendMessage(MessageFactory.createMsg("The top three cards in draw pile: " + s));
        } catch (EKIOException e) {
            e.printStackTrace();
        }
    }
}
