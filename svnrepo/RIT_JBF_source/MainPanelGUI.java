/*
 * This class extends JPanel so that the Spectrometer simulation can be added to
 * the JBioFramework program interface. It holds the main user interface for
 * the Spectrometer simulation.
 *
 * version 2
 * NOTE: Update to Version 3!
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
    private OutputGraphGUI outputGraph;

    /**
     * The constructor uses a GridBagLayout to arrange the five different elements
     * of the GUI. TO FIX: The last element in the x = 0 column will be dragged downward
     * when the window is resized, but the other elements in its column stay put
     * where they're supposed to. Figure out how to get the runButton to stay right
     * below the other elements.
     */
    public MainPanelGUI() {
        super();
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(grid);
        constraints.insets = new Insets(10, 10, 10, 10);

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
        constraints.gridy = 3;
        grid.setConstraints(proteaseLabel, constraints);
        add(proteaseLabel);

        proteaseBox = new JComboBox(proteaseChoices);
        constraints.gridy = 4;
        grid.setConstraints(proteaseBox, constraints);
        add(proteaseBox);

        RunButton runButton = new RunButton();
        constraints.gridy = 5;
        grid.setConstraints(runButton, constraints);
        add(runButton);

        outputGraph = new OutputGraphGUI();
        constraints.gridy = 0;
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        grid.setConstraints(outputGraph, constraints);
        add(outputGraph);

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
