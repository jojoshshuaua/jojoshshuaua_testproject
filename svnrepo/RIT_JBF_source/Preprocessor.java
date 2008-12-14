import java.io.*;
import java.util.Vector;

/**
 * This class generates the preprocessed proteome files created after a 
 * genBank, fasta, or protein database file has been run.  In doing this
 * the time to load a proteome (if already loaded once) will be cut down.
 * (Hopefully...)
 *
 * @author Jill Zapoticznyj
 */

public class Preprocessor{
    
    private static final String EXTENTION = ".e2d";
    private static final int HEADER_LENGTH = 12;
    private final int lineLength = 50;

    private final String FILE_HEADER = "FILE:       ";
    private final String NUMENZYME_HEADER = "NUMENZYMES: ";
    private final String PROTTITLE_HEADER = "NAME:       ";
    private final String FUNCTION_HEADER = "FUNCTION:   ";
    private final String SEQUENCE_HEADER = "SEQUENCE:   ";
    private final String MOLWT_HEADER = "MOLWEIGHT:  ";
    private final String PIVAL_HEADER = "PIVAL:      ";
    private final String LINE_SEPARATOR = "-----";
    
    private Vector sequence;
    private Vector sequenceTitle;
    private Vector piVals;
    private Vector molwts;
    private Vector functions;
    private String fileName;
    private int numEnzymes;
    private Electro2D electro2D;
    
    public Preprocessor( Electro2D e ){
	sequence = e.getSequences();
	sequenceTitle = e.getSequenceTitles();
	molwts = e.getMolecularWeights();
	piVals = e.getPiValues();
	functions = e.getFunctions();
	//numEnzymes = 
	fileName = e.getLastFileLoaded();
    }
    
    public void writeToFile(){
	int length = 0;
	String fcn = "";
	String seq = "";
	String tmp = fileName.substring( 0, fileName.indexOf( "." )) + "_" + 
					 fileName.substring( 
					       fileName.indexOf( "." )+1 );
	File theFile = new File( "./../data/" + tmp + EXTENTION );
	PrintWriter out = null;
	if( !theFile.exists() ){
	try{
	    out = new PrintWriter( new BufferedWriter(
					       new FileWriter( theFile)),
					   true );
	}catch( IOException e ){
	    System.err.println( "Error writing to file" );
	}
	out.println( FILE_HEADER + fileName );
	// out.println(NUMENZYME_HEADER + numEnzymes );
	out.println( LINE_SEPARATOR );
	
	for( int i = 0; i < sequenceTitle.size(); i++ ){
	    out.println( PROTTITLE_HEADER +
			 (String)sequenceTitle.elementAt( i ) );
	    fcn = (String)functions.elementAt( i );
	    length = fcn.length();
	    while( length >= 0 ){
		if( length <= lineLength ){
		    out.println( FUNCTION_HEADER + fcn );
		    length = -1;
		}
		else{
		    out.println( FUNCTION_HEADER + fcn.substring( 0,
							      lineLength ) );
		    fcn = fcn.substring( lineLength );
		    length = length - lineLength;
		}
	    }
	    
	    seq = (String)sequence.elementAt( i );
	    length = seq.length();
	    
	    while( length >= 0 ){
		if( length <= lineLength ){
		    out.println( SEQUENCE_HEADER + seq );
		    length = -1;
		}
		else{
		    out.println( SEQUENCE_HEADER + seq.substring( 0,
							       lineLength ));
		    seq = seq.substring( lineLength );
		    length = length - lineLength;
		}
	    }
	    
	    out.println( MOLWT_HEADER + (String)molwts.elementAt( i ));
	    out.println( PIVAL_HEADER + (String)piVals.elementAt( i ));
	    out.println( LINE_SEPARATOR );
	}
	out.close();
	}
    }

    public static int getHeaderLength(){
	return HEADER_LENGTH;
    }

