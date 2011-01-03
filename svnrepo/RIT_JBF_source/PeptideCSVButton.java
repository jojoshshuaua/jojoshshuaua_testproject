import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
/**
 * Writes the current protein data to a comma separated value file.
 *
 * @author Jill Zapoticznyj
 *
 * Much of this code is based off of the RemoveProteinButton class
 * @author Adam Bazinet
 *
 */
public class PeptideCSVButton extends Canvas implements MouseListener {

	private String[][] values;
	private int enzymeIndex = -1;
	private String proteinName = "";
	private PeptideCSVGenerator pGen = null;
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
     *
     */
    public PeptideCSVButton( String name ) {
    	proteinName = name;
		this.addMouseListener(this);
    }
    
    public void setValues( String[][] vals ){
    	values = vals;
    }
    
    public void setIndex( int val ){
    	enzymeIndex = val;
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
	// change the background color depending on the location of the cursor
	if(highlighted == true) {
	    bufferGraphics.setColor(fillColorOn);
	} else {
	    bufferGraphics.setColor(fillColorOff);
	}
	bufferGraphics.fillRoundRect(0, 0, d.width-1, d.height-1, 15, 15);
	bufferGraphics.setColor(textColor);
	bufferGraphics.setFont(textFont);
	bufferGraphics.drawString("Record to CSV", 8, 14);

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
     * Responds to the cursor being placed over this button.
     */
    public void mouseEntered(MouseEvent e) {
	//change the image of the cursor
	setCursor(new Cursor(Cursor.HAND_CURSOR));
	highlighted = true; //register that the cursor is over the button
	//and redraw the button to reflect the change in bg color
	repaint();
	//display a message in the status bar reflecting the purpose of this
	// button.
    }
    
    /**
     * Responds to the mouse being removed from over the button.
     */
    public void mouseExited(MouseEvent e) {
	//change the image of the cursor
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	highlighted = false; //register that the cursor is not over the button
	//and redraw the button to reflect the change in bg color
	repaint();
	//remove the message from the status bar
    }
    
    /**
     * Responds to the user clicking on the button.
     */
    public void mouseClicked(MouseEvent e) {
		pGen = new PeptideCSVGenerator( values, enzymeIndex, proteinName );
		
		if( enzymeIndex != -1 && values != null ){
			pGen.writeToCSV();
		}
    }

}
