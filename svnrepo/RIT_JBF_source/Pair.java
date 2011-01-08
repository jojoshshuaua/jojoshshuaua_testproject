/*
 * Pair.java
 */

/**
 * Represents a simple pair.
 * This can be used anywhere one wants to return two values instead of one.
 * @author Kyle Dewey
 */
public class Pair< T, U > {
    public final T first;
    public final U second;
    public Pair( T first, U second ) {
	this.first = first;
	this.second = second;
    }
}
