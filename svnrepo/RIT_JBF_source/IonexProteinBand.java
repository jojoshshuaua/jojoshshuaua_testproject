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
public class IonexProteinBand implements Comparable< IonexProteinBand > {
    // begin constants
    public static final double INCREMENT = 2.36;
    public static final int BAND_WIDTH = 3;
    public static final double UNBINDING_NACL = 100;
    public static final Comparator< Double > POSITION_COMPARE =
	new Comparator< Double >() {
	public int compare( Double first,
			    Double second ) {
	    double diff = first.doubleValue() - second.doubleValue();
	    if ( diff < 0.0 ) {
		return -1;
	    } else if ( diff > 0.0 ) {
		return 1;
	    } else {
		return 0;
	    }
	}
    };

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
    private IonexModel model = null; // the model we are associated with
    private java.util.List< Double > position = null; // given a frame, returns the protein's position
    private Color color = null; // the color of the protein band
    private IonexProtein protein = null; // the actual protein
    // end instance variables

    /**
     * Dud constructor.  In some cases, we need to perform certain
     * operations that require an adaptor of a band. (i.e. searching
     * for proteins with a given position using binarySearch)
     */
    private IonexProteinBand() {}
	
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

    /**
     * initializes the position array
     */
    protected void initializePosition() {
	double unbindingConc = getUnbindingConc( model.solvent.pH,
						 model.resin );
	position = new ArrayList< Double >( IonexModel.NUM_FRAMES );
	for( int frame = 0; frame < IonexModel.NUM_FRAMES; frame++ ) {
	    
	    position.add( new Double( model.getPosition( unbindingConc,
							 frame ) * INCREMENT ) );
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
	       charge > 0.0 ) ||
	     ( resin.charge == Resin.CHARGE.NEGATIVE &&
	       charge < 0.0 ) ) {
	    // won't stick no matter what
	    retval = 0.0;
	} else {
	    retval = Math.abs( charge / UNBINDING_NACL );
	}

	return retval;
    }
	     
    /**
     * Gets whether or not this protein is bound
     */
    public boolean isBound( int frame ) {
	return position.get( frame ).doubleValue() <= 0.0;
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
	return (int)Math.round( position.get( frame ).doubleValue() );
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

    /**
     * Compares this protein to another using the underlying protein.
     */
    public int compareTo( IonexProteinBand other ) {
	return getProtein().compareTo( other.getProtein() );
    }

    /**
     * Gets the amount of the protein that is eluting, a percentage between 0-1.
     * This correlates to how much of the Y axis should be consumed by a given
     * protein.
     */
    public double amountEluting( int frame ) {
	int pos = getPosition( frame );
	int bandWidth = IonexProteinBand.BAND_WIDTH;
	double retval;
	
	if ( pos + bandWidth < IonexModel.COLUMN_HIGH_Y ||
	     pos > IonexModel.COLUMN_HIGH_Y ) {
	    retval = 0.0;
	} else {
	    retval = (double)Math.abs( IonexModel.COLUMN_HIGH_Y - ( pos + bandWidth ) ) / bandWidth;
	}
	return retval;
    }

    /**
     * Determines the frame at which this protein starts eluting.
     */
    public int calcStartEluting() {
	int index = Collections.binarySearch( position,
					      new Double( IonexModel.COLUMN_HIGH_Y ),
					      POSITION_COMPARE );
	int retval = IonexModel.getSmallestIndex( position,
						  index,
						  POSITION_COMPARE ) - 1;
	if ( retval < 0 ) {
	    retval = 0;
	}
	return retval;
    }
    
    /**
     * Gets the frames during which this protein is eluting.
     */
    public java.util.List< Integer > getElutingFrames() {
	java.util.List< Integer > retval = new ArrayList< Integer >();
	int frame = calcStartEluting();
	
	while( frame < IonexModel.NUM_FRAMES &&
	       amountEluting( frame ) > 0.0 ) {
	    retval.add( new Integer( frame ) );
	    frame++;
	}

	return retval;
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
     * Creates a special "fake" band that always returns the given
     * position for both getPosition functions.
     * Note that this returns null for everything else
     */
    public static IonexProteinBand makePositionProtein( final int position ) {
	return new IonexProteinBand() {
	    public int getPosition() {
		return position;
	    }
	    public int getPosition( int frame ) {
		return position;
	    }
	};
    }
}
