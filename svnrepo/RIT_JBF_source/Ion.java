/*
 * This class extends ArrayList to function as a 'holder' for AminoAcids.
 * Ion knows the total mass and charge of itself, based on the AminoAcids
 * it contains, and calculates and sets its own mass/charge ratio. The variable
 * hits is set by Spectrometer as it counts how many of each type of ion it has.
 * Hits is used to determine the ion's intensity on the OutputGraphGUI. Ion also
 * knows its position on the OutputGraphGUI so the user can click on individual
 * peaks.
 * 
 * version 3
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
     * Adds the SpecAminoAcid to the end of the Ion. Ion adds its mass and charge
     * to the appropriate instance variables.
     *
     * @param a SpecAminoAcid to be added to the end of the Ion.
     * @return true if successfully added AmmionAcid.
     */
    @Override
    public boolean add(SpecAminoAcid a) {
        super.add(a);
        if(this.size() > 0) {
            // subtract the mass in Daltons of OH group if the chain is not empty
            setMass(totalMass - 17.00734);
        }
        // Add the mass of the new amino acid, minus the weight in Daltons of
        // two hydrogens.
        setMass(totalMass + a.getMass() - 2.01588);
        // Add the charge of the amino acid, minus one positive charge to
        // represent the new amino acid no longer having a positive amino group.
        setCharge(totalCharge + a.getCharge() - 1);
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
     * Used only by Ion in add method to keep a running total of the charge.
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
     * Used to return the Ion's charge.
     *
     * @return totalCharge.
     */
    public int getCharge() {
        return totalCharge;
    }

    /**
     * Used to return the Ion's mass charge ratio.
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
