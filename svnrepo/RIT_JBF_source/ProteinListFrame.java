
/**
 * This class constructs a window to display two different protein lists and
 * allows the manipulation of those lists to alter the protein sequences run on
 * the GelCanvas.
 *
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class ProteinListFrame extends JFrame {

    Electro2D electro2D;
    JList sequenceOneList;
    JList sequenceTwoList;
    Vector sequenceTitlesOne;
    Vector sequenceTitlesTwo;

    /**
     * Constructor for the window; sets up the instance variables and builds
     * the GUI.
     *
     * @param param     The title of the window.
     * @param electro2D Reference to the electro2D where the protein lists are.
     */
    public ProteinListFrame(String param, Electro2D electro2D) {
        super(param);
        this.electro2D = electro2D;
        setLayout(new GridBagLayout());
        JLabel sequenceOneLabel = new JLabel("Sequence One");
        JLabel sequenceTwoLabel = new JLabel("Sequence Two");
        sequenceOneList = new JList(electro2D.getSequenceTitles());
        sequenceTwoList = new JList(electro2D.getSequenceTitles2());
        sequenceTitlesOne = new Vector(electro2D.getSequenceTitles());
        sequenceTitlesTwo = new Vector(electro2D.getSequenceTitles2());
        JScrollPane sequenceOneScroll = new JScrollPane(sequenceOneList);
        JScrollPane sequenceTwoScroll = new JScrollPane(sequenceTwoList);
        JLabel removeLabel = new JLabel("Select proteins to remove: ");
        JButton sequenceOneButton = new JButton("Sequence One");
        JButton sequenceTwoButton = new JButton("Sequence Two");
        JButton commonButton = new JButton("Common");
        JButton selectedButton = new JButton("Selected");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = (new Insets(5, 5, 5, 5));
        c.gridx = 0;
        c.gridy = 0;
        add(sequenceOneLabel, c);

        c.gridx = 1;
        add(sequenceTwoLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(sequenceOneScroll, c);

        c.gridx = 1;
        add(sequenceTwoScroll, c);

        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        add(removeLabel, c);

        c.gridy = 3;
        c.gridwidth = 1;
        add(sequenceOneButton, c);

        c.gridx = 1;
        add(sequenceTwoButton, c);

        c.gridx = 0;
        c.gridy = 4;
        add(commonButton, c);

        c.gridx = 1;
        add(selectedButton, c);

        sequenceOneButton.addActionListener(new OneListener());
        sequenceTwoButton.addActionListener(new TwoListener());
        commonButton.addActionListener(new CommonListener());
        selectedButton.addActionListener(new SelectedListener());

        pack();
        setVisible(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    /**
     * This method is called by electro2D's refreshProteinList and 
     * refreshProteinList2 method when a new protein sequence is loaded.
     * updateSequences copies the protein sequence titles into its own lists
     * for display.
     */
    public void updateSequences(Vector pL1, Vector pL2) {
        sequenceOneList.setListData(pL1);
        sequenceTwoList.setListData(pL2);
        sequenceTitlesOne = new Vector(pL1);
        sequenceTitlesTwo = new Vector(pL2);
    }
    
    /**
     * Listener for the Sequence One button. Removes all proteins in sequence
     * one and all sequence one proteins in sequence two.
     */
    private class OneListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for(int x = 0; x < sequenceTitlesOne.size(); x++) {
                for (int y = 0; y < sequenceTitlesTwo.size(); y++) {
                    if (sequenceTitlesOne.get(x).equals(sequenceTitlesTwo.get(y))) {
                        sequenceTitlesTwo.remove(y);
                    }
                }
            }
            sequenceTitlesOne.clear();
            sequenceOneList.setListData(sequenceTitlesOne);
            sequenceTwoList.setListData(sequenceTitlesTwo);
            sequenceOneList.validate();
            sequenceTwoList.validate();
        }
    }

    /**
     * Listener for the Sequence Two button. Removes all proteins in sequence
     * two and all sequence two proteins in sequence one.
     */
    private class TwoListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for(int x = 0; x < sequenceTitlesTwo.size(); x++) {
                for (int y = 0; y < sequenceTitlesOne.size(); y++) {
                    if (sequenceTitlesTwo.get(x).equals(sequenceTitlesOne.get(y))) {
                        sequenceTitlesOne.remove(y);
                    }
                }
            }
            sequenceTitlesTwo.clear();
            sequenceOneList.setListData(sequenceTitlesOne);
            sequenceTwoList.setListData(sequenceTitlesTwo);
            sequenceOneList.validate();
            sequenceTwoList.validate();

        }
    }

    /**
     * Listener for the Common button. Removes all proteins in sequence one and
     * sequence two that are shared between sequence one and sequence two.
     */
    private class CommonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Vector copySequenceTwo = (Vector)sequenceTitlesTwo.clone();
            sequenceTitlesTwo.removeAll(sequenceTitlesOne);
            sequenceTitlesOne.removeAll(copySequenceTwo);

            sequenceOneList.setListData(sequenceTitlesOne);
            sequenceTwoList.setListData(sequenceTitlesTwo);
            sequenceOneList.validate();
            sequenceTwoList.validate();
        }
    }

    /**
     * Listener for the Selected button. Removes all proteins that are selected.
     */
    private class SelectedListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int[] oneIndexes = sequenceOneList.getSelectedIndices();
            int[] twoIndexes = sequenceTwoList.getSelectedIndices();
            Vector oneProteins = new Vector();
            Vector twoProteins = new Vector();
            for(int x = 0; x < oneIndexes.length; x++) {
                oneProteins.add(sequenceTitlesOne.get(oneIndexes[x]));
            }
            for(int y = 0; y < twoIndexes.length; y++) {
                twoProteins.add(sequenceTitlesTwo.get(twoIndexes[y]));
            }
            sequenceTitlesOne.removeAll(oneProteins);
            sequenceTitlesTwo.removeAll(twoProteins);

            sequenceOneList.setListData(sequenceTitlesOne);
            sequenceTwoList.setListData(sequenceTitlesTwo);
            sequenceOneList.validate();
            sequenceTwoList.validate();
        }
    }

}
