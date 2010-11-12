/*
 * IonexProteinBand.java
 */

import java.util.*;
import java.awt.*;

/**
 * Represents a protein band in an ionex experiment.
 *
 * @author Kyle Dewey
 * @author John Manning
 * @author Kristen Cotton
 */
public class IonexProteinBand {
    // begin constants
    public static final int UNBOUND_BAND_WIDTH = 3;
    public static final double UNBINDING_NACL = 100;
    
    // colors which we don't want to assign because they are hard to
    // see
    public static final Set< Color > BAD_COLORS =
	new HashSet< Color >() {
	{
	    add( Color.BLACK );
	    add( Color.LIGHT_GRAY );
	    add( Color.WHITE );
	    add( Color.YELLOW );
	}
    };
    // end constants

    // begin statics
    private static ColorGetter colorGetter = new ColorGradient( BAD_COLORS );
    private static Map< IonexProtein, IonexProteinBand > assignedBands = 
	new HashMap< IonexProtein, IonexProteinBand >();
    // end statics

    // begin instance variables
    private int maxBandWidth = 0; // the maximum bandwidth ever set
    private int oldPosition; // the band's old position in the column
    private int position; // the band's position in the column
    private int oldBandWidth = 0; // the previous width of the protein band
    private int bandWidth = 0; // the width of the protein band
    private Color color; // the color of the protein band
    private IonexProtein protein; // the actual protein
    private boolean isBound; // if the protein in the band is bound
    // end instance variables

    /**
     * Creates a new, unbound protein.
     */
    public IonexProteinBand( IonexProtein protein ) {
	this( protein, false );
    }

    /**
     * Creates a new protein band.
     */
    public IonexProteinBand( IonexProtein protein,
			     boolean isBound ) {
	this.protein = protein;
	setBandWidth( UNBOUND_BAND_WIDTH );
	setBound( isBound );
	color = colorGetter.getNextColor(); 
	assignedBands.put( protein, this );
    }

    public static IonexProteinBand getBand( IonexProtein protein ) {
	return assignedBands.get( protein );
    }

    /**
     * Clears out the colors that have been assigned to this protein
     */
    public static void resetColors() {
	colorGetter = new ColorGradient( BAD_COLORS );
    }

    public static void resetBands() {
	assignedBands.clear();
    }

    public static void reset() {
	resetColors();
	resetBands();
    }

    /**
     * gets the color of the protein
     */
    public Color getColor() {
	return color;
    }

    /**
     * Sets the band width
     */
    public void setBandWidth( int bandWidth ) {
	oldBandWidth = this.bandWidth;
	this.bandWidth = bandWidth;
	maxBandWidth = Math.max( bandWidth, maxBandWidth );
    }

    /**
     * Gets the width of the band, without any attempt to recalculate it.
     */
    public int getBandWidth() {
	return bandWidth;
    }

    /**
     * Sets whether or not the protein is bound.
     */
    public void setBound( boolean isBound ) {
	this.isBound = isBound;
    }

    /**
     * Sets whether or not this protein is bound, based on the
     * current experimental conditions
     *
     * @param pH The pH it is at
     * @param concNaCl The concentration of NaCl
     * @param resin The resin that it can bind to
     */
    public void setBound( double pH,
			  double concNaCl,
			  Resin resin ) {
	double charge = protein.getCharge( pH );

	// a protein is not bound if it has the same charge as
	// the resin or if its charge is less than the initial
	// salt concentration
	setBound( ( resin.charge == Resin.CHARGE.POSITIVE && 
		    charge < -concNaCl * UNBINDING_NACL ) ||
		  ( resin.charge == Resin.CHARGE.NEGATIVE && 
		    charge > concNaCl *  UNBINDING_NACL ) );
    }

    /**
     * Gets whether or not this protein is bound
     */
    public boolean isBound() {
	return isBound;
    }

    /**
     * Gets the position of this band
     */
    public int getPosition() {
	return position;
    }

    /**
     * Sets the position of this band
     */
    public void setPosition( int position ) {
	oldPosition = this.position;
	this.position = position;
    }

    /**
     * Increments the protein's position
     */
    public void incrementPosition() {
	setPosition( getPosition() + 1 );
    }

    /**
     * gets the protein contained within
     */
    public IonexProtein getProtein() {
	return protein;
    }

    public int getOldPosition() {
	return oldPosition;
    }

    public int getOldBandWidth() {
	return oldBandWidth;
    }

    public int getMaxBandWidth() {
	return maxBandWidth;
    }

    public String toString() {
	return protein.toString();
    }
}
