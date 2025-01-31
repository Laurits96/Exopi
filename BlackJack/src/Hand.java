package src;

import java.util.ArrayList;

public class Hand{
    private ArrayList<Card> hand;
    private int bet;

    public Hand(){
        this.hand = new ArrayList<>();
        this.bet = 0;
    }

    public Hand(Card card1){
        this.hand = new ArrayList<>();
        this.hand.add(card1);
    }

    public void add(Card card){
        this.hand.add(card);
    }

    public Card removeCard(int index){
        return this.hand.remove(index);
    }

    public int size(){
        return this.hand.size();
    }

    public int sumHand(){
        return this.hand.stream().mapToInt(Card::getValue).sum();
    }

    public boolean isSameRank(){
        return this.hand.get(0).getRank().equals(this.hand.get(1).getRank());
    }

    public boolean isBust(){
        return this.sumHand() > 21;
    }

    public void placeBet(int bet){
        this.bet=-bet;
    }

    public int getBet(){
        return this.bet;
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
