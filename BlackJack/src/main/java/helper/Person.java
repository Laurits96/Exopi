package src.main.java.helper;

public interface Person{
    public Hand getHand(int index);
    public void addToHand(Card card);
    public void addToHand(Card card, int index);
    public void clearHands();
}
