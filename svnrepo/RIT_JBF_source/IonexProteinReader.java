/*
 * IonexProteinReader.java
 */

import java.io.*;

/**
 * Interface that describes a class that can read in an ionex protein
 *
 * @author Kyle Dewey
 */
public interface IonexProteinReader {
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
