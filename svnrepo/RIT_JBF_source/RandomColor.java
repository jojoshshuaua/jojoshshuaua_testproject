/*
 * RandomColor.java
 */

import java.util.*;
import java.awt.*;

/**
 * For the generation of random colors.
 * Prefers colors that are defined as constants in Color.
 *
 * @author Kyle Dewey
 */
public abstract class RandomColor {
    // begin constants
    public static final int MAX_RGB = 255;
    private static Random random = new Random();
    // end constants

    // colors which will be assigned before truly random colors are
    // attempted to be generated
    public static final Set< Color > PREFERRED_COLORS =
	new HashSet< Color >() {
	{
	    add( Color.BLACK );
	    add( Color.BLUE );
	    add( Color.CYAN );
	    add( Color.DARK_GRAY );
	    add( Color.GRAY );
	    add( Color.GREEN );
	    add( Color.LIGHT_GRAY );
	    add( Color.MAGENTA );
	    add( Color.ORANGE );
	    add( Color.PINK );
	    add( Color.WHITE );
	    add( Color.YELLOW );
	}
    };
    // end constants

    /**
     * gets a random element from the given set
     * @return a random element, or null if there are no elements in
     * the given set
     */
    public static < T > T getRandomElement( Set< T > set ) {
	T retval = null;

	if ( set.size() > 0 ) {
	    int currentNum = 0;
	    int whichOne = random.nextInt( set.size() );
	    for( T currentElement : set ) {
		if ( currentNum == whichOne ) {
		    retval = currentElement;
		    break;
		}
		currentNum++;
	    }
	}

	return retval;
    }
     
    /**
     * Returns a truly random color
     */
    public static Color getAnyRandomColor() {
	int maxRGBExclusive = MAX_RGB + 1;
	return new Color( random.nextInt( maxRGBExclusive ),
			  random.nextInt( maxRGBExclusive ),
			  random.nextInt( maxRGBExclusive ) );
    }

    /**
     * Returns a truly random color not seen in the given set
     */
    public static Color getAnyRandomColor( Set< Color > seenColors ) {
	Color retval;

	do {
	    retval = getAnyRandomColor();
	} while( seenColors.contains( retval ) );

	return retval;
    }
	
    /**
     * Returns a new random color not seen in the given set.
     * Will prefer colors seen in PREFERRED_COLORS.
     */
    public static Color getRandomColor( Set< Color > seenColors ) {
	// PREFERRED_COLORS - seenColors = primary candidates
	Color retval;
	Set< Color > primeChoices = new HashSet< Color >( PREFERRED_COLORS );
	primeChoices.removeAll( seenColors );

	if ( primeChoices.size() > 0 ) {
	    retval = getRandomElement( primeChoices );
	} else {
	    retval = getAnyRandomColor( seenColors );
	}

	return retval;
    }
}
