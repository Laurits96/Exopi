import com.blackjack.helper.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CardTest {

    @Test
    public void testCardCreation() {
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        assertEquals(Suit.HEARTS.getSymbol(), card.getSuit());
        assertEquals(Rank.ACE.getSymbol(), card.getRank());
    }

    @Test
    public void testGetSuit() {
        Card card = new Card(Suit.SPADES, Rank.KING);
        assertEquals("♠", card.getSuit());
    }

    @Test
    public void testGetRank() {
        Card card = new Card(Suit.DIAMONDS, Rank.TEN);
        assertEquals("10", card.getRank());
    }

    @Test
    public void testGetValue() {
        Card card = new Card(Suit.CLUBS, Rank.JACK);
        assertEquals(10, card.getValue());
    }

    @Test
    public void testToString() {
        Card card = new Card(Suit.HEARTS, Rank.QUEEN);
        assertEquals("Q♥", card.toString());
    }
}