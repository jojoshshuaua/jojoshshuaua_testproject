/* Decompiled by Mocha from Plot.class */
/* Originally compiled from Plot.java */

import java.awt.*;
import java.io.PrintStream;
import java.text.DecimalFormat;

public class Plot extends Panel implements Runnable
{
    Font plotFont;
    Thread runner;
    int pause;
    Electrophoresis parent;
    int numberOfStds;
    Protein stds[];
    Protein sample;
    Protein dye;
    private Image offScreenImage;
    private Graphics offScreenGraphics;
    FontMetrics fm;
    Font font;
    protected boolean imageCreated;
    protected boolean standardsSet;
    protected boolean paintRM;
    protected boolean paintUserRM;
    protected boolean stopAnimation;
    protected boolean showExperimentalMW;
    protected boolean showSampleMW;
    protected boolean questionRCorr;
    protected boolean showLogMW;
    protected boolean showNotBracketed;
    protected boolean graphVerticalLine;
    protected boolean graphHorizontalLine;
    protected boolean newYLine;
    protected boolean newXLine;
    protected boolean     Played;
    protected boolean harpPlayed;
    protected int bottomEdge;
    protected int rightEdge;
    protected int xAxesLabelY;
    protected int xAxesRMLabelY;
    protected int yAxesLabelY;
    protected int yAxisLabelX;
    protected int charHalfHeight;
    protected int charHeight;
    protected int charWidth;
    protected int charHalfWidth;
    protected double yDivision;
    protected int division;
    protected int gridRows;
    protected int gridCols;
    protected int rightGridCol;
    protected int leftGridCol;
    protected int bottomGridRow;
    protected int topGridRow;
    protected int rows;
    protected int cols;
    protected int xArray[];
    protected int yArray[];
    protected double logMwMax;
    protected double logMwMin;
    protected double ln10;
    protected double yConversion;
    protected double deltaPixelY;
    protected double deltaPixelX;
    protected double deltaMw;
    protected double mwOffsetFromMax;
    protected double pixOffsetFromTop;
    protected double mouseRM;
    protected double plotRM;
    protected double experimentalMW;
    protected double logMw;
    protected double sumX;
    protected double sumY;
    protected double sumXsq;
    protected double sumYsq;
    protected double sumProd;
    protected double rCorr;
    protected double rCorrSq;
    protected int nPoints;
    protected double slope;
    protected double yIntercept;
    protected int yPos;
    protected int xPos;
    protected int xMouse;
    protected int xPlot;
    protected int userLineY;
    protected int userLineX;
    protected Point lineCoord;
    protected int fitLineX1;
    protected int fitLineX2;
    protected int fitLineY1;
    protected int fitLineY2;
    protected String cursorPos;
    protected int dotPos;
    protected double errorMargin;
    protected DecimalFormat twoDigits;

    Plot(Electrophoresis electrophoresis) {
        pause = 20;
        numberOfStds = 7;
        stds = new Protein[numberOfStds];
        sample = new Protein();
        dye = new Protein();
        stopAnimation = true;
        newYLine = true;
        newXLine = true;
        yAxisLabelX = 1;
        gridRows = 10;
        gridCols = 10;
        rows = 14;
        cols = 14;
        xArray = new int[cols];
        yArray = new int[rows];
        ln10 = 1.0;
        yConversion = 1.0;
        deltaPixelY = 1.0;
        deltaPixelX = 1.0;
        deltaMw = 1.0;
        mwOffsetFromMax = 1.0;
        pixOffsetFromTop = 1.0;
        yPos = 1;
        xPos = 1;
        lineCoord = new Point(0, 0);
        errorMargin = 0.2;
        parent = electrophoresis;
        ln10 = Math.log(10.0);
        rightGridCol = cols - 1;
        leftGridCol = rightGridCol - gridCols;
        bottomGridRow = rows - 1;
        topGridRow = bottomGridRow - gridRows;
	twoDigits = new DecimalFormat( "0.00" );
    } // Plot

    public void start() {
        if (runner == null)
        {
            runner = new Thread(this);
            runner.start();
        }
    } // start

