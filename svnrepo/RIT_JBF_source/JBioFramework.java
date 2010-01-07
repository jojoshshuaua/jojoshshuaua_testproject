/* 
 * JBioFramework
 *
 * @author Jill Zapoticznyj 
 */

import java.awt.Color;
import java.awt.event.*;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.MenuBar;
import java.awt.Menu;
import java.awt.MenuItem;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.awt.*;

public class JBioFramework extends JFrame {
	JBioFrameworkPanel jbfPanel = null;
	public static ConfigHandler config = null;
	MenuItem miOpen = null;
	MenuItem miOptions = null;
	MenuItem miExit = null;
	
	public void readConfigFile( ConfigHandler cfh ) {
		try{
			XMLReader rmReader = XMLReaderFactory.createXMLReader();
			rmReader.setContentHandler(cfh);
			InputSource inputSource = new InputSource(new FileInputStream(
    	    	               "configFile.xml"));
			rmReader.parse(inputSource);
		}catch( Exception ex ){
			System.err.println( ex.getMessage() );
			System.exit( 1 );
		}
	}
	
    public JBioFramework() {
    	
    	config = new ConfigHandler();
    	readConfigFile( config );
    	
        jbfPanel = new JBioFrameworkPanel( config.getConfigArray() );
       // jbfPanel.setPanels( config.getConfigArray() );
		//this.setBackground( Color.BLACK );
		MenuBar mb = new MenuBar();
		Menu m = new Menu( "File" );
		miOpen = new MenuItem( "Open" );
		miOptions = new MenuItem( "Options" );
		
		//set up exit menu item
		miExit = new MenuItem( "Exit" );
		miExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit( 0 );
			}
		});
		
		
		m.add( miOpen );
		m.add( miOptions );
		m.add( miExit );
		mb.add( m );
		
		this.setMenuBar( mb );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(jbfPanel);
        //System.out.println( tabbedPane.getTabCount() );
        //System.out.println( tabbedPane.getBackgroundAt( 1 ) );
        //tabbedPane.getComponentAt(0).setBackground( Color.BLACK );
        //System.out.println( tabbedPane.getBackgroundAt( 0 ) );
        //Uncomment the following line to use scrolling tabs.
        //tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);


    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = JBioFramework.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JBioFramework jbf = new JBioFramework();
        jbf.pack();
        jbf.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
