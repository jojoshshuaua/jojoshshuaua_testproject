import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class SearchProteinFunction implements MouseListener, ActionListener{
    
    private Vector dots1;
    private Vector dots2;
    private GelCanvasSwingVersion gel;
    private Electro2D electro2D;
    private JFrame window;
    private JButton search;
    private JButton reset;
    private JTextField searchTerm;
    private JTextField excludeTerm;
    private JPanel buttonPane;
    private JPanel buttonLabelPane;
    private JPanel buttonSelectionPane;
    private JPanel searchLabelPane;
    private JPanel searchFieldPane;
    private JPanel textPane;
    private JLabel includesLabel;
    private JLabel excludesLabel;
    private String searchField;
    private ButtonGroup radioButtons;
    private JRadioButton sequenceButton;
    private JRadioButton titleButton;
    private JRadioButton functionButton;

    public SearchProteinFunction( Electro2D e2D ){
	
	electro2D = e2D;
	radioButtons = new ButtonGroup();
	
	gel = electro2D.getGel();
	dots1 = new Vector();
	dots2 = new Vector();
	excludesLabel = new JLabel( "Excludes: " );
	includesLabel = new JLabel( "Includes: " );
	searchTerm = new JTextField();
	excludeTerm = new JTextField();
	buttonPane = new JPanel();
	buttonLabelPane = new JPanel();
	buttonSelectionPane = new JPanel();
	textPane = new JPanel();
	searchLabelPane = new JPanel();
	searchFieldPane = new JPanel();
	window = new JFrame();
	window.addWindowListener( new WindowAdapter(){
		public void windowClosing( WindowEvent e ) {
					  window.hide();
				      }
				  }
				  );

        search = new JButton("Search");
        reset = new JButton("Reset");
        search.addActionListener(new SearchListener());
        reset.addActionListener(new ResetListener());
	sequenceButton = new JRadioButton( "Sequence", false );
	titleButton = new JRadioButton( "Protein Title", true );
	functionButton = new JRadioButton( "Protein Function", false );
        searchField = "function";
	sequenceButton.addActionListener( this );
	titleButton.addActionListener( this );
	functionButton.addActionListener( this );
	sequenceButton.setActionCommand( "sequence" );
	titleButton.setActionCommand( "title" );
	functionButton.setActionCommand( "function" );
	functionButton.setMnemonic( KeyEvent.VK_F );
	sequenceButton.setMnemonic( KeyEvent.VK_S );
	titleButton.setMnemonic( KeyEvent.VK_T );
	radioButtons.add( sequenceButton );
	radioButtons.add( titleButton );
	radioButtons.add( functionButton );

	buttonLabelPane.setLayout( new GridLayout(0,1) );
	JLabel labelPane = new JLabel( "Select Search Field" );
	buttonLabelPane.add( labelPane );
	buttonSelectionPane.setLayout( new GridLayout( 0, 1 ) );
	buttonSelectionPane.add( titleButton );
	buttonSelectionPane.add( functionButton );
	buttonSelectionPane.add( sequenceButton );
	
	buttonPane.setLayout( new BorderLayout() );
	buttonPane.add( buttonLabelPane, BorderLayout.NORTH );
	buttonPane.add( buttonSelectionPane, BorderLayout.CENTER );
	searchLabelPane.setLayout( new GridLayout(0,1));
	Label searchLabel = new Label( "Enter Search Term" );
	searchLabelPane.add( searchLabel );
	searchFieldPane.setLayout( new GridLayout(0,1) );
	searchFieldPane.add( includesLabel );
	searchFieldPane.add( searchTerm );
	searchFieldPane.add( excludesLabel );
	searchFieldPane.add( excludeTerm );
	searchFieldPane.add( search );
	searchFieldPane.add( reset );
	
	textPane.setLayout( new BorderLayout() );
	textPane.add( searchLabelPane, BorderLayout.NORTH );
	textPane.add( searchFieldPane, BorderLayout.CENTER );
	

	window.setLayout( new BorderLayout() );
	window.setTitle( "Search Proteins" );
	window.add( buttonPane, BorderLayout.WEST );
	window.add( textPane, BorderLayout.EAST );
	window.pack();
	window.setVisible( true );
	
    }

    private class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            searchFor(searchTerm.getText(), excludeTerm.getText());
        }
    }

    private class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayAll();
        }
    }

    public void searchFor( String fcnName, String limitations ){
	dots1 = gel.getDots();
	dots2 = gel.getDots2();

        if (dots1 == null) {
            dots1 = new Vector();
        }

        if (dots2 == null) {
            dots2 = new Vector();
        }

	ProteinDotSwingVersion prot = null;
	if( searchField.equals( "function" ) ){
	    for( int i = 0; i < dots1.size(); i++ ){
		prot = (ProteinDotSwingVersion)dots1.elementAt( i );
		if( ((E2DProtein)prot.getPro()).getFunction().indexOf( fcnName )
		    == -1 ){
		    prot.doNotShowMe();
		}
		else if( ((E2DProtein)prot.getPro()).getFunction().indexOf(
			limitations ) != -1 && !limitations.equals( "" ) ){
		    prot.doNotShowMe();
		}
	    }
	    if( dots2.size() != 0 ){
		for( int j = 0; j < dots2.size(); j++ ){
		    prot = (ProteinDotSwingVersion)dots2.elementAt( j );
		    if( ((E2DProtein)prot.getPro()).getFunction().indexOf(
				      		       fcnName ) == -1 ){
			prot.doNotShowMe();
		    }
		    else if(((E2DProtein)prot.getPro()).getFunction().indexOf(
			    limitations ) != -1 && !limitations.equals( "" ) ){
			prot.doNotShowMe();
		    }
		}
	    }
	}
	else if(searchField.equals( "sequence" )){
	    for( int i = 0; i < dots1.size(); i++ ){
		prot = (ProteinDotSwingVersion)dots1.elementAt( i );
		if(((E2DProtein)prot.getPro()).getSequence().indexOf( fcnName )
		   == -1 ){
		    prot.doNotShowMe();
		}
	    }
	    if( dots2.size() != 0 ){
		for( int j = 0; j < dots2.size(); j++ ){
		    if(((E2DProtein)prot.getPro()).getSequence().indexOf( fcnName)
		       == -1 ){
			prot.doNotShowMe();
		    }
		}
	    }
	}
	else if(searchField.equals( "title" ) ){
	    for( int i = 0; i < dots1.size(); i++ ){
		prot = (ProteinDotSwingVersion)dots1.elementAt( i );
		if(((E2DProtein)prot.getPro()).getID().indexOf( fcnName ) 
		   == -1 ){
		    prot.doNotShowMe();
		}
		else if(((E2DProtein)prot.getPro()).getID().indexOf( 
			   limitations ) != -1 && !limitations.equals("" ) ){
		    prot.doNotShowMe();
		}
	    }
	    if( dots2.size() != 0 ){
		for( int j = 0; j < dots2.size(); j++ ){
		    prot = (ProteinDotSwingVersion)dots2.elementAt( j );
		    if(((E2DProtein)prot.getPro()).getID().indexOf( fcnName)
		       == -1 ){
			prot.doNotShowMe();
		    }
		    else if(((E2DProtein)prot.getPro()).getID().indexOf(
			   limitations ) != -1 && !limitations.equals( "" ) ){
			prot.doNotShowMe();
		    }
		}
	    }
	}
        int count = 0;
        for(ProteinDotSwingVersion dot : (Vector<ProteinDotSwingVersion>)dots1) {
            if (dot.getShowMe()) {
                count++;
            }
        }
        for(ProteinDotSwingVersion dot : (Vector<ProteinDotSwingVersion>)dots2) {
            if (dot.getShowMe()) {
                count++;
            }
        }
        JOptionPane.showMessageDialog(null, "Search found " + count + " proteins.");
        count = 0;
        gel.update(gel.getGraphics());
    }
    
    public void displayAll(){
	dots1 = gel.getDots();
	dots2 = gel.getDots2();
	for( int i = 0; i < dots1.size(); i++ ){
	    ((ProteinDotSwingVersion)dots1.elementAt( i )).doShowMe();
	}
	if( dots2.size() != 0 ){
	    for( int j = 0; j < dots2.size(); j++ ){
		((ProteinDotSwingVersion)dots2.elementAt( j )).doShowMe();
	    }
	}
//	gel.repaint();
        gel.update(gel.getGraphics());
    }
    
    public void hideExcludeFields(){
	//if( !title.getSelected() && !function.getSelected() ){
	    searchFieldPane.remove( excludesLabel );
	    searchFieldPane.remove( excludeTerm );
	    window.validate();
	    //}
    }
    
    public void actionPerformed( ActionEvent e ){
	searchField = e.getActionCommand();
	
    }

    public void mouseClicked( MouseEvent e ){
	    searchFieldPane.add( excludesLabel );
	    searchFieldPane.add( excludeTerm );
	    window.validate();
    }
    public void mousePressed( MouseEvent e ){}
    public void mouseReleased( MouseEvent e ){}
    public void mouseEntered( MouseEvent e ){}
    public void mouseExited( MouseEvent e ){}

    private class HideExclude implements MouseListener {
	
	private SearchProteinFunction spf;

	public HideExclude( SearchProteinFunction s ){
	    spf = s;
	}
	
	public void mouseClicked( MouseEvent e ){
	    spf.hideExcludeFields();
	}
	public void mousePressed( MouseEvent e ){}
	public void mouseReleased( MouseEvent e ){}
	public void mouseEntered( MouseEvent e ){}
	public void mouseExited( MouseEvent e ){}
    
    }

}







