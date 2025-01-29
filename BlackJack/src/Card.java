package src;
import java.util.List;
public class Card {
    private String suit;
    private String rank;
    private List<Integer> value;


    public Card(String suit, String rank, List<Integer> value){
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    public String getSuit(){
        return this.suit;
    }

    public String getRank(){
        return this.rank;
    }

    public List<Integer> getValue(){
        return this.value;
    }

    @Override
    public String toString(){
         return getRank()+" "+getSuit();
    }
}
