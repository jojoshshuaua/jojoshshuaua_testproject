
/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class RestartButtonSwingVersion extends JButton implements ActionListener {

    Electro2D electro2D;

    public RestartButtonSwingVersion(Electro2D e) {
        super("Restart");
        addActionListener(this);
        electro2D = e;
    }

    public void actionPerformed(ActionEvent e) {
        GelCanvasSwingVersion g = electro2D.getGel();
	g.clearIEF();
	g.resetLocation();
	g.resetRanges();
	g.clearCanvas();
	electro2D.resetIEF();
	IEFProteinSwingVersion.resetProtein();

	if( ProteinDotSwingVersion.getShow() ){
	    ProteinDotSwingVersion.setShow();
	    electro2D.stopThread();
	}
	g.restartCanvas();

	electro2D.resetPlay();
	electro2D.resetSdsStatus();
	electro2D.setBool();
	electro2D.clearpH();
	electro2D.setIEF();
	GelCanvasSwingVersion.setRed();
	GelCanvasSwingVersion.setGreen();
	GelCanvasSwingVersion.setBlue();
	IEFProteinSwingVersion.resetTempWidth();
        PlayButtonSwingVersion.setCompare(false);
        electro2D.resetBothProteinLists();
        electro2D.setSequences(new Vector());
        electro2D.setSequenceTitles(new Vector());
        electro2D.setMolecularWeights(new Vector());
        electro2D.setPiValues(new Vector());
        electro2D.setFunctionValues(new Vector());
        electro2D.setSequences2(new Vector());
        electro2D.setSequenceTitles2(new Vector());
        electro2D.setMolecularWeights2(new Vector());
        electro2D.setPiValues2(new Vector());
        electro2D.setFunctionValues2(new Vector());
        electro2D.refreshProteinList();
        electro2D.refreshProteinList2();

    }

}
