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
//public class SearchProteinButton extends Canvas implements MouseListener {
public class SearchProteinButton extends JButton implements ActionListener {

    private Electro2D electro2D;
    private String pro_id = "";  //the ID of the protein
    /**
     * Constructor, performs some perfunctory tasks.
     *
     * @param e - a reference to Electro2D
     * @param id - the string being searched for
     */
    public SearchProteinButton( Electro2D e, String id, String text) {
        super(text);
	electro2D = e; //give the button a reference to Electro2D
	pro_id = id; //set the search value to the string passed to the method
	this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        electro2D.showSearchPage(pro_id);
    }
}