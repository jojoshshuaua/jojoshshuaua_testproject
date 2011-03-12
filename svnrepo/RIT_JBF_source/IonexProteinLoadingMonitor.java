/*
 * IonexProteinLoadingMonitor.java
 */

import java.awt.*;
import javax.swing.*;

/**
 * A loading bar specifically for Ionex protein loading.
 * Loading proteins can take a LONG time (several minutes on my laptop
 * for about 1,600 proteins).
 * @author Kyle Dewey
 */
public class IonexProteinLoadingMonitor
    extends ProgressMonitor implements IonexProteinLoadProgressResponder {
    // begin constants
    public static final String LOADING_MESSAGE = "Loading protein files...";
    // end constants

    public IonexProteinLoadingMonitor( Component parentComponent ) {
	super( parentComponent,
	       LOADING_MESSAGE,
	       "",
	       0,
	       100 );
	setProgress( 0 );
    }

    public void loadingProtein( IonexProteinLoadProgress protein ) {
	System.out.println( "LOADING: " + protein.fileName );
	setNote( protein.fileName );
	setMaximum( protein.total );
	setProgress( protein.which );
    }

    public void setMaximum( int max ) {
	if ( getMaximum() != max ) {
	    super.setMaximum( max );
	}
    }

    public boolean stopLoadingProteins() {
	return isCanceled();
    }
}
