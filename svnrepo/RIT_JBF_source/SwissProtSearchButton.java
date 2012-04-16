import javax.swing.*;
import java.awt.event.*;
/**
 * Removes highlighted proteins from the protein list.
 *
 * @author Jill Zapoticznyj
 *
 * Much of this code is based off of the RemoveProteinButton class
 * @author Adam Bazinet
 *
 */
public class SwissProtSearchButton extends JButton implements ActionListener {

    private Electro2D electro2D;
    private String pro_id = "";  //the ID of the protein

    /**
     * Constructor, performs some perfunctory tasks.
     *
     * @param e - a reference to Electro2D
     * @param id - the string being searched for
     */
    public SwissProtSearchButton( Electro2D e, String id, String text) {
        super(text);
	electro2D = e; //give the button a reference to Electro2D
	pro_id = id; //set the search value to the string passed to the method
	//have the button register itself as a MouseListener in order to
	//respond to mouse events from the user
	this.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        electro2D.showSwsSearchPage(pro_id);
    }
}
