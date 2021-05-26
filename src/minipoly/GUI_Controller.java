package minipoly;

public class GUI_Controller {

    private Model model;
    private GUI_View view;
    
    public GUI_Controller(Model model, GUI_View view){
        this.model = model;
        this.view = view;
    }

    /**
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * @return the view
     */
    public GUI_View getView() {
        return view;
    }
    
    /**
     * @param view the view to set
     */
    public void setView(GUI_View view){
        this.view = view;
    }
    
    //play turn for the GUI taking in a roll value if cheat mode activated, and returning a value to interpret what type of property the player is located on
    public double playTurn(int roll) {
        double locType = model.playTurn_GUI(roll);
        return locType;
    }
}