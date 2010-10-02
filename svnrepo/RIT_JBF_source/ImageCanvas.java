/*
 * ImageCanvas.java
 * Version: $Id:$
 * Revisions: $Log:$
 */

import java.awt.*;
import javax.swing.*;

/**
 * This class displays the process being run through a series of
 * threads.
 *
 * @author Kyle Dewey
 * @author John Manning
 * @author Kristen Cotton
 */
public class ImageCanvas extends JPanel implements Runnable {

    final int COLLOY = 25;
    final int COLHIY = 226;
    final int COLLOX = 280;
    final int COLHIX = 376;
    final int delay = 100;   //milliseconds between frames
    final int DETECTORIGINX = 6;
    final int DETECTORIGINY = 309;
    final int DETECTPEAK = 259;
    final int DETECTTOP = 275;

	Thread  m_animator;
    Image	m_offscreen;
    Graphics m_offgraphics;
	Image    m_imgBack;
    int		m_nTime = 0;
	Ionex	m_theExp;
	Point   m_pLastConcen;
	Point   m_pNewConcen;
	Point   m_pNewDetect;
	Point   m_pLastDetect;
	double	m_dTopConc;		// concentration entering column
	double	m_dBottomConc;	// concentration leaving column

	public ImageCanvas(Ionex  theExp)
	{
		m_theExp = theExp;
		m_pLastConcen = new Point(DETECTORIGINX, DETECTORIGINY);
		m_pLastDetect = new Point(DETECTORIGINX, DETECTORIGINY);
		m_pNewConcen = new Point(DETECTORIGINX, DETECTORIGINY);
		m_pNewDetect = new Point(DETECTORIGINX, DETECTORIGINY);
	}

    public void start()
	{
		if( m_animator == null){
			m_animator = new Thread(this);
		}
		m_animator.start();
    }

    public void pause() {
		m_animator = null;
    }

    public void stop() {
//		m_animator.stop();
		m_animator = null;

		//reinitialize
		m_nTime = 0;
		m_pLastConcen.move( DETECTORIGINX, DETECTORIGINY);
		m_pLastDetect.move( DETECTORIGINX, DETECTORIGINY);
		m_pNewConcen.move( DETECTORIGINX, DETECTORIGINY);
		m_pNewDetect.move( DETECTORIGINX, DETECTORIGINY);
    }

   public void run() {

	   //Just to be nice, lower this thread's priority
        //so it can't interfere with other processing going on.
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        //Remember the starting time.
        long startTime = System.currentTimeMillis();

        //This is the animation loop.
        while (Thread.currentThread() == m_animator) {
            //Advance the animation frame.
            animate();

            //Display it.
            repaint();

            //Delay depending on how far we are behind.
            try {
                startTime += delay;
                Thread.sleep(Math.max(0,
                                      startTime-System.currentTimeMillis()));
            } catch (InterruptedException e) {
                break;
            }
        }
    }

	public void paint(Graphics g)
	{
		update( g);
	}

	public synchronized void update(Graphics g)
	{
		//get the background image
		if (m_offscreen == null){
			m_offscreen = createImage( 488, 395);
			m_offgraphics = m_offscreen.getGraphics();

			// reset the background image
			m_offgraphics.drawImage( m_imgBack, 0, 0, null);
		}

		//now draw the protein bands
		drawProteins( m_offgraphics);

		//now draw the detector
		m_offgraphics.setColor( Color.blue);
		m_offgraphics.drawLine( m_pLastDetect.x, m_pLastDetect.y,
								m_pNewDetect.x, m_pNewDetect.y);

		//now draw the concentration graph
		m_offgraphics.setColor( Color.red);
		m_offgraphics.drawLine( m_pLastConcen.x, m_pLastConcen.y,
								m_pNewConcen.x, m_pNewConcen.y);

		// now draw the concentrations at the top and bottom of the column
		m_offgraphics.setColor( Color.white);
		m_offgraphics.fillRect( COLHIX + 5, COLLOY, 50, 10);
		m_offgraphics.fillRect( COLHIX + 5, COLHIY - 10, 50, 10);

		String strConc = new String( String.valueOf( m_dTopConc));
		m_offgraphics.setColor( Color.gray);
		m_offgraphics.drawString( formatFloat( strConc), COLHIX + 5, COLLOY + 10);

		strConc = String.valueOf( m_dBottomConc);
		m_offgraphics.drawString( formatFloat( strConc), COLHIX + 5, COLHIY);

		//now actually draw to the screen
		g.drawImage(m_offscreen, 0, 0, null);
	}

