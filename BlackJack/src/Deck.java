package src;
import java.util.*;
public class Deck {
    private ArrayList<Card> cardDeck;
    
    public Deck(int noDecks){
        InitializeDeck();
    }

    public void InitializeDeck(){
        this.cardDeck = new ArrayList<>();
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
