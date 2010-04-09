/**
 *  The main applet class, responsible for GUI setup and more.
 *
 * @author Adam Bazinet
 * @author Jill Zapoticznyj
 *
 * Created 4/17/03
 */

import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.Font;

/**
 * The main electro2D class.
 */
public class Electro2D extends JPanel implements ActionListener {

    private FileFrame fileFrame;          //pop up for loading file data
    private JFrame proteinListFrame;       //pop up for displaying protein lists
    private JPanel proteinListPanel;
    private ProteinListButtonSwingVersion proteinListButton;
    
    /** components of the main applet **/
    private GelCanvasSwingVersion gelCanvas;          //area where animation takes place
    private HelpButtonSwingVersion helpButton;        //brings up help page
    private AboutButtonSwingVersion aboutButton;      //brings up about page
    private AddProteinButtonSwingVersion addProteinButton;   //brings up file frame
    private RemoveProteinButton removeProteinButton;  //removes proteins
    private PlayButtonSwingVersion playButton;        //starts/pauses animation
    private StopButtonSwingVersion stopButton;        //stops animation
    private RestartButtonSwingVersion restartButton;  //restarts animation
    private CSVButtonSwingVersion csvButton;          //writes to csv file
    private CompareProteinsButtonSwingVersion secondProt; //loads second file for comparison
    private java.awt.List proteinList;    //current protein list
    private int[] selectedIndexes;        //selected indexes in the list
    private AnimationChooserSwingVersion animationChooser;      //select animation to control
    private RangeChoiceSwingVersion rangeChooser;     //select the range for IEF
    private DotThread dotThread;          //thread controlling the SDS-PAGE
                                          //animation
    private ColorKeyButtonSwingVersion colorkey;      //protein color key
    private IEFThread iefThread;          //thread controlling IEF animation
    private boolean resetPressed;         //detects whether reset was pressed
                                          //or not 
    private RangeImage rangeImage;    //the disabled image for entering a range
    private Graphics graphics;
    private boolean rangeReload;      //determines whether or not the user 
                                      //enters a pH range manually or not
    private SearchProteinFieldButtonSwingVersion searchButton; //opens a frame which allows the
    //user to search through the proteins for specific information

    private PercentAcrylamideSwingVersion percentAcrylamide;  //the Choices for entering the
                                          //% acrylamide for the gel
    private Vector<JLabel> rangeLabels;
    private Vector<JLabel> mwLabels;
    private WebGenerator web;             //generates the website
    private GenerateHTMLButtonSwingVersion webButton;
    


    /** protein data vectors **/
    private String lastFileLoaded = "";   //name of the last data file loaded
    private double minMW;
    private double maxMW;
    private double minPi;
    private double maxPi;
    private Vector sequences;             //sequence data    
    private Vector sequenceTitles;        //sequence titles
    private Vector molecularWeights;      //molecular weights of proteins
    private Vector piValues;              //pI values of proteins  
    private Vector functions;             //functions of proteins

    private boolean set2ndFile = false;
    private java.awt.List proteinList2;
    private Vector sequences2;
    private Vector sequenceTitles2 = new Vector();
    private Vector functions2;
    private Vector molecularWeights2;
    private Vector piValues2;
    private FileFrame fileFrame2;

    private JPanel leftPanel;

