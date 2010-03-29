import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class ImageZoom extends Frame implements MouseListener{
    
    //private Frame canvasFrame;
    private Panel canvasPanel;
    private Canvas imageCanvas;
    private int startX;
    private int startY;
    private int stopX;
    private int stopY;
    private Vector bigDot;
    private static final int size = 5;
    private ImageProducer prod;
    private Image filtered;
    private Electro2D electro2D;
    
    private MediaTracker tracker;
    
    public ImageZoom( Electro2D e2d, ImageProducer gelSource, int x1, int y1, int x2, int y2,
		      Vector dot ){
	electro2D = e2d;
	bigDot = dot;
	startX = x1;
	startY = y1;
	stopX = x2;
	stopY = y2;

	this.addMouseListener( this );
	
	ImageProducer prod1 = new FilteredImageSource( gelSource, new CropImageFilter(
						     startX, startY, ( stopX - startX ), ( stopY - startY ) )/* new ReplicateScaleFilter( (e2d.getGel()).getWidth(), (e2d.getGel()).getHeight()) */);
	filtered = createImage( prod1 );
	prod = new FilteredImageSource( filtered.getSource(), 
						      new ReplicateScaleFilter(
									       5*( stopX - startX ), 5*(stopY - startY ) ) );
	filtered = createImage( prod );
	
	//filtered = filtered.getScaledInstance( size*( stopX - startX ), 
	//size*( stopY - startY ), 
	//  Image.SCALE_SMOOTH );
    
	this.setTitle( "Gel Zoom" );
	this.setBounds( 0, 0,5*( stopX - startX ), 5*( stopY - startY ) );
	this.addWindowListener( new WindowAdapter(){
		public void windowClosing(WindowEvent e ){
		    hide();
		}
	    }
				       );
	this.setLayout( null );
	canvasPanel = new Panel();
	canvasPanel.setBounds( 0, 0, ( stopX - startX ), ( stopY - startY ));
	canvasPanel.setLayout( null );
	
	//imageCanvas = new Canvas();
	//canvasPanel.add( imageCanvas );
	//this.add(  );
	
	
	//this.setBounds( 0, 0, canvasPanel.getWidth(), canvasPanel.getHeight() );
	//imageCanvas.setBounds( 0, 0, canvasPanel.getWidth(), canvasPanel.getHeight() );
	tracker = new MediaTracker( this );
	//filtered = e2d.createImage( prod );
	tracker.addImage( filtered, 0 );
	
	try{
	    tracker.waitForID( 0 );
	}catch( InterruptedException ex ){
	    System.err.println( "Error loading zoomed image" );
	}
    //filtered = filtered.getScaledInstance( ( stopX - startX ), 
    //				       size*( stopY - startY ), 
    //				       Image.SCALE_SMOOTH );
	//	try{
	//  tracker.waitForID(0);
	//}catch(InterruptedException exc ){}
	

	this.show();
    }
    
    public void paint( Graphics g ){
	
	//this.setBounds( 0, 0, size*( stopX - startX ), size*( stopY - startY ) );
	
	g.drawImage( filtered, 0, 0, imageCanvas );
	//System.out.println( bigDot );
    }

    public void mouseClicked( MouseEvent e ){

	double x = e.getX() / 5;
	double y = e.getY() / 5;
	ProteinDotSwingVersion thedot = null;
	double diameter = ProteinDotSwingVersion.getDiameter();
	for( int i = 0; i < bigDot.size(); i++ ){
	    thedot = (ProteinDotSwingVersion)bigDot.get( i );
	    if( ( thedot.returnX() + diameter >= startX + x ) && 
		( thedot.returnX() <= startX + x ) ){
		if( ( thedot.returnY() + diameter >= startY + y ) && 
		    ( thedot.returnY() <= startY + y ) ){
		    ProteinFrame pf = new ProteinFrame( electro2D, 
							thedot.getPro().getID(), 0 );
		    pf.setResizable( false );
		    pf.show();
		}
	    }
	}
    }
    
    public void mousePressed( MouseEvent e ){}
    public void mouseEntered( MouseEvent e ){}
    public void mouseReleased( MouseEvent e ){}
    public void mouseExited( MouseEvent e ){}
}








