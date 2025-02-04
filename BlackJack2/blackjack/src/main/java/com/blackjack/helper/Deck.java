package com.blackjack.helper;

import java.util.*;
public class Deck {
    private ArrayList<Card> cardDeck;
    private int noDecks = 0;
    
    public Deck(int noDecks){
        this.cardDeck = new ArrayList<>();
        this.noDecks = noDecks;
    }

    public void InitializeDeck(){
        for (int i = 0; i < noDecks; i++){
            addDeck();
        }
    }

    public void addDeck(){
        for ( Suit suit: Suit.values()){
            for (Rank rank : Rank.values()) {
                this.cardDeck.add(new Card(suit, rank));
            }
        }
    }

    public ArrayList<Card> getCardDeck(){
        return this.cardDeck;
    }

    public Card pickCard(){
        int picked = new Random().nextInt(this.cardDeck.size());
        Card dealt = this.cardDeck.get(picked);
        this.cardDeck.remove(picked);
        return dealt;
    }

    public void toPrint(){
        for (Card card : this.cardDeck){
            System.out.println(card.toString() );
        }
    }

    
}
