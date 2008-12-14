import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.io.*;
/**
 * Stops the animation.
 *
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 */
public class StopButton extends Canvas implements MouseListener {

    private Electro2D electro2D;
    private Color bgColor = Color.BLACK;
    // private Color bgColor = new Color(176,196,222); //light steel blue
    //private Color fillColorOff = new Color(54,100,139); //steel blue 4
    private Color fillColorOff = Color.RED;
    private Color fillColorOn = new Color(255,165,0); //orange
    private boolean highlighted = false;
    private Color graphicColor = Color.white;
    private Rectangle d = null;  //dimensions of this component

    //double buffering variables
    private Image buffer = null;
    private Graphics bufferGraphics = null;

    /**
     * Constructor, performs some perfunctory tasks.
     *
     * @param e - a reference to the Electro2D class
     */
    public StopButton(Electro2D e) {
	electro2D = e; //give the button a reference to Electro2D
	//have the button register itself as a mouse listener in order to 
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
	
	//draw to the buffer
	bufferGraphics.setColor(bgColor);
	bufferGraphics.fillRect(0, 0, d.width, d.height);
	
	//change the background color depending on the location of the cursor
	if(highlighted == true) {
	    bufferGraphics.setColor(fillColorOn);
	} else {
	    bufferGraphics.setColor(fillColorOff);
	}
	bufferGraphics.fillOval(0, 0, d.width, d.height);
	bufferGraphics.setColor(graphicColor);
	bufferGraphics.fillRect(9,9,9,9);  //draw stop graphic

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
     * Responds to the cursor being placed over this button
     */
    public void mouseEntered(MouseEvent e) {
	//change the cursor image
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	highlighted = true; //register that the cursor is over the button
	//and redraw the image to reflect the change in bg color
	repaint();
	//display a message in the status bar reflecting the purpose of this
	//button
	//electro2D.showStatus("Stop the simulation.");
    }

    /**
     * Responds to the cursor being removed from over the button
     */
    public void mouseExited(MouseEvent e) {
	//change the cursor image
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	highlighted = false; //register that the cursor is not over the button
	//and redraw the image to reflect the change in background color
	repaint();
	//remove the message from the status bar
	//electro2D.showStatus("");
    }

    /**
     * Responds to the user clicking on this button
     */
    public void mouseClicked(MouseEvent e) {
	// stop the animation thread
	electro2D.stopThread();
	electro2D.resetPlay();
	    
    }

}
