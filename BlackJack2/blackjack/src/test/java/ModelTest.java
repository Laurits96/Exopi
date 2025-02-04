import com.blackjack.*;

import static org.junit.jupiter.api.Assertions.*;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

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
}
