
/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;

public class StopButtonSwingVersion extends JButton implements ActionListener {

    Electro2D electro2D;

    public StopButtonSwingVersion(Electro2D e) {
        super("Stop");
        addActionListener(this);
        electro2D = e;
    }

    public void actionPerformed(ActionEvent e) {
        electro2D.stopThread();
        electro2D.resetPlay();
    }
}
