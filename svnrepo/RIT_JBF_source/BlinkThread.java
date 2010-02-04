import java.awt.*;

public class BlinkThread extends Thread {

    private ProteinDot theDot;
    private GelCanvasSwingVersion theGel;
    
    public BlinkThread( ProteinDot p, GelCanvasSwingVersion g ){
	theGel = g;
	theDot = p;
    }

    public void run(){
	
	while( !GelCanvasSwingVersion.getBlink() ){
	}

	while( GelCanvasSwingVersion.getBlink() && !DotThread.getDotState() ){
	    if( theDot.getColor() == Color.RED ){
		theDot.changeColor( Color.GREEN );
	    }
	    else{
		theDot.changeColor( Color.RED );
	    }
	    theGel.repaint();
	    try{
		sleep( (long)1000 );
	    }catch( Exception e ){}
	}
	
	theDot.changeColor( Color.GREEN );

    }
}
