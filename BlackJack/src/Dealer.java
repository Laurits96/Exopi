package src;

import java.util.*;

public class Dealer {
    private ArrayList<Card> hand;
    
    public Dealer(){
        this.hand = new ArrayList<>();
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
}
