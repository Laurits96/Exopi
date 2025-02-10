package com.blackjack;

import java.util.*;

import com.blackjack.helper.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
public class Model{
    private ArrayList<Player> players = new ArrayList<>();
    private int playersTurn;
    private int playersNumberOfHands;
    private Dealer dealer;
    private Deck deck;
    private PropertyChangeSupport support;
    private boolean gameStarted = false;

    public Model(){
        this.players.add(new Player(1));
        this.playersTurn = 0;
        this.playersNumberOfHands = 0;
        this.dealer = new Dealer();
        this.deck = new Deck(4);
        this.support = new PropertyChangeSupport(this);
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public Deck getDeck(){
        return this.deck;
    }

    public Dealer getDealer(){
        return this.dealer;
    }

    public void addPlayer(){
        this.players.add(new Player(this.players.size() + 1));
        support.firePropertyChange("newPlayer", null, this.players.get(this.players.size()-1).getID());
    }

    public void setGameStarted(boolean gameStarted){
        this.gameStarted = gameStarted;
    }

    public boolean getGameStarted(){
        return this.gameStarted;
    }

    public int getPlayerTurn(){
        return this.playersTurn;
    }

    public void nextPlayer(){
        this.playersTurn++;
        if (this.playersTurn == this.players.size()){
            this.dealerHit();
        }else {
            this.playersNumberOfHands = 0;
            support.firePropertyChange("nextTurn", null, null);
            if (this.players.get(this.playersTurn).getHand(0).isSameRank()){
                support.firePropertyChange("split", null, null);
            }
        }
    }    

    public void dealCard(){
        this.dealer.addToHand(this.deck.pickCard());
        // this.dealer.addToHand(new Card(Suit.DIAMONDS, Rank.ACE));
        // this.dealer.addToHand(new Card(Suit.DIAMONDS, Rank.JACK));
        this.players.stream().forEach(player -> {
            player.addToHand(new Card(Suit.DIAMONDS, Rank.ACE));
            player.addToHand(new Card(Suit.HEARTS, Rank.ACE));
            // player.addToHand(this.deck.pickCard());
            // player.addToHand(this.deck.pickCard());
        });     
        support.firePropertyChange("dealt", null, null);
        if (this.players.get(0).getHand(0).isSameRank()){
            support.firePropertyChange("split", null, null);
        }
    }  
    
    public void stand(){
        if (this.players.get(this.playersTurn).numberOfHands() > this.playersNumberOfHands+1){
            this.playersNumberOfHands++;
        }
        else {
            this.nextPlayer();
        } 
    }

    public void hit(Player player){
        player.addToHand(this.deck.pickCard(), playersNumberOfHands);
        support.firePropertyChange("player", null,player);
        if (player.getHand(playersNumberOfHands).isBust()){ 
            playersNumberOfHands++;
        }
        if (player.numberOfHands()<(playersNumberOfHands+1)){
            nextPlayer();
        }
    }

    public void dealerHit(){
        while (this.dealer.play()){
            this.dealer.addToHand(this.deck.pickCard());
        }
        this.support.firePropertyChange("updateDealer", null, null);
        this.support.firePropertyChange("show winners", isAllForfeit(), this.findWinners());
    }

    public void forfeit(Player player){
        player.setForfeited(true);
        this.nextPlayer();
    }

    public void addBetToPlayers(ArrayList<Double> betsPlaced){
        getPlayers().forEach(player ->{
            player.setBet(betsPlaced.get(player.getID()-1));
            player.setBankroll(-betsPlaced.get(player.getID()-1));
        });
    }

    public String findWinners(){
        StringBuilder result = new StringBuilder();
        if (this.dealer.getHand(0).isBust()){
            return this.dealerBust(result);
        }
        else if(this.dealer.getHand(0).isBlackJack()){
            return this.dealerBlackJack(result);
        }
        else if(this.dealer.getHand(0).sumHand() >= 17){
            if(this.players.stream().allMatch(player -> player.getHand(0).isBust())){
                result.append("Dealer won all");
                return result.toString();
            }
            return this.dealerStopped(result);
        }
        else{
            result.append("still handling stuff...");
            return result.toString();
        }
    }

    private String dealerBust(StringBuilder result){
        result.append("Dealer busts\n");
        getPlayers().forEach(player -> {
            ArrayList<Integer> winningHands = new ArrayList<>();
            player.getAllHands().forEach(hand -> {
                if(player.isForfeited()){
                    player.setBankroll(player.getBet()*0.5);
                    winningHands.add(0);   
                }
                else if (hand.isBust()){
                    winningHands.add(0);                           
                } 
                else if (hand.isBlackJack()){
                    player.setBankroll(player.getBet()*2.5);
                    winningHands.add(1);   
                } 
                else{
                    player.setBankroll(player.getBet()*2);
                    winningHands.add(1);   
                }
            });
            if (winningHands.stream().mapToInt(Integer::intValue).sum()>0){
                result.append("Player ").append(player.getID()).append(" won ").append(winningHands.stream().filter(x -> x == 1).count()).append(" hand(s)\n");
            }
        });
        return result.toString();
    }

    private String dealerBlackJack(StringBuilder result){
        result.append("Dealer has BlackJack\n");
        getPlayers().forEach(player -> {
            ArrayList<Integer> winningHands = new ArrayList<>();
            player.getAllHands().forEach(hand -> {
                if(player.isForfeited()){
                    player.setBankroll(player.getBet()*0.5);
                    winningHands.add(1);
                }
                else if (hand.isBlackJack()){
                    player.setBankroll(player.getBet());
                    winningHands.add(1);
                } 
                else if(!hand.isBust() && hand.size()==5){
                    player.setBankroll(player.getBet());
                    winningHands.add(1);
                }
                else{
                    winningHands.add(0);
                }
            });
            if (winningHands.stream().mapToInt(Integer::intValue).sum()>0){
                result.append("Player ").append(player.getID()).append(" tied with ").append(winningHands.stream().filter(x -> x == 1).count()).append(" hand(s)\n");
            }
        });
        return result.toString();
    }

    private String dealerStopped(StringBuilder result){
        result.append("Dealer stopped at " + dealer.getHand(0).sumHand()+ "\n");
        getPlayers().stream().forEach(player -> {
            ArrayList<Integer> winningHands = new ArrayList<>();
            player.getAllHands().forEach(hand -> {
                if(player.isForfeited()){
                    player.setBankroll(player.getBet()*0.5);
                    winningHands.add(0);
                }
                else if(hand.isBust()){
                    winningHands.add(0);
                }
                else if (hand.isBlackJack()){
                    player.setBankroll(player.getBet()*2.5);
                    winningHands.add(1);
                } 
                else if(hand.sumHand() > this.dealer.getHand(0).sumHand() || hand.size() == 5){
                    player.setBankroll(player.getBet()*2);
                    winningHands.add(1);
                }
                else if(hand.sumHand() == this.dealer.getHand(0).sumHand()){
                    player.setBankroll(player.getBet());
                    winningHands.add(1);
                }
            });
            if (winningHands.stream().mapToInt(Integer::intValue).sum()>0){
                result.append("Player ").append(player.getID()).append(" won ").append(winningHands.stream().filter(x -> x == 1).count()).append(" hand(s)\n");
            } 
        });
        if(!(result.toString().contains("Player"))){
            result.append("Dealer won all");
        }
        return result.toString();
    }

    public boolean isAllForfeit(){
        for (Player player : this.players ){
            if (!player.isForfeited()){
                return false;
            }
        }
        return true;
    }

    public void reset(){
        this.deck.InitializeDeck();
        this.players.stream().forEach(player -> player.clearHands());
        this.dealer.getHand(0).clearHand();
        this.playersTurn = 0;
        this.playersNumberOfHands = 0;
        this.gameStarted = false;
        support.firePropertyChange("reset", null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}