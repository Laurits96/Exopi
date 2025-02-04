import com.blackjack.helper.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class HandTest {

    private Hand hand;

    @BeforeEach
    void setUp(){
        hand = new Hand();
    }

    @Test
    void testIsSameRank(){
        hand.add(new Card(Suit.DIAMONDS, Rank.TEN));
        hand.add(new Card(Suit.SPADES, Rank.TEN));
        assertTrue(hand.isSameRank(), "Hand contains two cards of same rank");
    }

    @Test
    void testAdd(){
        Card card = new Card(Suit.DIAMONDS, Rank.FIVE);
        hand.add(card);
        assertEquals(hand.getCard(0), card);
    }
    
}
