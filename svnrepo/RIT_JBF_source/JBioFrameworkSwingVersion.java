/*
 * The main frame for the JBioFramework program
 * Adds Electro2D, Electrophoresis, and Ionex applications to itself
 * with a JTabbedPane.
 */

/**
 *
 * @author Amanda Fisher
 */
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.io.*;

public class JBioFrameworkSwingVersion extends JFrame {

    private JTabbedPane tabbedPane;
    private Electro2D electro2D;
    private Electrophoresis electrophoresis;
    private ionex ionex;

    public static void main(String[] args) {
        JBioFrameworkSwingVersion jbfSwing = new JBioFrameworkSwingVersion();
    }

    public JBioFrameworkSwingVersion() {
        super();

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        electro2D = new Electro2D();
        electrophoresis = new Electrophoresis();
        ionex = new ionex();

        tabbedPane = new JTabbedPane();

/*what happens if one of these classes comes back null?
  think about a try-catch block or some other error handling */
        tabbedPane.addTab("Electro2D", electro2D);
        tabbedPane.addTab("Electrophoresis", electrophoresis);
        tabbedPane.addTab("Ionex", ionex);

        add(tabbedPane);

        /**
         * Use a toolit to find the screen size of the user's monitor and set
         * the window size to it.
         */

        setSize(Toolkit.getDefaultToolkit().getScreenSize());

        this.pack();
    }    
}
