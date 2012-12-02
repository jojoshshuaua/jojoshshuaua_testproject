import java.awt.*;
import java.awt.event.*;
/**
 * Removes highlighted proteins from the protein list.
 *
 * @author Adam Bazinet
 */
public class RemoveProteinButton extends Canvas implements MouseListener {

    private Electro2D electro2D;
    private Color bgColor = Color.BLACK;
    private Color fillColorOff = Color.RED;
    private Color fillColorOn = new Color(255,165,0); //orange
    private boolean highlighted = false;
    private Color textColor = Color.white;
    private Font textFont = new Font("Arial", Font.BOLD, 14);
    private Rectangle d = null;  //dimensions of this component

    //double buffering variables
    private Image buffer = null;
    private Graphics bufferGraphics = null;

    /**
     * Constructor, performs some perfunctory tasks.
     */
    public RemoveProteinButton(Electro2D e) {
	electro2D = e; //give the button a reference to Electro2D
	//have the button register itself as a MouseListener in order to
	// respond to mouse events from the user
	this.addMouseListener(this);
    }
    
    /**
     * Paints user controls.
     */
    public void paint(Graphics g) {

	//initialize buffering variables
	if(d == null || buffer == null || bufferGraphics == null) {
	    d = getBounds();
	    //set image size equal to component size
	    buffer = createImage(d.width, d.height);
	    bufferGraphics = buffer.getGraphics();
	}
	
	//draw the image to the buffer
	bufferGraphics.setColor(bgColor);
	bufferGraphics.fillRect(0, 0, d.width, d.height);
	//set the background color depending on whether or not the cursor
	//is over the button
	if(highlighted == true) {
	    bufferGraphics.setColor(fillColorOn);
	} else {
	    bufferGraphics.setColor(fillColorOff);
	}
	bufferGraphics.fillRoundRect(0, 0, d.width-1, d.height-1, 15, 15);
	bufferGraphics.setColor(textColor);
	bufferGraphics.setFont(textFont);
	bufferGraphics.drawString("Remove Proteins", 8, 14);

	//finally, draw image onto Graphics object
	g.drawImage(buffer, 0, 0, this);

    }
    public void update(Graphics g) {
	paint(g);
    }

    /**
     * Mouse listener methods. Used to respond to user button presses, etc.
     */
    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {
	//do nothing
    }
    /**
     * Responds to the cursor being placed over the button
     */
    public void mouseEntered(MouseEvent e) {
	//change the cursor image when it is placed over the button
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	highlighted = true; //register that the cursor is over the button
	//and repaint it in order to reflect the change in bg color
	repaint();
    }
    /**
     * Responds to the cursor being moved from over the button
     */
    public void mouseExited(MouseEvent e) {
	//change the cursor image when it is moved from over the button
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	highlighted = false; //register that the cursor is not over the button
	//and repaint it to reflect the change in the background color
	repaint();
    }
    /**
     * Responds to a mouse click
     */
    public void mouseClicked(MouseEvent e) {
	//remove the highlighted proteins from the list
	electro2D.removeHighlightedProteins();
    }

}
