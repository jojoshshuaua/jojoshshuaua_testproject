/**
 * This class simulates a protein.  It stores the protein's molecular
 * weight, pI, identity, and links to informative internet sites
 * about it.
 * 
 * @author Jill Zapoticznyj
 */

import java.awt.*;
import java.util.Random;  // randomly generates colors for protein
//import java.net.URL;  // represents a URL for a website
import java.util.HashMap;

public class E2DProtein {

    // the color which will be presented by the protein
    // in the simulation

    private static final Color colors[] = {
        /*Color.BLUE, Color.RED, Color.GREEN,*/ Color.YELLOW,
	new Color( 254, 143, 74 ), new Color( 160, 11, 206 ), 
	new Color( 72, 100, 100 ), new Color( 0, 95, 95 ), Color.CYAN,
        new Color( 158, 49, 49 ), new Color( 0, 135, 16 ),
	new Color( 255, 96, 0 ) };  
    
    private String myID; //protein title
    private double myMolWt; //molecular weight
    private double myPI; //pI value
    private Color myColor; //color
    private String mySequence; //amino acid sequence data
    private static Color rnaColor = Color.BLUE;
    private static Color dnaColor = Color.RED;
    private static Color enzymeColor = Color.GREEN;
    private static Color hypotheticalColor = Color.PINK;
    private static Color transportColor = new Color( 117, 92, 50 );
    private static Color receptorColor = new Color( 176, 196, 222 );
    private static Color transductionColor = new Color( 255, 216, 202 );
    private String myFunction;

    /**
     * constructor for protein
     *
     * @param id the protein's type
     * @param molWt the molecular weight of the protein
     * @param pI the pI value for the protein
     * @param urls the URL for information about this protein
     * @param fcn the function of this protein
     */

    public E2DProtein( String id, double molWt, double pI, String sequence, 
		    String fcn ){
	myID = id;
	myMolWt = molWt;
	mySequence = sequence;
	myFunction = fcn;
	myPI = pI;
	if( id.indexOf( "DNA" ) != -1 || id.indexOf( "dna" ) != -1 ){
	    myColor = dnaColor;
	}
	else if(/* id.indexOf( "RNA" ) != -1 ||*/
		 id.indexOf( "ribosomal" ) != -1 
		 /*||id.indexOf( "rna" ) != -1*/ ){
	    myColor = rnaColor;
	}
	else if( id.indexOf( "hypothetical" ) != -1 ){
	    myColor = hypotheticalColor;
	}
	else if(fcn.length() > 6 && fcn.substring( 0, 6 ).equals( "Enzyme" ) ){
	    myColor = enzymeColor;
	}
	else if( fcn.indexOf( "transport" ) != -1 ){
	    myColor = transportColor;
	}
	else if( fcn.indexOf( "receptor" ) != -1 || fcn.indexOf( "reception" )
		 != -1 ){
	    myColor = receptorColor;
	}
	else if( fcn.indexOf( "transduction" ) != -1 ){
	    myColor = transductionColor;
	    System.out.println( "TRANSDUCTION " + myFunction );
	}
	else{
	    Random r = new Random();
	    myColor = colors[ r.nextInt( 9 ) ];
	}
    }

    /**
     * accessors
     */

    /**
     * returns protein title
     *
     * @return myID
     */
    public String getID(){
	return myID;
    }

    /**
     * returns molecular weight
     *
     * @return myMolWt
     */
    public double getMW(){
	return myMolWt;
    }

    /**
     * returns color
     *
     * @return myColor
     */
    public Color getColor(){
	return myColor;
    }

    /**
     * returns pI value
     *
     * @return myPI
     */
    public double getPI(){
	return myPI;
    }
    
    /**
     * returns sequence data
     *
     * @return mySequence
     */
    public String getSequence(){
	return mySequence;
    }

    /**
     * creates a string representation of this object
     *
     * @return myID
     */
    public String toString(){
	return myID;
    }

    /**
     * returns protein function
     *
     * @return myFunction
     */
    public String getFunction(){
	return myFunction;
    }
    
    /**
     * returns colors
     *
     * @return HashMap of default colors
     */
    public static HashMap getColorGuide(){
	
	HashMap retVal = new HashMap();
	
	
	retVal.put( "dna in Title", dnaColor );
	retVal.put( "ribosomal in Title", rnaColor );
	retVal.put( "Enzyme EC in Function", enzymeColor );
	retVal.put( "hypothetical protein", hypotheticalColor );
	retVal.put( "transport protein in Function", transportColor );
	retVal.put( "receptor in Function", receptorColor );
	retVal.put( "transduction in Function", transductionColor );
	
	return retVal;
    }

} // Protein.java