    /**
     * This method initializes all GUI components.
     */
    public Electro2D() {
	// initializing all of the components
//	this.setTitle( "2D Electrophoresis Simulator" );
//	this.setBounds( 0, 0, 875, 667 );
//	this.addWindowListener(
	//		       new WindowAdapter() {
		//		 public void windowClosing( WindowEvent e ) {
			//		  System.exit( 0 );
				//      }
			//	  }
			  //     );
	

	fileFrame = new FileFrame(this, 1);  //init frame
	fileFrame2 = new FileFrame(this, 2);
	fileFrame.setResizable(false);    //don't allow user to change size

	web = new WebGenerator( this );
	webButton = new GenerateHTMLButtonSwingVersion(this);
	
	//read in deactivated range Image
	rangeImage = new RangeImage(
		     Toolkit.getDefaultToolkit().getImage(
					    "rangeSelectDeactivated.jpg" ) );

	rangeLabels = new Vector<JLabel>();
	mwLabels = new Vector();
	resetPressed = false;
	rangeReload = false;
	gelCanvas = new GelCanvasSwingVersion(this);
	secondProt = new CompareProteinsButtonSwingVersion(this);
	searchButton = new SearchProteinFieldButtonSwingVersion(this);
	csvButton = new CSVButtonSwingVersion( this );
	helpButton = new HelpButtonSwingVersion(this);
	aboutButton = new AboutButtonSwingVersion(this);
	addProteinButton = new AddProteinButtonSwingVersion(this);
	removeProteinButton = new RemoveProteinButton(this);
	colorkey = new ColorKeyButtonSwingVersion();
	
	playButton = new PlayButtonSwingVersion(this);
	stopButton = new StopButtonSwingVersion(this);
	restartButton = new RestartButtonSwingVersion(this);

	proteinListFrame = new JFrame( "Protein Lists" );
	proteinListFrame.setBounds( 0, 0, 300, 250 );
	proteinListFrame.setResizable(false);
	proteinListPanel = new JPanel();
	proteinListPanel.setBounds( 0, 0, 300, 250 );
	proteinListPanel.setLayout( null );
//	proteinListPanel.setBackground( Color.BLACK );
	
	proteinListButton = new ProteinListButtonSwingVersion( this );
	
	proteinList = new java.awt.List();
	proteinList.setMultipleMode(false); //Don't allow multiple selections
	proteinList2 = new java.awt.List();
	proteinList2.setMultipleMode(false);


	// give proteinList a mouse Listener to respond to the user's actions
	proteinList.addMouseListener(new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
		    setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		public void mouseExited(MouseEvent e) {
		    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	    });

	// set this class as the proteinList's action Listener to respond to
	// selections of objects
	proteinList.addActionListener(this);
	proteinList2.addActionListener(this);

	//initializing the range, voltage, percent acrylamide,
	//and animation choice boxes, providing each with mouse
	//listeners to change cursor images
	// and display information at th(I e bottom of the screen

	//voltageChooser = new Choice();
	//voltageChooser.setForeground(new Color(54,100,139));
	//voltageChooser.addItem("50 V");
	//voltageChooser.addItem("100 V");
	//give volageChooser a mouse listener to respond to user's actions
	//voltageChooser.addMouseListener(new MouseAdapter() {
	//	public void mouseEntered(MouseEvent e) {
	//	    setCursor(new Cursor(Cursor.HAND_CURSOR));
	//	}
	//	public void mouseExited(MouseEvent e) {
	//	    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	//	}
	//   });

	animationChooser = new AnimationChooserSwingVersion();

	rangeChooser = new RangeChoiceSwingVersion( this );
		
	// init %Acrylamide field and set initial value to 15
	percentAcrylamide = new PercentAcrylamideSwingVersion();
	
	sequences = new Vector();
	sequenceTitles = new Vector();
	molecularWeights = new Vector();
	piValues = new Vector();
/*
 * The following code was commented out October of 2009 during the conversion
 * of this application from awt components to Swing.
 * /
/*	this.setLayout(null);
	//this.setBackground(new Color(176, 196, 222));
	this.setBackground( new Color( 0, 0, 0 ) );
	//set bounds on components (approximate, for now) (x, y, width, height)
/* The gelCanvas line is left uncommented until it itself is converted to swing
 * /
	gelCanvas.setBounds(192, 49, 670, 600);
   //     gelCanvas.setBounds(192, 49, 100,100);
/*	helpButton.setBounds(10,47,49,20);
	aboutButton.setBounds(62,47,59,20);
	addProteinButton.setBounds(10,77,109,20);//(10, 290, 109, 20);
	removeProteinButton.setBounds(proteinListFrame.getWidth()/4 - 5, 
				      175, 137, 20);
	colorkey.setBounds( 10, 566, 88, 20 );
	playButton.setBounds( 15,175,33,33 );//(138,104,33,33);
	stopButton.setBounds( 58,179,27,27 );//(181,108,27,27);
	restartButton.setBounds( 95,179,27,27 );//(218,108,27,27);
	csvButton.setBounds( 10,536,118,20 );//( 10, 265, 118, 20 );
	webButton.setBounds( 10,506,125,20 );//(134, 265, 125, 20 );
	percentAcrylamide.setBounds( 21,368,65,50 );//( 32, 228, 65, 50 );
	rangeImage.setBounds( 19,315,78,24 );//( 149, 173, 78, 24 );
	secondProt.setBounds( 10, 446, 144, 20 );//( 126, 218, 144, 20 );
	searchButton.setBounds( 10,476,130,20);//( 126, 240, 130, 20 );
	proteinListButton.setBounds( 10,416,152,20 );//( 20, 320, 168, 20 );

	proteinList.setBounds( 7, 10, 280, 155 );
	//	voltageChooser.setBounds( 21,245,65,50 );//( 33, 107, 65, 50 );
	animationChooser.setBounds( 15, 117, 96, 50 );//( 144,50,96,50 );
	rangeChooser.setBounds( 15,248,82,50 );//( 24, 170, 82, 50 );
*/
	proteinListPanel.add( proteinList );
	proteinListPanel.add( removeProteinButton );
	proteinListFrame.getContentPane().add( proteinListPanel );
	proteinListFrame.addWindowListener( new WindowAdapter(){
		public void windowClosing( WindowEvent e ){
		    proteinListFrame.setVisible(false);
		}
	    }
					    );
/* These add lines are commented out, October 2009, to allow for structured
 * Swing GUI building in the lines of code following these
 * /
	//add components to applet panel
/*	this.add(colorkey);
	this.add(helpButton);
	this.add(aboutButton);
	this.add(addProteinButton);
	this.add(csvButton);
	this.add( webButton );
	this.add(playButton);
	this.add(stopButton);
	this.add(restartButton);
	//	this.add(voltageChooser);
	this.add(animationChooser);
	this.add( rangeChooser );
	this.add( rangeImage );
	this.add( percentAcrylamide );
	this.add( secondProt );
	this.add( gelCanvas );
	this.add( searchButton );
	this.add( proteinListButton );
	this.setBackground( Color.BLACK );
*/

       /*
        * new code for designing a Swing GUI; uses JPanels and layout managers
        * to arrange the buttons and labels to look similar to how the old awt
        * code did it
        */

       this.setLayout(new GridBagLayout());
       GridBagConstraints constraint = new GridBagConstraints();
       
       leftPanel = new JPanel();
       leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

       constraint.gridx = 0;
       constraint.gridy = 0;
       constraint.insets = (new Insets(0, 10, 0, 10));
       constraint.fill = GridBagConstraints.VERTICAL;
       this.add(leftPanel, constraint);

       constraint.gridx = 1;
       constraint.gridy = 0;
       constraint.weightx = 1.0;
       constraint.fill = GridBagConstraints.BOTH;
       this.add(gelCanvas, constraint);

       JPanel firstPanel = new JPanel();
       firstPanel.add(helpButton);
       firstPanel.add(aboutButton);
       leftPanel.add(firstPanel);

       JPanel secondPanel = new JPanel();
       secondPanel.add(addProteinButton);
       leftPanel.add(secondPanel);

       JPanel thirdPanel = new JPanel();
       thirdPanel.setLayout(new GridLayout(1, 1, 0, 0));
       thirdPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Choose Animation", TitledBorder.CENTER, TitledBorder.TOP));
       thirdPanel.add(animationChooser);
       leftPanel.add(thirdPanel);

       JPanel fourthPanel = new JPanel();
       fourthPanel.setLayout(new GridLayout(1, 3, 2, 2));
       fourthPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Animation Buttons", TitledBorder.CENTER, TitledBorder.TOP));
       fourthPanel.add(playButton);
       fourthPanel.add(stopButton);
       fourthPanel.add(restartButton);
       leftPanel.add(fourthPanel);

       JPanel fifthPanel = new JPanel();
       fifthPanel.setLayout(new GridLayout(1, 1, 0, 0));
       fifthPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Choose PH", TitledBorder.CENTER, TitledBorder.TOP));
       fifthPanel.add(rangeChooser);
       leftPanel.add(fifthPanel);

       JPanel sixthPanel = new JPanel();
       sixthPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Choose Acrylamide %", TitledBorder.CENTER, TitledBorder.TOP));
       sixthPanel.setLayout(new GridLayout(1, 1, 0, 0));
       sixthPanel.add(percentAcrylamide);
       leftPanel.add(sixthPanel);
       
       JPanel seventhPanel = new JPanel();
       JLabel additionalOptions = new JLabel("Additional Options");
       additionalOptions.setFont(new Font("SansSerif", Font.BOLD, 18));
       seventhPanel.add(additionalOptions);
       leftPanel.add(seventhPanel);

       JPanel eighthPanel = new JPanel();
       eighthPanel.add(proteinListButton);
       leftPanel.add(eighthPanel);

       JPanel ninthPanel = new JPanel();
       ninthPanel.add(secondProt);
       leftPanel.add(ninthPanel);

       JPanel tenthPanel = new JPanel();
       tenthPanel.add(searchButton);
       leftPanel.add(tenthPanel);

       JPanel eleventhPanel = new JPanel();
       eleventhPanel.add(webButton);
       leftPanel.add(eleventhPanel);

       JPanel twelfPanel = new JPanel();
       twelfPanel.add(csvButton);
       leftPanel.add(twelfPanel);

       JPanel thirteenthPanel = new JPanel();
       thirteenthPanel.add(colorkey);
       leftPanel.add(thirteenthPanel);

    }

