/*
 * Ionex.java
 * Version: $Id:$
 * Revisions: $Log:$
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.io.*;


/**
 * The main class for the ionex frame, which runs a simulation
 * of an Ion Exchange Chromatography.
 *
 * @author Kyle Dewey
 * @author John Manning
 * @author Kristen Cotton
 */
public class Ionex extends JPanel implements ActionListener {

    private static Font fnt = null;
    
    public static void main( String[] args ){
	JFrame fr = new JFrame();
	
	Ionex i = new Ionex();
	i.init();
	fr.add( i );
	fr.pack();
	fr.setVisible( true );
	fr.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }
    
    JPanel			m_controlPanel;
    IDD_DIALOG1		controls;
    ImageCanvas		m_imageCanvas;
    
    // variables to hold the experiment definition
    double		m_dConc1 = 0.0;
    double		m_dConc2 = 0.0;
    int			m_nBuffer = 0;
    boolean		m_bPositiveResin = true;
    CProtein[]	m_arrProteins;
    
    Vector		m_arrSelProteins;	//the currently selected proteins
    
    int[]		m_arrProteinIndex= {-1, -1, -1, -1, -1};
    char        m_cState = 'S';   // the process state ([R]UN, [P]AUSE or [S]TOP)
    int			m_nCharge = 0;
    int			m_nSolSpeed = 0;
    Color[]		m_colors = {Color.red, Color.green, Color.blue, Color.cyan, Color.magenta, Color.gray};
    
    //file chooser object
    private JFileChooser fileChoose = null;
    
    final int COLLOY = 25;
    final int COLHIY = 226;
    
    
    ProteinFile[] m_arrAvailProteins;
    
    
    // ionex Class Constructor
    //--------------------------------------------------------------------------
    public Ionex() {
	m_arrProteins = new CProtein[5];
	m_arrSelProteins = new Vector( 5);
	fileChoose = new JFileChooser();
	m_arrAvailProteins = new ProteinFile[100];
	this.init();
    }
    
    // The init() method is called by the AWT when an applet is first loaded or
    // reloaded.  Override this method to perform whatever initialization your
    // applet needs, such as initializing data structures, loading images or
    // fonts, creating frame windows, setting the layout manager, or adding UI
    // components.
    //--------------------------------------------------------------------------
    public void init() {
        // If you use a ResourceWizard-generated "control creator" class to
        // arrange controls in your applet, you may want to call its
        // CreateControls() method from within this method. Remove the following
        // call to resize() before adding the call to CreateControls();
        // CreateControls() does its own resizing.
        //----------------------------------------------------------------------
	Image	imageBkgd;
	setLayout(new BorderLayout());
	
	//get the image, wait until it's loaded
	imageBkgd = Toolkit.getDefaultToolkit().getImage("macro.gif");
	MediaTracker tracker = new MediaTracker( this);
	tracker.addImage( imageBkgd, 0);
	try{
	    tracker.waitForID( 0 );
	}
	catch( InterruptedException e){}
	
	//Initialize the canvas
	m_imageCanvas = new ImageCanvas( this);
	m_imageCanvas.setImage( imageBkgd );
	
	//add them to the main window layout
	add( "Center", m_imageCanvas);
	
	// initialize the controls in a panel
	m_controlPanel = new JPanel();
	add( "East", m_controlPanel);
	fnt = new Font("arial", Font.PLAIN, 14);
	this.setFont(fnt);
	m_controlPanel.setFont( fnt);
	controls = new IDD_DIALOG1 (m_controlPanel);
	//		controls = new IDD_DIALOG1 (this);
	controls.CreateControls( this );
	
	//initialize the controls
	initControls();
	resetControls();
	setState( 'S');
	
	//don't let the process begin until settings have been saved
	controls.IDC_START.setEnabled( false );
	
    }
    
    public void actionPerformed( ActionEvent evt ) {
	String arg = evt.getActionCommand();

	if( arg.equals( "Reset Settings")){
	    resetControls();
	} else if( arg.equals( "Load Experiment")){
	    saveInput();
	} else if( arg.equals( "Add Protein")){
	    addProtein();
	} else if( arg.equals( "Remove Protein")){
	    removeProtein();
	} else if( arg.equals( "Start")){
	    processStart();
	} else if( arg.equals( "Stop")){
	    processStop();
	} else if( arg.equals( "Pause")){
	    processPause();
	}
    }

