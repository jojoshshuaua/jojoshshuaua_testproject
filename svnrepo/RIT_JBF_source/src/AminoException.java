/*
 * This class is an exception thrown by AminoAcid when an incorrect input is
 * given to one of its two constructors.
 * 
 * version 3
 */

/**
 * 
 * @author Amanda Fisher
 */
public class AminoException extends Exception {
    public AminoException() {}
    public AminoException(String message) {
        super(message);
    }
}
