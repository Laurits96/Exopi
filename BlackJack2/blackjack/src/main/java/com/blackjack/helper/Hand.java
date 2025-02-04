package com.blackjack.helper;


import java.util.ArrayList;

public class Hand{
    private ArrayList<Card> hand;
    private int aceCount;

    public Hand(){
        this.hand = new ArrayList<>();
        this.aceCount = 0;
    }

    public Hand(Card card1){
        this.hand = new ArrayList<>();
        this.hand.add(card1);
    }

    public void add(Card card){
        this.hand.add(card);
        if(card.getRank().equals("A")){
            this.aceCount++;
        }
    }

    public Card getCard(int i){
        return this.hand.get(i);
    }

    public Card removeCard(int index){
        return this.hand.remove(index);
    }

    public int size(){
        return this.hand.size();
    }

    public int sumHand(){
        if ( aceCount > 0 && this.hand.stream().mapToInt(Card::getValue).sum() > 21){
            int sum = this.hand.stream().mapToInt(Card::getValue).sum();
            for (int i = 0; i < aceCount; i++){
                if (sum > 21){
                    sum -= 10;
                }
            }
            return sum;
        }else{
            return this.hand.stream().mapToInt(Card::getValue).sum();
        }
    }

    public boolean isSameRank(){
        if (size()>1){
            return this.hand.get(0).getRank().equals(this.hand.get(1).getRank());
        }else {return false;}
    }

    public boolean isBust(){
        return this.sumHand() > 21;
    }

    public boolean isBlackJack(){
        return this.hand.size() == 2 && this.sumHand() == 21;
    }

    public void clearHand(){
        this.hand.clear();
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