    /**
     * Accessor method for the leftPanel instance variable so that
     * GelCanvasSwingVersion can correctly set its getMinimumSize() method.
     *
     * @return the leftPanel variable that holds all of the buttons
     */
    public JPanel getButtonPanel() {
        return leftPanel;
    }

	
	/**
     * Any drawing on the applet panel itself is done here.
     */

    public void displayProtList(){
	
	proteinListFrame.validate();
	proteinListFrame.setVisible(true);

    }

    /**
     * displays the incrementing pH values above the gel after the IEF
     * animation.
     *
     * @param loc - the location of the label
     * @param value - the value to be placed on the label
     */
    public ArrayList<Integer> showPH() {

        JLayeredPane layeredPane = ((JFrame)this.getTopLevelAncestor()).getLayeredPane();
        double minPH = getMinRange();
        double maxPH = getMaxRange();
        ArrayList<Integer> linePositions = new ArrayList<Integer>();

        double pHOffset = (maxPH-minPH)/7;
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        pHOffset = Double.valueOf(twoDForm.format(pHOffset));

        int labelOffset = gelCanvas.getWidth()/7;
        for(int i = 0; i < 8; i++) {
            JLabel newLabel;
            if(i==7) {
                newLabel = new JLabel(twoDForm.format(maxPH));
                newLabel.setBounds(gelCanvas.getX() + (i*labelOffset)-20, gelCanvas.getY(), 40, 15);
            } else {
                newLabel = new JLabel(twoDForm.format(minPH + i*pHOffset));
                newLabel.setBounds(gelCanvas.getX() + (i*labelOffset) -9, gelCanvas.getY(), 40, 15);
            }
            layeredPane.add(newLabel);
            rangeLabels.add(newLabel);
            layeredPane.setLayer(newLabel, JLayeredPane.PALETTE_LAYER);
            linePositions.add((gelCanvas.getX() +(i*labelOffset) -9));
            
        }

        return linePositions;
    }

    /**
     * This method removes the labels that show the pH values.  It is called
     * when the restart button is pressed to clear the canvas.
     */
    public void clearpH(){
	
	// remove each of the labels from the applet's image
	for( int i = 0; i < rangeLabels.size(); i++ ){
	    this.remove( (JLabel)rangeLabels.elementAt( i ) );
	}
	
	//remove all of the labels from the vector
	rangeLabels.removeAllElements();
	//clear the molecular weight labels as well (since this method is
	// only called from the restart button)
	clearMW();
	//repaint the applet to reflect the change
	this.repaint();
    }

    /**
     * This method removes the labels that show the molecular weight values.
     * It is called every time the dot animation is stopped in order to reflect
     * the change in position of the dots.
     */
    public void clearMW(){
	//remove all of the labels from the applet's image
	for( int i = 0; i < mwLabels.size(); i++ ){
	    this.remove((JLabel)mwLabels.elementAt(i));
	}
	//remove all of the labels from the vector
	mwLabels.removeAllElements();
	//repaint the applet to reflect the change
	this.repaint();
    }

