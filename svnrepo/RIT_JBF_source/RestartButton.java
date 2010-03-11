import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
/**
 * Restarts animation from beginning.
 *
 * @param Jill Zapoticznyj
 * @param Adam Bazinet
 */
public class RestartButton extends Canvas implements MouseListener {

    private Electro2D electro2D;
    private Color bgColor = Color.BLACK;
    private Color fillColorOff = Color.RED;
    private Color fillColorOn = new Color(255,165,0); //orange
    private int[] xPoints = { 13,21,6 }; //the x coords of part of the image
    private int[] yPoints = { 6,13,13 }; //the y coords of part of the image
    private boolean highlighted = false;
    private Color graphicColor = Color.white;
    private Rectangle d = null;  //dimensions of this component

    //double buffering variables
    private Image buffer = null;
    private Graphics bufferGraphics = null;

    /**
     * Constructor, performs some perfunctory tasks.
     *
     * @param e - a reference to Electro2D
     */
    public RestartButton(Electro2D e) {
	electro2D = e; //give the button a reference to Electro2D

	//have the button recognize itself as a MouseListener in order to
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
	bufferGraphics.fillPolygon(xPoints, yPoints, 3);
	bufferGraphics.fillRect(7,16,13,3);

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
     * Responds to the user placing the cursor over the button
     */
    public void mouseEntered(MouseEvent e) {
	//change the cursor image
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	highlighted = true; //register that the cursor is over the button
	//and redraw the image in order to reflect the change in bg color
	repaint();
	//display a message in the status bar to reflect the purpose of this 
	// button.
	//electro2D.showStatus("Restart the simulation.");
    }
    /**
     * Responds to the user moving the cursor from over the button
     */
    public void mouseExited(MouseEvent e) {
	// change the cursor image
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	highlighted = false; //register that the cursor is over the button
	//and redraw the image in order to reflect the change in bg color
	repaint();
	//remove the message from the status bar
	//electro2D.showStatus("");
    }
    /**
     * responds to a the user clicking on the button
     */
    public void mouseClicked(MouseEvent e) {

	GelCanvas g = electro2D.getGel(); //get a reference to GelCanvas
	g.clearIEF(); //clear the IEF space
	g.resetLocation();
	g.resetRanges();
	/*g.resetMW();*/
	g.clearCanvas();
	//reset the IEFProtein thread and information
	electro2D.resetIEF();
	IEFProteinSwingVersion.resetProtein();
	
	//if the dots have been drawn, clear them from the screen and stop the
	// thread
	if( ProteinDot.getShow() ){
	    ProteinDot.setShow();
	    electro2D.stopThread();
	}
	g.restartCanvas();

	//reset the animation values
	electro2D.resetPlay();
	electro2D.resetSdsStatus();
	electro2D.setBool();
	electro2D.clearpH();
	electro2D.setIEF();
	GelCanvas.setRed();
	GelCanvas.setGreen();
	GelCanvas.setBlue();
	IEFProtein.resetTempWidth();
	
    }
}
