/*
 * LineGraph.java
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;

/**
 * A simple line graph.  Utilizes lines in a Graphics object.
 *
 * @author Kyle Dewey
 */
public class LineGraph extends JLabel {
    // begin constants
    public static final String IONEX_GRAPH_PICTURE_PATH = "graph.gif";
    public static final String DETECTOR_NAME = "DETECTOR";
    public static final Color DETECTOR_COLOR = Color.BLUE;
    public static final String CONCENTRATION_NAME = "CONCENTRATION";
    public static final Color CONCENTRATION_COLOR = Color.RED;
    public static final Map< String, Color > DEFAULT_COLOR_MAP =
	new HashMap< String, Color >() {
	{
	    put( DETECTOR_NAME, DETECTOR_COLOR );
	    put( CONCENTRATION_NAME, CONCENTRATION_COLOR );
	}
    };
    // end constants

    // begin instance variables
    protected IonexView view; // view this graph is associated with
    protected Map< String, Color > colors; // color of each line
    protected Map< String, java.util.List< Point > > points; // points for each line
    // end instance variables

    /**
     * Creates a new line graph that is associated with the given view,
     * and that has the given line names.
     * @param view The view we are associated with
     * @param colors The colors for each line
     */
    public LineGraph( IonexView view,
		      Map< String, Color > colors ) {
	super( new ImageIcon( IONEX_GRAPH_PICTURE_PATH ) );
	this.view = view;
	this.colors = colors;
	points = colorsToPoints( colors );
    }

    /**
     * Creates a new line graph with DEFAULT_COLOR_MAP
     */
    public LineGraph( IonexView view ) {
	this( view,
	      DEFAULT_COLOR_MAP );
    }

    public Set< String > lineNames() {
	return colors.keySet();
    }

    /**
     * Verifies that the given name is valid
     */
    public void verifyName( String name ) throws UnknownLineException {
	if ( !colors.containsKey( name ) ) {
	    throw new UnknownLineException( "Unknown line with name: " + name );
	}
    }

    /**
     * Resets all graphs
     */
    public void reset() {
	try {
	    for( String current : colors.keySet() ) {
		reset( current );
	    }
	} catch ( UnknownLineException e ) {
	    // shouldn't be possible
	    System.err.println( e );
	}
    }

    /**
     * Resets the graph with the given name
     */
    public void reset( String name ) throws UnknownLineException {
	verifyName( name );
	points.get( name ).clear();
    }

    public void addPoint( String name,
			  int x, int y ) throws UnknownLineException {
	addPoint( name,
		  new Point( x, y ) );
    }

    /**
     * Adds a point to the given graph.  Note that it does not
     * actually update the graph
     */
    public void addPoint( String name, 
			  Point point ) throws UnknownLineException {
	verifyName( name );
	points.get( name ).add( point );
    }

    /**
     * Draws all lines up to the current frame.
     */
    public void paintComponent( Graphics graphics ) {
	super.paintComponent( graphics );
	IonexModel model = view.getModel();
	if ( model != null ) {
	    paintComponent( model.getCurrentFrame(),
			    graphics );
	}
    }

    /**
     * Draws all lines up to the given cutoff
     */
    public void paintComponent( int cutoff,
				Graphics graphics ) {
	try {
	    for( String name : colors.keySet() ) {
		paintComponent( name,
				cutoff,
				graphics );
	    }
	} catch ( UnknownLineException e ) {
	    // shouldn't be possible
	    System.err.println( e );
	}
    }

    /**
     * Draws the given line up to the given point.
     */
    public void paintComponent( String name,
				int cutoff,
				Graphics graphics ) throws UnknownLineException {
	verifyName( name );
	paintComponent( colors.get( name ),
			points.get( name ),
			cutoff,
			graphics );
    }

    /**
     * Draws a line of a given color
     */
    public void paintComponent( Color color,
				java.util.List< Point > points,
				int cutoff,
				Graphics graphics ) {
	int size = points.size();
	int limit = ( cutoff < size ) ? cutoff : size;
	if ( size >= 2 ) {
	    Point first = points.get( 0 );
	    for( int x = 1; x < limit; x++ ) {
		Point current = points.get( x );
		drawLine( first, 
			  current, 
			  color,
			  graphics );
		first = current;
	    }
	}
    }

    protected void drawLine( Point first,
			     Point second,
			     Color color,
			     Graphics g ) {
	Color originalColor = g.getColor();
	g.setColor( color );
	g.drawLine( first.x, first.y,
		    second.x, second.y );
	g.setColor( originalColor );
    }
    
    /**
     * Given a mapping of line names to colors of those lines,
     * it will return a mapping of the same names to empty lists
     */
    public static Map< String, java.util.List< Point > > 
	colorsToPoints( Map< String, Color > colors ) {
	Map< String, java.util.List< Point > > retval = 
	    new HashMap< String, java.util.List< Point > >( colors.size() );
	for( String name : colors.keySet() ) {
	    retval.put( name, new ArrayList< Point >() );
	}
	return retval;
    }
}
