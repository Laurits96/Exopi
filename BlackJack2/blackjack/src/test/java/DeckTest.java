import com.blackjack.helper.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class DeckTest {

    private Deck deck;
    private int noDecks;

    @BeforeEach
    void setUp(){
        this.noDecks = 4;
        this.deck = new Deck(noDecks);

    }

    @Test
    void testDeckSize() {
        assertEquals(noDecks*52, deck.getCardDeck().size(), "A new deck should have 52 cards.");
    }

    @Test
    void testPickCard(){
        for (int i = 0; i < noDecks*52;i++){
            Card removed = deck.pickCard();
            assertEquals(noDecks*52-(i+1), deck.getCardDeck().size(), "Drawing a card should decrease deck size by 1.");
            deck.getCardDeck().stream().forEach(card -> {
                assertTrue(!(removed.equals(card)));
            });
        }
        assertEquals(0, deck.getCardDeck().size(), "All cards removed");
    }


}
