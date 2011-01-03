import java.io.*;
import java.util.*;

public class PeptideGen{

    private static BufferedReader inReader;
    public static void main( String[] args ) throws Exception{
	try{
		instructions();
		inReader = new BufferedReader(new InputStreamReader(System.in));
		String input = inReader.readLine();
		int enzyme = Integer.parseInt(input);
	
		System.out.println( "Please enter an amino acid sequence" );
		String sequence = inReader.readLine();
		
		switch(enzyme){
		case 1:{
		    System.out.println("\nEnzyme 1");
	    	outputResult( enzyme1(sequence) );
	    	break;
		}
		case 2:{
	    	System.out.println("\nEnzyme 2");
	    	outputResult( enzyme2(sequence) );
	    	break;
		}
		case 3:{
	    	System.out.println("\nEnzyme 3");
	    	outputResult( enzyme3(sequence) );
	    	break;
		}
		case 4:{
	    	System.out.println("\nEnzyme 4");
	    	outputResult( enzyme4(sequence) );
	    	break;
		}
		case 5:{
	    	System.out.println("\nEnzyme 5");
	    	outputResult( enzyme5(sequence)); 
	    	break;
		}
		case 6:{
		    System.out.println("\nEnzyme 6");
		    outputResult( enzyme6(sequence)); 
		    break;
		}
		case 7:{
		    System.out.println("\nEnzyme 7");
		    outputResult( enzyme7(sequence)); 
		    break;
		}
		case 8:{
		    System.out.println("\nEnzyme 8");
		    outputResult( enzyme8(sequence));
		    break;
		}
		case 9:{
		    System.out.println("\nEnzyme 9");
		    outputResult( enzyme9(sequence));
		    break;
		}
		case 10:{
		    System.out.println("\nEnzyme 10");
		    outputResult( enzyme10(sequence) );
		    break;
		}
		case 11:{
		    System.out.println("\nEnzyme 11");
		    outputResult( enzyme11(sequence) );
		    break;
		}
		case 12:{
		    System.out.println("\nEnzyme 12");
		    outputResult( enzyme12(sequence) );
		    break;
		}
		case 13:{
		    System.out.println("\nEnzyme 13");
		    outputResult( enzyme13(sequence) );
		    break;
		}
		case 14:{
		    System.out.println("\nEnzyme 14");
		    outputResult( enzyme14(sequence) );
		    break;
		}
		case 15:{
		    System.out.println("\nEnzyme 15");
		    outputResult( enzyme15(sequence));
		    break;
		}
		case 16:{
		    System.out.println("\nEnzyme 16");
		    outputResult( enzyme16(sequence) );
		    break;
		}
		default:{
			System.out.println("Valid numbers are 1 - 16.");
		    }
		}
		}catch(Exception ex){
	    	System.err.println(ex.getMessage());
		}
    }
    
    public ArrayList generateArrayList( int enzyme, String sequence ){
    	ArrayList retVal = null;
    	
    	switch(enzyme){
		case 1:{
		    retVal = enzyme1(sequence);
	    	break;
		}
		case 2:{
	    	retVal = enzyme2(sequence);
	    	break;
		}
		case 3:{
	    	retVal = enzyme3(sequence);
	    	break;
		}
		case 4:{
	    	retVal = enzyme4(sequence);
	    	break;
		}
		case 5:{
	    	retVal = enzyme5(sequence); 
	    	break;
		}
		case 6:{
		    retVal = enzyme6(sequence); 
		    break;
		}
		case 7:{
		    retVal = enzyme7(sequence); 
		    break;
		}
		case 8:{
		    retVal = enzyme8(sequence);
		    break;
		}
		case 9:{
		    retVal = enzyme9(sequence);
		    break;
		}
		case 10:{
		    retVal = enzyme10(sequence);
		    break;
		}
		case 11:{
		    retVal = enzyme11(sequence);
		    break;
		}
		case 12:{
		    retVal = enzyme12(sequence);
		    break;
		}
		case 13:{
		    retVal = enzyme13(sequence);
		    break;
		}
		case 14:{
		    retVal = enzyme14(sequence);
		    break;
		}
		case 15:{
		    retVal = enzyme15(sequence);
		    break;
		}
		case 16:{
		    retVal = enzyme16(sequence);
		    break;
		}
		default:{
			System.out.println("Valid numbers are 1 - 16.");
		    }
		}
    	
    	return retVal;
    }

