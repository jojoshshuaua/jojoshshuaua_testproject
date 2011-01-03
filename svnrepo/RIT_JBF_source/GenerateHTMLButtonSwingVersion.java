
/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;

public class GenerateHTMLButtonSwingVersion extends JButton implements ActionListener {

    Electro2D electro2D;

    public GenerateHTMLButtonSwingVersion (Electro2D e) {

        super("Generate HTML Page");
        electro2D = e;
        addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {

        new HTMLGenScreen(electro2D);

    }

}
