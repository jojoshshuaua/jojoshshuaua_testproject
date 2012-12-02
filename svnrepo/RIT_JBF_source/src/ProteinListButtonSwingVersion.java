
/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;

public class ProteinListButtonSwingVersion extends JButton implements ActionListener {

    Electro2D electro2D;

    public ProteinListButtonSwingVersion(Electro2D e) {

        super("Display Protein List");
        electro2D = e;
        addActionListener(this);

    }

    public void actionPerformed (ActionEvent e) {

        electro2D.displayProtList();

    }

}
