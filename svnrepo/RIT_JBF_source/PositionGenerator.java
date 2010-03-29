/**
 * This class generates the position of a group of dots after a certain amount 
 * of time.
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class PositionGenerator extends Thread{
    private int numSeconds;
    private Vector dots;
    private Electro2D electro2D;
    private static final Color colors[] = {
        /*Color.BLUE, Color.RED, Color.GREEN,*/ Color.YELLOW,
	new Color( 254, 143, 74 ), new Color( 160, 11, 206 ), 
	new Color( 72, 100, 100 ), new Color( 0, 95, 95 ), Color.CYAN,
        new Color( 158, 49, 49 ), new Color( 0, 135, 16 ),
	new Color( 255, 96, 0 ) };  
    private static double maxPi = 10;
    private static double minPi = 3;
    private static Color rnaColor = Color.BLUE;
    private static Color dnaColor = Color.RED;
    private static Color enzymeColor = Color.GREEN;
    private static Color hypotheticalColor = Color.PINK;
    private static Color transportColor = new Color( 117, 92, 50 );
    private static Color receptorColor =/* new Color( 0, 54, 42 )*/Color.BLACK;
    private static Color transductionColor = new Color( 255, 216, 202 );
    private Vector molecularWeights;
    private Vector piValues;
    private Vector functions;
    private Vector titles;
    private static final double defaultVoltage = 50;
    private static final double defaultPercent = 15;
    private static final int numLoopsPerSecond = 11;
    private int numSecs;

    public PositionGenerator( int seconds, Electro2D e ){
	numSeconds = seconds;
	molecularWeights = e.getMolecularWeights();
	piValues = e.getPiValues();
	functions = e.getFunctions();
	titles = e.getSequenceTitles();
	dots = new Vector();
	electro2D = e;

	//this.generateDots();
	
    }
    
    public void /*generateDots()*/ run(){
	GelCanvasSwingVersion gel = electro2D.getGel();
	double width = gel.getWidth();
	double pI = 0;
	double MW = 0;
	double xLoc = 0;
	double yLoc = 48;
	Color color = null;
	String title = "";
	String function = "";
	for( int numSecs = 0; numSecs< numSeconds; numSecs++ ){
	    dots = new Vector();
	for( int i = 0; i < piValues.size(); i++ ){
	    //first generate the xLocation
	    pI = Double.parseDouble( (String)piValues.elementAt(i) );
	    
	    if( pI <= minPi ){
		xLoc = 1;
	    }
	    else if( pI >= maxPi ){
		xLoc = width - 6;
	    }
	    else{
		xLoc = (int)((width - 4)*((pI-minPi)/(maxPi - minPi)));
	    }
	    
	    //then generate the location
	    
	    MW = Double.parseDouble( (String)molecularWeights.elementAt(i));
	    
	    for( int j = 0; j < numLoopsPerSecond * numSecs; j++ ){
		yLoc = ( 10 / defaultPercent ) * (defaultVoltage/25) * .25 
		  * (100000/MW) + yLoc;
		//yLoc = yLoc + yLoc;
		
	    }
	    System.out.println( numSecs + " " + yLoc);

	    //finally generate Color
	    
	    title = (String)titles.elementAt( i );
	    function = (String)functions.elementAt(i);
	    if( title.indexOf( "DNA" ) != -1 || title.indexOf( "dna" ) != -1 ){
		color = dnaColor;
	    }
	    else if( title.indexOf( "ribosomal" ) != -1 ){
		color = rnaColor;
	    }
	    else if( title.indexOf( "hypothetical" ) != -1 ){
		color = hypotheticalColor;
	    }
	    else if( function.length() > 6 && function.substring(0,6).equals(
						    "Enzyme" )){
		color = enzymeColor;
	    }
	    else if( function.indexOf( "transport" ) != -1 ){
		color = transportColor;
	    }
	    else if( function.indexOf( "receptor" ) != -1 || 
		     function.indexOf( "reception" ) != -1 ){
		color = receptorColor;
	    }
	    else if( function.indexOf( "transduction" ) != -1 ){
		color = transductionColor;
	    }
	    else{
		Random r = new Random();
		color = colors[ r.nextInt( 9 ) ];
	    }
	    
	    //add dot to vector and reset values for next dot

	    dots.add( new ProteinDotSwingVersion(color, gel, xLoc, yLoc) );
	    color = null;
	    xLoc = 0;
	    yLoc = 48;
	    pI = 0;
	    MW = 0;
	    function = "";
	    title = "";
	}
	((GelCanvasSwingVersion)gel).genGIFFile( dots, numSecs );
	//gel.repaint();
	//  if( numSecs%2 != 0 || numSecs%2 == 0 ){
//  	 ((GelCanvas)gel).genGIFFile( dots, numSecs );
//  	}
	try{
	    this.sleep( (long)1000 );
	}catch(Exception e ){}
	}
	electro2D.setVisible(false);
	System.exit(0);
    }
}

