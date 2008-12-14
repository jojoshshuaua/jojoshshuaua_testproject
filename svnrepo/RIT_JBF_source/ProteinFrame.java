/**
 * ProteinFrame.java
 *
 * This class encapsulates all the functionality required to pop up a frame
 * and display information about a particular protein.
 *
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 */

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;

public class ProteinFrame extends Frame {

    private Electro2D electro2D;          //reference to calling applet
    private String proteinTitle;          //name of the protein
    private String ptTruncated;           //name truncated
    private Panel proteinPanel;           //panel to add components to
    private Rectangle dimensions;         //dimension holders
    private Label titleLabel;             //holds protein name
    private Label mwLabel;                //protein MW
    private Label piLabel;                //protein pI
    private static int xLoc = 0;          //the x Location of the frame
    private static int yLoc = 0;          // the y Location of the frame
    private Font theFont;                 //font used in this panel
    private SearchProteinButton search;   // button clicked to search for info
    private SwissProtSearchButton swsSearch;//button clicked to search for info
    private BlastSearchButton blstSearch; //button clicked to search Blast site
    private String searchID = null;       // the id for the GenBank search
    private String swsSearchID = null;    // id for the SwissProt search
    private int fileNum;                  // while file the proteins came from
    private String sequenceString = null; // the sequence of amino acids for
                                          // the protein
    private String proteinFunction = "";  // the function of the protein
    private ECSearchButton ecSearch;      // button clicked to search for EC
                                          // number
    private PeptideGenButton pgButton = null;
    private Label function;               // protein function
    private Label functionLabel;
    private ArrayList functionList;

