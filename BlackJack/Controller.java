public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;

        this.view.addDealButtonListener(e -> handleDealButton());
        this.view.addStandButtonListener(e -> handleStandButton());
        this.view.addHitButtonListener(e -> handleHitButton());
            
    }

    private void handleDealButton(){
        this.model.dealCard();
        this.view.enableHitButton();
    }

    private void handleStandButton(){
        this.view.disableHitButton();
        this.model.stand();
    }

    private void handleHitButton(){
        this.model.hit(this.model.getPlayers().get(0));
    }
}

