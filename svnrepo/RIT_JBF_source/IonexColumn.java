/*
 * IonexColumn.java
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Represents the column in ionex.
 * The column shows what proteins are inside of it, along with the
 * [NaCl] that is at the top and the bottom of the column
 *
 * @author Kyle Dewey
 * @author John Manning
 * @author Kristen Cotton
 */
public class IonexColumn extends JLabel implements MouseListener {
    // begin constants
    public static final Color DEFAULT_COLUMN_COLOR = Color.WHITE;
    public static final String IONEX_COLUMN_PICTURE_PATH = "column.gif";

    // showing actual NaCl value
    public static final Font NACL_CONC_FONT = new Font( "Monospaced",
							Font.PLAIN,
							20 );
    public static final Color NACL_CONC_COLOR = Color.GRAY;
    public static final int TOP_NACL_READ_X = IonexView.COLUMN_END_X + 35;
    public static final int TOP_NACL_READ_Y = IonexView.COLUMN_START_Y + 20;
    public static final int BOTTOM_NACL_READ_X = TOP_NACL_READ_X;
    public static final int BOTTOM_NACL_READ_Y = IonexView.COLUMN_END_Y - 10;

    // compares protein bands based on position
    public static final Comparator< IonexProteinBand > COMPARE_ON_POSITION =
	new Comparator< IonexProteinBand >() {
	public int compare( IonexProteinBand first,
			    IonexProteinBand second ) {
	    return first.getPosition() - second.getPosition();
	}
    };
    // end constants

    // begin instance variables
    private IonexView view; // view this column is associated with
    private int columnStartX; // starting X position of the column
    private int columnEndX; // ending X position of the column
    private int columnStartY; // starting Y position of the column
    private int columnEndY; // ending Y position of the column
    private Color color; // the background color of the column
    // proteins in the column
    private java.util.List< IonexProteinBand > sortedProteins = null; 
    // end instance variables

    /**
     * Creates a new column that will be drawn on the given graphics 
     * object.  The column will be of the given size.
     */
    public IonexColumn( IonexView view,
			Color color,
			int columnStartX,
			int columnStartY,
			int columnEndX,
			int columnEndY ) {
	super( new ImageIcon( IONEX_COLUMN_PICTURE_PATH ) );
	this.view = view;
	this.color = color;
	this.columnStartX = columnStartX;
	this.columnEndX = columnEndX;
	this.columnStartY = columnStartY;
	this.columnEndY = columnEndY;
	addMouseListener( this );
    }
    
    /**
     * Creates a column with the default background color
     */
    public IonexColumn( IonexView view,
			int columnStartX,
			int columnStartY,
			int columnEndX,
			int columnEndY ) {
	this( view,
	      DEFAULT_COLUMN_COLOR,
	      columnStartX,
	      columnStartY,
	      columnEndX,
	      columnEndY );
    }

    /**
     * Draws the given protiein on the column
     * @pre The previous location is clear
     */
    protected void drawProtein( IonexProteinBand protein,
				Graphics g ) {
	Color originalColor = g.getColor();
	int position = protein.getPosition();
	int bandWidth = IonexProteinBand.BAND_WIDTH;
	
	if ( position < IonexModel.COLUMN_HIGH_Y ) {
	    g.setColor( protein.getColor() );
	    g.fillRect( columnStartX,
			position + IonexModel.COLUMN_LOW_Y,
			columnEndX - columnStartX,
			bandWidth );
	    g.setColor( originalColor );
	}
    }

    /**
     * Updates the column with the given protein information.
     * The column is cleared before updating.  Note that proteins
     * will be updated in order or decreasing position (greatest
     * position first).
     */
    public void drawProteins( java.util.List< IonexProteinBand > proteins,
			      Graphics g ) {
	sortByPositionDesc( proteins );
	sortedProteins = proteins;

	for ( IonexProteinBand current : proteins ) {
	    drawProtein( current, g );
	}
    }

    /**
     * Assumes the model is valid.
     */
    public void drawProteins( IonexModel model,
			      Graphics g ) {
	drawProteins( model.getProteinBands(), g );
    }

    /**
     * Updates the column with the given model
     * Assumes the model is valid
     */
    public void updateColumn( IonexModel model,
			      Graphics g ) {
	Pair< Double, Double > conc = model.getConcNaCl();
	drawProteins( model, g );
	drawNumber( conc.first.doubleValue(),
		    TOP_NACL_READ_X,
		    TOP_NACL_READ_Y,
		    g );
	drawNumber( conc.second.doubleValue(),
		    BOTTOM_NACL_READ_X,
		    BOTTOM_NACL_READ_Y,
		    g );
    }