    /**
     * Creates and displays the labels that reflect the position of a
     * particular molecular weight along the gel.  It is called every time the
     * dot animation is stopped in order to reflect the change in position
     * of the dots.
     *
     * @param loc100 - the location of the 100,000 molecular weight
     * @param loc50 - the location of the 50,000 molecular weight
     * @param loc25 - the location of the 25,000 molecular weight
     * @param loc10 - the location of the 10,000 molecular weight
     */
    public void showMW(int loc100, int loc50, int loc25, int loc10, boolean reMake) {

        JLayeredPane layeredPane = ((JFrame)this.getTopLevelAncestor()).getLayeredPane();
        JLabel hundredK = new JLabel("100K");
	mwLabels.add(hundredK);
        layeredPane.add(hundredK);
        layeredPane.setLayer(hundredK, JLayeredPane.PALETTE_LAYER);
	((JLabel)mwLabels.elementAt(mwLabels.size() - 1)).setBounds(gelCanvas.getX() - 30, loc100+20, 30, 15);
	((JLabel)mwLabels.elementAt(mwLabels.size() - 1)).setForeground(Color.BLACK);

	JLabel fiftyK = new JLabel("50K");
        mwLabels.add(fiftyK);
        layeredPane.add(fiftyK);
        layeredPane.setLayer(fiftyK, JLayeredPane.PALETTE_LAYER);
	((JLabel)mwLabels.elementAt(mwLabels.size() - 1)).setBounds(gelCanvas.getX() - 20, loc50+20, 30, 15);
	((JLabel)mwLabels.elementAt(mwLabels.size() - 1)).setForeground(Color.BLACK);

	JLabel twentyfiveK = new JLabel("25K");
        mwLabels.add(twentyfiveK);
        layeredPane.add(twentyfiveK);
        layeredPane.setLayer(twentyfiveK, JLayeredPane.PALETTE_LAYER);
	((JLabel)mwLabels.elementAt(mwLabels.size() - 1)).setBounds(gelCanvas.getX() - 20, loc25+20, 30, 15);
	((JLabel)mwLabels.elementAt(mwLabels.size() - 1 )).setForeground(Color.BLACK);

	JLabel tenK = new JLabel("10K");
        mwLabels.add(tenK);
        layeredPane.add(tenK);
        layeredPane.setLayer(tenK, JLayeredPane.PALETTE_LAYER);
	((JLabel)mwLabels.elementAt(mwLabels.size() - 1)).setBounds(gelCanvas.getX() - 20, loc10+20, 30, 15);
	((JLabel)mwLabels.elementAt(mwLabels.size() - 1)).setForeground(Color.BLACK);

    }

    /**
     * Changes the choice selected on the animationChooser Choice box to
     * SDS-PAGE after the IEF animation is completed.
     */
    public void setSDS(){

	//chose the SDS-PAGE value in animationChooser
	animationChooser.setSelectedItem( "SDS-PAGE" );
	//repaint the applet to reflect the change
	//this.repaint();
    }

    /**
     * Changes the choice selected on the animationChooser Choice box to
     * IEF after the reset button is pressed.
     */
    public void setIEF(){
	//choose the IEF value in animationChooser
	animationChooser.setSelectedItem( "IEF" );
	//repaint the applet to reflect the change
	//this.repaint;
    }

    /**
     * Returns the top-level frame that contains the specified
     * component, or null if there is no top-level frame.
     */
    Frame getFrame(Component comp) {
	Component theTop = null;
	Component parent = comp;

	while (parent != null) {
	    theTop = parent;
	    parent = parent.getParent();
	}

	return (theTop instanceof Frame ? (Frame)theTop : null);
    }

    /**
     * Brings up the TextField to enable a user to enter a range for
     * the IEF animation.
     */
    public void allowSelectRange(){
	if( !rangeReload ){
	    
	    // remove the image and add the two text fields to allow the user
	    // to enter a range

	    remove( rangeImage );
	    update( graphics );
	    rangeReload = true;
	}
    }

    /**
     * Disables the TextField that allows a range to be entered
     */
    public void disableSelectRange(){
	if( rangeReload ){
	    
	    // remove the two text fields and add the image to disable the
	    // manual entry of a range

	    add( rangeImage );
	    update( graphics );
	    rangeReload = false;
	}
    }

    /**
     *  Retrieves protein sequence information from a file.
     *  This is achieved by popping up a new window with options for 
     *  selecting a file from the server, or copying and pasting a file 
     *  into a textbox.
     *  Currently, supported file types are .fasta, .faa, .gbk, and .pdb.
     */
    public void getSequenceData() {

	// display the fileFrame
	fileFrame.toFront();
	fileFrame.setVisible(true);
    }

    public void getSequenceData2(){
	fileFrame2.toFront();
	fileFrame2.setVisible(true);
	proteinList2.setBounds( 10, 170, 250, 155 );
	removeProteinButton.setBounds( proteinListFrame.getWidth()/4 - 5,
				       340, 137, 20 );
	proteinListPanel.add( proteinList2 );
	proteinListPanel.setBounds( 0, 0, 300, 420 );
	proteinListFrame.setBounds( 0, 0, 300, 420 );
	//proteinList.setBounds( 10, 20, 250, 155 );
	//this.repaint();
	proteinListFrame.validate();
    }




    /**
     * Brings up the help.html page.
     */
    public void showHelpPage() {
	
	//create a URL object
//  	     //catch and display any errors that occurred while assigning
//  	     //information to the URL


	 
	 //if no errors occurred, open a new window and display the help page
	// if(helpPage != null){
	     // getAppletContext().showDocument(helpPage, "_blank");
	File f = new File( "HTML Files" + File.separator + "Help" + File.separator + "help.html" );
	try{
	    BrowserLauncher.openURL( f.toURL().toString() );
	} catch(IOException e ){System.err.println( e.getMessage());}
	// }
    }

    /**
     * Brings up the about.html page.
     */
    public void showAboutPage() {
	
	// create a URL object
	//   URL aboutPage = null;

	 //if no errors occurred, open a new window and display the about page
	     File f = new File( "HTML Files" + File.separator + "about.html" );
	     System.out.println( f.exists() );
	     try{
		 BrowserLauncher.openURL( f.toURL().toString() );
	     }catch(IOException e ){System.err.println( e.getMessage()); e.printStackTrace();}
    }

