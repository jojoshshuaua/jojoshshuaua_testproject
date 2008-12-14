import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import java.awt.image.BufferedImage;

/**
 * The canvas where all animation of the 2D process takes place.
 *
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 */

public class GelCanvas extends Canvas implements MouseListener{
    private Graphics graphic = null;
    private Electro2D electro2D;  //a reference to the Electro2D class
    private Color bgColor = Color.white;
    private Rectangle d = null;  //dimensions of this component
    private Vector ief = new Vector();  // holds the IEFProteins
    private Vector dots = new Vector();  // holds the ProteinDots
    private boolean drawDots = true;  //set to true if the user wants SDS-PAGE
    private CompIEF comp;          // compares IEFProteins
    private static int red = 54;  //initial red value of background for IEF
    private static int green = 100; //initial green value of background for IEF
    private static int blue = 139;  //intial blue value of background for IEF
    private static boolean clear = true;
    private static boolean reLine = false; //keeps track of if the pH lines
                                           // have been drawn
    private static double max = -1;
    private static double min = -1;
    private final Color lineGray = Color.LIGHT_GRAY;//new Color( 154, 154, 154 );

    //positions for the molecular weight markers
    private double hundredK = 48;
    private double fiftyK = 48;
    private double twentyfiveK = 48;
    private double tenK = 48;

    private double lowPercent; //lower value of percent acrylamide of the gel
    private double highPercent;
    private int voltage; //the voltage being used
    private int reps; //how many times the genDots() method has been called
    private boolean drawMW = false;
    private boolean calcMW = true;
    private boolean redraw = false;
    private boolean reLabel = false;
    private boolean increment = true;
    private double xLoc;
    private double yLoc;
    private boolean refreshLoc = false;
    
    private MediaTracker tracker = new MediaTracker(this);

    private static boolean blink = false;
    
    // private boolean drawE2DLabels = true;

    //double buffering variables
    private Image buffer = null;
    private Graphics bufferGraphics = null;
    
    private Vector ief2 = new Vector();
    private Vector dots2 = new Vector();

    private GIFEncoder gencode;
    
    private int startX = -1;
    private int startY = -1;
    private int stopX = -1;
    private int stopY = -1;
    private boolean mousePress = false;
    
    /**
     * Constructor, performs some perfunctory tasks.
     */
    public GelCanvas(Electro2D e) {
	super();
	//assign electro2D the value passed as a parameter for this method
	electro2D = e;
	gencode = null;
	//have GelCanvas register itself as a mouseListener in order to
	// respond to mouse clicks
	this.addMouseListener(this);
    }
    
    /**
     * Prepares the information to be rendered on the canvas.  
     * Separates the vectors of protein information into two 
     * vectors - one to represent the proteins after the
     * isoelectric focusing, and another to represent the 
     * proteins after the entire 2DGE process has been implemented.
     */
    
