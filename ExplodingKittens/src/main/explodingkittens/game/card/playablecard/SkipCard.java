package main.explodingkittens.game.card.playablecard;

import main.explodingkittens.game.GameState;
import main.explodingkittens.game.card.Card;

public class SkipCard extends PlayableCard {

    public SkipCard(){
        super();
        this.name = "Skip";
    }

    @Override
    public void action(GameState gameState) {
        gameState.addNrTurns(-1);
    }
}
