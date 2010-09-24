/*
 * Swing version of the ProteinDot class.
 */

/**
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 * @author Amanda Fisher
 */

import java.awt.*;
import javax.swing.*;

public class ProteinDotSwingVersion extends Component {

    private Graphics graphic;
    private E2DProtein myProtein;
    private double myMolecularWeight;
    private Color myColor;
    private static final double DIAMETER = 7;
    private static boolean showAllDots = false;
    private boolean showMe = true;
    private JPanel myPanel;
    private double myX;
    private double myY;
    private static double minPercentAcrylamide = -1;
    private static double maxPercentAcrylamide = -1;

    /**
     * constructor
     *
     * @param pro the protein being represented
     * @param canvas JPanel to hold the protein
     * @param x the x coordinate for this dot
     * @param y the y coordinate for this dot
     */
    public ProteinDotSwingVersion(E2DProtein pro, JPanel panel, double x, double y) {
        myProtein = pro;
	myMolecularWeight = myProtein.getMW();
	myColor = myProtein.getColor();
	myPanel = panel;
	myX = x;
	myY = y;
    }

    public ProteinDotSwingVersion( Color c, JPanel panel, double x, double y) {
	myColor = c;
	myPanel = panel;
	myX = x;
	myY = y;
    }

    /**
     * gives the percent acrylamide value to this class
     *
     * @param lp minimum acrylamide percent
     * @param hp maximum acrylamide percent
     */
    public static void setPercent( double lp, double hp ){
	minPercentAcrylamide = lp;
	maxPercentAcrylamide = hp;
    }

    /**
     * changes the y value of this dot by a factor of the percent acrylamide
     * and molecular weight of the protein being represented
     */
    public void changeY( ){

	if (minPercentAcrylamide != maxPercentAcrylamide) {
	myY = (10 * 1 / (((myY - 48) * (maxPercentAcrylamide -
               minPercentAcrylamide)/532) + minPercentAcrylamide)) * (2) *
               .25 * (100000/myMolecularWeight) + myY;
	} else {
	    myY = (10 * 1 / minPercentAcrylamide) * (2) * .25 *
                  (100000/myMolecularWeight ) + myY;
	}
	myPanel.repaint();
    }

    /**
     * This method is called when the restart button is pressed.
     * It sets the dot back to its starting position for the animation
     */

    public void restart() {
	myY = 48;
    }

    /**
     * accessor method
     *
     * @return x coordinate of the dot protein
     */
    public double returnX() {
	return myX;
    }

    /**
     * accessor method
     *
     * @return y coordinate of teh dot protein
     */
    public double returnY() {
	return myY;
    }

    /**
     * accessor method
     *
     * @return diameter of protein dots (always will be 7)
     */
    public static int getDiameter() {
	return (int)DIAMETER;
    }

    /**
     * Toggles the value for show.
     */
    public static void setShow() {
	if(showAllDots) {
	    showAllDots = false;
	}
	else {
	    showAllDots = true;
	}
    }

    /**
     * Lets the program know whether or not to draw this individual dot to the
     * screen.  To be used when user searches for proteins by function.
     */
    public void doNotShowMe() {
	showMe = false;
    }

    /**
     * Sets showMe to true to display this individual protein.
     */
    public void doShowMe() {
	showMe = true;
    }

    /**
     * accessor method
     *
     * @return whether or not to show this protein dot
     */
    public boolean getShowMe() {
	return showMe;
    }

    /**
     * accessor method
     *
     * @return whether or not all the dots are being displayed
     */
    public static boolean getShow() {
	return showAllDots;
    }

    /**
     * accessor method
     *
     * @return protein represented by this dot
     */
    public E2DProtein getPro() {
	return myProtein;
    }

    /**
     * accessor method
     *
     * @return color of this protein dot
     */
    public Color getColor() {
	return myColor;
    }

    /**
     * changes the color of this protein dot
     *
     * @param col new color for the protein dot
     */
    public void changeColor(Color col) {
	myColor = col;
    }

    /**
     * draws the object as a circle of the color specified
     * by the object's protein at the object's coordinates.
     *
     * @param g the Graphics object that will draw the dot
     */
    public void draw(Graphics g) {
	graphic = g;

	if(showAllDots && showMe) {
	    g.setColor(myColor);
	    g.drawOval((int)(myX), (int)(myY), (int)(DIAMETER), (int)(DIAMETER));
	    g.fillOval((int)(myX), (int)(myY), (int)(DIAMETER), (int)(DIAMETER));
	    g.setColor(new Color(54, 100, 139));
	}
    }

    public void update(){
	draw( graphic );
    }

}