    public void stop() {
        if (runner != null)
        {
            runner.stop();
            runner = null;
        }
    } // stop

    public void update(Graphics g) {
        paint(g);
    } // update

    private void calcLine() {
        if (nPoints <= 1)
        {
            slope = 0.0;
            yIntercept = 0.0;
            return;
        }
        slope = ((double)nPoints * sumProd - sumX * sumY) / ((double)nPoints * sumXsq - sumX * sumX);
        yIntercept = (sumY - slope * sumX) / nPoints;
    }

    private void showNotBracket(Graphics g) {
        if (showNotBracketed)
            g.drawString("RM not bracketed by Standards", xArray[leftGridCol], size().height / 8);
    }

    private void resetSums() {
        sumX = 0.0;
        sumY = 0.0;
        sumXsq = 0.0;
        sumYsq = 0.0;
        sumProd = 0.0;
        nPoints = 0;
    }

    private Point calcLinePoint(double d1) {
        double d2 = calcLogMw(d1);
        double d3 = logMwMax - d2;
        double d4 = d3 * yConversion;
        int j = (int)((double)yArray[topGridRow] + d4);
        int i = (int)((double)xArray[leftGridCol] + d1 * deltaPixelX);
        return new Point(i, j);
    }

    protected void calcMaxMinLogs() {
	// make divisions n*.05 & then make Max plot round number above maxMw
        int i = 0;
        int j = 9999999;
	float  mDelta, test;

        for (int k = 0; k < numberOfStds; k++)
        {
            if (stds[k].mw > i)
                i = stds[k].mw;
            if (stds[k].mw < j)
                j = stds[k].mw;
        }
        logMwMax = Math.log((double)i) / ln10;
        logMwMin = Math.log((double)j) / ln10;
	mDelta = (float) ((double)1.1 * (logMwMax - logMwMin));
	for ( yDivision = 0; 10*yDivision < mDelta; yDivision += .05 ){
	}
	for (test = (int) logMwMax; test <= logMwMax; test += yDivision) {
	}
	logMwMax = test;
	logMwMin = logMwMax - 10*yDivision;
	//        yDivision = (logMwMax - logMwMin) / 10.0;
        deltaMw = logMwMax - logMwMin;
    }

    private void drawYScale(Graphics g) {
        double d = logMwMax;
        int i = topGridRow;
        if (standardsSet)
        {
            for (int j = 0; j <= gridRows; j++)
            {
                g.drawString(twoDigits.format(d), yAxisLabelX+ 5*charHalfWidth , yArray[i] + charHalfHeight);
                i++;
                d -= yDivision;
            }
        }
    }

    private void showExpMW(Graphics g) {
	DecimalFormat fiveDigits = new DecimalFormat( "00000." );

        if (showExperimentalMW)
            g.drawString("Experimental MW = " + String.valueOf( (int) experimentalMW ), xArray[leftGridCol], getSize().height / 8);
    } //showExpMW

    private boolean mouseOnXAxis(int i, int j) {
        byte b = 3;
        if (i < xArray[leftGridCol] || i > xArray[rightGridCol] || j < yArray[bottomGridRow] - b || j > yArray[bottomGridRow] + b)
            return false;
        xMouse = i;
        return true;
    } // mouseOnXAxis

    private void sumYsqs(double d) {
        sumYsq += d * d;
    } // sumYsqs

    private void calcStdCoords() {
        Point point = new Point(0, 0);
        for (int i = 0; i < numberOfStds; i++)
        {
            if (stds[i].selected)
            {
                point = calcLinePoint(stds[i].relativeMigration);
                stds[i].plotXPos = point.x;
                stds[i].plotYPos = point.y;
            }
        }
    } // calcStdCoords

