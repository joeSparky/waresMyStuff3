package com.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import com.forms.SmartForm;
import com.runApps.RunApplication;

public class XML {
	// allow the name of the database name to be overridden for testing databases
	static String overriddenDatabaseName = null;
	static Document dom = null;
	static DocumentBuilderFactory dbf = null;
	static DocumentBuilder db = null;
	static Element doc = null;
	public static final String XMLFILENAME = "commonParams.xml";
	public static final String DISPATCHPARAMNAME = "dispatchForm";
	public static final String LOGINPARAMNAME = "loginForm";
	public static final String CSVPATH = "dcsvPath";
	static final String DEFAULTCOMPANY = "dCompany";
	private static final String DEFAULTUSERNAME = "dUserName";
	private static final String DEFAULTPASSWORD = "dPassword";
	protected static Hashtable<String, String> params = null;
	static InputStream is = null;
	SessionVars sVars = null;

//	void initXMLReader(String path) throws ParserConfigurationException, SAXException, IOException {
//		
//	}

	public String readXML(String tag) throws Exception {
		if (tag.equals(MyConnection.XMLDBNAME) && overriddenDatabaseName != null)
			return overriddenDatabaseName;

		// if we've read this parameter earlier
		if (params.get(tag) != null)
			// return what we read earlier
			return params.get(tag);

		// if this is the first value we've tried to read from the xml file

		if (dbf == null) {
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();

			// see if the xml file exists
			File f = new File(params.get(XMLFILENAME));
			if (!f.exists())
				throw new Exception("file " + XMLFILENAME + " does not exist at " + params.get(XMLFILENAME));
			if (f.isDirectory())
				throw new Exception(XMLFILENAME + " at " + params.get(XMLFILENAME) + " is a directory.");

			is = new FileInputStream(params.get(XMLFILENAME));
			dom = db.parse(is);
			doc = dom.getDocumentElement();
		}
		// checkXMLSetup();

		NodeList nl = doc.getElementsByTagName(tag);
		if (nl.getLength() != 1)
			throw new Exception("tag " + tag + " has a length of " + nl.getLength());
		if (!nl.item(0).hasChildNodes())
			throw new Exception("tag " + tag + " has no child nodes");
		params.put(tag, nl.item(0).getFirstChild().getNodeValue());
		return params.get(tag);
	}

//	public XML() throws Exception {
//		checkXMLSetup();
//	}

//	private void checkXMLSetup() throws Exception {
//		if (params.get(XMLFILENAME) == null)
//			throw new Exception("file path not set up for " + XMLFILENAME + ". XML not set up.");
//	}

//	/**
//	 * initialize the hash table of key words and their values. initialize the path
//	 * to the xml file based upon the sVars context
//	 * 
//	 * @throws Exception
//	 */
//	protected XML() throws Exception {
//		
//	}

	protected XML(SessionVars sVars) throws Exception {
		if (params == null)
			params = new Hashtable<String, String>();
		if (sVars == null)
			throw new Exception("null sVars in XML.java");
		this.sVars = sVars;
//	}
//
////	}
////
//	public void setSvars(SessionVars sVars) throws Exception {
//		if (sVars == null)
//			throw new Exception("null sVars in XML.java");
//		this.sVars = sVars;

		if (sVars.context == null)
			// running in testing mode
			params.put(XMLFILENAME,
					System.getProperty("user.dir") + System.getProperty("file.separator") + "test" + XMLFILENAME);
		else
			// running in the tomcat container
			params.put(XMLFILENAME, sVars.context.getRealPath("/") + XMLFILENAME);

//		initXMLReader(params.get(XMLFILENAME));
	}

//	public XML(SessionVars sVars, String dBName) throws ParserConfigurationException, SAXException, IOException {
//		overriddenDatabaseName = dBName;
//		initWithsVars(sVars);
//	}

	public String getSeparator(SessionVars sVars) {
		if (sVars != null && sVars.context != null)
			return "/";
		else
			return System.getProperty("file.separator");
	}

	public SmartForm getDispatch(SessionVars sVars) throws Exception {
		if (params.get(DISPATCHPARAMNAME) == null)
			params.put(DISPATCHPARAMNAME, readXML(DISPATCHPARAMNAME));
		return new RunApplication().dispatchThis(params.get(DISPATCHPARAMNAME), sVars);
	}

	public SmartForm getLogin(SessionVars sVars) throws Exception {
		if (params.get(LOGINPARAMNAME) == null)
			params.put(LOGINPARAMNAME, readXML(LOGINPARAMNAME));
		return new RunApplication().dispatchThis(params.get(LOGINPARAMNAME), sVars);
	}

	public String getCSVPath() throws Exception {
		return readXML(CSVPATH);
	}

	public String getDefaultCompanyName() throws Exception {
//		Company ret = new Company(sVars);
		return readXML(DEFAULTCOMPANY);

	}

	public String getDefaultUserName() throws Exception {
		return readXML(DEFAULTUSERNAME);
	}

	public String getDefaultUserPassword() throws Exception {
		return readXML(DEFAULTPASSWORD);
	}

	public String getDefaultDbName() throws Exception {
		return readXML(MyConnection.XMLDBNAME);
	}
}