    public void prepare(){
	
	ief = new Vector();
	dots = new Vector();
	
	//send the maximum and minimum values for the pH range to IEFProtein
        max = electro2D.getMaxRange();
	min = electro2D.getMinRange();
	IEFProtein.setRange( max, min );

	//  //send the percent acrylamide value to ProteinDot
  	lowPercent = electro2D.getLowPercent();
  	highPercent = electro2D.getHighPercent();
//  	ProteinDot.setPercent( lowPercent, highPercent );
	
	//create the CompIEF object
	comp = new CompIEF( max, min );

	//retrieve the voltage value from electro2D
	String v = electro2D.getVoltage();
	try{
	    voltage = Integer.parseInt( v.substring( 0, v.length() - 2 ) );
	}catch( NumberFormatException e ){
	    System.err.println( "The error was " + e.getMessage() );
	}
	
	//get the data Vectors from the file
	Vector sequenceTitles = electro2D.getSequenceTitles();
	Vector molwt = electro2D.getMolecularWeights();
	Vector pI_vals = electro2D.getPiValues();
	Vector sequences = electro2D.getSequences();
	Vector functions = electro2D.getFunctions();
	
	// Make IEFProteins for the protein data in the vectors
	
	Rectangle bounds = this.getBounds();
	//Vector tempIEF = new Vector();
	for( int i = 0; i < sequenceTitles.size(); i++ ){
		
	    ief.addElement( new IEFProtein( new E2DProtein(
				((String)sequenceTitles.elementAt( i )),
				((Double.valueOf(
				(String)molwt.elementAt( i )))).doubleValue(), 
			        ((Double.valueOf(
			       (String)pI_vals.elementAt( i )))).doubleValue(),
				(String)sequences.elementAt( i ), 
				(String)functions.elementAt( i )), bounds ) );
				
		if( ((String)sequenceTitles.elementAt(i)).equals( "AF0001" ) ){
			System.out.println( "Found AF0001 " + i );
		}
	}
	
//*****************************TEST THIS CHANGE JILL***************************	
	
	// Combine IEFProteins who have the same range of pI values
	boolean allSorted = false;
	while( !allSorted ){
		allSorted = true;
		System.out.println( ief.size() );
		//System.out.println( ((IEFProtein)ief.elementAt(0)).getMaxPI());
		for( int i = ief.size() - 1; i >= 0; i-- ){
		    for( int j = i-1; j >= 0; j-- ){
		    	//System.out.println( "j = " + j );
		    	//System.out.println( "i = " + i );
//		    	System.out.println(((IEFProtein)ief.elementAt(i)).getMaxPI());
				//System.out.println( "i = " + i );
				//System.out.println( "j = " + j );
				if( comp.compare( (IEFProtein)ief.elementAt(i),
					  (IEFProtein)ief.elementAt(j) ) == 0 ){
		    
		//System.out.println( ((IEFProtein)(ief.elementAt(i))).getMinPI());
			   	 	// copy ief.get( i )'s protein to proteins.get(j)
				
			    	( ( IEFProtein )( ief.elementAt( i ) ) ).addProtein( 
		   				( ( IEFProtein )( ief.elementAt( j ) ) ).getProtein() );
						    
			    	// remove the copied IEFProtein from the 
			    	// list of IEFProteins
			
			    	ief.remove( j );
			
				    //System.out.println( "removed one. j was = " + (j) + "  j now = " + (j+1) );
				   // System.out.println( "i was = " + i + "  i now = " + (ief.size()-1) );				    
				    //i = ief.size() - 1;
				    i--;
				    j = i;
				    //i = ief.size();
				    //j = i - 1;
				    //System.out.println( ((IEFProtein)ief.elementAt(0)).getMaxPI());
				    //j++;
			    	//j = i;
			    	allSorted = false;
				}
			}
	    }
	    System.out.println ("*****************************************");
	}
	//ief = ( Vector )tempIEF.clone();

	// create the ProteinDot objects

	Vector tempProtein = new Vector();
	double tempx = 0;
	double tempy = 0;
	for( int i = 0; i < ief.size(); i++ ){
	    
	    //get the x and y locations of the IEFProtein, as well as
	    //the vector of proteins it represents
	    tempx = ((IEFProtein)(ief.elementAt( i ) ) ).returnX();
	    tempy = ((IEFProtein)(ief.elementAt( i ) ) ).returnY();
	    tempProtein = 
		( (IEFProtein)(ief.elementAt( i ) ) ).getProtein();
	    for( int j = 0; j < tempProtein.size(); j++ ){
		//create a new ProteinDot object for each protein contained
		//in the IEFProtein
		dots.addElement( new ProteinDot( 
		      ((E2DProtein)(tempProtein.elementAt( j )) ), this, tempx,
						tempy + 43, electro2D ) );
		
		
	    }
	    tempProtein.removeAllElements();
	}
	
	
    }

