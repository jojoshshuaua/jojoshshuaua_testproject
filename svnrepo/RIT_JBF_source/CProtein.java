/*
 * CProtein.java
 * Version: $Id:$
 * Revisions: $Log:$
 */

import java.io.*;
import java.net.*;

/**
 * This class deals with the protein files that will be loaded into
 * ionex.
 *
 * @author John Manning
 * @author Kristen Cotton
 */
public class CProtein {

    String		m_strName;		// string containing compound information
    String		m_strFile;		// the file name of the protein
	int			m_nPos;			// position within the column
	double      m_dPH;			// the pH of the system
	boolean		m_bBound;		// whether or not the protein is bound
	int			m_nAmount;		// amount of protein added to the column
	int			m_nBandwidth;	// width of the protein band
	boolean		m_bMix;			// indicates that this protein is mixed with others
	double		m_dCharge;
	Ionex		m_applet;
	static final int	ASP = 0;
	static final int	GLU = 1;
	static final int	TYR = 2;
	static final int	CYS = 3;
	static final int	LYS = 4;
	static final int	ARG = 5;
	static final int	HIS = 6;
	static final int	CTERM = 7;
	static final int   NTERM = 8;
	int			m_arrChargedAminos[] = {0,0,0,0,0,0,0,1,1};
	double      m_arrAminoPK[] = {4.4, 4.4, 10.0, 8.5, 10.0, 12.0, 6.5, 3.1, 8.0};

	public CProtein( String strName, int nAmount, String strFile, double dPH, Ionex applet)
	{
		m_strName = strName;
		m_nAmount = nAmount;
		m_strFile = strFile;
		m_dPH = dPH;
		m_applet = applet;
	}

	//reads in the protein information from the file specified
	public boolean load( )
	{
		URL					url;
		InputStream			in;
		DataInputStream		dis;
		String				strLine = null;
		String				strTag;

         try{
			url = new URL( "pdb/" + m_strFile);
			in = url.openStream();
            dis = new DataInputStream(in);

			//read in the file line by line and get the info we need
            while(( strLine = dis.readLine()) != null){
				strTag = new String( strLine.substring( 0, 6));
				if( strTag.equals("ATOM  ")){
					// there is an entry for each atom in the protein
					strTag = new String( strLine.substring( 11, 15));
					// we're only interested in alpha-carbons, tagged CA
					if( strTag.equals( "  CA")){
						//we need to know what type of Amino it is
						strTag = new String( strLine.substring( 17, 20));

						//we need to know the number of each of the charged Aminos
						if( strTag.equals("ASP")) m_arrChargedAminos[ASP]++;
						if( strTag.equals("GLU")) m_arrChargedAminos[GLU]++;
						if( strTag.equals("TYR")) m_arrChargedAminos[TYR]++;
						if( strTag.equals("CYS")) m_arrChargedAminos[CYS]++;
						if( strTag.equals("LYS")) m_arrChargedAminos[LYS]++;
						if( strTag.equals("ARG")) m_arrChargedAminos[ARG]++;
						if( strTag.equals("HIS")) m_arrChargedAminos[HIS]++;
					}
				}
            }
            in.close();
         }
		catch( FileNotFoundException e) {}
		catch( IOException e) {}
		catch( SecurityException e) {}

		//initialize the charge
		getCharge( m_dPH);


		return true;
	}

	public double getCharge( double dPH)
	{
		double	dAntilog;
		double  dCharge;

		m_dCharge = 0;

		for( int i = 0; i < 9; i++){
			dAntilog = Math.pow( 10.0, (dPH - m_arrAminoPK[i]));

			switch( i){
				// acidic aminos
			case ASP:
			case GLU:
			case CTERM:
			case TYR:
			case CYS:
				dCharge = -(dAntilog / ( 1.0 + dAntilog));
				break;

				// basic aminos
			case HIS:
			case NTERM:
			case LYS:
			case ARG:
				dCharge = 1.0 / (1.0 + dAntilog);
				break;

				// anything else
			default:
				dCharge = 0;
			}

			m_dCharge += m_arrChargedAminos[i] * dCharge;
		}

		return m_dCharge;
	}

	public double GetCharge( double dPH)
	{
		double dCharge = 0.0;

		return dCharge;
	}

	public String GetName()
	{
		return m_strName;
	}

	public int GetAmount()
	{
		return m_nAmount;
	}

	public void SetAmount( int nAmount)
	{
		m_nAmount = nAmount;
	}

		public String GetFile()
	{
		return m_strFile;
	}

	void moveProtein()
	{
		m_nPos += 1;

		if( m_nBandwidth > 3){
			m_nBandwidth -= 1;
		}
	}

}
