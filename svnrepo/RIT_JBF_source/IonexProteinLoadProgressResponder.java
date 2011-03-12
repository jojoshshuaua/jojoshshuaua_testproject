/*
 * IonexProteinLoadProgressResponder.java
 */

/**
 * Something that responds to proteins being loaded in.
 * @author Kyle Dewey
 */
public interface IonexProteinLoadProgressResponder {
    /**
     * Indicates that we are loading in the given protein.
     */
    public void loadingProtein( IonexProteinLoadProgress protein );

    /**
     * Indicates if we want to stop loading proteins.
     */
    public boolean stopLoadingProteins();
}
