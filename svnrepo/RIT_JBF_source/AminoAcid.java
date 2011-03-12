/*
 * AminoAcid.java
 */

import java.util.*;

/**
 * Represents an amino acid.  Contains information about the pKa, and 
 * methods to calculate its charge at a given pH.  Note that amino
 * acids are singletons, and that they are intended to be used in the
 * flyweight design pattern.
 *
 * @author Kyle Dewey
 */
public class AminoAcid {
    // begin constants
    public static final int NUM_AMINO_ACIDS = 20;
    public static final double NO_CHARGE = Double.NaN;
    public enum CHARGE { POSITIVE, NEGATIVE, UNCHARGED, UNKNOWN };
    public enum AMINO_ACID_TYPE { ARG, // arginine
    	    HIS, // histadine
	    LYS, // lysine
	    ASP, // aspartic acid
	    GLU, // glutamic acid
	    SER, // serine
	    THR, // threonine
	    ASN, // asparagine
	    GLN, // glutamine
	    CYS, // cysteine
	    GLY, // glycine
	    PRO, // proline
	    ALA, // alanine
	    ILE, // isoleucine
	    LEU, // leucine
	    MET, // methionine
	    PHE, // phenylalanine
	    TRP, // tryptophan
	    TYR, // tyrosine
	    VAL, // valine
	    ASX, // asparagine or aspartic acid
	    GLX, // glutamine or glutamic acid
	    XLE, // leucine or isoleucine
	    UNK // unknown; note that Xaa is also appropriate
	    };

    private static final Map< String, AMINO_ACID_TYPE > codes =
	new HashMap< String, AMINO_ACID_TYPE >( NUM_AMINO_ACIDS ) {
	{
	    put( "ARG", AMINO_ACID_TYPE.ARG );
	    put( "R", AMINO_ACID_TYPE.ARG );
	    put( "HIS", AMINO_ACID_TYPE.HIS );
	    put( "H", AMINO_ACID_TYPE.HIS );
	    put( "LYS", AMINO_ACID_TYPE.LYS );
	    put( "K", AMINO_ACID_TYPE.LYS );
	    put( "ASP", AMINO_ACID_TYPE.ASP );
	    put( "D", AMINO_ACID_TYPE.ASP );
	    put( "GLU", AMINO_ACID_TYPE.GLU );
	    put( "E", AMINO_ACID_TYPE.GLU );
	    put( "SER", AMINO_ACID_TYPE.SER );
	    put( "S", AMINO_ACID_TYPE.SER );
	    put( "THR", AMINO_ACID_TYPE.THR );
	    put( "T", AMINO_ACID_TYPE.THR );
	    put( "ASN", AMINO_ACID_TYPE.ASN );
	    put( "N", AMINO_ACID_TYPE.ASN );
	    put( "GLN", AMINO_ACID_TYPE.GLN );
	    put( "Q", AMINO_ACID_TYPE.GLN );
	    put( "CYS", AMINO_ACID_TYPE.CYS );
	    put( "C", AMINO_ACID_TYPE.CYS );
	    put( "GLY", AMINO_ACID_TYPE.GLY );
	    put( "G", AMINO_ACID_TYPE.GLY );
	    put( "PRO", AMINO_ACID_TYPE.PRO );
	    put( "P", AMINO_ACID_TYPE.PRO );
	    put( "ALA", AMINO_ACID_TYPE.ALA );
	    put( "A", AMINO_ACID_TYPE.ALA );
	    put( "ILE", AMINO_ACID_TYPE.ILE );
	    put( "I", AMINO_ACID_TYPE.ILE );
	    put( "LEU", AMINO_ACID_TYPE.LEU );
	    put( "L", AMINO_ACID_TYPE.LEU );
	    put( "MET", AMINO_ACID_TYPE.MET );
	    put( "M", AMINO_ACID_TYPE.MET );
	    put( "PHE", AMINO_ACID_TYPE.PHE );
	    put( "F", AMINO_ACID_TYPE.PHE );
	    put( "TRP", AMINO_ACID_TYPE.TRP );
	    put( "W", AMINO_ACID_TYPE.TRP );
	    put( "TYR", AMINO_ACID_TYPE.TYR );
	    put( "Y", AMINO_ACID_TYPE.TYR );
	    put( "VAL", AMINO_ACID_TYPE.VAL );
	    put( "V", AMINO_ACID_TYPE.VAL );
	    put( "ASX", AMINO_ACID_TYPE.ASX );
	    put( "B", AMINO_ACID_TYPE.ASX );
	    put( "GLX", AMINO_ACID_TYPE.GLX );
	    put( "Z", AMINO_ACID_TYPE.GLX );
	    put( "XLE", AMINO_ACID_TYPE.XLE );
	    put( "J", AMINO_ACID_TYPE.XLE );
	    put( "XAA", AMINO_ACID_TYPE.UNK );
	    put( "UNK", AMINO_ACID_TYPE.UNK );
	    put( "X", AMINO_ACID_TYPE.UNK );
	}
    };

