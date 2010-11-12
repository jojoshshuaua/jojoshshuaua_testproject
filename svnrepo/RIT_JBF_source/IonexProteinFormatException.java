/*
 * IonexProteinFormatException.java
 */

/**
 * Exception thrown when a format level error is found in a file containing
 * protein information.
 *
 * @author Kyle Dewey
 */
public class IonexProteinFormatException extends Exception {
    public IonexProteinFormatException() {}
    public IonexProteinFormatException( String message ) {
	super( message );
    }
}
