/*
 * CacheCall.java
 */

/**
 * When a value is not already in the cache, this will be called to
 * get the value.  Input and output type = I, O.
 * @author Kyle Dewey
 */
public interface CacheCall< I, O > {
    public O call( I input );
}
