/**
 * This class compares two IEFProteins based on pI representation and
 * x values.
 *
 * @author Jill Zapoticznyj
 */

import java.util.*;

public class CompIEF implements Comparator{
    
    private double max;  //the maximum pH value for the IEF
    private double min;  //the minimum pH value for the IEF

    /**
     * Constructor for the CompIEF object
     *
     * @param _max the max value
     * @param _min the min value
     */    
    public CompIEF( double _max, double _min ){
	max = _max;
	min = _min;
    }

    /**
     * Compares two IEFProtein objects by their pI values and 
     * x coordinate.  The method returns a 0 if they are equal,
     * a negative one if the 2nd object is greater than the first,
     * and a 1 if the first object is greater than the second.
     *
     * @param o1 the first IEFProtein object
     * @param o2 the second IEFProtein object
     *
     * @return retVal
     */
    public int compare( Object o1, Object o2 ){
		
		// cast the objects to IEFProteins
		IEFProteinSwingVersion i1 = (IEFProteinSwingVersion)o1;
		IEFProteinSwingVersion i2 = (IEFProteinSwingVersion)o2;
		int retVal = -1;
		// the width of an IEFProtein bar
		double width = IEFProteinSwingVersion.returnWidth();
		// the range each IEFProtein represents
		double range = 1/( max - min );
		// get the objects' pI values and x coordinates
		double minpi1 = i1.getMinPI();
		double minpi2 = i2.getMinPI();
		double maxpi1 = i1.getMaxPI();
		double maxpi2 = i2.getMaxPI();
		
		int x1 = i1.returnX();
		int x2 = i2.returnX();

		// if the pI values are both greater than the max value for the range,
		// they are equal
		if( (minpi1 >= max) && (minpi2 >= max) ){
		    retVal = 0;
		}
		// if the pI values are both less than the min value for the range,
		// they are equal
		else if( (minpi1 <= min) && (minpi2 <= min) ){
		    retVal = 0;
		}
		//if the x coordinates are within 1/2 the width of a bar of eachother..
		else if(( x1 + ((3*width)/4) >= x2 ) && ( x1 - ((3*width)/4) <= x2 ) ){
	    
		    //..and the pI values are within range of eachother, they are equal
		    if( (minpi2 <= minpi1 + range) && ( minpi2 >= minpi1 ) ){
				retVal = 0;
		    }
	    
		    else if( (maxpi2 >= maxpi1 - range ) && ( maxpi2<=maxpi1 ) ){
				retVal = 0;
	    	}

		}
		// if the x coordinate of the first IEFProtein is greater than that of
		// the second, the first IEFProtein is greater.
		else if( x1 > x2 ){
	    	retVal = 1;
		}
	
		//else, the second was greater and retVal still equals -1.
		
		return retVal;
    }
    
}