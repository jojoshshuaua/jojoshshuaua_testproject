
/**
 * Performs a BLAST search for the protein sequence given to it from
 * ProteinFrame.
 *
 * @author Jill Zapoticznyj
 *
 */
import javax.swing.*;
import java.awt.event.*;

//public class BlastSearchButton extends Canvas implements MouseListener {
public class BlastSearchButton extends JButton implements ActionListener {

    private Electro2D electro2D;
/**    private Color bgColor = Color.BLACK;
    private Color fillColorOff = Color.RED;
    private Color fillColorOn = new Color(255,165,0); //orange
    private boolean highlighted = false;
    private Color textColor = Color.white;
    private Font textFont = new Font("Arial", Font.BOLD, 14);
    private Rectangle d = null;  //dimensions of this component
**/    private String pro_sequence = "";  //the sequence of the protein

    //double buffering variables
//    private Image buffer = null;
//    private Graphics bufferGraphics = null;

    /**
     * Constructor, performs some perfunctory tasks.
     *
     * @param e - a reference to Electro2D
     * @param id - the string being searched for
     */
    public BlastSearchButton( Electro2D e, String seq ) {
        super("Blast Search");
	electro2D = e; //give the button a reference to Electro2D
	pro_sequence = seq; //set the search value to the string passed to the method
	//have the button register itself as a MouseListener in order to
	//respond to mouse events from the user
	this.addActionListener(this);
    }

    
    /**
     * Paints user controls.
     */
/**    public void paint(Graphics g) {

	//initialize buffering variables
	if(d == null || buffer == null || bufferGraphics == null) {
	    d = getBounds();
	    //set image size equal to component size
	    buffer = createImage(d.width, d.height);
	    bufferGraphics = buffer.getGraphics();
	}
	
	//draw to the buffer
	bufferGraphics.setColor(bgColor);
	bufferGraphics.fillRect(0, 0, d.width, d.height);
	// change the background color depending on the location of the cursor
	
	if(highlighted == true) {
	    bufferGraphics.setColor(fillColorOn);
	} else {
	    bufferGraphics.setColor(fillColorOff);
	}
	bufferGraphics.fillRoundRect(0, 0, d.width-1, d.height-1, 15, 15);
	bufferGraphics.setColor(textColor);
	bufferGraphics.setFont(textFont);
	bufferGraphics.drawString("BLAST Search", 8, 14);

	//finally, draw image onto Graphics object
	g.drawImage(buffer, 0, 0, this);

    }
    public void update(Graphics g) {
	paint(g);
    }
/**
    /**
     * Mouse listener methods. Used to respond to user button presses, etc.
     */
/**    public void mousePressed(MouseEvent e) {
	//do nothing
    }
    public void mouseReleased(MouseEvent e) {
	//do nothing
    }
**/
    /**
     * Responds to the cursor being placed over this button.
     */
/**    public void mouseEntered(MouseEvent e) {
	//change the image of the cursor
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	highlighted = true; //register that the cursor is over the button
	//and redraw the button to reflect the change in bg color
	repaint();
	//display a message in the status bar reflecting the purpose of this
	// button.
	//electro2D.showStatus("Perform a Blast search for this protein " +
	//	     "sequence.");
    }
/**
    /**
     * Responds to the mouse being removed from over the button.
     */
/**    public void mouseExited(MouseEvent e) {
	//change the image of the cursor
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	highlighted = false; //register that the cursor is not over the button
	//and redraw the button to reflect the change in bg color
	repaint();
	//remove the message from the status bar
	//electro2D.showStatus("");
    }
**/
    /**
     * Responds to the user clicking on the button.
     */
/**    public void mouseClicked(MouseEvent e) {
	//open the search page and perform the search for the protein
	electro2D.showBlastSearchPage( pro_sequence );
    }
**/
    public void actionPerformed(ActionEvent e) {
        electro2D.showBlastSearchPage(pro_sequence);
    }

}
