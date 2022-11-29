package com.db;

import java.sql.Connection;

import org.apache.commons.dbcp2.BasicDataSource;

import com.errorLogging.Internals;

//import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class MyConnection {
	static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String XMLDBNAME = "dbName";
	public static final String MXLDBACCOUNT = "dbAccount";
	public static final String MXLDBPASSWORD = "dbPassword";

	BasicDataSource basicDataSource = null;
	SessionVars sVars = null;
	String dbName = null;

	// create a BasicDataSource connection pool to the dbName database
	protected MyConnection(SessionVars sVars, String dbName) throws Exception {
		this.sVars = sVars;
		this.dbName = dbName;
		//createBasicDataSource(dbName);
	}

	/**
	 * create a BasicDataSource connected to the dbName database. If a
	 * BasicDataSource was already set up, it will be closed before creating the new
	 * BasicDataSource. Be sure to put it back when done with the new databases so
	 * that the next connections go to the dbName in the XML file!
	 * 
	 * @param dbName
	 * @throws Exception
	 */
	public void createBasicDataSource(String dbName) throws Exception {
		if (basicDataSource != null)
			basicDataSource.close();
		basicDataSource = new BasicDataSource();
		String url = "jdbc:mysql://localhost:3306/";
		url += dbName;
		url += "?user=" + sVars.xml.readXML(MXLDBACCOUNT);
		url += "&password=" + sVars.xml.readXML(MXLDBPASSWORD);
		url += "&serverTimezone=UTC";
		basicDataSource.setUrl(url);
		basicDataSource.setMaxTotal(5);
	}

	public Connection getConnection() throws Exception {
		if (basicDataSource == null)
			createBasicDataSource(dbName);
		return basicDataSource.getConnection();
	}

	// load the driver class
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			Internals.logStartupError(e);
		}
	}
}
