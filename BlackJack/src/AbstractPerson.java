package src;

import java.util.*;

public abstract class AbstractPerson implements Person{  
    protected ArrayList<Hand> hands;
    
    AbstractPerson(){
        this.hands = new ArrayList<>();
        this.hands.add(new Hand());
    }

    @Override
    public Hand getHand(int index){
        return this.hands.get(index);
    }

    @Override
    public void addToHand(Card card){
        this.hands.get(0).add(card);
    }

    @Override
    public void addToHand(Card card, int index){
        this.hands.get(index).add(card);
    }

    @Override
    public void clearHands(){
        this.hands.clear();
        this.hands.add(new Hand());
    };

    public void printHand(){
        this.hands.stream().forEach(hand -> System.out.println(hand.handToString()));
    }

}
