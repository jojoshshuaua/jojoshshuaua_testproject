//------------------------------------------------------------------------------
// IDD_DIALOG1.java:
//		Implementation for container control initialization class IDD_DIALOG1
//
// WARNING: Do not modify this file.  This file is recreated each time its
//          associated .rct/.res file is sent through the Java Resource Wizard!
//
// This class can be use to create controls within any container, however, the
// following describes how to use this class with an Applet.  For addtional
// information on using Java Resource Wizard generated classes, refer to the
// Visual J++ 1.1 documention.
//
// 1) Import this class in the .java file for the Applet that will use it:
//
//      import IDD_DIALOG1;
//
// 2) Create an instance of this class in your Applet's 'init' member, and call
//    CreateControls() through this object:
//
//      IDD_DIALOG1 ctrls = new IDD_DIALOG1 (this);
//      ctrls.CreateControls();
//
// 3) To process events generated from user action on these controls, implement
//    the 'handleEvent' member for your applet:
//
//      public boolean handleEvent (Event evt)
//      {
//
//      }
//
//------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class IDD_DIALOG1
{
	JPanel    m_Parent       = null;
	boolean      m_fInitialized = false;
	

	// Control definitions
	//--------------------------------------------------------------------------
	JLabel         IDC_STATIC1;
	JTextField     IDC_SOLVENTA;
	JLabel         IDC_STATIC2;
	JLabel         IDC_STATIC3;
	JTextField     IDC_SOLVENTB;
	JLabel         IDC_STATIC4;
	JLabel         IDC_STATIC5;
	ButtonGroup group1;
	JRadioButton      IDC_POS;
	JRadioButton      IDC_NEG;
	JLabel         IDC_STATIC6;
	ButtonGroup group2;
	JRadioButton      IDC_BUFFER1;
	JRadioButton      IDC_BUFFER2;
	JRadioButton      IDC_BUFFER3;
	JList          IDC_PROTEINS;
	JButton        IDC_REMOVE;
	JButton        IDC_ADD;
	JTextField     IDC_AMOUNT;
	JLabel         IDC_STATIC7;
	JButton        IDC_START;
	JButton        IDC_PAUSE;
	JButton        IDC_STOP;
	JButton        IDC_UPDATE;
	JButton        IDC_RESET;
	JComboBox         IDC_SELECTPROTEIN;


	// Constructor
	//--------------------------------------------------------------------------
	public IDD_DIALOG1 (JPanel parent)
	{
		m_Parent = parent;
	}

	// Initialization.
	//--------------------------------------------------------------------------
	public boolean CreateControls( ActionListener listener )
	{
		// Can only init controls once
		//----------------------------------------------------------------------
		if (m_fInitialized || m_Parent == null)
			return false;

		m_Parent.setLayout( new GridLayout( 0, 2 ) );
		

	
		
	

		// Control creation
		//----------------------------------------------------------------------
		IDC_STATIC1 = new JLabel ("Solvent A Concentration:");
		m_Parent.add(IDC_STATIC1);
		

		IDC_SOLVENTA = new ConcenTextField ("");
		m_Parent.add(IDC_SOLVENTA);
		

		IDC_STATIC2 = new JLabel ("M NaCl", Label.LEFT);
		m_Parent.add(IDC_STATIC2);
		

		IDC_STATIC3 = new JLabel ("Solvent B Concentration:");
		m_Parent.add(IDC_STATIC3);
		

		IDC_SOLVENTB = new ConcenTextField ("");
		m_Parent.add(IDC_SOLVENTB);
		

		IDC_STATIC4 = new JLabel ("M NaCl");
		m_Parent.add(IDC_STATIC4);
		

		IDC_STATIC5 = new JLabel ("Resin:");
		m_Parent.add(IDC_STATIC5);
		

		group1 = new ButtonGroup ();
		IDC_POS = new JRadioButton ("DEAE - Sephadex", false);
        group1.add(IDC_POS);

		m_Parent.add(IDC_POS);
		

		IDC_NEG = new JRadioButton ("CM - Sephadex", false);
        group1.add(IDC_NEG);

		m_Parent.add(IDC_NEG);
		

		IDC_STATIC6 = new JLabel ("Buffer:");
		m_Parent.add(IDC_STATIC6);
		

		group2 = new ButtonGroup ();
		IDC_BUFFER1 = new  JRadioButton("Sodium Acetate, pH 4.8", false);
        group2.add( IDC_BUFFER1 );
		m_Parent.add(IDC_BUFFER1);
		

		IDC_BUFFER2 = new JRadioButton ("Sodium Phosphate, pH 7.2", false);
        group2.add( IDC_BUFFER2 );
		m_Parent.add(IDC_BUFFER2);
		

		IDC_BUFFER3 = new JRadioButton ("Tris HCl, pH 8.0", false);
        group2.add( IDC_BUFFER3 );
		m_Parent.add(IDC_BUFFER3);
		

		IDC_PROTEINS = new JList ();
		m_Parent.add(IDC_PROTEINS);
		

		IDC_REMOVE = new JButton ("Remove Protein");
		IDC_REMOVE.addActionListener( listener );
		m_Parent.add(IDC_REMOVE);
		

		IDC_ADD = new JButton ("Add Protein");
		IDC_ADD.addActionListener( listener );
		m_Parent.add(IDC_ADD);
		

		IDC_AMOUNT = new AmountTextField ("");
		m_Parent.add(IDC_AMOUNT);
		

		IDC_STATIC7 = new JLabel ("mg");
		m_Parent.add(IDC_STATIC7);
	

		IDC_START = new JButton ("Start");
		IDC_START.addActionListener( listener );
		m_Parent.add(IDC_START);
		

		IDC_PAUSE = new JButton ("Pause");
		IDC_PAUSE.addActionListener( listener );
		m_Parent.add(IDC_PAUSE);
		

		IDC_STOP = new JButton ("Stop");
		IDC_STOP.addActionListener( listener );
		m_Parent.add(IDC_STOP);
		
		IDC_UPDATE = new JButton ("Load Experiment");
		IDC_UPDATE.addActionListener( listener );
		m_Parent.add(IDC_UPDATE);
		

		IDC_RESET = new JButton ("Reset Settings");
		IDC_RESET.addActionListener( listener );
		m_Parent.add(IDC_RESET);
		

		IDC_SELECTPROTEIN = new JComboBox();
		m_Parent.add(IDC_SELECTPROTEIN);
		

		m_fInitialized = true;
		return true;
	}
}
