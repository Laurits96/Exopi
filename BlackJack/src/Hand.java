package src;

import java.util.ArrayList;

public class Hand{
    private ArrayList<Card> hand;
    private int aceCount;

    public Hand(){
        this.hand = new ArrayList<>();
        this.aceCount = 0;
    }

    public void add(Card card){
        this.hand.add(card);
        if(card.getRank().equals("A")){
            this.aceCount++;
        }
    }

    public void clearHand(){
        this.hand.clear();
        this.aceCount = 0;
    }

    public int sumHand(){
        return this.hand.stream() 
            .flatMap(card -> card.getValue().stream()) 
            .mapToInt(Integer::intValue)
            .sum();
    }

    public String handToString(){
        String x = "";
        for( Card card: this.hand){
            x += card.getRank() + " " + card.getSuit() + "  ";
        }
        return x;
    }

    public void printHand(){
        System.out.println(handToString());
    }
    
}
