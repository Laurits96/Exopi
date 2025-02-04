package src.main.java.helper;
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

    public int getValue(){
        return this.rank.getValue();
    }

    @Override
    public String toString(){
        return getRank()+getSuit();
    }
}
