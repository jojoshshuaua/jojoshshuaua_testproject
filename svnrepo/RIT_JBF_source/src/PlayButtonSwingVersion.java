
/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;

public class PlayButtonSwingVersion extends JButton implements ActionListener {

    Electro2D electro2D;
    boolean playing;
    private boolean iefDrawn;
    private boolean sdsDrawn;
    private static boolean compareFiles;
    String choice;
    boolean isPlaying;

    public PlayButtonSwingVersion(Electro2D e) {

        super("Run");
        addActionListener(this);
        electro2D = e;
        playing = false;
        iefDrawn = false;
        sdsDrawn = false;
        compareFiles = false;
    }

    public boolean getSdsStatus(){
	return sdsDrawn;
    }

    public void resetPlay(){
	playing = false;
        this.setText("Run");
    }

    public void resetIEF(){
	iefDrawn = false;
    }

    public void resetSdsStatus(){
	sdsDrawn = false;
    }

    public static void setCompare(boolean bool){
	compareFiles = bool;
    }

    public void actionPerformed(ActionEvent e) {
        
        if(playing == true) {
	    playing = false;
	    electro2D.stopThread();
	}
	// otherwise determine which parts of the animation need to be drawn
	// and start the appropriate thread.
	else {
	    //get the animation the user wishes to see
            electro2D.clearpH();
	    choice = electro2D.getAnimationChoice();
	    if( electro2D.getSequencesReady()){

		//if the data for the animation needs to be processed,
		//do so
		    if( choice.equals( "IEF" )){
			electro2D.getGel().prepare();
			electro2D.resetBool();
                        if( compareFiles ){
			    electro2D.getGel().prepare2();
			}
		    }

		//if the user selected IEF animation and the image is not
		// already displayed on the screen, perform the IEF
		// animation
		if( choice.equals( "IEF") ){
		    if( iefDrawn == false ){
			electro2D.restartIEF();
			iefDrawn = true;
			electro2D.getGel().resetReLine();
		    }
		}
		//if the user selected SDS-PAGE animation, and the IEF is
		// already drawn, perform the SDS-PAGE animation
		else if( choice.equals( "SDS-PAGE" )){
                    if( iefDrawn == true ){
			electro2D.getGel().clearCanvas();
                        electro2D.getGel().clearIEF();
			electro2D.getGel().resetLocation();
                        electro2D.clearpH();
			playing = true;
			electro2D.restartThread();
			sdsDrawn = true;
		    }
		}
	    }
	}

        // let the user know whether they can pause or play the animation
        if(playing == false) {
	    this.setText("Run");
	} else {
	    this.setText("Pause");
	}

    }
}
