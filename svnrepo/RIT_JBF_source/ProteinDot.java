/**
 * This class represents a protein after going through the 2DE
 * simulator.  
 *
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 */

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ProteinDot extends Component{
    
    private Graphics graphic;  // the graphics object which draws the dot
    private Electro2D electro2D;  // reference to the main applet class
    private E2DProtein myPro;  // the protein represented by this dot
    private double myMW;  //the molecular weight of the protein
    private Color myCol;  // the color of the dot
    private static final double diameter = 7; //the diameter of the dots
    private static boolean show = false; // tells whether or not to display all
                                         //dots
    private boolean showMe = true; // tells whether or not to hide this 
                                   //individual proteinDot
    private Canvas myCanvas;  // reference to GelCanvas
    private double myX;  // the x coordinate of this dot
    private double myY;  // the y coordinate of this dot
    private int voltage = 0;  // the voltage entered by the user
    private static double lowPercent = -1;  // the % acrylamide entered by the 
                                         //user
    private static double highPercent = -1;
   
    /**
     * constructor
     *
     * @param pro the protein being represented
     * @param canvas canvas to hold the protein
     * @param x the x coordinate for this dot
     * @param y the y coordinate for this dot
     */
    public ProteinDot( E2DProtein pro, Canvas canvas, double x,
		       double y, Electro2D e ){
	myPro = pro; //assign a protein to this dot
	myMW = myPro.getMW();
	electro2D = e; //give the dot a reference to Electro2D
	String v = e.getVoltage(); //get the voltage entered by the user
	try{
	    voltage = Integer.parseInt( v.substring( 0, 
					v.length() - 2 ) );
	}catch( NumberFormatException p ){
	    System.err.println( "The error was " + p.getMessage() );
	}
	
	//get the color for this dot
	myCol = pro.getColor();
	//give the dot a reference to GelCanvas
	myCanvas = canvas;
	//set the initial x and y coordinates of this dot
	myX = x;
	myY = y;
    }

    public ProteinDot( Color c, Canvas canvas,/* double mw,*/ double x, double y, 
		       Electro2D e ){
	//myMW = mw;
	myCol = c;
	myCanvas = canvas;
	myX = x;
	myY = y;
	electro2D = e;
	String v = e.getVoltage();
	try{
	    voltage = Integer.parseInt( v.substring( 0, v.length() - 2 ) );
	}catch(NumberFormatException p){
	    System.err.println( "The error was " + p.getMessage() );
	}
    }

    /**
     * Gives the percent acrylamide value to this class
     */
    public static void setPercent( double lp, double hp ){
	lowPercent = lp;
	highPercent = hp;
    }

    /**
     * Changes the y value of this dot by a factor of the percent acrylamide,
     * voltage used, and the molecular weight of the protein being represented
     */
    public void changeY( ){
	//double mw = myPro.getMW();
	if(lowPercent != highPercent ){
	myY = ( 10 * 1/(((myY-48)*(highPercent - lowPercent)/532)+lowPercent) ) * ( voltage / 25 ) * .25 *
	    ( 100000/myMW ) + myY;
	}
	else{
	    myY = (10 * 1/lowPercent) * ( voltage / 25 ) * .25 * ( 100000/myMW ) + myY;
	}
	myCanvas.repaint();
    }

    /**
     * This method is called when the restart button is pressed.
     * It sets the dot back to its starting position for the animation
     */

    public void restart(){
	myY = 48;
    }

    // accessors - allow canvas to retrieve the current location of the dot

    public double returnX(){
	return myX;
    }

    public double returnY(){
	return myY;
    }
    
    public static int getDiameter( ){
	return (int)diameter;
    }
    
    /**
     * Toggles the value for show.
     */
    public static void setShow(){
	if( show ){
	    show = false;
	}
	else{
	    show = true;
	}
    }
    
    /**
     * Lets the program know whether or not to draw this individual dot to the
     * screen.  To be used when user searches for proteins by function.
     */
    public void doNotShowMe(){
	showMe = false;
    }

    /**
     * Sets showMe to true to display this individual protein.
     */
    public void doShowMe(){
	showMe = true;
    }

    public boolean getShowMe(){
	return showMe;
    }

    /**
     * Returns whether or not the dots are being displayed
     *
     * @return show
     */
    public static boolean getShow(){
	return show;
    }

    /**
     * returns the Protein represented by this dot
     */
    
    public E2DProtein getPro(){
	return myPro;
    }

    public Color getColor(){
	return myCol;
    }

    public void changeColor( Color col ){
	myCol = col;
    }

    /**
     * draw draws the object as a circle of the color specified
     * by the object's protein at the object's coordinates.
     *
     * @param g the Graphics object that will draw the dot
     */

    public void draw( Graphics g ){
	//AlphaComposite ac = AlphaComposite.getInstance( AlphaComposite.SRC_OVER ,  .50f);
	graphic = g;
	//((Graphics2D)graphic).setComposite( ac );
	Ellipse2D.Double dot = new Ellipse2D.Double( myX,
					  myY, diameter, diameter );
	//if the dots are supposed to be shown, draw the dots
	if( show && showMe ){
	    g.setColor( myCol );
	
	    g.drawOval( (int)(myX), (int)(myY), (int)(diameter), (int)(diameter) );
	    g.fillOval( (int)(myX), (int)(myY), (int)(diameter), (int)(diameter) );
	    g.setColor( new Color( 54, 100, 139 ) );
	}
    }
    
    public void startThread(){
	//if( myCol == Color.RED ){
	    /*********************************************
	     * start thread to make this blink
	     ********************************************/
	new BlinkThread( this, (GelCanvas)myCanvas ).start();
	//}
    }



    public void update(){
	draw( graphic );
    }
} //ProteinDot.java














