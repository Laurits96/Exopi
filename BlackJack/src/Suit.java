package src;
public enum Suit {
    HEARTS("H"),
    CLUBS("C"),
    DIAMONDS("D"),
    SPADES("S");

    private final String symbol;

    Suit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}