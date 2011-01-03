/**
 * ColorFrame.java works as a key for the protein colors displayed in the
 * gel's final product.
 */

import java.util.HashMap;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

public class ColorFrame {
    
    private static Frame colorFrame;
    private Panel colorPanel;
    private ArrayList labels;
    private HashMap colorkey;
    
    public ColorFrame(){
	colorkey = E2DProtein.getColorGuide();
	
	colorFrame = new Frame( "Color Key" );
	colorFrame.addWindowListener( new WindowAdapter(){
		public void windowClosing( WindowEvent e ){
		    colorFrame.hide();
		}
	    }
				      );
	labels = new ArrayList();
	
	colorPanel = new Panel();
	colorPanel.setLayout( new GridLayout( 0,1 ) );
	
	labels.add( new Label( "dna in Title", Label.CENTER ) );
	((Label)labels.get( 0 )).setBackground( (Color)colorkey.get( "dna in Title" ) );
	colorPanel.add( (Label)labels.get(0) );
	
	labels.add( new Label( "ribosomal in Title", Label.CENTER ) );
	((Label)labels.get( labels.size() - 1 )).setBackground( (Color)colorkey.get( "ribosomal in Title" ));
	colorPanel.add( (Label)labels.get( labels.size() - 1 ) );
	
	labels.add( new Label( "Enzyme EC in Function", Label.CENTER ) );
	((Label)labels.get( labels.size() - 1 )).setBackground( (Color)colorkey.get( "Enzyme EC in Function" ) );
	colorPanel.add( (Label)labels.get( labels.size() - 1 ) );

	labels.add( new Label( "hypothetical protein", Label.CENTER ) );
	((Label)labels.get(labels.size() - 1)).setBackground( (Color)colorkey.get( "hypothetical protein" ) );
	colorPanel.add( (Label)labels.get(labels.size() - 1) );
	
	labels.add( new Label( "transport protein in Function", 
			       Label.CENTER ) );
	((Label)labels.get(labels.size() - 1)).setBackground( (Color)colorkey.get( "transport protein in Function" ) );
	((Label)labels.get(labels.size() - 1)).setForeground( Color.WHITE );
	colorPanel.add( (Label)labels.get(labels.size() - 1) );

	labels.add( new Label( "receptor in Function", Label.CENTER ) );
	((Label)labels.get(labels.size() - 1)).setBackground( (Color)colorkey.get( "receptor in Function" ) );
	colorPanel.add( (Label)labels.get(labels.size() - 1) );

	labels.add( new Label( "transduction in Function", Label.CENTER ) );
	((Label)labels.get(labels.size() - 1)).setBackground( (Color)colorkey.get( "transduction in Function" ) );
	colorPanel.add( (Label)labels.get(labels.size() - 1) );
	
	colorFrame.setBounds( 0, 0, 400, 300 );
	colorPanel.setBounds( 0, 0, 400, 300 );
	colorFrame.add( colorPanel );
    }

    public void showKey(){
	colorFrame.pack();
	colorFrame.show();
    }
}