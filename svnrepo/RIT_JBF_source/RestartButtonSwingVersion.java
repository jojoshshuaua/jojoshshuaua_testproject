
/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;

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

    }

}