    /**
     * Blanks out the entire column visually.
     * Doesn't update the graphics object.
     */
    public void blankColumn( Graphics g ) {
	Color originalColor = g.getColor();
	g.setColor( color );
	g.fillRect( columnStartX,
		    columnStartY,
		    columnEndX - columnStartX,
		    columnEndY - columnEndY );
	g.setColor( originalColor );
    }

    public void paintComponent( Graphics g ) {
	super.paintComponent( g );
	IonexModel model = view.getModel();
	if ( model != null ) {
	    updateColumn( model, g );
	}
    }

    /**
     * Determines if the given event is in the column
     */
    public boolean inColumn( MouseEvent e ) {
        return ( e.getX() >= columnStartX &&
		 e.getX() <= columnEndX &&
		 e.getY() >= columnStartY &&
		 e.getY() <= columnEndY );
    }

    /**
     * Gets all the proteins clicked on for the given mouseEvent.
     */
    public java.util.List< IonexProteinBand > getProteinsClicked( MouseEvent e ) {
	if ( inColumn( e ) ) {
	    return getProteinsAt( e.getY() );
	} else {
	    return new ArrayList< IonexProteinBand >();
	}
    }

    public void mouseClicked( MouseEvent e ) {
	java.util.List< IonexProteinBand > clicked = getProteinsClicked( e );

	if ( clicked.size() > 0 ) {
	    new IonexProteinDescriber( clicked ).show();
	}
    }

    public void mouseEntered( MouseEvent e ) {}
    public void mouseExited( MouseEvent e ) {}
    public void mousePressed( MouseEvent e ) {}
    public void mouseReleased( MouseEvent e ) {}

    /**
     * Gets all proteins at the given position
     */
    public java.util.List< IonexProteinBand > getProteinsAt( int position ) {
	int start = position - IonexProteinBand.BAND_WIDTH;

	if ( position < 0 ) {
	    position = 0;
	}

	return getProteinsBetween( start, position );
    }

    /**
     * Gets proteins between the two given positions.
     */
    public java.util.List< IonexProteinBand > getProteinsBetween( int start,
								  int end ) {
	if ( sortedProteins != null ) {
	    return getProteinsBetween( sortedProteins,
				       start,
				       end );
	} else {
	    return new ArrayList< IonexProteinBand >();
	}
    }

    /*public void setBounds( int x, int y, int w, int h ) {
	super.setBounds( x, y,
			 columnEndX - columnStartX,
			 columnEndY - columnStartY );
			 }*/

    /**
     * Sorts the given proteins according to position
     * The protein with the greatest position is put first
     * Note that this is destructive.
     */
    public static void sortByPositionDesc( java.util.List< IonexProteinBand > proteins ) {
	Collections.sort( proteins,
			  COMPARE_ON_POSITION );
    }

    /**
     * Takes a list of protein bands sorted by position.
     * Gets all proteins between the two given positions.
     */
    public static java.util.List< IonexProteinBand > 
	getProteinsBetween( java.util.List< IonexProteinBand > proteins,
			    int startPosition,
			    int endPosition ) {
	int startIndex = Collections.binarySearch( proteins,
						   IonexProteinBand.makePositionProtein( startPosition ),
						   COMPARE_ON_POSITION );
	int endIndex = Collections.binarySearch( proteins,
						 IonexProteinBand.makePositionProtein( endPosition ),
						 COMPARE_ON_POSITION );
	startIndex = IonexModel.getSmallestIndex( proteins,
						  startIndex,
						  COMPARE_ON_POSITION );
	endIndex = IonexModel.getBiggestIndex( proteins,
					       endIndex,
					       COMPARE_ON_POSITION );
	return proteins.subList( startIndex,
				 endIndex );
    }

    /**
     * Formats the given number to have some number of leading digits
     * and no more than two digits after the decimal
     */
    public static String formatFloat( double number ) {
	String asString = Double.toString( number );
	int position = asString.indexOf( '.' );

	if ( !( position < 0 ||
		position + 3 > asString.length() ) ) {
	    asString = asString.substring( 0, position + 3 );
	}

	return asString;
    }

    /**
     * Formats the given number and draws it at the given position
     */
    public static void drawNumber( double number,
				   Color color,
				   Font font,
				   int x,
				   int y,
				   Graphics g ) {
	Font originalFont = g.getFont();
	Color originalColor = g.getColor();
	g.setFont( font );
	g.setColor( color );
	g.drawString( formatFloat( number ),
		      x, y );
	g.setColor( originalColor );
	g.setFont( originalFont );
    }

    /**
     * Like <code>drawNumber</code>, but it uses NACL_CONC_COLOR and
     * NACL_CONC_FONT
     */
    public static void drawNumber( double number,
				   int x,
				   int y,
				   Graphics g ) {
	drawNumber( number,
		    NACL_CONC_COLOR,
		    NACL_CONC_FONT,
		    x, y, g );
    }
}
