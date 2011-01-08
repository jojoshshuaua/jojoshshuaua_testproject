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
    public static final double INCREMENT = 2.36;
    public static final int BAND_WIDTH = 3;
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
    private IonexModel model; // the model we are associated with
    private double[] position; // given a frame, returns the protein's position
    private Color color; // the color of the protein band
    private IonexProtein protein; // the actual protein
    // end instance variables

    /**
     * Creates a new protein band.
     */
    public IonexProteinBand( IonexProtein protein,
			     IonexModel model ) {
	this.model = model;
	this.protein = protein;
	initializePosition();
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
     * initializes the position array
     */
    protected void initializePosition() {
	double unbindingConc = getUnbindingConc( model.solvent.pH,
						 model.resin );
	position = new double[ IonexModel.NUM_FRAMES ];
	for( int frame = 0; frame < position.length; frame++ ) {
	    position[ frame ] = model.getPosition( unbindingConc,
						   frame ) * INCREMENT;
	}
    }

    /**
     * gets the color of the protein
     */
    public Color getColor() {
	return color;
    }

    /**
     * Gets the concentration at which the given protein will unbind for
     * the given resin and pH.  Note that if the protein will not bind, it will
     * return 0.
     *
     * @param pH The pH the protein is in
     * @param resin The resin the protein is in.
     *
     * @return The [NaCl] the protein will unbind at
     */
    public double getUnbindingConc( double pH,
				    Resin resin ) {
	double charge = protein.getCharge( pH );
	double retval;

	if ( ( resin.charge == Resin.CHARGE.POSITIVE &&
	       charge > 0 ) ||
	     ( resin.charge == Resin.CHARGE.NEGATIVE &&
	       charge < 0 ) ) {
	    // won't stick no matter what
	    retval = 0;
	} else {
	    retval = Math.abs( charge / UNBINDING_NACL );
	}

	return retval;
    }
	     
    /**
     * Gets whether or not this protein is bound
     */
    public boolean isBound( int frame ) {
	return position[ frame ] <= 0.0;
    }

    /**
     * Gets the position of this band for the current frame
     */
    public int getPosition() {
	return getPosition( model.getCurrentFrame() );
    }

    /**
     * Gets the position of this band for the given frame
     */
    public int getPosition( int frame ) {
	return (int)Math.round( position[ frame ] );
    }

    /**
     * gets the protein contained within
     */
    public IonexProtein getProtein() {
	return protein;
    }

    public String toString() {
	return protein.toString();
    }
}
