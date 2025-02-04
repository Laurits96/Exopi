package src.test.java;

import src.main.java.*;
import src.main.java.helper.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    private Model model;
    private Player player;
    private Dealer dealer;

    @BeforeEach
    public void setUp() {
        model = new Model();
        dealer = new Dealer();
        model.getPlayers().add(player);
    }

    @Test
    public void testDealerStopped_PlayerWinsWithHigherHandSum() {
        // Set up dealer's hand
        dealer.addToHand(new Card(Suit.HEARTS, Rank.TEN));
        dealer.addToHand(new Card(Suit.HEARTS, Rank.SEVEN));

        // Set up player's hand
        player.addToHand(new Card(Suit.CLUBS, Rank.TEN));
        player.addToHand(new Card(Suit.CLUBS, Rank.EIGHT));

        StringBuilder result = new StringBuilder();
        String outcome = model.dealerStopped(result);

        assertTrue(outcome.contains("Player 1 won"));
    }

    @Test
    public void testDealerStopped_PlayerWinsWithBlackJack() {
        // Set up dealer's hand
        dealer.addToHand(new Card(Suit.HEARTS, Rank.TEN));
        dealer.addToHand(new Card(Suit.HEARTS, Rank.SEVEN));

        // Set up player's hand
        player.addToHand(new Card(Suit.CLUBS, Rank.ACE));
        player.addToHand(new Card(Suit.CLUBS, Rank.TEN));

        StringBuilder result = new StringBuilder();
        String outcome = model.dealerStopped(result);

        assertTrue(outcome.contains("Player 1 won"));
    }

    @Test
    public void testDealerStopped_PlayerTiesWithDealer() {
        // Set up dealer's hand
        dealer.addToHand(new Card(Suit.HEARTS, Rank.TEN));
        dealer.addToHand(new Card(Suit.HEARTS, Rank.SEVEN));

        // Set up player's hand
        player.addToHand(new Card(Suit.CLUBS, Rank.TEN));
        player.addToHand(new Card(Suit.CLUBS, Rank.SEVEN));

        StringBuilder result = new StringBuilder();
        String outcome = model.dealerStopped(result);

        assertTrue(outcome.contains("Player 1 tied"));
    }

    @Test
    public void testDealerStopped_PlayerLoses() {
        // Set up dealer's hand
        dealer.addToHand(new Card(Suit.HEARTS, Rank.TEN));
        dealer.addToHand(new Card(Suit.HEARTS, Rank.SEVEN));

        // Set up player's hand
        player.addToHand(new Card(Suit.CLUBS, Rank.TEN));
        player.addToHand(new Card(Suit.CLUBS, Rank.SIX));

        StringBuilder result = new StringBuilder();
        String outcome = model.dealerStopped(result);

        assertFalse(outcome.contains("Player 1 won"));
    }

    @Test
    public void testDealerStopped_PlayerForfeits() {
        // Set up dealer's hand
        dealer.addToHand(new Card(Suit.HEARTS, Rank.TEN));
        dealer.addToHand(new Card(Suit.HEARTS, Rank.SEVEN));

        // Set up player's hand
        player.addToHand(new Card(Suit.CLUBS, Rank.TEN));
        player.addToHand(new Card(Suit.CLUBS, Rank.EIGHT));
        player.setForfeited(true);

        StringBuilder result = new StringBuilder();
        String outcome = model.dealerStopped(result);

        assertFalse(outcome.contains("Player 1 won"));
    }
}