    private static final Map< AMINO_ACID_TYPE, AminoAcid > aminoAcids = 
	new HashMap< AMINO_ACID_TYPE, AminoAcid >( NUM_AMINO_ACIDS ) {
	{
	    put( AMINO_ACID_TYPE.ARG, 
		 new AminoAcid( "R", "Arg", CHARGE.POSITIVE, 
				12.10, 9.00, 2.03 ) );
	    put( AMINO_ACID_TYPE.HIS, 
		 new AminoAcid( "H", "His", CHARGE.POSITIVE, 
				6.04, 9.09, 1.70 ) );
	    put( AMINO_ACID_TYPE.LYS, 
		 new AminoAcid( "K", "Lys", CHARGE.POSITIVE, 
				10.67, 9.16, 2.15 ) );
	    put( AMINO_ACID_TYPE.ASP, 
		 new AminoAcid( "D", "Asp", CHARGE.NEGATIVE, 
				3.71, 9.66, 1.95 ) );
	    put( AMINO_ACID_TYPE.GLU, 
		 new AminoAcid( "E", "Glu", CHARGE.NEGATIVE, 
				4.15, 9.58, 2.16 ) );
	    put( AMINO_ACID_TYPE.SER, 
		 new AminoAcid( "S", "Ser", 9.05, 2.13 ) );
	    put( AMINO_ACID_TYPE.THR, 
		 new AminoAcid( "T", "Thr", 8.96, 2.20 ) );
	    put( AMINO_ACID_TYPE.ASN, 
		 new AminoAcid( "N", "Asn", 8.76, 2.16 ) );
	    put( AMINO_ACID_TYPE.GLN, 
		 new AminoAcid( "Q", "Gln", 9.00, 2.18 ) );
	    put( AMINO_ACID_TYPE.CYS, 
		 new AminoAcid( "C", "Cys", 10.28, 1.91 ) );
	    put( AMINO_ACID_TYPE.GLY, 
		 new AminoAcid( "G", "Gly", 9.58, 2.34 ) );
	    put( AMINO_ACID_TYPE.PRO, 
		 new AminoAcid( "P", "Pro", 10.47, 1.95 ) );
	    put( AMINO_ACID_TYPE.ALA, 
		 new AminoAcid( "A", "Ala", 9.71, 2.33 ) );
	    put( AMINO_ACID_TYPE.ILE, 
		 new AminoAcid( "I", "Ile", 9.60, 2.26 ) );
	    put( AMINO_ACID_TYPE.LEU, 
		 new AminoAcid( "L", "Leu", 9.58, 2.32 ) );
	    put( AMINO_ACID_TYPE.MET, 
		 new AminoAcid( "M", "Met", 9.08, 2.16 ) );
	    put( AMINO_ACID_TYPE.PHE, 
		 new AminoAcid( "F", "Phe", 9.09, 2.18 ) );
	    put( AMINO_ACID_TYPE.TRP, 
		 new AminoAcid( "W", "Trp", 9.34, 2.38 ) );
	    put( AMINO_ACID_TYPE.TYR, 
		 new AminoAcid( "Y", "Tyr", 9.04, 2.24 ) );
	    put( AMINO_ACID_TYPE.VAL, 
		 new AminoAcid( "V", "Val", 9.52, 2.27 ) );
	    put( AMINO_ACID_TYPE.ASX,
		 new AmbiguousAminoAcid( "B", "Asx" ) );
	    put( AMINO_ACID_TYPE.GLX,
		 new AmbiguousAminoAcid( "Z", "Glx" ) );
	    put( AMINO_ACID_TYPE.XLE,
		 new AmbiguousAminoAcid( "J", "Xle" ) );
	    put( AMINO_ACID_TYPE.UNK,
		 new AmbiguousAminoAcid( "X", "Unk" ) );
	}
    };
    // end constants

    // begin instance variables
    public final String oneLetterCode;
    public final String threeLetterCode;
    public final CHARGE charge;
    public final double sideChainPKa; // if charged; NaN if it's not
    public final double carboxylPKa; // applies to all
    public final double aminoPKa; // applies to all
    // end instance variables