    public static void readFromFile( BufferedReader in, Electro2D electro2D, 
				     int fileNum ){
	int fileNameLoc = 0;
	int endofHeader = 1;
	Vector sequences = new Vector();
	Vector sequenceTitles = new Vector();
	Vector molwts = new Vector();
	Vector functions = new Vector();
	Vector piVals = new Vector();
	Vector fileData = new Vector();
	String seq = "";
	String molwt = "";
	String pI = "";
	String title = "";
	String filename = "";
	String function = "";
	String line = "";
	String line1 = "";
	int hlength = getHeaderLength();
	double doubleVal;
	double maxMW = -1;
	double minMW = -1;
	double maxPi = -1;
	double minPi = -1;
	
	try{
	    while( (line1 = in.readLine()) != null ){
		fileData.add( line1 );
	    }
	}catch(Exception e ){
	    System.err.println( "Error reading from file.  Be sure you " +
				"entered the file name correctly." );
	}
	
	if( ((String)fileData.elementAt( fileNameLoc ) ).indexOf( "FILE:" )
	    == -1 ){
	    System.err.println( "Invalid file format.  Missing file name " +
				"line." );
	}
	else{
	    
	    electro2D.setLastFileLoaded( ((String)fileData.elementAt( fileNameLoc )).substring( hlength ) );
	    // System.out.println( ((String)fileData.elementAt( fileNameLoc )).substring( hlength));
	    for( int i = endofHeader + 1; i < fileData.size() - 1; i++ ){
		line = (String)fileData.elementAt( i );
		while( line.indexOf( "NAME:" ) != -1 ){
		    title = title + line.substring( hlength );
		    if( i + 1 != fileData.size() ){
			i = i + 1;
			line = (String)fileData.elementAt( i );
		    }
		    else{ 
			 System.err.println( "Error reading from file." );
			 return;
		    }
		}
		while( line.indexOf( "FUNCTION:" ) != -1 ){
		    function = function + line.substring( hlength );
		    if( i + 1 < fileData.size() ){
			i = i + 1;
			line = (String)fileData.elementAt( i );
		    }
		    else{
			System.err.println( "Error reading from file." );
			return;
		    }
		}
		while( line.indexOf( "SEQUENCE:" ) != -1 ){
		    seq = seq + line.substring( hlength );
		    if( i + 1 < fileData.size() ){
			i = i + 1;
			line = (String)fileData.elementAt( i );
		    }
		    else{
			System.err.println( "Error reading from file." );
			return;
		    }
		}
		if( line.indexOf( "MOLWEIGHT:" ) != -1 ){
		    molwt = line.substring( hlength );
		    doubleVal = Double.parseDouble( molwt );
		    if( minMW == -1 || doubleVal <= minMW ){
			minMW = doubleVal;
		    }
		    if( maxMW == -1 || doubleVal >= maxMW ){
			maxMW = doubleVal;
		    }
		    if( i + 1 != fileData.size()){
			i = i + 1;
			line = (String)fileData.elementAt( i );
		    }
		    else{
			System.err.println( "Error reading from file." );
			return;
		    }
		}
		if( line.indexOf( "PIVAL:" ) != -1 ){
		    pI = line.substring( hlength );
		    doubleVal = Double.parseDouble( pI );
		    if( minPi == -1 || doubleVal <= minPi ){
			minPi = doubleVal;
		    }
		    if( maxPi == -1 || doubleVal >= maxPi ){
			maxPi = doubleVal;
		    }
		    i = i + 1;
		}
		
		sequenceTitles.add( title );
		sequences.add( seq );
		molwts.add( molwt );
		piVals.add( pI );
		functions.add( function );
		title = seq = molwt = pI = function = "";
	    }
	    if( fileNum == 1 ){
		electro2D.setSequences( sequences );
		electro2D.setFunctionValues( functions );
		electro2D.setSequenceTitles( sequenceTitles );
		electro2D.setMolecularWeights( molwts );
		electro2D.setPiValues( piVals );
		electro2D.setMaxAndMinVals( maxMW, minMW, maxPi, minPi );
	    }
	    else if( fileNum == 2 ){
		electro2D.setSequences2( sequences );
		electro2D.setFunctionValues2( functions );
		electro2D.setSequenceTitles2( sequenceTitles );
		electro2D.setMolecularWeights2( molwts );
		electro2D.setPiValues2( piVals );
		electro2D.setMaxAndMinVals( maxMW, minMW, maxPi, minPi );
	    }
	    // electro2D.setLastFileLoaded( 
	    try{
		in.close();
	    }catch(Exception e){}
	}
    }
}









