/**
 * This class, which extends the java.awt.Choice class and implements
 * the ItemListener interface, responds to a user selecting a range
 * for the IEF animation.  If the range is "Enter A Range" the text field
 * for the range is made functional.  Otherwise, it will be a static image.
 *
 * @author Jill Zapoticznyj
 */

import java.awt.*;
import java.awt.event.*;

public class RangeChoice extends Choice implements ItemListener {
    
    private Electro2D electro2D; //a reference to Electro2D

    /**
     * Constructor - creates a RangeChoice object
     *
     * @param e - a reference to the Electro2D class
     */
    public RangeChoice( Electro2D e ){
	super(); //initialize the parent class
	electro2D = e; //give the RangeChoice a reference to Electro2D
	
	//have RangeChoice register itself as an ItemListener in order to
	//allow for changes based on the item the user selects
	addItemListener( this );
    }

    /**
     * This method is called whenever the user makes a selection from the
     * Choice object
     *
     * @param e - the event
     */
    public void itemStateChanged( ItemEvent e ){
	//if the user selected "Enter A Range", make the text fields
	// for entering the information functional
	if( getSelectedItem().equals( "Enter A Range" ) ){
	    electro2D.allowSelectRange();
	}
	
	//deactivate the text fields for entering the information
	else{
	    electro2D.disableSelectRange();
	}
    }
}
