
/**
 *
 * @author Amanda Fisher
 */
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class HelpButtonSwingVersion extends JButton implements ActionListener {

    public HelpButtonSwingVersion () {

        super("Help");
        addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {

	//create a URL object
        //catch and display any errors that occurred while assigning
        //information to the URL
	// if no errors occurred, open a new window and display the help page
	File f = new File( "HTML Files" + File.separator + "Help" + File.separator + "help.html" );
	try{
	    BrowserLauncher.openURL( f.toURL().toString() );
	} catch(IOException i){
            System.err.println( i.getMessage());
        }
        
    }
}
