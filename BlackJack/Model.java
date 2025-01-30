import src.*;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
public class Model{
    private ArrayList<Player> players = new ArrayList<>();
    private Dealer dealer;
    private Deck deck;
    private PropertyChangeSupport support;

    public Model(){
        this.players.add(new Player(1));
        this.dealer = new Dealer();
        this.deck = new Deck(1);
        this.support = new PropertyChangeSupport(this);
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public Deck getDeck(){
        return this.deck;
    }
    public Dealer getDealer(){
        return this.dealer;
    }

    public Card pickCard(){
        int picked = new Random().nextInt(this.deck.getCardDeck().size());
        Card dealt = this.deck.getCardDeck().get(picked);
        this.deck.getCardDeck().remove(picked);
        return dealt;
    }

    public void dealCard(){
        this.deck.InitializeDeck();
        this.dealer.getHand().clearHand();
        this.players.stream().forEach(player -> player.getHand().clearHand());
        this.dealer.addToHand(pickCard());
        support.firePropertyChange("state", null, null);
        this.players.stream().forEach(player -> {
            player.addToHand(pickCard());
            player.addToHand(pickCard());
        });
        support.firePropertyChange("state", null, null);
    }  

   public void dealerHit(){
        while (this.dealer.getHand().sumHand() < 17){
            this.dealer.addToHand(pickCard());
        }
        support.firePropertyChange("state", null, null);
        if (this.dealer.getHand().sumHand() > 21){
            support.firePropertyChange("Player won", null, null);
        }
        else{
            this.players.stream().forEach(player -> {
                if (player.getHand().sumHand() > this.dealer.getHand().sumHand() && player.getHand().sumHand() <= 21){
                    support.firePropertyChange("Player won", null, null);
                }
                else{
                    support.firePropertyChange("Player lost", null, null);
                }
            });
        }
    }
    
    public void stand(){
        dealerHit();
        support.firePropertyChange("state", null, null);
    }

    public void hit(Player player){
        player.addToHand(pickCard());
        support.firePropertyChange("state", null, null);
        if (player.getHand().sumHand() > 21){
            support.firePropertyChange("Player lost", null, null);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
    
    public void updateState() {
        support.firePropertyChange("state", null, null);
    }
}