/**
 * FileFrame.java
 *
 * This class encapsulates all the functionality required to pop up a frame
 * and load protein data from a file.
 *
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 * Accessed on November 6, 2008 by John Manning
 * looking for problems in Mac version of this file.
*/

import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.IOException;
import java.io.File;

public class FileFrame extends Frame implements ActionListener {

 /** variables for the file reading pop-up frame **/
    private Electro2D electro2D;          //reference to calling applet
    private Panel filePanel;              //panel to add components to
    private Rectangle ffd;                //dimension holders
    private Label fsLabel;                //file select label
    private TextField fileSelect;         //place to type file name
    private TextArea fileInput;           //place to paste file contents
    private Button loadButton;            //load file button
    private Label instructions;           //instructions
    private Label instructions2;         
    private Label instructions3;
    private Label flistLabel;             //brings up list of files
    private WindowListener ffwl;          //listen for window closing, etc.
    private int fileNum;
    private Choice fileChoice;
	private final String directoryString = "./../data/";

    /**
     * Constructor for FileFrame.  Initializes all of the data members
     *
     * @param e - the Electro2D object
     */
    public FileFrame( Electro2D e, int i ) {
	
	fileNum = i;
	electro2D = e;
	
	fileChoice = new Choice();
	fileChoice.setLocation(190,65);
	
	refreshFileList();

	//set the title to be displayed at the top of the frame
	setTitle("Load Protein Data File");

	filePanel = new Panel();                  //init panel
	filePanel.setLayout(null);                //abs. positioning
	filePanel.setBackground( Color.BLACK );
	
	//let the frame know to get rid of itself if the user clicks the
	// close button
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing( WindowEvent e ) {
		    dispose();
		}
	    });
	
	//store the instructions in three labels
	instructions = new Label("", Label.LEFT);
	instructions.setFont(new Font("Arial",Font.PLAIN,12));
	instructions.setForeground( Color.WHITE );
	instructions2 = new Label("", Label.LEFT);
	instructions2.setFont(new Font("Arial",Font.PLAIN,12));
	instructions2.setForeground( Color.WHITE );
	instructions3 = new Label("", Label.LEFT);
	instructions3.setFont(new Font("Arial",Font.PLAIN,12));
	instructions3.setForeground( Color.WHITE );
	instructions.setText("Instructions: Select the name of the file that" +
			     " contains your protein sequence data. "); 
	instructions2.setText("Please Note: Some files may take longer to load.");
	//	instructions3.setText("Otherwise, paste the contents of " +
	//			     "the file into the text area below.");
	//	flistLabel = new Label("Click here for a list of available files.", 
	//			       Label.LEFT);
	//flistLabel.setForeground(Color.red);
	//flistLabel.setFont(new Font("Arial",Font.PLAIN,12));

	// display the directory of files if the user clicks on the label
	//flistLabel.addMouseListener(new MouseAdapter() {
	//	public void mouseClicked(MouseEvent e) {
	//	    try{
	//		File f = new File( "./../directoryListing.html" );
	//		BrowserLauncher.openURL( f.toURL().toString() );
	//		// electro2D.getAppletContext().
	//		//showDocument(directoryListing, "_blank");
	//	    }catch( IOException exc ){
	//		System.err.println( "The error was " +
	//				    exc.getMessage() );
	//	    }
	//	}
		// change the cursor image based on its location
	//	public void mouseEntered(MouseEvent e) {
	//	    setCursor(new Cursor(Cursor.HAND_CURSOR));
	//	}
	//	public void mouseExited(MouseEvent e) {
	//	    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	//	}		    
	//   });
	
	
	fsLabel = new Label("Select Filename:", Label.RIGHT);
	fsLabel.setFont(new Font("Arial",Font.BOLD,14));
	fsLabel.setForeground( Color.WHITE );
	fileSelect = new TextField(35);       //width of 25 chars 
	loadButton = new Button("Load File");
	loadButton.addActionListener(this);  //listen for button press
	
	//size the text area for the file contents
	fileInput = new TextArea("",75,200,TextArea.SCROLLBARS_BOTH);
		
	// set the size of this frame and its contents
	this.setBounds(0,0,575,190);         //set frame position
	ffd = this.getBounds();              //store frame size

	filePanel.setBounds(0,0,ffd.width,ffd.height);

	ffd = this.getBounds();              //store frame size

	filePanel.setBounds(0,0,ffd.width,ffd.height);
	
	instructions.setBounds(60,15,600,15);
	instructions2.setBounds(145,32,500,15);
	//instructions3.setBounds(20,49,350,15);
	//flistLabel.setBounds(410,49,205,15);
	fsLabel.setBounds(65, 65, 120, 20);
	//fileSelect.setBounds(264, 80, 144, 20);
	loadButton.setBounds(this.getWidth()/2 - 40, 110, 80, 20);
	//fileInput.setBounds((ffd.width / 2) - 304, 110, 600, 300);

	this.add(filePanel);   
	//add components
	filePanel.add(instructions);
	filePanel.add(instructions2);
	//filePanel.add(instructions3);
	//filePanel.add(flistLabel);
	filePanel.add(fsLabel);
	filePanel.add( fileChoice );
	filePanel.add(loadButton);
	//filePanel.add(fileInput);
	
    }

    /**
     * This method scans the textarea for data.  If none is found, attempts to
     * load file from server.
     */
    public void loadFile() {
		
	//first, get filename from textbox
	String filename = fileChoice.getSelectedItem();
	if(filename == null || filename.equals("")) {
	    MessageFrame error = new MessageFrame();
	    error.setMessage("Please enter a file name.");
	    error.show();
	} else {
	    
	    String extension = filename.substring
		(filename.lastIndexOf(".") + 1);

	    // if the file's extention is not one of the supported types
	    // display an error message
	    if(!extension.equalsIgnoreCase("faa") && 
	       !extension.equalsIgnoreCase("fasta") &&
	       !extension.equalsIgnoreCase("pdb") &&
	       !extension.equalsIgnoreCase("gbk") &&
	       !extension.equalsIgnoreCase("e2d" )) {

		MessageFrame error = new MessageFrame();
		error.setMessage("File extension is not valid.");
		error.show();
	    } else {
		
		//If the user did not enter the contents of the file...
		if(fileInput.getText() == null || 
		   fileInput.getText().equals("")) { //...read file from server
		    
		    //call the proper method to read the file depending on 
		    // its type
		    if(extension.equalsIgnoreCase("faa") || 
		       extension.equalsIgnoreCase("fasta")) {
	
		       GenomeFileParser.fastaParse(filename, electro2D, "", fileNum);

		    } else if(extension.equalsIgnoreCase("pdb")) {
			
			GenomeFileParser.pdbParse(filename, electro2D, "", fileNum);

		    } else if(extension.equalsIgnoreCase("gbk")) {
			
			GenomeFileParser.gbkParse(filename, electro2D, "", fileNum);

		    } else if( extension.equalsIgnoreCase("e2d")) {
			GenomeFileParser.e2dParse( filename, electro2D, "", fileNum);
		    }
		  
		} else {
		    // otherwise, read the contents from the text area
		    
		    if(extension.equalsIgnoreCase("faa") || 
		       extension.equalsIgnoreCase("fasta")) {
			
			GenomeFileParser.fastaParse(filename, electro2D,
					      fileInput.getText(), fileNum);
			//System.out.println( fileInput.getText() );
			
		    } else if(extension.equalsIgnoreCase("pdb")) {
			
			GenomeFileParser.pdbParse(filename, electro2D,
					    fileInput.getText(), fileNum);
			
		    } else if(extension.equalsIgnoreCase("gbk")) {
			
			GenomeFileParser.gbkParse(filename, electro2D,
					    fileInput.getText(), fileNum);
			
		    } else if( extension.equalsIgnoreCase( "e2d" ) ){
			GenomeFileParser.e2dParse( filename, electro2D, 
					     fileInput.getText(), fileNum );
			
		    }
		}
	    }
	}
    }
    
    /**
     * This is the action listener method for the loadButton object
     *
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
	// change the cursor image
	setCursor(new Cursor(Cursor.WAIT_CURSOR));
	//try to read the contents of the file
	loadFile();
	//System.out.priln( "got here" );
	// set the cursor image back to normal
	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	// display the protein titles from the file
	if( fileNum == 1 ){
	    electro2D.refreshProteinList();
	}
	else if( fileNum == 2 ){
	    electro2D.refreshProteinList2();
	}
	
	//close the frame
	dispose();

	refreshFileList();
    }

    public void refreshFileList(){
	fileChoice.removeAll();
	File fl = new File( directoryString );
	if( !fl.exists() ){
		System.err.println( "Warning: No data files found!" );
		fl.mkdir();
	}
	String[] sa = fl.list( new ImageFilter() );
	for( int file = 0; file < sa.length; file++ ){
	      //System.out.println( sa[file] );
	      fileChoice.add( sa[file] );
	}
    }

}