    //initialize the controls
    void initControls() {
	String				strProtein, strFile;
	String				strLine = null;
	CProtein			protein;
	BufferedReader input;
	
	//read in the list of available proteins
	try{
	    input = new BufferedReader( new FileReader( "pdb/pdb.idx" ) );
	    int i = 0;
	    
            while(( strLine = input.readLine()) != null) {
		int nPos = strLine.indexOf( '\t');
		strFile = new String( strLine.substring( nPos + 1));
		strProtein = new String( strLine.substring( 0, nPos));
		m_arrAvailProteins[i] = new ProteinFile( strProtein, strFile);
		controls.IDC_SELECTPROTEIN.addItem( strProtein );
		
                i++;
            }
            input.close();
	}
	catch( FileNotFoundException e) {}
	catch( IOException e) {}
	catch( SecurityException e) {}
	
	//select the first item
	//controls.IDC_SELECTPROTEIN.select( 0 );
    }


    //reset the controls to the member variable values
    void resetControls() {
	String		strTemp;
	
	controls.IDC_SOLVENTA.setText(( new Double( m_dConc1)).toString());
	controls.IDC_SOLVENTB.setText(( new Double( m_dConc2)).toString());
	switch( m_nBuffer){
	case 0:
	    controls.IDC_BUFFER1.setSelected( true);
	    break;
	case 1:
	    controls.IDC_BUFFER2.setSelected( true);
	    break;
	case 2:
	default:
	    controls.IDC_BUFFER3.setSelected( true);
	    break;
	}
	
	if( m_bPositiveResin){
	    controls.IDC_POS.setSelected( true);
	}
	else{
	    controls.IDC_NEG.setSelected( true);
	}
	
	resetProteinList();
    }
    
    //
    void resetProteinList() {
	int		i;
	
	//copy the saved protein list to the currently selected protein list
	//and add the proteins to the listbox
	controls.IDC_PROTEINS.setModel( new DefaultListModel() );
	
	for( i = 0; i < 5; i++){
	    if( m_arrProteins[i] != null){
		CProtein protein = new CProtein( m_arrProteins[i].GetName(),
						 m_arrProteins[i].GetAmount(),
						 m_arrProteins[i].GetFile(),
						 getPH(),
						 this);
		m_arrSelProteins.setElementAt( protein, i);
		String strTemp = new String( String.valueOf( m_arrProteins[i].GetAmount()) +
					     "mg " + m_arrProteins[i].GetName());
                
                DefaultListModel dlm = (DefaultListModel) controls.IDC_PROTEINS.getModel();
                dlm.addElement( strTemp);
                controls.IDC_PROTEINS.setModel( dlm );
		
	    }
	    else{
		//there isn't anymore proteins, clear out any that are stored
		if( m_arrSelProteins.size() > i){
		    m_arrSelProteins.removeElementAt( i);
				}
	    }
	}
	
	//enable/disable the Add and Remove controls
        DefaultListModel dlm = (DefaultListModel) controls.IDC_PROTEINS.getModel();
	
	if( dlm.size() == 5){
	    controls.IDC_ADD.setEnabled( false );
	}
	else{
	    controls.IDC_ADD.setEnabled( true );
	}
	if( dlm.size() == 0){
	    controls.IDC_REMOVE.setEnabled( false );
	}
	else{
	    controls.IDC_REMOVE.setEnabled( true );
	}
    }
    
    //save the entered values in the member variables
    void saveInput() {
	int		nProteins = 0;
	
	//reload the image canvas
	m_imageCanvas.resetBackground();
	
	//disable the start button
	controls.IDC_START.setEnabled( false );
	//		showStatus( "Please wait while the experiment is being loaded...");
	
	//save the settings
	m_dConc1 = new Double( controls.IDC_SOLVENTA.getText()).doubleValue();
	m_dConc2 = new Double( controls.IDC_SOLVENTB.getText()).doubleValue();
	m_bPositiveResin = controls.IDC_POS.isSelected();
	if( controls.IDC_BUFFER1.isSelected()){
	    m_nBuffer = 0;
	}
	if( controls.IDC_BUFFER2.isSelected()){
	    m_nBuffer = 1;
	}
	if( controls.IDC_BUFFER3.isSelected()){
	    m_nBuffer = 2;
	}
	
	//copy the currently selected protein list to the saved protein list
	for( int i = 0; i < 5; i++){
	    if( i < m_arrSelProteins.size()){
		CProtein p = (CProtein)m_arrSelProteins.elementAt(i);
		CProtein protein = new CProtein( p.GetName(),
						 p.GetAmount(),
						 p.GetFile(),
						 getPH(),
						 this);
		protein.load();
		m_arrProteins[i] = protein;
	    }
	    else{
		m_arrProteins[i] = null;
	    }
	}
	
	//load the column
	loadColumn();
	
	//the process may begin now
	controls.IDC_START.setEnabled( true );
	
	
	//get the image canvas ready to run the experiment
	m_imageCanvas.prepareBackground();
	
    }
    
