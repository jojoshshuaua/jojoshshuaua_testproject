/*
 * IonexProteinLoadProgress.java
 */

/**
 * Indicates what protein is being loaded at the moment.
 * @author Kyle Dewey
 */
public class IonexProteinLoadProgress {
    // begin instance variables
    public final String fileName; // name of the file being loaded in
    public final int which; // which protein this is
    public final int total; // total number of proteins
    // end instance variables

    public IonexProteinLoadProgress( String fileName,
				     int which,
				     int total ) {
	this.fileName = fileName;
	this.which = which;
	this.total = total;
    }
}
