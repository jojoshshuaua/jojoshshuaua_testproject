/**
 * Allows the user to choose the method by which to sort the HTML page to be
 * automatically generated.  
 *
 * @author Jill Zapoticznyj
 *
 * Created: 11/06/2003
 */

import javax.swing.*;
import java.awt.event.*;

public class HTMLGenScreen extends JFrame implements ActionListener,
						    MouseListener{

    private static final int PI_VAL = 1;
    private static final int TITLE_VAL = 0;
    private static final int FUNCTION_VAL = 3;
    private static final int MW_VAL = 2;
    private JPanel contentPanel;
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
	piButton = new JRadioButton( "pI Value" );
	piButton.setActionCommand( "pI" );
	piButton.addActionListener( this );
	mwButton = new JRadioButton( "Molecular Weight Value" );
	mwButton.setActionCommand( "mw" );
	mwButton.addActionListener( this );
	titleButton = new JRadioButton( "Protein Title" );
	titleButton.setActionCommand( "title" );
	titleButton.addActionListener( this );
	titleButton.setSelected( true );

	buttons = new ButtonGroup();
	buttons.add( titleButton );
	buttons.add( piButton );
	buttons.add( mwButton );
	buttons.add( functionButton );
	
	submit = new JButton( "Generate HTML Document" );
	submit.addMouseListener( this );
    
	contentPanel = new JPanel();
	contentPanel.setLayout( new BoxLayout(
				       contentPanel, BoxLayout.Y_AXIS ) ); 
	contentPanel.add( titleButton );
	contentPanel.add( piButton );
	contentPanel.add( mwButton );
	contentPanel.add( functionButton );
	contentPanel.add( submit );

	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	this.add( contentPanel );
	this.setVisible( true );
        pack();
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
	electro2D.generateWebPage();
	this.dispose();
    }
    public void mousePressed( MouseEvent e ){}
    public void mouseReleased( MouseEvent e ){}
    public void mouseEntered( MouseEvent e ){}
    public void mouseExited( MouseEvent e ){}
}