    /**
     * Brings up a SwissProt-TrEMBL search for the particular protein
     * id provided
     */
    public void showSwsSearchPage( String id ){
	URL searchPage = null;
	String searchID = "http\u003A\u002F\u002Fus.expasy.org/cgi-bin/sprot-"
	    + "search-ful?makeWild=&SEARCH=" + id;  //the URL address of the 
	                                            //search page

	try{
	    //try to assign the search id to searchPage
	    searchPage = new URL( searchID );
	}catch( MalformedURLException e ){
	    // if an error occurs, display it to the user
	    System.err.println( "The error was " + e );
	}
	
	//if no errors occured, open the search page
	if( searchPage != null ){
	    try{
	    //getAppletContext().showDocument( searchPage, "_blank" );
	    BrowserLauncher.openURL( searchID );
	    }catch(IOException e ){System.err.println( e.getMessage() );}
	}
	
    }

   /**
     * Brings up a Blast search for the particular protein
     * sequence provided
     */
    public void showBlastSearchPage( String seq ){
	URL searchPage = null;
	String searchID = "http\u003A\u002F\u002Fus.expasy.org/cgi-bin/blast."+
	    "pl?sequence=" + seq;  //the URL address of the 
	                                            //search page

	try{
	    //try to assign the search id to searchPage
	    searchPage = new URL( searchID );
	}catch( MalformedURLException e ){
	    // if an error occurs, display it to the user
	    System.err.println( "The error was " + e );
	}
	
	//if no errors occured, open the search page
	if( searchPage != null ){
	    try{
	    BrowserLauncher.openURL( searchID );
	    }catch(IOException e){System.err.println( e.getMessage());}
	    //getAppletContext().showDocument( searchPage, "_blank" );
	}
	
    }

    /**
     * Brings up GenBank search for the particular protein
     * id provided
     */
    public void showSearchPage( String id ){
	
	//create a URL object
	URL searchPage = null;
	String searchId = "";  //the name used in the search
	
	//get the name of the loaded file and figure out the extention
	String filename = getLastFileLoaded();
	String extention = filename.substring( filename.indexOf( "." ) + 1 );

	// if the extention is .pdb, create the proper search string for the 
	// URL by searching for the name of the pdb file.
	if( extention.equals( "pdb" ) ){
	    id = filename.substring( 0, filename.indexOf( "." ) );
	    searchId = "http\u003A\u002F\u002Fwww.ncbi.nlm.nih.gov" +
	   "\u002Fentrez\u002Fquery.fcgi?db=structure&" + "cmd=search&term=" +
		id;
	}
	else{

	    // otherwise, use the id passed to the method as the name for
	    // the search.
	    searchId = "http\u003A\u002F\u002Fwww.ncbi.nlm.nih.gov" +
	     "\u002Fentrez\u002Fquery.fcgi?db=protein&" + "cmd=search&term=" +
	     id;
	    }

	try{
	    //assign the search information to the URL 
	    searchPage = new URL( searchId );
	}catch( MalformedURLException e ){
	    //catch and display any errors that occurred when assigning the
	    //information to the URL
	    System.err.println( "Bad URL: " + searchPage );
	}

	//if no errors occurred, open a new window with the search results
	if( searchPage != null ){
	    try{
	    BrowserLauncher.openURL( searchId );
	    }catch(IOException e ){System.err.println( e.getMessage() );}
	    //getAppletContext().showDocument( searchPage, "_blank" );
	}
    }

    /**
     * Brings up Enzyme Commission search for the particular protein
     * id provided
     */
    public void showECSearchPage( String id ){
	
	int index = 0;
	//create a URL object
	//URL searchPage = null;
	String searchId = "";  //the name used in the search
	Vector ecNums = new Vector(); //holds the EC numbers contained in the
	                              // id string
	
	while( id.length() > 0 && id.indexOf( "\u003B" ) != -1 ){
	    ecNums.addElement( id.substring( 0, id.indexOf( "\u003B" ) ) );
	    index = id.indexOf( "\u003B" );
	    if( index + 1 == id.length() ){
		id = "";
	    }
	    else{
		id = id.substring( id.indexOf( "\u003B" ) + 1 );
		id = id.trim();
	    }
	}

	searchId = "http\u003A\u002F\u002Fwww.genome.ad.jp\u002Fdbget-bin"+
		"\u002Fwww_bget?enzyme+";
	    for( int d = 0; d < ecNums.size(); d++ ){
		ecNums.insertElementAt( (searchId +
					 (String)ecNums.elementAt(d)), d );
		ecNums.removeElementAt( d + 1 );
	    }

	try{
	    //assign the search information to the URL
	    for( int d = 0; d < ecNums.size(); d++ ){
		ecNums.insertElementAt( (new URL((String)ecNums.elementAt(d))),
					d );
		ecNums.removeElementAt( d + 1 );
	    }
	}catch( MalformedURLException e ){
	    //catch and display any errors that occurred when assigning the
	    //information to the URL
	    System.err.println( "Bad URL: " + searchId );
	}
	catch( Exception f ){
	    System.err.println( "The error was " + f.getMessage() );
	}

	//if no errors occurred, open a new window with the search results
	if( ecNums.size() != 0 ){
	    try{
	    for( int d = 0; d < ecNums.size(); d++ ){
		BrowserLauncher.openURL( ((URL)ecNums.elementAt(d)).toString());
	    }
	    }catch(IOException e ){System.err.println( e.getMessage() );}
	}
    }
    
    /**
     *  Cycles through the list and removes any highlighted proteins.
     */
    public void removeHighlightedProteins() {
	selectedIndexes = proteinList.getSelectedIndexes();
	String[] selectedItems = proteinList.getSelectedItems();
	for(int x = 0; x < selectedIndexes.length; x++) {
	    removeProteinbyTitle( selectedItems[ x ] );
	    proteinList.remove(selectedIndexes[x] - x);
	    
	}
	if( sequenceTitles2 != null ){
	    int[] selectedIndexes2 = proteinList2.getSelectedIndexes();
	    selectedItems = proteinList2.getSelectedItems();
	    for( int x = 0; x < selectedIndexes2.length; x++ ){
		removeProteinbyTitle2( selectedItems[x] );
		proteinList2.remove( selectedIndexes2[x]-x );
	    }
	}
    }
    
