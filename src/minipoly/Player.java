package minipoly;

import static java.lang.Math.abs;
import java.util.ArrayList;

public class Player extends gameObject {
    private int number;
    private double balance;
    private boolean myTurn;
    private ArrayList<Property> properties = new ArrayList<>();
    
    public Player(String n, int num){
        balance = 100;
        myTurn = false;
        number = num;
        
        setColumn(10);
        setRow(10);
        setName(n);
    }
    
    //set the players new location values (row and column)
    public void movePlayer(int roll) {
        int newCol;
        int newRow; 
        if (getRow() == 10) {
            newCol = getColumn() - roll;
            if (newCol < 0) {
                newRow = getRow() + newCol;
                if (newRow < 0) {
                    setRow(0);
                    setColumn(abs(newRow));
                } else {
                    setRow(newRow);
                    setColumn(0);
                }
            } else {
                setColumn(newCol);
            }       
        } else if (getRow() == 0) {
            newCol = getColumn() + roll;
            if (newCol > 10) {
                newRow = getRow() + (newCol - 10);
                if (newRow > 10) {
                    setRow(10);
                    setColumn(newRow - 10);
                } else {
                    setRow(newRow);
                    setColumn(10);
                }
            } else {
                setColumn(newCol);
            }
        } else if ((getColumn() == 0) && ((getRow() != 0) || 
                (getRow() != 10) )) {
            newRow = getRow() - roll;
            if (newRow < 0) {
                newCol = getColumn() - newRow;
                if (newCol > 10) {
                    setColumn(10);
                    setRow(newRow - 10);
                } else {
                    setRow(0);
                    setColumn(newCol);
                }
            } else {
                setRow(newRow);
            }
        } else if ((getColumn() == 10) && ((getRow() != 0) || 
                (getRow() != 10) )) {
            newRow = getRow() + roll;
            if (newRow > 10) {
                newCol = getColumn() - (newRow - 10);
                if (newCol < 0) {
                    setColumn(0);
                    setRow(abs(newCol));
                } else {
                    setRow(10);
                    setColumn(newCol);
                }
            } else {
                setRow(newRow);
            }
        }
    }
    
    //checks to see if the player can improve the property
    public boolean canImprove(Property Prop) {
        if (Prop.getImprovements() == 5) {
            return false;
        }
        if (Prop.improvementCost() > getBalance()) {
            return false;
        }
        char set = Prop.getSet();
        int count = 0; 
        for (Property p : getProperties()) {
            if (p.getSet() == set) {
                count++;
            }
        }
        if (count != 3) {
            return false;
        }
        return true;
    }
    
    //improves the property, taking the cost from their balance
    public void improveProperty(Property Prop) {
        double newBalance = getBalance() - Prop.improvementCost();
        setBalance(newBalance);
        int newImp = Prop.getImprovements() + 1;
        Prop.setImprovements(newImp);
    }
        
    //buys the property, taking the cost from their balance and assigning the property to that player
    public void buyProperty(Property Prop) {
        double newBalance = getBalance() - Prop.getValue();
        setBalance(newBalance);
        getProperties().add(Prop);
        Prop.setIsOwned(true);
        Prop.setOwner(this);
    }

    /**
     * @return the money
     */
    public double getBalance() {
        return balance;
    }

    /**
     * @param balance the money to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * @return the myTurn
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * @param myTurn the myTurn to set
     */
    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

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
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }
}