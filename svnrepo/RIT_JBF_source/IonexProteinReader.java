/*
 * IonexProteinReader.java
 */

import java.io.*;

/**
 * Interface that describes a class that can read in an ionex protein.
 * It is intended that there is a different reader for each type of
 * file.
 * @author Kyle Dewey
 */
public interface IonexProteinReader {
    /**
     * Gets a file filter that recognizes this given type of file.
     */
    public FileFilter getFileFilter();

    /**
     * Reads in a protein from the given file.
     *
     * @param file The file
     * @exception FileNotFoundException if the file wasn't found
     * @exception IonexProteinFormatException If there is a format-level
     * error in the file
     * @exception IOException If an error occurred on reading the file
     */
    public IonexProtein readProtein( File file ) throws FileNotFoundException,
							IonexProteinFormatException,
							IOException;
}
