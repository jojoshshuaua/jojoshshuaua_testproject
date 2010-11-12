/*
 * IonexView.java
 */

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.text.*;
import java.io.*;

/**
 * Contains the definition for the GUI of the Ionex.
 * Concerned only with showing information, NOT the actual computations
 * involved, which are handled by IonexModel.java.
 *
 * Based on the old ImageCanvas.java and IDD_DIALOG.java.
 *
 * @author Kyle Dewey
 * @author John Manning
 * @author Kristen Cotton
 */
public class IonexView extends JPanel implements IonexViewInterface,
						 ActionListener {
    // begin constants
    public static final double DEFAULT_PROTEIN_AMOUNT = 0.0;
    public static final double DEFAULT_START_NACL_CONCENTRATION = 0.0;
    public static final double DEFAULT_END_NACL_CONCENTRATION = 1.0;
    public static final double MIN_NACL_CONCENTRATION = 0.0;
    public static final double MAX_NACL_CONCENTRATION = 1.0;
    public static final String IONEX_PICTURE_PATH = "macro.gif";
    public static final int IONEX_LABEL_WIDTH = 509;
    public static final int IONEX_LABEL_HEIGHT = 558;
    public static final String PDB_START_DIRECTORY = ".";
    public static final String PROTEOME_FILE_EXTENSION = ".PROTEOME";

    // those related to the positions of graphical items
    // column
    public static final int COLUMN_START_X = 291;
    public static final int COLUMN_END_X = 386;
    public static final int COLUMN_START_Y = IonexModel.COLUMN_LOW_Y;
    public static final int COLUMN_END_Y = IonexModel.COLUMN_HIGH_Y;

    // detector
    public static final Color DETECTOR_COLOR = Color.BLUE;
    public static final int DETECTOR_START_X = 30;
    public static final int DETECTOR_START_Y = 355;
    public static final int DETECTOR_END_X = 485;
    public static final int DETECTOR_END_Y = 390;
    
    // showing actual NaCl value
    public static final Color NACL_CONC_COLOR = Color.GRAY;
    public static final int TOP_NACL_READ_X = COLUMN_END_X + 10;
    public static final int TOP_NACL_READ_Y = COLUMN_START_Y + 5;
    public static final int BOTTOM_NACL_READ_X = TOP_NACL_READ_X;
    public static final int BOTTOM_NACL_READ_Y = COLUMN_END_Y + 5;

    // concentration
    public static final Color CONCENTRATION_COLOR = Color.RED;
    public static final int CONCENTRATION_START_X = DETECTOR_START_X;
    public static final int CONCENTRATION_START_Y = DETECTOR_START_Y;
    public static final int CONCENTRATION_END_X = DETECTOR_END_X;
    public static final int CONCENTRATION_END_Y = DETECTOR_END_Y;

    // protein list
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final int PROTEIN_LIST_START_X = 12;
    public static final int PROTEIN_LIST_END_X = 485;
    public static final int PROTEIN_LIST_START_Y = 430;
    public static final int PROTEIN_LIST_END_Y = 470;

    // those related to button commands
    public static final String REMOVE_PROTEIN = "Remove Protein";
    public static final String ADD_PROTEIN = "Add Protein";
    public static final String ADD_PROTEOME = "Add Proteome";
    public static final String START = "Start";
    public static final String PAUSE = "Pause";
    public static final String LOAD_EXPERIMENT = "Load Experiment";
    public static final String RESET = "Reset Settings";
    // end constants

    // begin instance variables
    // GUI components in order that they appear in the GUI
    private JFormattedTextField solventA; // starting [NaCl]
    private JFormattedTextField solventB; // ending [NaCl]
    private JComboBox buffer; // the buffer to use, i.e. Tris
    private JComboBox resin; // the resin to use, i.e. CM-Sephadex
    private JComboBox protein; // what protein to add
    private JList columnProteins; // the proteins to be put in the column
    private JButton addProtein; // adds a protein
    private JButton addProteome; // loads in a proteome
    private JButton removeProtein; // if a protein is selected in the list,
                                   // then this becomes active and allows for
                                   // its removal from the list
    private JButton start; // makes the simulation start or unpause
    private JButton pause; // pauses the simualtion
    private JButton reset; // resets the settings (removes proteins, and sets
                           // [NaCl] to zero
    private JButton load; // loads the experiment - preps it for simulation

    // the different panels within the GUI
    private JPanel solventsPanel;
    private JPanel buffersResinsPanel;
    private JPanel addProteinPanel;
    private JPanel removeProteinPanel;
    private JPanel controlPanel;
    private JLabel ionexImage;

    // helper components for the GUI
    private IonexColumn column;
    private LineGraph solutionConcentration;
    private LineGraph detectorResponse;
    //private GraphicalList activeProteinList;

    // other needed classes
    private Set< IonexProtein > proteinsInColumn;
    private IonexModel model;
    private boolean needToClear = false;

    // for showing the NaCl concentration
    private double topNaCl = DEFAULT_START_NACL_CONCENTRATION;
    private double bottomNaCl = DEFAULT_END_NACL_CONCENTRATION;
    // end instance variables

    public IonexView() {
	proteinsInColumn = new HashSet< IonexProtein >();
	initializeGUIComponents();
	makeSubPanels();
	makeMainPanel();
	setVisible( true );
	makeDrawingComponents();
	removeProtein.setEnabled( false );
    }

    /**
     * Makes the components that perform drawing within the Ionex image
     */
    protected void makeDrawingComponents() {
	column = new IonexColumn( ionexImage,
				  COLUMN_START_X,
				  COLUMN_START_Y,
				  COLUMN_END_X,
				  COLUMN_END_Y );
	solutionConcentration = new LineGraph( ionexImage,
					       CONCENTRATION_COLOR );
	detectorResponse = new LineGraph( ionexImage,
					  DETECTOR_COLOR );
	/*activeProteinList = new GraphicalList( ionexImage,
					       PROTEIN_LIST_START_X,
					       PROTEIN_LIST_START_Y,
					       PROTEIN_LIST_END_X,
					       PROTEIN_LIST_END_Y ); */
    }

    /**
     * Makes the ionex image
     */
    protected JLabel makeIonexImage() {
	return new JLabel( new ImageIcon( IONEX_PICTURE_PATH ) ) {
	    public void setBounds( int x, int y, int w, int h ) {
		super.setBounds( x, 
				 y, 
				 IONEX_LABEL_WIDTH, 
				 IONEX_LABEL_HEIGHT );
	    }
	    public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		if ( model != null ) {
		    if ( needToClear ) {
			clearView( g );
			needToClear = false;
		    }
		    updateView( g );
		}
	    }
	};
    }

    public void clearGraphs( Graphics g ) {
	// NOTE: assumes that detector and concentration are on top
	// of each other
	Color originalColor = g.getColor();
	g.setColor( BACKGROUND_COLOR );
	g.fillRect( DETECTOR_START_X,
		    DETECTOR_START_Y,
		    DETECTOR_END_X - DETECTOR_START_X,
		    DETECTOR_END_Y - DETECTOR_START_Y );
	g.setColor( originalColor );
	solutionConcentration.reset();
	detectorResponse.reset();
    }

    public void clearView( Graphics g ) {
	//activeProteinList.clearArea( BACKGROUND_COLOR, g );
	column.blankColumn( g );
	clearGraphs( g );
    }
	
    public void updateView( Graphics g ) {
	//activeProteinList.paintComponent( g );
	column.paintComponent( model.getProteinBands(), g );
	solutionConcentration.paintComponent( g );
	detectorResponse.paintComponent( g );
	drawNumber( topNaCl,
		    TOP_NACL_READ_X,
		    TOP_NACL_READ_Y,
		    g ); // top [NaCl]
	drawNumber( bottomNaCl,
		    BOTTOM_NACL_READ_X,
		    BOTTOM_NACL_READ_Y,
		    g ); // bottom [NaCl]
    }

    /**
     * Sets up the main panel, which contains all subpanels
     */
    protected void makeMainPanel() {
	// setup everything to the right into its own panel
	JPanel allControls = new JPanel();
	allControls.setLayout( new GridLayout( 5, 1 ) );
	allControls.add( solventsPanel );
	allControls.add( buffersResinsPanel );
	allControls.add( addProteinPanel );
	allControls.add( removeProteinPanel );
	allControls.add( controlPanel );
	
	// now actually make the main panel
	this.setLayout( new GridLayout( 1, 2 ) );
	this.add( ionexImage );
	this.add( allControls );
	this.setPreferredSize( new Dimension( 500, 500 ) );
    }

    /**
     * Makes the panel for solvents
     */
    protected JPanel makeSolventsPanel() {
	JPanel retval = new JPanel();
	retval.setLayout( new GridLayout( 2, 3 ) );
	retval.add( new JLabel( "Starting NaCl Concentration:" ) );
	retval.add( solventA );
	retval.add( new JLabel( "M NaCl" ) );
	retval.add( new JLabel( "Ending NaCl Concentration:" ) );
	retval.add( solventB );
	retval.add( new JLabel( "M NaCl" ) );
	return retval;
    }

    /**
     * Makes the panel for buffers and resins
     */
    protected JPanel makeBuffersResinsPanel() {
	JPanel retval = new JPanel();
	retval.setLayout( new GridLayout( 2, 2 ) );
	retval.add( new JLabel( "Buffer:" ) );
	retval.add( buffer );
	retval.add( new JLabel( "Resin:" ) );
	retval.add( resin );
	return retval;
    }

    /**
     * Makes the panel for adding proteins
     */
    protected JPanel makeAddProteinPanel() {
	JPanel retval = new JPanel();
	retval.setLayout( new GridLayout( 1, 4 ) );
	retval.add( new JLabel( "Add Protein:" ) );
	retval.add( protein );
	retval.add( addProtein );
	retval.add( addProteome );
	return retval;
    }

    /**
     * Makes the panel for removing proteins
     */
    protected JPanel makeRemoveProteinPanel() {
	JPanel retval = new JPanel();
	retval.setLayout( new GridLayout( 1, 3 ) );
	retval.add( new JLabel( "Remove Protein:" ) );
	retval.add( new JScrollPane( columnProteins ) );
	retval.add( removeProtein );
	return retval;
    }

    /**
     * Makes the control panel
     */
    protected JPanel makeControlPanel() {
	JPanel retval = new JPanel();
	retval.setLayout( new GridLayout( 2, 2 ) );
	retval.add( start );
	retval.add( pause );
	retval.add( reset );
	retval.add( load );
	return retval;
    }

    /**
     * Connects all the GUI components to each other via JPanels and
     * various layouts
     */
    protected void makeSubPanels() {
	solventsPanel = makeSolventsPanel();
	buffersResinsPanel = makeBuffersResinsPanel();
	addProteinPanel = makeAddProteinPanel();
	removeProteinPanel = makeRemoveProteinPanel();
	controlPanel = makeControlPanel();
    }

    /**
     * Makes a button with the given label.
     * Sets this class to be an actionListener
     */
    protected JButton makeActionButton( String label ) {
	JButton retval = new JButton( label );
	retval.addActionListener( this );
	return retval;
    }

    /**
     * Initializes all buttons
     */
    protected void initializeButtons() {
	addProtein = makeActionButton( ADD_PROTEIN );
	addProteome = makeActionButton( ADD_PROTEOME );
	removeProtein = makeActionButton( REMOVE_PROTEIN );
	start = makeActionButton( START );
	pause = makeActionButton( PAUSE );
	reset = makeActionButton( RESET );
	load = makeActionButton( LOAD_EXPERIMENT );
    }

    /**
     * Initializes all GUI components
     */
    protected void initializeGUIComponents() {
	ionexImage = makeIonexImage();
	solventA = makeFloatingPointField( DEFAULT_START_NACL_CONCENTRATION,
					   MIN_NACL_CONCENTRATION,
					   MAX_NACL_CONCENTRATION,
					   false );
	solventB = makeFloatingPointField( DEFAULT_END_NACL_CONCENTRATION,
					   MIN_NACL_CONCENTRATION,
					   MAX_NACL_CONCENTRATION,
					   true );
	buffer = makeSolventsBox();
	resin = makeResinsBox();
	protein = makeProteinsBox();
	columnProteins = makeProteinsList();
	initializeButtons();
    }

    /**
     * Creates the list of proteins.
     */
    protected JList makeProteinsList() {
	JList retval = new JList( new DefaultListModel() );
	retval.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
	retval.setLayoutOrientation( JList.VERTICAL );
	retval.setCellRenderer( new DefaultListCellRenderer() {
		public Component getListCellRendererComponent( JList list,
							       Object value,
							       int index,
							       boolean selected,
							       boolean focus ) {
		    Component retval = super.getListCellRendererComponent( list,
									   value,
									   index,
									   selected,
									   focus );
		    IonexProteinBand band;
		    if ( retval instanceof JComponent &&
			 value != null &&
			 value instanceof IonexProtein &&
			 ( band = IonexProteinBand.getBand( (IonexProtein)value ) ) != null ) {
			((JComponent)retval).setForeground( band.getColor() );
		    }

		    return retval;
		}
	    } );
	retval.addListSelectionListener( new ListSelectionListener() {
		public void valueChanged( ListSelectionEvent e ) {
		    removeProtein.setEnabled( true );
		}
	    } );
	return retval;
    }

    /**
     * Creates the JComboBox for solvents.
     */
    protected JComboBox makeSolventsBox() {
	JComboBox retval = new JComboBox( Solvent.getSolventsSorted() );
	retval.setSelectedIndex( 0 );
	return retval;
    }

    /**
     * Creates the JComboBox for resins
     */
    protected JComboBox makeResinsBox() {
	JComboBox retval = new JComboBox( Resin.getResins() );
	retval.setSelectedIndex( 0 );
	return retval;
    }

    /**
     * Creates a formatted text field that will accept floating point numbers
     * @param initial An initial value for the field
     * @param min A minimal value for the field
     * @param max A maximal value for the field
     * @param isBottom If this is the bottom field
     */
    protected JFormattedTextField makeFloatingPointField( double initial,
							  final double min,
							  final double max,
							  final boolean isBottom ) {
	JFormattedTextField retval = 
	    new JFormattedTextField( new DecimalFormat() {
		    public Number parse( String source,
					 ParsePosition parsePosition ) {
			Number retval = super.parse( source,
						     parsePosition );
			if ( retval != null ) {
			    if ( retval.doubleValue() < min ) {
				retval = new Double( min );
			    } else if ( retval.doubleValue() > max ) {
				retval = new Double( max );
			    }

			    if ( isBottom ) {
				double top = getDoubleFromField( solventA );
				if ( retval.doubleValue() < top ) {
				    retval = new Double( top );
				}
			    }
			}

			return retval;
		    }
		} );

	retval.setValue( new Double( initial ) );
	return retval;
    }

    /**
     * Creates a JComboBox for proteins
     */
    protected JComboBox makeProteinsBox() {
	JComboBox retval = new JComboBox( IonexProtein.getAvailableProteins() );
	retval.setSelectedIndex( 0 );
	return retval;
    }

    /**
     * Adds the given protein to the list of proteins
     */
    protected void addProteinToList( IonexProtein protein ) {
	DefaultListModel listModel = 
	    (DefaultListModel)columnProteins.getModel();
	listModel.addElement( protein );
    }

    public void addProtein( IonexProtein toAdd ) {
	if ( !proteinsInColumn.contains( toAdd ) ) {
	    proteinsInColumn.add( toAdd );
	    addProteinToList( toAdd );
	}
    }

    /**
     * Adds the selected protein to the set of proteins that are in the column
     */
    public void addProtein() {
	addProtein( (IonexProtein)protein.getSelectedItem() );
    }

    /**
     * Removes the selected protein from the proteins that are set to
     * be in the column
     */
    public void removeProtein() {
	IonexProtein selected = (IonexProtein)columnProteins.getSelectedValue();
	DefaultListModel listModel = (DefaultListModel)columnProteins.getModel();

	proteinsInColumn.remove( selected );
	listModel.removeElementAt( columnProteins.getSelectedIndex() );
	if ( proteinsInColumn.size() == 0 ) {
	    removeProtein.setEnabled( false );
	}
    }

    /**
     * Sets all settings back to the defaults.  This means that the solvent
     * concentrations get set to 0, and all proteins are removed from the column.
     */
    public void reset() {
	solventA.setValue( new Double( DEFAULT_START_NACL_CONCENTRATION ) );
	solventB.setValue( new Double( DEFAULT_END_NACL_CONCENTRATION ) );
	proteinsInColumn.clear();
	( (DefaultListModel)columnProteins.getModel() ).clear();
	buffer.setSelectedIndex( 0 );
	resin.setSelectedIndex( 0 );
    }

    public static double getDoubleFromField( JFormattedTextField field ) {
	double retval = Double.NEGATIVE_INFINITY;
	try {
	    retval = Double.parseDouble( field.getValue().toString() );
	} catch( NumberFormatException e ) {
	    // shouldn't be possible
	}

	return retval;
    }

    /**
     * Sets up an ionex experiment.  Doesn't actually start it.
     */
    public void loadExperiment() {
	double startConcNaCl;
	double endConcNaCl;
	Solvent bufferToUse;
	Resin resinToUse;
	IonexProtein[] proteins;
	
	IonexProteinBand.reset();
	startConcNaCl = getDoubleFromField( solventA );
	endConcNaCl = getDoubleFromField( solventB );
	startConcNaCl = Double.parseDouble( solventA.getValue().toString() );
	endConcNaCl = Double.parseDouble( solventB.getValue().toString() );
	bufferToUse = (Solvent)buffer.getSelectedItem();
	resinToUse = (Resin)resin.getSelectedItem();
	proteins = proteinsInColumn.toArray( new IonexProtein[ 0 ] );
	model = new IonexModel( startConcNaCl,
				endConcNaCl,
				bufferToUse,
				resinToUse,
				proteins,
				this );
	columnProteins.repaint();
	//writeOutProteins( model.getProteinBands() );
	start.setEnabled( true );
	pause.setEnabled( false );
	needToClear = true;
    }

    /**
     * Actually starts the ionex simulation
     */
    public void startSimulation() {
	if( model != null ) {
	    start.setEnabled( false );
	    pause.setEnabled( true );
	    model.startSimulation();
	}
    }

    /**
     * Pauses the simulation
     */
    public void pauseSimulation() {
	if ( model != null ) {
	    pause.setEnabled( false );
	    start.setEnabled( true );
	    model.pauseSimulation();
	}
    }

    public static boolean isProteomeFile( String path ) {
	return path.toUpperCase().endsWith( PROTEOME_FILE_EXTENSION );
    }

    public void addProteomeFromProteomeFile( File proteome ) {
	try {
	    Scanner input = new Scanner( proteome );
	    
	    while ( input.hasNextLine() ) {
		String line = input.nextLine();
		try { 
		    IonexProtein current = IonexProtein.loadProtein( new File( line ) );
		    IonexProtein.addAvailableProtein( current );
		    addProtein( current );
		} catch ( FileNotFoundException e ) {
		    System.err.println( e );
		} catch ( IonexProteinFormatException e ) {
		    System.err.println( e );
		} catch ( IOException e ) {
		    System.err.println( e );
		}
	    }
	} catch ( FileNotFoundException e ) {
	    System.err.println( e );
	} catch ( IOException e ) {
	    System.err.println( e );
	}
    }

    public void addProteomeFromDirectory( File directory ) {
	String path = directory.getPath();
	for( IonexProtein current : IonexProtein.loadAvailableProteinsNoError( path ) ) {
	    IonexProtein.addAvailableProtein( current );
	    addProtein( current );
	}
    }

    public void addProteome() {
	JFileChooser chooser = new JFileChooser( PDB_START_DIRECTORY );
	chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
	chooser.setFileFilter( new javax.swing.filechooser.FileFilter() {
		public boolean accept( File file ) {
		    return ( file.isDirectory() || 
			     isProteomeFile( file.getName() ) );
		}
		public String getDescription() {
		    return "Proteome files or PDB directory paths";
		}
	    } );
	int chooserRetval = chooser.showOpenDialog( addProteome );
	
	if ( chooserRetval == JFileChooser.APPROVE_OPTION ) {
	    File selected = chooser.getSelectedFile();

	    if ( selected.isDirectory() ) {
		addProteomeFromDirectory( selected );
	    } else if ( isProteomeFile( selected.getName() ) ) {
		addProteomeFromProteomeFile( selected );
	    }
	}
    }

    /**
     * Code for responding to user button input
     */
    public void actionPerformed( ActionEvent e ) {
	String command = e.getActionCommand();

	if ( command.equals( REMOVE_PROTEIN ) ) {
	    removeProtein();
	} else if ( command.equals( ADD_PROTEIN ) ) {
	    addProtein();
	} else if ( command.equals( ADD_PROTEOME ) ) {
	    addProteome();
	} else if ( command.equals( START ) ) {
	    startSimulation();
	} else if ( command.equals( PAUSE ) ) {
	    pauseSimulation();
	} else if ( command.equals( LOAD_EXPERIMENT ) ) {
	    loadExperiment();
	} else if ( command.equals( RESET ) ) {
	    reset();
	}
    }

    /**
     * Makes a white box around the given area
     * intended for clearing the NaCl concentrations
     */
    protected void clearNaClArea( int x, int y ) {
	Graphics g = ionexImage.getGraphics();
	int size = 30;

	if ( g != null ) {
	    Color originalColor = g.getColor();
	    g.setColor( IonexColumn.DEFAULT_COLUMN_COLOR );
	    g.fillRect( x,
			y - size / 2,
		        size, size );
	    g.setColor( originalColor );
	}
    }

    /**
     * Updates the position of a protein in the view.
     * Note that the model calls this
     */
    public void updateProteinPosition() {
	detectorResponse.addPoint( getDetectorX(),
				   getDetectorY() );
	ionexImage.repaint();
    }

    /**
     * Updates the top NaCl concentration
     */
    public void updateTopNaClConcentration( double conc ) {
	topNaCl = conc;
	ionexImage.repaint();
    }

    /**
     * Formats the given number and draws it at the given position
     * Assumes this is for the NACL concentration
     */
    protected void drawNumber( double number,
			       int x,
			       int y,
			       Graphics g ) {
	Color originalColor = g.getColor();
	g.setColor( NACL_CONC_COLOR );
	g.drawString( formatFloat( number ),
		      x, y );
	g.setColor( originalColor );
    }

    /**
     * Updates the bottom NaCl concentration
     */
    public void updateBottomNaClConcentration( double conc ) {
	bottomNaCl = conc;
	solutionConcentration.addPoint( getConcentrationX(),
					getConcentrationY() );
	ionexImage.repaint();
    }

    /**
     * Appends the given string on to the given string until the
     * length is correct.
     */
    public static String appendUntil( String string,
				      String append,
				      int targetLength ) {
	while( string.length() < targetLength ) {
	    string = string + append;
	}

	return string;
    }

    /**
     * Formats the given number to have some number of leading digits
     * and no more than two digits after the decimal
     */
    public static String formatFloat( double number ) {
	String asString = Double.toString( number );
	int position = asString.indexOf( '.' );

	if ( !( position < 0 ||
		position + 3 > asString.length() ) ) {
	    asString = asString.substring( 0, position + 3 );
	}

	return asString;
    }
    
    public int getDetectorX() {
	int retval;

	if ( model != null ) {
	    retval = DETECTOR_START_X + (int)( model.getPercentageThrough() * 
					       ( DETECTOR_END_X - DETECTOR_START_X ) );
	} else {
	    retval = DETECTOR_START_X;
	}

	return retval;
    }

    public int getDetectorY() {
	int retval;

	if ( model != null ) {
	    double amountEluting = 0.0;
	    
	    for( IonexProteinBand current : model.getProteinBands() ) {
		amountEluting = Math.max( amountEluting,
					  model.amountEluting( current ) );
	    }
	    retval = DETECTOR_END_Y - (int)( ( DETECTOR_END_Y - DETECTOR_START_Y ) * amountEluting );
	} else {
	    retval = DETECTOR_END_Y;
	}

	return retval;
    }

    public int getConcentrationX() {
	int retval;

	if ( model != null ) {
	    retval =  CONCENTRATION_START_X + (int)( model.getPercentageThrough() *
			     ( CONCENTRATION_END_X - CONCENTRATION_START_X ) );
	} else {
	    retval = CONCENTRATION_START_X;
	}

	return retval;
    }

    public int getConcentrationY() {
	int retval;

	int yDiff = CONCENTRATION_END_Y - CONCENTRATION_START_Y;
	double maxConcDiff = MAX_NACL_CONCENTRATION - MIN_NACL_CONCENTRATION;
	double percentY = bottomNaCl / maxConcDiff; // the percentage of the y difference used
	retval = (int)( CONCENTRATION_END_Y - ( percentY * yDiff ) );
	
	return retval;
    }

    public void simulationDone() {
	start.setEnabled( false );
	pause.setEnabled( false );
    }
}
