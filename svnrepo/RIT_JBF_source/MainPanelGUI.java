/*
 * This class extends JPanel so that the Spectrometer simulation can be added to
 * the JBioFramework program interface. It holds the main user interface for
 * the Spectrometer simulation.
 *
 */

/**
 *
 * @author Amanda Fisher
 */
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainPanelGUI extends JPanel {

    private String[] proteaseChoices = {"Trypsin", "Chymotrypsin"};
    private JTextArea inputArea;
    private JComboBox proteaseBox;
    private TandemGraphGUI tandemGraph;
    private JLabel infoScreen;
    private OutputGraphGUI outputGraph;
    
    /**
     * The constructor uses a GridBagLayout to arrange the eight different
     * elements of the GUI- the label explaining the input box, the input box,
     * the label OR, the button to load a sequence, the protease selection
     * drop down box, the info label, the big graph and the small graph.
     */
    public MainPanelGUI() {
        super();
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(grid);
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel inputLabel = new JLabel("Input protein sequence to be analyzed: ");
        constraints.gridx = 0;
        constraints.gridy = 0;
        grid.setConstraints(inputLabel, constraints);
        add(inputLabel);

        inputArea = new JTextArea(7, 20);
        inputArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(inputArea);
        constraints.gridy = 1;
        grid.setConstraints(scrollPane, constraints);
        add(scrollPane);

        JLabel orLabel = new JLabel("OR");
        constraints.gridy = 2;
        grid.setConstraints(orLabel, constraints);
        add(orLabel);

        LoadButton loadButton = new LoadButton();
        constraints.gridy = 3;
        grid.setConstraints(loadButton, constraints);
        add(loadButton);

        JLabel proteaseLabel = new JLabel("Select protease: ");
        constraints.gridy = 4;
        grid.setConstraints(proteaseLabel, constraints);
        add(proteaseLabel);

        proteaseBox = new JComboBox(proteaseChoices);
        constraints.gridy = 5;
        grid.setConstraints(proteaseBox, constraints);
        add(proteaseBox);

        RunButton runButton = new RunButton();
        constraints.gridy = 6;
        grid.setConstraints(runButton, constraints);
        add(runButton);

        infoScreen = new JLabel("<html> Mass: N/A <P> <P> Charge: N/A <P> <P>");
        constraints.gridy = 7;
        constraints.fill = GridBagConstraints.BOTH;
        grid.setConstraints(infoScreen, constraints);
        add(infoScreen);

        tandemGraph = new TandemGraphGUI();
        constraints.gridy = 0;
        constraints.gridx = 1;
        constraints.gridheight = 7;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        grid.setConstraints(tandemGraph, constraints);
        add(tandemGraph);

        outputGraph = new OutputGraphGUI(this);
        constraints.gridy = 7;
        constraints.gridheight = 1;
        grid.setConstraints(outputGraph, constraints);
        add(outputGraph);

    }

    /**
     * runTandem changes the information displayed in the infoScreen Jlabel
     * when a user clicks on a peak in the OutputGraphGUI. It also alerts
     * TandemGraphGUI that there is peptide sequencing to be done.
     *
     * @param ion The ion the user selected for peptide sequencing.
     */
    public void runTandem(Ion ion) {
        infoScreen.setText("<html> Mass: " + ion.getMass() + "<P> <P> Charge: " 
                + ion.getCharge());
        tandemGraph.drawSequencePeaks(ion);
    }

   /**
     * Inner class that loads a protein file selected by the user.
     */
    private class LoadButton extends JButton implements ActionListener {

        /**
         * Constructor passes the String to be displayed on the button to
         * JButton's constructor and registers itself as its own actionListener.
         */
        public LoadButton() {
            super("Load Sequence From File");
            addActionListener(this);
        }

        /**
         * The actionPerformed method is called when the user clicks on the button.
         * It opens a JFileChooser so the user may select which file they would
         * like to obtain sequence information from.
         *
         * @param e Unused.
         */
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("FASTA files",
                "fasta");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                String parsedSequence = FastaParser.parse(chooser.getSelectedFile());
                inputArea.setText(parsedSequence);
            }

        }

    } // End of LoadButton

    /**
     * Inner class that starts the simulation running once the user clicks on it.
     */
    private class RunButton extends JButton implements ActionListener {

        /**
         * Constructor passes the String to be displayed on the button to
         * JButton's constructor and registers itself as its own actionListener.
         */
        public RunButton() {
            super("Run Spectrum");
            addActionListener(this);
        }

        /**
         * The actionPerformed method is called when the user clicks on the button.
         * It begins the simulation.
         *
         * @param e Unused.
         */
        public void actionPerformed(ActionEvent e) {
            Spectrometer.runAnalysis(inputArea.getText(), outputGraph,
                    proteaseBox.getSelectedItem().toString());
        }

    } // End of RunButton

}
