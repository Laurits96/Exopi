package src;
import java.util.*;
public class Deck {
    private ArrayList<Card> cardDeck;
    
    public Deck(int noDecks){
        InitializeDeck();
    }

    public ArrayList<Card> getCardDeck(){
        return this.cardDeck;
    }

    public void toPrint(){
        for (Card card:this.cardDeck){
            System.out.println(card.toString() );
        }
    }

    public void InitializeDeck(){
        this.cardDeck = new ArrayList<>();
        for ( Suit suit: Suit.values()){
            for (Rank rank : Rank.values()) {
                this.cardDeck.add(new Card(suit, rank));
            }
        }
    }
}