    public static void instructions() {
	System.out.println( "Available enzymes:\n" + 
			    "1. Trypsin \n" + 
			    "2. Trypsin (C-term to K/R, even before P) \n" + 
			    "3. Trypsin (higher specificity) \n" + 
			    "4. Lys C \n" + 
			    "5. CNBr \n" + 
			    "6. Arg C \n" +
			    "7. Asp N \n" + 
			    "8. Asp N + N-terminal Glu \n" +
			    "9. Glu C (bicarbonate) \n"+
			    "10. Glu C (phosphate) \n" +
			    "11. Chymotrypsin (C-term to F/Y/W/M/L, not before P, not after Y if P is N-term to Y) \n" +
			    "12. Chymotrypsin (C-term to F/Y/W, not before P, not after Y if P is N-term to Y) \n" + 
			    "13. Trypsin/Chymotrypsin (C-term to K/R/F/Y/W, not before P, not after Y if P is N-term to Y \n" +
			    "14. Pepsin (pH 1.3) \n" +
			    "15. Pepsin (pH > 2) \n" + 
			    "16. Proteinase K \n");
	System.out.println("Please enter the number of one of the available enzymes.");
    }

    public static void outputResult( ArrayList peptides ){
	System.out.println("----------------------------------------------------------------");
	ArrayList mol = new ArrayList();
	System.out.println(peptides.size());
	for( int i = 0; i < peptides.size(); i++ ){
	    System.out.println( (String)peptides.get(i) + "    ");
	    System.out.println("here");
	    mol.add( new Double(getMW((String)peptides.get(i))));
	    System.out.println("here2");
	    System.out.println( getMW((String)peptides.get(i)) );
	    System.out.println("here3");
	}
	System.out.println("here4");
    }
    
