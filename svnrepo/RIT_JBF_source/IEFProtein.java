/**
 * This class represents a single bar of proteins that are a result
 * of IEF.
 *
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 */

import java.awt.*;
import java.awt.Graphics2D;
import java.util.*;

public class IEFProtein extends Component{

    // the colors that represent the number of proteins 
    // w/in a certain range

    private static Color[] colors = { Color.blue, Color.green, Color.yellow,
				      Color.red, Color.orange, Color.pink };
    private Color myColor;  // the current color of the IEFProtein
    private Vector proteins;  // the collection of proteins this bar represents
    private int myX;  // the x-coordinate of this protein
    private static int myY = 5;  // the y-coordinate of this protein
    private double tempX = 0;
    private double increments = 0;
    /**********/
    private double myPi; // the pI value of the original protein
    private double maxPi; //the maximum pI of the proteins stored
    private double minPi; //the minimum pI of the proteins stored
    private static int myHeight = 40; //the initial height of the IEFProtein
    private static double myWidth = 0; //the initial width of the IEFProtein
    private static double tempWidth = 0;//the temporary width of the IEFProtein
    private Vector name;  //stores the names of all the proteins
    private static double rMax = 10; // the default max pH range
    private static double rMin = 3; //the default min pH range
    private static Rectangle bounds; //the size of GelCanvas
    
    /**
     * constructor - creates the IEFProtein object
     *
     * @param p the first protein to be represented by this object
     */
    public IEFProtein( E2DProtein p, Rectangle r ){
	name = new Vector(); //initialize the vector
	bounds = r; //store the size of the rectange
	proteins = new Vector(); // initialize the vector
	proteins.add( p ); //adds the first protein to the vector
	
	//get the pI values
	myPi = p.getPI(); 
	minPi = p.getPI();
	maxPi = minPi;
        if( myPi < rMin ){
	    maxPi = rMin;
	}
      
	//add the name of the protein to the vector
	name.add( p.toString() );
	myColor = colors[ 0 ]; // set the initial color of the object

	// determine the x coordinate of this IEFProtein
	double w = bounds.getWidth();
   
	if(  myPi <= ((double)rMin) ){
	    myX = 1;
	}
	else if( myPi >= rMax/* && myPi >= ((double)rMax)+.2*/ ){
	    myX = (int)( w  - 6 );
	}
	else{
	    myX =(int)( ( w - 4 ) * ( (
				       (myPi - rMin ) / ( rMax - rMin )) ));
	}

	Random rand = new Random();
	tempX = rand.nextInt( (int)w - 7  );
	tempX = tempX + 1;
	increments = (myX - tempX)/50;

	
    }


    
    /**
     * addProtein adds a collection of proteins c to the vector of proteins
     * being
     * represented by this IEFProtein object
     *
     * @param c the collection to be added to the vector
     */   
    
    public void addProtein( Collection c ){
	//copy all of the proteins passed to this object into its 
	//vector
	proteins.addAll( (Vector)(c) );
	
	//change the color of the IEFProtein based on the number of proteins
	// it now has
	if( proteins.size() < 10 ){
	    myColor = colors[ 0 ];
	}
	else if( proteins.size() < 20 ){
	    myColor = colors[ 1 ];
	    }
	else if( proteins.size() < 30 ){
	    myColor = colors[ 2 ];
	}
	else if( proteins.size() < 40 ){
	    myColor = colors[ 3 ];
	}
	else if( proteins.size() < 60 ){
	    myColor = colors[ 4 ];
	}
	else{
	    myColor = colors[ 5 ];
	}
	
	//redraw the protein to the screen
	this.repaint();
	
	//add the new protein names to the vector of names and 
	//set any changes in the max or min pI values
	E2DProtein p = null;
	double pi = 0;
	for( int i = 0; i < ((Vector)c).size(); i++ ){
	    
	    name.add( ((E2DProtein)((Vector)c).elementAt( i )).toString() );
	    
	    p = ((E2DProtein)((Vector)c).elementAt(i));
	    pi = p.getPI();

	    if( pi > maxPi ){
			maxPi = pi;
	    }
	    else if( pi < minPi ){
			minPi = pi;
	    }
	}
	
		myPi = (maxPi + minPi) / 2;
	
    }
    
