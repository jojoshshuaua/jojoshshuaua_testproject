/*
 * IonexModelInterface.java
 */

/**
 * Defines what actions the model must understand in response to a
 * controller.
 *
 * @author Kyle Dewey
 */
public interface IonexModelInterface {
    /**
     * Gets the protein bands that are in the model
     * this is neccessary to see which proteins are traveling
     * with each other in the model, and also to get what color
     * each protein band is
     */
    public IonexProteinBand[] getProteinBands();

    /**
     * Gets how far into the simulation we are.
     *
     * @return a value between 0-1, representing how far along we are
     */
    public double getPercentageThrough();

    /**
     * Tells the model to begin the simulation.
     */
    public void startSimulation();
    
    /**
     * Tells the model to pause the simulation
     */
    public void pauseSimulation();
}
