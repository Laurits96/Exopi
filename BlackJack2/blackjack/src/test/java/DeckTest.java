import com.blackjack.helper.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class DeckTest {

    private Deck deck;
    private int noDecks;

    @BeforeEach
    void setUp(){
        noDecks = 4;
        deck = new Deck(noDecks);
        deck.InitializeDeck();
    }

    @Test
    void testDeckSize() {
        assertEquals(noDecks*52, deck.getCardDeck().size(), "A new deck should have 52 cards.");
    }

    @Test
    void testPickCard(){
        deck.pickCard();
        assertEquals(noDecks*52-1, deck.getCardDeck().size(), "Drawing a card should decrease deck size by 1.");
    }


}
