/*
 * A 2D simulation of electrophoresis using Swing components.
 */

/**
 * @author Jill Zapoticznyj
 * @author Adam Bazinet
 * @author Amanda Fisher
 */

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class GelCanvasSwingVersion extends JPanel implements MouseListener {

    private Electro2D electro2D;

    private Vector barProteins;
    private Vector dotProteins;
    private Vector barProteins2;
    private Vector dotProteins2;
    private double maxPH = -1;
    private double minPH = -1;
    private double lowAcrylamide;
    private double highAcrylamide;
    private CompIEF comp;
    private static final int VOLTAGE = 50;

    private Graphics graphic;
    private Rectangle gelCanvasRectangle;
    private Image bufferImage;
    private Graphics bufferImageGraphics;
    private boolean pHLinesNeedToBeDrawn;
    private boolean mWLinesNeedToBeDrawn;
    private boolean redrawPHAndMWLines;
    private boolean indicateProteinPosition;

    private double tenK = 48;
    private double twentyfiveK = 48;
    private double fiftyK = 48;
    private double hundredK = 48;
    private int genDotsRepeats;
    private boolean calculateMW = true;
    private boolean reMWLabel = false;
    private boolean barProteinsStillMoving = true;

    private static int iefRed = 54;
    private static int iefGreen = 100;
    private static int iefBlue = 139;

    private double xLoc;
    private double yLoc;

    private static boolean blink = false;

    /**
     * Constructs a gel canvas and adds itself as a mouse listener
     *
     * @param e Used to link back to the electro2D that will use the gel canvas
     */
    public GelCanvasSwingVersion(Electro2D e) {
        super();

        electro2D = e;
        addMouseListener(this);
    }

    /**
     * The different mouse listener methods that must be overridden
     * in order to implement mouse listener
     */
    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Sets up the gel canvas for animating the electrophoresis simulation
     */
    public void prepare() {
        /**
         * Make two vectors that will contain the objects that are the image
         * representations of the proteins in gel canvas
         */
        barProteins = new Vector();
        dotProteins = new Vector();

        /**
         * Set up the static variables in IEFProtein so it knows the pH range
         */
        maxPH = electro2D.getMaxRange();
	minPH = electro2D.getMinRange();
	IEFProteinSwingVersion.setRange( maxPH, minPH );

        /**
         * Create the CompIEF object that later will help sort the proteins
         */
       comp = new CompIEF( maxPH, minPH );

       /**
        * Call the next method, which will handle setting up the barProtein
        * vector
        */
       fillBarProteinVector();
    }

    public void fillBarProteinVector() {
        /**
         * Get all the information barProtein vector will need from electro2D
         * into local variable vectors.
         */
        Vector sequenceTitles = electro2D.getSequenceTitles();
	Vector molecularWeights = electro2D.getMolecularWeights();
	Vector pIValues = electro2D.getPiValues();
	Vector sequences = electro2D.getSequences();
	Vector functions = electro2D.getFunctions();

        /**
         * Fill up the barProteins vector
         */
        for(int i = 0; i < sequenceTitles.size(); i++) {

	    barProteins.addElement(new IEFProteinSwingVersion(new E2DProtein(
				((String)sequenceTitles.elementAt(i)),
				((Double.valueOf(
				(String)molecularWeights.elementAt(i)))).doubleValue(),
			        ((Double.valueOf(
    			        (String)pIValues.elementAt(i)))).doubleValue(),
				(String)sequences.elementAt(i),
				(String)functions.elementAt(i)), this));
	}

        /**
         * Call the next method, which will sort the elements in barProteins
         */
        sortBarProteins();
    }

    public void sortBarProteins() {
        /**
         * This nested for loop will do a sort of collapsing exercise; every
         * protein in the barProtein vector will be evaluated by the CompIEF
         * class against every other protein, and if they're the same they'll
         * be collapsed into the same element.
         *
         * The for loops start out with their iterators at -1 the length of
         * barProteins so that they can access their elements in order correctly
         */
	for(int i = barProteins.size() - 1; i >= 0; i--) {
	    for(int j = i-1; j >= 0; j--) {
		if(comp.compare((IEFProteinSwingVersion)barProteins.elementAt(i),
		(IEFProteinSwingVersion)barProteins.elementAt(j)) == 0) {
                    ((IEFProteinSwingVersion)(barProteins.elementAt(i))).addProtein(
                    ((IEFProteinSwingVersion)(barProteins.elementAt(j))).getProtein());
                    barProteins.remove(j);
		    i--;
		    j = i;
		}
	    }
	}

        /**
         * call the next method, makeDotProteins(), which will convert the bar
         * proteins into dot proteins, animation wise
         */

        makeDotProteins();
    }

    public void makeDotProteins() {

        /**
         * this next for loop goes through each element in the vector that we
         * just collapsed everything in and retrieves all the proteins that
         * were represented by each bar in the last animation
         */
        Vector tempProteins = new Vector();
	double tempx = 0;
	double tempy = 0;
	for(int i = 0; i < barProteins.size(); i++) {

            /**
             * retrieve the coordinates and proteins of each bar protein
             */
	    tempx = ((IEFProteinSwingVersion)(barProteins.elementAt(i))).returnX();
	    tempy = ((IEFProteinSwingVersion)(barProteins.elementAt(i))).returnY();
	    tempProteins = ((IEFProteinSwingVersion)(barProteins.elementAt(i))).getProtein();
	    for(int j = 0; j < tempProteins.size(); j++) {
                /**
                 * make a protein dot animation for each protein contained in
                 * the bar proteins
                 */
		dotProteins.addElement(new ProteinDotSwingVersion(
		      ((E2DProtein)(tempProteins.elementAt(j))), this, tempx,
						tempy + 43));
	    }
	    tempProteins.removeAllElements();
        }
    }

    /**
     * The prepare2 method is called only when the user wishes to compare
     * two proteome files.  The method performs the same basic steps as
     * the original prepare method cascade, as well as comparing the proteins
     * from the second file to those already contained in the first file.
     * If two proteins are the same, it colors the first file's matching
     * proteins green and removes the proteins from the second file's
     * collection of proteins.
     */
    public void prepare2() {
	Vector sequenceTitles2 = electro2D.getSequenceTitles2();
	Vector molecularWeights2 = electro2D.getMolecularWeights2();
	Vector pIValues2 = electro2D.getPiValues2();
        Vector sequences2 = electro2D.getSequences2();
	Vector functions2 = electro2D.getFunctions2();

        barProteins2 = new Vector();
	dotProteins2 = new Vector();

        /**
         * compare the sequences of the second file to the sequences of
         * the proteins in the first file
         */
        for(int i = dotProteins.size() - 1; i >= 0; i--) {
	    /**
             * color the proteins in the first file red
             */
	    ((ProteinDotSwingVersion)dotProteins.elementAt(i)).changeColor(Color.red);
	    String tempSequence = ((ProteinDotSwingVersion)dotProteins.elementAt(i)).getPro().getSequence();
	    for(int j = sequences2.size() - 1; j >= 0; j--) {

                /**
                 * if the sequences match, remove the sequence and its
                 * corresponding information from the second file's list of
                 * info and color the protein green in the vector of proteins
                 * from the first file
                 */
		if(((String)sequences2.elementAt(j)).equals(tempSequence)){
                    sequences2.remove(j);
		    sequenceTitles2.remove(j);
		    molecularWeights2.remove(j);
		    pIValues2.remove(j);
		    functions2.remove(j);
		    ((ProteinDotSwingVersion)dotProteins.elementAt(i)).changeColor(Color.green);
		    break;
		}
	    }
        }

        /**
         * Next, make IEF bar proteins out of the proteins on the second list
         * that didn't match the proteins in the first list so that their
         * positions on the gel field can later be determined
         */
        for(int i = 0; i < sequences2.size(); i++) {

	    barProteins2.addElement(new IEFProteinSwingVersion(new E2DProtein(
				((String)sequenceTitles2.elementAt(i)),
				((Double.valueOf(
				(String)molecularWeights2.elementAt(i)))).doubleValue(),
			        ((Double.valueOf(
			       (String)pIValues2.elementAt(i)))).doubleValue(),
				(String)sequences2.elementAt(i),
				(String)functions2.elementAt(i)), this));

	}

        /**
         * Convert the bar proteins into dot proteins and color them all yello
         * to designate them as being the ones from the second list that did
         * not match.
         */
        int tempx;
        int tempy;
        Vector tempProteins;
        ProteinDotSwingVersion tempDot;
        for(int i = 0; i < barProteins2.size(); i++) {
            tempx = ((IEFProteinSwingVersion)barProteins2.elementAt(i)).returnX();
            tempy = ((IEFProteinSwingVersion)barProteins2.elementAt(i)).returnY();
            tempProteins = ((IEFProteinSwingVersion)barProteins2.elementAt(i)).getProtein();

            tempDot = new ProteinDotSwingVersion((E2DProtein)tempProteins.elementAt(0), this, tempx, tempy + 43);
            tempDot.changeColor(Color.yellow);
            dotProteins2.addElement(tempDot);
        }
    }

    /**
     * The paintComponent method does the actual drawing when the System calls
     * for the GelCanvas to be set up.
     *
     * @override overrides the paintComponent method of JPanel
     */
    public void paintComponent(Graphics g) {
        /**
         * We first set up the graphics object to equal an instance variable
         * so other methods can interact with it.
         */
        graphic = g;

        /**
         * We then set up a buffer image area so that once we're done painting
         * on it we can transfer what we've drawn to the actual area that the
         * user can see. This reduces flickering.
         */
        if(gelCanvasRectangle == null || bufferImage == null || bufferImageGraphics == null) {
	    gelCanvasRectangle = getBounds();
	    bufferImage = this.createImage(gelCanvasRectangle.width, gelCanvasRectangle.height);
	    bufferImageGraphics = bufferImage.getGraphics();
	}

        /**
         * Next we check to see if it's time to draw the pH lines by finding if
         * an animation has been run by looking at whether or not the PH values
         * are different from their startig values of -1 as well as checking to
         * see if the Line boolean indicates that the lines have already been
         * drawn.
         */
      	if(maxPH != -1 && minPH != -1 && pHLinesNeedToBeDrawn) {
	    drawPHLines();
	}

        /**
         * Check to see if the SDS animation has been run, and if it has
         * draw the lines for molecular weight.
         */
     	if(mWLinesNeedToBeDrawn) {
	    initiateMWLines();
	    mWLinesNeedToBeDrawn = false;
	    redrawPHAndMWLines = true;
	} else if (redrawPHAndMWLines) {
            drawPHLines();
            redoMWLines();
        }

        /**
         * When the user clicks on a protein name in the protein list in
         * Electro2D, the drawProteinPosition method will draw a draw axis on
         * the gel canvas with the protein of interest at its origin.
         */
        if (indicateProteinPosition) {
            redrawLocation();
        }

        /**
         * Next, we color the buffer image with a rectangle the size of our
         * canvas in a sort of subdued blue color. Then we make a red
         * rectangle at the top of the image that's almost as long as the
         * image itself but only 46 pixals tall.
         * Then we copy it all over to our canvas.
         */
	bufferImageGraphics.setColor(new Color(54,100,139));
	bufferImageGraphics.drawRect(0,0,gelCanvasRectangle.width-1,gelCanvasRectangle.height-1);
	bufferImageGraphics.setColor( Color.RED );
	bufferImageGraphics.drawRect( 1, 1, gelCanvasRectangle.width - 3, 46 );
        graphic.drawImage(bufferImage, 0, 0, this);
    }

    /**
     * This method is responsible for drawing the dot proteins onto the canvas
     *
     * @param g the graphics object
     */
    public void update(Graphics g) {
        // First, clear off any dots left over from the last animation
	bufferImageGraphics.clearRect(1, 48, gelCanvasRectangle.width - 2, gelCanvasRectangle.height - 49);
	// Then, draw the protein dots
        for(int i = 0; i < dotProteins.size(); i++) {
	    ((ProteinDotSwingVersion)(dotProteins.elementAt(i))).draw(bufferImageGraphics);
	}
	if(dotProteins2 != null) {
	    for(int i = 0; i < dotProteins2.size(); i++) {
		((ProteinDotSwingVersion)(dotProteins2.elementAt(i))).draw(bufferImageGraphics);
	    }
	}

        // update the background
	if(mWLinesNeedToBeDrawn && tenK != 48) {
	    redoMWLines();
	    drawPHLines();
	}

        // transfer the buffer image to the real canvas
	g.drawImage(bufferImage, 0, 0, this);
	paint(g);
    }

    /**
     * returns the graphics object used to draw on the canvas
     *
     * @return graphic
     */
    public Graphics getGraphic() {
	return graphic;
    }

    /**
     * This method draws the dotted gray vertical lines that represent
     * where the different pH values are on the canvas.
     */
    public void drawPHLines() {
	int length = 0;
	int loc = 0;
	
	bufferImageGraphics.setColor(Color.GRAY);

        /**
         * Loop through each integer that's in the range between the minPH
         * and the maxPH and use that integer to figure out the starting point
         * for the line. Then draw a dotted line down the length of the canvas.
         */
	for(int i = (int)minPH + 1; i <= (int)maxPH; i = i + 1) {
	    length = 0;
	    loc =(int)((getWidth() - 4 ) * ((i - minPH)/(maxPH - minPH )));
	    while(length < this.getHeight()) {
		bufferImageGraphics.drawLine(loc, length, loc, length + 5);
		length = length + 10;
	    }
            // Show the pH labels.
	    electro2D.showpH(loc, i);
	}

	pHLinesNeedToBeDrawn = false;
    }

    /**
     * Use this method to say that the pH lines need to be redrawn, but not the
     * molecular weight lines.
     */
    public void setreLine() {
	pHLinesNeedToBeDrawn = true;
	redrawPHAndMWLines = false;
    }

    /**
     * Use this method to say that the pH lines and the molecular wieght
     * lines shouldn't be redrawn.
     */
    public void resetReLine() {
	pHLinesNeedToBeDrawn = false;
	redrawPHAndMWLines = false;
    }

    /**
     * This method is called by the reset button and sets all of the animation
     * variables back to their default values.
     */
    public void resetRanges() {
        minPH = -1;
        maxPH = -1;
        tenK = 48;
	twentyfiveK = 48;
	fiftyK = 48;
        hundredK = 48;
        reMWLabel = false;
        calculateMW = true;
        redrawPHAndMWLines = false;
        mWLinesNeedToBeDrawn = false;
        barProteinsStillMoving = true;
    }

    /**
     * This method is used by DotThread to let the paint method know it's time
     * to generate the molecular weight markers.
     *
     * @param i Number of times the genDots() method was called.
     */
    public void setMWLines(int i){
        mWLinesNeedToBeDrawn = true;
        calculateMW = true;
        genDotsRepeats = i;
    }

    /**
     * This method initializes the lines that denote the different ranges of
     * molecular weight and draws them for the first time.
     */
    public void initiateMWLines() {
        lowAcrylamide = electro2D.getLowPercent();
        highAcrylamide = electro2D.getHighPercent();
        int height = this.getHeight();

        if(calculateMW) {
            if(lowAcrylamide == highAcrylamide) {
                for(int i = 0; i < genDotsRepeats; i++) {
                    hundredK = hundredK + (10 * (1/lowAcrylamide)) * (VOLTAGE/25) * .25 * (100000/100000);
                    fiftyK = fiftyK + (10 * (1/lowAcrylamide)) * (VOLTAGE/25) * .25 * (100000/50000);
                    twentyfiveK = twentyfiveK + (10 * (1/lowAcrylamide)) * (VOLTAGE/25) * .25 * (100000/25000);
                    tenK = tenK + (10 * (1/lowAcrylamide)) * (VOLTAGE/25) * .25 * (100000/10000);
                }
            } else {
                for(int i = 0; i < genDotsRepeats; i++) {
                    hundredK = hundredK + (10 * 1/(((hundredK - 48) * (highAcrylamide - lowAcrylamide)/(height - 48))+lowAcrylamide)) * (VOLTAGE/25)* .25 * (100000/100000);
                    fiftyK = fiftyK + (10 * 1/(((fiftyK - 48) * (highAcrylamide - lowAcrylamide)/(height - 48))+ lowAcrylamide)) * (VOLTAGE/25) * .25 * (100000/50000);
                    twentyfiveK = twentyfiveK + (10 * 1/(((twentyfiveK - 48) * (highAcrylamide - lowAcrylamide)/(height - 48))+ lowAcrylamide)) * (VOLTAGE/25) * .25 * (100000/25000);
                    tenK = tenK + (10 * 1/(((tenK - 48)*(highAcrylamide - lowAcrylamide)/(height - 48)) + lowAcrylamide)) * (VOLTAGE/25) * .25 * (100000/10000);
                }
            }

            calculateMW = false;
            int width = 0;
            bufferImageGraphics.setColor(Color.LIGHT_GRAY);

            while (width < this.getWidth()) {
                bufferImageGraphics.drawLine(width, (int)hundredK, width + 5, (int)hundredK);
                bufferImageGraphics.drawLine(width, (int)fiftyK, width + 5, (int)fiftyK);
                bufferImageGraphics.drawLine(width, (int)twentyfiveK, width + 5, (int)twentyfiveK);
                bufferImageGraphics.drawLine(width, (int)tenK, width + 5, (int)tenK);
                width = width + 10;
            }

            electro2D.clearMW();
            electro2D.showMW((int)hundredK, (int)fiftyK, (int)twentyfiveK, (int)tenK, reMWLabel);
            reMWLabel = true;
            graphic.drawImage(bufferImage, 0, 0, this);
        }
    }

    /**
     * This method redraws the lines that denote the different ranges of
     * molecular weight after the initializeMWLines method has already been
     * run.
     */
    public void redoMWLines() {
        lowAcrylamide = electro2D.getLowPercent();
        highAcrylamide = electro2D.getHighPercent();
        int width = 0;
        bufferImageGraphics.setColor(Color.LIGHT_GRAY);

        while(width < this.getWidth()) {
            bufferImageGraphics.drawLine(width, (int)hundredK, width + 5, (int)hundredK);
            bufferImageGraphics.drawLine(width, (int)fiftyK, width + 5, (int)fiftyK);
            bufferImageGraphics.drawLine(width, (int)twentyfiveK, width + 5, (int)twentyfiveK);
            bufferImageGraphics.drawLine(width, (int)tenK, width + 5, (int)tenK);
            width = width + 10;
        }

        electro2D.clearMW();
    	electro2D.showMW((int)hundredK, (int)fiftyK, (int)twentyfiveK, (int)tenK, reMWLabel);
        reMWLabel = true;
    }

    /**
     * This method draws the IEF proteins, which appear as moving rectangles at
     * the top of the animation, to the screen.
     */
    public void drawIEF() {
        for(int i = 0; i < barProteins.size(); i++){
            if(barProteinsStillMoving) {
                ((IEFProtein)barProteins.elementAt(i)).changeX();
            } else {
                ((IEFProtein)barProteins.elementAt(i)).setX();
            }
            ((IEFProtein)(barProteins.elementAt(i))).draw(bufferImageGraphics);
        }

        for(int i = 0; i < barProteins2.size(); i++){
            if(barProteinsStillMoving) {
                ((IEFProtein)barProteins2.elementAt(i)).changeX();
            } else {
                ((IEFProtein)barProteins2.elementAt(i)).setX();
            }
            ((IEFProtein)(barProteins2.elementAt(i))).draw(bufferImageGraphics);
        }

        graphic.drawImage(bufferImage, 0, 0, this);
        this.repaint();
    }

    /**
     * This method gives the illusion that the barProteins are being squashed
     * into the lower part of the animation.
     */
    public void shrinkIEF() {
        clearIEF();
        drawIEF();
    }

    /**
     * This method clears the IEF animation area.
     */
    public void clearIEF() {
        bufferImageGraphics.setColor(Color.WHITE);
        bufferImageGraphics.clearRect(2, 2, gelCanvasRectangle.width - 3, 45);
    }

    /**
     * Returns the red value for the background of the IEF animation.
     *
     * @return IEFRED
     */
    public static int getRed() {
        return iefRed;
    }

    /**
     * Returns the green value for the background of the IEF animation.
     *
     * @return IEFGREEN
     */
    public static int getGreen() {
        return iefGreen;
    }

    /**
     * Returns the blue value for the background of the IEF animation.
     *
     * @return IEFBLUE
     */
    public static int getBlue() {
        return iefBlue;
    }

    /**
     * Resets the red value for the IEF animation background when the reset
     * button is pressed.
     */
    public static void setRed() {
        iefRed = 54;
    }

    /**
     * Resets the green value for the IEF animation background when the reset
     * button is pressed.
     */
    public static void setGreen() {
        iefGreen = 100;
    }

    /**
     * Resets the blue value for the IEF animation background when the reset
     * button is pressed.
     */
    public static void setBlue() {
        iefBlue = 139;
    }

    /**
     * Controls the animation for the initial display of IEF proteins.
     */
    public void animateIEF() {
        int finalRed = 0;
        int finalGreen = 0;
        int finalBlue = 0;

        double width = IEFProtein.returnTempWidth();
	double finalWidth = IEFProtein.returnWidth();

        bufferImageGraphics.setColor(new Color(iefRed, iefGreen, iefBlue));
	bufferImageGraphics.fillRect(2, 2, gelCanvasRectangle.width - 3, 45);

        IEFProtein.changeWidth();
	drawIEF();

        iefRed = iefRed - 1;
	iefGreen = iefGreen - 2;
	iefBlue = (int)(iefBlue - 2.78);

        if(iefRed <= finalRed || iefGreen <= finalGreen || iefBlue <= finalBlue || width >= finalWidth) {
            barProteinsStillMoving = false;
            bufferImageGraphics.setColor(new Color(finalRed, finalGreen, finalBlue));
            IEFProtein.setWidth();
            bufferImageGraphics.fillRect(2, 2, gelCanvasRectangle.width - 3, 45);
	    drawIEF();
        }
    }

    /**
     * Returns the vector that contains the ProteinDots.
     *
     * @return the protein dots
     */
    public Vector getDots() {
        return dotProteins;
    }

    /**
     * Returns the second vector that contains ProteinDots.
     *
     * @return the protein dots used for comparison
     */
    public Vector getDots2() {
        return dotProteins2;
    }

    /**
     * Increments the y values for the protein dots depending on whether the
     * start button for the second animation has been pushed or not.
     */
    public void genDots() {
        clearCanvas();

        for(int i = 0; i < dotProteins.size(); i++) {
            ((ProteinDot)(dotProteins.elementAt(i))).changeY();
        }
        if(dotProteins2 != null) {
      	    for(int j = 0; j < dotProteins2.size(); j++) {
		((ProteinDot)(dotProteins2.elementAt(j))).changeY();
	    }
        }
        repaint();
    }

    /**
     * Called with the reset button, sets the y values for the protein dots back
     * to their original locations.
     */
    public void restartCanvas() {
        for(int i = 0; i < dotProteins.size(); i++) {
	    ((ProteinDot)(dotProteins.elementAt(i))).restart();
	}
        for(int i = 0; i < dotProteins2.size(); i++) {
	    ((ProteinDot)(dotProteins2.elementAt(i))).restart();
	}
        update(graphic);
	repaint();
    }

    /**
     * Clears the canvas in preperation for more animation.
     */
    public void clearCanvas() {
        graphic.setColor(new Color(54, 100, 139));
	graphic.clearRect(1, 48, gelCanvasRectangle.width - 1, gelCanvasRectangle.height - 47);
    }

    /**
     * Draws black axis lines over a protein on the canvas whose name has been
     * selected from a list of proteins.
     *
     * @param id the title of the protein to be indicated
     */
    public void drawLocation(String id) {
        xLoc = 0;
        yLoc = 0;
        bufferImageGraphics.setColor(Color.BLACK);
        bufferImageGraphics.fillRect(2, 2, gelCanvasRectangle.width - 4, 45);
        for(int i = 0; i < dotProteins.size(); i++) {
            if((((ProteinDot)dotProteins.elementAt(i)).getPro().getID()).equals(id)) {
                xLoc = ((ProteinDot)dotProteins.elementAt(i)).returnX();
		yLoc = ((ProteinDot)dotProteins.elementAt(i)).returnY();
                i = dotProteins.size();
            }
        }
        indicateProteinPosition = true;
        repaint();
    }

    /**
     * Used by the DotThread class.
     */
    public void startDotBlink() {
        blink = true;
    }

    /**
     * Returns the statis of blink.
     *
     * @return blink
     */
    public static boolean getBlink() {
        return blink;
    }

    /**
     * Sets blink to false.
     */
     public static void stopBlink() {
         blink = false;
     }

}