    /**
     * Dud constructor.
     * Intended for ambiguous amino acids.
     */
    protected AminoAcid( String oneLetterCode,
			 String threeLetterCode ) {
	this( oneLetterCode,
	      threeLetterCode,
	      CHARGE.UNKNOWN,
	      0.0,
	      0.0,
	      0.0 );
    }

    /**
     * Creates a new Amino Acid with a given pKa.  Note that it is hidden
     * to force calling code to use the method to get singletons.
     * 
     * @param oneLetterCode The single letter code
     * @param threeLetterCode The three letter code
     * @param charge The charge of the amino acid
     * @param sideChainPKa The pKa of the side chain.  Use NO_CHARGE 
     * for uncharged side chains
     * @param aminoPKa The pKa of the NH2
     * @param carboxylPKa The pKa of the COOH
     */
    private AminoAcid( String oneLetterCode,
		       String threeLetterCode,
		       CHARGE charge,
		       double sideChainPKa,
		       double aminoPKa,
		       double carboxylPKa ) {
	this.oneLetterCode = oneLetterCode;
	this.threeLetterCode = threeLetterCode;
	this.charge = charge;
	this.sideChainPKa = sideChainPKa;
	this.aminoPKa = aminoPKa;
	this.carboxylPKa = carboxylPKa;
    }

    /**
     * Creates a new uncharged amino acid.
     *
     * @param oneLetterCode Single letter amino acid code
     * @param threeLetterCode Three letter amino acid code
     * @param aminoPKa The PKa of the NH2
     * @param carboxylPKa The pKa of the COOH
     */
    private AminoAcid( String oneLetterCode,
		       String threeLetterCode,
		       double aminoPKa,
		       double carboxylPKa ) {
	this( oneLetterCode,
	      threeLetterCode,
	      CHARGE.UNCHARGED,
	      NO_CHARGE,
	      aminoPKa,
	      carboxylPKa );
    }

    /**
     * Gets the amino acid with the given AMINO_ACID_TYPE.
     *
     * @param type The type of amino acid to get
     * @return The amino acid with the given type
     */
    public static AminoAcid getAminoAcid( AMINO_ACID_TYPE type ) {
	return aminoAcids.get( type );
    }

    /**
     * Gets the amino acid with the given three letter or one letter code
     * @param code The three-letter code for the amino acid.  Case insensitive.
     * @return The amino acid with the given code, or null if there is none
     */
    public static AminoAcid getAminoAcid( String code ) {
	AminoAcid retval = null;
	code = code.toUpperCase();
	if ( codes.containsKey( code ) ) {
	    retval = getAminoAcid( codes.get( code ) );
	}
	return retval;
    }

    /**
     * Given whether or not the given group is charged, the pKa of the group,
     * and the pH of the solution the group is in, it returns the charge of
     * the group.
     * @pre The pH is between 0 and 14
     * @param charge The charge of the group.
     * @param pKa The pKa of the group
     * @param pH The pH of the solution the group is in
     * @return The charge of the group, or 0.0 if the charge was neither 
     * positive nor negative
     */
    public static double getChargeStatic( CHARGE charge,
					  double pKa,
					  double pH ) {
	double retval = 0.0;

	if ( charge == CHARGE.POSITIVE ||
	     charge == CHARGE.NEGATIVE ) {
	    double antilog = Math.pow( 10.0, pH - pKa );
	    if ( charge == CHARGE.NEGATIVE ) {
		retval = -(antilog / ( 1.0 + antilog ) );
	    } else {
		retval = 1.0 / ( 1.0 + antilog );
	    }
	}

	return retval;
    }
    
    public double getCharge( CHARGE charge,
			     double pKa,
			     double pH ) {
	return getChargeStatic( charge,
				pKa,
				pH );
    }

    /**
     * Gets the charge of the amino acid's side chain.
     * Merely passes the side chain information to getCharge()
     */
    public double getSideChainCharge( double pH ) {
	return getCharge( charge,
			  sideChainPKa,
			  pH );
    }

    /**
     * Gets the charge of the amino terminus.
     * Passes off to getCharge()
     */
    public double getAminoCharge( double pH ) {
	return getCharge( CHARGE.POSITIVE,
			  aminoPKa,
			  pH );
    }

    /**
     * Gets the charge of the carboxyl terminus.
     * Passes off to getCharge()
     */
    public double getCarboxylCharge( double pH ) {
	return getCharge( CHARGE.NEGATIVE,
			  carboxylPKa,
			  pH );
    }
}
