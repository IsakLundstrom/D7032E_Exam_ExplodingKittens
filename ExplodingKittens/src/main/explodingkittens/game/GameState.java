package main.explodingkittens.game;

import main.explodingkittens.entity.PlayingEntity;
import main.explodingkittens.game.cardpack.ECardPacks;
import main.explodingkittens.game.cardpile.CardPile;
import main.explodingkittens.network.IClient;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private final List<PlayingEntity> playingOrder;
    private final CardPile drawPile;
    private final CardPile discardPile;
    private final List<ECardPacks> cardPacksUsed;
    private int nrTurns;
    private int nrNopes;

    public GameState() {
        playingOrder = new ArrayList<>();
        drawPile = new CardPile();
        discardPile = new CardPile();
        cardPacksUsed = new ArrayList<>();
        cardPacksUsed.add(ECardPacks.ExplodingKittens);
        nrTurns = 1;
        nrNopes = 0;
    }

    public void addCardPackToGame(String cardPack) {
        switch (ECardPacks.valueOf(cardPack)) {
            case ImplodingKittens -> {
                cardPacksUsed.add(ECardPacks.ImplodingKittens);
            }
        }
    }

    public void initPlayingEntities(List<IClient> clients) {
        for (int i = 0; i < clients.size(); i++) {
            playingOrder.add(new PlayingEntity(i, clients.get(i)));
        }
    }

    public List<PlayingEntity> getPlayingOrder() {
        return playingOrder;
    }

    public void nextPlayer() {
        PlayingEntity wasFirst = playingOrder.get(0);
        playingOrder.remove(0);
        playingOrder.add(wasFirst);
    }

    public CardPile getDrawPile() {
        return drawPile;
    }

    public CardPile getDiscardPile() {
        return discardPile;
    }

    public List<ECardPacks> getCardPacksUsed() {
        return cardPacksUsed;
    }

    public int getNrTurns() {
        return nrTurns;
    }

    public void addNrTurns(int nrExtraTurns) {
        nrExtraTurns += nrExtraTurns;
    }

    public boolean wasNoped() {
        return nrNopes % 2 == 1;
    }

    public int getNrNopes(){
        return nrNopes;
    }

    public void nopePlayed(){
        nrNopes++;
    }

    public int getMaxNrPlayers() {
        int nrPlayers = 0;
        for (ECardPacks pack : cardPacksUsed) {
            nrPlayers += pack.getNrExtraPlayers();
        }
        return nrPlayers;
    }
}
