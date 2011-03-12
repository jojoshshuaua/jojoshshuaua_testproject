/*
 * ClickableLineGraph.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *      $Log$
 *
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Clickable version of a line graph.
 * Points on the graph are allowed to be clicked, which causes protein
 * information to be displayed.
 * Note that this assumes that all graphs will have the same X values!
 *
 * @author Kyle Dewey
 */
public class ClickableLineGraph extends LineGraph implements MouseListener {
    // begin constants
    public static final int NO_FRAME = -1;
    // end constants

    // begin instance variables
    // given an x value, gets which frame was clicked
    private java.util.List< Integer > xToFrame = null;
    // end instance variables

    public ClickableLineGraph( IonexView view,
			       Map< String, Color > colors ) {
	super( view, colors );
	initialize();
    }
    public ClickableLineGraph( IonexView view ) {
	super( view );
	initialize();
    }

    /**
     * Performs initialization specific to ClickableLineGraph.
     */
    protected void initialize() {
	addMouseListener( this );
    }

    /**
     * Generates a mapping of X values to frame values.
     */
    protected java.util.List< Integer > makeXToFrame() {
	String lineName = lineNames().iterator().next();
	java.util.List< Point > frameToPoint = points.get( lineName );
	java.util.List< Integer > retval = new ArrayList< Integer >();

	// before the graph begins
	addToList( new Integer( NO_FRAME ),
		   frameToPoint.get( 0 ).x,
		   retval );

	// between each point
	for( int frame = 1; frame < frameToPoint.size(); frame++ ) {
	    addToList( new Integer( frame - 1 ),
		       frameToPoint.get( frame ).x - frameToPoint.get( frame - 1 ).x,
		       retval );
	}

	return retval;
    }

    /**
     * Gets the number of the frame given a mouse event from a click.
     * I.e. what frame we were on for the click position.
     * returns NO_FRAME if there is no frame that correlates to this x
     * position.
     */
    public int getClickedFrame( MouseEvent event ) {
	int retval = NO_FRAME;

	if ( xToFrame == null ) {
	    xToFrame = makeXToFrame();
	}

	if ( event.getX() < xToFrame.size() ) {
	    retval = xToFrame.get( event.getX() ).intValue();
	}

	return retval;
    }

    /**
     * Shows the proteins for the given frame.
     * Assumes the frame is valid.
     * Doesn't show any proteins if there are none for the frame
     */
    public void showProteins( int frame ) {
	IonexModel model = view.getModel();

	if ( model != null &&
	     frame <= model.getCurrentFrame() ) {
	    Set< IonexProteinBand > eluting = 
		model.getProteinsEluting( frame );
	    if ( eluting.size() > 0 ) {
		new IonexProteinDescriber( eluting ).show();
	    }
	}
    }

    public void mouseClicked( MouseEvent e ) {
	if ( view.getModel() != null ) {
	    int frame = getClickedFrame( e );
	    if ( frame != NO_FRAME ) {
		showProteins( frame );
	    }
	}
    }

    public void mouseEntered( MouseEvent e ) {}
    public void mouseExited( MouseEvent e ) {}
    public void mousePressed( MouseEvent e ) {}
    public void mouseReleased( MouseEvent e ) {}

    public void reset( String name ) throws UnknownLineException {
	super.reset( name );
	xToFrame = null;
    }

    /**
     * Adds the given item to a list the given number of times.
     */
    public static < T > void addToList( T item,
					int numTimes,
					java.util.List< T > list ) {
	while( numTimes > 0 ) {
	    list.add( item );
	    numTimes--;
	}
    }
}
