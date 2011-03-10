/*
 * IonexProteinGenBankReader.java
 */

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Reads in a protein from a GenBank file.
 *
 * @author Kyle Dewey
 */
public class IonexProteinGenBankReader implements IonexProteinReader {
    // begin constants
    public static final String ORIGIN = "ORIGIN";

    // for determining if this is a DNA or protein file
    public static final Pattern DNA_OR_AA =
	Pattern.compile( "^LOCUS\\s*\\w*\\s*\\d*\t(..).*" );
    public static final int DNA_OR_AA_GROUP = 1;
    public static final String DNA = "BP";
    public static final String AMINO_ACID = "AA";
    public static final char QUOTE = '"';

    // for determining if we are in coding sequence
    public static final Pattern IN_CDS =
	Pattern.compile( "^\\s*/translation.*" );

    // for separating protein from non-protein
    public static final Pattern REPLACE_NON_AMINO_ACID =
	Pattern.compile( "[^acdefghiklmnpqrstvwy]", 
			 Pattern.CASE_INSENSITIVE );

    // for getting the name of a protein
    public static final Pattern GET_NAME =
	Pattern.compile( "^DEFINITION\\s*(.*).*" );
    public static final int GET_NAME_GROUP = 1;
    // end constants

    public IonexProteinGenBankReader() {}

    /**
     * Gets the type of the file (DNA or protein) that this genbank file is.
     * @param firstLine The first line of the file
     * @return DNA, AMINO_ACID, or null if the file type could not be determined
     */
    public static String getFileType( String firstLine ) {
	String retval = null;
	Matcher matcher = DNA_OR_AA.matcher( firstLine );

	if ( matcher.matches() ) {
	    String toVerify = matcher.group( DNA_OR_AA_GROUP ).toUpperCase();
	    if ( toVerify.equals( DNA ) ||
		 toVerify.equals( AMINO_ACID ) ) {
		retval = toVerify;
	    }
	}

	return retval;
    }

    /**
     * Gets the number of times the given character appears in the given string
     */
    public static int getTimesSeen( String string, char character ) {
	int retval = 0;

	for( int x = 0; x < string.length(); x++ ) {
	    if ( string.charAt( x ) == character ) {
		retval++;
	    }
	}

	return retval;
    }

    /**
     * Gets all the text contains within quotes.
     * Assumes that the opening and ending quote are within the given
     * text.
     */
    public static String getTextInQuotes( String text ) {
	int openingQuote = text.indexOf( QUOTE );
	int endingQuote = text.lastIndexOf( QUOTE );
	return text.substring( openingQuote + 1,
			       endingQuote );
    }

    /**
     * Gets all the text contained within quotes.
     * Assumes the quotes are set up like they are in a genbank file.
     * @param initial The initial line, containing the opening quote
     * @param input The scanner to get more text from, if needed
     * @post The scanner is moved beyond the closing quote
     * @return All the text between the quotes, without the quotes
     */
    public static String getTextInQuotes( String initial, Scanner input ) 
	throws IOException {
	StringBuffer buffer = new StringBuffer( initial );
	
	if ( getTimesSeen( initial, QUOTE ) < 2 ) {
	    // need to read in until we see the other quote
	    boolean seenOther = false;

	    while( input.hasNextLine() &&
		   !seenOther ) {
		String nextLine = input.nextLine();
		buffer.append( nextLine );
		if ( nextLine.indexOf( QUOTE ) != -1 ) {
		    seenOther = true;
		}
	    }
	}

	return getTextInQuotes( buffer.toString() );
    }

    /**
     * Strips out all non-amino acid characters from the given string.
     * case insensitive
     */
    public static String stripNonAminoAcid( String string ) {
	Matcher matcher = REPLACE_NON_AMINO_ACID.matcher( string );
	return matcher.replaceAll( "" );
    }

    /**
     * Reads in a protein file.  Returns all the one letter codes
     */
    public static String readProteinFile( Scanner input ) 
	throws IOException {
	boolean inOrigin = false;
	StringBuffer retval = new StringBuffer( "" );

	while( input.hasNextLine() &&
	       !inOrigin ) {
	    if ( input.nextLine().startsWith( ORIGIN ) ) {
		inOrigin = true;
	    }
	}

	// at this point, we are in protein sequence
	while( input.hasNextLine() ) {
	    retval.append( stripNonAminoAcid( input.nextLine() ) );
	}
	input.close();

	return retval.toString();
    }

    /**
     * Reads in a DNA file.  Returns all the one letter codes
     */
    public static String readDNAFile( Scanner input ) 
	throws IOException {
	StringBuffer retval = new StringBuffer( "" );

	while( input.hasNextLine() ) {
	    String line = input.nextLine();
	    if ( IN_CDS.matcher( line ).matches() ) {
		retval.append( getTextInQuotes( line, input ) );
	    }
	}
	input.close();

	return retval.toString();
    }
    
    /**
     * Gets an array of amino acids based on a string containing single
     * letter amino acid codes
     */
    public static AminoAcid[] getAminoAcids( String sequence ) 
	throws UnknownAminoAcidException {
	AminoAcid[] retval = new AminoAcid[ sequence.length() ];

	for( int x = 0; x < retval.length; x++ ) {
	    retval[ x ] = IonexProteinPDBReader.getAminoAcid( sequence.charAt( x ) );
	}

	return retval;
    }

    /**
     * Gets the name of the given protein
     * @param nameLine The line containing the name.  Assumed to be the second line
     */
    public static String getName( String nameLine ) {
	String retval = "";
	Matcher matcher = GET_NAME.matcher( nameLine );

	if ( matcher.matches() ) {
	    retval = matcher.group( GET_NAME_GROUP );
	}
	
	return retval.trim();
    }

    public IonexProtein readProtein( File file ) 
	throws FileNotFoundException,
	       IonexProteinFormatException,
	       IOException {
	Scanner input = new Scanner( file );
	String fileType = getFileType( input.nextLine() );
	String sequence = null;
	String name = getName( input.nextLine() );

	if ( fileType.equals( DNA ) ) {
	    sequence = readDNAFile( input );
	} else if ( fileType.equals( AMINO_ACID ) ) {
	    sequence = readProteinFile( input );
	} else {
	    throw new IonexProteinFormatException( "Could not determine if given " +
						   "GenBank file: " + file.getName() +
						   " contained amino acids or DNA" );
	}

	return new IonexProtein( name,
				 getAminoAcids( sequence ) );
    }
}
