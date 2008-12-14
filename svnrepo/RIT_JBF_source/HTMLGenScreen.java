/**
 * Allows the user to choose the method by which to sort the HTML page to be
 * automatically generated.  
 *
 * @author Jill Zapoticznyj
 *
 * Created: 11/06/2003
 */

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.*;
import java.awt.event.*;

public class HTMLGenScreen extends Frame implements ActionListener,
						    MouseListener{

    //    private static Color background = new Color( 202, 225, 255 );
    private static Color background = Color.BLACK;
    private static Color foreground = Color.WHITE;
    private static final int PI_VAL = 1;
    private static final int TITLE_VAL = 0;
    private static final int FUNCTION_VAL = 3;
    private static final int MW_VAL = 2;
    private Panel contentPanel;
    private ButtonGroup buttons;
    private JRadioButton functionButton;
    private JRadioButton titleButton;
    private JRadioButton piButton;
    private JRadioButton mwButton;
    private JButton submit;
    private int sortField;
    private Electro2D electro2D;

    public HTMLGenScreen( Electro2D e ){
	super( "Sort Web Page By:" );
	setResizable(false);
	electro2D = e;
	sortField = 0;
	functionButton = new JRadioButton( "Protein Function" );
	functionButton.setActionCommand( "function" );
	functionButton.addActionListener( this );
	functionButton.setBackground( background );
	functionButton.setForeground( foreground );
	piButton = new JRadioButton( "pI Value" );
	piButton.setActionCommand( "pI" );
	piButton.addActionListener( this );
	piButton.setBackground( background );
	piButton.setForeground( foreground );
	mwButton = new JRadioButton( "Molecular Weight Value" );
	mwButton.setActionCommand( "mw" );
	mwButton.addActionListener( this );
	mwButton.setForeground( foreground );
	mwButton.setBackground( background );
	titleButton = new JRadioButton( "Protein Title" );
	titleButton.setActionCommand( "title" );
	titleButton.addActionListener( this );
	titleButton.setSelected( true );
	titleButton.setBackground( background );
	titleButton.setForeground( foreground );

	buttons = new ButtonGroup();
	buttons.add( titleButton );
	buttons.add( piButton );
	buttons.add( mwButton );
	buttons.add( functionButton );
	
	submit = new JButton( "Generate HTML Document" );
	//submit.setSize( 75, 30 );
	submit.addMouseListener( this );
    
	contentPanel = new Panel();
	contentPanel.setBackground( background );
	contentPanel.setLayout( new BoxLayout(
				       contentPanel, BoxLayout.Y_AXIS ) ); 
	contentPanel.add( titleButton );
	contentPanel.add( piButton );
	contentPanel.add( mwButton );
	contentPanel.add( functionButton );
	contentPanel.add( submit );

	this.addWindowListener( new WindowAdapter(){
		public void windowClosing( WindowEvent e ){
		    dispose();
		}
	    }
				);
	this.setBounds( 0, 0, 218, 160 );
	contentPanel.setBounds( 0, 0, 200, 150 );
	this.add( contentPanel );
	this.setVisible( true );
    }

    public void actionPerformed( ActionEvent e ){
	String cmd = e.getActionCommand();
	if( cmd.equals( "title" ) ){
	    sortField = TITLE_VAL;
	}
	else if( cmd.equals( "pI" ) ){
	    sortField = PI_VAL;
	}
	else if( cmd.equals( "mw" ) ){
	    sortField = MW_VAL;
	}
	else if( cmd.equals( "function" ) ){
	    sortField = FUNCTION_VAL;
	}
    }

    public void mouseClicked( MouseEvent e ){
	WebGenerator.setSearch( sortField );
	//open the search page and perform the search for the protein
	electro2D.generateWebPage();
	this.dispose();
    }
    public void mousePressed( MouseEvent e ){}
    public void mouseReleased( MouseEvent e ){}
    public void mouseEntered( MouseEvent e ){}
    public void mouseExited( MouseEvent e ){}
}
