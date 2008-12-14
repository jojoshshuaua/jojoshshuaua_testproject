// Explicit imports
import java.util.HashMap;
import java.io.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * 
 */

public class ConfigHandler extends DefaultHandler {
	
	private AttributesImpl attr = null;
	//Flags to tell what tag we currently are in.
	private boolean inClassElement = false;
	private int numPanels = 0;
	private int panelConfigCount = 0;
	private static HashMap[] configArray;
	private HashMap currentHash;
	
	/**
	 * Constructor 
	 */
	ConfigHandler() {
		
	}

	public HashMap[] getConfigArray(){
		return configArray;
	}

	/**
	 * Receive notification of the beginning of the document.
	 *
	 * <p>Initialize all flags and data members.</p>
	 */
	public void startDocument() throws SAXException {

	}

	/**
	 * Receive notification of the start of an element.
	 *
	 * @param uri The Namespace URI, or the empty string if the element has no
	 *	Namespace URI or if Namespace processing is not being performed.
	 * @param localName The local name (without prefix), or the empty string
	 *	if Namespace processing is not being performed.
	 * @param qName The qualified name (with prefix), or the empty string if
	 *	qualified names are not available.
	 * @param attributes The attributes attached to the element. If there are no
	 *	attributes, it shall be an empty Attributes object.
	 */
	public void startElement(
		String uri,
		String localName,
		String qName,
		Attributes attributes)
		throws SAXException {
			
		attr = new AttributesImpl( attributes );
		System.out.println( qName );
		
		if( qName.equalsIgnoreCase( "panels" ) ){
			numPanels = Integer.parseInt( attr.getValue( "num" ) );
			configArray = new HashMap[ numPanels ];
		}
		else if( qName.equalsIgnoreCase( "class" ) ){
			inClassElement = true;
			
			String value = attr.getValue( "name" );
			String title = attr.getValue( "title" );
			
			System.out.println( value );
			if( panelConfigCount < numPanels ){
				currentHash = new HashMap();
				currentHash.put( "class", value );
				currentHash.put( "name", title );				
			}
			else {
				System.err.println( "There was a problem reading the config file: " );
				System.err.println( "Too many panel specifications.  Number allowed: " + numPanels );
				System.exit( 1 );
			}
		}
		else if( qName.equalsIgnoreCase("settings") && inClassElement ){
			//settings attrs include width, height, bgcolor, and commandLine
			String width = "";
			int widthIndex = attr.getIndex( "width" );
			String height = "";
			int heightIndex = attr.getIndex( "height" );
			String bgcolor = "";
			int bgcolorIndex = attr.getIndex( "bgcolor" );
			String commandLine = "";
			int commandLineIndex = attr.getIndex( "commandLine" );
			
			if( widthIndex != -1 && heightIndex != -1 ){
				width = attr.getValue( widthIndex );
				height = attr.getValue( heightIndex );
				currentHash.put( "width", width );
				currentHash.put( "height", height );
			}
			
			if( bgcolorIndex != -1 ){
				bgcolor = attr.getValue( bgcolorIndex );
				currentHash.put( "bgcolor", bgcolor );
			}
			
			if( commandLineIndex != -1 ){
				commandLine = attr.getValue( commandLineIndex );
				currentHash.put( "commandLine", commandLine );
			}
			
			
		}
		
	}

	/**
	 * Receive notification of character data inside an element.
	 *
	 * <p>Based on state flags, grab the character data and store it in
	 * the appropriate component fields for use by the
	 * {@link endDocument #endDocument} method.</p>
	 *
	 * @param ch The characters.
	 * @param start The start position in the character array.
	 * @param length The number of characters to use from the character array.
	 */
	public void characters(char[] ch, int start, int length)
		throws SAXException {
		String s = new String(ch, start, length);
		System.out.println(s);
	}

	/**
	 * Receive notification of the end of an element.
	 *
	 * @param uri The Namespace URI, or the empty string if the element has
	 *	no Namespace URI or if Namespace processing is not being performed.
	 * @param localName The local name (without prefix), or the empty string
	 *	if Namespace processing is not being performed.
	 * @param qName The qualified XML 1.0 name (with prefix), or the empty
	 *	string if qualified names are not available.
	 */
	public void endElement(String uri, String localName, String qName)
		throws SAXException {
		System.out.println( qName );
		if( qName.equalsIgnoreCase( "class" ) && inClassElement ){
			
			configArray[ panelConfigCount ] = currentHash;
			inClassElement = false;
			panelConfigCount++;
		}
	}

	public static void main( String args[] ){
		try{
			XMLReader rmReader = XMLReaderFactory.createXMLReader();
			ConfigHandler ch = new ConfigHandler();
			rmReader.setContentHandler(ch);
			InputSource inputSource = new InputSource(new FileInputStream(
    	                    "configFile.xml"));
			rmReader.parse(inputSource);
			for( int i = 0; i < configArray.length; i++ ){
				System.out.println( configArray[i] );
			}
		}catch( Exception ex ){
			ex.printStackTrace();
		}
	}

}

