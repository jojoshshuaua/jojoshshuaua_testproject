/* 
 * TabbedPaneDemo.java is a 1.4 example that requires one additional file:
 *   images/middle.gif. 
 */

import java.awt.Container;

import java.util.HashMap;
import java.awt.Color;
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


public class JBioFrameworkPanel extends JPanel {
	JTabbedPane tabbedPane = null;
	
    public JBioFrameworkPanel(HashMap[] config) {
        super(new GridLayout(1, 1));
		tabbedPane = new JTabbedPane();
		
		setPanels( config );
        //Add the tabbed pane to this panel.
        add( tabbedPane );
    }

	public void setPanels( HashMap[] config ){
		
		ClassLoader cld = ClassLoader.getSystemClassLoader();
		Container tmpClass = null;
		Class tmpCls = null;
		String tmpTitle = "";
		String tmpClassName = "";
		HashMap tmpConfigHash = null;
		int nameIndex = -1;
		int classIndex = -1;
		int dimIndex = -1;
		int bgColorIndex = -1;
		int commandLineIndex = -1;
		
		
		///TODO: create a mapping of config settings (ex: name="2D Gel Electrophoresis")
		///This will allow for better control of settings like preferred size
		///and parameter passing for instantiation of classes.
		for( int i = 0; i < config.length; i++ ){
			tmpConfigHash = config[i];
			tmpClassName = (String)tmpConfigHash.get( "class" );
			tmpTitle = (String)tmpConfigHash.get( "name" );
			System.out.println( tmpClassName + "  " + tmpTitle );
			try{
                            tmpCls = cld.loadClass( tmpClassName );
			    tmpClass = (Container)(tmpCls.newInstance());
			}catch( Exception ex ){
				System.err.println( ex.getMessage() );
				System.exit( 1 );
			}   
			//set colors 
        	/*if( tmpConfigHash.containsKey( "bgcolor" ) ){
        		String bgcolor = (String)tmpConfigHash.get( "bgcolor" );
        		if( bgcolor.equals( "black" ) ){
        			tmpClass.setBackground( Color.BLACK );
        		}
        		else if( bgcolor.equals( "blue" ) ){
        			tmpClass.setBackground( Color.BLUE );
        		}
        		else if( bgcolor.equals( "cyan" ) ){
        			tmpClass.setBackground( Color.CYAN );
        		}
        		else if( bgcolor.equals( "dark_gray" ) ){
        			tmpClass.setBackground( Color.DARK_GRAY );
        		}
        		else if( bgcolor.equals( "gray" ) ){
        			tmpClass.setBackground( Color.GRAY );
        		}
        		else if( bgcolor.equals( "green" ) ){
        			tmpClass.setBackground( Color.GREEN );
        		}
        		else if( bgcolor.equals( "light_gray" ) ){
        			tmpClass.setBackground( Color.LIGHT_GRAY );
        		}
        		else if( bgcolor.equals( "magenta" ) ){
        			tmpClass.setBackground( Color.MAGENTA );
        		}
        		else if( bgcolor.equals( "orange" ) ){
        			tmpClass.setBackground( Color.ORANGE );
        		}
        		else if( bgcolor.equals( "pink" ) ){
        			tmpClass.setBackground( Color.PINK );
        		}
        		else if( bgcolor.equals( "red" ) ){
        			tmpClass.setBackground( Color.RED );
        		}
        		else if( bgcolor.equals( "white" ) ){
        			tmpClass.setBackground( Color.WHITE );
        		}
        		else if( bgcolor.equals( "yellow" ) ){
        			tmpClass.setBackground( Color.YELLOW );
        		}	
        	}*/
        	
        	if( tmpConfigHash.containsKey( "width" ) ){
        		int width = Integer.parseInt( (String)tmpConfigHash.get( "width" ) );
        		int height = Integer.parseInt( (String)tmpConfigHash.get( "height" ) );
        		tmpClass.setPreferredSize( new Dimension( width, height ) );
        		//tmpClass.setMinimumSize( new Dimension( width, height ) );
        		//tmpClass.setBounds( new java.awt.Rectangle( new Dimension( width, height ) ) );
        	}
        	
        	tabbedPane.addTab( tmpTitle, tmpClass );
                          
        	//tabbedPane.setMnemonicAt( i, KeyEvent.VK_1 );

		}
	
		add(tabbedPane);
	
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
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(false);

        //Create and set up the window.
        JFrame frame = new JFrame("JBioFramework");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        //Create and set up the content pane.
        JComponent newContentPane = new JBioFrameworkPanel(new HashMap[0]);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.getContentPane().add(new JBioFrameworkPanel( new HashMap[0]),
                                 BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
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
