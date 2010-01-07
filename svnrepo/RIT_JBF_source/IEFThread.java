/**
 * IEFThread is the thread controlling the IEF phase of the 2DGE
 * animation.
 *
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 */


public class IEFThread extends Thread{
    
    private boolean go = false; //determines whether or not the user has
                                //selected the IEF animation
    private GelCanvas gel;      //a reference to GelCanvas
    private Electro2D electro2D;
    
    /**
     * Constructor
     *
     * @param g a reference to the GelCanvas
     */

    public IEFThread( GelCanvas g, Electro2D e ){
	//assigns gel the value that was passed as a parameter to the method
	gel = g;
	electro2D = e;
    }

    /**
     * This method is called when the play button is pressed for
     * the IEF animation.  Allows the thread to draw the IEFProteins
     * to the GelCanvas' image
     */
    
    public void setIEF(){
	go = true;
    }

    /**
     * This method is called after the IEFProteins were drawn to the
     * image in GelCanvas.
     */

    public void resetIEF(){
	go = false;
    }

    /**
     * The run method for the thread that draws the IEFProteins
     * to the GelCanvas image.  If the proteins have not yet been
     * drawn, it calls the drawIEF method in GelCanvas to draw
     * the IEFProteins.
     */

    public void run(){
	
	// if the user has selected IEF animation
	if( go == true ){
	    
	    //get the current and final widths of the IEFProteins
	    double width = IEFProtein.returnTempWidth();
	    double finalWidth = IEFProtein.returnWidth();

	    
	    if( IEFProtein.returnTempWidth() >= finalWidth ){
		gel.drawIEF();
	    }

	    //while the current width and the values of the background colors
	    // are less than their final values, call the animateIEF method
	    while( IEFProtein.returnTempWidth() <= finalWidth && GelCanvas.getBlue() >= 0 &&
		   GelCanvas.getGreen() >= 0 && GelCanvas.getRed() >=
		   0 ){
		gel.animateIEF();
		try{
		    sleep( (long)250 );
		}catch( Exception e ){
		    System.err.println( "the error was " + e.getMessage() );
		}
	    }
	    //display the pH markers along the gel
	    gel.setreLine();
	    gel.repaint();
	    //change the value selected in the animationChooser
	    electro2D.setSDS();
	}
    }
} //IEFThread
