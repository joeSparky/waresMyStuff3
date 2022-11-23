package com.parts.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.MyConnection;
import com.db.MyStatement;
import com.db.SessionVars;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.security.MyObject;

/**
 * update the database structures if needed
 * 
 * @author joe
 *
 */
public class DBChanges {
	private static boolean hasRun = false;

	public static void doDBChanges(SessionVars sVars) throws SQLException, Exception {
		if (hasRun)
			return;
		hasRun = true;
		checkPartLink(sVars);
		checkLocation(sVars);
	}

	public static boolean hasRun() {
		return hasRun;
	}

	private static void checkPartLink(SessionVars sVars) throws SQLException, Exception {
		// see if inventory date column exists
		// ALTER TABLE tableName DROP COLUMN inventoried
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		PartLink pl = new PartLink(new Part(sVars), new Location(sVars), sVars);
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);

			// if the `inventoried` column exists
			rs = st.executeQuery(
					"select column_name from information_schema.columns where table_schema = Database() AND table_name = '"
							+ pl.getMyFileName() + "' and column_name = 'inventoried'");
			if (rs.next())
				// delete it
				st.executeMyUpdate("ALTER TABLE " + pl.getMyFileName() + " DROP COLUMN `inventoried`");
			// if the new column does not exist
			rs = st.executeQuery(
					"select column_name from information_schema.columns where table_schema = Database() and table_name = '"
							+ pl.getMyFileName() + "' and column_name = '" + PartsInventory.getColumnName() + "'");

			if (!rs.next()) {
				// add it
				st.executeMyUpdate("ALTER TABLE " + pl.getMyFileName() + " ADD COLUMN `"
						+ PartsInventory.getColumnName() + "` DATE");
				st.executeMyUpdate(
						"update " + pl.getMyFileName() + " set " + PartsInventory.getColumnName() + "=curdate()");
			}
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}
	private static void checkLocation(SessionVars sVars) throws SQLException, Exception {
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		Location location = new Location(sVars);
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);

			// if the `inventoried` column exists
			rs = st.executeQuery(
					"select column_name from information_schema.columns where table_schema = Database() AND table_name = '"
							+ location.getMyFileName() + "' and column_name = 'inventoried'");
			if (rs.next())
				// delete it
				st.executeMyUpdate("ALTER TABLE " + location.getMyFileName() + " DROP COLUMN `inventoried`");
			// if the new column does not exist
			rs = st.executeQuery(
					"select column_name from information_schema.columns where table_schema = Database() and table_name = '"
							+ location.getMyFileName() + "' and column_name = '" + MyObject.INVENTORYFIELDNAME + "'");

			if (!rs.next()) {
				// add it
				st.executeMyUpdate("ALTER TABLE " + location.getMyFileName() + " ADD COLUMN `"
						+ MyObject.INVENTORYFIELDNAME + "` DATE");
				st.executeMyUpdate(
						"update " + location.getMyFileName() + " set " + MyObject.INVENTORYFIELDNAME + "=curdate()");
			}
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}
}