	private String formatFloat( String strF)
	{
		String	str;

		int nPos = strF.indexOf( '.');
		if(( nPos < 0) || ( nPos + 3 > strF.length())){
			str = new String( strF);
		}
		else{
			str = new String( strF.substring( 0, nPos + 3));
		}

		return str;

	}

	public void resetBackground()
	{
		// reset the background image
		m_offgraphics.drawImage( m_imgBack, 0, 0, null);

		//call repaint to draw to the screen
		repaint();
	}

	public void prepareBackground()
	{
		// reset the background image
		m_offgraphics.drawImage( m_imgBack, 0, 0, null);

		//redraw the names of the proteins
		for( int i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}

			if( m_theExp.m_arrProteins[i].m_bMix){
				//use the color for mixed proteins
				m_offgraphics.setColor( m_theExp.m_colors[5]);
			}
			else{
				m_offgraphics.setColor( m_theExp.m_colors[i]);
			}

			m_offgraphics.drawString( m_theExp.m_arrProteins[i].GetName(), 10, 344 + (i * 11));
		}

		//call repaint to draw to the screen
		repaint();
	}

	public void animate(){

		m_nTime++;

		if( m_nTime >= 460){
			// we're done
			m_theExp.processStop();
			return;
		}

		//move the proteins in the column
		moveProteins();

		//move the concentration graph point
		m_pLastConcen.move( m_pNewConcen.x, m_pNewConcen.y);
		calcConc();

		//move the detector graph point
		m_pLastDetect.move( m_pNewDetect.x, m_pNewDetect.y);
		calcDetect();

		repaint();
	}

    void moveProteins() {
	double		dConc;			// concentration at protein location
	CProtein	protein;
	int			i;

	//loop through the proteins and determine their positions
	for( i = 0; i < m_theExp.m_arrProteins.length; i++){
	    if( m_theExp.m_arrProteins[i] == null){
		continue;
	    }
	    
	    protein = m_theExp.m_arrProteins[i];
	    
	    if( protein.m_bBound){

		// find the concentration at the location of the protein
		if( m_nTime <= (150 + protein.m_nPos)){
		    // the time for the initial wash to move through the column
		    dConc = m_theExp.m_dConc1;
		}
		else{
		    if( m_nTime <= (300 + protein.m_nPos)){
			// concentration entering column changes
			dConc = m_theExp.m_dConc1 +
			    (float)((( m_theExp.m_dConc2 - m_theExp.m_dConc1)/150) *
				    (m_nTime - 150 - protein.m_nPos));
		    }
		    else{
			// only the high concentration now
			dConc = m_theExp.m_dConc2;
		    }
		}
		
		// determine if the protein is still bound or not
		if( Math.abs( protein.m_dCharge) < (dConc * 100)){
		    protein.m_bBound = false;
		}
	    }
	    
	    // protein is not bound, move it
	    if( !protein.m_bBound){
		(m_theExp.m_arrProteins[i]).moveProtein();
	    }
	}
    }

    void calcConc() {
	//find the concentration of the solvent entering and leaving the column
	if( m_nTime <= 150){
	    m_dTopConc = m_theExp.m_dConc1;
	    m_dBottomConc = m_theExp.m_dConc1;
	}
	else{
	    if( m_nTime <= 300){
		// concentration entering column changes
		// the time for the initial wash to move through the column
		m_dTopConc = (m_theExp.m_dConc1 +
			      (float)((( m_theExp.m_dConc2 - m_theExp.m_dConc1)/150) * (m_nTime - 150)));
		m_dBottomConc = m_theExp.m_dConc1;
	    }
	    else{
		//only final wash entering column
		if( m_nTime <= 450){
		    m_dTopConc = m_theExp.m_dConc2;
		    m_dBottomConc = (m_theExp.m_dConc1 +
				     (float)((( m_theExp.m_dConc2 - m_theExp.m_dConc1)/150) * (m_nTime - 300)));
		}
		else{
		    // only the high concentration now
		    m_dTopConc = m_theExp.m_dConc2;
		    m_dBottomConc = m_theExp.m_dConc2;
		}
	    }
	}
	
	//move the graph point
	m_pNewConcen.move( DETECTORIGINX + m_nTime,
			   DETECTORIGINY - 1 - (int)(m_dBottomConc * (DETECTORIGINY - DETECTTOP)));
	
    }

	void calcDetect()
	{
		int		i;
		int		nPos;
		int     nMaxAmount = 0, nMixAmount = 0;
		int		nAmount;
		int		nHeight;
		int		nNewPoint;

		//move the x
		m_pNewDetect.move( DETECTORIGINX + m_nTime, DETECTORIGINY);

		// loop through the proteins, to find the largest amount entered
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}
			nMaxAmount = Math.max( nMaxAmount, m_theExp.m_arrProteins[i].m_nAmount);
		}

		//now see if there are any that are being eluted together and calculate that amount
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}

			if( m_theExp.m_arrProteins[i].m_bMix){
				nMixAmount += m_theExp.m_arrProteins[i].m_nAmount;
			}
		}

		nMaxAmount = Math.max( nMaxAmount, nMixAmount);

		// loop through the proteins, see if there's any near the bottom
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}

			//calculate the detector position for this proteins peak
			nHeight = DETECTORIGINY - DETECTPEAK;

			if( m_theExp.m_arrProteins[i].m_bMix){
				nAmount = nMixAmount;
			}
			else{
				nAmount = m_theExp.m_arrProteins[i].m_nAmount;
			}
			nHeight = (int)(nHeight * (float)((float)nAmount/(float)nMaxAmount)); //force it to use floats!

			nPos = m_theExp.m_arrProteins[i].m_nPos;

			switch( Math.abs(( COLHIY - COLLOY + 2) - ( nPos +1))){
			case 2:
				nHeight = (int)(nHeight * (4.0/7.0));
				break;

			case 1:
				nHeight = (int)(nHeight * (6.0/7.0));
				break;

			case 0:
				break;

			default:
				nHeight = 0;
			}

			nNewPoint = DETECTORIGINY - nHeight;

			// if the point is already set, only set it if
			// the new setting shows a higher detector response
			if( m_pNewDetect.y > nNewPoint){
				m_pNewDetect.y = nNewPoint;
			}
		}
	}


	public void setImage( Image img)
	{
		m_imgBack = img;
	}

	void drawProteins( Graphics g)
	{
		int	i;

		//erase the column
		g.setColor( Color.white);
		g.fillRect( COLLOX + 1, COLLOY + 1, COLHIX - COLLOX - 1, COLHIY - COLLOY - 1);

		//draw bound proteins first
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}
			if( m_theExp.m_arrProteins[i].m_bBound){
				drawProteinBand( g, i);
			}
		}

		// now draw mobil ones
		for( i = 0; i < m_theExp.m_arrProteins.length; i++){
			if( m_theExp.m_arrProteins[i] == null){
				continue;
			}
			if( !m_theExp.m_arrProteins[i].m_bBound){
				drawProteinBand( g, i);
			}
		}
	}

	void drawProteinBand( Graphics g, int nProtein)
	{
		Rectangle	rect;
		int			nPos, nWidth;	//placement and width of protein band

		nPos = m_theExp.m_arrProteins[ nProtein].m_nPos;
		nWidth = m_theExp.m_arrProteins[ nProtein].m_nBandwidth;

		//draw the protein if its still in the column
		if( nPos >= (COLHIY - COLLOY)){
			return;
		}

		rect = new Rectangle( COLLOX + 1, COLLOY + nPos,
							  COLHIX - COLLOX - 1, nWidth);

		// don't draw anything outside of the column
		if(( rect.y + rect.height) >= COLHIY){
			rect.height = COLHIY - rect.y;
		}

		// draw the protein with the correct color
		if( m_theExp.m_arrProteins[ nProtein].m_bMix){
			g.setColor( m_theExp.m_colors[5]);
		}
		else{
			g.setColor( m_theExp.m_colors[nProtein]);
		}

		g.fillRect( rect.x, rect.y, rect.width, rect.height);
	}

}