    /**
     * this method returns the voltage selected by the user
     *
     * @return a string
     */
    public String getVoltage(){
	return "50 V";
    }

    /**
     * this method returns the maximum pH range that the user selected
     *
     * @return max the maximum pH to be used in the simulation
     */
    public double getMaxRange(){
	// set up the default maximum range of 10
	Double max = new Double(10);
	String ranges = (String) rangeChooser.getSelectedItem();
	// if the user selected a range from the list, supply the correct max
	if( ranges.equals( "3 - 10" ) ){
	    max = 10.0;
            return (double) max;
	}
	
	else if( ranges.equals( "4 - 7" ) ){
	    max = 7.0;
            return (double) max;
	}
	/**
         * if the user chose to enter their own value, first check to make sure
         * it is in the correct format
	 */
        else if (ranges.matches("\\d+.?\\d*-\\d+.?\\d*")){
            /**
             * split the user's range into a String array so that Scanner can
             * find the correct Double
             */
            String[] lowAndHigh = ranges.split("-");
            Scanner scan = new Scanner(lowAndHigh[1]);
            max = scan.nextDouble();
            /**
             * the maximum pH cannot be below 0 or above 14 because a pH of any
             * other value is a physical impossibility
             */
            if ((max < 0) || (max > 14)) {

                MessageFrame mess = new MessageFrame();
                String m = max + " is not a valid number.  Please " +
                    "press restart and try again using values between 0" +
                    "and 14.";
                mess.setMessage( m );
                mess.setVisible(true);

            } else {
                // return the user's maximum pH
                return (double) max;

            }

        }
        /**
         * if none of the provided choices were selected and the user did not
         * enter a value, then use the default max of 10
         */
        return (double) max;
    }
    
    /**
     * this method returns the minimum pH range that the user selected
     *
     * @return min the minimum pH to be used in the simulation
     */

    public double getMinRange(){
        // set up the default minimum pH of 3
        Double min = new Double(3);
	String ranges = (String) rangeChooser.getSelectedItem();
        /**
         * if the user selected one of the provided choices set the minimum pH
         * to the appropriate value
         */
	if( ranges.equals( "3 - 10" )){
	    min = 3.0;
            return (double) min;
	}

	else if( ranges.equals( "4 - 7" ) ){
	    min = 4.0;
            return (double) min;
	}
        /**
         * if the user chose to enter their own value, check to make sure that
         * it is in the correct format
         */
	else if (ranges.matches("\\d+.?\\d*-\\d+.?\\d*")){
            // make the scanner object to find the correct Double
            String[] lowAndHigh = ranges.split("-");
            Scanner scan = new Scanner(lowAndHigh[0]);
            min = scan.nextDouble();
            /**
             * the minimum pH cannot be below 0 or above 14 because a pH of any
             * other value is a physical impossibility
             */
            if ((min < 0) || (min > 14)) {
                    
                MessageFrame mess = new MessageFrame();
                String m = min + " is not a valid number.  Please " +
                    "press restart and try again using values between 0" +
                    "and 14.";
                mess.setMessage( m );
                mess.setVisible(true);
                    
            } else {
                // return the user's minimum pH value
                return (double) min;
                    
            }

        }
        /**
        * if the user did not select any of the provided choices and did not
        * enter their own pH value, use the default pH value of 3
        */
        return (double) min;

    }

    /**
     * Returns the value stored in the % acrylamide text field
     *
     * @return percent
     */
    public double getLowPercent(){
	// get the value in the text box
	String value = (String) percentAcrylamide.getSelectedItem();
	value = value.trim();
	//return value;
	double percent = -1;
	
	if( value.indexOf( "-" ) != -1 ){
	    value = value.substring( 0, value.indexOf( "-" ) );
	    value = value.trim();
	}
         // try to change the value into a number
        try{
	    percent = Double.parseDouble( value );
	}catch( NumberFormatException e ){
	    //if the value was not a valid number, display the error
	    // message in a new frame
	    MessageFrame mess = new MessageFrame();
	    String m = value + " is not a valid number.  Please " +
		"press restart and try again.";
	    mess.setMessage( m );
	    mess.setVisible(true);
	
	}
	
	return percent;
    }

    /**
     * Returns the higher of the values stored in the % acrylamide text field
     *
     * @return percent
     */
    public double getHighPercent(){
	// get the value in the text box
	String value = (String) percentAcrylamide.getSelectedItem();
	value = value.trim();
	//return value;
	double percent = -1;
	
	if( value.indexOf( "-" ) != -1 ){
	    value = value.substring( value.indexOf( "-" ) + 1 );
	    value = value.trim();
	}
         // try to change the value into a number
        try{
	    percent = Double.parseDouble( value );
	}catch( NumberFormatException e ){
	    //if the value was not a valid number, display the error
	    // message in a new frame
	    MessageFrame mess = new MessageFrame();
	    String m = value + " is not a valid number.  Please " +
		"press restart and try again.";
	    mess.setMessage( m );
	    mess.setVisible(true);
	
	}
	
	return percent;
    }
    
    

    /**
     * this method returns the animation selected by the user
     *
     * @return a string
     */
    public String getAnimationChoice(){
	return (String) animationChooser.getSelectedItem();
    }

    /**
     * this method resets the play button's image to the play image
     */
    public void resetPlay(){
	playButton.resetPlay();
    }

    /**
     *  Refreshes the protein list, called after reading in new proteins 
     *  from a file. Currently, the list is cleared and re-filled with 
     *  proteins from the latest file read in.
     */
    public void refreshProteinList() {
	// removes everything from the list of proteins
	proteinList.removeAll();
	// refreshes the list with the new protein titles
	for(int x = 0; x < sequenceTitles.size(); x++) {
	    proteinList.add((String)sequenceTitles.elementAt(x));
	}
    }
    
