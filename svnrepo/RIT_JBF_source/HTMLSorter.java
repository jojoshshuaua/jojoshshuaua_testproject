/**
 * This class creates the ordered Vector of all of the proteins to be 
 * displayed in hte HTML file.
 */

import java.util.*;

public class HTMLSorter{
    
    private TreeSet proteinInfo;
    private int compBy;
    
    public HTMLSorter( int cb, Vector t, Vector p, Vector m, Vector f ){
	
	compBy = cb;
	proteinInfo = new TreeSet( new HTMLComparator( compBy ) );
	Vector tmp;
	
	for( int i = 0; i < t.size(); i++ ){
	    tmp = new Vector();
	    tmp.add((String)t.elementAt( i ) );
	    tmp.add((String)p.elementAt( i ) );
	    tmp.add((String)m.elementAt( i ) );
	    tmp.add((String)f.elementAt( i ) );
	    proteinInfo.add( tmp );
	}
    }

    public TreeSet getSorted(){
	return proteinInfo;
    }
}
