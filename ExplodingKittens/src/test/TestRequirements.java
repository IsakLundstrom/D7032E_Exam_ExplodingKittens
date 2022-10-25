package test;

import static org.junit.Assert.*;

import main.explodingkittens.exception.EKException;
import main.explodingkittens.game.*;
import main.explodingkittens.game.card.Card;
import main.explodingkittens.game.card.unplayablecard.kittencard.ExplodingKittenCard;
import main.explodingkittens.game.card.playablecard.AttackCard;
import main.explodingkittens.game.card.playablecard.SeeTheFutureCard;
import main.explodingkittens.game.card.playablecard.ShuffleCard;
import main.explodingkittens.game.card.playablecard.SkipCard;
import main.explodingkittens.game.card.playablecard.targetcard.FavorCard;
import main.explodingkittens.game.card.unplayablecard.DefuseCard;
import main.explodingkittens.game.card.unplayablecard.NopeCard;
import main.explodingkittens.game.card.unplayablecard.catcard.TacoCatCard;
import main.explodingkittens.game.cardpack.ECardPacks;
import main.explodingkittens.game.cardpile.CardPile;
import main.explodingkittens.network.TestClient;
import main.explodingkittens.network.IClient;
import main.explodingkittens.util.message.MessageFactory;
import org.junit.*;

import java.util.List;
import java.util.Random;

public class TestRequirements {

    GameState gameState;
    GameLogic gameLogic;

    @Before
    public void init() {
        gameState = new GameState();
        gameLogic = new GameLogic();
    }

    /*
     * 1. There can be between 2 and 5 players
     */

    @Test(expected = EKException.class)
    public void r1_LessThanMinAllowedPlayers() throws EKException {
        List<IClient> clients = List.of(new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
    }

    @Test
    public void r1_ExactMinAllowedPlayers() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        assertTrue(true);
    }

