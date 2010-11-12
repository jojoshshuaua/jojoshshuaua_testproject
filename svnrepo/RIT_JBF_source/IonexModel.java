/*
 * IonexModel.java
 */

import java.util.*;
import java.awt.Color;

/**
 * The model for Ionex (as in model-view-controller, or MVC).  Contains
 * all the information for the ion exchange experiment, and is responsible
 * for all the actual computation involved in the experiment.  The view is 
 * updated when changes are made to the model.
 *
 * @author Kyle Dewey
 * @author John Manning
 * @author Kristen Cotton
 */
public class IonexModel implements IonexModelInterface, Runnable {
    // begin constants 
    // related to time
    public static final int NUM_FRAMES = 460;
    public static final int FRAME_DELAY = 100; // milliseconds between frames

    // related to the column itself
    public static final int COLUMN_LOW_Y = 107;
    public static final int COLUMN_HIGH_Y = 305;
    public static final int COLUMN_SIZE_Y = COLUMN_HIGH_Y - COLUMN_LOW_Y;
    public static final int BEYOND_COLUMN = -1;

    // represents all bound proteins, in a single band
    // for simplicity, it is put in the list of unbound proteins, but it
    // will always be in the front of the list if there are any bound
    // proteins
    public static final IonexProtein BOUND_PROTEIN =
	new IonexProtein( "BOUND PROTEINS",
			  new AminoAcid[ 0 ] ) {
		public double getCharge( double pH ) {
		    return Double.NEGATIVE_INFINITY;
		}
	};
    public static final IonexProteinBand BOUND_PROTEIN_BAND = 
	new IonexProteinBand( BOUND_PROTEIN, true ) {
	    public Color getColor() {
		return Color.BLACK;
	    }
	    public boolean isBound() {
		return true;
	    }
	    public int getPosition() {
		return 0;
	    }
	};
    // end constants

    // begin instance variables
    public final double startConcNaCl; // starting concentration of NaCl
    public final double endConcNaCl; // ending concentration of NaCl
    public final Solvent solvent; // the solvent used for the simulation
    public final Resin resin; // the resin used for the simulation
    public final IonexViewInterface view; // the view to update
    private Thread runner; // runs the model
    private boolean running = false; // set to true if we are running
    private int currentFrame = 0;
    private double topConcNaCl; // concentration of NaCl at the
                                // top of the column (currently)
    private double bottomConcNaCl; // concentration of NaCl at the bottom
                                   // of the column (currently)
    private List< IonexProteinBand > boundProteins; // proteins bound at the top
    private List< IonexProteinBand > unboundProteins; // proteins moving in
                                                      // the column
    // end instance variables

    /**
     * Creates a new model.
     */
    public IonexModel( double startConcNaCl,
		       double endConcNaCl,
		       Solvent solvent,
		       Resin resin,
		       IonexProtein[] proteins,
		       IonexViewInterface view ) {
	List< IonexProteinBand > allBands;
	this.startConcNaCl = startConcNaCl;
	this.endConcNaCl = endConcNaCl;
	this.solvent = solvent;
	this.resin = resin;
	this.view = view;

	// at the start, the top and bottom have the starting concentration
	topConcNaCl = bottomConcNaCl = startConcNaCl;

	// put the proteins into bands
	allBands = proteinsToBands( proteins );
	boundProteins = getBoundProteins( allBands );
	unboundProteins = getUnboundProteins( allBands );
	unboundProteins.add( BOUND_PROTEIN_BAND ); // visible proteins

	sortByCharge( unboundProteins );

	// reflect that we are ready in the view
	updateView();
    }

    public static List< IonexProteinBand > getBoundProteins( List< IonexProteinBand > proteins ) {
	List< IonexProteinBand > retval = new ArrayList< IonexProteinBand >();
	for( IonexProteinBand current : proteins ) {
	    if ( current.isBound() ) {
		retval.add( current );
	    }
	}

	return retval;
    }

    public static List< IonexProteinBand > getUnboundProteins( List< IonexProteinBand > proteins ) {
	List< IonexProteinBand > retval = new ArrayList< IonexProteinBand >();
	for( IonexProteinBand current : proteins ) {
	    if ( !current.isBound() ) {
		retval.add( current );
	    }
	}

	return retval;
    }

    /**
     * Gets the protein bands.  This is primarily so the view can determine
     * what colors have been assigned to each protein
     */
    public IonexProteinBand[] getProteinBands() {
	return unboundProteins.toArray( new IonexProteinBand[ 0 ] );
    }

    /**
     * Updates everything on the view.
     * Note that when possible, incremental updates are preferred.
     */
    public void updateView() {
	view.updateProteinPosition();
	updateViewNaClConcentrations();
    }

    /**
     * Updates the NaCl concentrations on the view
     */
    public void updateViewNaClConcentrations() {
	view.updateTopNaClConcentration( topConcNaCl );
	view.updateBottomNaClConcentration( bottomConcNaCl );
    }

    /**
     * Converts the given proteins into a parallel list of protein bands.
     */
    protected List< IonexProteinBand > proteinsToBands( IonexProtein[] proteins ) {
	List< IonexProteinBand > retval = 
	    new ArrayList< IonexProteinBand >( proteins.length );
	
	for( IonexProtein current : proteins ) {
	    IonexProteinBand band = new IonexProteinBand( current );
	    band.setBound( solvent.pH,
			   startConcNaCl,
			   resin );
	    retval.add( band );
	}

	return retval;
    }

