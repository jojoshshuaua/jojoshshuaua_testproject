/*
 * LineGraph.java
 */

import java.util.*;
import java.awt.*;

/**
 * A simple line graph.  Utilizes lines in a Graphics object.
 *
 * @author Kyle Dewey
 */
public class LineGraph {
    private class Point {
	public int x;
	public int y;
	public Point( int x, int y ) {
	    this.x = x;
	    this.y = y;
	}
    }
    // begin constants
    public static final Color DEFAULT_COLOR = Color.BLACK;
    public static final Color BACKGROUND_COLOR = IonexView.BACKGROUND_COLOR;
    // end constants

    // begin instance variables
    private Component parent; // what to draw the lines on
    private Color color; // the color to make the graph
    private java.util.List< Point > points; // the points to draw
    // end instance variables

    /**
     * Creates a new line graph of DEFAULT_COLOR
     */
    public LineGraph( Component parent ) {
	this( parent,
	      DEFAULT_COLOR );
    }

    /**
     * Creates a new line graph of the given color
     */
    public LineGraph( Component parent,
		      Color color ) {
	this.parent = parent;
	this.color = color;
	points = new ArrayList< Point >();
    }

    /**
     * Resets the graph
     */
    public void reset() {
	points.clear();
    }

    /**
     * Adds a point to the given graph.  Note that it does not
     * actually update the graph
     */
    public void addPoint( int x, int y ) {
	points.add( new Point( x, y ) );
    }

    /**
     * Draws up to the given point.
     */
    public void paintComponent( Graphics g, 
				int cutoff ) {
	paintComponent( color, 
			g, 
			cutoff );
    }

    /**
     * Draws a line of a given color
     */
    public void paintComponent( Color color,
				Graphics g,
				int cutoff ) {
	int size = points.size();
	int limit = ( cutoff < size ) ? cutoff : size;

	if ( size >= 2 ) {
	    Point first = points.get( 0 );
	    for( int x = 1; x < limit; x++ ) {
		Point current = points.get( x );
		drawLine( first, 
			  current, 
			  color,
			  g );
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
}
