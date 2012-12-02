/*
 * UnknownLineException.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *      $Log$
 *
 */

/**
 * Exception thrown when a given line in a line graph isn't recognized
 * @author Kyle Dewey
 */
public class UnknownLineException extends Exception {
    public UnknownLineException() {}
    public UnknownLineException( String message ) {
	super( message );
    }
}
