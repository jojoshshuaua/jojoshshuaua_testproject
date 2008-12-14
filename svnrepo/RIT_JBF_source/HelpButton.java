import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
/**
 * Brings up the file loader frame.
 */
public class HelpButton extends Canvas implements MouseListener {

    private Electro2D electro2D;
    private Color bgColor = Color.BLACK;
    //private Color bgColor = new Color(176,196,222); //light steel blue
    //private Color fillColorOff = new Color(54,100,139); //steel blue 4
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
    public HelpButton(Electro2D e) {
	
	// assigns electro2D the value of the parameter passed to this method
	electro2D = e;
	//let the button register itself as a mouseListener to respond to
	// mouse clicks
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
	
	bufferGraphics.setColor(bgColor);
	bufferGraphics.fillRect(0, 0, d.width, d.height);
	
	//set the color of the background depending on whether or not
	// the cursor is over this object
	if(highlighted == true) {
	    bufferGraphics.setColor(fillColorOn);
	} else {
	    bufferGraphics.setColor(fillColorOff);
	}
	bufferGraphics.fillRoundRect(0, 0, d.width-1, d.height-1, 15, 15);
	bufferGraphics.setColor(textColor);
	bufferGraphics.setFont(textFont);
	bufferGraphics.drawString("Help", 8, 14);

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

    public void mouseEntered(MouseEvent e) {
	//change the image of the cursor when the mouse is placed 
	// over this object
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	highlighted = true;
	repaint();
	//display a message to the user
	//electro2D.showStatus("Need help?");
    }
    public void mouseExited(MouseEvent e) {
	//change the image of the cursor when the mouse is removed 
	// from over this object
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	highlighted = false;
	repaint();
	// hide the message in the status bar
	//electro2D.showStatus("");
    }
    public void mouseClicked(MouseEvent e) {
	//open the help page
	electro2D.showHelpPage();
    }

}
