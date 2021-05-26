package minipoly;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class GUI_View extends Application {
    
    GUI_Controller ctrl;
    Model Bd;
    
    Label introMessage = new Label("Welcome to Minipoly.\nEnter player "
                + "names below.");
    Label P1Message = new Label("Player 1 Name:");
    Label P2Message = new Label("Player 2 Name:");
    Label cheatMessage = new Label("Tick to activate cheat mode");
    JLabel text = new JLabel("Enter desired roll:");
    
    TextField P1_Box = new TextField();
    TextField P2_Box = new TextField();
    JTextField roll = new JTextField();
    
    CheckBox cheatBox = new CheckBox();
    
    Button btn = new Button("Play");
    JButton submit = new JButton("Submit");
    JButton rollBtn = new JButton("Roll");
    JButton buyBtn = new JButton("Buy");
    JButton improBtn = new JButton("Improve");
    JButton endBtn = new JButton("End Turn");

    @Override
    public void start(Stage Stage) {
        //creates model and controller and assigns them
        Bd = new Model();
        ctrl = new GUI_Controller(Bd, this);
        //creates the initial window for setting up the game, allowing for the players to input their names and select cheat mode if desired
        btn.setOnAction((ActionEvent event) -> {
            boolean c = false;
            String P1_name;
            String P2_name;
            if (P1_Box.getText().isEmpty()) {
                P1_name = "A";
            } else {
                P1_name = P1_Box.getText();
            }
            if (P2_Box.getText().isEmpty()) {
                P2_name = "B";
            } else {
                P2_name = P2_Box.getText();
            }
            Player P1 = new Player(P1_name, 1);
            Player P2 = new Player(P2_name, 2);
            Bd.setPlayers(P1, P2);
            if (cheatBox.isSelected()) {
                c = true;
            }
            Stage.close();
            createGame(Bd, ctrl, c);
        });
        
        GridPane intro = new GridPane();
        intro.setPadding(new Insets(10, 10, 10, 10));
        intro.setHgap(10);
        
        HBox iMessage = new HBox(introMessage);
        intro.add(iMessage, 0, 0);
        VBox firstColumn = new VBox(10, P1Message, P1_Box, 
                cheatMessage, btn);
        intro.add(firstColumn, 0, 1);
        VBox secondColumn = new VBox(10, P2Message, P2_Box, cheatBox);
        intro.add(secondColumn, 1, 1);
                
        Scene introScene = new Scene(intro, 350, 180);
        Stage.setTitle("Minipoly");
        Stage.setScene(introScene);
        Stage.show();
    }
    
    //creation of the game
    public void createGame(Model Bd, GUI_Controller ctrl, boolean c) {
        //create properties, decide the player turn order and activates cheatmode if selected
        ctrl.getModel().createProperties();
        ctrl.getModel().decideOrder();
        if (c) {
            ctrl.getModel().setCheatMode(true);
        }
        //sets player position on the board
        ctrl.getModel().movePlayers();
        
        JFrame frame = new JFrame("Minipoly");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1270, 940);
        
        //creates the area for the board
        JPanel Board = new JPanel();
        Board.setBorder(new LineBorder(new Color(0, 0, 0)));
        Board.setLayout(null);
        
        //adds the display tile for each of the properties
        Bd.getProperties().forEach((p) -> {
            Board.add(p.getTile());
        });
        
        //creates the side panel for user info and buttons
        JPanel sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(350, 350));
        sideBar.setBorder(new LineBorder(new Color(0, 0, 0)));
        LayoutManager lm = new BoxLayout(sideBar, BoxLayout.Y_AXIS);
        sideBar.setLayout(lm);
        
        //info panel to describe the current player info and latest updates
        JPanel infoPanel = createInfoPanel(ctrl.getModel());
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        sideBar.add(infoPanel);
                
        //button to initiate roll, will automatically disable and activate buttons if applicable
        //if cheat mode activated then an input bar will appear below the roll button to input desired roll
        rollBtn.addActionListener((java.awt.event.ActionEvent ae) -> {
            double locType = 0;
            int rollVal = 0;
            if (ctrl.getModel().isCheatMode()) {
                if (!roll.getText().isEmpty()) {                 
                    try {
                        rollVal = Integer.parseInt(roll.getText());
                    } catch (NumberFormatException e) {
                        rollVal = 0;
                    }
                }
                locType = ctrl.playTurn(rollVal);
                rollBtn.setEnabled(false);
                endBtn.setEnabled(true);
                roll.setText("");
            } else {
                locType = ctrl.playTurn(0);
                rollBtn.setEnabled(false);
                endBtn.setEnabled(true);
            }
            if (locType == 1) {
                String extraInfo = ctrl.getModel().getCurrentPlayer().getName()
                        + " has been moved back to GO.";
                updateInfoPanel(infoPanel, ctrl, extraInfo);
            } else if (locType == 2) {
                String extraInfo = "You own this property.";
                updateInfoPanel(infoPanel, ctrl, extraInfo);
                Player P = ctrl.getModel().getCurrentPlayer();
                Property Prop = ctrl.getModel().getCurrentProperty(P);
                if (P.canImprove(Prop)) {
                    improBtn.setEnabled(true);
                }
            } else if (locType == 3) {
                String extraInfo = "This property is not owned.";
                updateInfoPanel(infoPanel, ctrl, extraInfo);
                Player P = ctrl.getModel().getCurrentPlayer();
                Property Prop = ctrl.getModel().getCurrentProperty(P);
                if (P.getBalance() > Prop.getValue()) {
                    buyBtn.setEnabled(true);
                }
            } else if (locType > 3) {
                Player O = ctrl.getModel().getOtherPlayer();
                String extraInfo = "You have paid " + O.getName()+ " £" + 
                        locType + " in rent.";
                updateInfoPanel(infoPanel, ctrl, extraInfo);
            }
        });
        //buy button to allow player to buy a property
        buyBtn.addActionListener((java.awt.event.ActionEvent ae) -> {
            buyBtn.setEnabled(false);
            Player P = ctrl.getModel().getCurrentPlayer();
            Property Prop = ctrl.getModel().getCurrentProperty(P);
            P.buyProperty(Prop);
            String extraInfo = "Property bought.";
            updateInfoPanel(infoPanel, ctrl, extraInfo);
            if (P.canImprove(Prop)) {
                improBtn.setEnabled(true);
            }
        }); 
        //improvement button to allow player to improve a property
        improBtn.addActionListener((java.awt.event.ActionEvent ae) -> {
            Player P = ctrl.getModel().getCurrentPlayer();
            Property Prop = ctrl.getModel().getCurrentProperty(P);
            P.improveProperty(Prop);
            if (!P.canImprove(Prop)) {
                improBtn.setEnabled(false);
            }
        });
        //end button to allow player to end their turn
        endBtn.addActionListener((java.awt.event.ActionEvent ae) -> {
            rollBtn.setEnabled(true);
            endBtn.setEnabled(false);
            buyBtn.setEnabled(false);
            improBtn.setEnabled(false);
            Player P = ctrl.getModel().getCurrentPlayer();
            Player O = ctrl.getModel().getOtherPlayer();
            P.setMyTurn(false);
            O.setMyTurn(true);
            if (P.getBalance() < 0) {
                String extraInfo = (O.getName() + " wins!");
                updateInfoPanel(infoPanel, ctrl, extraInfo);
                rollBtn.setEnabled(false);
            } else if (O.getBalance() < 0) {
                String extraInfo = (P.getName() + " wins!");
                updateInfoPanel(infoPanel, ctrl, extraInfo);
                rollBtn.setEnabled(false);
            } else {
                String extraInfo = "";
                updateInfoPanel(infoPanel, ctrl, extraInfo);
            }
        });
        
        rollBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        improBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        endBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        sideBar.add(rollBtn);
        if (ctrl.getModel().isCheatMode()) {
            roll.setPreferredSize(new Dimension(50, 25));
            roll.setMaximumSize(roll.getPreferredSize());
            sideBar.add(text);
            sideBar.add(roll);
        }
        sideBar.add(buyBtn);
        sideBar.add(improBtn);
        sideBar.add(endBtn);
        buyBtn.setEnabled(false);
        improBtn.setEnabled(false);
        endBtn.setEnabled(false);

        frame.add(Board, BorderLayout.CENTER);
        frame.add(sideBar, BorderLayout.EAST);
        
        frame.setVisible(true);
        
    }
    
    //infopanel to display player name, balance, properties, whos turn it is, and any of the latest updates
    public JPanel createInfoPanel(Model Bd) {
        Player P1 = Bd.getPlayers().get(0);
        Player P2 = Bd.getPlayers().get(1);
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        infoPanel.setBounds(50, 50, 250, 300);
        infoPanel.setBackground(Color.white);
        LayoutManager lm = new BoxLayout(infoPanel, BoxLayout.Y_AXIS);
        infoPanel.setLayout(lm);
        JLabel turn = new JLabel("It is " + Bd.getCurrentPlayer().getName()
                + "'s turn.");
        JLabel extraInfo = new JLabel("");
        JLabel P1_Name = new JLabel(P1.getName() + "'s info:");
        JLabel P1_Balance = new JLabel("Balance: £" + P1.getBalance());
        JLabel P1_Properties = new JLabel("Properties: ");
        JLabel P2_Name = new JLabel(P2.getName() + "'s info:");
        JLabel P2_Balance = new JLabel("Balance: £" + P2.getBalance());
        JLabel P2_Properties = new JLabel("Properties: ");
        infoPanel.add(turn);
        infoPanel.add(extraInfo);
        infoPanel.add(P1_Name);
        infoPanel.add(P1_Balance);
        infoPanel.add(P1_Properties);
        infoPanel.add(P2_Name);
        infoPanel.add(P2_Balance);
        infoPanel.add(P2_Properties);
        return infoPanel;
    }
    
    //update method for the info panel
    public void updateInfoPanel(JPanel infoPanel, GUI_Controller ctrl, String extraInfo) {
        Component turnText = infoPanel.getComponent(0);
        if (turnText instanceof JLabel) {
            ((JLabel) turnText).setText("It is " + 
                    ctrl.getModel().getCurrentPlayer().getName() + "'s turn.");
        }
        Component extraText = infoPanel.getComponent(1);
        if (extraText instanceof JLabel) {
            ((JLabel) extraText).setText(extraInfo);
        }
        Component p1bText = infoPanel.getComponent(3);
        if (p1bText instanceof JLabel) {
            ((JLabel) p1bText).setText("Balance: £"
                    + ctrl.getModel().getPlayers().get(0).getBalance());
        }
        Component p1pText = infoPanel.getComponent(4);
        String p1props = "Properties:";
        for (Property p : ctrl.getModel().getPlayers().get(0).getProperties()) { 
            p1props += (" " + p.getName());
        }
        if (p1pText instanceof JLabel) {
            ((JLabel) p1pText).setText(p1props);
        }
        Component p2bText = infoPanel.getComponent(6);
        if (p2bText instanceof JLabel) {
            ((JLabel) p2bText).setText("Balance: £"
                    + ctrl.getModel().getPlayers().get(1).getBalance());
        }
        Component p2pText = infoPanel.getComponent(7);
        String p2props = "Properties:";
        for (Property p : ctrl.getModel().getPlayers().get(1).getProperties()) { 
            p2props += (" " + p.getName());
        }
        if (p2pText instanceof JLabel) {
            ((JLabel) p2pText).setText(p2props);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}