    @Test
    public void r1_ExactMaxAllowedPlayers() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient(), new TestClient(), new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        assertTrue(true);
    }

    @Test(expected = EKException.class)
    public void r1_MoreThanMaxAllowedPlayers() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient(), new TestClient(), new TestClient(), new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
    }

    /*
     * 2. Each player should be given 1 Defuse card
     */

    @Test
    public void r2() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();
        for (Player p : gameState.getAllPlayers()) {
            if (!p.getHand().getCards().contains(new DefuseCard())) {
                fail();
            }
        }
        assertTrue(true);
    }

    /*
     * 3. The deck should consist of the following cards before the first hands are dealt:
     *  a. 2 Defuse cards for 2-4 players, 1 Defuse card for 5 players.
     *  b. 4 Attack cards
     *  c. 4 Favor cards
     *  d. 5 Nope cards
     *  e. 4 Shuffle cards
     *  f. 4 Skip cards
     *  g. 5 SeeTheFuture cards
     *  h. 4 HairyPotatoCat cards
     *  i. 4 Cattermelon cards
     *  j. 4 RainbowRalphingCat cards
     *  k. 4 TacoCat cards
     *  l. 4 OverweightBikiniCat cards
     */

    @Test
    public void r3_TwoPlayers() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();
        //Get the draw pile and restore it before dealt cards
        CardPile drawPile = gameState.getDrawPile();
        for (Player p : gameState.getAllPlayers()) {
            p.getHand().extract(new DefuseCard().getName());
            drawPile.getCards().addAll(p.getHand().getCards());
            drawPile.extract(new ExplodingKittenCard().getName());
        }
        //Get base game cards
        CardPile comparePile = new CardPile();
        comparePile.insert(ECardPacks.ExplodingKittens.getCards());
        comparePile.insert(List.of(new DefuseCard(), new DefuseCard())); // two defuses
        comparePile.sort();
        drawPile.sort();
        assertEquals(drawPile.getCards(), comparePile.getCards());
    }

    @Test
    public void r3_FourPlayers() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient(), new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();
        //Get the draw pile and restore it before dealt cards
        CardPile drawPile = gameState.getDrawPile();
        for (Player p : gameState.getAllPlayers()) {
            p.getHand().extract(new DefuseCard().getName());
            drawPile.getCards().addAll(p.getHand().getCards());
            drawPile.extract(new ExplodingKittenCard().getName());
        }
        //Get base game cards
        CardPile comparePile = new CardPile();
        comparePile.insert(ECardPacks.ExplodingKittens.getCards());
        comparePile.insert(List.of(new DefuseCard(), new DefuseCard())); // two defuses
        comparePile.sort();
        drawPile.sort();
        assertEquals(drawPile.getCards(), comparePile.getCards());
    }

    @Test
    public void r3_FivePlayers() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient(), new TestClient(), new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();
        //Get the draw pile and restore it before dealt cards
        CardPile drawPile = gameState.getDrawPile();
        for (Player p : gameState.getAllPlayers()) {
            p.getHand().extract(new DefuseCard().getName());
            drawPile.getCards().addAll(p.getHand().getCards());
            drawPile.extract(new ExplodingKittenCard().getName());
        }
        //Get base game cards
        CardPile comparePile = new CardPile();
        comparePile.insert(ECardPacks.ExplodingKittens.getCards());
        comparePile.insert(new DefuseCard()); // one defuse
        comparePile.sort();
        drawPile.sort();
        assertEquals(drawPile.getCards(), comparePile.getCards());
    }

    /*
     * 4. Shuffle the deck before the first hands are dealt.
     */
    @Test
    public void r4() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        gameLogic.giveOneDefuse(gameState);
        gameLogic.initDrawPile(gameState);
        Random random = new Random(1);
        gameState.getDrawPile().shuffle(random);

        CardPile comparePile = new CardPile();
        comparePile.insert(ECardPacks.ExplodingKittens.getCards());
        comparePile.insert(new DefuseCard());
        comparePile.insert(new DefuseCard());
        comparePile.shuffle(random);
        assertNotSame(gameState.getDrawPile().getCards(), comparePile.getCards());
    }


    /*
     * 5. Deal 7 cards from the deck to each player.
     */

    @Test
    public void r5() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient(), new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        for (Player p : gameState.getAllPlayers()) {
            if (p.getHand().getSize() != 8) {
                fail();
            }
        }
        assertTrue(true);
    }

    /*
     * 6. Insert enough ExplodingKitten cards into the deck so there is 1 fewer than the number of players
     */

    @Test
    public void r6() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient(), new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        for (int i = 0; i < gameState.getAllPlayers().size() - 1; i++) {
            if (gameState.getDrawPile().extract(new ExplodingKittenCard()) == null) {
                fail();
            }
        }
        assertTrue(true);
    }

    /*
     * 7. Shuffle the deck before the game starts
     */

    @Test
    public void r7() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        gameLogic.giveOneDefuse(gameState);
        gameLogic.initDrawPile(gameState);
        Random random = new Random(1);
        gameState.getDrawPile().shuffle(random);
        gameLogic.insertKittensDrawPile(gameState);
        gameState.getDrawPile().shuffle(random);

        CardPile comparePile = new CardPile();
        comparePile.insert(ECardPacks.ExplodingKittens.getCards());
        comparePile.insert(new DefuseCard());
        comparePile.insert(new DefuseCard());
        comparePile.insert(new ExplodingKittenCard());
        comparePile.shuffle(random);

        assertNotSame(gameState.getDrawPile().getCards(), comparePile.getCards());
    }

    /*
     * 8. Random player goes first
     */

    @Test
    public void r8() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient(), new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        gameLogic.giveOneDefuse(gameState);
        gameLogic.initDrawPile(gameState);
        Random random = new Random(1);
        gameState.getDrawPile().shuffle(random);
        gameLogic.insertKittensDrawPile(gameState);
        gameState.getDrawPile().shuffle(random);
        gameLogic.randomPlayerGoesFirst(gameState, random);

        assertNotSame(gameState.getAllPlayers(), gameState.getPlayingOrder());
    }

    /*
     * 9. On a player’s turn the player can either Pass or Play one or several cards
     *  a. Pass will end the turn and the player will draw a card (hoping it is not an ExplodingKitten)
     *  b. A player can play as many or as few cards as desired upon one’s turn
     */

    @Test
    public void r9_Pass() throws EKException, InterruptedException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        TestClient t1 = (TestClient) p1.getClient();

        p1.setHand(new CardPile());
        p2.setHand(new CardPile());
        gameState.getDrawPile().insert(new AttackCard(), 0);
        t1.setReadMsg(MessageFactory.createMsg(GameConstants.PASS));
        game.startTurn();
        game.endTurn();

        assertEquals(p1.getHand().getCards().get(0), new AttackCard());
        assertEquals(p2, gameState.getPlayingOrder().get(0));
    }

    @Test
    public void r9_Play() throws EKException, InterruptedException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        TestClient t1 = (TestClient) p1.getClient();

        p1.setHand(new CardPile());
        p2.setHand(new CardPile());
        p1.getHand().insert(new ShuffleCard());
        p1.getHand().insert(new ShuffleCard());
        gameState.getDrawPile().insert(new AttackCard(), 0);
        t1.setReadMsg(MessageFactory.createMsg("Shuffle"));
        game.startTurn();
        game.endTurn();
        t1.setReadMsg(MessageFactory.createMsg("Shuffle"));
        game.startTurn();
        game.endTurn();

        assertEquals(p1, gameState.getPlayingOrder().get(0));
    }

    /*
     * 10. If an ExplodingKitten card is drawn the Player has to play a Defuse card if one is on hand or explode.
     *  a. A defused ExplodingKitten card is inserted back into the deck at the desired location by the surviving Player
     *  b. An exploded Player discards the cards on hand, including the ExplodingKitten card, and may not take additional turns.
     */

    @Test
    public void r10_hasDefuse() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        p1.setHand(new CardPile());
        p1.getHand().insert(new DefuseCard());
        TestClient t = (TestClient) p1.getClient();
        t.setReadMsg(MessageFactory.createMsg("Place at 0"));
        gameState.getDrawPile().insert(new ExplodingKittenCard(), 0);
        gameLogic.drawCard(gameState, p1);

        assertEquals(t.getSendMsg(), MessageFactory.createMsg("You defused the kitten!"));
    }

    @Test
    public void r10_hasNoDefuse() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        p1.setHand(new CardPile());
        TestClient t = (TestClient) p1.getClient();
        gameState.getDrawPile().insert(new ExplodingKittenCard(), 0);
        gameLogic.drawCard(gameState, p1);

        assertEquals(t.getSendMsg(), MessageFactory.createMsg("You exploded! Better luck next time!"));
    }

    /*
     * 11. Playing a card
     *  a. Playing an Attack card will end the turn without drawing a card
     *   i. The next Player takes 2 turns in a row unless that Player plays another Attack card, in which case the
     *      consecutive player has to take an additional 2 turns (e.g. 4 turns, then 6, etc.)
     *  b. Playing a Favor card forces another player to give you 1 card of their choice to you.
     *  c. Playing a Shuffle card will shuffle the deck.
     *  d. Playing a Skip card will end your turn without drawing a card (only 1 tun in case of taking multiple turns)
     *  e. Playing a SeeTheFuture card will allow the Player to view the top 3 cards in the deck.
     *  f. Playing two cards of any kind will allow the Player to steal random card from another player
     *  g. Playing three cards of any kind will allow the Player to name a card to receive from another player (if available)
     */

    @Test
    public void r11_AttackOnes() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        p1.getHand().insert(new AttackCard());
        gameLogic.playSingleCard(gameState, p1, "Attack", null);

        assertEquals(gameState.getPlayingOrder().get(0), p2);
        assertEquals(gameState.getNrTurns(), 2);
    }

    @Test
    public void r11_AttackTwice() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        p1.getHand().insert(new AttackCard());
        p2.getHand().insert(new AttackCard());
        gameLogic.playSingleCard(gameState, p1, "Attack", null);
        gameLogic.playSingleCard(gameState, p2, "Attack", null);

        assertEquals(gameState.getPlayingOrder().get(0), p1);
        assertEquals(gameState.getNrTurns(), 4);
    }

    @Test
    public void r11_Favor() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        p1.getHand().insert(new FavorCard());
        p2.getHand().insert(new AttackCard());

        TestClient t2 = (TestClient) p2.getClient();
        t2.setReadMsg(MessageFactory.createMsg("Attack"));

        gameLogic.playSingleCard(gameState, p1, "Favor", p2);

        assertEquals(p1.getHand().get("Attack"), new AttackCard());
    }

    @Test
    public void r11_Shuffle() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        CardPile drawPileBefore = new CardPile();
        drawPileBefore.insert(gameState.getDrawPile().getCards());
        Player p1 = gameState.getPlayingOrder().get(0);
        p1.getHand().insert(new ShuffleCard());
        gameLogic.playSingleCard(gameState, p1, "Shuffle", null);

        assertNotSame(gameState.getDrawPile(), drawPileBefore);
    }

    @Test
    public void r11_Skip() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        CardPile handBefore = new CardPile();
        handBefore.insert(p1.getHand().getCards());
        p1.getHand().insert(new SkipCard());
        gameLogic.playSingleCard(gameState, p1, "Skip", null);
        p1.getHand().sort();

        assertEquals(p1.getHand().getCards(), handBefore.getCards());
    }

    @Test
    public void r11_SeeTheFuture() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        p1.getHand().insert(new SeeTheFutureCard());
        TestClient t1 = (TestClient) p1.getClient();
        String s = "";
        for (int i = 0; i < (Math.min(gameState.getDrawPile().getSize(), 3)); i++) {
            s += gameState.getDrawPile().getCards().get(i) + " ";
        }
        gameLogic.playSingleCard(gameState, p1, "SeeTheFuture", null);

        assertEquals("The top three cards in draw pile: " + s, t1.getSendMsg().getMessage());
    }

    @Test
    public void r11_TwoCombo() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        p1.setHand(new CardPile());
        p2.setHand(new CardPile());
        p1.getHand().insert(new TacoCatCard());
        p1.getHand().insert(new TacoCatCard());
        p2.getHand().insert(new AttackCard());
        Card card = gameLogic.playTwoCombo(gameState, p1, "Two TacoCard", p2);

        assertEquals(card, new AttackCard());
    }

    @Test
    public void r11_ThreeComboCardIsAvailable() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        p1.setHand(new CardPile());
        p2.setHand(new CardPile());
        p1.getHand().insert(new TacoCatCard());
        p1.getHand().insert(new TacoCatCard());
        p1.getHand().insert(new TacoCatCard());
        p2.getHand().insert(new AttackCard());
        Card card = gameLogic.playThreeCombo(gameState, p1, "Three TacoCard", p2, "Attack");

        assertEquals(card, new AttackCard());
    }

    @Test
    public void r11_ThreeComboCardNotAvailable() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        p1.getHand().insert(new TacoCatCard());
        p1.getHand().insert(new TacoCatCard());
        p1.getHand().insert(new TacoCatCard());
        p2.setHand(new CardPile());
        Card card = gameLogic.playThreeCombo(gameState, p1, "Three TacoCard", p2, "Attack");

        assertNull(card);
    }

    /*
     * 12. A Nope card can be played within 5 seconds of any card(s) defined in rule 11 and 12 to stop their action.
     *  a. A Nope card played on top of another Nope alternates its meaning (between Nope and Yup)
     */

    @Test
    public void r12_ZeroNopes() throws EKException, InterruptedException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);

        p1.setHand(new CardPile());
        p2.setHand(new CardPile());

        gameLogic.handleNopes(gameState, "Nope msg");

        assertFalse(gameState.wasNoped());
    }

    @Test
    public void r12_OneNope() throws EKException, InterruptedException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        TestClient t1 = (TestClient) p1.getClient();

        p1.setHand(new CardPile());
        p2.setHand(new CardPile());
        p1.getHand().insert(new NopeCard());
        t1.setReadMsg(MessageFactory.createMsg(""));

        gameLogic.handleNopes(gameState, "Nope msg");

        assertTrue(gameState.wasNoped());
    }

    @Test
    public void r12_TwoNope() throws EKException, InterruptedException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        TestClient t1 = (TestClient) p1.getClient();
        TestClient t2 = (TestClient) p2.getClient();

        p1.setHand(new CardPile());
        p2.setHand(new CardPile());
        p1.getHand().insert(new NopeCard());
        p2.getHand().insert(new NopeCard());
        t1.setReadMsg(MessageFactory.createMsg(""));
        t2.setReadMsg(MessageFactory.createMsg(""));

        gameLogic.handleNopes(gameState, "Nope msg");

        assertFalse(gameState.wasNoped());
    }

    /*
     * 13. When all but one players have exploded the remaining player is announced as the winner to all players.
     */

    @Test
    public void r13() throws EKException {
        List<IClient> clients = List.of(new TestClient(), new TestClient());
        gameState.addCardPack(ECardPacks.ExplodingKittens);
        gameState.initPlayers(clients);
        Game game = new Game(gameState);
        game.setupGame();

        Player p1 = gameState.getPlayingOrder().get(0);
        Player p2 = gameState.getPlayingOrder().get(1);
        TestClient t1 = (TestClient) p1.getClient();
        TestClient t2 = (TestClient) p2.getClient();

        p1.setHand(new CardPile());
        gameState.getDrawPile().insert(new ExplodingKittenCard(), 0);
        gameState.setDrawCard(true);
        game.endTurn();
        if (gameLogic.checkGameOver(gameState)) {
            gameLogic.sendGameOver(gameState);
        }

        assertEquals(t1.getSendMsg(), MessageFactory.createMsg("Player " + p2.getId() + " won the game!"));
        assertEquals(t2.getSendMsg(), MessageFactory.createMsg("You win!"));
    }

}
