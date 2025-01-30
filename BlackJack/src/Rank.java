package src;

import java.util.*;

public enum Rank {
    TWO("2", Arrays.asList(2)),
    THREE("3", Arrays.asList(3)),
    FOUR("4", Arrays.asList(4)),
    FIVE("5", Arrays.asList(5)),
    SIX("6", Arrays.asList(6)),
    SEVEN("7", Arrays.asList(7)),
    EIGHT("8", Arrays.asList(8)),
    NINE("9", Arrays.asList(9)),
    TEN("10", Arrays.asList(10)),
    JACK("J", Arrays.asList(10)),
    QUEEN("Q", Arrays.asList(10)),
    KING("K", Arrays.asList(10)),
    ACE("A", Arrays.asList(1, 11));

    private final String symbol;
    private final List<Integer> values;

    Rank(String symbol, List<Integer> values) {
        this.symbol = symbol;
        this.values = values;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public List<Integer> getValues() {
        return values;
    }
}