    /**
     * Given a protein title from proteinList, removes the protein's 
     * information from the vectors.
     *
     * @param title the title to be removed
     */
    public boolean removeProteinbyTitle(String title) {
	for(int x = 0; x < sequenceTitles.size(); x++) {
	    if(((String)sequenceTitles.elementAt(x)).equals(title)) {
		molecularWeights.removeElementAt(x);
		piValues.removeElementAt(x);
		sequenceTitles.removeElementAt(x);
		return true;
	    }
	}
	
	return false;
    }

    /**
     * Given a protein title from proteinList2, removes the protein's
     * information from the vectors.
     *
     * @param title the title to be removed
     */
    public boolean removeProteinbyTitle2( String title ){
	if( sequenceTitles2 != null ){
	    for( int x = 0; x < sequenceTitles2.size(); x++ ){
		if(((String)sequenceTitles2.elementAt(x)).equals(title)){
		    molecularWeights2.removeElementAt(x);
		    piValues2.removeElementAt(x);
		    sequenceTitles2.removeElementAt(x);
		    return true;
		}
	    }
	}
	return false;
    }
    
    /***/
    public void refreshProteinList2() {
	proteinList2.removeAll();
	for( int x = 0; x < sequenceTitles2.size(); x++ ){
	    proteinList2.add((String)sequenceTitles2.elementAt(x));
	}
    }

    /**
     *  Responds to a double-click in the protein list by bringing up a 
     *  new window with information on that protein.
     */
    public void actionPerformed(ActionEvent e) {
	//set up a new frame, give it ref. to the applet and the protein name
	ProteinFrame proteinFrame = new ProteinFrame(this,
						     e.getActionCommand(), 1);
	//proteinFrame.setResizable(false);
	proteinFrame.setVisible(true);
	proteinFrame.updateLabel();
	
	if( playButton.getSdsStatus() ){
	    gelCanvas.drawLocation( e.getActionCommand() );
	}
    }

    /**
     * Sets the SDS-PAGE status in playButton to false to let the 
     * actionListener know that the SDS-PAGE portion of the animation
     * has not been drawn
     */
    public void resetSdsStatus(){
	playButton.resetSdsStatus();
    }

    /**
     * Reinitializes the dotThread 
     */
    public void restartThread(){
	//initializes the thread, sets a value, and calls the start method
	dotThread = new DotThread( gelCanvas, this );
	dotThread.startDots();
	dotThread.start();
    }
    
    /**
     * this method is called by the reset button once it is pressed.
     * it lets the applet know it should reload the protein information
     */
    public void setBool(){
	resetPressed = true;
    }
    
    /**
     * This method lets the applet know that the information has already been
     * loaded and the reset button has not been pressed.
     */
    public void resetBool(){
	resetPressed = false;
    }

    /**
     * This method returns the current state of the reset button.  If it is
     * true, the button has been pressed and false if it has not been pressed
     *
     * @return resetPressed - state of reset button
     */
    public boolean getBool(){
	return resetPressed;
    }

    /**
     * This method reinitializes the IEFThread in charge of the IEF animation.
     */
    public void restartIEF(){
	//initializes the thread, sets a value and calls the start method
	iefThread = new IEFThread( gelCanvas, this );
	iefThread.setIEF();
	iefThread.start();
    }

    /**
     * This method returns an instance of the IEFThread
     *
     * @return iefThread
     */
    public IEFThread getIEFThread(){
	return iefThread;
    }

    /**
     * This method resets a value for the IEF animation in the PlayButton class
     */
    public void resetIEF(){
	playButton.resetIEF();
    }
	

    /**
     * Stops the dotThread
     */
    public void stopThread(){
	dotThread.stopDots();
    }

    /**
     * Utility methods.
     */
    
    /**
     * Returns the last file loaded
     *
     * @return lastFileLoaded
     */
    public String getLastFileLoaded() {
	return lastFileLoaded;
    }

    /**
     * Returns the gelcanvas object
     *
     * @return gelCanvas
     */
    public GelCanvasSwingVersion getGel() {
	return gelCanvas;
    }

    /**
     * Returns the sequences of amino acids
     *
     * @return sequences
     */
    public Vector getSequences() {
	return sequences;
    }

    /**
     * Returns the sequence titles
     *
     * @return sequenceTitles
     */
    public Vector getSequenceTitles() {
	return sequenceTitles;
    }

    /**
     * Returns the DotThread
     *
     * @return dotThread
     */
    public DotThread getThread(){
	return dotThread;
    }

    /**
     * Returns the molecular weights
     *
     * @return molecularWeights
     */
    public Vector getMolecularWeights() {
	return molecularWeights;
    }

    /**
     * returns the molecular weight of a protein given its name
     *
     * @return a string
     */
    public String getMWbyTitle(String title) {
	for(int x = 0; x < sequenceTitles.size(); x++) {
	    if(((String)sequenceTitles.elementAt(x)).equals(title)) {
		return (String)molecularWeights.elementAt(x);
	    }
	}

	if( sequenceTitles2 != null ){
	    for( int x = 0; x < sequenceTitles2.size(); x++ ){
		if(((String)sequenceTitles2.elementAt(x)).equals(title)){
		    return (String)molecularWeights2.elementAt(x);
		}
	    }
	}
	return "";
    }

    /** 
     * returns the function of a protein given its name
     *
     * @return a string
     */
    public String getFunctionbyTitle(String title) {
	for(int x = 0; x < sequenceTitles.size(); x++) {
	    if(((String)sequenceTitles.elementAt(x)).equals(title)) {
		return (String)functions.elementAt(x);
	    }
	}

	if( sequenceTitles2 != null ){
	    for( int x = 0; x < sequenceTitles2.size(); x++ ){
		if(((String)sequenceTitles2.elementAt(x)).equals(title)){
		    return (String)functions2.elementAt(x);
		}
	    }
	}
	return "";
    }

