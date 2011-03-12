/*
 * DefaultIonexProteinLoadProgressResponder.java
 */

/**
 * Accepts notes that we are loading proteins, but it doesn't do anything
 * with them
 * @author Kyle Dewey
 */
public class DefaultIonexProteinLoadProgressResponder 
    implements IonexProteinLoadProgressResponder {
    public void loadingProtein( IonexProteinLoadProgress protein ) {}
    public boolean stopLoadingProteins() {
	return false;
    }
}
