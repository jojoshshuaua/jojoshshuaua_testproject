
/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class AboutButtonSwingVersion extends JButton implements ActionListener {
    Electro2D electro2D;

    public AboutButtonSwingVersion () {
        super("About");
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        // open a new window and display the about page
	File f = new File( "HTML Files" + File.separator + "about.html" );
	try{
            BrowserLauncher.openURL(f.toURL().toString());
	}catch(IOException i){
            System.err.println(i.getMessage());
            i.printStackTrace();
        }
    }

}