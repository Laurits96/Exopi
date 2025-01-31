import src.*;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
public class Model{
    private ArrayList<Player> players = new ArrayList<>();
    private int playersTurn;
    private int playersNumberOfHands;
    private Dealer dealer;
    private Deck deck;
    private PropertyChangeSupport support;

    public Model(){
        this.players.add(new Player(1));
        this.playersTurn = 0;
        this.playersNumberOfHands = 0;
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

    public void addPlayer(){
        this.players.add(new Player(this.players.size() + 1));
        support.firePropertyChange("newPlayer", null, this.players.get(this.players.size()-1).getID());
    }

    public int getPlayerTurn(){
        return this.playersTurn;
    }

    public void nextPlayer(){
        this.playersTurn++;
        if (this.playersTurn == this.players.size()){
            this.dealerHit();

        }else {
            
            this.playersNumberOfHands = 0;
            support.firePropertyChange("nextTurn", null, null);
            if (this.players.get(this.playersTurn).getHand(0).isSameRank()){
                support.firePropertyChange("split", null, null);
            }
        }
    }    

    public void dealCard(){
        this.dealer.addToHand(this.deck.pickCard());
        this.players.stream().forEach(player -> {
            player.addToHand(new Card(Suit.CLUBS, Rank.TEN));
            player.addToHand(new Card(Suit.HEARTS, Rank.TEN));
            // player.addToHand(this.deck.pickCard());
            // player.addToHand(this.deck.pickCard());
        });     
        support.firePropertyChange("dealt", null, null);
        if (this.players.get(0).getHand(0).isSameRank()){
            support.firePropertyChange("split", null, null);
        }
    }  
    
    public void stand(){
        if (this.players.get(this.playersTurn).numberOfHands() > this.playersNumberOfHands+1){
            this.playersNumberOfHands++;
        }
        else {
            this.nextPlayer();
        } 
    }

    public void hit(Player player){
        player.addToHand(this.deck.pickCard(), playersNumberOfHands);
        support.firePropertyChange("player", null,player);
        if (player.getHand(playersNumberOfHands).isBust()){ 
            playersNumberOfHands++;
        }
        if (player.numberOfHands()<(playersNumberOfHands+1)){
            nextPlayer();
        }
    }

    public void dealerHit(){
        while (this.dealer.play()){
            this.dealer.addToHand(this.deck.pickCard());
        }
        support.firePropertyChange("updateDealer", null, null);
        support.firePropertyChange("show winners", null, this.findWinners());
    }

    public String findWinners(){
        StringBuilder result = new StringBuilder();
        if (this.dealer.getHand(0).isBust()){
            return this.dealerBust(result);
        } else if(this.dealer.getHand(0).isBlackJack()){
            return this.dealerBlackJack(result);
        }
        else{
            result.append("still handling stuff...");
            return result.toString();
        }
    }

    public String dealerBust(StringBuilder result){
        result.append("Dealer busts\n");
        getPlayers().stream().forEach(player -> {
            ArrayList<Integer> winningHands = new ArrayList<>();
            player.getAllHands().forEach(hand -> {
                int index = player.getAllHands().indexOf(hand);
                if (hand.isBust()){
                    winningHands.add(0);                                              
                } 
                else if (hand.isBlackJack()){
                    player.setBankroll(player.getHand(index).getBet()*2.5);
                    winningHands.add(1);
                } 
                else{
                    System.out.println("Bankroll: " + player.getBankroll());
                    player.setBankroll(player.getHand(index).getBet()*2);
                    System.out.println("Bankroll: " + player.getBankroll());
                    winningHands.add(1);
                }
            });
            
            if (winningHands.stream().mapToInt(Integer::intValue).sum()>0){
                result.append("Player ").append(player.getID()).append(" won ").append(winningHands.stream().filter(x -> x == 1).count()).append(" hands\n");
            }
        });
        return result.toString();
    }

    public String dealerBlackJack(StringBuilder result){
        result.append("Dealer has BlackJack\n");
        getPlayers().stream().forEach(player -> {
            ArrayList<Integer> winningHands = new ArrayList<>();
            player.getAllHands().forEach(hand -> {
                int index = player.getAllHands().indexOf(hand);
                if (hand.isBlackJack()){
                    player.setBankroll(player.getHand(index).getBet());
                    winningHands.add(1);
                } 
                else{
                    winningHands.add(0);
                }
            });
            if (winningHands.stream().mapToInt(Integer::intValue).sum()>0){
                result.append("Player ").append(player.getID()).append(" won ").append(winningHands.stream().filter(x -> x == 1).count()).append(" hands\n");
            }
        });
        return result.toString();
    }

    public void reset(){
        this.deck.InitializeDeck();
        this.players.stream().forEach(player -> player.clearHands());
        this.dealer.getHand(0).clearHand();
        this.playersTurn = 0;
        this.playersNumberOfHands = 0;
        support.firePropertyChange("dealt", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}