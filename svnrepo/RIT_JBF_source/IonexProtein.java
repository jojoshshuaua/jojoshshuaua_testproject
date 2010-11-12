/*
 * IonexProtein.java
 */

import java.io.*;
import java.util.*;

/**
 * Represents a protein made up of AminoAcid objects.
 * @author Kyle Dewey
 */
public class IonexProtein implements Comparable< IonexProtein > {
    // begin constants
    public static final String PDB_DIRECTORY_PATH = "./pdb/";
    public static final String PDB_FILE_EXTENSION = ".PDB";
    public static final Map< String, IonexProteinReader > READERS =
	new HashMap< String, IonexProteinReader >() {
	{
	    put( PDB_FILE_EXTENSION, new IonexProteinPDBReader() );
	}
    };
    // end constants

    // begin statics
    private static Set< IonexProtein > availableProteins = 
	loadAvailableProteinsNoError( PDB_DIRECTORY_PATH );
    // end statics

    // begin instance variables
    private String name; // the name of the protein
    private AminoAcid[] sequence; // the sequence of the protein
    // end instance variables

    /**
     * Creates a new protein with the given name and sequence
     *
     * @param name The name of the protein
     * @param sequence The sequence of the protein
     */
    public IonexProtein( String name,
			 AminoAcid[] sequence ) {
	this.name = name;
	this.sequence = sequence;
    }

    /**
     * Gets the charge on the protein at the given pH.
     * @pre The pH is between 0 and 14
     * @param pH The pH the protein is at
     * @return The overall charge of the protein at this pH
     */
    public double getCharge( double pH ) {
	double retval = 0.0;

	for( int x = 0; x < sequence.length; x++ ) {
	    // see if we are at an end - we need to do amino and/or carboxyl
	    if ( x == 0 ) {
		retval += sequence[ x ].getAminoCharge( pH );
	    } 
	    if ( x == sequence.length - 1 ) {
		retval += sequence[ x ].getCarboxylCharge( pH );
	    }

	    // add in the side chain
	    retval += sequence[ x ].getSideChainCharge( pH );
	}

	return retval;
    }


    /**
     * Uses the name of the protein as a key.
     */
    public int hashCode() {
	return name.hashCode();
    }

    public boolean equals( Object other ) {
	return ( other instanceof IonexProtein &&
		 ((IonexProtein)other).getName().equals( this.getName() ) );
    }

    public String getName() {
	return name;
    }

    public String toString() {
	return name;
    }

    /**
     * Compares this protein to another one.
     * The name is used as the basis of comparison.
     */
    public int compareTo( IonexProtein other ) {
	return name.compareTo( other.getName() );
    }

    /**
     * Determines if the given file is a pdb file
     */
    public static boolean isPDBFile( File file ) {
	return file.getName().toUpperCase().endsWith( PDB_FILE_EXTENSION );
    }

    /**
     * Creates a new file filter that is specific to PDB files
     */
    public static FileFilter makePDBFileFilter() {
	return new FileFilter() {
	    public boolean accept( File file ) {
		return isPDBFile( file );
	    }
	};
    }

    /**
     * Gets all the available proteins there are
     */
    public static IonexProtein[] getAvailableProteins() {
	return availableProteins.toArray( new IonexProtein[ 0 ] );
    }

    /**
     * Adds the given protein to the available proteins, but only if
     * it is different from all the proteins within
     *
     * @return true if it was added, else false (false means this was a duplicate)
     */
    public static boolean addAvailableProtein( IonexProtein protein ) {
	boolean retval = !availableProteins.contains( protein );

	if ( retval ) {
	    availableProteins.add( protein );
	}

	return retval;
    }

    /**
     * Loads in all proteins from the given directory path
     */
    public static Set< IonexProtein > loadAvailableProteins( String path ) 
	throws FileNotFoundException,
	IonexProteinFormatException,
	IOException {
	Set< IonexProtein > proteins = new HashSet< IonexProtein >();
	File dirAsFile = new File( path );
	
	for( File currentFile : dirAsFile.listFiles() ) {
	    IonexProtein current = loadProtein( currentFile );

	    if ( current != null ) {
		proteins.add( current );
	    }
	}

	return proteins;
    }

    /**
     * Version of loadAvailableProteins that won't throw any exceptions.
     */
    public static Set< IonexProtein > loadAvailableProteinsNoError( String path ) {
	Set< IonexProtein > retval = new HashSet< IonexProtein >();

	try {
	    retval = loadAvailableProteins( path );
	} catch ( FileNotFoundException e ) {
	    System.err.println( e );
	} catch ( IonexProteinFormatException e ) {
	    System.err.println( e );
	} catch ( IOException e ) {
	    System.err.println( e );
	}

	return retval;
    }

    /**
     * Loads in a protein from the given file.
     * Automatically determines format.
     *
     * @param file The file containing the protein information
     * @return A new protein, or null if the file format was not understood
     * @exception FileNotFoundException if the file could not be found
     * @exception IonexProteinFormatException If there is a format-level error
     * in the file
     * @exception IOException If an error occurred on reading
     */
    public static IonexProtein loadProtein( File file ) 
	throws FileNotFoundException,
	IonexProteinFormatException,
	IOException {
	IonexProtein retval = null;

	if ( isPDBFile( file ) ) {
	    retval = READERS.get( PDB_FILE_EXTENSION ).readProtein( file );
	}

	return retval;
    }
}