    /**
     * enzyme11 handles Chymotrypsin (C-term to F/Y/W/M/L, not before P, not after Y if P is C-term to Y)
     *
     * @param pSequence - amino acid sequence to be parsed
     */
    public static ArrayList enzyme11( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
    		currChar = pSequence.charAt(i);
    		seq = seq + currChar;
    		switch( currChar ){
    			case 'Y': {
    				if( ( i == 0 || pSequence.charAt( i-1 ) != 'P' ) &&
    					( i+1 == pSequence.length() || pSequence.charAt( i+1 ) != 'P' ) ){
    					peptides.add( seq );
    					seq = "";
    				}
    			}
    			case 'F': case 'L': case 'M': case 'W': {
    				if( pSequence.length() == i+1 || pSequence.charAt(i+1) != 'P' ){
    					peptides.add( seq );
    					seq = "";
    				}
    			}
    		}
    	}
    	if( !seq.equals("") ){
	    	peptides.add( seq );
    	}
    	return peptides;
    }
   
    
    /**
     * enzyme10 handles Glu C (phosphate)
     *
     * @param pSequence - amino acid sequence to be parsed
     */
    public static ArrayList enzyme10( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
    		currChar = pSequence.charAt( i );
    		seq = seq + currChar;
    		switch( currChar ){
    			case 'E': case 'D': {
    				if( pSequence.length() == i + 1 || 
    					(pSequence.charAt( i+1 ) != 'P' && 
    						pSequence.charAt( i+1 ) != 'E' ) ){
    					peptides.add( seq );
    					seq = "";
    				}
    			}
    		}
    	}
    	if( !seq.equals("") ){
    		peptides.add( seq );
    	}
    	return peptides;
    }
    
    /**
     * enzyme1 handles Trypsin
     *
     * @param pSequence - amino acid sequence to be parsed
     */
    public static ArrayList enzyme1( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
    	    currChar = pSequence.charAt( i );
    	    seq = seq + currChar;
    	    switch( currChar ){
	        	case 'K': case 'R':{
		    		if( pSequence.length() == i+1 || 
						pSequence.charAt(i+1)!='P' ){
						peptides.add( seq );
						seq = "";
						break;
		    		}
	        	}
    	    }//switch
    	}//for
		if( !seq.equals("") ){
			peptides.add( seq );
		}
		return peptides;
    }//enzyme1
    
    
    /**
     * enzyme5 handles CNBr
     *
     * @param pSequence - the amino acid sequence to be parsed
     */
    public static ArrayList enzyme5( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
    	    currChar = pSequence.charAt( i );
    	    seq = seq + currChar;
    	    switch( currChar ){
    	    	case 'M': {
    	    	    peptides.add( seq );
    	    	    seq = "";
    	    	    break;
    	    	}//case
    	    }//switch
    	}//for
    	if( !seq.equals("") ){
	    	peptides.add( seq );
    	}
    	return peptides;
    }//enzyme5
    
    /**
     * enzyme7 handles Asp N
     *
     * @param pSequence - the amino acid sequence to be parsed
     */
    public static ArrayList enzyme7( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
    	    currChar = pSequence.charAt( i );
    	    
    	    switch( currChar ){
    	    	case 'D': {
    	    	    peptides.add( seq );
    	    	    seq = Character.toString(currChar);
    	    	    break;
    	    	}
    	    	default:
    	    	    seq = seq + currChar;
    	    }//switch
    	}//for
    	if( !seq.equals("") ){
	    	peptides.add( seq );
    	}
    	return peptides;
    }//enzyme7
    
    /**
     * enzyme4 handles Lys C
     *
     * @param pSequence - the amino acid sequence to be parsed
     */
    public static ArrayList enzyme4( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
    	    currChar = pSequence.charAt( i );
    	    seq = seq + currChar;
    	    switch( currChar ){
    	    	case 'K': {
    	    	    peptides.add( seq );
    	    	    seq = "";
    	    	    break;
    	    	}//case...
    	    }//switch
    	}//for
    	if(!seq.equals("")){
    		peptides.add( seq );
    	}
    	return peptides;
    }//enzyme4
    
    /**
     * enzyme16 handles Proteinase K
     *
     * @param pSequence - the amino acid sequence to be parsed
     */
    public static ArrayList enzyme16( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
  	for( int i = 0; i < pSequence.length(); i++ ){
    	    currChar = pSequence.charAt( i );
    	    seq = seq + currChar;
    	    switch( currChar ){
    	    	case 'A': case 'C': case 'G': case 'M': case 'F': case 'S': case 'Y': case 'W': {
    	    	    peptides.add(seq);
    	    	    seq = "";
    	    	    break;
    	    	}//case...
    	    }//switch
    	}//for
    	if( !seq.equals("") ){
    		peptides.add( seq );
    	}
    	return peptides;
    }//enzyme16

    /**
     * enzyme15 handles Pepsin (pH > 2)
     *
     * @param pSequence - the amino acid sequence to be parsed
     */
    public static ArrayList enzyme15( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
    	    currChar = pSequence.charAt(i);
    	    seq = seq + currChar;
    	    switch( currChar ){
    	    	case 'F': case 'L': case 'W': case 'Y': case 'A': case 'E': case 'Q': {
    	    	    peptides.add( seq );
    	    	    seq = "";
    	    	    break;
    	    	}//case...
    	    }//switch
    	}//for
    	if(!seq.equals("")){
    		peptides.add( seq );
    	}
    	return peptides;
    }//enzyme15


    /**
     * enzyme14 handles Pepsin (pH 1.3)
     *
     * @param pSequence - the amino acid sequence to be parsed
     */
    public static ArrayList enzyme14( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
    	    currChar = pSequence.charAt( i );
    	    seq = seq + currChar;
    	    switch( currChar ){
    	    	case 'F': case 'L': {
    	    	    peptides.add( seq );
    	    	    seq = "";
    	    	    break;
    	    	}//case F: case L
    	    }//switch
    	}//for
	if( !seq.equals("")){
	peptides.add( seq );
	}
	return peptides;
    }
	
    /**
     * enzyme8 handles Asp N + N-terminal Glu
     *
     * @param pSequence - the Amino Acid sequence to be parsed
     */
     public static ArrayList enzyme8( String pSequence ){
     	ArrayList peptides = new ArrayList();
     	char currChar = ' ';
     	String seq = "";
     	
     	for( int i = 0; i < pSequence.length(); i++ ){
     	   currChar = pSequence.charAt( i );
     	   
     	   switch( currChar ){
     	       case 'E': case 'D': {
     	       	   peptides.add(seq);
     	       	   seq = Character.toString(currChar);
     	       	   break;
     	       }
     	       default:
     	       	   seq = seq + currChar;
     	   }
     	}
     	if( !seq.equals("")){
     	peptides.add( seq );
    	}
     	return peptides;

     }

    /**
     * enzyme9 handles Glu C (bicarbonate)
     *
     * @param pSequence - the amino acid sequence to be parsed
     */
     public static ArrayList enzyme9( String pSequence ){
     	ArrayList peptides = new ArrayList();
     	char currChar = ' ';
     	String seq = "";
     	
     	for( int i = 0; i < pSequence.length(); i++ ){
     	    currChar = pSequence.charAt(i);
	    seq = seq + currChar;
     	    switch( currChar ){
	    case 'E': {
		if( (i == pSequence.length() - 1 || (pSequence.charAt(i+1) != 'P' && pSequence.charAt(i+1)!='E'))){
		    peptides.add(seq);
		    seq = "";
		}
		
		break;
	    }//case 'E'
	    }//switch
	    
	}//for
	if( !seq.equals("") ){
	peptides.add(seq);
	}
	return peptides;
     }
    

    /**
     * enzyme12 handles Chymotrypsin (C-term to F/Y/W, not before P, not after Y if P is N-term to Y)
     *
     * @param pSequence - the amino acid sequence to be parsed
     */
    public static ArrayList enzyme12( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
	for( int i = 0; i < pSequence.length(); i++ ){
		currChar = pSequence.charAt(i);
		seq = seq + currChar;
		switch( currChar ){
			case 'Y':{
				if((i != pSequence.length() - 1 && pSequence.charAt(i+1) != 'P') || 
				(i != 0 && pSequence.charAt(i-1) != 'P')){
					peptides.add(seq);
					seq = "";
				}
				break;
			}
			case 'F': case 'W': {
				if((i != pSequence.length() - 1 && pSequence.charAt(i+1) != 'P')){
					peptides.add(seq);
					seq = "";
				}
				break;
			}
		}//switch
	}//for
	if(!seq.equals("")){
	peptides.add(seq);
	}
	return peptides;
	
    }

    /**
     * enzyme6 handles the Arg C handling of an amino acid sequence
     *
     * @param pSequence = amino acid chain to be parsed
     */
    public static ArrayList enzyme6( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
	    currChar = pSequence.charAt( i );
	    seq = seq + currChar;
	    switch( currChar ){
	    	case 'R':{
	    	    if(( i == pSequence.length() - 1 ) || (pSequence.charAt(i+1) != 'P') ){
	    	    	peptides.add(seq);
	    	    	seq = "";
	    	    }
	    	}
	    } // switch
	} // for
	if( !seq.equals("")){
	peptides.add(seq);
	}
	return peptides;
    	
    } // enzyme6

    /**
     * enzyme2 handles the Trypsin (c-term to K/R even before P) handling of an
     * amino acid sequence
     *
     * @param pSequence = amino acid chain to be parsed
     **/
    public static ArrayList enzyme2( String pSequence ){
	ArrayList peptides = new ArrayList();
	char currChar = ' ';
	String seq = "";
	
	for( int i = 0; i < pSequence.length(); i++ ){
	    currChar = pSequence.charAt(i);
	    seq = seq + currChar;
	    
	    switch( currChar ){
	    case 'K': case 'R': {
		peptides.add( seq );
		seq = "";
		break;
	    }
	    }//switch
	}//for
	if( !seq.equals("")){
	peptides.add( seq );
	}	
	return peptides;
    }
    
    /**
     * enzyme13 handles the Trypsin/Chymotrypsin enzyme handling of an 
     * amino acid sequence.
     *
     * @param pSequence = the sequence to be parsed
     **/
    public static ArrayList enzyme13( String pSequence ){ 
	ArrayList peptides = new ArrayList();
	char currChar = ' ';
	String seq = "";
	
	for( int i = 0; i < pSequence.length(); i++){
	    currChar = pSequence.charAt(i);
	    seq = seq + currChar;
	    
	    switch(currChar){
	    case 'Y':{
		if((i!=pSequence.length() - 1 && pSequence.charAt(i+1)!='P')
		   && (i!=0 && pSequence.charAt(i-1)!= 'P')){
		    peptides.add(seq);
		    seq = "";
		} //end if
		break;
	    } // case Y
	    case 'K': case 'R': case 'F': case 'W': {
		if( i!=pSequence.length() - 1 && pSequence.charAt(i+1)!='P'){
		    peptides.add(seq);
		    seq = "";
		}
		break;
	    } // case K...
	    } // switch
	} // for
	
	if(!seq.equals("")){
	    peptides.add(seq);
	}
	return peptides;
    }
    
    public static ArrayList enzyme3( String pSequence ){
    	ArrayList peptides = new ArrayList();
    	char currChar = ' ';
    	String seq = "";
    	
    	for( int i = 0; i < pSequence.length(); i++ ){
    		currChar = pSequence.charAt( i );
    		seq = seq + currChar;
    		
    		switch( currChar ){
    			case 'K':{
    				if(!( (i != pSequence.length() - 1 && (pSequence.charAt( i+1 ) == 'P')) || ((i!=0 && i != pSequence.length() - 1) && ( (pSequence.charAt(i-1) == 'C' && ( pSequence.charAt(i+1) == 'Y' || pSequence.charAt(i+1) == 'H' || pSequence.charAt(i+1) == 'D')) || (pSequence.charAt(i-1) == 'D' && pSequence.charAt(i+1) == 'D') || (pSequence.charAt(i-1) == 'K' && pSequence.charAt(i+1) == 'R'))))){
    					//do nothing
    					peptides.add( seq );
    					seq = "";
    				}
    				else{
    					
    				}
				break;
    			}
    			case 'R':{
    				if(!((i != pSequence.length() - 1 && pSequence.charAt( i + 1 ) == 'P') || ((i != 0 && i != pSequence.length() - 1) && ((pSequence.charAt(i-1) == 'R' && (pSequence.charAt(i+1) == 'R' || pSequence.charAt(i+1) == 'H' || pSequence.charAt(i+1) == 'F')) || (pSequence.charAt(i-1) == 'K' && pSequence.charAt(i+1) == 'R') || (pSequence.charAt(i-1) == 'D' && pSequence.charAt(i+1) == 'D') || (pSequence.charAt(i-1) == 'C' && pSequence.charAt(i+1) == 'K'))))){
    					//do nothing
    					peptides.add( seq );
    					seq = "";
    				}
    				else{
    					
    				}
				break;
    			}
    		}
    	}
    	if( !seq.equals("")){
    		peptides.add( seq );
    	}
    	return peptides;
    }

    /**
     * This method calculates the molecular weight from inputted sequence
     *
     * @param     pro     protein sequence
     */
    public static double getMW( String pro ) {

        // Length of protein sequence
        int Plength = pro.length();

        // Molecular weight of protein
        double weight = 0;//I( String pro ) {


        /* Determine the molecular weight for each AA until reach
	   end of sequence. Add the weight for each AA to the value
	   of total weight. Since a water molecule (MW 18) is lost with
	   each bond, the weight given for each AA is its molecular
	   weight minus that of water. The MW of one water molecule is
	   then added to the total molecular weight of the protein sequence.
	   If the AA character does not match one of the 20 accepted
	   abbreviations, then a weight of 0 is given for that AA. */

        for ( int f = 0; f < Plength; f++ ) {
            switch ( pro.charAt( f ) ) {
            case 'A': weight += 71.0938; break;
            case 'R': weight += 156.2022; break;
            case 'N': weight += 114.1188; break;
            case 'D': weight += 115.1036; break;
            case 'C': weight += 103.1538; break;
            case 'Q': weight += 128.1456; break;
            case 'E': weight += 129.1304; break;
            case 'G': weight += 57.067; break;
            case 'H': weight += 137.156; break;
            case 'I': weight += 113.1742; break;
            case 'L': weight += 113.1742; break;
            case 'K': weight += 128.1888; break;
            case 'M': weight += 131.2074; break;
            case 'F': weight += 147.1914; break;
            case 'P': weight += 97.1316; break;
            case 'S': weight += 87.0932; break;
            case 'T': weight += 101.12; break;
            case 'W': weight += 186.228; break;
            case 'Y': weight += 163.1908; break;
            case 'V': weight += 99.1474; break;
            default : weight += 0;
            }
        }

        // Add molecular weight of water to total weight
        weight += 18;

        // Return the molecular weight of the protein
        return weight;
    }

}





