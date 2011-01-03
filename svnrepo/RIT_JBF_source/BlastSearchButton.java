
/**
 * Performs a BLAST search for the protein sequence given to it from
 * ProteinFrame.
 *
 * @author Jill Zapoticznyj
 *
 */
import javax.swing.*;
import java.awt.event.*;

public class BlastSearchButton extends JButton implements ActionListener {

    private Electro2D electro2D;
    private String pro_sequence = "";

    /**
     * Constructor, performs some perfunctory tasks.
     *
     * @param e - a reference to Electro2D
     * @param id - the string being searched for
     */
    public BlastSearchButton( Electro2D e, String seq ) {
        super("Blast Search");
	electro2D = e;
	pro_sequence = seq;
	this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        electro2D.showBlastSearchPage(pro_sequence);
    }

}
