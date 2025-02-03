public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;

        this.view.addDealButtonListener(e -> handleDealButton());
        this.view.addStandButtonListener(e -> handleStandButton());
        this.view.addHitButtonListener(e -> handleHitButton());
        this.view.addSplitButtonListener(e -> handleSplitButton());
        this.view.addForfeitButtonListener(e -> handleForfeitButton());
        this.view.addAddPlayerButtonListener(e -> handleAddPlayerButton());
    }

    private void handleDealButton(){
        if (this.view.isBetsPlaced()){
            this.model.reset();
            this.model.dealCard();
            this.model.addBetToPlayers(this.view.betsPlaced());
            this.view.updatePlayerLabel();
            this.view.disableAddPlayerButton();
            this.view.enableHitButton();
            this.view.enableStandButton();
            this.view.disableDealButton();
        }
    }

    private void handleStandButton(){
        this.model.stand();  
    }

    private void handleHitButton(){
        this.model.hit(this.model.getPlayers().get(this.model.getPlayerTurn()));
    }

    private void handleSplitButton(){
        if(this.model.getPlayers().get(this.model.getPlayerTurn()).getBankroll() >= this.model.getPlayers().get(this.model.getPlayerTurn()).getBet()){
            this.model.getPlayers().get(this.model.getPlayerTurn()).splitHand();
            this.model.getPlayers().get(this.model.getPlayerTurn()).getAllHands().forEach(hand -> {
                if (hand.size() == 1){
                    hand.add(this.model.getDeck().pickCard());
                }
            });
            this.view.addSplitHand(this.model.getPlayers().get(this.model.getPlayerTurn()));
            this.model.getPlayers().get(this.model.getPlayerTurn()).setBankroll(-this.model.getPlayers().get(this.model.getPlayerTurn()).getBet());
            this.view.disableSplitButton();
            this.view.disableForfeitButton();
        }
        else{
            this.view.showJOptionPane("You can split in spirit, but your bankroll says otherwise.");
            this.view.disableSplitButton();
        }
    }

    private void handleForfeitButton(){
        this.model.forfeit(this.model.getPlayers().get(this.model.getPlayerTurn()));
    }

    private void handleAddPlayerButton(){
        this.model.addPlayer();
    }
}