    public void plotStandards(Graphics g) {
        byte b1 = 6;
        byte b2 = b1;
        int i = b2 / 2;
        int j = b1 / 2;
        new Point(0, 0);
        if (standardsSet)
        {
            for (int k = 0; k < numberOfStds; k++)
            {
                if (stds[k].selected)
                {
                    g.setColor(stds[k].color);
                    g.fillOval(stds[k].plotXPos - j, stds[k].plotYPos - i, b1, b2);
                    g.setColor(Color.black);
                    g.drawOval(stds[k].plotXPos - j, stds[k].plotYPos - i, b1, b2);
                }
            }
            g.drawLine(fitLineX1, fitLineY1, fitLineX2, fitLineY2);
            g.drawString("Slope = " + twoDigits.format( slope ), xArray[7], yArray[4]);
            g.drawString("y Intercept = " + twoDigits.format( yIntercept ), xArray[7], yArray[4] + charHeight);
            g.drawString("r = " + twoDigits.format( rCorr ), xArray[7], yArray[4] + charHeight * 2);
            g.drawString("r Squared = " + twoDigits.format( rCorrSq ), xArray[7], yArray[4] + charHeight * 3);
        }
    } // plotStandards

    private void drawAxes(Graphics g) {
	DecimalFormat oneDigit = new DecimalFormat( "0.0" );
        g.setColor(Color.black);
        g.drawString("Plot of Log MW as f(Relative Migration)", 10, charHeight);
        g.drawLine(xArray[leftGridCol], yArray[bottomGridRow], xArray[rightGridCol], yArray[bottomGridRow]);
        g.drawLine(xArray[leftGridCol], yArray[topGridRow], xArray[leftGridCol], yArray[bottomGridRow]);

        int i1 = leftGridCol;
        for (int j = 0; j <= gridCols; j += 2) {
            g.drawString(oneDigit.format(j/10.0), xArray[i1]-charWidth, xAxesLabelY);
	    i1 += 2;
        }

        g.drawString("Relative Migration", xArray[leftGridCol+2]+charHalfWidth, xAxesRMLabelY+charHalfHeight);
	String yLabel = "Log MW";
	i1 = yArray[ leftGridCol+5 ] - charHeight * ( yLabel.length() / 2 );  //get verticle center
	for (int j = 0; j < yLabel.length(); j++ ) {
	    g.drawString(yLabel.substring(j, j+1), yAxisLabelX + charWidth/4, i1 );
	    i1 += charHeight;
	}
        g.setColor(Color.lightGray);
        i1 = topGridRow;
        for (int k = 0; k < gridRows; k++)
        {
            g.drawLine(xArray[leftGridCol], yArray[i1], xArray[rightGridCol], yArray[i1]);
            i1++;
        }
        i1 = leftGridCol + 1;
        for (int i2 = 0; i2 < gridCols; i2++)
        {
            g.drawLine(xArray[i1], yArray[topGridRow], xArray[i1], yArray[bottomGridRow]);
            i1++;
        }
        g.setColor(Color.black);
    }

    private double calcLogMw(double d) {
        if (slope == 0.0)
            return 0.0;
        else
            return slope * d + yIntercept;
    }

    private void sumXs(double d)
    {
        sumX += d;
    }

    private void showLgMW(Graphics g)
    {
        if (showLogMW)
        {
            plotFont = new Font("Courier New", 0, 10);
            g.setFont(plotFont);
            g.drawString(twoDigits.format(logMw), xArray[leftGridCol]+charHalfWidth, userLineY);
        }
    }

    private void graphVertLine(Graphics g)
    {
        if (graphVerticalLine)
            g.drawLine(xPlot, yArray[bottomGridRow], xPlot, userLineY);
    }

    public void paint(Graphics g)
    {
        if (!imageCreated)
        {
            offScreenImage = createImage( getSize().width, getSize().height );
            font = getFont();
            fm = getFontMetrics(font);
            calcDimensions();
            if (standardsSet)
            {
                yConversion = deltaPixelY / deltaMw;
                calcStdCoords();
                calcLineCoords();
                imageCreated = true;
            }
        }
        offScreenGraphics = offScreenImage.getGraphics();
        offScreenGraphics.setColor(Color.white);
        offScreenGraphics.fillRect(0, 0, getSize().width, getSize().height);
        offScreenGraphics.setColor(Color.black);
        offScreenGraphics.drawRect(0, 0, getSize().width, getSize().height);
        offScreenGraphics.setColor(g.getColor());
        drawAxes(offScreenGraphics);
        drawYScale(offScreenGraphics);
        plotStandards(offScreenGraphics);
        displayRM(offScreenGraphics);
        plotUserRM(offScreenGraphics);
        showExpMW(offScreenGraphics);
        showSampMW(offScreenGraphics);
        showLgMW(offScreenGraphics);
        graphVertLine(offScreenGraphics);
        graphHorizLine(offScreenGraphics);
        showNotBracket(offScreenGraphics);
        g.drawImage(offScreenImage, 0, 0, this);
    }

