package main.explodingkittens.game;

import main.explodingkittens.exception.EKRequirmentException;
import main.explodingkittens.game.cardpack.ECardPacks;
import main.explodingkittens.game.cardpile.CardPile;
import main.explodingkittens.network.IClient;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private final List<Player> playingOrder;
    private final List<Player> allPlayers;
    private final CardPile drawPile;
    private final CardPile discardPile;
    private final List<ECardPacks> cardPacksUsed;
    private int nrTurns;
    private int nrNopes;
    private boolean drawCard;

    public GameState() {
        playingOrder = new ArrayList<>();
        allPlayers = new ArrayList<>();
        drawPile = new CardPile();
        discardPile = new CardPile();
        cardPacksUsed = new ArrayList<>();
        nrTurns = 1;
        nrNopes = 0;
        drawCard = false;
    }

    /**
     * Add a card pack used
     *
     * @param cardPack the card pack
     */
    public void addCardPack(ECardPacks cardPack) {
        cardPacksUsed.add(cardPack);
    }

    /**
     * Add a card pack used from string to game state
     *
     * @param cardPack the card pack to add as string
     */
    public void addCardPackFromString(String cardPack) {
        switch (ECardPacks.valueOf(cardPack)) {
            case ImplodingKittens -> {
                cardPacksUsed.add(ECardPacks.ImplodingKittens);
            }
        }
    }

    /**
     * Init all players in the game state from a list of clinets
     *
     * @param clients the list of clients
     */
    public void initPlayers(List<IClient> clients) {
        for (int i = 0; i < clients.size(); i++) {
            Player p = new Player(i, clients.get(i));
            allPlayers.add(p);
            playingOrder.add(p);
        }
    }

    /**
     * Get a player by id
     *
     * @param id the id
     * @return the player
     */
    public Player getPlayerById(int id) {
        for (Player p : allPlayers) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Get all players in the game
     *
     * @return all players
     */
    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    /**
     * Get the alive players and the order of playing
     *
     * @return the order of players
     */
    public List<Player> getPlayingOrder() {
        return playingOrder;
    }

    /**
     * Shift to the next player
     */
    public void nextPlayer() {
        Player wasFirst = playingOrder.get(0);
        playingOrder.remove(0);
        playingOrder.add(wasFirst);
    }

    /**
     * Remove a player from the game
     *
     * @param player the player
     * @return the removed player
     */
    public Player removePlayerFromGame(Player player) {
        discardPile.insert(player.getHand().getCards());
        return playingOrder.remove(playingOrder.indexOf(player));
    }

    /**
     * Get the draw pile
     *
     * @return the draw pile
     */
    public CardPile getDrawPile() {
        return drawPile;
    }

    /**
     * Get the discard pile
     *
     * @return the discard pile
     */
    public CardPile getDiscardPile() {
        return discardPile;
    }

    /**
     * Get what card packs is chosen to be used
     *
     * @return a list of card packs
     */
    public List<ECardPacks> getCardPacksUsed() {
        return cardPacksUsed;
    }

    /**
     * Get the number of turns the player have left
     *
     * @return the number of turns left
     */
    public int getNrTurns() {
        return nrTurns;
    }

    /**
     * Set the number of turns
     *
     * @param nrTurns number of turns to set to
     */
    public void setNrTurns(int nrTurns) {
        this.nrTurns = nrTurns;
    }

    /**
     * Add turns to the number of turns a player have left
     *
     * @param deltaTurns the turns to add or subtract
     */
    public void addNrTurns(int deltaTurns) {
        this.nrTurns += deltaTurns;
    }

    /**
     * Check if an action was noped or not
     *
     * @return true if noped, otherwise false
     */
    public boolean wasNoped() {
        return nrNopes % 2 == 1;
    }

    /**
     * Get number of nopes played before an action
     *
     * @return number of nopes played
     */
    public int getNrNopes() {
        return nrNopes;
    }

    /**
     * Set number of nopes
     *
     * @param nrNopes number of nopes
     */
    public void setNrNopes(int nrNopes) {
        this.nrNopes = nrNopes;
    }

    /**
     * Increase number of nopes played by 1
     */
    public void nopePlayed() {
        nrNopes++;
    }

    /**
     * Should player draw a card or not at end of turn
     *
     * @return if draw card or not
     */
    public boolean shouldDrawCard() {
        return drawCard;
    }

    /**
     * Set if to draw card or not
     *
     * @param drawCard
     */
    public void setDrawCard(boolean drawCard) {
        this.drawCard = drawCard;
    }
}
