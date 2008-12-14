import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
/**
 * Brings up the about page.
 */
public class AboutButton extends Canvas implements MouseListener {

    private Electro2D electro2D;
    private Color bgColor = Color.BLACK;
    private Color fillColorOff = Color.RED;
    private Color fillColorOn = new Color(255,165,0); //orange
    private boolean highlighted = false;  //changes when the cursor is placed
                                          //over the button
    private Color textColor = Color.white;
    private Font textFont = new Font("Arial", Font.BOLD, 14);
    private Rectangle d = null;  //dimensions of this component

    //double buffering variables
    private Image buffer = null;
    private Graphics bufferGraphics = null;

    /**
     * Constructor, performs some perfunctory tasks.
     */
    public AboutButton(Electro2D e) {
	//assign the Electro2D parameter to electro2D
	electro2D = e;
	//have the button register itself as capable to respond to 
	// mouse events
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
	
	//draw the button on the image buffer
	bufferGraphics.setColor(bgColor);
	bufferGraphics.fillRect(0, 0, d.width, d.height);

	//set the background color, depending on whether the mouse is
	// or is not over this object
	if(highlighted == true) {
	    bufferGraphics.setColor(fillColorOn);
	} else {
	    bufferGraphics.setColor(fillColorOff);
	}
	bufferGraphics.fillRoundRect(0, 0, d.width-1, d.height-1, 15, 15);
	bufferGraphics.setColor(textColor);
	bufferGraphics.setFont(textFont);
	bufferGraphics.drawString("About", 8, 14);

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
     * Responds to the cursor being placed over the button
     */
    public void mouseEntered(MouseEvent e) {
	// change the cursor image
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	//let paint know that the cursor is over the object
	highlighted = true;
	repaint();
	//Show information about the applet at the bottom of the screen
	//electro2D.showStatus("2-D Electrophoresis Simulation, 2003, Rochester "
	//	     + "Institute of Technology");
    }

    /**
     * Responds to the cursor being removed from over the button
     */
    public void mouseExited(MouseEvent e) {
	// change the cursor image
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	//let paint know that the cursor is not over the object
	highlighted = false;
	repaint();
	//Remove information displayed at the bottom of the screen
	//	electro2D.showStatus("");
    }
    /**
     * Responds to a mouse click
     */
    public void mouseClicked(MouseEvent e) {
	// Open up the about page
	electro2D.showAboutPage();
    }

}





