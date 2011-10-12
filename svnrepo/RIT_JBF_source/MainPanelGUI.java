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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JCheckBox;

public class MainPanelGUI extends JPanel {

    private String[] proteaseChoices = {"Trypsin", "Chymotrypsin"};
    private JTextArea inputArea;
    private JComboBox proteaseBox;
    private TandemGraphGUI tandemGraph;
    private JLabel massDisplay;
    private ToggleFragmentButton blueBs;
    private ToggleFragmentButton redYs;
    private OutputGraphGUI outputGraph;
    private Ion ion;
    
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

        massDisplay = new JLabel("<html> Mass: N/A <P>");
        constraints.gridy = 7;
        grid.setConstraints(massDisplay, constraints);
        add(massDisplay);

        blueBs = new ToggleFragmentButton("B fragments", true);
        constraints.gridy = 8;
        grid.setConstraints(blueBs, constraints);
        add(blueBs);

        redYs = new ToggleFragmentButton("Y fragments", true);
        constraints.gridy = 9;
        grid.setConstraints(redYs, constraints);
        add(redYs);

        tandemGraph = new TandemGraphGUI();
        constraints.gridy = 0;
        constraints.gridx = 1;
        constraints.gridheight = 7;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        grid.setConstraints(tandemGraph, constraints);
        add(tandemGraph);

        outputGraph = new OutputGraphGUI(this);
        constraints.gridy = 7;
        constraints.gridheight = 3;
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
    public void runTandem(Ion selected) {
        ion = selected;
        massDisplay.setText("<html> Mass: " + ion.getMass());
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

    /**
     * Inner class that repaints the TandemGraphGUI when the user clicks on it.
     */
    private class ToggleFragmentButton extends JCheckBox implements ItemListener {

        /**
         * Constructor passes the String to be displayed on the button to
         * JCheckBox's constructor and registers itself as its own actionListener.
         */
        public ToggleFragmentButton(String text, boolean state) {
            super(text, state);
            addItemListener(this);
        }

        /**
         * The actionPerformed method is called when the user clicks on the button.
         * It repaints TandemGraphGUI.
         *
         * @param e Unused.
         */
        public void itemStateChanged(ItemEvent e) {
            Object source = e.getItemSelectable();
            if (source == blueBs) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    tandemGraph.setBlueBs(false);
                } else {
                    tandemGraph.setBlueBs(true);
                }
            } else {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    tandemGraph.setRedYs(false);
                } else {
                    tandemGraph.setRedYs(true);
                }
            }
            if(ion != null) {
                tandemGraph.drawSequencePeaks(ion);
            }
        }

    } // End of FragmentToggleButton
}
