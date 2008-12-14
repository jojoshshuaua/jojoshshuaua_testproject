import java.awt.*;
import java.awt.event.*;

public class SearchProteinResetButton extends Button 
    implements MouseListener {
    
    private SearchProteinFunction spf;
    
    public SearchProteinResetButton( SearchProteinFunction sf ){
	super( "Reset" );
	spf = sf;
	this.addMouseListener( this );
    }
    
    public void mouseClicked( MouseEvent e ){
	spf.displayAll();
    }
    public void mousePressed( MouseEvent e ){}
    public void mouseReleased( MouseEvent e ){}
    public void mouseEntered( MouseEvent e ){}
    public void mouseExited( MouseEvent e ){}
}