    /**
     * The prepare2 method is called only when the user wishes to compare
     * two proteome files.  The method performs the same basic steps as 
     * the original prepare method, as well as comparing the proteins from
     * the second file to those already contained in the first file.  
     * If two proteins are the same, it colors the first file's matching
     * proteins green and removes the proteins from the second file's 
     * collection of proteins.
     */
    public void prepare2(){
	
	Vector sequences = electro2D.getSequences2();
	Vector sequenceTitles = electro2D.getSequenceTitles2();
	Vector molWts = electro2D.getMolecularWeights2();
	Vector piValues = electro2D.getPiValues2();
	Vector functions = electro2D.getFunctions2();

	//if( sequences == null ){
	//  System.out.println( "hi" );
	//}
	
	ief2 = new Vector();
	dots2 = new Vector();
	Vector ief3 = new Vector();
	String seq = "";
	Rectangle bounds = this.getBounds();
	
	// compare the sequences of the second file to the sequences of
	// the proteins in the first file
        for( int i =dots.size()-1; i >=0; i-- ){
	    //color the proteins of the first file red
	    ((ProteinDot)dots.elementAt(i)).changeColor( Color.blue );
	    seq = ((ProteinDot)dots.elementAt(i)).getPro().getSequence();
	    for( int j = sequences.size() - 1; j >= 0; j-- ){
		//if the sequences match, remove the sequence and its 
		// corresponding information from the second file's list of 
		// info and color the protein green in the vector of proteins
		// from the first file.
		if( ((String)sequences.elementAt( j )).equals( seq ) ){
		    
		    sequences.remove( j );
		    sequenceTitles.remove( j );
		    molWts.remove( j );
		    piValues.remove( j );
		    functions.remove( j );
		  ((ProteinDot)dots.elementAt( i )).changeColor( Color.red );
		  //((ProteinDot)dots2.elementAt( dots2.size() - 1 )).changeColor( Color.red );
		  System.out.println( "SAME!" );
		  break;
		}
	    }
	}
	

       // Make IEFProteins for the protein data in the vectors
	
	//Rectangle bounds = this.getBounds();
	//Vector tempIEF = new Vector();

	for( int i = 0; i < /*piValues.size()*/sequenceTitles.size(); i++ ){
		
	    ief2.addElement( new IEFProtein( new E2DProtein(
				((String)sequenceTitles.elementAt( i )),
				((Double.valueOf(
				(String)molWts.elementAt(i)))).doubleValue(), 
			        ((Double.valueOf(
			       (String)piValues.elementAt(i)))).doubleValue(),
				(String)sequences.elementAt( i ), 
				(String)functions.elementAt( i )), bounds ) );
	
	}
	
	
	
	// Combine IEFProteins who have the same range of pI values
	
	for( int i = ief2.size() - 1; i > 0; i-- ){
	    for( int j = i - 1; j > 0; j-- ){
		
		if( comp.compare( (IEFProtein)ief2.elementAt(j),
				  (IEFProtein)ief2.elementAt(i) ) == 0 ){
		    
		
		    // copy ief.get( i )'s protein to proteins.get(j)
			
		    ( ( IEFProtein )( ief2.elementAt( j ) ) ).addProtein( 
		   ( ( IEFProtein )( ief2.elementAt( i ) ) ).getProtein() );
						    
		    // remove the copied IEFProtein from the 
		    // list of IEFProteins
			
		    ief2.remove( i );
			
		    i--;
		    j = i;
		}
		
	    }
	}
	
	// create the ProteinDot objects

	Vector tempProtein = new Vector();
	double tempx = 0;
	double tempy = 0;
	for( int i = 0; i < ief2.size(); i++ ){
	    tempx = ((IEFProtein)(ief2.elementAt( i ) ) ).returnX();
	    tempy = ((IEFProtein)(ief2.elementAt( i ) ) ).returnY();
	    tempProtein = 
		( (IEFProtein)(ief2.elementAt( i ) ) ).getProtein();
	    for( int j = 0; j < tempProtein.size(); j++ ){
		dots2.addElement( new ProteinDot( 
		      ((E2DProtein)(tempProtein.elementAt( j )) ), this, tempx,
						tempy + 43, electro2D ) );
		
		((ProteinDot)dots2.elementAt( dots2.size()-1 )).changeColor(
							       Color.yellow );
	    }
	    tempProtein.removeAllElements();
	}
	
	//System.out.println( ief3 );

	//ief2.add( ief3 );
    }

    /**
     * Paints the current state of the animation.  All drawing is done to
     * the 
     * offscreen image, which is then painted on to the graphics object.  
     * A widely-used technique to reduce flicker.
     */
	
    public void paint(Graphics g) {
	
	graphic = g;
	//initialize buffering variables
	if(d == null || buffer == null || bufferGraphics == null) {
	    d = getBounds();
	    //set image size equal to component size
	    buffer = this.createImage(d.width, d.height);

	    bufferGraphics = buffer.getGraphics();
	}

	//if the ranges have been set, and the IEF has been drawn, draw the
	// pH value markers
	if( max != -1 && min != -1 && reLine ){
	    drawLines();
	}
	//if the SDS animation has been completed for the first time,
	//create the markers
	if( drawMW ){
	    this.genMWLines();
	    drawMW = false;
	    redraw = true;
	}
	//if the SDS animation has been completed and the positions for the
	//markers are already known, redraw the markers.
	else if( redraw ){
	    drawLines();
	    this.drawMWLines();
	}
	if( refreshLoc ){
	    this.redrawLocation();
	}
	    
	bufferGraphics.setColor( new Color(54, 100, 139 ));
	//draw border
	bufferGraphics.setColor(new Color(54,100,139));
	bufferGraphics.drawRect(0,0,d.width-1,d.height-1);
	bufferGraphics.setColor( Color.RED );
	bufferGraphics.drawRect( 1, 1, d.width - 3, 46 );
	bufferGraphics.setColor(new Color(54,100,139));
	
	//finally, draw image onto Graphics object
	//br = buffer.getScaledInstance( 2*d.width, 2*d.height, Image.SCALE_SMOOTH );
	//tracker.addImage( br, 0 );
	//try{
	//  tracker.waitForID( 0 );
	//}catch( Exception exc ){}
	g.drawImage(buffer, 0, 0, this);
	
    }
	

