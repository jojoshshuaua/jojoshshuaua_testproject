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

public class IDD_DIALOG1
{
	Container    m_Parent       = null;
	boolean      m_fInitialized = false;
	DialogLayout m_Layout;

	// Control definitions
	//--------------------------------------------------------------------------
	Label         IDC_STATIC1;
	TextField     IDC_SOLVENTA;
	Label         IDC_STATIC2;
	Label         IDC_STATIC3;
	TextField     IDC_SOLVENTB;
	Label         IDC_STATIC4;
	Label         IDC_STATIC5;
	CheckboxGroup group1;
	Checkbox      IDC_POS;
	Checkbox      IDC_NEG;
	Label         IDC_STATIC6;
	CheckboxGroup group2;
	Checkbox      IDC_BUFFER1;
	Checkbox      IDC_BUFFER2;
	Checkbox      IDC_BUFFER3;
	List          IDC_PROTEINS;
	Button        IDC_REMOVE;
	Button        IDC_ADD;
	TextField     IDC_AMOUNT;
	Label         IDC_STATIC7;
	Button        IDC_START;
	Button        IDC_PAUSE;
	Button        IDC_STOP;
	Button        IDC_UPDATE;
	Button        IDC_RESET;
	List          IDC_SELECTPROTEIN;


	// Constructor
	//--------------------------------------------------------------------------
	public IDD_DIALOG1 (Container parent)
	{
		m_Parent = parent;
	}