    /**
     * Sorts the given list of protein bands by charge.  The most
     * positively charged proteins will be at the front of the list.
     * Destructive.
     */
    protected void sortByCharge( List< IonexProteinBand > proteins ) {
	Collections.sort( proteins,
			  new Comparator< IonexProteinBand >() {
			      public int compare( IonexProteinBand first,
						  IonexProteinBand second ) {
				  double charge1 = first.getProtein().getCharge( solvent.pH );
				  double charge2 = second.getProtein().getCharge( solvent.pH );
				  if ( charge1 > charge2 ) {
				      return -1;
				  } else if ( charge1 < charge2 ) {
				      return 1;
				  } else {
				      return 0;
				  }
			      }
			  } );
    }

    protected void unbindProteins() {
	if ( boundProteins.size() > 0 ) {
	    for( int x = 0; x < boundProteins.size(); x++ ) {
		IonexProteinBand current = boundProteins.get( x );
		current.setBound( solvent.pH,
				  topConcNaCl,
				  resin );
		if ( !current.isBound() ) {
		    boundProteins.remove( x );
		    unboundProteins.add( current );
		    x--;
		}
	    }
	    
	    if ( boundProteins.size() == 0 ) {
		unboundProteins.remove( BOUND_PROTEIN_BAND );
	    }
	}
    }

    /**
     * Moves the proteins in the column
     * @todo Make some of these values constants pending better understanding
     */
    public void moveProteins() {
	boolean changeMade = false;
	unbindProteins();
	for( IonexProteinBand current : unboundProteins ) {
	    current.incrementPosition();
	} // for each protein

	if ( unboundProteins.size() > 0 ) {
	    view.updateProteinPosition();
	}
    } // moveProteins
	    
    /**
     * Determines the current top and bottom concentrations of NaCl.
     *
     * @todo Make some of these values constants pending better
     * understanding of what they actually represent
     */
    public void calcConcNaCl() {
	if ( currentFrame <= 150 ) {
	    topConcNaCl = bottomConcNaCl = startConcNaCl;
	} else if ( currentFrame <= 300 ) {
	    // concentration entering column changes
	    // the time for the initial wash to move through the column
	    topConcNaCl = startConcNaCl + 
		((( endConcNaCl - startConcNaCl ) / 150 ) * ( currentFrame - 150 ) );
	    bottomConcNaCl = startConcNaCl;
	} else if ( currentFrame <= 450 ) {
	    // only final wash entering column
	    topConcNaCl = endConcNaCl;
	    bottomConcNaCl = startConcNaCl + 
		((( endConcNaCl - startConcNaCl ) / 150 ) * ( currentFrame - 300 ) );
	} else {
	    // only the high concentration now
	    topConcNaCl = bottomConcNaCl = endConcNaCl;
	}

	updateViewNaClConcentrations();
    }

    /**
     * Moves on to the next frame
     */
    public void advanceFrame() {
	currentFrame++;

	moveProteins(); // move the proteins in the columns
	calcConcNaCl(); // adjust for changes in amounts of NaCl
	// detector was previously updated here
    }

    /**
     * Actually runs the simulation.
     */
    public void run() {
	//Just to be nice, lower this thread's priority
        //so it can't interfere with other processing going on.
        Thread.currentThread().setPriority( Thread.MIN_PRIORITY );
	
        // Remember the starting time.
        long startTime = System.currentTimeMillis();
	
        // This is the animation loop.
	while( running &&
	       currentFrame <= NUM_FRAMES ) {
	    advanceFrame();
	    try {
		startTime += FRAME_DELAY;
		Thread.sleep( Math.max( 0, startTime - System.currentTimeMillis() ) );
	    } catch ( InterruptedException e ) {
		running = false;
		break;
	    }
	}

	if ( currentFrame > NUM_FRAMES ) {
	    view.simulationDone();
	}
    }

    /**
     * Triggers the start of the simulation
     */
    public void startSimulation() {
	if ( !running ) {
	    running = true;
	    runner = new Thread( this );
	    runner.start();
	}
    }

    /**
     * Triggers the simulation to pause
     */
    public void pauseSimulation() {
	running = false; // at the next clock tick, this will trigger stopping
    }

    public double getPercentageThrough() {
	return (double)currentFrame / NUM_FRAMES;
    }

    /**
     * Gets the amount of the protein that is eluting, a percentage between 0-1.
     * This correlates to how much of the Y axis should be consumed by a given
     * protein.
     */
    public double amountEluting( IonexProteinBand protein ) {
	int midPoint = (int)Math.ceil( (double)protein.getMaxBandWidth() / 2 );
	double amountPer = 1.0 / midPoint;
	int position = proteinPosition( protein.getPosition() );
	double retval;

	if ( position != BEYOND_COLUMN ) {
	    int bandWidth = proteinBandWidth( position,
					      protein.getBandWidth() );
	    if ( bandWidth <= midPoint ) {
		retval = bandWidth * amountPer;
	    } else {
		retval = ( protein.getMaxBandWidth() - bandWidth ) *
		    amountPer;
	    }
	} else {
	    retval = 0.0;
	}

	return retval;
    }

    public static int proteinBandWidth( int position,
					int originalBandWidth ) {
	int retval = originalBandWidth;

	// if the protein started to elute, then the actual
	// band width is different
	if ( position != BEYOND_COLUMN &&
	     position + originalBandWidth > COLUMN_HIGH_Y ) {
	    retval = COLUMN_HIGH_Y - position;
	}

	return retval;
    }

    public static int proteinPosition( int originalPosition ) {
	int retval = originalPosition + COLUMN_LOW_Y;

	if ( retval > COLUMN_HIGH_Y ) {
	    retval = BEYOND_COLUMN;
	}

	return retval;
    }
} // IonexModel
