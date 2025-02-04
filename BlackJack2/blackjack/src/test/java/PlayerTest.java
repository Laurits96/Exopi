import com.blackjack.helper.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player(0);
    }

    @Test
    public void testAddToHand() {
        Card card = new Card(Suit.HEARTS, Rank.ACE);
        player.addToHand(card);
        assertEquals(card, player.getHand(0).getCard(0));
    }

    @Test
    public void testClearHands() {
        Card card = new Card(Suit.SPADES, Rank.KING);
        player.addToHand(card);
        player.clearHands();
        assertTrue(player.getAllHands().size()==1);
        assertTrue(player.getHand(0).size()==0);
    }

    @Test
    public void testSplit(){
        Card card1 = new Card(Suit.SPADES, Rank.KING);
        Card card2 = new Card(Suit.HEARTS, Rank.KING);
        player.addToHand(card1);
        player.addToHand(card2);
        player.splitHand();
        assertTrue(player.getAllHands().size()==2);
        assertEquals(card1, player.getHand(0).getCard(0));
        assertEquals(card2, player.getHand(1).getCard(0));
    }
}