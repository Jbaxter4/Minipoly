package minipoly;

import java.util.ArrayList;

public class Property extends gameObject {
    private int value, improvements, occupant;
    private double rent;
    private Player owner;
    private char set;
    private boolean isOwned;
    private Tile tile;
    
    public Property(int col, int val, int row, String name){
        value = val;
        rent = val/10;
        set = name.charAt(0);
        improvements = 0;
        owner = null;
        occupant = 0;
        isOwned = false;
        tile = null;
        setColumn(col);
        setRow(row);
        setName(name);
    }
    
    //calulate and return the rent value for the property 
    public double calulateRent() {
        if (isIsOwned()) {
            int count = 0;
            ArrayList<Property> properties = getOwner().getProperties();
            for (Property p : properties) {
                if (p.getSet() == getSet()) {
                    count++;
                }
            }
            if (count == 3) {
                if (getImprovements() == 0) {
                    return (getValue()/5);
                } else if (getImprovements() == 5) {
                    double r = ((getValue()/5) + ((2*getValue()) + 
                            (0.8*getValue()))/10);
                    return r;
                } else if ((getImprovements() > 0) && (getImprovements() < 5)) {
                    double r = ((getValue()/5) + 
                            ((getImprovements()*0.5*getValue())/10));
                    return r;
                }
            } else {
                return (getValue()/10);
            }
        }
        return (getValue()/10);
    }
    
    //calulate and return the improvement cost for the property
    public double improvementCost() {
        int i = getImprovements();
        if (i < 4) {
            return getValue()*0.5;
        } else {
            return getValue()*0.8;
        }
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * @return the improvements
     */
    public int getImprovements() {
        return improvements;
    }

    /**
     * @param improvements the improvements to set
     */
    public void setImprovements(int improvements) {
        this.improvements = improvements;
        this.tile.setImproLabel(improvements);
    }

    /**
     * @return the rent
     */
    public double getRent() {
        return rent;
    }

    /**
     * @param rent the rent to set
     */
    public void setRent(double rent) {
        this.rent = rent;
    }

    /**
     * @return the owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * @return the set
     */
    public char getSet() {
        return set;
    }

    /**
     * @param set the set to set
     */
    public void setSet(char set) {
        this.set = set;
    }
    
    /**
     * @return the occupant
     */
    public int getOccupant() {
        return occupant;
    }

    /**
     * @param occupant the occupant to set
     */
    public void setOccupant(int occupant) {
        this.occupant = occupant;
    }

    /**
     * @return the isOwned
     */
    public boolean isIsOwned() {
        return isOwned;
    }

    /**
     * @param isOwned the isOwned to set
     */
    public void setIsOwned(boolean isOwned) {
        this.isOwned = isOwned;
    }

    /**
     * @return the tile
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * @param tile the tile to set
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
