import src.*;
import java.util.*;
public class Model {
    private ArrayList<Player> players = new ArrayList<>();
    private Deck deck;

    public Model(){
        this.players.add(new Player(1));
        this.deck = new Deck(1);
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public Deck getDeck(){
        return this.deck;
    }
}
