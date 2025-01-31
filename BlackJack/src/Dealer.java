package src;
public class Dealer extends AbstractPerson{
    
    public Dealer(){ 
    }

    public boolean play(){
        return (this.hands.get(0).sumHand() < 17);
    }
}
