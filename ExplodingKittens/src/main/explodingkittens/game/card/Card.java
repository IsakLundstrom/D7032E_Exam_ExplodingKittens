package main.explodingkittens.game.card;

import main.explodingkittens.game.GameState;

public abstract class Card {

    protected String name;
    protected boolean playable;
    protected boolean nopeable;

    public Card() {
        this.name = "Card name placeholder";
        this.playable = true;
        this.nopeable = true;
    }

    public void drawAction(GameState gameState){

    };

    public void playAction(GameState gameState){

    };

    public String getName() {
        return name;
    }

    public boolean isPlayable() {
        return playable;
    }

    public boolean isNopeable() {
        return nopeable;
    }

    @Override
    public String toString() {
        return name;
    }
}
