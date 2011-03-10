/*
 * IonexProteinDescriber.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *      $Log$
 *
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Panel that shows information for proteins.
 * @author Kyle Dewey
 */
public class IonexProteinDescriber 
    extends JPanel implements ListSelectionListener {
    // begin constants
    public static final int NUM_COLUMNS_SEQUENCE = 50;
    public static final String WINDOW_TITLE = "Ionex Protein Information";
    // end constants

    // begin instance variables
    private java.util.List< IonexProteinBand > proteins;
    private JList visualList; // shows all proteins in a list
    private JTextArea sequence; // one letter sequence of the protein
    // end instance variables

    /**
     * Creates a new describer showing the given proteins
     */
    public IonexProteinDescriber( Collection< IonexProteinBand > proteins ) {
	this.proteins = new ArrayList< IonexProteinBand >( proteins );
	Collections.sort( this.proteins );
	visualList = new JList( proteins.toArray() );
	sequence = new JTextArea( "", 0, NUM_COLUMNS_SEQUENCE );
	sequence.setEditable( false );
	sequence.setFont( new Font( "Monospaced",
				    Font.PLAIN,
				    sequence.getFont().getSize() ) );
	
	setLayout( new GridLayout( 2, 1 ) );
	add( visualList );
	add( new JScrollPane( sequence,
			      ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
			      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED ) );
	visualList.addListSelectionListener( this );
	visualList.setSelectedIndex( 0 );
    }

    /**
     * Shows the description for the given protein
     */
    public void showDescription( IonexProteinBand protein ) {
	sequence.setText( truncateCols( protein.getProtein().oneLetterCodes() ) );
    }

    /**
     * Shows a description given the list selection event
     */
    public void showDescription( ListSelectionEvent e ) {
	showDescription( proteins.get( e.getFirstIndex() ) );
    }

    public void valueChanged( ListSelectionEvent e ) {
	showDescription( e );
    }

    public void show() {
	JFrame frame = new JFrame( WINDOW_TITLE );
	frame.add( this );
	frame.pack();
	frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
	frame.setVisible( true );
    }

    /**
     * Like <code>truncateCols</code>, but it uses NUM_COLUMNS_SEQUENCE.
     */
    public static String truncateCols( String text ) {
	return truncateCols( text, NUM_COLUMNS_SEQUENCE );
    }

    /**
     * Truncates a single line of text so that it spans multiple lines.
     * @param text The text to break up
     * @param numCols Number of columns per line
     * @return the reformatted text
     */
    public static String truncateCols( String text,
				       int numCols ) {
	String retval = "";
	String remaining = text;

	do {
	    int numToTake = Math.min( remaining.length(),
				      numCols );
	    retval += remaining.substring( 0, numToTake ) + "\n";
	    remaining = remaining.substring( numToTake );
	} while( remaining.length() > 0 );

	return retval;
    }
}
