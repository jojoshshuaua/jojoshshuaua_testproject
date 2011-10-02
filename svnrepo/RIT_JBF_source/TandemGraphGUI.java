/*
 * TandemGraphGUI displays the mass spec of a peak from OutputGraphGUI.
 */

/**
 *
 * @author Amanda Fisher
 */

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class TandemGraphGUI extends JPanel {

    int width;
    int height;
    int xAxisWidth;
    int yAxisHeight;
    int xAxisStartingPoint;
    int yAxisStartingPoint;
    int halfHashMarkLength;
    int numericalDifference = 200;
    int startingPoint = 0;

    private ArrayList<Ion> peakLines;

    /**
     * paintComponent is overridden from the JComponent class to allow
     * TandemGraphGUI to paint the graph the appropriate size whenever the user
     * resizes the window. All positions in it are relative to its size.
     * (paintComponent is only called by the JVM itself; never the coder- we
     * can just call repaint)
     *
     * @param g The graphics object, supplied by the internal workings of Java.
     */
    protected void paintComponent(Graphics g) {

        width = getWidth();
        height = getHeight();
        xAxisWidth = width - width*3/20;
        yAxisHeight = height - height*1/5;
        xAxisStartingPoint = width/10;
        yAxisStartingPoint = height/20;
        halfHashMarkLength = 10;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.BLACK);
        //draw horizontal axis
        g.drawLine(xAxisStartingPoint, yAxisStartingPoint+yAxisHeight,
                   xAxisStartingPoint+xAxisWidth, yAxisStartingPoint+yAxisHeight);
        //draw verticle axis
        g.drawLine(xAxisStartingPoint, yAxisStartingPoint,
                   xAxisStartingPoint, yAxisStartingPoint+yAxisHeight);

        //draw labels
        g.drawString("I", width/40, height/2);
        g.drawString("m/e", width/2, height*49/50);

        //draw horizontal axis hash marks and numbers
        int yStart = yAxisStartingPoint + yAxisHeight - halfHashMarkLength;
        int yEnd = yAxisStartingPoint + yAxisHeight + halfHashMarkLength;
        for(int i = 0; i < 16; i++) {
            int xPos = xAxisStartingPoint + i*xAxisWidth/15;
            g.drawLine(xPos, yStart, xPos, yEnd);
            String markNumber = String.valueOf(i*numericalDifference
                                                + startingPoint);
            g.drawString(markNumber, xPos-10, yEnd + 15);
        }

        //draw verticle axis hash marks and numbers
        int xStart = xAxisStartingPoint - halfHashMarkLength;
        int xEnd = xAxisStartingPoint + halfHashMarkLength;
        for(int i = 0; i < 11; i++) {
            int yPos = yAxisStartingPoint + i*yAxisHeight/10;
            g.drawLine(xStart, yPos, xEnd, yPos);
            String markNumber = String.valueOf(100-i*10);
            g.drawString(markNumber, xStart-25, yPos+5);
        }

        drawPeaks(g);
    }

    /**
     * drawSequencePeaks figures what ions are created from the initial
     * ion and makes an arrayList of ions with them. It then calls drawPeaks
     * to display the ions in the list.
     *
     * b and y fragments: In truth, peptides can be broken down into two smaller
     * fragments at any of three different types of places in their chain. We're
     * just interested in the break that can happen at the CO-NH bond between
     * residues. This particular break can give rise to a b fragment or a y
     * fragment, depending on which side of the break the charge goes to. If it
     * ends up on the N-terminal side, we call it a b-fragment; if it ends up
     * on the C-terminal side, we call it a y-fragment. The other side of the
     * break recieves no charge, and so doesn't show up in mass spec. Because
     * each ion can break only once, you'll get a nice spread of each possible
     * break in the output- each b peak one residue's mass away from the previous
     * b peak, and each y peak one residue's mass away from the previous y peak.
     * The b-fragments let you 'read' the peptide sequence from left to right
     * across the output graph, and the y-fragments let you 'read' the peptide
     * sequence from right to left. You can assume the last residue in each
     * sequence will be whatever the protease used to digest the original peptide
     * cuts at.
     *
     * Source: http://www.astbury.leeds.ac.uk/facil/MStut/mstutorial.htm
     *
     * @param ion The ion the user selected for peptide sequencing.
     */
    public void drawSequencePeaks(Ion ion) {
        peakLines = new ArrayList<Ion>();
        // First, make the b-fragment ions.
        Ion bIon;
        for (int i = 0; i < ion.size(); i++) {
            bIon = new Ion();
            for (int j = 0; j < i; j++) {
                bIon.add(ion.get(j));
            }
            bIon.add(ion.get(i));
            bIon.setCharge(1);
            peakLines.add(bIon);
        }

        // Next, make the y-fragment ions.
        Ion yIon;
        for (int i = ion.size()-1; i > -1; i--) {
            yIon = new Ion();
            for (int j = ion.size()-1; j > i; j--) {
                yIon.add(ion.get(j));
            }
            yIon.add(ion.get(i));
            yIon.setCharge(1);
            peakLines.add(yIon);
        }

        // Resize the x axis.
        resizeXAxis();
        // Finally,  put the ions on the graph.
        repaint();
    }

    /**
     * drawPeaks will draw the lines that represent output from the Spectrometer.
     *
     * @param g Used like a paintbrush to draw the lines.
     */
    public void drawPeaks(Graphics g) {
        g.setColor(Color.BLACK);
        int xPos;
        int yPos;
        if (peakLines != null) {

            for(Ion ion : peakLines) {
                xPos = (int)(xAxisStartingPoint + xAxisWidth *
                        ((ion.getMassChargeRatio() - startingPoint)
                        /(numericalDifference*15)));
                //yPos = (int)(yAxisStartingPoint + yAxisHeight - yAxisHeight *
                //        ion.getHits()/mostHits);
                // For peptide sequencing, right now assume all peaks will be
                // even.
                yPos = (int)(yAxisStartingPoint + yAxisHeight - yAxisHeight);
                ion.setXCoordinate(xPos);
                g.drawLine(xPos, yAxisStartingPoint + yAxisHeight, xPos, yPos);
            }
        }
    }

    /**
     * drawSequencePeaks calls this method right before it calls repaint so that
     * OutputGraphGUI can resize its x-axis to start at the smallest m/e ratio
     * and end at the largest m/e ratio.
     *
     * @param smallestme - the smallest m/e ratio among the ions
     * @param biggestme - the biggest m/e ratio among the ions
     */
    public void resizeXAxis() {
        // Determine the smallest m/e ratio and the biggest m/e ratio
        // among the ions present.
        double smallestme = peakLines.get(0).getMassChargeRatio();
        double biggestme = peakLines.get(0).getMassChargeRatio();
        for(int i = 1; i < peakLines.size(); i++) {
            if(peakLines.get(i).getMassChargeRatio() < smallestme) {
                smallestme = peakLines.get(i).getMassChargeRatio();
            } else if(peakLines.get(i).getMassChargeRatio() > biggestme) {
                biggestme = peakLines.get(i).getMassChargeRatio();
            }
        }

        // Determine the numerical separation between the hash marks
        startingPoint = (int)smallestme - 1;
        startingPoint = startingPoint - (startingPoint % 5);
        int end = (int)biggestme + 1;
        int deltame = end - startingPoint;
        // Make sure the numerical separation is always at least 1.
        if(deltame > 15) {
            int remainder = deltame % 15;
            numericalDifference = (deltame + (15 - remainder))/15;
        } else {
            numericalDifference = 1;
        }

    }

}
