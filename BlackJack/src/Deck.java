package src;
import java.util.*;
public class Deck {
    private List<String> suits = List.of("H", "C", "D", "S");
    private List<Map.Entry<String, List<Integer>>> cardRanking = new ArrayList<>(Arrays.asList(
            new AbstractMap.SimpleEntry<>("1", Arrays.asList(1)),
            new AbstractMap.SimpleEntry<>("2", Arrays.asList(2)),
            new AbstractMap.SimpleEntry<>("3", Arrays.asList(3)),
            new AbstractMap.SimpleEntry<>("4", Arrays.asList(4)),
            new AbstractMap.SimpleEntry<>("5", Arrays.asList(5)),
            new AbstractMap.SimpleEntry<>("6", Arrays.asList(6)),
            new AbstractMap.SimpleEntry<>("7", Arrays.asList(7)),
            new AbstractMap.SimpleEntry<>("8", Arrays.asList(8)),
            new AbstractMap.SimpleEntry<>("9", Arrays.asList(9)),
            new AbstractMap.SimpleEntry<>("10", Arrays.asList(10)),
            new AbstractMap.SimpleEntry<>("J", Arrays.asList(10)),
            new AbstractMap.SimpleEntry<>("Q", Arrays.asList(10)),
            new AbstractMap.SimpleEntry<>("K", Arrays.asList(10)),
            new AbstractMap.SimpleEntry<>("A", Arrays.asList(1,11))
        ));

    private ArrayList<Card> cardDeck;
    
    public Deck(int noDecks){
        this.cardDeck = new ArrayList<>();
        for (int i=0;i<noDecks;i++){
            for ( String suit: this.suits){
                for (Map.Entry<String, List<Integer>> entry : this.cardRanking) {
                    this.cardDeck.add(new Card(suit,entry.getKey(), entry.getValue()));
                }
            }
        }   
    }

    public Card dealCard(Player player){
        int picked = new Random().nextInt(this.cardDeck.size()+1);
        Card dealt = this.cardDeck.get(picked);
        this.cardDeck.remove(picked);
        return dealt;
    }

    public void toPrint(){
        for (Card card:this.cardDeck){
            System.out.println(card.getRank()+ " " + card.getSuit());
        }
    }

}

