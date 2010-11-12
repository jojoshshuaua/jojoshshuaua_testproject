/*
 * Resin.java
 */

import java.util.*;

/**
 * Represents a resin used for Ionex.
 *
 * @author Kyle Dewey
 */
public class Resin {
    // begin constants
    // note that "UNCHARGED" is not an option, unlike with amino acids
    public enum CHARGE { POSITIVE, NEGATIVE };
    // end constants

    // begin statics
    private static Set< Resin > resins = 
	new HashSet< Resin >() {
	{
	    add( new Resin( "CM - Sephadex", CHARGE.NEGATIVE ) );
	    add( new Resin( "DEAE - Sephadex", CHARGE.POSITIVE ) );
	}
    };
    // end statics

    // begin instance variables
    public final String name; // the name of the resin
    public final CHARGE charge; // the charge of the resin
    // end instance variables

    /**
     * Creates a new resin
     */
    private Resin( String name, CHARGE charge ) {
	this.name = name;
	this.charge = charge;
    }

    /**
     * Gets the hash code, based on the name 
     */
    public int hashCode() {
	return name.hashCode();
    }

    public String toString() {
	return name;
    }

    /**
     * Gets all available resins
     */
    public static Resin[] getResins() {
	return resins.toArray( new Resin[ resins.size() ] );
    }
}
