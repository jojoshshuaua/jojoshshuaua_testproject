/*
 * Protease is the abstract class inherited by all classes that are responsible for
 * cutting a protein's sequence at specific points. Only method they are required
 * to have is cut.
 *
 */

/**
 *
 * @author Amanda Fisher
 */
import java.util.ArrayList;

public abstract class Protease {
    public abstract ArrayList<String> cut(String sequence) throws ProteaseException;
}
