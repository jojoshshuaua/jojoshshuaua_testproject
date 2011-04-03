/*
 * CompositeFileFilter.java
 */

import java.util.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;

/**
 * Given a bunch of file filters, it will combine them into a single filter.
 * If any of the underlying filters accept a file, it will accept.
 * @author Kyle Dewey
 */
public class CompositeFileFilter extends DescriptionFileFilter {
    // begin instance variables
    private List< javax.swing.filechooser.FileFilter > fileFilters; 
    // end instance variables

    public CompositeFileFilter( Collection< javax.swing.filechooser.FileFilter > fileFilters,
				String description ) {
	super( description );
	this.fileFilters = new ArrayList< javax.swing.filechooser.FileFilter >( fileFilters );
    }

    /**
     * Gets the first file filter that accepts the given file.
     * @return null if none of them do
     */
    public javax.swing.filechooser.FileFilter getAcceptingFilter( File file ) {
	javax.swing.filechooser.FileFilter retval = null;

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

	       