    /**
     * This method is used to refresh the screen once the Y values
     * of the ProteinDots have been changed by genDots()
     *
     * @param g the graphics object that does the drawing
     */

    public void update(Graphics g) {
	//hide the already drawn dots
	//this.clearIEF();
	g.setColor( bgColor );
	bufferGraphics.setColor( bgColor );
	bufferGraphics.clearRect( 1, 48, d.width - 2, d.height - 49 );
	for( int i = 0; i < dots.size(); i++ ){
	    //draw the dots at their new y coordinate
	    ((ProteinDot)(dots.elementAt( i ) ) ).draw( bufferGraphics );
	}
	if( dots2 != null ){
	    for( int i = 0; i < dots2.size(); i++ ){
		((ProteinDot)(dots2.elementAt( i ) ) ).draw( bufferGraphics );
	    }
	}
	    
	
	//refresh the image on the applet
	if( drawMW && tenK != 48 ){
	    drawMWLines();
	    drawLines();
	}
	g.drawImage( buffer, 0, 0, this );
	//if( drawE2DLabels ){
	this.paint( g );
	//}
	//this.validate();
    }

    /**
     * This method is used only when generating gif files without the 
     * gel screen displayed to the user.
     */
    public void genGIFFile( Vector dts, int seconds ){
	ProteinDot.setShow();
	System.out.println( "generating image" );
	dots = dts;
	System.out.println( dots.size() );
	drawMW = true;
	//drawE2DLabels = false;
	max = 10;
	min = 3;
	//if(d == null || buffer == null || bufferGraphics == null) {
	//  d = getBounds();
	    //set image size equal to component size
	//  buffer = new BufferedImage( d.width, d.height, BufferedImage.TYPE_INT_RGB);
	    // System.out.println( buffer );
	//  bufferGraphics = new BufferedImage( d.width, d.height, BufferedImage.TYPE_INT_RGB ).getGraphics();
	//}
	//d = getBounds();
	//buffer = createImage(d.width, d.height);
	//bufferGraphics = buffer.getGraphics();
	//paint( bufferGraphics );
	//update( bufferGraphics );
	this.repaint();
	try{
	    GIFEncoder gifEnc = new GIFEncoder( buffer );
	    gifEnc.Write( new BufferedOutputStream( new FileOutputStream( 
			electro2D.getLastFileLoaded() + seconds + ".gif" ) ) );
	}catch( IOException x ){
	    System.err.println( x.getMessage() );
	}catch( AWTException x2 ){
	    System.err.println( x2.getMessage() );
	}
	//System.exit(0);
    }
    /**
     * returns the graphics object used to draw on the canvas
     *
     * @return graphic
     */
    public Graphics getGraphic(){
	return graphic;
    }

    /**
     * draws the pH dotted lines on the canvas
     */
    public void drawLines( ){
	
	int length = 0;
	int loc = 0;
	//set the color of the lines
	bufferGraphics.setColor( lineGray );
	//for each of the integers in the range of the pH values...
	for( int i = (int)min + 1; i <= max; i = i + 1 ){
	    length = 0;
	    //calculate the location this value would have along the gel
	    loc =(int)( ( this.getWidth() - 4 ) * ( (
					     (i - min ) / ( max - min )) ));
	    // and while the length of the line is less than the length of the
	    //gel...
	    while( length < this.getHeight() ){
		//draw the line
		bufferGraphics.drawLine( loc, length, loc, length + 5 );
		length = length + 10;
	    }
	    //  if( drawE2DLabels ){
	    //display the labels on the applet that show the pH values
	    electro2D.showpH( loc, i );
	    // }
	}
	//if( drawE2DLabels ){
	//create a label for the minimum value
	electro2D.showpH( 0, (int)min );
	//}
	//show that the lines have been drawn
	reLine = false;
    }
    
    /**
     * This method sets the pH line boolean values to let the paint
     * method know that the lines need to be redrawn.
     */
    public void setreLine(){
	reLine = true; //redraw the pH lines
	redraw = false; //don't redraw the molecular weight lines
    }

    /**
     * This method sets the line boolean values to let paint know not
     * to draw the dotted lines for the pH and mol. weight
     */
    public void resetReLine(){
	reLine = false; //don't draw the pH markers
	redraw = false; //don't draw the MW markers
    }

    /**
     * This method is called by the restart button.  It sets all of the 
     * animation control values back to their initial values.
     */
    public void resetRanges(){
	//reset the range values
	max = -1;
	min = -1;
	//reset the MW positions
	hundredK = 48;
	fiftyK = 48;
	tenK = 48;
	twentyfiveK = 48;
	//reset the animation conditions
	reLabel = false;
	calcMW = true;
	redraw = false;
	drawMW = false;
	increment = true;
    }

