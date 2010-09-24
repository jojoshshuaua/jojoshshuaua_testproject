/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;

public class AddProteinButtonSwingVersion extends JButton implements ActionListener {
    Electro2D electro2D;

    public AddProteinButtonSwingVersion (Electro2D e) {

        super("Add Proteins");
        addActionListener(this);
        electro2D = e;
}

    public void actionPerformed (ActionEvent e) {

        electro2D.getSequenceData();

    }
}
