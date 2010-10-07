/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

        super("Play");
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
        this.setText("Play");
    }

    public void resetIEF(){
	iefDrawn = false;
    }

    public void resetSdsStatus(){
	sdsDrawn = false;
    }

    public static void setCompare(){
	compareFiles = true;
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
		//if((( electro2D.getGel().getDots().size() == 0 ) ||
		//  ( electro2D.getBool() ))){
		    if( choice.equals( "IEF" )){
			electro2D.getGel().prepare();
			electro2D.resetBool();
			if( compareFiles ){
			    electro2D.getGel().prepare2();
			}
		    }
		    //}

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
                    electro2D.getGel().clearIEF();
                    repaint();
                    System.out.println("PlayButtonSwingVersion 96: iefDrawn = " + iefDrawn);
		    if( iefDrawn == true ){
			/*electro2D.getGel().resetWriteRanges();*/
			electro2D.getGel().clearCanvas();
			electro2D.getGel().resetLocation();
			playing = true;
			electro2D.restartThread();
			repaint();
			sdsDrawn = true;
                        /*electro2D.getGel().resetDrawMW();*/
			//electro2D.getGel().resetReLine();

		    }
		}
	    }
	}

        // let the user know whether they can pause or play the animation
        if(playing == false) {
	    this.setText("Play");
	} else {
	    this.setText("Pause");
	}

    }
}
