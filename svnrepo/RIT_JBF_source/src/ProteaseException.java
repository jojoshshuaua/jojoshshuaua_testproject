/*
 * This class is an exception thrown by one of the Protease classes whenever
 * an incorrect input is given to the cut method.
 *
 * version 2
 */

/**
 *
 * @author Amanda Fisher
 */
public class ProteaseException extends Exception {
    public ProteaseException() {}
    public ProteaseException(String message) {
        super(message);
    }
}
