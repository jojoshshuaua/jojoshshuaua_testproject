//******************************************************************************
// ionex.java:	Applet
//
//******************************************************************************
import java.applet.*;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.io.*;
import java.net.*;


//==============================================================================
// Main Class for applet ionex
//
//==============================================================================
public class ionex extends Panel implements Runnable
{
	private static Font fnt = null;
	
	public static void main( String[] args ){
	Frame fr = new Frame();
	
	ionex i = new ionex();
	i.init();
	fr.add( i );
	fr.setVisible( true );
}
	// THREAD SUPPORT:
	//		m_ionex	is the Thread object for the applet
	//--------------------------------------------------------------------------
//	private Thread	m_ionex = null;
	Panel			m_controlPanel;
	IDD_DIALOG1		controls;
	ImageCanvas		m_imageCanvas;

	// variables to hold the experiment definition
	double		m_dConc1 = 0.0;
	double		m_dConc2 = 0.0;
	int			m_nBuffer = 0;
	boolean		m_bPositiveResin = true;
	CProtein[]	m_arrProteins;

	Vector		m_arrSelProteins;	//the currently selected proteins

//	CProtein[]	m_arrSelProteins;	//the currently selected proteins
//	Vector		m_vAvailProteins;
	int[]		m_arrProteinIndex= {-1, -1, -1, -1, -1};
    char        m_cState = 'S';   // the process state ([R]UN, [P]AUSE or [S]TOP)
	int			m_nCharge = 0;
	int			m_nSolSpeed = 0;
	Color[]		m_colors = {Color.red, Color.green, Color.blue, Color.cyan, Color.magenta, Color.gray};
	
	//file chooser object
	private JFileChooser fileChoose = null;
	
final int COLLOY = 25;
final int COLHIY = 226;


proteinFile[] m_arrAvailProteins;


	// ionex Class Constructor
	//--------------------------------------------------------------------------
	public ionex()
	{
		m_arrProteins = new CProtein[5];
//		m_arrSelProteins = new CProtein[5];
		m_arrSelProteins = new Vector( 5);
//		m_vAvailProteins = new Vector( 10, 10);
		fileChoose = new JFileChooser();
		m_arrAvailProteins = new proteinFile[100];
		this.init();
	}

	// APPLET INFO SUPPORT:
	//		The getAppletInfo() method returns a string describing the applet's
	// author, copyright date, or miscellaneous information.
    //--------------------------------------------------------------------------
	public String getAppletInfo()
	{
		return "Name: ionex\r\n" +
		       "Author: Kristen Cotton\r\n" +
		       "Created with Microsoft Visual J++ Version 1.1";
	}


	// The init() method is called by the AWT when an applet is first loaded or
	// reloaded.  Override this method to perform whatever initialization your
	// applet needs, such as initializing data structures, loading images or
	// fonts, creating frame windows, setting the layout manager, or adding UI
	// components.
    //--------------------------------------------------------------------------
	public void init()
	{
        // If you use a ResourceWizard-generated "control creator" class to
        // arrange controls in your applet, you may want to call its
        // CreateControls() method from within this method. Remove the following
        // call to resize() before adding the call to CreateControls();
        // CreateControls() does its own resizing.
        //----------------------------------------------------------------------
		Image	imageBkgd;
		setLayout(new BorderLayout());
		
		//get the image, wait until it's loaded
		imageBkgd = Toolkit.getDefaultToolkit().getImage(
					    "macro.gif");
		MediaTracker tracker = new MediaTracker( this);
		tracker.addImage( imageBkgd, 0);
		try{
			tracker.waitForID( 0);
		}

		catch( InterruptedException e){}

		//Initialize the canvas
		m_imageCanvas = new ImageCanvas( this);
		m_imageCanvas.setImage( imageBkgd);

		//add them to the main window layout
		add( "Center", m_imageCanvas);

		// initialize the controls in a panel
		m_controlPanel = new Panel();
		add( "East", m_controlPanel);
		fnt = new Font("arial", Font.PLAIN, 14);
		this.setFont(fnt);
		m_controlPanel.setFont( fnt);
		controls = new IDD_DIALOG1 (m_controlPanel);
//		controls = new IDD_DIALOG1 (this);
		controls.CreateControls();

		//initialize the controls
		initControls();
		resetControls();
		setState( 'S');

		//don't let the process begin until settings have been saved
		controls.IDC_START.disable();

	}