    /**
     * This method lets the paint method know to generate the molecular
     * weight markers.
     *
     * @param i - how many times the genDots() method was called
     */
     public void setMWLines( int i ){
	 drawMW = true;
	 calcMW = true;
	 reps = i;
     }

    /**
     * this method draws the molecular weight lines onto the gel.
     */
    public void drawMWLines(){
	lowPercent = electro2D.getLowPercent();
	highPercent = electro2D.getHighPercent();
	    int width = 0;
	    //set the line color
	    bufferGraphics.setColor( lineGray );
	    //while the line is smaller than the width of the gel
	    while( width < this.getWidth() ){
		bufferGraphics.setColor( lineGray );
		//draw a line for each of the molecular weights being
		//represented
		bufferGraphics.drawLine( width, (int)hundredK, width + 5,
					 (int)hundredK );
		bufferGraphics.drawLine( width, (int)fiftyK, width + 5,
					 (int)fiftyK );
		bufferGraphics.drawLine( width, (int)twentyfiveK, width + 5,
					 (int)twentyfiveK );
		bufferGraphics.drawLine( width, (int)tenK, width + 5,
					 (int)tenK );
		width = width + 10;
	    }
	    
	    //redraw the molecular weight labels in their new positions on
	    // the applet
	    //if( drawE2DLabels ){
	    electro2D.clearMW();
	    electro2D.showMW( (int)hundredK, (int)fiftyK, (int)twentyfiveK,
			      (int)tenK, reLabel );
	    reLabel = true;
	    //}
	    
    }

	

    /**
     * this method calculates the position of the molecular weight lines on the
     * gel and draws their inital position
     */
    public void genMWLines(){
	lowPercent = electro2D.getLowPercent();
	highPercent = electro2D.getHighPercent();
	//if the value for the molecular weight needs to be calculated, do so
	int height = this.getHeight();
	if( calcMW ){
	    if( lowPercent == highPercent ){
		for( int i = 0; i < reps; i++ ){
		    hundredK = hundredK + ( 10 * 1/lowPercent ) * ( voltage / 25 )
			* .25 * ( 100000/100000 );
		    fiftyK = fiftyK + ( 10 * 1/lowPercent ) * ( voltage / 25 ) 
			* .25 * ( 100000/50000 );
		    twentyfiveK = twentyfiveK + ( 10 * 1/lowPercent ) *
			( voltage / 25 ) * .25 * ( 100000/25000 );
		    tenK = tenK + ( 10 * 1/lowPercent ) * ( voltage / 25 ) *
			.25 * ( 100000/10000 );
		}
	    }
	    else{
		for( int i = 0; i < reps; i++ ){
		   hundredK = hundredK + ( 10 * 1/(((hundredK-48) * ( highPercent - lowPercent)/(height - 48))+lowPercent) ) * ( voltage / 25 )
			* .25 * ( 100000/100000 );
		    fiftyK = fiftyK + ( 10 * 1/(((fiftyK-48) * ( highPercent - lowPercent)/(height - 48))+ lowPercent) ) * ( voltage / 25 ) 
			* .25 * ( 100000/50000 );
		    twentyfiveK = twentyfiveK + ( 10 * 1/(((twentyfiveK - 48) * (highPercent - lowPercent)/(height - 48))+ lowPercent )) *
			( voltage / 25 ) * .25 * ( 100000/25000 );
		    tenK = tenK + ( 10 * 1/(((tenK - 48)*(highPercent - lowPercent)/(height - 48)) + lowPercent) ) * ( voltage / 25 ) *
			.25 * ( 100000/10000 );  

		}
	    }
	
	    calcMW = false;
	    int width = 0;
	    //set the line color
	    bufferGraphics.setColor( lineGray );
	    //while the line length is less than the width of the gel,
	    //draw the dotted lines in their correct positions
	    while( width < this.getWidth() ){
		bufferGraphics.setColor( lineGray );
		bufferGraphics.drawLine( width, (int)hundredK, width + 5,
					 (int)hundredK );
		bufferGraphics.drawLine( width, (int)fiftyK, width + 5,
					 (int)fiftyK );
		bufferGraphics.drawLine( width, (int)twentyfiveK, width + 5,
					 (int)twentyfiveK );
		bufferGraphics.drawLine( width, (int)tenK, width + 5,
					 (int)tenK );
		width = width + 10;
	    }
	   
	    //    if( drawE2DLabels){
	    electro2D.clearMW();
	    electro2D.showMW( (int)hundredK, (int)fiftyK, (int)twentyfiveK,
			      (int)tenK, reLabel );
	    reLabel = true;
	    //}
	    //draw the new image to the screen
	    graphic.drawImage( buffer, 0, 0, this );
	}
	
    }
    
