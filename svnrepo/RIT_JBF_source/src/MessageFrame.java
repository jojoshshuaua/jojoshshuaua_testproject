/**
 * MessageFrame.java
 *
 * This class encapsulates all the functionality required to pop up a frame
 * and display a message to the user.
 *
 * @author Adam Bazinet
 * @author Jill Zapoticznyj
 */

import java.awt.*;
import java.awt.event.*;

public class MessageFrame extends Frame {

 /** variables for the file reading pop-up frame **/

    private Panel thePanel;              //panel to add components to
    private Label theLabel;              //label to display message on
    private String theMessage = "";      //message to display to user
    
    //construct simple GUI
    public MessageFrame() {
	
	//set the title to Alert
	setTitle("Alert");
	thePanel = new Panel();          //init components
	//position the message label to be centered in the frame
	theLabel = new Label(theMessage, Label.CENTER);
	
	// allow the user to close the frame
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    dispose();
		}
	    });

	// set the sizes of the frame and its components
	this.setBounds(20,140,600,100);
	thePanel.setBounds(0,0,600,100);
	theLabel.setBounds(0,0,600,100);
	
	//add the components
	this.add(thePanel);
	thePanel.add(theLabel);
    }
    //set the message
    public void setMessage(String message) {
	theMessage = message;
	theLabel.setText(theMessage);
    }
}//MessageFrame



