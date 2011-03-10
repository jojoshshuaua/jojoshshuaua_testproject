/*
 * Cache.java
 */

import java.util.*;

/**
 * Represents a cache, for purposes of memoization.
 * @author Kyle Dewey
 */
public class Cache<I, O> {
    // begin constants
    public static final int DEFAULT_MAX_SIZE = 10;
    // end constants

    // begin instance variables
    private final CacheCall<I,O> function;
    private final int maxSize;
    private Map< I, O > cache;
    private Map< I, Integer > timesUsed;
    // end instance variables

    /**
     * Creates a new cache of the given size and the given function
     */
    public Cache( int maxSize, CacheCall<I, O> function ) {
	this.maxSize = maxSize;
	this.function = function;
	cache = new HashMap< I, O >( maxSize );
	timesUsed = new HashMap< I, Integer >( maxSize );
    }

    /**
     * creates a new cache with the given function and a default size
     */
    public Cache( CacheCall<I,O> function ) {
	this( DEFAULT_MAX_SIZE, function );
    }

    /**
     * Conditionally frees up a spot in the cache.  This is done only if
     * we are at the max size
     */
    protected void conditionalOpenSpot() {
	if ( cache.size() >= maxSize ) {
	    openSpot();
	}
    }

    /**
     * Gets the least accessed input in the cache.
     * Returns null if there is nothing in the cache
     */
    public I getLeastAccessed() {
	int minTimesUsed = Integer.MAX_VALUE;
	I retval = null;

	for( I key : timesUsed.keySet() ) {
	    int currentTimesUsed = timesUsed.get( key ).intValue();
	    if ( currentTimesUsed < minTimesUsed ) {
		minTimesUsed = currentTimesUsed;
		retval = key;
	    }
	}

	return retval;
    }

    /**
     * Unconditionally frees a spot in the cache
     */
    protected void openSpot() {
	I least = getLeastAccessed();
	cache.remove( least );
	timesUsed.remove( least );
    }

    /**
     * Increments the number of times the given value has been seen.
     * A no-op if it has not been seen.
     */
    public void incrementTimesSeen( I input ) {
	if ( cache.containsKey( input ) ) {
	    timesUsed.put( input,
			   new Integer( timesUsed.get( input ) + 1 ) );
	}
    }

    /**
     * Caches the given values.
     */
    public void cache( I input, O output ) {
	if ( !cache.containsKey( input ) ) {
	    conditionalOpenSpot();
	    cache.put( input, output );
	    timesUsed.put( input, 0 );
	} 
    }

    /**
     * gets the given output value given the input value from the cache.
     * Returns null if it is not in there.
     */
    public O getCache( I input ) {
	O retval = null;

	if ( cache.containsKey( input ) ) {
	    retval = cache.get( input );
	    incrementTimesSeen( input );
	}

	return retval;
    }

    /**
     * Gets the output value on the given input value.
     * If it is not already in the cache, it will cache it.  It will
     * used the cached value if it is already in the cache
     */
    public O get( I input ) {
	O retval;

	if ( !cache.containsKey( input ) ) {
	    retval = function.call( input );
	    cache( input, retval );
	} else {
	    retval = getCache( input );
	}

	return retval;
    }
}
