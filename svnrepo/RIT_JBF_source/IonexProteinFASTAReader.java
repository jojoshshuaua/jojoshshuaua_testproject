/*
 * IonexProteinFASTAReader.java
 */

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Reads in a protein from a FASTA file.
 *
 * @author Kyle Dewey
 */
public class IonexProteinFASTAReader implements IonexProteinReader {
    // begin constants
    public static final Pattern ANNOTATION_LINE = 
	Pattern.compile( "^>(.*).*" );
    public static final int ANNOTATION_LINE_GROUP = 1;
    // end constants

    /**
     * Determines if the given line is an annotation line
     * @param line The line to check
     * @return The annotation for the line, or null if it is not an annotation
     * line
     */
    public static String getAnnotation( String line ) {
	String retval = null;
	Matcher matcher = ANNOTATION_LINE.matcher( line );

	if ( matcher.matches() ) {
	    retval = matcher.group( ANNOTATION_LINE_GROUP );
	}

	return retval;
    }

    public IonexProtein readProtein( File file ) 
	throws FileNotFoundException,
	       IonexProteinFormatException,
	       IOException {
	Scanner input = new Scanner( file );
	StringBuffer sequence = new StringBuffer( "" );
	StringBuffer name = new StringBuffer( "" );

	while( input.hasNextLine() ) {
	    String line = input.nextLine();
	    String annotation = getAnnotation( line );

	    if ( annotation != null ) {
		name.append( annotation );
	    } else {
		sequence.append( IonexProteinGenBankReader.stripNonAminoAcid( line ) );
	    }
	}
	input.close();

	return new IonexProtein( name.toString(),
				 IonexProteinGenBankReader.getAminoAcids( sequence.toString() ) );
    }
}
				 