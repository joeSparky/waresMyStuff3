package comTest.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import com.db.MyConnection;
import com.db.SessionVars;

public class MyConnectionTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
	}

	@Test
	public void testGetCurrentDbName() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String query = "select database()";
		String connDbName = "";
		try {
			conn = sVars.connection.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			if (rs.next())
				connDbName = rs.getString(1);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		System.out.println(connDbName);

		// what the xml file thinks the database name is
		// mysql converts database names to lower case
		String xmlDbName = "";
		try {
			xmlDbName = sVars.xml.readXML(MyConnection.XMLDBNAME);//.toLowerCase();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (!connDbName.equals(xmlDbName))
			fail("connection database name:" + connDbName + " xml database name:" + xmlDbName);

		String newDbName = "";
		// try to connect to the cdm2 database
		try {
			sVars.connection.createBasicDataSource("cdm2");
			conn = sVars.connection.getConnection();
			st = conn.createStatement();
			// use the same query
			rs = st.executeQuery(query);
			if (rs.next())
				newDbName = rs.getString(1);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				fail(e.getLocalizedMessage());
			}
		}
		if (!newDbName.equals("cdm2"))
			fail("expected database name of cdm2, got " + newDbName);
	}

//	@Test
//	// see if Utilities.getDbName() matches the XML database name
//	public void testGetUtilitiesCurrentDbName() {
//		String dbName = Utilities.getDbName();
//		String xmlDbName = "";
//		try {
//			xmlDbName = new XML(new SessionVars()).readXML(MyConnection.XMLDBNAME).toLowerCase();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		if (!dbName.equals(xmlDbName))
//			fail("connection database name:" + dbName + " xml database name:" + xmlDbName);
//	}

//	@Test
//	// change the database used by MyConnection
//	public void changeConnectionDatabase() {
//		XML xml = null;
//		String url = "";
//		String otherDatabaseName = "partstestingdb";
//		String currentDatabaseName = Utilities.getDbName();
//		try {
//			MyConnection.closeAndOpenBasicDataSource();
//			xml = new XML(null);
//			url = "jdbc:mysql://localhost:3306/";
//			// url += xml.readXML(XMLDBNAME);
//			url += otherDatabaseName;
//			url += "?user=" + xml.readXML(com.db.MyConnection.MXLDBACCOUNT);
//			url += "&password=" + xml.readXML(com.db.MyConnection.MXLDBPASSWORD);
//			url += "&serverTimezone=UTC";
//			MyConnection.assignURL(url);
//			MyConnection.setMaxTotal();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (!Utilities.getDbName().equals(otherDatabaseName))
//			fail("expected database name:+otherDatabaseName+ Utilities reported database name:"
//					+ Utilities.getDbName());
//		// put it back so that the rest of the tests don't fail
//		Utilities.changeDatabase(currentDatabaseName);
//		
//	}
}
