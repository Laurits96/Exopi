package src;
import java.util.*;
public class Player{
    private int id;
    private ArrayList<Card> hand;
    private int bankroll;

    public Player(int id){
        this.id =id;
        this.hand = new ArrayList<>();
        this.bankroll = 200;
    }

    public int getBankroll(){
        return this.bankroll;
    }

    public void setBankroll(int x){
        this.bankroll+=x;
    }

    public void addHand(Card card){
        this.hand.add(card);
    }

    public void clearHand(){
        this.hand.clear();
    }

    public int sumHand(){
        return this.hand.stream() 
        .flatMap(card -> card.getValue().stream()) 
        .mapToInt(Integer::intValue)
        .sum();
    }
    
    public int getID(){
        return this.id;
    }
}