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
    public static final int COLUMN_LOW_Y = 25;
    public static final int COLUMN_HIGH_Y = 497;
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
    // end constants

    // begin instance variables
    public final double startConcNaCl; // starting concentration of NaCl
    public final double endConcNaCl; // ending concentration of NaCl
    public final Solvent solvent; // the solvent used for the simulation
    public final Resin resin; // the resin used for the simulation
    public final IonexViewInterface view; // the view to update
    public final IonexProteinBand boundProteinBand;
    private Thread runner; // runs the model
    private boolean running = false; // set to true if we are running
    private int currentFrame = 0;
    private List< Pair< Double, Double > > concTable; // index is frame, item is the top and bottom [NaCl]
    private IonexProteinBand[] allProteins;
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
	this.startConcNaCl = startConcNaCl;
	this.endConcNaCl = endConcNaCl;
	this.solvent = solvent;
	this.resin = resin;
	this.view = view;

	concTable = Arrays.asList( makeConcTable() );
	boundProteinBand = makeBoundProteinBand();
	// put the proteins into bands
	allProteins = proteinsToBands( proteins );
	sortByCharge( allProteins );

	// reflect that we are ready in the view
	updateView();
    }

    /**
     * Makes the bound protein band.
     */
    public IonexProteinBand makeBoundProteinBand() {
	return new IonexProteinBand( BOUND_PROTEIN, this ) {
	    public Color getColor() {
		return Color.BLACK;
	    }
	    public boolean isBound() {
		return true;
	    }
	    public int getPosition( int frame ) {
		return 0;
	    }
	    public int getPosition() {
		return 0;
	    }
	};
    }

    public int getNumProteins() {
	return allProteins.length;
    }

    /**
     * Gets the protein bands for the current frame
     */
    public List< IonexProteinBand > getProteinBands() {
	return getProteinBands( getCurrentFrame() );
    }

   /**
    * Gets the protein bands that should be drawn.  
    * This is intended for the view.
    */
    public List< IonexProteinBand > getProteinBands( int frame ) {
	List< IonexProteinBand > retval = new ArrayList< IonexProteinBand >();
	for( IonexProteinBand current : allProteins ) {
	    if ( current == boundProteinBand ||
		 !current.isBound( frame ) ) {
		retval.add( current );
	    }
	}

	if ( allProteins.length > 0 &&
	     retval.size() < allProteins.length ) {
	    // some proteins must be bound
	    retval.add( boundProteinBand );
	}

	return retval;
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
	Pair< Double, Double > conc = getConcNaCl();
	view.updateTopNaClConcentration( conc.first );
	view.updateBottomNaClConcentration( conc.second );
    }

    /**
     * Converts the given proteins into a parallel list of protein bands.
     */
    protected IonexProteinBand[] proteinsToBands( IonexProtein[] proteins ) {
	IonexProteinBand[] retval = new IonexProteinBand[ proteins.length ];
	
	for( int x = 0; x < retval.length; x++ ) {
	    retval[ x ] = new IonexProteinBand( proteins[ x ], this );
	}

	return retval;
    }

    /**
     * Sorts the given list of protein bands by charge.  The most
     * positively charged proteins will be at the front of the list.
     * Destructive.
     */
    protected void sortByCharge( IonexProteinBand[] proteins ) {
	// according to Java API, changes to the list write through
	// to the array
	List< IonexProteinBand > asList = Arrays.asList( proteins );
	Collections.sort( asList,
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

    /**
     * Gets the position for the given protein
     */
    public int getPosition( double unbindingConc,
			    int frame ) {
	int retval = frame - frameWhenTopNaCl( unbindingConc );
	if ( retval < 0 ) {
	    retval = 0;
	}

	return retval;
    }

    /**
     * Determines the current top and bottom concentrations of NaCl, based on
     * the given frame.
     *
     * @todo Make some of these values constants pending better
     * understanding of what they actually represent
     */
    public Pair< Double, Double > calcConcNaCl( int frame ) {
	double top, bottom;
	if ( frame <= 150 ) {
	    top = bottom = startConcNaCl;
	} else if ( frame <= 300 ) {
	    // concentration entering column changes
	    // the time for the initial wash to move through the column
	    top = startConcNaCl + 
		((( endConcNaCl - startConcNaCl ) / 150 ) * ( frame - 150 ) );
	    bottom = startConcNaCl;
	} else if ( frame <= 450 ) {
	    // only final wash entering column
	    top = endConcNaCl;
	    bottom = startConcNaCl + 
		((( endConcNaCl - startConcNaCl ) / 150 ) * ( frame - 300 ) );
	} else {
	    // only the high concentration now
	    top = bottom = endConcNaCl;
	}

	return new Pair< Double, Double >( new Double( top ),
					   new Double( bottom ) );
    }
	
    /**
     * Gets the top and bottom[NaCl] for the current frame
     */
    public Pair< Double, Double > getConcNaCl() {
	return getConcNaCl( getCurrentFrame() );
    }

    /**
     * Gets the top and bottom [NaCl] for a given frame
     */
    public Pair< Double, Double > getConcNaCl( int frame ) {
	return concTable.get( frame );
    }

    /**
     * Makes a table that associates a given top and bottom [NaCl] with 
     * a frame.  That is, given a top [NaCl], it can associate
     * it with a given frame.  Note that the top and bottom [NaCl] are
     * individually monotonically increasing, so these values are already sorted.
     */
    public Pair< Double, Double >[] makeConcTable() {
	Pair< Double, Double >[] retval = new Pair[ NUM_FRAMES ];
	for( int x = 0; x < retval.length; x++ ) {
	    retval[ x ] = calcConcNaCl( x );
	}
	return retval;
    }
    
    /**
     * Given an index into the concentration table, it will get the largest
     * index in the table at which the top [NaCl] is equal to the top [NaCl]
     * for the given index.  For instance, if given the list:
     * [1,2,2,3,4], and an index of 1, this will return 2.  But with
     * [1,2,3,4] and 1, it will return 1.
     */
    public static int getBiggestIndex( int index,
				List< Pair< Double, Double > > table ) {
	double value = table.get( index ).first.doubleValue();
	int x = index + 1;
	while( x < table.size() &&
	       value == table.get( x ).first.doubleValue() ) {
	    x++;
	}

	return x - 1;
    }

    /**
     * Finds the frame at which the top NaCl is at least the given value, using
     * the given list of concentrations at frames.
     */
    public static int frameWhenTopNaCl( double conc,
					List< Pair< Double, Double > > table ) {
	int retval = Collections.binarySearch( table,
					       new Pair< Double, Double >( conc, 0.0 ),
					       new Comparator< Pair< Double, Double > >() {
						   public int compare( Pair< Double, Double > one,
								       Pair< Double, Double > two ) {
						       return one.first.compareTo( two.first );
						   }
					       } );
	if ( retval < 0 ) {
	    retval = -retval + 1;
	}

	return getBiggestIndex( retval, table );
    }

    public int frameWhenTopNaCl( double conc ) {
	return frameWhenTopNaCl( conc, concTable );
    }

    /**
     * Sets and processes the given frame
     */
    protected void setAndProcessFrame( int frame ) {
	setCurrentFrame( frame );
	processFrame( frame );
    }

    /**
     * Sets the current frame to be the given frame
     */
    public void setCurrentFrame( int frame ) {
	currentFrame = frame;
	if ( !running ) {
	    processFrame( currentFrame );
	}
    }

    /**
     * Increments the current frame.
     */
    public void incrementCurrentFrame() {
	setCurrentFrame( getCurrentFrame() + 1 );
    }

    /**
     * Performs all the work neccessary to process the given frame
     */
    protected void processFrame( int frame ) {
	adjustNaClConc();
	moveProteins( frame );
    }

    public void adjustNaClConc() {
	updateViewNaClConcentrations();
    }

    public void moveProteins( int frame ) {
	view.updateProteinPosition();
    }

    /**
     * Moves on to the next frame
     */
    public void advanceFrame() {
	incrementCurrentFrame();
	processFrame( currentFrame );
    }

    /**
     * Actually runs the simulation.
     */
    public void run() {
	//Just to be nice, lower this thread's priority
        //so it can't interfere with other processing going on.
        Thread.currentThread().setPriority( Thread.MIN_PRIORITY );
	
        // This is the animation loop.
	while( running &&
	       currentFrame + 1 < NUM_FRAMES ) {
	    advanceFrame();
	    try {
		Thread.sleep( FRAME_DELAY );
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
	return getPercentageThrough( getCurrentFrame() );
    }

    public double getPercentageThrough( int frame ) {
	return (double)frame / NUM_FRAMES;
    }

    public int getCurrentFrame() {
	return currentFrame;
    }

    public double amountEluting( IonexProteinBand protein ) {
	return amountEluting( protein,
			      getCurrentFrame() );
    }

    /**
     * Gets the amount of the protein that is eluting, a percentage between 0-1.
     * This correlates to how much of the Y axis should be consumed by a given
     * protein.
     */
    public double amountEluting( IonexProteinBand protein, int frame ) {
	int position = protein.getPosition( frame );
	int bandWidth = IonexProteinBand.BAND_WIDTH;
	double retval;
	
	if ( position + bandWidth < COLUMN_HIGH_Y ||
	     position > COLUMN_HIGH_Y ) {
	    retval = 0.0;
	} else {
	    retval = (double)Math.abs( COLUMN_HIGH_Y - ( position + bandWidth ) ) / bandWidth;
	}
	return retval;
    }
} // IonexModel
