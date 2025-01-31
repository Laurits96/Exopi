package src;

import java.util.ArrayList;

public class Player extends AbstractPerson{
    private int id;
    private double bankroll;
    private boolean forfeited;

    public Player(int id){
        this.id =id;
        this.bankroll = 200;
        this.forfeited = false;
    }

    public ArrayList<Hand> getAllHands(){
        return this.hands;
    }

    public int getID(){
        return this.id;
    }

    public double getBankroll(){
        return this.bankroll;
    }

    public void setBankroll(double bet){
        this.bankroll+=bet;
    }

    public boolean isForfeited() {
        return forfeited;
    }

    public void setForfeited(boolean forfeited) {
        this.forfeited = forfeited;
    }

    public int numberOfHands(){
        return this.hands.size();
    }

    public void splitHand(){
        this.hands.add(new Hand(this.hands.get(0).removeCard(1)));
    }
}