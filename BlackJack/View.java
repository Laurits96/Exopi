import src.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;

public class View extends JFrame implements PropertyChangeListener {
	private ArrayList<ArrayList<JLabel>> playerHandLabels;
	private ArrayList<JLabel> playerLabels;
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
		dealerHand.setBounds(50, 60, 400, 30);
		
		dealButton.setBounds(200, 20, 100, 30);
		standButton.setBounds(200, 220, 100, 30);
		hitButton.setBounds(50, 220, 100, 30);
		splitButton.setBounds(50, 280, 100, 30);
		forfeitButton.setBounds(200, 280, 100, 30);
		addPlayerButton.setBounds(350, 20, 100, 30);
		
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
		hitButton.setToolTipText("Hit, and get another card");
		splitButton.setEnabled(false);
		splitButton.setToolTipText("Only usable when starting hand consist of cards with the same value");
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
			dealerHand.revalidate();
			dealerHand.repaint();
			this.model.getPlayers().stream().forEach(player -> {
				updatePlayerHandLabel(player);
				redo(playerHandLabels.get(player.getID()-1).get(0));
			});
			
		}
		else if("nextTurn".equals(evt.getPropertyName())){
			updatePlayerLabel();
		}
		else if ("dealer won".equals(evt.getPropertyName())){
			JOptionPane.showMessageDialog(this, "Dealer won");
			this.model.dealCard();

		}		
		else if ("Player lost".equals(evt.getPropertyName())){
			JOptionPane.showMessageDialog(this, "Player lost");
			this.model.reset();
		} 
		else if ("show winners".equals(evt.getPropertyName())){
			JOptionPane.showMessageDialog(this, (String) evt.getNewValue());
			this.model.reset();
		}
		else if ("newPlayer".equals(evt.getPropertyName())){
			addSinglePlayerInfo(this.model.getPlayers().get((int)evt.getNewValue()-1));
		}
		else if ("split".equals(evt.getPropertyName())){
			enablesplitButton();
		}
	}

	public void addPlayerInfo(){
		this.model.getPlayers().stream().forEach(player -> {
			this.playerTempLabel = new JLabel("Player "+player.getID());
			this.playerTempLabel.setBounds(50+(player.getID()-1)*150, 100, 100, 30);
			if(this.model.getPlayerTurn()==(player.getID()-1)){
				this.playerTempLabel.setForeground(Color.RED);
			}
			else{
				this.playerTempLabel.setForeground(Color.white);
			}
			this.playerTempLabel.setBackground(new Color(40, 119, 91, 100));
			this.playerLabels.add(this.playerTempLabel);
			add(this.playerTempLabel);

			createNewHandLabel(player);
		});
	}

	public void addSinglePlayerInfo(Player player){
		this.playerTempLabel = new JLabel("Player "+player.getID());
		this.playerTempLabel.setBounds(50+(player.getID()-1)*150, 100, 100, 30);
		this.playerTempLabel.setForeground(Color.WHITE);
		this.playerTempLabel.setBackground(new Color(40, 119, 91, 100));
		this.playerLabels.add(this.playerTempLabel);
		add(this.playerTempLabel);
		redo(this.playerTempLabel);

		createNewHandLabel(player);
	}

	public void addSplitHand(Player player){
		this.playerHandLabels.get(player.getID()-1).get(0).setText(player.getHand(0).handToString());
		redo(this.playerHandLabels.get(player.getID()-1).get(0));

		this.playerHandTempLabel = new JLabel(player.getHand(1).handToString());
		this.playerHandTempLabel.setBounds(50+(player.getID()-1)*150, 180, 400, 30);
		this.playerHandTempLabel.setForeground(Color.WHITE);
		this.playerHandTempLabel.setBackground(new Color(40, 119, 91, 100));
		this.playerHandLabels.get(player.getID()-1).add(this.playerHandTempLabel);
		add(this.playerHandTempLabel);
		redo(this.playerHandTempLabel); // see om det gør ngoet

		this.playerHandLabels.get(player.getID()-1).get(1).setText(player.getHand(1).handToString());
		this.playerHandLabels.get(player.getID()-1).forEach(l->redo(l));
	}

	public void createNewHandLabel(Player player){
		ArrayList<JLabel> playerHandTempLabels = new ArrayList<>();
		this.playerHandTempLabel = new JLabel("");
		this.playerHandTempLabel.setBounds(50+(player.getID()-1)*150, 140, 400, 30);
		this.playerHandTempLabel.setForeground(Color.WHITE);
		this.playerHandTempLabel.setBackground(new Color(40, 119, 91, 100));
		playerHandTempLabels.add(this.playerHandTempLabel);
		this.playerHandLabels.add(playerHandTempLabels);
		add(this.playerHandTempLabel);
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
}
