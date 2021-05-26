package minipoly;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Model {
    private boolean needsDisplay = false;
    private boolean cheatMode = false;
    private ArrayList<Property> properties = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    
    //create all the properties on the board and their respective tiles (for the GUI)
    public void createProperties() {
        getProperties().add(new Property(10, 0, 10, "GO"));
        getProperties().add(new Property(0, 1, 0, "BACK"));
        getProperties().add(new Property(9, 50, 10, "A1"));
        getProperties().add(new Property(7, 50, 10, "A2"));
        getProperties().add(new Property(6, 70, 10, "A3"));
        getProperties().add(new Property(4, 100, 10, "B1"));
        getProperties().add(new Property(2, 100, 10, "B2"));
        getProperties().add(new Property(1, 120, 10, "B3"));
        getProperties().add(new Property(0, 150, 9, "C1"));
        getProperties().add(new Property(0, 150, 7, "C2"));
        getProperties().add(new Property(0, 170, 6, "C3"));
        getProperties().add(new Property(0, 200, 4, "D1"));
        getProperties().add(new Property(0, 200, 2, "D2"));
        getProperties().add(new Property(0, 220, 1, "D3"));
        getProperties().add(new Property(1, 250, 0, "E1"));
        getProperties().add(new Property(3, 250, 0, "E2"));
        getProperties().add(new Property(4, 270, 0, "E3"));
        getProperties().add(new Property(6, 300, 0, "F1"));
        getProperties().add(new Property(8, 300, 0, "F2"));
        getProperties().add(new Property(9, 320, 0, "F3"));
        getProperties().add(new Property(10, 350, 1, "G1"));
        getProperties().add(new Property(10, 350, 3, "G2"));
        getProperties().add(new Property(10, 370, 4, "G3"));
        getProperties().add(new Property(10, 400, 6, "H1"));
        getProperties().add(new Property(10, 400, 8, "H2"));
        getProperties().add(new Property(10, 420, 9, "H3"));
        getProperties().add(new Property(2, 0, 0, "EMPT"));
        getProperties().add(new Property(5, 0, 0, "EMPT"));
        getProperties().add(new Property(7, 0, 0, "EMPT"));
        getProperties().add(new Property(10, 0, 0, "EMPT"));
        getProperties().add(new Property(0, 0, 3, "EMPT"));
        getProperties().add(new Property(0, 0, 5, "EMPT"));
        getProperties().add(new Property(0, 0, 8, "EMPT"));
        getProperties().add(new Property(0, 0, 10, "EMPT"));
        getProperties().add(new Property(10, 0, 2, "EMPT"));
        getProperties().add(new Property(10, 0, 5, "EMPT"));
        getProperties().add(new Property(10, 0, 7, "EMPT"));
        getProperties().add(new Property(3, 0, 10, "EMPT"));
        getProperties().add(new Property(5, 0, 10, "EMPT"));
        getProperties().add(new Property(8, 0, 10, "EMPT"));
        for (Property p : properties) {
            Tile t = new Tile(p);
        }
    }
    
    //roll, move player, then return what type of property they are currently on
    public double playTurn_GUI(int r) {
        assert r >= 0: "r value not set";
        Player current = getCurrentPlayer();
        Player other = getOtherPlayer();
        int roll;
        Random rand = new Random();
        roll = rand.nextInt(12) + 1;
        if (r > 0) {
            roll = r;
        }
        assert roll != 0: "roll has not increased";
        current.movePlayer(roll);
        movePlayers();
        double locType = checkLocation_GUI(current, other);
        return locType;
    }
    
    //return what type of property they are currently on
    public double checkLocation_GUI(Player P, Player O) {
        assert P != null: "Current player does not exist";
        assert O != null: "Other player does not exist";
        int col = P.getColumn();
        int row = P.getRow();
        Property Prop = null;
        for (Property p : getProperties()) {
            if ((p.getColumn() == col) && (p.getRow() == row)) {
                Prop = p;
            }
        }
        assert Prop != null: "Property has not been assigned";
        if (Prop.getValue() == 1) {
            P.setColumn(10);
            P.setRow(10);
            movePlayers();
            return 1;
        } else if (Prop.getOwner() == P) {
            return 2;
        } else if (Prop.getOwner() == O) {
            double rent = payRent(P, O, Prop);
            return rent;
        } else if ((!Prop.isIsOwned()) && (Prop.getValue() != 0)) {
            if (P.getBalance() >= Prop.getValue()) {
                return 3;
            }
        }
        return 0;
    }
    
    //get the property that the player is currently on
    public Property getCurrentProperty(Player P) {
        assert P != null: "Current player does not exist";
        for (Property p : getProperties()) {
            if ((p.getColumn() == P.getColumn()) && (p.getRow() == P.getRow())) {
                return p;
            }
        }
        return null;
    }
    
    //update the players location and change any properties and tiles accordingly
    public void movePlayers(){
        Player p1 = players.get(0);
        Player p2 = players.get(1);
        for (Property p : getProperties()) {
            p.setOccupant(0);
            p.getTile().setOcupLabel(0, p1, p2);
        }
        int P1_pos = p1.getColumn();
        int P1_row = p1.getRow();
        int P2_pos = p2.getColumn();
        int P2_row = p2.getRow();
        if ((P1_pos == P2_pos) && (P1_row == P2_row)){
            for (Property p : getProperties()) {
                if (p.getRow() == P1_row) {
                    if (p.getColumn() == P1_pos) {
                        p.setOccupant(3);
                        p.getTile().setOcupLabel(3, p1, p2);
                    }
                }
            }
        } else {
            for (Property p : getProperties()) {
                if (p.getRow() == P1_row) {
                    if (p.getColumn() == P1_pos) {
                        p.setOccupant(1);
                        p.getTile().setOcupLabel(1, p1, p2);
                    }
                }
            }
            for (Property p : getProperties()) {
                if (p.getRow() == P2_row) {
                    if (p.getColumn() == P2_pos) {
                        p.setOccupant(2);
                        p.getTile().setOcupLabel(2, p1, p2);
                    }
                }
            }
        }
    }
    
    //randomly pick which player goes first
    public void decideOrder() {
        Player p1 = players.get(0);
        Player p2 = players.get(1);
        Random rand = new Random();
        int order = rand.nextInt(2) + 1;
        if (order == 1) {
            p1.setMyTurn(true);
        } else {
            p2.setMyTurn(true);
        }
    } 
        
    //get the currently playing player
    public Player getCurrentPlayer() {
        if (getPlayers().get(0).isMyTurn()) {
            return getPlayers().get(0);
        } else {
            return getPlayers().get(1);
        }
    }
    
    //get the player not currently playing
    public Player getOtherPlayer() {
        if (getPlayers().get(0).isMyTurn()) {
            return getPlayers().get(1);
        } else {
            return getPlayers().get(0);
        }
    }
    
    //automatically pay rent from one player to the other
    public double payRent(Player P, Player O, Property Prop) {
        assert P != null: "Current player does not exist";
        assert O != null: "Other player does not exist";
        assert Prop != null: "Property does not exist";
        double rent = Prop.calulateRent();
        assert rent > 0: "Rent cannot be zero";
        double pMoney = P.getBalance() - rent;
        P.setBalance(pMoney);
        double oMoney = O.getBalance() + rent;
        O.setBalance(oMoney);
        return rent;
    }
    
    //roll, move players, update the board, check location to initiate rent or property buying/improving, then switch whos turn it is
    public void playTurn_CLI(Scanner kb) {
        assert kb != null: "Scanner does not exist";
        Player current = getCurrentPlayer();
        Player other = getOtherPlayer();
        Random rand = new Random();
        int roll = rand.nextInt(12) + 1;
        if (isCheatMode()) {
            System.out.println("Enter desired dice roll (1-12) for " + 
                    current.getName() + ": "); 
            String rollText = kb.nextLine();
            if (!rollText.isEmpty()) {                 
                try {
                    roll = Integer.parseInt(rollText);
                } catch (NumberFormatException e) {}
            }  
        } else {
            System.out.println(current.getName() + " press enter to roll: ");
            kb.nextLine();
        }
        assert roll != 0: "roll has not increased";
        System.out.println(current.getName() + " rolled a " + roll + ".");
        current.movePlayer(roll);
        movePlayers();
        display_CLI();
        checkLocation_CLI(current, other, kb);
        current.setMyTurn(false);
        other.setMyTurn(true);
    }
    
    //check the current location of the current player. Activate different methods depending on what property the player is on
    public void checkLocation_CLI(Player P, Player O, Scanner kb) {
        assert P != null: "Current player does not exist";
        assert O != null: "Other player does not exist";
        assert kb != null: "scanner does not exist";
        int col = P.getColumn();
        int row = P.getRow();
        Property Prop = null;
        for (Property p : getProperties()) {
            if ((p.getColumn() == col) && (p.getRow() == row)) {
                Prop = p;
            }
        }
        assert Prop != null: "Property has not been assigned";
        if (Prop.getValue() == 1) {
            P.setColumn(10);
            P.setRow(10);
            System.out.println("You're in jail! You will start next turn at GO"
                    + ".");
        } else if (Prop.getOwner() == P) {
            ownerMenu_CLI(P, Prop, kb);
        } else if (Prop.getOwner() == O) {
            double rent = payRent(P, O, Prop);
            System.out.println(O.getName() + " owns this property.");
            System.out.println(P.getName() + " payed £" + rent + " in rent to " + 
                    O.getName() + ".");
            System.out.println(P.getName() + " new balance is: £" + P.getBalance());
            System.out.println(O.getName() + " new balance is: £" + O.getBalance());
        } else if ((!Prop.isIsOwned()) && (Prop.getValue() != 0)) {
            if (P.getBalance() >= Prop.getValue()) {
                System.out.println("This property is not owned.");
                buyerMenu_CLI(P, Prop, kb);
            }
        }
    }
    
    //displays the option to buy an unowned property and then buys it if selected
    public void buyerMenu_CLI(Player P, Property Prop, Scanner kb) {
        assert P != null: "Current player does not exist";
        assert Prop != null: "Property does not exist";
        assert kb != null: "scanner does not exist";
        int num = 2;
        System.out.println("It costs: £" + Prop.getValue() + ".");
        System.out.println("Enter 1 to buy the property or 2 to end your "
                + "turn: ");
        String option = kb.nextLine();
        if (!option.isEmpty()) {                 
            try {
                num = Integer.parseInt(option);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                buyerMenu_CLI(P, Prop, kb);
            }
        }  
        switch (num) {
            case 1:
                P.buyProperty(Prop);
                display_CLI();
                ownerMenu_CLI(P, Prop, kb);
                break;
            case 2:
                break;
            default:
                System.out.println("Invalid input.");
                buyerMenu_CLI(P, Prop, kb);
                break;
        }
    }
    
    //displays the option to improve a property and improves it if selected
    public void ownerMenu_CLI(Player P, Property Prop, Scanner kb) {
        assert P != null: "Current player does not exist";
        assert Prop != null: "Property does not exist";
        assert kb != null: "scanner does not exist";
        System.out.println("You own this property.");
        int imp = Prop.getImprovements();
        switch (imp) {
            case 0:
                System.out.println("This property has no improvements.");
                break;
            case 1:
                System.out.println("This property has 1 house.");
                break;
            case 2:
                System.out.println("This property has 2 houses.");
                break;
            case 3:
                System.out.println("This property has 3 houses.");
                break;
            case 4:
                System.out.println("This property has 4 houses.");
                break;
            case 5:
                System.out.println("This property has a hotel.");
                break;
        }
        if (P.canImprove(Prop)) {
            System.out.println("Enter 1 to improve the property or 2 to end "
                    + "your turn: ");
            int num = 2;
            String option = kb.nextLine();
            if (!option.isEmpty()) {                 
                try {
                    num = Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                    ownerMenu_CLI(P, Prop, kb);
                }
            }  
            switch (num) {
                case 1:
                    P.improveProperty(Prop);
                    display_CLI();
                    ownerMenu_CLI(P, Prop, kb);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Invalid input.");
                    ownerMenu_CLI(P, Prop, kb);
            }
        }
    }
    
    //displays the board and the player information
    public void display_CLI() {
        Player P1 = players.get(0);
        Player P2 = players.get(1);
        System.out.println(drawBoard_CLI(P1, P2));
        System.out.println(P1.getName() + " Balance: £" + P1.getBalance());
        String s1 = P1.getName() + " Properties: ";
        ArrayList<Property> P1_Prop = P1.getProperties();
        for (int i = 0; i < P1_Prop.size(); i++) {
            s1 += (P1_Prop.get(i).getName() + " ");
        }
        System.out.println(s1);
        System.out.println(P2.getName() + " Balance: £" + P2.getBalance());
        String s2 = P2.getName() + " Properties: ";
        ArrayList<Property> P2_Prop = P2.getProperties();
        for (int i = 0; i < P2_Prop.size(); i++) {
            s2 += (P2_Prop.get(i).getName() + " ");
        }
        System.out.println(s2);
    }
    
    //compiles the string to make up the board to be displayed later
    public String drawBoard_CLI(Player P1, Player P2){
        assert P1 != null: "Player1 does not exist";
        assert P2 != null: "Player2 does not exist";
        String board = "";
        for (int row = 0; row < 11; row++) {
            for (int i = 0; i < 11; i++) {
                if ((getName(i, row)).length() > 0){
                    board += "|" + getName(i, row) + "|";
                } else {
                    board += "      ";
                }
            }
            board += "\n";
            for (int i = 0; i < 11; i++) {
                if ((getName(i, row)).length() > 0){
                    board += "|" + getValue(i, row) + "|";
                } else {
                    board += "      ";
                }
            }
            board += "\n";
            for (int i = 0; i < 11; i++) {
                if ((getName(i, row)).length() > 0){
                    board += "|" + getImprovement(i, row) + "|";
                } else {
                    board += "      ";
                }
            }
            board += "\n";
            for (int i = 0; i < 11; i++) {
                if ((getName(i, row)).length() > 0){
                    board += "|" + getHousing(i, row, P1, P2) + "|";
                } else {
                    board += "      ";
                }
            }
            board += "\n";
        }
        assert board.length() == 2948: "board string not compiled correctly";
        return board;
    }
    
    //returns the name of the property with coordinates x y
    public String getName(int x, int y) {
        assert x >= 0: "x value to low";
        assert x < 12: "x value to high";
        assert y >= 0: "y value to low";
        assert y < 12: "y value to high";
        for (Property p : getProperties()) {
            if (p.getRow() == y) {
                if (p.getColumn() == x) {
                    String nam = p.getName();
                    if (nam.equalsIgnoreCase("BACK")) {
                        return p.getName();
                    } else if (nam.equalsIgnoreCase("EMPT")) {
                        return "    ";
                    } else {
                        return " " + p.getName() + " ";
                    }
                }
            }
        }
        return "";
    }
    
    //returns the value of the property with coordinates x y
    public String getValue(int x, int y) {
        assert x >= 0: "x value to low";
        assert x < 12: "x value to high";
        assert y >= 0: "y value to low";
        assert y < 12: "y value to high";
        for (Property p : getProperties()) {
            if (p.getRow() == y) {
                if (p.getColumn() == x) {
                    int val = p.getValue();
                    if (val <= 1) {
                        return "    ";
                    } else if ((val > 1) && (val < 100)) {
                        String v = " £" + Integer.toString(val);
                        return v;
                    } else {
                        String v = "£" + Integer.toString(val);
                        return v;
                    }
                }
            }
        }
        return "    ";
    }
    
    //returns the current improvements of the property with coordinates x y
    public String getImprovement(int x, int y){
        assert x >= 0: "x value to low";
        assert x < 12: "x value to high";
        assert y >= 0: "y value to low";
        assert y < 12: "y value to high";
        for (Property p : getProperties()) {
            if (p.getRow() == y) {
                if (p.getColumn() == x) {
                    int imp = p.getImprovements();
                    switch (imp) {
                        case 0:
                            return "    ";
                        case 1:
                            return "   h";
                        case 2:
                            return "  hh";
                        case 3:
                            return " hhh";
                        case 4:
                            return "hhhh";
                        case 5:
                            return "HOTL";
                    }
                }
            }
        }
        return "    ";
    }
    //returns the current occupants of the property with coordinates x y
    public String getHousing(int x, int y, Player P1, Player P2){
        assert x >= 0: "x value to low";
        assert x < 12: "x value to high";
        assert y >= 0: "y value to low";
        assert y < 12: "y value to high";
        assert P1 != null: "Player1 does not exist";
        assert P2 != null: "Player2 does not exist";
        char P1_Initial = P1.getName().charAt(0);
        char P2_Initial = P2.getName().charAt(0);
        for (Property p : getProperties()) {
            if (p.getRow() == y) {
                if (p.getColumn() == x) {
                    int oc = p.getOccupant();
                    switch (oc) {
                        case 0:
                            return " || ";
                        case 1:
                            return P1_Initial + "|| ";
                        case 2:
                            return " ||" + P2_Initial;
                        case 3:
                            return P1_Initial + "||" + P2_Initial;
                    }
                }
            }
        }
        return " || ";
    }
    
    //Model getters and setters

    /**
     * @return the properties
     */
    public ArrayList<Property> getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    /**
     * @return the needsDisplay
     */
    public boolean isNeedsDisplay() {
        return needsDisplay;
    }

    /**
     * @param needsDisplay the needsDisplay to set
     */
    public void setNeedsDisplay(boolean needsDisplay) {
        this.needsDisplay = needsDisplay;
    }

    /**
     * @return the cheatMode
     */
    public boolean isCheatMode() {
        return cheatMode;
    }

    /**
     * @param cheatMode the cheatMode to set
     */
    public void setCheatMode(boolean cheatMode) {
        this.cheatMode = cheatMode;
    }

    /**
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @param P1 the first Player to set
     * @param P2 the second Player to set
     */
    public void setPlayers(Player P1, Player P2) {
        players.clear();
        players.add(P1);
        players.add(P2);
    }
}