    private void calcFit()
    {
        if (nPoints <= 1)
        {
            rCorr = 0.0;
            rCorrSq = 0.0;
            return;
        }
        double d1 = (double)nPoints * sumProd - sumX * sumY;
        double d2 = (double)nPoints * sumXsq - sumX * sumX;
        double d3 = (double)nPoints * sumYsq - sumY * sumY;
        d2 = Math.pow(d2, 0.5);
        d3 = Math.pow(d3, 0.5);
        rCorr = d1 / (d2 * d3);
        rCorrSq = rCorr * rCorr;
    }

    private void showSampMW(Graphics g)
    {
        if (showSampleMW)
        {
            String string = sample.abbr + " MW = " + String.valueOf(sample.mw);
            g.drawString(string, xArray[leftGridCol], getSize().height / 6);
            parent.displayProtein(sample);
            if (!harpPlayed)
            {
                //parent.playHarp();
                harpPlayed = true;
                return;
            }
        }
        else if (questionRCorr)
        {
            g.drawString("No match! RM was OK, poor line fit?", xArray[leftGridCol], getSize().height / 6);
            if (!    Played)
            {
                //parent.play    ();
                    Played = true;
            }
        }
    }

    public void setResults(Protein aprotein[], Protein protein1, Protein protein2)
    {
        stds = aprotein;
        sample = protein1;
        dye = protein2;
        resetSums();
        for (int i = 0; i < numberOfStds; i++)
        {
            if (stds[i].selected)
            {
                stds[i].relativeMigration = stds[i].GetDistance() / dye.GetDistance();
                sumXs(stds[i].relativeMigration);
                sumXsqs(stds[i].relativeMigration);
                double d = Math.log((double)stds[i].mw) / ln10;
                sumYs(d);
                sumYsqs(d);
                sumProds(stds[i].relativeMigration, d);
                nPoints++;
            }
        }
        calcLine();
        calcFit();
        sample.relativeMigration = sample.GetDistance() / dye.GetDistance();
        calcMaxMinLogs();
        standardsSet = true;
        graphVerticalLine = false;
        graphHorizontalLine = false;
        showExperimentalMW = false;
        showLogMW = false;
        showSampleMW = false;
        questionRCorr = false;
        imageCreated = false;
        repaint();
    }

    private void sumXsqs(double d)
    {
        sumXsq += d * d;
    }

    private void sumYs(double d)
    {
        sumY += d;
    }

    private void plotUserRM(Graphics g)
    {
        if (paintUserRM)
        {
            logMw = calcLogMw(plotRM);
            lineCoord = calcLinePoint(plotRM);
            if (newYLine)
            {
                newYLine = false;
                userLineY = yArray[bottomGridRow];
            }
            else if (userLineY >= lineCoord.y + 2)
                userLineY -= 2;
            graphVerticalLine = true;
            if (userLineY <= lineCoord.y + 2)
            {
                if (newXLine)
                {
                    newXLine = false;
                    userLineX = xPlot;
                }
                else
                    userLineX -= 2;
                graphHorizontalLine = true;
                if (userLineX <= xArray[leftGridCol])
                {
                    showLogMW = true;
                    experimentalMW = Math.pow(10.0, logMw);
                    showExperimentalMW = true;
                    double d1 = Math.abs(((double)sample.mw - experimentalMW) / sample.mw);
                    double d2 = Math.abs((sample.relativeMigration - plotRM) / sample.relativeMigration);
                    if (d1 < errorMargin)
                        showSampleMW = true;
                    else if (d2 < errorMargin)
                        questionRCorr = true;
                    stop();
                    resetFlags();
                    paintUserRM = false;
                    return;
                }
            }
            else if (userLineY <= lineCoord.y + 2)
            {
                showNotBracketed = true;
                stop();
                resetFlags();
                paintUserRM = false;
            }
        }
    }

