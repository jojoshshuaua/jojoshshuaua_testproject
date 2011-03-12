/*
 * AmbiguousAminoAcid.java
 */

/**
 * Represents an ambiguous amino acid.
 * Such amino acids are merely placeholders, and have no charge.
 * @author Kyle Dewey
 */
public class AmbiguousAminoAcid extends AminoAcid {
    protected AmbiguousAminoAcid( String oneLetterCode,
				  String threeLetterCode ) {
	super( oneLetterCode,
	       threeLetterCode );
    }
    public double getCharge( CHARGE charge,
			     double pka,
			     double ph ) {
	    return 0.0;
    }
}
