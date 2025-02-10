package com.blackjack.helper;

import java.util.*;

public abstract class AbstractPerson {  
    protected ArrayList<Hand> hands;
    
    AbstractPerson(){
        this.hands = new ArrayList<>();
        this.hands.add(new Hand());
    }

    public Hand getHand(int index){
        return this.hands.get(index);
    }

    public void addToHand(Card card){
        this.hands.get(0).add(card);
    }

    public void addToHand(Card card, int index){
        this.hands.get(index).add(card);
    }

    public void clearHands(){
        this.hands.clear();
        this.hands.add(new Hand());
    };

    public void printHand(){
        this.hands.stream().forEach(hand -> System.out.println(hand.handToString()));
    }

}
