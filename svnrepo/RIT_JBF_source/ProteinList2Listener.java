/**
 * ProteinList2Listener.java
 * An actionListener for the proteinList2 object in Electro2D
 *
 * @author Jill Zapoticznyj
 */

import java.awt.*;
import java.awt.event.*;

public class ProteinList2Listener implements ActionListener{
    
    private Electro2D electro2D;
    
    public ProteinList2Listener( Electro2D e ){
	electro2D = e;
    }
  
    /**
     *  Responds to a double-click in the protein list by bringing up a 
     *  new window with information on that protein.
     */
    public void actionPerformed(ActionEvent e) {
	//set up a new frame, give it ref. to the applet and the protein name
	ProteinFrame proteinFrame = new ProteinFrame(electro2D,
						     e.getActionCommand(), 2);
	proteinFrame.setResizable(false);
	proteinFrame.show();
    }
    
}
