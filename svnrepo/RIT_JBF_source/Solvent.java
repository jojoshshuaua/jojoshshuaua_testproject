/*
 * Solvent.java
 */

import java.util.*;

/**
 * Represents a solvent for usage in Ionex.  Note that these are intended
 * to be used as singletons.
 *
 * @author Kyle Dewey
 */
public class Solvent {
    // begin statics
    private static Set< Solvent > solvents = 
	new HashSet< Solvent >() {
	{
	    add( new Solvent( "Sodium Acetate, pH 4.8", 4.8 ) );
	    add( new Solvent( "Sodium Phosphate, pH 7.2", 7.2 ) );
	    add( new Solvent( "Tris HCl, pH 8.0", 8.0 ) );
	}
    };
    // end statics

    // begin instance variables
    public final String name; // the name of the solvent
    public final double pH; // the pH of the solvent
    // end instance variables

    /**
     * Creates a new solvent.
     */
    private Solvent( String name, double pH ) {
	this.name = name;
	this.pH = pH;
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
     * Gets all available solvents, in no particular order.
     */
    public static Solvent[] getSolvents() {
	return solvents.toArray( new Solvent[ solvents.size() ] );
    }

    /**
     * Gets all solvents, sorted by pH.  The lower pH goes first
     */
    public static Solvent[] getSolventsSorted() {
	List< Solvent > asList = new ArrayList< Solvent >( solvents );
	Collections.sort( asList,
			  new Comparator< Solvent >() {
			      public int compare( Solvent first,
						  Solvent second ) {
				  if ( first.pH < second.pH ) {
				      return -1;
				  } else if ( first.pH > second.pH ) {
				      return 1;
				  } else {
				      return 0;
				  }
			      }
			  } );
	return asList.toArray( new Solvent[ solvents.size() ] );
    }
}
