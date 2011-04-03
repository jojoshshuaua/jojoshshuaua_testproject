/*
 * DescriptionFileFilter.java
 */

import javax.swing.filechooser.FileFilter;
import java.io.*;

/**
 * Represents a file filter with a description.
 * @author Kyle Dewey
 */
public abstract class DescriptionFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
    // begin instance variables
    private String description; // the description of this filter
    // end instance variables

    /**
     * Creates a new file filter with the given description.
     * @param description The description to use
     */
    public DescriptionFileFilter( String description ) {
	this.description = description;
    }

    /**
     * Gets the description of this filter
     * @return A description of this filter
     */
    public String getDescription() {
	return description;
    }
}