    public void removeProtein() {
	int nSelItem;
	
	//remove from listbox
	nSelItem = controls.IDC_PROTEINS.getSelectedIndex();
	if(( nSelItem < 0 ) || ( nSelItem > 4)){
	    // we'll just ignore it
	    return;
	}
	
        DefaultListModel dlm = (DefaultListModel) controls.IDC_PROTEINS.getModel();
	dlm.removeElementAt( nSelItem );
        controls.IDC_PROTEINS.setModel( dlm );
	controls.IDC_ADD.setEnabled( true );
	
	//remove from protein list
	m_arrSelProteins.removeElementAt( nSelItem);
	
	//reset the controls
	checkAddRemove();
    }

    void checkAddRemove() {
	if( m_arrSelProteins.size() <= 0){
	    controls.IDC_REMOVE.setEnabled( false );
	}
	else{
	    controls.IDC_REMOVE.setEnabled( true );
	}

	if( m_arrSelProteins.size() >= 5){
	    controls.IDC_ADD.setEnabled( false );
	}
	else{
	    controls.IDC_ADD.setEnabled( true );
	}
	
    }
    
    public void addProtein() {
	int		nAmount = 0;
	String	strName;
	String	strFile;
	int		j = 0;
	
	//get the text of the selected protein item, parse it for the protein name and the file name
	strName = new String( controls.IDC_SELECTPROTEIN.getSelectedItem().toString() );
	j = controls.IDC_SELECTPROTEIN.getSelectedIndex();
	strFile = m_arrAvailProteins[j].GetFile();
	
	// get the amount entered
	String strAmount = controls.IDC_AMOUNT.getText();
	if( strAmount.equals( "")){
	    nAmount = 0;
	}
	else{
	    nAmount = new Integer( strAmount).intValue();
	}
	
	// see if this protein is already in the list, if so add the amount
	for( int i = 0; i < m_arrSelProteins.size(); i++){
	    CProtein p = (CProtein)m_arrSelProteins.elementAt(i);
	    if( strFile.equals( p.GetFile())){
		int nNewAmount = p.GetAmount() + nAmount;
		p.SetAmount( nNewAmount);
		
		//add the new amount to the listbox
                DefaultListModel dlm = (DefaultListModel) controls.IDC_PROTEINS.getModel();
		dlm.removeElementAt( i );
		dlm.add( i, String.valueOf( nNewAmount) + "mg " + strName );
                controls.IDC_PROTEINS.setModel( dlm );
		return;
	    }
	}
	
	m_arrSelProteins.addElement( new CProtein( strName, nAmount, strFile, getPH(), this));
	
	//add it to the listbox
        DefaultListModel dlm = (DefaultListModel) controls.IDC_PROTEINS.getModel();
	dlm.addElement( strAmount + "mg " + strName);
        controls.IDC_PROTEINS.setModel( dlm );
	
	//reset the controls
	checkAddRemove();
    }

    public void processStart() {
	// if starting from scratch, reload column
	if( m_cState == 'S'){
	    loadColumn();
	    
	    //get the image canvas ready to run the experiment
	    m_imageCanvas.prepareBackground();
	    
	}
	
	//set the state
	setState('R');
	
	
	
	// start the animation
	m_imageCanvas.start();
	
    }
    
    public void processStop() {
	//set the state
	setState('S');
	
	
	// stop the animation
	m_imageCanvas.stop();
    }
    
    public void processPause() {
	//set the state
	setState('P');
	
	
	// pause the animation
	m_imageCanvas.pause();
    }
    
