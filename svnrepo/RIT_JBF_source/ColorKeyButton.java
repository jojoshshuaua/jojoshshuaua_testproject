import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
/**
 * Brings up the color key frame.
 * 
 * @author Jill Zapoticznyj
 */
public class ColorKeyButton extends Canvas implements MouseListener {

    private Color bgColor = Color.BLACK;
    //private Color bgColor = new Color(176,196,222); //light steel blue
    //private Color fillColorOff = new Color(54,100,139); //steel blue 4
    private Color fillColorOff = Color.RED;
    private Color fillColorOn = new Color(255,165,0); //orange
    private boolean highlighted = false;
    private Color textColor = Color.white;
    private Font textFont = new Font("Arial", Font.BOLD, 14);
    private Rectangle d = null;  //dimensions of this component
    
    private ColorFrame key;
    
    //double buffering variables
    private Image buffer = null;
    private Graphics bufferGraphics = null;

    /**
     * Constructor, performs some perfunctory tasks.
     */
    public ColorKeyButton() {
	//create an instance of the color key
	key = new ColorFrame();
	//have the button recognize itself as a mouse listener so it is able
	//to properly respond to mouse events
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
	
	//draw the object to the image buffer.
	bufferGraphics.setColor(bgColor);
	bufferGraphics.fillRect(0, 0, d.width, d.height);
	
	//change the color of the background depending on whether or
	//not the mouse is over the button
	if(highlighted == true) {
	    bufferGraphics.setColor(fillColorOn);
	} else {
	    bufferGraphics.setColor(fillColorOff);
	}
	bufferGraphics.fillRoundRect(0, 0, d.width-1, d.height-1, 15, 15);
	bufferGraphics.setColor(textColor);
	bufferGraphics.setFont(textFont);
	bufferGraphics.drawString("Color Key", 9, 14);

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
	//do nothing
    }
    public void mouseReleased(MouseEvent e) {
	//do nothing
    }
    
    /**
     * Responds to a mouse entering over the object
     */
    public void mouseEntered(MouseEvent e) {
	// change the cursor image
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	//let the paint method know the cursor is over the object
	highlighted = true;
	repaint();
	//display information at the bottom of the screen
	//
    }

    /**
     * Responds to a mouse exiting from over the object
     */
    public void mouseExited(MouseEvent e) {
	// change the cursor image
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	// let the paint method know the cursor has left from over the object
	highlighted = false;
	repaint();
	//remove the information that was being displayed at the bottom of the
	// screen
	//electro2D.showStatus("");
    }

    /**
     * Responds to a mouse click
     */
    public void mouseClicked(MouseEvent e) {
	//display the key
	key.showKey();
    }

}