    /**
     * This method is used to draw the IEFProtein rectangles to the screen
     *
     */

    public void drawIEF( ){
	
	//if the animation is still continuing, move the X value of the
	// IEFProtein a predetermined amount closer to its final x coordinate
	for( int i = 0; i < ief.size(); i++ ){
	    if( increment ){
		((IEFProtein)ief.elementAt(i) ).changeX();
	    }
	    else{
		//otherwise, place the IEFProtein at its final X location.
		((IEFProtein)ief.elementAt(i)).setX();
	    }
	    //draw the IEFProteins to the image buffer
	    (( IEFProtein )(ief.elementAt( i ) ) ).draw( bufferGraphics );
	}
	
	//repeat the same process for the proteins from the second file
	// (if any)
	for( int i = 0; i < ief2.size(); i++ ){
	    if( increment ){
		((IEFProtein)ief2.elementAt( i ) ).changeX();
	    }
	    else{
		((IEFProtein)ief2.elementAt( i ) ).setX();
	    }
	    ((IEFProtein)(ief2.elementAt( i ) ) ).draw( bufferGraphics );
	}
	//draw the buffer to the applet
	graphic.drawImage( buffer, 0, 0, this );
	this.repaint();
    }

    /**
     * This method is used to clear the background and redraw the IEFProteins
     * when they are being shrunk down
     */
    public void shrinkIEF(){
	// clear the IEF area of the animation
	clearIEF();
	// redraw the IEFProteins
	this.drawIEF();
    }
    
    /**
     * This method clears out the background of the IEF area of the animation
     */
    public void clearIEF(){
	// set the color to the background color of the applet and draw a 
	// rectangle over the IEF area of the animation
	bufferGraphics.setColor( bgColor );
	bufferGraphics.clearRect( 2, 2, d.width - 3, 45 );
    }

    /**
     * return the red value of the background for the IEF animation
     *
     * @return red
     */
    public static int getRed(){
	return red;
    }

    /**
     * return the green value of the background for the IEF animation
     *
     * @return green
     */
    public static int getGreen(){
	return green;
    }

    /**
     * return the blue value of the background for the IEF animation
     *
     * @return blue
     */
    public static int getBlue(){
	return blue;
    }

    /**
     * resets the red value for the background of the IEF animation
     * when the reset button is pressed
     */
    public static void setRed(){
	red = 54;
    }
    
    /**
     * resets the green value for the background of the IEF animation 
     * when the reset button is pressed
     */
    public static void setGreen(){
	green = 100;
    }
    
    /**
     * resets the blue value for the background of the IEF animation when the
     * reset button is presed
     */
    public static void setBlue(){
	blue = 139;
    }

    /**
     * Controls the animation for the inital display of the IEFProteins
     */
    public void animateIEF(){
	clear = false;
	// the final values for the background color
	int finalRed = 0;//176;
	int finalGreen = 0;//196;
	int finalBlue = 0;//222;

	// get the current and final widths of the IEFProteins
	double width = IEFProtein.returnTempWidth();
	double finalWidth = IEFProtein.returnWidth();
	
	//change the background color of the IEF animation
	bufferGraphics.setColor( new Color( red, green, blue ) );
	bufferGraphics.fillRect( 2, 2, d.width - 3, 45 );
	
	//change the width of the IEFProteins
	IEFProtein.changeWidth();
	
	//redraw the IEFProteins to the screen
	drawIEF();

	//increment the values for the color of the background
	red = red - 1;
	green = green - 2;
	blue = (int)(blue - 2.78 );

	// if the colors have reached their final values, draw the
	// IEFProteins at their final width
	if( red <= finalRed || green<=finalGreen || blue<=finalBlue || 
	    width>=finalWidth ){
	    increment = false;
	    bufferGraphics.setColor( new Color( finalRed, finalGreen, finalBlue ) );
	    IEFProtein.setWidth();
	    bufferGraphics.fillRect( 2, 2, d.width - 3, 45 );
	    clear = true;
	    drawIEF();
	}
    }

    /**
     * Returns the vector of ProteinDots
     *
     * @return dots
     */
    public Vector getDots() {
	return dots;
    }

    /**
     * returns the dots2 vector.
     *
     * @return dots2
     */
    public Vector getDots2(){
	return dots2;
    }

   
    /**
     * increments the y values for the dots for the animation, depending
     * on whether or not the start button for the second phase of the 2DGE
     * has been pressed or not.
     */

