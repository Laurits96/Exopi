package src;
import java.util.List;
public class Card {
    private Suit suit;
    private Rank rank;

    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit(){
        return this.suit.getSymbol();
    }

    public String getRank(){
        return this.rank.getSymbol();
    }

    public List<Integer> getValue(){
        return this.rank.getValues();
    }

    @Override
    public String toString(){
        return getRank()+" "+getSuit();
    }
}
