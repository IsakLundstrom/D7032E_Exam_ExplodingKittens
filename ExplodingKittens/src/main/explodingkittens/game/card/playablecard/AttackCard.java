package main.explodingkittens.game.card.playablecard;

import main.explodingkittens.game.GameState;

public class AttackCard extends PlayableCard {

    public AttackCard() {
        super();
        this.name = "Attack";
    }

    @Override
    public void action(GameState gameState) {
        gameState.addNrTurns(gameState.getNrTurns() == 1 ? 1 : 2);
        gameState.nextPlayer();
    }
}
