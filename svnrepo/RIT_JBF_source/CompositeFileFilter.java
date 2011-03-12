/*
 * CompositeFileFilter.java
 */

import java.util.*;
import java.io.*;

/**
 * Given a bunch of file filters, it will combine them into a single filter.
 * If any of the underlying filters accept a file, it will accept.
 * @author Kyle Dewey
 */
public class CompositeFileFilter implements FileFilter {
    // begin instance variables
    private List< FileFilter > fileFilters; 
    // end instance variables

    public CompositeFileFilter( Collection< FileFilter > fileFilters ) {
	this.fileFilters = new ArrayList< FileFilter >( fileFilters );
    }

    /**
     * Gets the first file filter that accepts the given file.
     * @return null if none of them do
     */
    public FileFilter getAcceptingFilter( File file ) {
	FileFilter retval = null;

	for( FileFilter current : fileFilters ) {
	    if ( current.accept( file ) ) {
		retval = current;
		break;
	    }
	}

	return retval;
    }

    public boolean accept( File file ) {
	return getAcceptingFilter( file ) != null;
    }
}

	       