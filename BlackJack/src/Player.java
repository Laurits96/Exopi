package src;
public class Player extends AbstractPerson{
    private int id;
    private int bankroll;

    public Player(int id){
        this.id =id;
        this.bankroll = 200;
    }

    public int getBankroll(){
        return this.bankroll;
    }

    public void setBankroll(int x){
        this.bankroll+=x;
    }
    
    public int getID(){
        return this.id;
    }
}