    /**
     * returns the pI value of a protein given its name
     *
     * @return a string
     */
    public String getPIbyTitle(String title) {
	for(int x = 0; x < sequenceTitles.size(); x++) {
	    if(((String)sequenceTitles.elementAt(x)).equals(title)) {
		return (String)piValues.elementAt(x);
	    }
	}
	if( sequenceTitles2 != null ){
	    for( int x = 0; x < sequenceTitles2.size(); x++ ){
		if(((String)sequenceTitles2.elementAt(x)).equals(title)){
		    return (String)piValues2.elementAt(x);
		}
	    }
	}
	return "";
    }

    /**
     * returns the sequence value of a protein given its name
     *
     * @return a string
     */
    public String getSequencebyTitle(String title) {
	for(int x = 0; x < sequenceTitles.size(); x++) {
	    if(((String)sequenceTitles.elementAt(x)).equals(title)) {
		return (String)sequences.elementAt(x);
	    }
	}
	if( sequenceTitles2 != null ){
	    for( int x = 0; x < sequenceTitles2.size(); x++ ){
		if(((String)sequenceTitles2.elementAt(x)).equals(title)){
		    return (String)sequences2.elementAt(x);
		}
	    }
	}
	return "";
    }

    /**
     * sets the values for the max and min pI and MW
     *
     * @param minmw the min MW val
     * @param maxmw the max MW val
     * @param minpi the min pI val
     * @param maxpi the max pI val
     */
    public void setMaxAndMinVals( double maxmw, double minmw, double maxpi, 
				  double minpi ){
	maxMW = maxmw;
	minMW = minmw;
	maxPi = maxpi;
	minPi = minpi;
    }

    public double getMaxPi(){
	return maxPi;
    }
    
    public double getMinPi(){
	return minPi;
    }
    
    public double getMaxMW(){
	return maxMW;
    }

    public double getMinMW(){
	return minMW;
    }

    /**
     * returns the pI values
     *
     * @return piValues
     */
    public Vector getPiValues() {
	return piValues;
    }

    /**
     * returns the protein functions
     *
     * @return functions
     */
    public Vector getFunctions(){
	return functions;
    }

    public Vector getPiValues2(){
	return piValues2;
    }

    public Vector getSequences2(){
	return sequences2;
    }

    public Vector getSequenceTitles2(){
	return sequenceTitles2;
    }

    public Vector getMolecularWeights2(){
	return molecularWeights2;
    }

    public Vector getFunctions2(){
	return functions2;
    }

    /**
     * stores the name of the last file loaded
     */
    public void setLastFileLoaded(String l) {
	lastFileLoaded = l;
    }

    /**
     * stores the vector of sequence data
     */
    public void setSequences(Vector s){
	sequences = s;
    }

    /**
     * sets the vector of sequence titles
     */
    public void setSequenceTitles(Vector st) {
	sequenceTitles = st;
    }

    /**
     * sets the vector of molecular weights
     */
    public void setMolecularWeights(Vector mw) {
	molecularWeights = mw;
    }

    /**
     * sets the vector of protein functions
     */
    public void setFunctionValues( Vector fcn ){
	functions = fcn;
    }

    /**
     * sets the vector of pI values
     */
    public void setPiValues(Vector pi) {
	piValues = pi;
    }

    public void setSequences2( Vector s ){
	sequences2 = new Vector();
	sequences2 = s;
    }

    public void setSequenceTitles2( Vector st ){
	sequenceTitles2 = new Vector();
	sequenceTitles2 = st;
    }

    public void setMolecularWeights2( Vector mw ){
	molecularWeights2 = new Vector();
	molecularWeights2 = mw;
    }

    public void setPiValues2( Vector pi ){
	piValues2 = new Vector();
	piValues2 = pi;
    }
    public void setFunctionValues2( Vector fcn ){
	functions2 = new Vector();
	functions2 = fcn;
    }

    /**
     * Standard applet methods.
     */
    public void start() { }
    public void stop() { }
    public void destroy() { }

    /**
     * Return a short info string.
     *
     * @return a string
     */
    public String getAppletInfo() {
	return "Electro2D...copyright 2003 Adam L Bazinet & Jill Zapoticznyj";
    }   
    
    public void writeToCSV(){
	CSVCreator csv = new CSVCreator( this );
	csv.writeToCSV();
    }
    
    public void generateWebPage(){
	web.genFile( this.getLastFileLoaded() );
    }

//    public static void main( String[] args ){
//	if( args.length >= 5 ){
//	    Electro2D e = new Electro2D();
//	    String file = args[2];
//	    String extention = file.substring( file.indexOf( "." ) + 1 );
//	    if( extention.equals( "faa" ) ){
//		GenomeFileParser.fastaParse( file, e, "", 1 );
//	    }
//	    else if( extention.equals( "pdb" ) ){
//		GenomeFileParser.pdbParse( file, e, "", 1 );
//	    }
//	    else{
//		System.err.println( file + " is not a valid file.  Please "
//				    + "enter a .pdb or .faa file." );
//		System.exit( 1 );
//	    }
//	    int numSecs = Integer.parseInt( args[4] );
//	      e.setVisible( true );
//	      //e.setVisible(false);
//	    //e.setVisible( false );
//	    PositionGenerator posGen = new PositionGenerator( numSecs,
//							      e );
//	    //e.setVisible( false );
//	    posGen.start();
//	    //System.exit( 0 );
//
//
//	}
//	else{
//		Frame jf = new Frame();
//		jf.addWindowListener(
//			       new WindowAdapter() {
//				 public void windowClosing( WindowEvent e ) {
//					  System.exit( 0 );
//				      }
//				  }
//			       );
//		jf.setTitle( "2D Electrophoresis Simulator" );
//		jf.setBounds( 0, 0, 875, 667 );
////		jf.setBackground( Color.BLACK );
//	    Electro2D e = new Electro2D();
////	    e.setBackground( Color.BLACK );
//	    jf.add(e);
//	    jf.setResizable( false );
//	    jf.setVisible(true);
//	}
//    }
//
} //Electro2D