    public void genDots( ){
	clearCanvas();
	
	for( int i = 0; i < dots.size(); i++ ){

	    // ...change the y value for the dots...
	    
	    ((ProteinDot)(dots.elementAt( i ) ) ).changeY();
	    
	    // if( ((ProteinDot)(dots.elementAt( i ))).returnY() + 20 >= getBounds().height ){
	    //	electro2D.stopThread();
	    //}
	}
	if( dots2 != null ){
	    for( int j = 0; j < dots2.size(); j++ ){
		((ProteinDot)(dots2.elementAt(j))).changeY();
	    }
	}
        // ...then update the panel
	this.repaint();
    }

    /**
     * This method is called when the restart button is pressed.
     * It resets the Y values of the proteinDots back to their
     * initial positions
     */

    public void restartCanvas(){
	for( int i = 0; i < dots.size(); i++ ){
	    
	    //set the y coordinates back to their initial values
	    ((ProteinDot)(dots.elementAt( i ) ) ).restart();
	}
	
	for( int i = 0; i < dots2.size(); i++ ){
	    ((ProteinDot)(dots2.elementAt( i ) ) ).restart();
	}
	
	//redraw the dots
	update( graphic );
	repaint();
    }

    /**
     * clears the dots before it renews the y values of the proteins
     *
     */
    public void clearCanvas() {
	graphic.setColor( new Color( 54, 100, 139 ) );
	graphic.clearRect( 1, 48, d.width - 1, d.height - 47 );
    }

    /**
     * This method is used to draw the lines indicating the position of a 
     * protein whose title was double clicked on in the list of proteins 
     * being displayed.  It draws black lines at the protein's corresponding
     * pI and molecular weight values which extend out to the protein's 
     * position.
     *
     * @param id the title of the protein to be found
     */
    public void drawLocation( String id ){
        xLoc = 0;
        yLoc = 0;
	bufferGraphics.setColor( new Color( 0, 0, 0 ) );
	bufferGraphics.fillRect( 2, 2, d.width - 4, 45 );
	//for all of the proteins in the animation, if the ID of the protein
	//matches the one passed to the method, get the x and y position of
	//the protein and break out of the loop.
	for( int i = 0; i < dots.size(); i++ ){
	    if( (((ProteinDot)dots.elementAt( i )).getPro().getID() ).equals( id ) ){
		xLoc = ((ProteinDot)dots.elementAt(i)).returnX();
		yLoc = ((ProteinDot)dots.elementAt(i)).returnY();
		i = dots.size();
	    }
	}
	refreshLoc = true;
	//	bufferGraphics.setColor( Color.BLACK );
	//bufferGraphics.drawLine( (int)xLoc, (int)yLoc, 0, (int)yLoc );
	//bufferGraphics.drawLine( (int)xLoc, (int)yLoc, (int)xLoc, 0 );
	
	//repaint the image to the screen
	this.repaint();
    }

    public void startDotBlink(){
	for( int i = 0; i < dots.size(); i++ ){
	    if( ((ProteinDot)dots.get( i )).getColor() == Color.RED ){
		//((ProteinDot)dots.get( i )).startThread();
	    }
	}
	blink = true;
    }

    public static boolean getBlink(){
	return blink;
    }

    public static void stopBlink(){
	blink = false;
    }

    /**
     * this method draws the location of an individual protein to the screen
     * based on the values of xLoc and yLoc as provided by the drawLocation
     * method
     */
    public void redrawLocation(){
	bufferGraphics.setColor( Color.LIGHT_GRAY );
	bufferGraphics.drawLine( (int)xLoc+2, (int)yLoc, 0, (int)yLoc );
	bufferGraphics.drawLine( (int)xLoc+2, (int)yLoc, (int)xLoc+2, 0 );
    }
    
    public void resetLocation(){
	refreshLoc = false;
    }

