package src;

import java.util.*;

public abstract class AbstractPerson implements Person{  
    protected ArrayList<Hand> hands;
    
    AbstractPerson(){
        this.hands = new ArrayList<>();
        this.hands.add(new Hand());
    }

    @Override
    public Hand getHand(){
        return this.hands.get(0);
    }

    @Override
    public void addToHand(Card card){
        this.hands.get(0).add(card);
    }

    @Override
    public void clearHands(){
        this.hands.clear();
    };

    public void printHand(){
        this.hands.stream().forEach(hand -> System.out.println(hand.handToString()));
    }

}
