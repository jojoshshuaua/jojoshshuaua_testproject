import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.Vector;

public class PeptideGenUI extends Frame implements ActionListener {
	private String sequence = "";
	private String proteinName = "";
	private JPanel mainPanel = null;
	private PeptideGen pGen = new PeptideGen();
	private Choice enzymes = null;
	private JTable table = null;
	private JScrollPane jScroll = null;
	private JButton genButton = null;
	private String[][] values;
	private PeptideCSVButton peptideBtn = null;

	public PeptideGenUI( String seq, String nm ){
		proteinName = nm;
		sequence = seq;
		table = null;
		jScroll = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setTitle("Peptide Generator for " + proteinName);
		jScroll.setBounds(10, 110, 500, 195);
		//jScroll.add(table);
	
		mainPanel = new JPanel();                  //init panel
		mainPanel.setLayout(null);                //abs. positioning
		mainPanel.setBackground(Color.BLACK);
		this.addWindowListener(new WindowAdapter() { //allow user to close win.
			public void windowClosing( WindowEvent e ) {
		    	dispose();
			}
	    });
	    
	    enzymes = new Choice();
	    enzymes.setLocation( 10, 20 );
	    enzymes.add("1. Trypsin" ); 
		enzymes.add("2. Trypsin (C-term to K/R, even before P)"); 
		enzymes.add("3. Trypsin (higher specificity)");
		enzymes.add("4. Lys C");
		enzymes.add("5. CNBr"); 
		enzymes.add("6. Arg C");
		enzymes.add("7. Asp N"); 
		enzymes.add("8. Asp N + N-terminal Glu");
		enzymes.add("9. Glu C (bicarbonate)");
		enzymes.add("10. Glu C (phosphate)");
		enzymes.add("11. Chymotrypsin (C-term to F/Y/W/M/L, not before P, not after Y if P is N-term to Y)");
		enzymes.add("12. Chymotrypsin (C-term to F/Y/W, not before P, not after Y if P is N-term to Y)");
		enzymes.add("13. Trypsin/Chymotrypsin (C-term to K/R/F/Y/W, not before P, not after Y if P is N-term to Y");
		enzymes.add("14. Pepsin (pH 1.3)");
		enzymes.add("15. Pepsin (pH > 2)"); 
		enzymes.add("16. Proteinase K");
		
		genButton = new JButton( "Generate Peptides" );
		genButton.addActionListener( this );
		genButton.setBounds( 10, 60, 170, 20 );
		
		peptideBtn = new PeptideCSVButton( proteinName );
		peptideBtn.setBounds( 10, 335, 118, 20 ); 
		//mainPanel.add( jScroll );
		
			mainPanel.add( jScroll );
		mainPanel.add( enzymes );
		mainPanel.add( genButton );
		mainPanel.add( peptideBtn );
		this.add( mainPanel );
		this.setBounds(0, 0, 600, 400);
		
		this.setVisible( true );
		//pack();
		pGen = new PeptideGen();
	}
	
	/**
     * This is the action listener method for the loadButton object
     *
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
		// change the cursor image
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		//try to read the contents of the file
		
		ArrayList arr = pGen.generateArrayList( enzymes.getSelectedIndex() + 1, sequence );
		if( arr == null ){
			System.out.println( "Invalid index value: " + enzymes.getSelectedIndex() );
		}
		else{
			System.out.println( arr.size() );
			values = new String[arr.size()][2];
			String[] tmp;

			for( int i = 0; i < arr.size(); i++ ){
				System.out.println( (String) arr.get(i) );
				tmp = new String[2];
				tmp[0] = (String)arr.get(i);
				tmp[1] = Double.toString(PeptideGen.getMW((String)arr.get(i)));
				values[i] = tmp;
			}
			System.out.println( values.length );
			String[] colNames = new String[2];
			colNames[0] = "Peptide Sequence";
			colNames[1] = "Molecular Weight";
			table = new JTable( values, colNames );
			table.setPreferredScrollableViewportSize(new Dimension(500, 190));
			jScroll.setViewportView( table );
			
			peptideBtn.setValues( values );
			peptideBtn.setIndex( enzymes.getSelectedIndex() + 1 );
		}
		//System.out.priln( "got here" );
		// set the cursor image back to normal
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		// display the protein titles from the file
		mainPanel.repaint();
    }
}