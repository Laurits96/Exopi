import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class View extends JFrame implements PropertyChangeListener {
	private JLabel playerHand;
	private JLabel player;
    private JLabel dealerHand;
	private JLabel dealer;	
    private JButton dealButton;
    private JButton standButton;
    private JButton hitButton;
    private Model model;

	private JPanel[][] panelHolder = new JPanel[3][3];    

	public View(Model model){
		this.model = model;
		this.model.addPropertyChangeListener(this);
		initializeUI();
	}

	public void initializeUI(){
		setSize(500, 400);
		setTitle("BlackJack");
		setLayout(null);
		getContentPane().setBackground(new Color(40, 119, 91));
		
		// Initialize UI components
        playerHand = new JLabel();
		player = new JLabel();
        dealerHand = new JLabel();
		dealer = new JLabel();
        dealButton = new JButton("Deal");
        standButton = new JButton("Stand");
        hitButton = new JButton("Hit");

		// Set bounds for each component
        dealer.setBounds(50, 20, 100, 30);
        dealerHand.setBounds(50, 60, 400, 30);
        player.setBounds(50, 100, 100, 30);
        playerHand.setBounds(50, 140, 400, 30);
        dealButton.setBounds(200, 20, 100, 30);
        standButton.setBounds(200, 200, 100, 30);
        hitButton.setBounds(50, 200, 100, 30);

        // Set properties for each component
        dealer.setText("Dealer");
        dealer.setForeground(Color.white);
        dealer.setBackground(new Color(40, 119, 91, 100));

        dealerHand.setForeground(Color.white);
        dealerHand.setBackground(new Color(40, 119, 91, 100));
        // dealerHand.setText(this.model.getDealer().getHand().handToString());
		dealerHand.setText(" ");

        player.setText("Player");
        player.setForeground(Color.white);
        player.setBackground(new Color(40, 119, 91, 100));

        playerHand.setForeground(Color.white);
        playerHand.setBackground(new Color(40, 119, 91, 100));
        // playerHand.setText(this.model.getPlayers().get(0).getHand().handToString());
		playerHand.setText(" ");

        // Add components to the frame
        add(dealer);
        add(dealerHand);
        add(player);
        add(playerHand);
        add(dealButton);
        add(standButton);
        add(hitButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		dealerHand.setText(model.getDealer().getHand().handToString());
        playerHand.setText(model.getPlayers().get(0).getHand().handToString());
		revalidate();
        repaint();
		if ("Player lost".equals(evt.getPropertyName())){
			JOptionPane.showMessageDialog(this, "Player lost");
			this.model.dealCard();
		} else if ("Player won".equals(evt.getPropertyName())){
			JOptionPane.showMessageDialog(this, "Player won");
			this.model.dealCard();
		}
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

	public void disableHitButton() {
	    hitButton.setEnabled(false);
	}

	public void enableHitButton() {
	    hitButton.setEnabled(true);
	}
}
