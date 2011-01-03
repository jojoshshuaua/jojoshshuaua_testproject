import java.io.*;

public class PeptideCSVGenerator{

	private final String directoryString = "./../CSV Files/";
	private String[][] values;
	private int index;
	private String proteinName = "";

    public PeptideCSVGenerator( String[][] vals, int ind, String name ){
		values = vals;
		index = ind;
		proteinName = name;
	}

    public void writeToCSV(){
	String truncatedName = "";
	if( (proteinName.replace( " ", "" )).length() < 15 ){
		truncatedName = (proteinName.replace( " ", "" ));
	}
	else{
		truncatedName = (proteinName.replace( " ", "" )).substring( 0, 15 );	
	}
	
	String filename = "Enzyme_" + index + "_" + truncatedName;
	PrintWriter out = null;
	try{
		File fl = new File( directoryString );
		if( !fl.exists() ){
			fl.mkdir();
		}
	    out = new PrintWriter( new BufferedWriter( new FileWriter( 
							   directoryString + filename + ".csv" )));
	}catch( IOException e ){
	    System.err.println( "Error writing to CSV file" );
	}
	
	
	out.println( "Peptide" + "," + "Molecular Weight" );

	for( int i = 0; i < values.length; i++ ){
		out.println( "\"" + values[i][0] + "\",\"" + values[i][1] + "\"" );
	}
	 
	try{
	    out.close();
	}catch( Exception e ){
	    System.err.println( "Error closing stream" );
	}
    }
}