    /**
     * Constructor - creates the ProteinFrame object as well as initializes
     * all of the data members
     *
     * @param e a reference to the Electro2D class
     * @param pt the title of the protein whose information is displayed
     */
    public ProteinFrame(Electro2D e, String pt, int filenum) {

	//set the font that the information is to be displayed in
	theFont = new Font( "Arial", Font.PLAIN, 12 );//("Arial",12,Font.PLAIN);
	//give the frame a reference to Electro2D
	electro2D = e;
	//set the title of the protein
	proteinTitle = pt;
	ptTruncated = proteinTitle;
	fileNum = filenum;
	setTitle("Protein Information");
	
	proteinPanel = new Panel();                  //init panel
	proteinPanel.setLayout(null);                //abs. positioning
	proteinPanel.setBackground(Color.BLACK);
	this.addWindowListener(new WindowAdapter() { //allow user to close win.
		public void windowClosing( WindowEvent e ) {
		    dispose();
		}
	    });
	
	//shorten the length of the name of the protein in order to fit it
	// into the frame
	if(proteinTitle.length() > 50) {
	    ptTruncated = proteinTitle.substring(0,50) + "...";
	}

	//get protein function 
	proteinFunction = electro2D.getFunctionbyTitle( proteinTitle );
	proteinFunction = proteinFunction.trim();
	
	
	// create a new BlastSearchButton based on the sequence of the protein
	// whose name was provided
	sequenceString = electro2D.getSequencebyTitle( proteinTitle );
	blstSearch = new BlastSearchButton( electro2D, sequenceString );
	


	String fileName = electro2D.getLastFileLoaded();
	String extention = fileName.substring( fileName.indexOf( "." ) );

	//depending on the database file used, extract the name of the protein
	// and create a button to allow a GenBank search for that protein
	int index = proteinTitle.indexOf( "|" );
	if( index != -1 ){
	    //if the protein is from a fasta file, the title is an abbreviated
	    //form of the proteinTitle string
	    searchID = proteinTitle.substring( index + 1 );
	    index = searchID.indexOf( "|" );
	    while( index != -1  && index != searchID.lastIndexOf("|") ){
		    searchID = searchID.substring( index + 1,
					   searchID.lastIndexOf( "|" ) );
			index = searchID.indexOf( "|" );
		}
		if( index != -1 ){
			searchID = searchID.substring( index + 1 );
		}
	    search = new SearchProteinButton( electro2D, searchID );
	}
	else if( index == -1 ){
	    //if the protein is not from a fasta file...

	    // String fileName = electro2D.getLastFileLoaded();
	    index = proteinTitle.indexOf( "\u003B" );
	    if( index != -1 ){
		//...but from a protein databank file, the id is the protein
		// title with the semicolon truncated
		searchID = proteinTitle.substring( 0, index );
		search = new SearchProteinButton( electro2D, searchID );
	    }
	    else {
		//...or from a protein databank file, the id is the protein 
		// title.
		searchID = proteinTitle;
		search = new SearchProteinButton( electro2D, searchID );
	    }
	}

	//using the file type to determine the string to assign it, 
	// create a button to allow a SwissProt/TrEMBL search for the protein
	
	if( proteinTitle.indexOf( "|" ) < 0 ){
	    swsSearchID = proteinTitle;
	    swsSearch = new SwissProtSearchButton( electro2D, swsSearchID );
	}
	else {
	    swsSearchID = proteinTitle.substring( 4,
				    proteinTitle.lastIndexOf( "|" ));
	    swsSearchID = swsSearchID.substring(
				    swsSearchID.lastIndexOf( "|" ) + 1 );
	    swsSearch = new SwissProtSearchButton( electro2D, swsSearchID );
	}

	
	//create labels to display the protein information
	titleLabel = new Label(ptTruncated,Label.CENTER);
	titleLabel.setForeground( Color.WHITE );

	mwLabel = new Label("Molecular Weight (MW): " + 
			    electro2D.getMWbyTitle(proteinTitle),Label.CENTER);
	mwLabel.setFont(theFont);
	mwLabel.setForeground( Color.WHITE );
	piLabel = new Label("pI Value (pI): " + 
			    electro2D.getPIbyTitle(proteinTitle),Label.CENTER);
	piLabel.setForeground( Color.WHITE );

	piLabel.setFont(theFont);

	functionLabel = new Label( "Function: " );
	functionLabel.setForeground( Color.WHITE );
	
	if( proteinFunction.substring( 0, 6 ).equals( "Enzyme" ) ){
	    String tempF = proteinFunction;
	    tempF = tempF.substring( 7,
				   tempF.lastIndexOf( "\u003B" )+1 );
	    ecSearch = new ECSearchButton( electro2D, tempF );
	}
	
	pgButton = new PeptideGenButton( electro2D, sequenceString, proteinTitle );
	
	if( proteinFunction.length() < 45 ){
	    function = new Label( proteinFunction );
	    function.setForeground( Color.WHITE );
	    function.setFont( theFont );
	    this.smallFunctionSetup();
	}
	else{
	    functionList = new ArrayList();
	    String tempF = proteinFunction;
	    for( int i = 0; tempF.length() > 0; i++ ){
		if( tempF.length() < 45 ){
		    Label l = new Label( tempF );
		    l.setForeground( Color.WHITE );
		    l.setFont( theFont );
		    functionList.add( new Label( tempF ) );
		    tempF = "";
		}
		else{
		    Label l = new Label( tempF.substring( 0, 45 ) + "-" );
		    l.setFont( theFont );
		    l.setForeground( Color.WHITE );
		    functionList.add( l );
		    tempF = tempF.substring( 45 );
		   
		}
	    }
	    this.largeFunctionSetup();
	}

    }
    
