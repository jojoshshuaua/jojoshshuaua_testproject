/*
 * IonexProteinPDBReader.java
 */

import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.filechooser.FileFilter;

/**
 * Reads in an Ionex protein from a PDB file
 *
 * @author Kyle Dewey
 */
public class IonexProteinPDBReader implements IonexProteinReader {
    // begin constants
    public static final String[] PDB_FILE_EXTENSIONS =
	new String[]{ ".PDB", ".ENT" };
    public static final javax.swing.filechooser.FileFilter PDB_FILTER =
	new ExtensionFileFilter( PDB_FILE_EXTENSIONS,
				 "PDB files" );

    // must begin with ATOM and be the alpha carbon.  Captures the
    // three letter code
    public static final Pattern AMINO_ACID_LINE = 
	Pattern.compile( "^ATOM.{9}CA..(...).*" );
    public static final Pattern NAME_LINE = 
	Pattern.compile( "^HEADER *(.*)" );
    public static final int NAME_GROUP = 1;
    public static final int AMINO_ACID_CODE_GROUP = 1;
    public static String UNKNOWN_AMINO_ACID_ERROR = 
	"Unknown three letter amino acid code: ";
    // end constants

    public IonexProteinPDBReader() {}

    /**
     * Gets an amino acid from the given single letter code
     */
    public static AminoAcid getAminoAcid( char code )
	throws UnknownAminoAcidException {
	return getAminoAcid( code + "" );
    }

    /**
     * Gets an amino acid from the given three or one letter code.
     * @param code The three letter code
     * @return The amino acid
     * @exception UnknownAminoAcidException If the code is unknown
     */
    public static AminoAcid getAminoAcid( String code ) throws UnknownAminoAcidException {
	AminoAcid retval = AminoAcid.getAminoAcid( code );
	if ( retval == null ) {
	    throw new UnknownAminoAcidException( UNKNOWN_AMINO_ACID_ERROR + code );
	}
	return retval;
    }

    /**
     * Gets an amino acid from a line of a PDB file.
     *
     * @param line The line of the PDB file
     * @return An amino acid, or null if the line didn't contain an amino acid
     * @exception UnknownAminoAcidException If the amino acid wasn't known
     */
    public static AminoAcid parseAminoAcidLine( String line ) throws UnknownAminoAcidException {
	AminoAcid retval = null;
	Matcher matcher = AMINO_ACID_LINE.matcher( line );

	if ( matcher.matches() ) {
	    retval = getAminoAcid( matcher.group( AMINO_ACID_CODE_GROUP ) );
	}

	return retval;
    }

    /**
     * Parses the line as a name
     * @return The name, or null if it wasn't a name line
     */
    public static String parseNameLine( String line ) {
	String retval = null;
	Matcher matcher = NAME_LINE.matcher( line );

	if ( matcher.matches() ) {
	    retval = matcher.group( NAME_GROUP );
	}

	return ( retval == null ) ? retval : retval.trim();
    }

    public javax.swing.filechooser.FileFilter getFileFilter() {
	return PDB_FILTER;
    }

    public IonexProtein readProtein( File file ) throws FileNotFoundException,
							IonexProteinFormatException,
							IOException {
	List< AminoAcid > aminoAcids = new LinkedList< AminoAcid >();
	Scanner input = new Scanner( file );
	String name = "";

	while( input.hasNextLine() ) {
	    String line = input.nextLine();
	    String newName = parseNameLine( line );

	    if ( newName != null ) {
		name = newName;
	    } else {
		AminoAcid current = parseAminoAcidLine( line );
		if ( current != null ) {
		    aminoAcids.add( current );
		}
	    }
	}
	input.close();

	return new IonexProtein( name,
				 aminoAcids.toArray( new AminoAcid[ 0 ] ) );
    }
}
