package com.security;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

import com.db.MyStatement;
import com.db.SessionVars;
import com.db.TableVersion;

/**
 * manage tables in database. create a table if it doesn't exist. remember what
 * we've created since we've started running so we don't have to dip into the
 * database with each new().
 * 
 * @author Joe
 *
 */
public class Table {
	/**
	 * map the file name to an executable
	 */
	private static Collection<String> tableExists = new ArrayList<String>();
	Object mutex = new Object();

	/**
	 * return true if a new database table was created.
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public boolean tableCreated(HasTableInterface o, SessionVars sVars) throws Exception {
		synchronized (mutex) {
			String lowerCase = o.getMyFileName().toLowerCase();
//			Internals.dumpStringContinue("tableCreated");
			if (!tableExists.contains(lowerCase)) {
				tableExists.add(lowerCase);
				// create the database instance
				Connection co = null;
				MyStatement st = null;
				try {
					co = sVars.connection.getConnection();
					st = new MyStatement(co);

					if (!st.tableExists(lowerCase)) {
						o.newTable(sVars);
						new TableVersion().addNewVersion(lowerCase, "first", 1, sVars);
						return true;
					}

				} finally {
					if (st != null)
						st.close();
					if (co != null)
						co.close();
				}
			}
			return false;
		}
	}

	// stack overflow when MyLinkObject.getFileName needs to create a table, but
	// tableCreated needs a fileName.
	public boolean tableCreated(HasTableInterface o, String fileName, SessionVars sVars) throws Exception {
		synchronized (mutex) {
			String lowerCase = fileName.toLowerCase();
//			Internals.dumpStringContinue("tableCreated");
			if (!tableExists.contains(lowerCase)) {
				tableExists.add(lowerCase);
				boolean tableExistsInDb = false;
				// create the database instance
				Connection co = null;
				MyStatement st = null;
				try {
					co = sVars.connection.getConnection();
					st = new MyStatement(co);
					if (st.tableExists(lowerCase))
						tableExistsInDb = true;
				} finally {
					if (st != null)
						st.close();
					if (co != null)
						co.close();
				}
				if (!tableExistsInDb) {
					o.newTable(sVars);
					new TableVersion().addNewVersion(lowerCase, "first", 1, sVars);
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * for testing only
	 */
	public static void clearTableExists() {
		tableExists = new ArrayList<String>();
	}
}