    /**
     * Mouse listener methods.  Will be used to display protein information
     * when user clicks on a band or a dot.
     */
    public void mousePressed(MouseEvent e) {
	mousePress = true;
	startX = e.getX();
	startY = e.getY();
	//do nothing
    }
    public void mouseReleased(MouseEvent e) {
	if( mousePress ){
	    stopX = e.getX();
	    stopY = e.getY();
	
	    if( startX != stopX && stopX > startX + 5 ){
		if( startY != stopY && stopY > startY + 5 ){
		    Vector bigDot = new Vector();
		    ProteinDot thedot = null;
		    double diameter = ProteinDot.getDiameter();
		    for( int i = 0; i < dots.size(); i++ ){
			thedot = (ProteinDot)dots.get(i);
			if( (thedot.returnX() + diameter >= startX )
			    && ( thedot.returnX() <= stopX ) ){
			    if((thedot.returnY() + diameter >=
				startY) && (thedot.returnY() <= stopY ) ){
				bigDot.add( thedot );
			    }
			}
		    }
		    if( dots2 != null ){
			for( int i = 0; i < dots2.size(); i++ ){
			    thedot = (ProteinDot)dots2.get(i);
			    if(( thedot.returnX() + diameter >= startX )
			       && (thedot.returnX() <= stopX ) ){
				if((thedot.returnY() + diameter >=
				    startY) && (thedot.returnY() <= stopY )){
				    bigDot.add( thedot );
				}
			    }
			}
		    }
		    ImageZoom zoom = new ImageZoom( electro2D, buffer.getSource(),
						    startX, startY, stopX,
						    stopY, bigDot );
		    
		}
	    }
	}
	//do nothing
    }
    public void mouseEntered(MouseEvent e) {
	//do nothing
    }
    public void mouseExited(MouseEvent e) {
	mousePress = false;
	//do nothing
    }
    public void mouseClicked(MouseEvent e) {
	
	//get the position of where the mouse was clicked
	double clickX = e.getX();
	double clickY = e.getY();

	// compare this position to the positions of the ProteinDots
	for( int i = 0; i < dots.size(); i++ ){
	    double dotX = ((ProteinDot)dots.elementAt( i)).returnX();
	    double dotY = ((ProteinDot)dots.elementAt(i)).returnY();
	    if(((ProteinDot)dots.elementAt(i)).getShowMe() && 
	       clickX <= dotX + 6 && clickX >= dotX - 1 ){
		if( clickY <= dotY + 7 && clickY >= dotY - 1){
		    
		    //if the positions are the same, display the information
		    // about the protein(s) on which the user clicked
		    ProteinFrame pFrame = new ProteinFrame( electro2D, 
			   ((ProteinDot)dots.elementAt( i )).getPro().getID(), 1);
		    // pFrame.setResizable( true );
		    pFrame.show();
		    pFrame.updateLabel();
		    
		}
	    
	    }
	}
	
	for( int i = 0; i < dots2.size(); i++ ){
	    double dotX = ((ProteinDot)dots2.elementAt( i ) ).returnX();
	    double dotY = ((ProteinDot)dots2.elementAt(i)).returnY();
	    if(((ProteinDot)dots2.elementAt(i)).getShowMe() &&
	       clickX <= dotX + 5 && clickX >= dotX - 1 ){
		if( clickY <= dotY + 5 && clickY >= dotY - 1 ){
		    ProteinFrame pFrame = new ProteinFrame( electro2D,
							    ((ProteinDot)dots2.elementAt(i)).getPro().getID(), 2);
		    //pFrame.setResizable( false );
		    pFrame.show();
		    pFrame.updateLabel();
		    
		}
	    }
	}

	// then check to see if canvas was clicked on an IEFProtein
	Vector p = new Vector();
	double iefWidth = IEFProtein.returnWidth();
	
	for( int j = 0; j < ief.size(); j++ ){
	    double iefX = ((IEFProtein)ief.elementAt( j )).returnX();
	    double iefY = ((IEFProtein)ief.elementAt( j )).returnY();
	    if( IEFProtein.returnHeight() > 0 ){
	       
		if( clickX >= iefX && clickX <= iefX + iefWidth ){
		    
		    if( clickY >= iefY &&
			clickY <= iefY + 40 ){

			// if the positions matched, display the information
			// about the IEFProtein on which the user clicked
			p = ((IEFProtein)ief.elementAt(j)).getProtein();
			IEFFrame iFrame = new IEFFrame(
					       (IEFProtein)ief.elementAt(j));
			iFrame.setResizable( true );
			iFrame.pack();
			iFrame.show();
			
			//System.out.println( p.getSequence() );
			
		    }
		}
	    }
	}

	for( int j = 0; j < ief2.size(); j++ ){
	    double iefX = ((IEFProtein)ief2.elementAt( j )).returnX();
	    double iefY = ((IEFProtein)ief2.elementAt( j )).returnY();
	    if( IEFProtein.returnHeight() > 0 ){
		
		if( clickX >= iefX && clickX <= iefX + iefWidth ){
		    
		    if( clickY >= iefY &&
			clickY <= iefY + 40 ){

			// if the positions matched, display the information
			// about the IEFProtein on which the user clicked
			p = ((IEFProtein)ief2.elementAt(j)).getProtein();
			IEFFrame iFrame = new IEFFrame(
					       (IEFProtein)ief2.elementAt(j));
			iFrame.setResizable( true );
			iFrame.pack();
			iFrame.show();
			//System.out.println( p.getSequence() );
			
			
		    }
		}
	    }
	}
	
	
    }
}
    
    









