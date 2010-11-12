/*
 * UnknownAminoAcidException.java
 */

public class UnknownAminoAcidException extends IonexProteinFormatException {
    public UnknownAminoAcidException() {}

    public UnknownAminoAcidException( String message ) {
	super( message );
    }
}
