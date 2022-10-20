package main.explodingkittens.game.card.effectcard;

import main.explodingkittens.game.GameState;
import main.explodingkittens.game.card.Card;

public class AttackCard extends Card {

    public AttackCard(){
        super();
        this.name = "Attack";
    }

    @Override
    public void playAction(GameState gameState) {
        gameState.addNrTurns(gameState.getNrTurns() == 1 ? 1 : 2); // if first attack add 1 otherwise add 2

    }
}
