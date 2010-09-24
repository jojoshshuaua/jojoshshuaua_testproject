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

public class SearchProteinFieldButtonSwingVersion extends JButton implements ActionListener {

    Electro2D electro2D;
    SearchProteinFunction spf;

    public SearchProteinFieldButtonSwingVersion(Electro2D e) {

        super("Search Protein Field");
        electro2D = e;
        addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {

        spf = new SearchProteinFunction(electro2D);

    }

}
