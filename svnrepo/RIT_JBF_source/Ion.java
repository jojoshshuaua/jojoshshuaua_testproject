/*
 * This class extends ArrayList to function as a 'holder' for AminoAcids.
 * Ion knows the total mass and charge of itself, based on the AminoAcids
 * it contains, and calculates and sets its own mass/charge ratio. The variable
 * hits is set by Spectrometer as it counts how many of each type of ion it has.
 * Hits is used to determine the ion's intensity on the OutputGraphGUI. Ion also
 * knows its possition on the OutputGraphGUI so the user can click on individual
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

    double totalMass;
    // totalCharge starts at 1 because all ions will have at least a charge of 1
    // due to the trailing amino group.
    int totalCharge = 1;
    int hits;
    int xCoordinate;

    /**
     * Adds the SpecAminoAcid to the end of the Ion. Ion adds its mass and charge
     * to the appropriate instance variables and calculates its massChargeRatio.
     *
     * @param a SpecAminoAcid to be added to the end of the Ion.
     * @return true if successfully added AmmionAcid.
     */
    @Override
    public boolean add(SpecAminoAcid a) {
        super.add(a);
        setMass(totalMass + a.getMass());
        setCharge(totalCharge + a.getCharge());
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
    public void setHits(int i) {
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
    public int getHits() {
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
