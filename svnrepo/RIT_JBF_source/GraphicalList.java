/*
 * GraphicalList.java
 */

import java.awt.*;
import javax.swing.*;

/**
 * A textual list that is drawn on a Graphics object.
 *
 * @author Kyle Dewey
 */
public class GraphicalList {
    // begin constants
    public static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    public static final int MAX_X = Integer.MAX_VALUE;
    public static final int MAX_Y = Integer.MAX_VALUE;
    // end constants 

    // begin instance variables
    private Component parent;
    private String[] lastStrings = new String[ 0 ];
    private Color[] lastColors = new Color[ 0 ];
    private int startX; // the starting x position of the list
    private int maxX; // the maximum X position we can draw to
    private int startY; // the starting y position of the list
    private int maxY; // the maximum y position we can draw to
    // end instance variables

    /**
     * Creates a new list with the given starting coordinates and
     * maximal coordinates that we can write to
     */
    public GraphicalList( Component parent,
			  int startX,
			  int startY,
			  int maxX,
			  int maxY ) {
	this.parent = parent;
	this.startX = startX;
	this.startY = startY;
	this.maxX = maxX;
	this.maxY = maxY;
    }

    /**
     * Creates a new list with the given starting coordinates and
     * the default maximal coordinates
     */
    public GraphicalList( Component parent,
			  int startX,
			  int startY ) {
	this( parent,
	      startX,
	      startY,
	      MAX_X,
	      MAX_Y );
    }

    public void clearArea( Color background,
			   Graphics g ) {
	Color originalColor = g.getColor();
	g.setColor( background );
	g.fillRect( startX,
		    startY,
		    maxX - startX,
		    maxY - startY );
	g.setColor( originalColor );
    }

    /**
     * Gets the height of the given string
     */
    public int getHeight( String string,
			  Graphics g ) {
	return (int)Math.ceil( g.getFontMetrics()
			        .getStringBounds( string, g )
			        .getHeight() );
    }

    /**
     * Gets the width of the given string
     */
    public int getWidth( String string,
			 Graphics g ) {
	return (int)Math.ceil( g.getFontMetrics()
			        .getStringBounds( string, g )
			        .getWidth() );
    }

    /**
     * Truncates the given string so that it will fit within the given
     * size.
     * @param string The string to truncate
     * @param toSize The size to make it fit within
     * @return The truncated string.  If it will fit within the size, then
     * it will not be truncated
     */
    protected String truncateString( String string, 
				     int toSize,
				     Graphics g ) {
	FontMetrics metrics = g.getFontMetrics();
	int size = getWidth( string, g );
	
	// incrementally take off a character
	while( size > toSize &&
	       string.length() > 0 ) {
	    string = string.substring( 0, string.length() - 1 );
	    size = getWidth( string, g );
	}

	return string;
    }

    /**
     * Prints the given information in the list.  Note that it will not
     * update the graphics object.
     * 
     * @param toPrint The strings to print.  Each will be placed on its
     * own line
     * @param colors Parallel array of colors for the strings.
     */
    public void printStrings( String[] strings,
			      Color[] colors ) {
	lastStrings = strings;
	lastColors = colors;
	parent.repaint();
    }

    public void paintComponent( Graphics g ) {
	printStrings( lastStrings,
		      lastColors,
		      g );
    }

    protected void printStrings( String[] strings,
				 Color[] colors,
				 Graphics g ) {
	int maxWidth = maxX - startX;
	int currentY = startY; // where to start printing on the x
	
	for( int x = 0; x < strings.length; x++ ) {
	    int height = getHeight( strings[ x ], g );
	    if ( currentY + height > maxY ) {
		break;
	    }
	    
	    printString( truncateString( strings[ x ], 
					 maxWidth,
					 g ),
			 colors[ x ],
			 startX,
			 currentY,
			 g );
	    currentY += height;
	}
    }

    /**
     * Creates an array that contains all of the same color
     */
    public static Color[] makeColorArray( int size, 
					  Color color ) {
	Color[] retval = new Color[ size ];
	for( int x = 0; x < retval.length; x++ ) {
	    retval[ x ] = color;
	}
	return retval;
    }

    /**
     * Prints all the strings using the default color
     */
    public void printStrings( String[] strings ) {
	printStrings( strings,
		      makeColorArray( strings.length,
				      DEFAULT_TEXT_COLOR ) );
    }

    /**
     * Unconditionally prints out the given string with the given color
     * at the given position
     */
    protected void printString( String string,
				Color color,
				int x,
				int y,
				Graphics g ) {
	Color originalColor = g.getColor();
	g.setColor( color );
	g.drawString( string, x, y );
	g.setColor( originalColor );
    }
}