	// Place additional applet clean up code here.  destroy() is called when
	// when you applet is terminating and being unloaded.
	//-------------------------------------------------------------------------
	public void destroy()
	{
		// TODO: Place applet cleanup code here
	}

	// ionex Paint Handler
	//--------------------------------------------------------------------------
	public void paint(Graphics g)
	{
	}
	
	public static Font getMyFont(){
		return fnt;
	}

	//		The start() method is called when the page containing the applet
	// first appears on the screen. The AppletWizard's initial implementation
	// of this method starts execution of the applet's thread.
	//--------------------------------------------------------------------------
	public void start()
	{
/*		if (m_ionex == null)
		{
			m_ionex = new Thread(this);
			m_ionex.start();
		}
		// TODO: Place additional applet start code here
*/
//		m_imageCanvas.start();
	}
	
	//		The stop() method is called when the page containing the applet is
	// no longer on the screen. The AppletWizard's initial implementation of
	// this method stops execution of the applet's thread.
	//--------------------------------------------------------------------------
	public void stop()
	{
/*
		if (m_ionex != null)
		{
			m_ionex.stop();
			m_ionex = null;
		}
*/
		m_imageCanvas.stop();
		// TODO: Place additional applet stop code here
	}

	// THREAD SUPPORT
	//		The run() method is called when the applet's thread is started. If
	// your applet performs any ongoing activities without waiting for user
	// input, the code for implementing that behavior typically goes here. For
	// example, for an applet that performs animation, the run() method controls
	// the display of images.
	//--------------------------------------------------------------------------
	public void run()
	{
/*
		while (true)
		{
			try
			{
				repaint();
				// TODO:  Add additional thread-specific code here
				Thread.sleep(50);
			}
			catch (InterruptedException e)
			{
				// TODO: Place exception-handling code here in case an
				//       InterruptedException is thrown by Thread.sleep(),
				//		 meaning that another thread has interrupted this one
				stop();
			}
		}
*/
	}


	public boolean action(Event evt, Object arg)
	{
		if(evt.target instanceof Button)
		{
			if( arg.equals( "Reset Settings")){
				resetControls();
				return true;
			}

			if( arg.equals( "Load Experiment")){
				saveInput();
				return true;
			}

			if( arg.equals( "Add Protein")){
				addProtein();
				return true;
			}

			if( arg.equals( "Remove Protein")){
				removeProtein();
				return true;
			}
			if( arg.equals( "Start")){
				processStart();
				return true;
			}
			if( arg.equals( "Stop")){
				processStop();
				return true;
			}
			if( arg.equals( "Pause")){
				processPause();
				return true;
			}
		}
		return false;
	}

