import java.awt.*;
import java.awt.event.*;

public class SearchProteinFunctionButton extends Button 
    implements MouseListener {
    
    private TextField search;
    private TextField exclude;
    private SearchProteinFunction spf;
    
    public SearchProteinFunctionButton( TextField s, TextField e,
					SearchProteinFunction sf ){
	super( "Search" );
	search = s;
	exclude = e;
	spf = sf;
	this.addMouseListener( this );
    }
    
    public void mouseClicked( MouseEvent e ){
	spf.searchFor( search.getText(), exclude.getText() );
    }
    public void mousePressed( MouseEvent e ){}
    public void mouseReleased( MouseEvent e ){}
    public void mouseEntered( MouseEvent e ){}
    public void mouseExited( MouseEvent e ){}
}
