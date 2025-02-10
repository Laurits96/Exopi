import com.blackjack.helper.*;
import com.blackjack.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class ModelTest {
    private Model model;

    @BeforeEach
    void setUp(){
        model = new Model();
    }

    @Test
    void testAddPlayer(){
        model.addPlayer();
        assertNotEquals(model.getPlayers().get(0).getID(), model.getPlayers().get(1).getID());
        assertEquals(model.getPlayers().get(0).getID(), model.getPlayers().get(1).getID()-1);
    }

    @Test
    void testDealCard(){
        model.dealCard();
        assertTrue(model.getDealer().getHand(0).size()==1);
        model.getPlayers().stream().forEach(player -> assertTrue(player.getHand(0).size()==2));
    }

    @Test
    void testHit(){
        model.dealCard();
        assertTrue(model.getPlayers().get(model.getPlayerTurn()).getHand(0).size()==2);
        model.hit(model.getPlayers().get(model.getPlayerTurn()));
        assertTrue(model.getPlayers().get(model.getPlayerTurn()).getHand(0).size()==3);
    }

    @Test
    void testDealerBust(){
        model.getDealer().addToHand(new Card(Suit.DIAMONDS, Rank.JACK));
        model.getDealer().addToHand(new Card(Suit.DIAMONDS, Rank.SIX));
        model.getDealer().addToHand(new Card(Suit.DIAMONDS, Rank.NINE));
        model.getPlayers().get(model.getPlayerTurn()).addToHand(new Card(Suit.SPADES, Rank.FIVE));
        model.getPlayers().get(model.getPlayerTurn()).addToHand(new Card(Suit.HEARTS, Rank.JACK));
        String output = model.findWinners();
        assertTrue(output.contains("Dealer busts"));
        assertTrue(output.contains("Player 1 won"));
    }

    @Test
    void testDealerBlackJack(){
        model.getDealer().addToHand(new Card(Suit.DIAMONDS, Rank.JACK));
        model.getDealer().addToHand(new Card(Suit.HEARTS, Rank.ACE));
        model.getPlayers().get(0).addToHand(new Card(Suit.SPADES, Rank.FIVE));
        model.getPlayers().get(0).addToHand(new Card(Suit.HEARTS, Rank.JACK));
        model.addPlayer();
        model.getPlayers().get(1).addToHand(new Card(Suit.HEARTS, Rank.QUEEN));
        model.getPlayers().get(1).addToHand(new Card(Suit.SPADES, Rank.ACE));
        String output = model.findWinners();
        assertTrue(output.contains("Dealer has BlackJack"));
        assertTrue(output.contains("Player 2 tied with"));
        assertFalse(output.contains("Player 1 tied with"));
    }

    @Test
    void testDealerStopped(){
        model.getDealer().addToHand(new Card(Suit.DIAMONDS, Rank.JACK));
        model.getDealer().addToHand(new Card(Suit.HEARTS, Rank.SEVEN));
        model.getPlayers().get(0).addToHand(new Card(Suit.SPADES, Rank.FIVE));
        model.getPlayers().get(0).addToHand(new Card(Suit.HEARTS, Rank.JACK));
        model.addPlayer();
        model.getPlayers().get(1).addToHand(new Card(Suit.HEARTS, Rank.QUEEN));
        model.getPlayers().get(1).addToHand(new Card(Suit.SPADES, Rank.NINE));
        String output = model.findWinners();
        assertTrue(output.contains("Dealer stopped at"));
        assertTrue(output.contains("Player 2 won"));
        assertFalse(output.contains("Player 1 won"));
    }
}
