package com.db;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.security.HasTableInterface;

public class TableVersion implements HasTableInterface {

//	public TableVersion() throws Exception {
//		new Table().tableCreated(this, sVars);
//	}
	private static final int REASONLENGTH = 100;
	private static final int TABLENAMELENGTH = 80;
	public static final String TABLENAME = "versions";

//	static {
//
//		try {
//			if (!new TableVersion().tableExists(TABLENAME)) {
//				newTable();
////				addNewVersion(TABLENAME, "first", 1);
//			}
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
//
//	}
	@Override
	public void newTable(SessionVars sVars) throws Exception {
		String query = "CREATE TABLE `" + TABLENAME + "` ("
				+ "`when` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," + "`reasonNumber` int(11), "
				+ "`reason` varchar(" + REASONLENGTH + ") NOT NULL default \"\", " + "`tableName` varchar("
				+ TABLENAMELENGTH + ") NOT NULL default \"\", " + "`recordNumber` int(11) auto_increment, "
				+ "PRIMARY KEY  (`recordNumber`)" + ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			st.executeMyUpdate("DROP TABLE IF EXISTS " + TABLENAME);
			st.executeMyUpdate(query);
			addNewVersion(TABLENAME, "first", 1, sVars);
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	public int getLatestVersion(String tableName, SessionVars sVars) throws Exception {
		if (!tableExists(TABLENAME, sVars))
			newTable(sVars);
		String query = "SELECT MAX(reasonNumber) AS reasonNumber FROM " + TABLENAME + " WHERE tableName='" + tableName
				+ "'";
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQuery(query);
			if (rs.next()) {
				int tmp = rs.getInt("reasonNumber");
				if (tmp == 0)
					return -1;
				else
					return tmp;
			} else
				return -1;
		} catch (Exception e) {
			return -1;
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	public void addNewVersion(String tableName, String reason, int version, SessionVars sVars) throws Exception {

		// if the version table has a higher version than the current application
		if (getLatestVersion(tableName, sVars) > version) {
//			Internals.dumpStringContinue("table:" + tableName + ", new version:" + version + ", existing version:"
//					+ getLatestVersion(tableName));
			return;
		}
		// no leading commas on first field and value
		String fields = "tableName";
		String values = "'" + tableName + "'";

		fields += ", reason";
		values += ", '" + reason + "'";

		fields += ", reasonNumber";
		values += ", '" + (version) + "'";
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			st.executeMyUpdate("INSERT INTO " + TABLENAME + " (" + fields + ") VALUES (" + values + ")");
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		return;
	}

	private boolean tableExists(String tableName, SessionVars sVars) throws Exception {
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			st.executeQuery("SELECT 1 FROM " + tableName + " LIMIT 1");
		} catch (SQLException e) {
			return false;
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		return true;
	}

	@Override
	public DoubleStrings extendAdd() {
		return new DoubleStrings();
	}

	@Override
	public DoubleStrings extendUpdate() {
		return new DoubleStrings();
	}

	@Override
	public Strings extendNewTable() {
		return new Strings();
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public String getCanonicalName() {
		return getClass().getCanonicalName();
	}

}
