package main.explodingkittens.game.card.playablecard.targetcard;

import main.explodingkittens.game.GameState;
import main.explodingkittens.game.Player;
import main.explodingkittens.game.card.playablecard.PlayableCard;

/**
 * Inheriting cards is card whose action needs a target
 */
public abstract class TargetCard extends PlayableCard {

    public TargetCard() {
        super();
    }

    /**
     * A new action for knowing who got targeted
     *
     * @param gameState current GameState
     * @param target    targeted Player
     */
    public abstract void action(GameState gameState, Player target);
}
