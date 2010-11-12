/*
 * ColorGradient.java
 */

import java.awt.*;
import java.util.*;

/**
 * Gets colors in a gradient.  The colors cycle over time, in a predictable
 * fashion.
 * 
 * @author Kyle Dewey
 */
public class ColorGradient implements ColorGetter {
    // begin constants
    // maximum RGB value that can be set
    // 3 bytes are used for RGB values, and these are at the lower order
    // each byte is 8 bits.  In -1, all bits are set.
    public static final int MAX_RGB = 
	-1 >>> ( Integer.SIZE - 3 * 8 );
    public static final int MIN_RGB = 0;
    public static final int DEFAULT_INCREMENT = 100;
    public static final Set< Color > DEFAULT_BAD_COLORS = 
	new HashSet< Color >();
    // end constants

    // begin instance variables
    private int increment;
    private Set< Color > badColors;
    private int lastRGB;
    // end instance variables

    public ColorGradient( int increment,
			  Set< Color > badColors ) {
	this.increment = increment;
	this.badColors = badColors;
	lastRGB = MIN_RGB;
    }

    public ColorGradient( int increment ) {
	this( increment,
	      DEFAULT_BAD_COLORS );
    }

    public ColorGradient( Set< Color > badColors ) {
	this( DEFAULT_INCREMENT,
	      badColors );
    }
    
    public ColorGradient() {
	this( DEFAULT_INCREMENT,
	      DEFAULT_BAD_COLORS );
    }

    protected void incrementRGB() {
	lastRGB += increment;
	if ( lastRGB > MAX_RGB ) {
	    lastRGB = MIN_RGB;
	}
    }

    public Color getNextColor() {
	Color retval;

	do {
	    retval = new Color( lastRGB );
	    incrementRGB();
	} while( badColors.contains( retval ) );

	return retval;
    }
}
