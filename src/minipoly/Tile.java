package minipoly;

import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Tile extends JPanel {
    
    private Property Prop;
    private JLabel nameLabel, valueLabel, improLabel, ocupLabel;
    
    public Tile(Property P) {
        Prop = P;
        P.setTile(this);
        nameLabel = new JLabel("");
        valueLabel = new JLabel("");
        improLabel = new JLabel("       ");
        ocupLabel = new JLabel("       ");
        setBorder(new LineBorder(new Color(0, 0, 0)));
        int x = 10 + (Prop.getColumn() * 80);
        int y = 10 + (Prop.getRow() * 80);
        setBounds(x, y, 80, 80);
        LayoutManager lm = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(lm);
        if (!(Prop.getName().equalsIgnoreCase("EMPT"))) {
            nameLabel.setText(Prop.getName());
        }
        if (Prop.getValue() > 1) {
            valueLabel.setText("£" + Prop.getValue());
        }
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);
        improLabel.setAlignmentX(CENTER_ALIGNMENT);
        ocupLabel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(nameLabel);
        this.add(valueLabel);
        this.add(improLabel);
        this.add(ocupLabel);
    }

    //sets the display for the current improvements made to this property
    public void setImproLabel(int i) {
        switch (i) {
            case 1:
                improLabel.setText("      H");
                break;
            case 2:
                improLabel.setText("    H H");
                break;
            case 3:
                improLabel.setText("  H H H");
                break;
            case 4:
                improLabel.setText("H H H H");
                break;
            case 5:
                improLabel.setText("HOTEL");
                break;
        }
    }
    
    //sets the display for the current occupants for this property
    public void setOcupLabel(int i, Player P1, Player P2) {
        char initial_p1 = P1.getName().charAt(0);
        char initial_p2 = P2.getName().charAt(0);
        switch (i) {
            case 0:
                ocupLabel.setText("       ");
                break;
            case 1:
                ocupLabel.setText(" " + initial_p1 + "     ");
                break;
            case 2:
                ocupLabel.setText("     " + initial_p2 + " ");
                break;
            case 3:
                ocupLabel.setText(" " + initial_p1 + "   " + initial_p2 + " ");
                break;
        }
    }
}