    public void setState( char cState) {
	m_cState = cState;
	
	switch( cState){
	case 'S':
	    //stopped
	    controls.IDC_PAUSE.setEnabled( false );
	    controls.IDC_STOP.setEnabled( false );
	    controls.IDC_START.setEnabled( true );
	    controls.IDC_UPDATE.setEnabled( true );
	    controls.IDC_RESET.setEnabled( true );
	    break;
	    
	case 'R':
	    // running
	    controls.IDC_PAUSE.setEnabled( true );
	    controls.IDC_STOP.setEnabled( true );
	    controls.IDC_START.setEnabled( false );
	    controls.IDC_UPDATE.setEnabled( false );
	    controls.IDC_RESET.setEnabled( false );
	    break;
	    
	case 'P':
	    //paused
	    controls.IDC_PAUSE.setEnabled( false );
	    controls.IDC_STOP.setEnabled( true );
	    controls.IDC_START.setEnabled( true );
	    controls.IDC_UPDATE.setEnabled( false );
	    controls.IDC_RESET.setEnabled( false );
	    break;
	}
    }
    
    double getPH() {
	switch( m_nBuffer){
	case 0:
	    return 4.8;
	case 1:
	    return 7.2;
	case 2:
	    return 8.0;
	}
	
	//should never happen
	return 0.0;
    }
    
    void loadColumn() {
	CProtein	protein;
	int			nPos = 1;
	double		arrCharge[] = {0,0,0,0,0};
	int			nIndex = 0;
	int			nTemp;
	int			i,j;
	
	//first loop through the proteins and determine whether
	//or not they are initially bound to the resin
	for( i = 0; i < m_arrProteins.length; i++){
	    if( m_arrProteins[i] == null){
		continue;
	    }
	    
	    protein = m_arrProteins[i];
	    
	    // a protein is not bound if it has the same charge as
	    // the resin or if its charge is less than the initial
	    // salt concentration
	    if((( !m_bPositiveResin) && (protein.m_dCharge <= m_dConc1 * 100))||
	       ((m_bPositiveResin) && (protein.m_dCharge >= -m_dConc1 * 100))){
		protein.m_bBound = false;
		
		//the width of unbound protein band is 3
		protein.m_nBandwidth = 3;
	    }
	    else{
		protein.m_bBound = true;
		
		//calculate the width of the protein band
		protein.m_nBandwidth = protein.m_nAmount/2;
		
		// save the charge information of bound proteins
		arrCharge[i] = Math.abs( protein.m_dCharge);
	    }
	}
	
	//put the highest charge proteins first in the column
	for( i = 0; i < m_arrProteins.length; i++){
	    if( m_arrProteins[i] == null){
		continue;
	    }
	    
	    // find the highest charge in the array
	    nTemp = i;
	    for( j = 0; j < m_arrProteins.length; j++){
		if( arrCharge[ j] > arrCharge[ nTemp]){
		    nTemp = j;
		}
	    }
	    
	    protein = m_arrProteins[ nTemp];
	    
	    // set the position of the protein within the column
	    protein.m_nPos = nPos;
	    
	    //if the protein is bound then it occupies space, otherwise not
	    if( protein.m_bBound){
		nPos += protein.m_nBandwidth;
		
		//if part of the protein doesn't fit in the column, recalculate the
		//bandwidth and the amount
		if( protein.m_nPos + protein.m_nBandwidth > COLHIY - COLLOY){
		    protein.m_nBandwidth = (COLHIY - COLLOY - 1) - protein.m_nPos;
		    protein.m_nAmount = protein.m_nBandwidth * 2;
		}
	    }
	    
	    //if the protein is out of the column, then set the amount to 0
	    if( protein.m_nPos > COLHIY - COLLOY){
		protein.m_nBandwidth = 0;
		protein.m_nAmount = 0;
	    }
	    
	    // indicate that this one has been done
	    arrCharge[nTemp] = -1;
	}
	
	//if more than one protein is not bound, it is mixed with others in the
	//same band, set the member variable
	nTemp = -1;
	for( i = 0; i < m_arrProteins.length; i++){
	    if( m_arrProteins[i] == null){
		continue;
	    }
	    
	    protein = m_arrProteins[i];
	    
	    if( protein.m_bBound == false){
		if( nTemp == -1){
		    nTemp = i;
		}
		else{
		    protein.m_bMix = true;
		    protein = m_arrProteins[nTemp];
		    protein.m_bMix = true;
		}
	    }
	}
    }
}
