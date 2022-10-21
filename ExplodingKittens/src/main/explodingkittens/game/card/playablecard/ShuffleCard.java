package main.explodingkittens.game.card.playablecard;

import main.explodingkittens.game.GameState;
import main.explodingkittens.game.card.Card;

public class ShuffleCard extends PlayableCard {

    public ShuffleCard(){
        super();
        this.name = "Shuffle";
    }

    @Override
    public void action(GameState gameState) {
        gameState.getDrawPile().shuffle();
    }
}
