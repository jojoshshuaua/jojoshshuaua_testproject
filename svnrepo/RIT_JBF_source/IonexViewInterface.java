/*
 * IonexViewInterface.java
 */

/**
 * Defines what actions the view must understand in response to changes 
 * in the model
 *
 * @author Kyle Dewey
 */
public interface IonexViewInterface {
    /**
     * Tells the view where a protein band is.
     *
     * @param protein The protein band we are referring to
     */
    public void updateProteinPosition();

    /**
     * Tells the view the concentration of NaCl at the top of 
     * the column
     *
     * @param concentration The concentration at the top of the column
     */
    public void updateTopNaClConcentration( double concentration );

    /**
     * Tells the view the concentration of NaCl at the bottom of the
     * column
     *
     * @param concentration The concentration at the bottom of the column
     */
    public void updateBottomNaClConcentration( double concentration );

    /**
     * Tells the view that we are done.
     */
    public void simulationDone();
}
