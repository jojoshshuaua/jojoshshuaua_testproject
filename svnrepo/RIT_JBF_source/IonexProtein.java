/*
 * IonexProtein.java
 */

import java.net.URI;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;

/**
 * Represents a protein made up of AminoAcid objects.
 * @author Kyle Dewey
 */
public class IonexProtein implements Comparable< IonexProtein > {
    // begin constants
    public static final String THREE_LETTER_CODE_DELIM = " - ";
    public static final IonexProteinReader PDB_READER = 
	new IonexProteinPDBReader();
    public static final IonexProteinReader FASTA_READER =
	new IonexProteinFASTAReader();
    public static final IonexProteinReader GENBANK_READER = 
	new IonexProteinGenBankReader();
    public static final Map< javax.swing.filechooser.FileFilter, IonexProteinReader > PROTEIN_READERS =
	new HashMap< javax.swing.filechooser.FileFilter, IonexProteinReader >() {
	{
	    put( PDB_READER.getFileFilter(), PDB_READER );
	    put( FASTA_READER.getFileFilter(), FASTA_READER );
	    put( GENBANK_READER.getFileFilter(), GENBANK_READER );
	}
    };
    public static final CompositeFileFilter PROTEIN_FILE_FILTER =
	new CompositeFileFilter( PROTEIN_READERS.keySet(),
				 "Protein files" );
    // end constants

    // begin statics
    private static Set< IonexProtein > availableProteins = 
	new HashSet< IonexProtein >();
    // end statics

    // begin instance variables
    private String name; // the name of the protein
    private AminoAcid[] sequence; // the sequence of the protein
    private String oneLetterSequence = null;
    private String threeLetterSequence = null;
    private Cache< Double, Double > chargeCache;
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
	chargeCache = new Cache< Double, Double >( new CacheCall< Double, Double >() {
		public Double call( Double input ) {
		    return new Double( getChargeNoCache( input.doubleValue() ) );
		}
	    } );
    }

    public double getCharge( double pH ) {
	return chargeCache.get( new Double( pH ) ).doubleValue();
    }

    /**
     * Gets the charge on the protein at the given pH.
     * @pre The pH is between 0 and 14
     * @param pH The pH the protein is at
     * @return The overall charge of the protein at this pH
     */
    public double getChargeNoCache( double pH ) {
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
     * Gets a string of the single letter codes
     */
    public String oneLetterCodes() {
	if ( oneLetterSequence == null ) {
	    oneLetterSequence = calculateOneLetterCodes();
	}
	return oneLetterSequence;
    }

    /**
     * Makes a string of all the one letter codes
     */
    public String calculateOneLetterCodes() {
	String retval = "";
	for( AminoAcid current : sequence ) {
	    retval += current.oneLetterCode;
	}
	return retval;
    }

    public String tripleLetterCodes() {
	if ( threeLetterSequence == null ) {
	    threeLetterSequence = calculateTripleLetterCodes();
	}
	return threeLetterSequence;
    }

    public String calculateTripleLetterCodes() {
	String retval = "";
	for( AminoAcid current : sequence ) {
	    retval += current.threeLetterCode + THREE_LETTER_CODE_DELIM;
	}
	return retval.substring( 0, THREE_LETTER_CODE_DELIM.length() );
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
    public static Set< IonexProtein > loadAvailableProteins( String path,
							     IonexProteinLoadProgressResponder progress ) 
	throws FileNotFoundException,
	IonexProteinFormatException,
	IOException {
	int proteinNum = 0;
	Set< IonexProtein > proteins = new HashSet< IonexProtein >();
	File[] proteinFiles = new File( path ).listFiles( PROTEIN_FILE_FILTER );
	while( proteinNum < proteinFiles.length &&
	       !progress.stopLoadingProteins() ) {
	    File currentFile = proteinFiles[ proteinNum ];
	    progress.loadingProtein( new IonexProteinLoadProgress( currentFile.getName(),
								   proteinNum,
								   proteinFiles.length ) );
	    IonexProtein currentProtein = loadProtein( currentFile );

	    if ( currentProtein != null ) {
		proteins.add( currentProtein );
	    }
	    proteinNum++;
	}

	return proteins;
    }

    public static Set< IonexProtein > loadAvailableProteins( String path ) 
	throws FileNotFoundException,
	IonexProteinFormatException,
	IOException {
	return loadAvailableProteins( path,
				      new DefaultIonexProteinLoadProgressResponder() );
    }

    /**
     * Version of loadAvailableProteins that won't throw any exceptions.
     */
    public static Set< IonexProtein > loadAvailableProteinsNoError( String path,
								    IonexProteinLoadProgressResponder progress ) {
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

    public static Set< IonexProtein > loadAvailableProteinsNoError( String path ) {
	return loadAvailableProteinsNoError( path,
					     new DefaultIonexProteinLoadProgressResponder() );
    }

    /**
     * gets a reader that can understand the given file
     * @return A reader that can understand the given file, or null if
     * there is no such reader
     */
    public static IonexProteinReader getReader( File file ) {
	IonexProteinReader retval = null;
	FileFilter match = PROTEIN_FILE_FILTER.getAcceptingFilter( file );

	if ( match != null ) {
	    retval = PROTEIN_READERS.get( match );
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
	IonexProteinReader reader = getReader( file );
	IonexProtein retval = null;

	if ( reader != null ) {
	    retval = reader.readProtein( file );
	}

	return retval;
    }

    /**
     * Gets all file filters that understand protein files.
     * They are sorted by their descriptions.
     * @return All file filters that understand protein files
     */
    public static javax.swing.filechooser.FileFilter[] getProteinFileFilters() {
	List< javax.swing.filechooser.FileFilter > retval = 
	    new ArrayList< javax.swing.filechooser.FileFilter >( PROTEIN_READERS.keySet() );
	Collections.sort( retval,
			  new Comparator< javax.swing.filechooser.FileFilter >() {
			      public int compare( javax.swing.filechooser.FileFilter first,
						  javax.swing.filechooser.FileFilter second ) {
				  return first.getDescription().compareTo( second.getDescription() );
			      }
			  } );
	return retval.toArray( new javax.swing.filechooser.FileFilter[ 0 ] );
    }
}
