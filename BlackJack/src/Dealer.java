package src;

import java.util.*;

public class Dealer extends AbstractPerson{
    
    
    public Dealer(){
        
    }

    // public void addToHand(Card card){
    //     this.hand.add(card);
    // }

    // public void clearHand(){
    //     this.hand.clear();
    // }

    // public int sumHand(){
    //     return this.hand.stream() 
    //         .flatMap(card -> card.getValue().stream()) 
    //         .mapToInt(Integer::intValue) 
    //         .sum();
    // }

    // public String handToString(){
    //     String x = "";
    //     for( Card card: this.hand){
    //         x += card.getRank() + " " + card.getSuit() + "  ";
    //     }
    //     return x;
    // }
}