	// Initialization.
	//--------------------------------------------------------------------------
	public boolean CreateControls()
	{
		// Can only init controls once
		//----------------------------------------------------------------------
		if (m_fInitialized || m_Parent == null)
			return false;

		// Parent must be a derivation of the Container class
		//----------------------------------------------------------------------
		if (!(m_Parent instanceof Container))
			return false;

		// Since there is no way to know if a given font is supported from
		// platform to platform, we only change the size of the font, and not
		// type face name.  And, we only change the font if the dialog resource
		// specified a font.
		//----------------------------------------------------------------------
		Font OldFnt = m_Parent.getFont();
		
		if (OldFnt != null)
		{
			Font NewFnt = new Font(OldFnt.getName(), OldFnt.getStyle(), 8);

			m_Parent.setFont(NewFnt);
		}

		// All position and sizes are in Dialog Units, so, we use the
		// DialogLayout manager.
		//----------------------------------------------------------------------
		m_Layout = new DialogLayout(m_Parent, 190, 247);
		m_Parent.setLayout(m_Layout);
		System.out.println( m_Layout );
		System.out.println( m_Parent );
		//m_Parent.addNotify();

		Dimension size   = m_Layout.getDialogSize();
		Insets    insets = m_Parent.insets();
		
		m_Parent.resize(insets.left + size.width  + insets.right,
                        insets.top  + size.height + insets.bottom);

		// Control creation
		//----------------------------------------------------------------------
		IDC_STATIC1 = new Label ("Solvent A Concentration:", Label.CENTER);
		m_Parent.add(IDC_STATIC1);
		m_Layout.setShape(IDC_STATIC1, 11, 28, 80, 8);

		IDC_SOLVENTA = new ConcenTextField ("");
		m_Parent.add(IDC_SOLVENTA);
		m_Layout.setShape(IDC_SOLVENTA, 97, 25, 40, 14);

		IDC_STATIC2 = new Label ("M NaCl", Label.LEFT);
		m_Parent.add(IDC_STATIC2);
		m_Layout.setShape(IDC_STATIC2, 146, 28, 28, 8);

		IDC_STATIC3 = new Label ("Solvent B Concentration:", Label.CENTER);
		m_Parent.add(IDC_STATIC3);
		m_Layout.setShape(IDC_STATIC3, 11, 49, 80, 8);

		IDC_SOLVENTB = new ConcenTextField ("");
		m_Parent.add(IDC_SOLVENTB);
		m_Layout.setShape(IDC_SOLVENTB, 97, 46, 40, 14);

		IDC_STATIC4 = new Label ("M NaCl", Label.LEFT);
		m_Parent.add(IDC_STATIC4);
		m_Layout.setShape(IDC_STATIC4, 146, 49, 28, 8);

		IDC_STATIC5 = new Label ("Resin:", Label.LEFT);
		m_Parent.add(IDC_STATIC5);
		m_Layout.setShape(IDC_STATIC5, 19, 64, 30, 8);

		group1 = new CheckboxGroup ();
		IDC_POS = new Checkbox ("DEAE - Sephadex", group1, false);
		m_Parent.add(IDC_POS);
		m_Layout.setShape(IDC_POS, 7, 74, 72, 10);

		IDC_NEG = new Checkbox ("CM - Sephadex", group1, false);
		m_Parent.add(IDC_NEG);
		m_Layout.setShape(IDC_NEG, 7, 84, 65, 10);

		IDC_STATIC6 = new Label ("Buffer:", Label.LEFT);
		m_Parent.add(IDC_STATIC6);
		m_Layout.setShape(IDC_STATIC6, 94, 64, 28, 8);

		group2 = new CheckboxGroup ();
		IDC_BUFFER1 = new Checkbox ("Sodium Acetate, pH 4.8", group2, false);
		m_Parent.add(IDC_BUFFER1);
		m_Layout.setShape(IDC_BUFFER1, 82, 74, 99, 10);

		IDC_BUFFER2 = new Checkbox ("Sodium Phosphate, pH 7.2", group2, false);
		m_Parent.add(IDC_BUFFER2);
		m_Layout.setShape(IDC_BUFFER2, 82, 84, 99, 10);

		IDC_BUFFER3 = new Checkbox ("Tris HCl, pH 8.0", group2, false);
		m_Parent.add(IDC_BUFFER3);
		m_Layout.setShape(IDC_BUFFER3, 82, 94, 99, 10);

		IDC_PROTEINS = new List (1, false);
		m_Parent.add(IDC_PROTEINS);
		m_Layout.setShape(IDC_PROTEINS, 7, 107, 174, 53);

		IDC_REMOVE = new Button ("Remove Protein");
		m_Parent.add(IDC_REMOVE);
		m_Layout.setShape(IDC_REMOVE, 7, 165, 57, 14);

		IDC_ADD = new Button ("Add Protein");
		m_Parent.add(IDC_ADD);
		m_Layout.setShape(IDC_ADD, 7, 182, 58, 14);

		IDC_AMOUNT = new AmountTextField ("");
		m_Parent.add(IDC_AMOUNT);
		m_Layout.setShape(IDC_AMOUNT, 79, 182, 40, 14);

		IDC_STATIC7 = new Label ("mg", Label.LEFT);
		m_Parent.add(IDC_STATIC7);
		m_Layout.setShape(IDC_STATIC7, 129, 185, 10, 8);

		IDC_START = new Button ("Start");
		m_Parent.add(IDC_START);
		m_Layout.setShape(IDC_START, 7, 225, 50, 14);

		IDC_PAUSE = new Button ("Pause");
		m_Parent.add(IDC_PAUSE);
		m_Layout.setShape(IDC_PAUSE, 69, 225, 50, 14);

		IDC_STOP = new Button ("Stop");
		m_Parent.add(IDC_STOP);
		m_Layout.setShape(IDC_STOP, 131, 225, 50, 14);

		IDC_UPDATE = new Button ("Load Experiment");
		m_Parent.add(IDC_UPDATE);
		m_Layout.setShape(IDC_UPDATE, 30, 6, 60, 14);

		IDC_RESET = new Button ("Reset Settings");
		m_Parent.add(IDC_RESET);
		m_Layout.setShape(IDC_RESET, 102, 6, 60, 14);

		IDC_SELECTPROTEIN = new List (1, false);
		m_Parent.add(IDC_SELECTPROTEIN);
		m_Layout.setShape(IDC_SELECTPROTEIN, 7, 198, 174, 23);

		m_fInitialized = true;
		return true;
	}
}