    // accessors - return the coordinates of the protein
    
    public int returnX() {
	return myX;
    }

    public int returnY(){
	return myY;
    }

    /**
     * return the pI values that this IEFProtein should be representing
     */

    /**
     * @return the maximum pI value
     */
    public double getMaxPI(){
	return maxPi;
    }

    /**
     * @return the minimum pI value
     */
    public double getMinPI(){
	return minPi;
    }

    /**
     * getProtein returns the protein object(s) that this IEFProtein
     * is representing.
     *
     * @return proteins
     */
    public Vector getProtein(){
	
	return proteins;
    }
    
    /**
     * changeWidth increments the width of the IEFProtein for the initial
     * animation.
     */
    public static void changeWidth(){
	
	//increase the width of the object by a 50th of its final width
	tempWidth = tempWidth + myWidth / 50;
	//change the position of the object a 50th of the distance from its
	//final position.
	//    tempX = tempX + increments;
    }

    /**
     * changes the position of the object a 50th of the distance from its 
     * final position.
     */
    public void changeX(){
	tempX = tempX + increments;
    }

    /**
     * Sets the tempX equal to the final X value for the end of the animation
     * of the IEF process
     */
    public void setX(){
	tempX = myX;
    }

    /**
     * Sets the tempWidth equal to the final width for the end of the
     * animation of the IEF process
     */
    public static void setWidth(){
	tempWidth = myWidth;
	//tempX = myX;
    }

    /**
     * draw displays the IEFProtein as a rectangle on the screen
     */
    public void draw( Graphics g ){
	
	//AlphaComposite ac = AlphaComposite.getInstance( AlphaComposite.SRC_OVER ,  1.0f);
	//graphic = g;
	//((Graphics2D)g).setComposite( ac );
	
	//calculate the final width
	myWidth = 563/100;
	
	//set the color to be drawn to the color of this IEFProtein
	g.setColor( myColor );

	//draw the rectangle to the screen
	g.fillRect( (int)tempX, myY, (int)tempWidth, myHeight );
	
    }

    /**
     * decreases the height of the protein for the beginning of the SDS-PAGE
     * animation
     */
    public static void shrinkProtein(){
	myHeight = myHeight - 2;
	//shift the y coordinate down so it appears that the object is being
	//compressed
	myY = myY + 2;
    }

    /**
     * returns the current height of the object
     *
     * @return myHeight
     */
    public static int returnHeight(){
	return myHeight;
    }

    /**
     * returns the final width of this object
     *
     * @return myWidth
     */
    public static double returnWidth(){
	return myWidth;
    }

    /**
     * returns the current width of this object
     *
     * @return tempWidth
     */
    public static double returnTempWidth(){
	return tempWidth;
    }
    
    /**
     * This method is called when the reset button is pressed.  It sets the
     * tempWidth data member back to zero to prepare to draw the IEFProteins
     * again
     */
    public static void resetTempWidth(){
	tempWidth = 0;
    }

    /**
     * This method is called when the reset button is pressed.  It sets the
     * height and y positions back to their original values
     */
    public static void resetProtein(){
	myHeight = 40;
	myY = 5;
    }

    /**
     * Returns the vector of protein titles
     *
     * @return name
     */
    public Vector getNames(){
	return name;
    }

    /**
     * sets rMax equal to the maximum pH value and rMin to the minimum
     * pH value entered by the user for the IEF animation
     */
    public static void setRange( double max, double min ){
	rMax = max;
	rMin = min;
	// calculate the final width
	myWidth = 563/100;
    }

}












