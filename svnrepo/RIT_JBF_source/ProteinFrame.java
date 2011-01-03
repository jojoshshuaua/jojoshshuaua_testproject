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
import javax.swing.*;
import java.util.ArrayList;

public class ProteinFrame extends JFrame {

    private Electro2D electro2D;          //reference to calling applet
    private String proteinTitle;          //name of the protein
    private String ptTruncated;           //name truncated
    private JPanel proteinInfoPanel;           //panel to add components to
    private JPanel searchPanel;
    private JLabel titleLabel;             //holds protein name
    private JLabel mwLabel;                //protein MW
    private JLabel piLabel;                //protein pI
    private SearchProteinButton search;   // button clicked to search for info
    private SwissProtSearchButton swsSearch;//button clicked to search for info
    private BlastSearchButton blstSearch; //button clicked to search Blast site
    private String searchID = null;       // the id for the GenBank search
    private String swsSearchID = null;    // id for the SwissProt search
    private int fileNum;                  // while file the proteins came from
    private String sequenceString; // the sequence of amino acids for
                                          // the protein
    private String proteinFunction = "";  // the function of the protein
    private JLabel function;               // protein function
    private JLabel functionLabel;
    private ArrayList<JLabel> functionList;

    /**
     * Constructor - creates the ProteinFrame object as well as initializes
     * all of the data members
     *
     * @param e a reference to the Electro2D class
     * @param pt the title of the protein whose information is displayed
     */
    public ProteinFrame(Electro2D e, String pt, int filenum) {

	//give the frame a reference to Electro2D
	electro2D = e;
	//set the title of the protein
	proteinTitle = pt;
	ptTruncated = proteinTitle;
	fileNum = filenum;
	setTitle("Protein Information");
	
	proteinInfoPanel = new JPanel();                  //init panel
	searchPanel = new JPanel();
        proteinInfoPanel.setLayout(new BoxLayout(proteinInfoPanel, BoxLayout.Y_AXIS));
        searchPanel.setLayout(new FlowLayout());
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);

	//get protein function 
	proteinFunction = electro2D.getFunctionbyTitle( proteinTitle );
	proteinFunction = proteinFunction.trim();
	
	
	// create a new BlastSearchButton based on the sequence of the protein
	// whose name was provided
	sequenceString = electro2D.getSequencebyTitle( proteinTitle );
	blstSearch = new BlastSearchButton( electro2D, sequenceString );
	
	String fileName = electro2D.getLastFileLoaded();

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
	titleLabel = new JLabel(ptTruncated);
	mwLabel = new JLabel("Molecular Weight (MW): " +
			    electro2D.getMWbyTitle(proteinTitle));
	piLabel = new JLabel("pI Value (pI): " +
			    electro2D.getPIbyTitle(proteinTitle));

	functionLabel = new JLabel( "Function: " + proteinFunction);

        add(proteinInfoPanel);
        proteinInfoPanel.add(titleLabel);
        proteinInfoPanel.add(mwLabel);
        proteinInfoPanel.add(piLabel);
        proteinInfoPanel.add(functionLabel);
        searchPanel.add(blstSearch);
        searchPanel.add(search);
        searchPanel.add(swsSearch);
        this.setLayout(new BorderLayout());
        this.add(proteinInfoPanel, BorderLayout.NORTH);
        this.add(searchPanel, BorderLayout.CENTER);

        pack();
        setSize(380, 150);
    }

}