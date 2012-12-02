
/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;

public class CompareProteinsButtonSwingVersion extends JButton implements ActionListener {

    Electro2D electro2D;

    public CompareProteinsButtonSwingVersion(Electro2D e) {

        super("Compare Proteins");
        electro2D = e;
        addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {

	electro2D.getSequenceData2();
	PlayButtonSwingVersion.setCompare(true);

    }
}
