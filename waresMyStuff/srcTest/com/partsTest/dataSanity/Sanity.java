package com.partsTest.dataSanity;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.MyConnection;
import com.db.MyStatement;
import com.db.SessionVars;

public class Sanity {
	// initial database name
	String initialDatabaseName = "";
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		if (sVars == null)
			sVars = new SessionVars();
		// make all future connection request go to the cdm2 database
		sVars.connection.createBasicDataSource("cdm2");
//		initialDatabaseName = sVars.xml.getDefaultDbName();
//				Utilities.getDbName();
//		Utilities.changeDatabase(sVars, "cdm2");
	}

	@After
	public void tearDown() {
		// change the database connections back to what's specified in the XML file
		try {
			sVars.connection.createBasicDataSource(MyConnection.XMLDBNAME);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * verify that all parents and all children specified in a parent / child table
	 * exist
	 **/
	@Test
	public void testParentChildLinksExist() {
		Connection co = null;
		MyStatement tableMs = null;
		ResultSet tableRs = null;
		MyStatement linkMs = null;
		ResultSet linkRs = null;
		MyStatement parentMs = null;
		ResultSet parentRs = null;
		MyStatement childMs = null;
		ResultSet childRs = null;
		ResultSet nextRs = null;
		try {
			co = sVars.connection.getConnection();
			tableMs = new MyStatement(co);
			tableRs = tableMs.executeQuery(
					"select table_name from information_schema.tables where table_schema = 'cdm' and table_name like '%qxchildqx%'");

			while (tableRs.next()) {
				String table_name = tableRs.getString("table_name");
				System.out.println("working on table " + table_name);
				// get a parent class and a child class
				String[] parts = table_name.split("qxchildqx");
				String parent = parts[0];
				String child = parts[1];
				System.out.println("parent:" + parent + " child:" + child);

				// list of all parents
				linkMs = new MyStatement(co);
				linkRs = linkMs.executeQuery("select distinct parentId from " + table_name);

				while (linkRs.next()) {
					parentMs = new MyStatement(co);
					parentRs = parentMs
							.executeQuery("SELECT * FROM " + parent + " WHERE id=" + linkRs.getInt("parentId"));
					if (!parentRs.next()) {
						parentMs.close();
						throw new Exception(
								"table:" + table_name + " parentId:" + linkRs.getInt("parentId") + " not found");
					}
					// System.out.println("unique parent id:" + linkRs.getInt("parentId"));
				}

				// list of all children
				// linkMs = new MyStatement();
				nextRs = linkMs.executeQuery("select distinct childId from " + table_name);

				while (nextRs.next()) {
					childMs = new MyStatement(co);
					childRs = childMs.executeQuery("SELECT * FROM " + child + " WHERE id=" + nextRs.getInt("childId"));
					if (!childRs.next()) {
						childMs.close();
						throw new Exception(
								"table:" + table_name + " childId:" + nextRs.getInt("childId") + " not found");
					}
					// System.out.println("unique parent id:" + linkRs.getInt("parentId"));
				}

			}
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		} finally {
			if (tableRs != null)
				try {
					tableRs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (linkRs != null)
				try {
					linkRs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (parentRs != null)
				try {
					parentRs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (childRs != null)
				try {
					childRs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (nextRs != null)
				try {
					nextRs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (tableMs != null)
				try {
					tableMs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (linkMs != null)
				try {
					linkMs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (parentMs != null)
				try {
					parentMs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (childMs != null)
				try {
					childMs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (co != null)
				try {
					co.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}

		}

	}

}
