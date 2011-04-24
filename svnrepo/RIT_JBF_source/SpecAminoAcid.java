/*
 * This class represents the 20 different basic types of Amino Acids a protein
 * can possess. The instance variables assume that the amino acid in question
 * is fully protenated.
 *
 * version 3
 *  ** masses obtained from http:www.i-mass.com/guide/aamass.html on 1/5/2010
 */

/**
 *
 * @author Amanda Fisher
 */
public class SpecAminoAcid {

    private String name;
    private double mass;
    private int charge;
    int hits;

    /**
     * Constructor for SpecAminoAcid when given a char. Passes to translate method.
     *
     * @param code Symbol to be turned into an SpecAminoAcid.
     * @throws AminoException When given a symbol that doesn't represent an
     *                        amino acid.
     */
    public SpecAminoAcid(char code) throws AminoException {
            translate(code);
    }

    /**
     * Constructor for SpecAminoAcid when given a String instead of a char. Turns
     * input into a char and passes to translate method.
     *
     * @param input Symbol to be turned in to an SpecAminoAcid.
     * @throws AminoException When given a symbol that doesn't represent an
     *                        amino acid or given more than one symbol.
     */
    public SpecAminoAcid(String input) throws AminoException {
        if(input.length() > 1) {
            throw new AminoException("String input too long; must be single char.");
        } else {
            char[] charArray = input.toCharArray();
            char code = charArray[0];
            translate(code);
        }
    }

    /**
     * Method that does the heavy lifting. Takes a char and interprets it,
     * assigning the correct name, mass, and charge to the instance of
     * SpecAminoAcid based on the symbol given.
     *
     * @param code Symbol to be turned in to an SpecAminoAcid.
     * @throws AminoException
     */
    private void translate(char code) throws AminoException {
        switch(code) {
            case 'H':   setName("Histidiine");    setMass(138.14); setCharge(1); break;
            case 'K':   setName("Lysine");        setMass(129.17); setCharge(1); break;
            case 'R':   setName("Arginine");      setMass(157.19); setCharge(1); break;
            case 'E':   setName("Glutamate");     setMass(130.16); setCharge(0); break;
            case 'D':   setName("Aspartate");     setMass(115.09); setCharge(0); break;
            case 'P':   setName("Proline");       setMass(98.12);  setCharge(0); break;
            case 'V':   setName("Valine");        setMass(99.13);  setCharge(0); break;
            case 'M':   setName("Methionine");    setMass(131.20); setCharge(0); break;
            case 'S':   setName("Serine");        setMass(87.07);  setCharge(0); break;
            case 'Q':   setName("Glutamine");     setMass(128.13); setCharge(0); break;
            case 'N':   setName("Asparagine");    setMass(114.10); setCharge(0); break;
            case 'L':   setName("Leucine");       setMass(113.16); setCharge(0); break;
            case 'I':   setName("Isoleucine");    setMass(113.16); setCharge(0); break;
            case 'A':   setName("Alanine");       setMass(71.08);  setCharge(0); break;
            case 'G':   setName("Glycine");       setMass(57.05);  setCharge(0); break;
            case 'F':   setName("Phenylalanine"); setMass(147.18); setCharge(0); break;
            case 'Y':   setName("Tyrosine");      setMass(163.18); setCharge(0); break;
            case 'W':   setName("Tryptophan");    setMass(186.21); setCharge(0); break;
            case 'T':   setName("Threonine");     setMass(101.11); setCharge(0); break;
            case 'C':   setName("Cysteine");      setMass(103.14); setCharge(0); break;
            default:    throw new AminoException("Incorrect symbol for amino acid = "
                        + code);
        }
    }

    /**
     * Used only by SpecAminoAcid in translate. Sets the name variable.
     *
     * @param n Name.
     */
    private void setName(String n) {
        name = n;
    }

    /**
     * Used only by SpecAminoAcid in translate. Sets the mass variable.
     *
     * @param m Mass.
     */
    private void setMass(double m) {
        mass = m;
    }

    /**
     * Used only by SpecAminoAcid in translate. Sets the charge variable.
     *
     * @param c Charge.
     */
    private void setCharge(int c) {
        charge = c;
    }

    /**
     * Used by Spectrometer when determining the right intensity to use for
     * each peak in the TandemOutputGUI.
     *
     * @param h Hits.
     */
    public void setHits(int h) {
        hits = h;
    }

    /**
     * Used by Spectrometer to increase the charge of the SpecAminoAcid when it is
     * being used in the TandemOutputGUI (to make sure each SpecAminoAcid has a charge
     * of at least 1).
     */
    public void increaseCharge() {
        charge = charge + 1;
    }

    /**
     * Used to return the SpecAminoAcid's name.
     *
     * @return Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Used to return the SpecAminoAcid's mass.
     *
     * @return Mass.
     */
    public double getMass() {
        return mass;
    }

    /**
     * Used to return the SpecAminoAcid's charge.
     *
     * @return Charge.
     */
    public int getCharge() {
        return charge;
    }

    /**
     * Used to return the SpecAminoAcid's mass charge ratio when being used in
     * TandemOutputGUI.
     *
     * @return MassChargeRatio.
     */
    public double getMassChargeRatio() {
        return mass/(double)charge;
    }
}
