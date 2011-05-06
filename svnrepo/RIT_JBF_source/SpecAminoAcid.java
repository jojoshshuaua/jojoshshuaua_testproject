/*
 * This class represents the 20 different basic types of Amino Acids a protein
 * can possess. The instance variables assume that the amino acid in question
 * is fully protenated.
 *
 * version 3
 *  ** base masses of amino acids in daltons obtained from
 *     http://www.encorbio.com/protocols/Prot-MW.htm on 5/4/2011. Amino acids
 *     with an extra charge were given an extra 1.00774 Da for hydrogen
 *     presence.
 *  ** pKa's obtained from http://www.cem.msu.edu/~cem252/sp97/ch24/ch24aa.html on 5/3/2011
 */

/**
 *
 * @author Amanda Fisher
 */
public class SpecAminoAcid {

    private String name;
    private double mass;
    private double COOHpKa;
    private double NHHpKa;
    private double sidechainPKa;
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
     * assigning the correct name, mass in Daltons, and charge to the instance
     * of SpecAminoAcid based on the symbol given.
     *
     * @param code Symbol to be turned in to an SpecAminoAcid.
     * @throws AminoException
     */
    private void translate(char code) throws AminoException {
        switch(code) { // adding hydrogens as I go
            case 'H':   setName("Histidine");     setMass(157.17191); setCharge(2); setCOOHpKa(1.77); setNHHpKa(9.18);   setSidechainPKa(6.10);  break;
            case 'K':   setName("Lysine");        setMass(148.20794); setCharge(2); setCOOHpKa(2.18); setNHHpKa(8.95);   setSidechainPKa(10.53); break;
            case 'R':   setName("Arginine");      setMass(176.21794); setCharge(2); setCOOHpKa(2.01); setNHHpKa(9.04);   setSidechainPKa(12.48); break;
            case 'E':   setName("Glutamate");     setMass(148.13578); setCharge(1); setCOOHpKa(2.10); setNHHpKa(9.46);   setSidechainPKa(4.07);  break;
            case 'D':   setName("Aspartate");     setMass(134.10578); setCharge(1); setCOOHpKa(2.10); setNHHpKa(9.82);   setSidechainPKa(3.86);  break;
            case 'P':   setName("Proline");       setMass(116.13794); setCharge(1); setCOOHpKa(2.00); setNHHpKa(10.60);  setSidechainPKa(-1);    break;
            case 'V':   setName("Valine");        setMass(118.15794); setCharge(1); setCOOHpKa(2.29); setNHHpKa(9.72);   setSidechainPKa(-1);    break;
            case 'M':   setName("Methionine");    setMass(150.21794); setCharge(1); setCOOHpKa(2.28); setNHHpKa(9.21);   setSidechainPKa(-1);    break;
            case 'S':   setName("Serine");        setMass(106.09794); setCharge(1); setCOOHpKa(2.21); setNHHpKa(9.15);   setSidechainPKa(-1);    break;
            case 'Q':   setName("Glutamine");     setMass(147.14794); setCharge(1); setCOOHpKa(2.17); setNHHpKa(9.13);   setSidechainPKa(-1);    break;
            case 'N':   setName("Asparagine");    setMass(133.12794); setCharge(1); setCOOHpKa(2.02); setNHHpKa(8.80);   setSidechainPKa(-1);    break;
            case 'L':   setName("Leucine");       setMass(132.17794); setCharge(1); setCOOHpKa(2.33); setNHHpKa(9.74);   setSidechainPKa(-1);    break;
            case 'I':   setName("Isoleucine");    setMass(132.17794); setCharge(1); setCOOHpKa(2.32); setNHHpKa(9.76);   setSidechainPKa(-1);    break;
            case 'A':   setName("Alanine");       setMass(90.09794);  setCharge(1); setCOOHpKa(2.35); setNHHpKa(9.87);   setSidechainPKa(-1);    break;
            case 'G':   setName("Glycine");       setMass(76.07794);  setCharge(1); setCOOHpKa(2.35); setNHHpKa(9.78);   setSidechainPKa(-1);    break;
            case 'F':   setName("Phenylalanine"); setMass(166.19794); setCharge(1); setCOOHpKa(2.58); setNHHpKa(9.24);   setSidechainPKa(-1);    break;
            case 'Y':   setName("Tyrosine");      setMass(182.19794); setCharge(1); setCOOHpKa(2.20); setNHHpKa(9.11);   setSidechainPKa(10.07); break;
            case 'W':   setName("Tryptophan");    setMass(205.23794); setCharge(1); setCOOHpKa(2.38); setNHHpKa(9.39);   setSidechainPKa(-1);    break;
            case 'T':   setName("Threonine");     setMass(120.12794); setCharge(1); setCOOHpKa(2.09); setNHHpKa(9.10);   setSidechainPKa(-1);    break;
            case 'C':   setName("Cysteine");      setMass(122.16794); setCharge(1); setCOOHpKa(2.05); setNHHpKa(10.25);  setSidechainPKa(8.00);  break;
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
     * Used by the translate method to set the alpha-Carboxylic Acid group's pKa
     * value for the amino acid.
     *
     * @param k The pKa value.
     */
    private void setCOOHpKa(double k) {
        COOHpKa = k;
    }

    /**
     * Used by teh translate method to set the alpha-Amino grou's pKa value for
     * the amino acid.
     *  
     * @param k The pKa value.
     */
    private void setNHHpKa(double k) {
        NHHpKa = k;
    }

    /**
     * Used by the translate method to set the sidechain pKa value for the amino
     * acid if it has one. If it does not, a value of -1 is substituted instead.
     *
     * @param k The pKa value, or the non-value.
     */
    private void setSidechainPKa(double k) {
        sidechainPKa = k;
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

    /**
     * Used to return the alpha-carboxylic acid pKa of the amino acid.
     * (Not used in the spectrometer simulation, but put in for making
     * SpecAminoAcid more complete)
     *
     * @return COOHpKa
     */
    public double getCOOHpKa() {
        return COOHpKa;
    }

    /**
     * Used to return the alpha-amino pKa of the amino acid.
     * (Not used in the spectrometer simulation, but put in for making
     * SpecAminoAcid more complete)
     *
     * @return NHHpKa
     */
    public double getNHHpKa() {
        return NHHpKa;
    }

    /**
     * Used to return the amino acid's sidechain pKa value. If the amino acid
     * does not have a sidechain pKa, then a value of -1 is returned instead.
     * (Not used in the spectrometer simulation, but put in for making
     * SpecAminoAcid more complete)
     * 
     * @return sidechainPKa
     */
    public double getSidechainPKa() {
        return sidechainPKa;
    }

}
