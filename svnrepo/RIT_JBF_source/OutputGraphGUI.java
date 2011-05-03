/*
 * This class extends JPanel to override the paintComponent method in order to
 * represent the output of the spectrometer on a graph. This OutputGraphGUI will
 * be added to the MainPanelGUI. OutputGraphGUI also sets each Ion's xCoordinate
 * instance variable.
 *
 * version 2, working on adding peak clickability for version 3
 */

/**
 *
 * @author Amanda Fisher
 */
import javax.swing.JPanel;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;


public class OutputGraphGUI extends JPanel implements MouseListener {

    private ArrayList<Ion> peakLines;
    double mostHits;
    int width;
    int height;
    int xAxisWidth;
    int yAxisHeight;
    int xAxisStartingPoint;
    int yAxisStartingPoint;
    int halfHashMarkLength;

    /**
     * The constructor's only purpose is to add itself as its own mouse
     * listener, so the user's clicks can be registered.
     */
    public OutputGraphGUI() {
        super();
        addMouseListener(this);
    }


    /**
     * setPeaks is called by the Spectrometer class to tell OutputGraphGUI where
     * to draw the lines that represent ion peaks.
     *
     * @param pL ArrayList of two element double arrays where each array's first
     * entry is the peak's mass charge ratio, and the second entry is the peak's
     * intensity.
     * @param mH Spectrometer gives the int number of the most hits for a specific
     * ion occuring so intensity of each peak can be calculated.
     */
    public void setPeaks(ArrayList<Ion> pL, double mH) {
        peakLines = pL;
        mostHits = mH;
        repaint();
    }

    /**
     * paintComponent is overridden from the JComponent class to allow
     * OutputGraphGUI to paint the graph the appropriate size whenever the user
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
        for(int i = 0; i < 18; i++) {
            int xPos = xAxisStartingPoint + i*xAxisWidth/17;
            g.drawLine(xPos, yStart, xPos, yEnd);
            String markNumber = String.valueOf(i*100);
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

        //call drawPeaks so it will take care of any mass spec lines
        drawPeaks(g);
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
                        ion.getMassChargeRatio()/1700);
                yPos = (int)(yAxisStartingPoint + yAxisHeight - yAxisHeight *
                        ion.getHits()/mostHits);
                ion.setXCoordinate(xPos);
                g.drawLine(xPos, yAxisStartingPoint + yAxisHeight, xPos, yPos);
            }
        }
    }

    /**
     * mouseClicked is invoked whenever the user clicks on the outputGraphGUI.
     *
     * @param e The event created by the user's click; can be used to find the
     *          x and y coordinates of the click for use with peak picking.
     */
    public void mouseClicked(MouseEvent e) {
        pickPeak(e.getX());
    }

    /**
     * peakPick is called by the mouseClicked method to identify which peak, if
     * any, the user has clicked on (or around).
     * 
     * Right now also displays the peak's m/e on standard output.
     * 
     * @param x The x coordinate of the click.
     */
    private void pickPeak(int x) {
        if(peakLines != null) {
            for(Ion ion : peakLines) {
                if(ion.getXCoordinate()+ 2 > x && ion.getXCoordinate() - 2 < x) {
                    System.out.println(ion.getMassChargeRatio());
                }
            }
        }
    }

    /**
     * The following four methods are stubbed out so the MouseListener class can
     * be implemented.
     *
     * @param e unused.
     */
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }

}
