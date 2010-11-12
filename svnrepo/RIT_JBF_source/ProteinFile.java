/*
 * ProteinFile.java
 * Version: $Id:$
 * Revisions: $Log:$
 */

/**
 * This class holds and returns information about a protein file
 *
 * @author John Manning
 * @author Kristen Cotton
 */
public class ProteinFile {

    String	m_strName;
	String	m_strFile;

	public ProteinFile( String strName, String strFile)
	{
		m_strName = strName;
		m_strFile = strFile;
	}

	public String GetName()
	{
		return m_strName;
	}

	public String GetFile()
	{
		return m_strFile;
	}
}
