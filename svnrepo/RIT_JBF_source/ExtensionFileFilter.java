/*
 * ExtensionFileFilter.java
 */

import java.io.*;
import java.util.*;

/**
 * File filter that does its work based on file extensions.
 * If a listed extension matches, then it accepts it.
 * @author Kyle Dewey
 */
public class ExtensionFileFilter implements FileFilter {
    // begin instance variables
    private Set< String > extensions; // recognized extensions
    // end instance variables

    public ExtensionFileFilter( Collection< String > extensions ) {
	this.extensions = new HashSet< String >( extensions );
    }

    public ExtensionFileFilter( String[] extensions ) {
	this( Arrays.asList( extensions ) );
    }

    public boolean accept( File file ) {
	return extensions.contains( getExtension( file ) );
    }

    /**
     * Gets the extension from the given file.
     * @return The file extension, or "" if there is no extension
     */
    public static String getExtension( File file ) {
	String retval = "";
	String name = file.getName().toUpperCase();
	int last = name.lastIndexOf( "." );

	if ( last != -1 ) {
	    retval = name.substring( last );
	}

	return retval;
    }
}