	//initialize the controls
	void initControls()
	{
		String				strProtein, strFile;
		File				fil;
		InputStream			in;
		DataInputStream		dis;
		String				strLine = null;
		CProtein			protein;

		//read in the list of available proteins
         try{
			fil = new File( "pdb/pdb.idx");
			in = new FileInputStream( fil );
            dis = new DataInputStream(in);
			int i = 0;

            while(( strLine = dis.readLine()) != null){
				int nPos = strLine.indexOf( '\t');
				strFile = new String( strLine.substring( nPos + 1));
				strProtein = new String( strLine.substring( 0, nPos));
				m_arrAvailProteins[i] = new proteinFile( strProtein, strFile);
				controls.IDC_SELECTPROTEIN.addItem( strProtein, i++);

//				m_vAvailProteins.addElement( strProtein);
            }                        
            in.close();
         }
		catch( FileNotFoundException e) {}
		catch( IOException e) {}
		catch( SecurityException e) {}

/*
		// populate the list of available proteins
		for( int i = 0; i < m_vAvailProteins.size(); i++){
			controls.IDC_SELECTPROTEIN.addItem( (String)m_vAvailProteins.elementAt( i));
		}
*/
/*
		// populate the list of available proteins
		for( int i = 0; i < m_arrAvailProteins.length; i++){
			if( m_arrAvailProteins[i] == null){
				break;
			}
			controls.IDC_SELECTPROTEIN.addItem(m_arrAvailProteins[ i].GetName(), i);
		}
*/
		//select the first item
		controls.IDC_SELECTPROTEIN.select( 0);
  }


	//reset the controls to the member variable values
	void resetControls()
	{
		String		strTemp;

		controls.IDC_SOLVENTA.setText(( new Double( m_dConc1)).toString());
		controls.IDC_SOLVENTB.setText(( new Double( m_dConc2)).toString());
		switch( m_nBuffer){
		case 0:
			controls.IDC_BUFFER1.setState( true);
			break;
		case 1:
			controls.IDC_BUFFER2.setState( true);
			break;
		case 2:
		default:
			controls.IDC_BUFFER3.setState( true);
			break;
	}

		if( m_bPositiveResin){
			controls.IDC_POS.setState( true);
		}
		else{
			controls.IDC_NEG.setState( true);
		}

		resetProteinList();
	}