    protected void calcDimensions()
    {
        rightEdge = getSize().width;
        xArray[0] = 0;
        division = rightEdge / cols;
        for (int i = 1; i < cols; i++)
            xArray[i] = xArray[i - 1] + division;
        bottomEdge = getSize().height;
        yArray[0] = 0;
        division = bottomEdge / rows;
        for (int j = 1; j < rows; j++)
            yArray[j] = yArray[j - 1] + division;
        deltaPixelX = xArray[rightGridCol] - xArray[leftGridCol];
        deltaPixelY = yArray[bottomGridRow] - yArray[topGridRow];
        font = getFont();
        fm = getFontMetrics(font);
        charWidth = fm.charWidth('0');
        charHalfWidth = charWidth / 2;
        charHalfHeight = fm.getAscent() / 2;
        charHeight = fm.getHeight();
        xAxesLabelY = yArray[bottomGridRow] + fm.getHeight();
        xAxesRMLabelY = xAxesLabelY + fm.getHeight() / 2;
        yAxesLabelY = yArray[topGridRow] - fm.getHeight() / 2;
    }

    private void displayRM(Graphics g)
    {
        if (paintRM)
        {
            mouseRM = (double)(xMouse - xArray[leftGridCol]) / deltaPixelX;
            //cursorPos = String.valueOf(mouseRM);
	    //            dotPos = cursorPos.indexOf(46);
            g.drawString(twoDigits.format(mouseRM), xArray[leftGridCol], yArray[bottomGridRow] + division);
	    //            g.drawString(cursorPos.substring(0, dotPos + 3), xArray[leftGridCol], yArray[bottomGridRow] + division);
        }
    }

    public boolean mouseDown(Event event, int i, int j)
    {
        //parent.playClick1();
        if (standardsSet)
        {
            for (int k = 0; k < numberOfStds; k++)
            {
                if (stds[k].matchPlotPosition(i, j))
                {
                    parent.displayProtein(stds[k]);
                    return true;
                }
            }
            if (mouseOnXAxis(i, j))
            {
                xPlot = xMouse;
                plotRM = mouseRM;
                paintUserRM = true;
                stopAnimation = false;
                showExperimentalMW = false;
                showSampleMW = false;
                questionRCorr = false;
                showLogMW = false;
                showNotBracketed = false;
                graphVerticalLine = false;
                graphHorizontalLine = false;
                start();
            }
        }
        return true;
    }

    private void sumProds(double d1, double d2)
    {
        sumProd += d1 * d2;
    }

    private void calcLineCoords()
    {
        Point point = new Point(0, 0);
        point = calcLinePoint(0.01);
        fitLineX1 = point.x;
        fitLineY1 = point.y;
        point = calcLinePoint(1.0);
        fitLineX2 = point.x;
        fitLineY2 = point.y;
    }

    public void run()
    {
        Thread.currentThread().setPriority(1);
        while (!stopAnimation)
        {
            try
            {
                Thread.sleep((long)pause);
            }
            catch (InterruptedException e)
            {
                System.out.println("thread interrupted");
            }
            repaint();
        }
    }

    private void graphHorizLine(Graphics g)
    {
        if (graphHorizontalLine)
            g.drawLine(xPlot, userLineY, userLineX, userLineY);
    }

    private void resetFlags()
    {
        stopAnimation = true;
        newYLine = true;
        newXLine = true;
        paintRM = false;
            Played = false;
        harpPlayed = false;
    }

    public boolean mouseMove(Event event, int i, int j)
    {
        if (standardsSet)
        {
            if (mouseOnXAxis(i, j))
                paintRM = true;
            else
                paintRM = false;
            repaint();
        }
        return true;
    }
}
