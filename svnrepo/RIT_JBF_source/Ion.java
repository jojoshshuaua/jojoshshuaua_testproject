/*
 * This class extends ArrayList to function as a 'holder' for AminoAcids.
 * Ion knows the total mass and charge of itself, based on the AminoAcids
 * it contains, and calculates and sets its own mass/charge ratio. The variable
 * hits is set by Spectrometer as it counts how many of each type of ion it has.
 * Hits is used to determine the ion's intensity on the OutputGraphGUI. Ion also
 * knows its position on the OutputGraphGUI so the user can click on individual
 * peaks.
 * 
 */

/**
 *
 * @author Amanda Fisher
 */
import java.util.ArrayList;

public class Ion extends ArrayList<SpecAminoAcid> {

    double totalMass = 0;
    int totalCharge = 0;
    double hits = 0;
    int xCoordinate = 0;

    /**
     * Adds the SpecAminoAcid to the end of the Ion. Ion adds its mass to 
     * the appropriate instance variable. Charge of an Ion will always be one in
     * peptide sequencing.
     *
     * @param a SpecAminoAcid to be added to the end of the Ion.
     * @return true if successfully added AmmionAcid.
     */
    @Override
    public boolean add(SpecAminoAcid a) {
        super.add(a);
        // add the mass of the amino acid to the ion
        setMass(totalMass + a.getMass());
        // if this is the second or greater amino acid added to the chain;
        if(this.size() > 1) {
            // subtract the mass in Daltons of H2O as the amino acids dehydrate
            // together to form the chain
            setMass(totalMass - 18.01528);
        }
        return true;
    }

    /**
     * Used only by Ion in add method to keep a running total of the mass.
     *
     * @param mass New totalMass.
     */
    private void setMass(double mass) {
        totalMass = mass;
    }

    /**
     * Used only by Spectrometer in RunAnalysis to set the charge of the ion to
     * one. Look for a better way to ensure that all ions only have a charge of one.
     *
     * @param charge New totalCharge.
     */
    public void setCharge(int charge) {
        totalCharge = charge;
    }

    /**
     * Used by Spectrometer to count how many ions share a specific mass charge
     * ratio.
     *
     * @param i New hits count.
     */
    public void setHits(double i) {
        hits = i;
    }

    /**
     * Used by OutputGraphGUI to let the Ion know where it is on the graph.
     *
     * @param x
     */
    public void setXCoordinate(int x) {
        xCoordinate = x;
    }

    /**
     * Used to return the Ion's mass.
     * 
     * @return totalMass.
     */
    public double getMass() {
        return totalMass;
    }

    /**
     * Used to return the Ion's charge. Should always return one; think about
     * removing this function.
     *
     * @return totalCharge.
     */
    public int getCharge() {
        return totalCharge;
    }

    /**
     * Used to return the Ion's mass charge ratio. Should always just be mass
     * over one. Think about removing this method.
     *
     * @return massChargeRatio.
     */
    public double getMassChargeRatio() {
        return totalMass/(double)totalCharge;
    }

    /**
     * Used to return the Ion's hits count.
     *
     * @return hits.
     */
    public double getHits() {
        return hits;
    }

    /**
     * Used by Spectrometer to determine if a user clicked on this Ion or not.
     *
     * @return xCoordinate.
     */
    public int getXCoordinate() {
        return xCoordinate;
    }

}
