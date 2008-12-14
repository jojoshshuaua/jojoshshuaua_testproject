import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
/**
 * The button in charge of starting and stopping the threads for the animation.
 *
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 */
public class PlayButton extends Canvas implements MouseListener {

    private Electro2D electro2D; //reference to main applet class
    private Color bgColor = Color.BLACK;
    //private Color bgColor = new Color(176,196,222); //light steel blue
    //private Color fillColorOff = new Color(54,100,139); //steel blue 4
    private Color fillColorOff = Color.RED;
    private Color fillColorOn = new Color(255,165,0); //orange
    private boolean highlighted = false; 
    private boolean playing = false; //controls the pausing of the animation
    private Color graphicColor = Color.white;
    private int[] xPoints = { 12,23,12 }; //the x coords of part of the image
    private int[] yPoints = { 9,16,23 }; // the y coords of part of the image
    private Rectangle d = null;  //dimensions of this component
    private boolean iefDrawn = false; 
    private static boolean compareFiles = false;
    private boolean sdsDrawn = false;
    //double buffering variables
    private Image buffer = null; 
    private Graphics bufferGraphics = null;

    /**
     * Constructor, performs some perfunctory tasks.
     */
    public PlayButton(Electro2D e) {
	//assign electro2D the value passed as a parameter to this method
	electro2D = e;
	// have PlayButton register itself as a MouseListener in order to
	// respond to mouse events.
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
	
	//set the background color depending on the location of the cursor
	if(highlighted == true) {
	    bufferGraphics.setColor(fillColorOn);
	} else {
	    bufferGraphics.setColor(fillColorOff);
	}
	bufferGraphics.fillOval(0, 0, d.width-1, d.height-1);
	bufferGraphics.setColor(graphicColor);
	if(playing == false) { //show play graphic
	    //draw triangle
	    bufferGraphics.fillPolygon(xPoints,yPoints,3);
	} else { //show pause graphic
	    bufferGraphics.fillRect(10,10,4,13);
	    bufferGraphics.fillRect(19,10,4,13);
	}

	//finally, draw image onto Graphics object
	g.drawImage(buffer, 0, 0, this);

    }
    public void update(Graphics g) {
	paint(g);
    }

    /**
     * This method returns whether or not the SDS-PAGE portion of the
     * animation has been drawn.  
     */
    public boolean getSdsStatus(){
	return sdsDrawn;
    }

    public void resetSdsStatus(){
	sdsDrawn = false;
    }

    /**
     * This method is called when the restart button is pressed.  
     * It sets iefDrawn to false so the play button knows which thread
     * to start.
     */
    public void resetIEF(){
	iefDrawn = false;
    }
    
    /**
     * Returns the state of the animation
     *
     * @return playing
     */
    public boolean getPlay(){
	return playing;
    }

    /**
     * Sets playing to false when the pause button is pressed in order
     * to stop the animation.
     */
    public void resetPlay(){
	playing = false;
	repaint();
    }

    public static void setCompare(){
	compareFiles = true;
    }

    public static void resetCompare(){
	compareFiles = false;
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
     * Changes the image of the cursor and displays a message to the user
     * in the status bar if the cursor is placed over the button.
     */
    public void mouseEntered(MouseEvent e) {
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	highlighted = true; //the cursor is on the object
	//repaint the object to register the bg color change
	repaint();
	//depending on the status of the animation, display the function
	// of this button in the status bar
	if(playing == true) {
	    // electro2D.showStatus("Pause the simulation.");
	} else {
	    // electro2D.showStatus("Start the simulation.");
	}
    }
    /**
     * Changes the image of the cursor and removes the message displayed in the
     * status bar if the cursor is moved off of the button
     */
    public void mouseExited(MouseEvent e) {
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	highlighted = false; //the cursor is no longer on the object
	//repaint the object to register the bg color change
	repaint();
	//remove the message from the status bar
	//electro2D.showStatus("");
    }
    /**
     * Start or stop certain parts of the animation when the user clicks
     * on this object.
     */
    public void mouseClicked(MouseEvent e) {
	
	//start or pause animation
	//if the animation is playing, stop it
	if(playing == true) {
	    playing = false;
	    electro2D.stopThread();
	    repaint();
	} 
	// otherwise determine which parts of the animation need to be drawn
	// and start the appropriate thread.
	else {
	    //get the animation the user wishes to see
	    String choice = electro2D.getAnimationChoice();
	    if( electro2D.getSequences().size() > 0 ){
		
		//if the data for the animation needs to be processed,
		//do so
		//if((( electro2D.getGel().getDots().size() == 0 ) ||
		//  ( electro2D.getBool() ))){
		    if( choice.equals( "IEF" )){
			electro2D.getGel().prepare();
			electro2D.resetBool();
			if( compareFiles ){
			    electro2D.getGel().prepare2();
			}
		    }
		    //}
		
		//if the user selected IEF animation and the image is not
		// already displayed on the screen, perform the IEF
		// animation
		if( choice.equals( "IEF") ){
		    if( iefDrawn == false ){
			electro2D.restartIEF();
			iefDrawn = true;
			electro2D.getGel().resetReLine();
		    }
		}
		//if the user selected SDS-PAGE animation, and the IEF is
		// already drawn, perform the SDS-PAGE animation
		else if( choice.equals( "SDS-PAGE" )){
		    if( iefDrawn == true ){
			/*electro2D.getGel().resetWriteRanges();*/
			electro2D.getGel().clearCanvas();
			electro2D.getGel().resetLocation();
			playing = true; 
			electro2D.restartThread();
			repaint();
			/*electro2D.getGel().resetDrawMW();*/
			//electro2D.getGel().resetReLine();
			sdsDrawn = true;
		    }
		}
	    }
	}
    }
}





