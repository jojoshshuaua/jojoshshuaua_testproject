/**
 * CSVCreator ports the contents of a proteome file to a Comma Separated 
 * Value file to be viewed in a spreadsheet.  It stores the protein name, 
 * sequence, molecular weight, and pI value to the file.
 *
 * @author Jill Zapoticznyj
 */

import java.io.*;
import java.util.*;

public class CSVCreator{
    
    //private Vector sequences;
    //private Vector sequenceTitles;
    //private Vector molwts;
    //private Vector piVals;
    private Vector proteins;
    private Electro2D electro2D;
    private final String directoryString = "./../CSV Files/";

    public CSVCreator( Electro2D e ){
	electro2D = e;
	//sequences = electro2D.getSequences();
	//sequenceTitles = electro2D.getSequenceTitles();
	//molwts = electro2D.getMolecularWeights();
	//piVals = electro2D.getPiValues();
	
	proteins = electro2D.getGel().getDots();
    }

    public void writeToCSV(){
	
	String filename = electro2D.getLastFileLoaded();
	PrintWriter out = null;
	try{
		File fl = new File( directoryString );
		if( !fl.exists() ){
			fl.mkdir();
		}
	    out = new PrintWriter( new BufferedWriter( new FileWriter( 
							   directoryString + filename.substring(0,
						filename.indexOf(".")) + ".csv" )));
	}catch( IOException e ){
	    System.err.println( "Error writing to CSV file" );
	}
	
	E2DProtein p = null;
	ProteinDot d = null;
	
	out.println( "Title" + "," + "Sequence" + "," + "Molecular Weight" + 
		     "," + "pI Value" + "," + "X-Coordinate" + "," + 
		     "Y-Coordinate" );

	for( int i = 0; i < proteins.size(); i++ ){
	    d = (ProteinDot)proteins.elementAt( i );
	    p = (E2DProtein)d.getPro();
	    
	    out.println( "\"" + p.getID() + "\"" + "," + p.getSequence() + "," + 
			 p.getMW() + "," + p.getPI() + "," + 
			 d.returnX() + "," + d.returnY() );
	}
	 
	try{
	    out.close();
	}catch( Exception e ){
	    System.err.println( "Error closing stream" );
	}
    }
}