    public void largeFunctionSetup(){
	
	titleLabel.setBounds(5,30,390,20);
	mwLabel.setBounds(5,60,390,20);
	piLabel.setBounds(5,90,390,20);
	functionLabel.setBounds( 42, 120, 65, 20 );
	int count = 23;
	int startLoc = 120;
	Label currentLabel;
	for( int i = 0; i < functionList.size(); i++ ){
	    currentLabel = ((Label)functionList.get( i ));
	    currentLabel.setBounds( 112, startLoc, 320, 20 );
	    startLoc = startLoc + count;
	    proteinPanel.add( currentLabel );
	}
	count = 25;
	startLoc = startLoc + 7;
	if( ecSearch == null ){
	    if( search != null ){
		search.setBounds( 130, startLoc/*185*/, 135, 20 );
	    }
	    if( swsSearch != null ){
		startLoc = startLoc + count;
		swsSearch.setBounds( 100, startLoc, 195, 20 );
	    }
	    startLoc = startLoc + count;
	    blstSearch.setBounds( 140, startLoc/*235*/, 115, 20 );
	    startLoc = startLoc + count;
	    pgButton.setBounds( 120, startLoc, 152, 20 );
	}
	else if( ecSearch != null ){
	    if( search != null ){
		search.setBounds( 60, startLoc/*185*/, 135, 20 );
		blstSearch.setBounds( 220, startLoc/*185*/, 115, 20 );
		startLoc = startLoc + count;
	    }
	    else{
		blstSearch.setBounds( 60, startLoc/*185*/, 115, 20 );
		startLoc = startLoc + count;
	    }
	    if( swsSearch != null ){
		swsSearch.setBounds( 100, startLoc/*210*/, 195, 20 );
		startLoc = startLoc + count;
	    }
	    ecSearch.setBounds( 105, startLoc/*235*/, 180, 20 );
	    startLoc = startLoc + count;
	    pgButton.setBounds( 120, startLoc, 152, 20 );
	    startLoc = startLoc + count;
	}
	
	this.setBounds(xLoc,yLoc,400,startLoc + 100);       //set frame position

	// Changing the location of the next frame allows for layering
	// in the case of multiple frames
	
	xLoc = xLoc + 15;       //set x Location of next frame            
	yLoc = yLoc + 15;       //set y location of the next frame
	
	//once the locations reach a certain point, start over to prevent 
	// windows going off the screen

	if( xLoc > 300 ){
	    xLoc = 0;
	    yLoc = 0;
	}

	dimensions = this.getBounds();       //store frame size

	proteinPanel.setBounds(0,0,dimensions.width,dimensions.height);
	
	this.add(proteinPanel);              //add components
	proteinPanel.add(titleLabel);
	proteinPanel.add(mwLabel);
	proteinPanel.add(piLabel);
	proteinPanel.add( functionLabel );
	proteinPanel.add( pgButton );
	if( search != null ){
	    proteinPanel.add( search );
	}
	if( search != null ){
	    proteinPanel.add( swsSearch );
	}
	proteinPanel.add( blstSearch );
	if( ecSearch != null ){
	    proteinPanel.add( ecSearch );
	}
	
    }

    public void smallFunctionSetup(){
    
	
	this.setBounds(xLoc,yLoc,400,400);         //set frame position

	// Changing the location of the next frame allows for layering
	// in the case of multiple frames
	
	xLoc = xLoc + 15;       //set x Location of next frame            
	yLoc = yLoc + 15;       //set y location of the next frame
	
	//once the locations reach a certain point, start over to prevent 
	// windows going off the screen

	if( xLoc > 300 ){
	    xLoc = 0;
	    yLoc = 0;
	}

	dimensions = this.getBounds();       //store frame size

	proteinPanel.setBounds(0,0,dimensions.width,dimensions.height);
	titleLabel.setBounds(5,30,390,20);
	titleLabel.setForeground( Color.WHITE );
	mwLabel.setBounds(5,60,390,20);
	mwLabel.setForeground( Color.WHITE );
	piLabel.setBounds(5,90,390,20);
	piLabel.setForeground( Color.WHITE );
	functionLabel.setBounds( 42, 120, 65, 20 );
	functionLabel.setForeground( Color.WHITE );
	function.setBounds( 112, 120, 350, 20 );
	function.setForeground( Color.WHITE );
	System.out.println( proteinFunction );
	if( ecSearch == null ){
	    if( search != null ){
		search.setBounds( 130, 165, 135, 20 );
	    }
	    if( swsSearch != null ){
		swsSearch.setBounds( 100, 195, 195, 20 );
	    }
	    blstSearch.setBounds( 140, 220, 115, 20 );
	    pgButton.setBounds( 120, 245, 152, 20 );
	}
	else if( ecSearch != null ){
	    if( search != null ){
		search.setBounds( 60, 165, 135, 20 );
	    }
	    if( swsSearch != null ){
		swsSearch.setBounds( 100, 195, 195, 20 );
	    }
	    blstSearch.setBounds( 220, 165, 115, 20 );
	    ecSearch.setBounds( 105, 225, 180, 20 );
		pgButton.setBounds( 120, 250, 152, 20 );
	}
	this.add(proteinPanel);              //add components
	proteinPanel.add(titleLabel);
	proteinPanel.add(mwLabel);
	proteinPanel.add(piLabel);
	proteinPanel.add( function );
	proteinPanel.add( functionLabel );
	proteinPanel.add( pgButton );
	if( search != null ){
	    proteinPanel.add( search );
	}
	if( search != null ){
	    proteinPanel.add( swsSearch );
	}
	proteinPanel.add( blstSearch );
	if( ecSearch != null ){
	    proteinPanel.add( ecSearch );
	}
	
	
    }
    
    public void updateLabel() {
	titleLabel.repaint();
    }
}





