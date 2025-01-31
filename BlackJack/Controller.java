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
        this.model.reset();
        this.model.dealCard();
        this.view.disableAddPlayerButton();
    }

    private void handleStandButton(){
            this.model.stand();
    }

    private void handleHitButton(){
        this.model.hit(this.model.getPlayers().get(this.model.getPlayerTurn()));
    }

    private void handleSplitButton(){
        this.model.getPlayers().get(this.model.getPlayerTurn()).splitHand();
        this.model.getPlayers().get(this.model.getPlayerTurn()).getAllHands().forEach(hand -> {
            if (hand.size() == 1){
                hand.add(this.model.getDeck().pickCard());
            }
        });
        this.view.addSplitHand(this.model.getPlayers().get(this.model.getPlayerTurn()));
        this.view.disableSplitButton();
    }

    private void handleForfeitButton(){
        System.out.println("Forfeit not yet implemented");
    }

    private void handleAddPlayerButton(){
        this.model.addPlayer();
    }
}

