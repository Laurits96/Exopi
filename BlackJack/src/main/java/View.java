package src.main.java;
import src.main.java.helper.Player;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.MatteBorder;

public class View extends JFrame implements PropertyChangeListener {
	private ArrayList<ArrayList<JLabel>> playerHandLabels;
	private ArrayList<JLabel> playerLabels;
	private ArrayList<JTextField> betInputs;
	private JLabel playerHandTempLabel;
	private JLabel playerTempLabel;
	private JLabel dealerHand;
	private JLabel dealer;	
	private JButton dealButton;
	private JButton standButton;
	private JButton hitButton;
	private JButton splitButton;
	private JButton forfeitButton;
	private JButton addPlayerButton;
	private Model model;    

	public View(Model model){
		this.playerHandLabels = new ArrayList<>(new ArrayList<>());
		this.playerLabels = new ArrayList<>();
		this.betInputs = new ArrayList<>();
		this.model = model;
		this.model.addPropertyChangeListener(this);
		initializeUI();
	}

	public void initializeUI(){
		setSize(900, 600);
		setTitle("BlackJack");
		setLayout(null);
		getContentPane().setBackground(new Color(40, 119, 91));
		
		// Initialize UI components
		dealerHand = new JLabel();
		dealer = new JLabel();
		dealButton = new JButton("Deal");
		standButton = new JButton("Stand");
		hitButton = new JButton("Hit");
		splitButton = new JButton("Split");
		forfeitButton = new JButton("Forfeit");
		addPlayerButton = new JButton("Add Player");

		// Set bounds for each component
		dealer.setBounds(50, 20, 100, 30);
		dealer.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));
		dealerHand.setBounds(50, 60, 400, 30);
		
		dealButton.setBounds(220, 20, 100, 30);
		standButton.setBounds(220, 220, 100, 30);
		hitButton.setBounds(50, 220, 100, 30);
		splitButton.setBounds(50, 280, 100, 30);
		forfeitButton.setBounds(220, 280, 100, 30);
		addPlayerButton.setBounds(390, 20, 100, 30);
		
		// Set properties for each component
		dealer.setText("Dealer");
		dealer.setForeground(Color.WHITE);
		dealerHand.setForeground(Color.WHITE);
		dealerHand.setText(" ");

		// Add components to the frame
		add(dealer);
		add(dealerHand);
		add(dealButton);
		add(standButton);
		add(hitButton);
		add(splitButton);
		add(forfeitButton);
		add(addPlayerButton);

		addPlayerInfo();

		// Set tooltips for each button
		dealButton.setToolTipText("Deal cards, and start a new game");
		standButton.setToolTipText("Stand, and let the dealer play");
		standButton.setEnabled(false);
		hitButton.setToolTipText("Hit, and get another card");
		hitButton.setEnabled(false);
		splitButton.setEnabled(false);
		splitButton.setToolTipText("Only usable when starting hand consist of cards with the same value");
		forfeitButton.setEnabled(false);
		forfeitButton.setToolTipText("Forfeit the current hand, and loose half your bet");
		addPlayerButton.setToolTipText("Add a new player to the game");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("updateDealer".equals(evt.getPropertyName())){
			dealerHand.setText(model.getDealer().getHand(0).handToString());
			dealerHand.revalidate();
			dealerHand.repaint();
		} 
		else if ("player".equals(evt.getPropertyName())){
			updatePlayerHandLabel((Player)evt.getNewValue());
			
		} 
		else if ("dealt".equals(evt.getPropertyName())){
			dealerHand.setText(model.getDealer().getHand(0).handToString());
			redo(dealerHand);
			this.model.getPlayers().stream().forEach(player -> {
				updatePlayerHandLabel(player);
				redo(playerHandLabels.get(player.getID()-1).get(0));
			});
			updatePlayerLabel();
			enableForfeitButton();
		}
		else if("nextTurn".equals(evt.getPropertyName())){
			updatePlayerLabel();
			enableForfeitButton();
		}
		else if ("show winners".equals(evt.getPropertyName())){
			if ((boolean) evt.getOldValue()){
				showJOptionPane("Folding already? Dealer wins—without lifting a finger!");
			}else{
				showJOptionPane((String) evt.getNewValue());
			}
			this.model.reset();
			updatePlayerLabel();
			enableAddPlayerButton();
			enableDealButton();
		}
		else if ("newPlayer".equals(evt.getPropertyName())){
			addSinglePlayerInfo(this.model.getPlayers().get((int)evt.getNewValue()-1));
		}
		else if ("split".equals(evt.getPropertyName())){
			enablesplitButton();
		}
		else if("reset".equals(evt.getPropertyName())){
			dealerHand.setText(model.getDealer().getHand(0).handToString());
			redo(dealerHand);
			updatePlayerLabel();
			resetAllPlayerHandLabel();
			enableAddPlayerButton();
			disableForfeitButton();
			disableHitButton();
			disableStandButton();
		}
	}

	public void addPlayerInfo(){
		this.model.getPlayers().stream().forEach(player -> {
			this.playerTempLabel = new JLabel("<html> Player "+player.getID() + "    $"+player.getBankroll() + "<br>"+ "Bet: ");
			this.playerTempLabel.setBounds(50+(player.getID()-1)*170, 100, 100, 35);
			this.playerTempLabel.setForeground(Color.RED);
			this.playerTempLabel.setBackground(new Color(40, 119, 91, 100));
			this.playerTempLabel.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));
			this.playerLabels.add(this.playerTempLabel);
			add(this.playerTempLabel);
			addBetInput(player);
			createNewHandLabel(player);
		});
	}

	public void addSinglePlayerInfo(Player player){
		this.playerTempLabel = new JLabel("<html> Player "+player.getID() + "    $"+player.getBankroll() + "<br>"+ "Bet: </html>");
		this.playerTempLabel.setBounds(50+(player.getID()-1)*170, 100, 100, 35);
		this.playerTempLabel.setForeground(Color.WHITE);
		this.playerTempLabel.setBackground(new Color(40, 119, 91, 100));
		this.playerTempLabel.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));
		this.playerLabels.add(this.playerTempLabel);
		add(this.playerTempLabel);
		redo();

		createNewHandLabel(player);
		addBetInput(player);
	}

	public void addSplitHand(Player player){
		this.playerHandLabels.get(player.getID()-1).get(0).setText(player.getHand(0).handToString());
		redo(this.playerHandLabels.get(player.getID()-1).get(0));

		this.playerHandTempLabel = new JLabel(player.getHand(1).handToString());
		this.playerHandTempLabel.setBounds(50+(player.getID()-1)*170, 180, 400, 30);
		this.playerHandTempLabel.setForeground(Color.WHITE);
		this.playerHandTempLabel.setBackground(new Color(40, 119, 91, 100));
		this.playerHandLabels.get(player.getID()-1).add(this.playerHandTempLabel);
		add(this.playerHandTempLabel);
		redo(this.playerHandTempLabel); 

		this.playerHandLabels.get(player.getID()-1).get(1).setText(player.getHand(1).handToString());
		this.playerHandLabels.get(player.getID()-1).forEach(l->redo(l));
	}

	public void createNewHandLabel(Player player){
		ArrayList<JLabel> playerHandTempLabels = new ArrayList<>();
		this.playerHandTempLabel = new JLabel("");
		this.playerHandTempLabel.setBounds(50+(player.getID()-1)*170, 140, 400, 30);
		this.playerHandTempLabel.setForeground(Color.WHITE);
		this.playerHandTempLabel.setBackground(new Color(40, 119, 91, 100));
		playerHandTempLabels.add(this.playerHandTempLabel);
		this.playerHandLabels.add(playerHandTempLabels);
		add(this.playerHandTempLabel);
		redo(playerHandTempLabel);
	}

	public void addBetInput(Player player){
		JTextField tempBetInput = new JTextField();
		tempBetInput.setText("Place bet");
		tempBetInput.setBackground(new Color(40, 119, 91, 100));
		tempBetInput.setForeground(Color.WHITE);
		tempBetInput.setCaretColor(Color.WHITE);
		tempBetInput.setBorder(BorderFactory.createLineBorder(new Color(40, 119, 91), 1));
		tempBetInput.setBounds(80+(player.getID()-1)*170, 117, 65, 15);
		tempBetInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                tempBetInput.selectAll();
				tempBetInput.revalidate();
				tempBetInput.repaint();
            }
        });
		betInputs.add(tempBetInput);
		add(tempBetInput);
		redo();
	}

	public boolean isBetsPlaced(){
		StringBuilder result = new StringBuilder();
		for (JTextField bet : this.betInputs){
			if (!(bet.getText().matches("[0-9]+"))){
				showJOptionPane("All Players must enter an amount to bet");
				return false;
			}
		}	
		this.betInputs.stream().forEach(bet->{
			if (Double.parseDouble(bet.getText()) > this.model.getPlayers().get(betInputs.indexOf(bet)).getBankroll()){
				result.append("Woah! Getting to carried away?\nYou cant bet more than you have\n");
			}
		});
		if (!result.isEmpty()){
			showJOptionPane(result.toString());
			return false;
		}

		return true;
	}

	public ArrayList<Double> betsPlaced(){
		ArrayList<Double> bets = new ArrayList<>();
		this.betInputs.stream().forEach(bet ->{
			bets.add(Double.parseDouble(bet.getText()));
		});
		return bets;
	}

	public void updatePlayerHandLabel(Player player){
		this.playerHandLabels.get(player.getID()-1).forEach(playerHandLabel -> {
			int index = this.playerHandLabels.get(player.getID()-1).indexOf(playerHandLabel);
    		playerHandLabel.setText(player.getHand(index).handToString());
			redo(playerHandLabel);
		});
	}

	public void updatePlayerLabel(){
		this.playerLabels.forEach(playerLabel -> {
			playerLabel.setText("<html>Player "+this.model.getPlayers().get(this.playerLabels.indexOf(playerLabel)).getID() + "   $"+this.model.getPlayers().get(this.playerLabels.indexOf(playerLabel)).getBankroll()  + "<br>"+ "Bet: </html>");
			int index = this.playerLabels.indexOf(playerLabel);
			if(this.model.getPlayerTurn()==index){
				playerLabel.setForeground(Color.RED);
			}
			else{
				playerLabel.setForeground(Color.white);
			}
			redo(playerLabel);
		});
	}

	public void redo(JLabel label){
		label.revalidate();
		label.repaint();
	}

	public void redo(){
		revalidate();
		repaint();
	}

	public void resetAllPlayerHandLabel() {
	    this.playerHandLabels.forEach(playerHandLabel -> {
	        if (playerHandLabel.size() > 1) {
	            playerHandLabel.subList(1, playerHandLabel.size()).forEach(label->{
					remove(label);
				});;
				playerHandLabel.subList(1, playerHandLabel.size()).clear();
	        }
	        // Reset the text of the first element
	        if (!playerHandLabel.isEmpty()) {
	            playerHandLabel.get(0).setText("");
	        }
	    });
		revalidate();
		repaint();
	}

	public void showJOptionPane(String string){
		JOptionPane.showMessageDialog(this, string);
	}

	public void addDealButtonListener(ActionListener listener){
		dealButton.addActionListener(listener);
	}

	public void addStandButtonListener(ActionListener listener){
		standButton.addActionListener(listener);
	}

	public void addHitButtonListener(ActionListener listener){
		hitButton.addActionListener(listener);
	}

	public void addSplitButtonListener(ActionListener listener){
		splitButton.addActionListener(listener);
	}

	public void addForfeitButtonListener(ActionListener listener){
		forfeitButton.addActionListener(listener);
	}

	public void addAddPlayerButtonListener(ActionListener listener){
		addPlayerButton.addActionListener(listener);
	}

	public void enableDealButton(){	
		dealButton.setEnabled(true);
	}

	public void disableDealButton() {
	    dealButton.setEnabled(false);
	}

	public void enableHitButton(){	
		hitButton.setEnabled(true);
	}

	public void disableHitButton() {
	    hitButton.setEnabled(false);
	}

	public void enableStandButton(){	
		standButton.setEnabled(true);
	}

	public void disableStandButton() {
	    standButton.setEnabled(false);
	}

	public void enablesplitButton() {
	    splitButton.setEnabled(true);
	}

	public void disableSplitButton() {
	    splitButton.setEnabled(false);
	}

	public void disableAddPlayerButton() {
	    addPlayerButton.setEnabled(false);
	}

	public void enableAddPlayerButton() {
	    addPlayerButton.setEnabled(true);
	}

	public void enableForfeitButton() {
	    forfeitButton.setEnabled(true);
	}

	public void disableForfeitButton() {
	    forfeitButton.setEnabled(false);
	}
}
