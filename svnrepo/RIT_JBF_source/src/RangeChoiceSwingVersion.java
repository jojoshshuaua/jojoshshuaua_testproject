
/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;

public class RangeChoiceSwingVersion extends JComboBox implements ItemListener {

    Electro2D electro2D;

    public RangeChoiceSwingVersion(Electro2D e) {

        super();
        electro2D = e;
        addItemListener(this);
        (this).addItem("3 - 10");
        (this).addItem("4 - 7");
        (this).addItem("Enter a Range");

    }
    public void itemStateChanged( ItemEvent e ){

        if( getSelectedItem().equals( "Enter a Range" ) ){

            (this).setEditable(true);

	} else if ( getSelectedItem().equals(("4 - 7")) || getSelectedItem().equals(("3 - 10"))) {
            
            (this).setEditable(false);
            
        }

    }

}