	// 
	void resetProteinList()
	{
		int		i;

		//copy the saved protein list to the currently selected protein list
		//and add the proteins to the listbox
		controls.IDC_PROTEINS.clear();
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
				controls.IDC_PROTEINS.addItem( strTemp);
			}
			else{
				//there isn't anymore proteins, clear out any that are stored
				if( m_arrSelProteins.size() > i){
					m_arrSelProteins.removeElementAt( i);
				}
			}
		}

		//enable/disable the Add and Remove controls
		if( controls.IDC_PROTEINS.countItems() == 5){
			controls.IDC_ADD.disable();
		}
		else{
			controls.IDC_ADD.enable();
		}
		if( controls.IDC_PROTEINS.countItems() == 0){
			controls.IDC_REMOVE.disable();
		}
		else{
			controls.IDC_REMOVE.enable();
		}
	}

	//save the entered values in the member variables
	void saveInput()
	{
		int		nProteins = 0;

		//reload the image canvas
		m_imageCanvas.resetBackground();

		//disable the start button
		controls.IDC_START.disable();
//		showStatus( "Please wait while the experiment is being loaded...");

		//save the settings
		m_dConc1 = new Double( controls.IDC_SOLVENTA.getText()).doubleValue();
		m_dConc2 = new Double( controls.IDC_SOLVENTB.getText()).doubleValue();
		m_bPositiveResin = controls.IDC_POS.getState();
		if( controls.IDC_BUFFER1.getState()){
			m_nBuffer = 0;
		}
		if( controls.IDC_BUFFER2.getState()){
			m_nBuffer = 1;
		}
		if( controls.IDC_BUFFER3.getState()){
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
		controls.IDC_START.enable();
//		showStatus( "The experiment has been loaded and is ready to run.");

		//get the image canvas ready to run the experiment
		m_imageCanvas.prepareBackground();

	}

	public void removeProtein()
	{
		int nSelItem;

		//remove from listbox
		nSelItem = controls.IDC_PROTEINS.getSelectedIndex();
		if(( nSelItem < 0 ) || ( nSelItem > 4)){
			// we'll just ignore it
			return;
		}
		controls.IDC_PROTEINS.delItem( nSelItem);
		controls.IDC_ADD.enable();

		//remove from protein list
		m_arrSelProteins.removeElementAt( nSelItem);

		//reset the controls
		checkAddRemove();
	}

		void checkAddRemove()
		{
		if( m_arrSelProteins.size() <= 0){
			controls.IDC_REMOVE.disable();
		}
		else{
			controls.IDC_REMOVE.enable();
		}

		if( m_arrSelProteins.size() >= 5){
			controls.IDC_ADD.disable();
		}
		else{
			controls.IDC_ADD.enable();
		}

		}
		
	public void addProtein()
	{
		int		nAmount = 0;
		String	strName;
		String	strFile;
		int		j = 0;
 
		//get the text of the selected protein item, parse it for the protein name and the file name
		strName = new String( controls.IDC_SELECTPROTEIN.getSelectedItem());
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
				controls.IDC_PROTEINS.delItem( i);
				controls.IDC_PROTEINS.addItem( String.valueOf( nNewAmount) + "mg " + strName, i);
				return;
			}
		}

		m_arrSelProteins.addElement( new CProtein( strName, nAmount, strFile, getPH(), this));

		//add it to the listbox
		controls.IDC_PROTEINS.addItem( strAmount + "mg " + strName);

		//reset the controls
		checkAddRemove();
	}

	public void processStart()
	{
		// if starting from scratch, reload column
		if( m_cState == 'S'){
			loadColumn();

		//get the image canvas ready to run the experiment
		m_imageCanvas.prepareBackground();

		}

		//set the state
		setState('R');

//		showStatus( "The experiment is running");

		// start the animation
		m_imageCanvas.start();

	}

	public void processStop()
	{
		//set the state
		setState('S');
//		showStatus( "The experiment has stopped");
		
		// stop the animation
		m_imageCanvas.stop();
	}

	public void processPause()
	{
		//set the state
		setState('P');
//		showStatus( "The experiment is paused");

		// pause the animation
		m_imageCanvas.pause();
	}

	public void setState( char cState)
	{
		m_cState = cState;

		switch( cState){
		case 'S':
			//stopped
			controls.IDC_PAUSE.disable();
			controls.IDC_STOP.disable();
			controls.IDC_START.enable();
			controls.IDC_UPDATE.enable();
			controls.IDC_RESET.enable();
			break;

		case 'R':
			// running
			controls.IDC_PAUSE.enable();
			controls.IDC_STOP.enable();
			controls.IDC_START.disable();
			controls.IDC_UPDATE.disable();
			controls.IDC_RESET.disable();
			break;

		case 'P':
			//paused
			controls.IDC_PAUSE.disable();
			controls.IDC_STOP.enable();
			controls.IDC_START.enable();
			controls.IDC_UPDATE.disable();
			controls.IDC_RESET.disable();
			break;
		}
	}

	double getPH()
	{
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

	void loadColumn()
	{
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


class CProtein
{ 
    String		m_strName;		// string containing compound information
    String		m_strFile;		// the file name of the protein
	int			m_nPos;			// position within the column
	double      m_dPH;			// the pH of the system
	boolean		m_bBound;		// whether or not the protein is bound
	int			m_nAmount;		// amount of protein added to the column
	int			m_nBandwidth;	// width of the protein band
	boolean		m_bMix;			// indicates that this protein is mixed with others
	double		m_dCharge;
	ionex		m_applet;
	static final int	ASP = 0;
	static final int	GLU = 1;
	static final int	TYR = 2;
	static final int	CYS = 3;
	static final int	LYS = 4;
	static final int	ARG = 5;
	static final int	HIS = 6;
	static final int	CTERM = 7;
	static final int   NTERM = 8;
	int			m_arrChargedAminos[] = {0,0,0,0,0,0,0,1,1};
	double      m_arrAminoPK[] = {4.4, 4.4, 10.0, 8.5, 10.0, 12.0, 6.5, 3.1, 8.0};

	public CProtein( String strName, int nAmount, String strFile, double dPH, ionex applet)
	{
		m_strName = strName;
		m_nAmount = nAmount;
		m_strFile = strFile;
		m_dPH = dPH;
		m_applet = applet;
	}
  
	//reads in the protein information from the file specified
	public boolean load( )
	{
		URL					url;
		InputStream			in;
		DataInputStream		dis;
		String				strLine = null;
		String				strTag;

         try{
			url = new URL( "pdb/" + m_strFile);
			in = url.openStream();
            dis = new DataInputStream(in);

			//read in the file line by line and get the info we need
            while(( strLine = dis.readLine()) != null){
				strTag = new String( strLine.substring( 0, 6));
				if( strTag.equals("ATOM  ")){
					// there is an entry for each atom in the protein
					strTag = new String( strLine.substring( 11, 15));
					// we're only interested in alpha-carbons, tagged CA
					if( strTag.equals( "  CA")){
						//we need to know what type of Amino it is
						strTag = new String( strLine.substring( 17, 20));

						//we need to know the number of each of the charged Aminos
						if( strTag.equals("ASP")) m_arrChargedAminos[ASP]++;
						if( strTag.equals("GLU")) m_arrChargedAminos[GLU]++;
						if( strTag.equals("TYR")) m_arrChargedAminos[TYR]++;
						if( strTag.equals("CYS")) m_arrChargedAminos[CYS]++;
						if( strTag.equals("LYS")) m_arrChargedAminos[LYS]++;
						if( strTag.equals("ARG")) m_arrChargedAminos[ARG]++;
						if( strTag.equals("HIS")) m_arrChargedAminos[HIS]++;
					}
				}
            }                        
            in.close();
         }
		catch( FileNotFoundException e) {}
		catch( IOException e) {}
		catch( SecurityException e) {}

		//initialize the charge
		getCharge( m_dPH);


		return true;
	}

	public double getCharge( double dPH)
	{
		double	dAntilog;
		double  dCharge;

		m_dCharge = 0;

		for( int i = 0; i < 9; i++){
			dAntilog = Math.pow( 10.0, (dPH - m_arrAminoPK[i]));

			switch( i){
				// acidic aminos
			case ASP:
			case GLU:
			case CTERM:
			case TYR:
			case CYS:
				dCharge = -(dAntilog / ( 1.0 + dAntilog));
				break;

				// basic aminos
			case HIS:
			case NTERM:
			case LYS:
			case ARG:
				dCharge = 1.0 / (1.0 + dAntilog);
				break;

				// anything else
			default:
				dCharge = 0;
			}

			m_dCharge += m_arrChargedAminos[i] * dCharge;
		}

		return m_dCharge;
	}

	public double GetCharge( double dPH)
	{
		double dCharge = 0.0;

		return dCharge;
	}

	public String GetName()
	{
		return m_strName;
	}

	public int GetAmount()
	{
		return m_nAmount;
	}

	public void SetAmount( int nAmount)
	{
		m_nAmount = nAmount;
	}

		public String GetFile()
	{
		return m_strFile;
	}

	void moveProtein()
	{
		m_nPos += 1;

		if( m_nBandwidth > 3){
			m_nBandwidth -= 1;
		}
	}

}

class ImageCanvas extends Canvas implements Runnable
{
final int COLLOY = 25;
final int COLHIY = 226;
final int COLLOX = 280;
final int COLHIX = 376;
final int delay = 100;   //milliseconds between frames
final int DETECTORIGINX = 6;
final int DETECTORIGINY = 309;
final int DETECTPEAK = 259;
final int DETECTTOP = 275;

	Thread  m_animator;
    Image	m_offscreen;
    Graphics m_offgraphics;
	Image    m_imgBack;
    int		m_nTime = 0;
	ionex	m_theExp;
	Point   m_pLastConcen;
	Point   m_pNewConcen;
	Point   m_pNewDetect;
	Point   m_pLastDetect;
	double	m_dTopConc;		// concentration entering column
	double	m_dBottomConc;	// concentration leaving column

	public ImageCanvas(ionex  theExp)
	{
		m_theExp = theExp;
		m_pLastConcen = new Point(DETECTORIGINX, DETECTORIGINY);
		m_pLastDetect = new Point(DETECTORIGINX, DETECTORIGINY);
		m_pNewConcen = new Point(DETECTORIGINX, DETECTORIGINY);
		m_pNewDetect = new Point(DETECTORIGINX, DETECTORIGINY);
	}

    public void start() 
	{
		if( m_animator == null){
			m_animator = new Thread(this);
		}
		m_animator.start();
    }

    public void pause() {
		m_animator = null;
    }

    public void stop() {
//		m_animator.stop();
		m_animator = null;

		//reinitialize
		m_nTime = 0;
		m_pLastConcen.move( DETECTORIGINX, DETECTORIGINY);
		m_pLastDetect.move( DETECTORIGINX, DETECTORIGINY);
		m_pNewConcen.move( DETECTORIGINX, DETECTORIGINY);
		m_pNewDetect.move( DETECTORIGINX, DETECTORIGINY);
    }

   public void run() {

	   //Just to be nice, lower this thread's priority
        //so it can't interfere with other processing going on.
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		
        //Remember the starting time.
        long startTime = System.currentTimeMillis();

        //This is the animation loop.
        while (Thread.currentThread() == m_animator) {
            //Advance the animation frame.
            animate();

            //Display it.
            repaint();

            //Delay depending on how far we are behind.
            try {
                startTime += delay;
                Thread.sleep(Math.max(0, 
                                      startTime-System.currentTimeMillis()));
            } catch (InterruptedException e) {
                break;
            }
        }
    }

	public void paint(Graphics g) 
	{
		update( g);
	}

	public synchronized void update(Graphics g) 
	{
		//get the background image
		if (m_offscreen == null){
			m_offscreen = createImage( 488, 395);
			m_offgraphics = m_offscreen.getGraphics();

			// reset the background image
			m_offgraphics.drawImage( m_imgBack, 0, 0, null);
		}

		//now draw the protein bands
		drawProteins( m_offgraphics);

		//now draw the detector
		m_offgraphics.setColor( Color.blue);
		m_offgraphics.drawLine( m_pLastDetect.x, m_pLastDetect.y,
								m_pNewDetect.x, m_pNewDetect.y);

		//now draw the concentration graph
		m_offgraphics.setColor( Color.red);
		m_offgraphics.drawLine( m_pLastConcen.x, m_pLastConcen.y,
								m_pNewConcen.x, m_pNewConcen.y);

		// now draw the concentrations at the top and bottom of the column
		m_offgraphics.setColor( Color.white);
		m_offgraphics.fillRect( COLHIX + 5, COLLOY, 50, 10);
		m_offgraphics.fillRect( COLHIX + 5, COLHIY - 10, 50, 10);

		String strConc = new String( String.valueOf( m_dTopConc));
		m_offgraphics.setColor( Color.gray);
		m_offgraphics.drawString( formatFloat( strConc), COLHIX + 5, COLLOY + 10);

		strConc = String.valueOf( m_dBottomConc);
		m_offgraphics.drawString( formatFloat( strConc), COLHIX + 5, COLHIY);

		//now actually draw to the screen
		g.drawImage(m_offscreen, 0, 0, null);
	}

	private String formatFloat( String strF)
	{
		String	str;

		int nPos = strF.indexOf( '.');
		if(( nPos < 0) || ( nPos + 3 > strF.length())){
			str = new String( strF);
		}
		else{
			str = new String( strF.substring( 0, nPos + 3));
		}

		return str;

	}

	public void resetBackground()
	{
		// reset the background image
		m_offgraphics.drawImage( m_imgBack, 0, 0, null);

		//call repaint to draw to the screen
		repaint();
	}

	public void prepareBackground()
	{
		// reset the background image
		m_offgraphics.drawImage( m_imgBack, 0, 0, null);

		//redraw the names of the proteins
		for( int i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}

			if( m_theExp.m_arrProteins[i].m_bMix){
				//use the color for mixed proteins
				m_offgraphics.setColor( m_theExp.m_colors[5]);
			}
			else{
				m_offgraphics.setColor( m_theExp.m_colors[i]);
			}

			m_offgraphics.drawString( m_theExp.m_arrProteins[i].GetName(), 10, 344 + (i * 11));
		}

		//call repaint to draw to the screen
		repaint();
	}

	public void animate(){

		m_nTime++;

		if( m_nTime >= 460){
			// we're done
			m_theExp.processStop();
			return;
		}

		//move the proteins in the column
		moveProteins();

		//move the concentration graph point
		m_pLastConcen.move( m_pNewConcen.x, m_pNewConcen.y);
		calcConc();

		//move the detector graph point
		m_pLastDetect.move( m_pNewDetect.x, m_pNewDetect.y);
		calcDetect();

		repaint();
	}

	void moveProteins()
	{
		double		dConc;			// concentration at protein location
		CProtein	protein;
		int			i;

		//loop through the proteins and determine their positions
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}

			protein = m_theExp.m_arrProteins[i];
			
			if( protein.m_bBound){

				// find the concentration at the location of the protein
				if( m_nTime <= (150 + protein.m_nPos)){		
					// the time for the initial wash to move through the column
					dConc = m_theExp.m_dConc1;
				}
				else{
					if( m_nTime <= (300 + protein.m_nPos)){
						// concentration entering column changes
						dConc = m_theExp.m_dConc1 + 
							(float)((( m_theExp.m_dConc2 - m_theExp.m_dConc1)/150) * 
									 (m_nTime - 150 - protein.m_nPos));
					}
					else{
						// only the high concentration now
						dConc = m_theExp.m_dConc2;
					}
				}

				// determine if the protein is still bound or not
				if( Math.abs( protein.m_dCharge) < (dConc * 100)){
					protein.m_bBound = false;
				}
			}

			// protein is not bound, move it
			if( !protein.m_bBound){
				(m_theExp.m_arrProteins[i]).moveProtein();
			}
		}
	}

	void calcConc()
	{
		//find the concentration of the solvent entering and leaving the column
		if( m_nTime <= 150){
			m_dTopConc = m_theExp.m_dConc1;
			m_dBottomConc = m_theExp.m_dConc1;
		}
		else{
			if( m_nTime <= 300){		
				// concentration entering column changes
				// the time for the initial wash to move through the column
			    m_dTopConc = (m_theExp.m_dConc1 + 
						(float)((( m_theExp.m_dConc2 - m_theExp.m_dConc1)/150) * (m_nTime - 150)));
				m_dBottomConc = m_theExp.m_dConc1;
			}
			else{
				//only final wash entering column
				if( m_nTime <= 450){
					m_dTopConc = m_theExp.m_dConc2;
					m_dBottomConc = (m_theExp.m_dConc1 + 
						(float)((( m_theExp.m_dConc2 - m_theExp.m_dConc1)/150) * (m_nTime - 300)));
				}
				else{
					// only the high concentration now
					m_dTopConc = m_theExp.m_dConc2;
					m_dBottomConc = m_theExp.m_dConc2;
				}
			}
		}

		//move the graph point
		m_pNewConcen.move( DETECTORIGINX + m_nTime,
			               DETECTORIGINY - 1 - (int)(m_dBottomConc * (DETECTORIGINY - DETECTTOP)));

	}

	void calcDetect()
	{
		int		i;
		int		nPos;
		int     nMaxAmount = 0, nMixAmount = 0;
		int		nAmount;
		int		nHeight;
		int		nNewPoint;

		//move the x
		m_pNewDetect.move( DETECTORIGINX + m_nTime, DETECTORIGINY);

		// loop through the proteins, to find the largest amount entered
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}
			nMaxAmount = Math.max( nMaxAmount, m_theExp.m_arrProteins[i].m_nAmount);
		}

		//now see if there are any that are being eluted together and calculate that amount
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}

			if( m_theExp.m_arrProteins[i].m_bMix){
				nMixAmount += m_theExp.m_arrProteins[i].m_nAmount;
			}
		}

		nMaxAmount = Math.max( nMaxAmount, nMixAmount);

		// loop through the proteins, see if there's any near the bottom
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}

			//calculate the detector position for this proteins peak
			nHeight = DETECTORIGINY - DETECTPEAK;

			if( m_theExp.m_arrProteins[i].m_bMix){
				nAmount = nMixAmount;
			}
			else{
				nAmount = m_theExp.m_arrProteins[i].m_nAmount;
			}
			nHeight = (int)(nHeight * (float)((float)nAmount/(float)nMaxAmount)); //force it to use floats!

			nPos = m_theExp.m_arrProteins[i].m_nPos;

			switch( Math.abs(( COLHIY - COLLOY + 2) - ( nPos +1))){
			case 2:
				nHeight = (int)(nHeight * (4.0/7.0));
				break;

			case 1:
				nHeight = (int)(nHeight * (6.0/7.0));
				break;

			case 0:
				break;

			default:
				nHeight = 0;
			}

			nNewPoint = DETECTORIGINY - nHeight;

			// if the point is already set, only set it if
			// the new setting shows a higher detector response
			if( m_pNewDetect.y > nNewPoint){
				m_pNewDetect.y = nNewPoint;
			}
		}
	}


	public void setImage( Image img)
	{
		m_imgBack = img;
	}

	void drawProteins( Graphics g)
	{
		int	i;

		//erase the column
		g.setColor( Color.white);
		g.fillRect( COLLOX + 1, COLLOY + 1, COLHIX - COLLOX - 1, COLHIY - COLLOY - 1);

		//draw bound proteins first
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}
			if( m_theExp.m_arrProteins[i].m_bBound){
				drawProteinBand( g, i);
			}
		}

		// now draw mobil ones
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}
			if( !m_theExp.m_arrProteins[i].m_bBound){
				drawProteinBand( g, i);
			}
		}
	}

	void drawProteinBand( Graphics g, int nProtein)
	{
		Rectangle	rect;
		int			nPos, nWidth;	//placement and width of protein band

		nPos = m_theExp.m_arrProteins[ nProtein].m_nPos;
		nWidth = m_theExp.m_arrProteins[ nProtein].m_nBandwidth;
		
		//draw the protein if its still in the column
		if( nPos >= (COLHIY - COLLOY)){
			return;
		}

		rect = new Rectangle( COLLOX + 1, COLLOY + nPos,
							  COLHIX - COLLOX - 1, nWidth);

		// don't draw anything outside of the column
		if(( rect.y + rect.height) >= COLHIY){
			rect.height = COLHIY - rect.y;
		}

		// draw the protein with the correct color
		if( m_theExp.m_arrProteins[ nProtein].m_bMix){
			g.setColor( m_theExp.m_colors[5]);
		}
		else{
			g.setColor( m_theExp.m_colors[nProtein]);
		}

		g.fillRect( rect.x, rect.y, rect.width, rect.height);
	}

}

class proteinFile
{
	String	m_strName;
	String	m_strFile;

	proteinFile( String strName, String strFile)
	{
		m_strName = strName;
		m_strFile = strFile;
	}

	public String GetName()
	{
		return m_strName;
	}

	public String GetFile()
	{
		return m_strFile;
	}
}

	