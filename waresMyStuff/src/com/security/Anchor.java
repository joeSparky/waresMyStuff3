package com.security;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.MyConnection;
import com.db.MyStatement;
import com.db.SessionVars;
import com.db.Strings;
import com.runApps.RunApplication;

public class Anchor implements HasTableInterface {
	// String simpleClassName;
	private String canonicalClassName;
	/**
	 * record number of the instance of the anchor in the instance's database. The
	 * anchorId of the third company in the company database would be 3.
	 */
	private int myObjectId;

	/**
	 * mySql column heading stored in MyObject. represents the record number of the
	 * anchor in the anchor database.
	 */
	private static String ANCHORID = "anchorId";

	/**
	 * record number of the anchor in the anchor database
	 */
	public int id;
	
	SessionVars sVars = null;

	// public int getId() {
	// return id;
	// }

	/**
	 * our database column with canonical name. saved in canonicalClassName
	 */
	private static String CLASSNAMECOLUMN = "anchorClass";
	/**
	 * the column name of our record number stored in the MyObject database. saved
	 * in this.id
	 */
	// public static String ANCHORIDCOLUMN = "anchorId";
	/**
	 * our copy of the class' instance id, saved in anchorId
	 */
	private static String INSTANCEIDCOLUMN = "classId";

	// public enum ANCHORSTATE {
	// NOTINITIALIZED, ANCHORRESERVED, ANCHORFOUND,
	// /**
	// * Only the record number of the anchor in our database is set. The
	// * other fields are not current. Avoid dipping into our database every
	// * time a MyObject is extracted.
	// */
	// ANCHORIDONLYSET
	// };
	//
	// public ANCHORSTATE anchorState = ANCHORSTATE.NOTINITIALIZED;

	// public Anchor() {
	// clear();
	// }

	public Anchor getAnchorOfInstance(MyObject instance) throws Exception {
		return find(instance.getAnchor().id);
	}

	public Anchor find(int id) throws Exception {
		String query = "SELECT * FROM " + getMyFileName() + " WHERE " + "id='" + id + "'";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = sVars.connection.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				extractInternal(rs);
				return this;
			} else {
				throw new Exception(
						"id:" + id + " not found in " + getMyFileName() + " " + new Exception().getStackTrace()[0]);
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
	}

	/**
	 * find me in the anchor database
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Anchor find(MyObject obj) throws Exception {
		String query = "SELECT * FROM " + getMyFileName() + " WHERE " + INSTANCEIDCOLUMN + "='" + obj.id + "' AND "
				+ CLASSNAMECOLUMN + "='" + obj.getCanonicalName() + "'";
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = sVars.connection.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				extractInternal(rs);
				return this;
			} else {
				throw new AnchorNotFoundException(
						"id:" + id + " not found in " + getMyFileName() + " " + new Exception().getStackTrace()[0]);
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
	}

	public Anchor(SessionVars sVars) throws Exception {
		clear();
		this.sVars = sVars;
		new Table().tableCreated(this, sVars);
	}

	/**
	 * update the anchor instance and return its record number in the anchor
	 * database
	 * 
	 * @param myObject
	 * @return
	 * @throws Exception
	 */
	public Anchor add(MyObject myObject) throws Exception {

		if (!myObject.isLoaded())
			throw new Exception("myObject must be loaded. " + new Exception().getStackTrace()[0]);

		try {
			find(myObject);
			return this;
		} catch (AnchorNotFoundException e) {
		}
		String query = "INSERT INTO " + getMyFileName() + " (" + CLASSNAMECOLUMN + ", " + INSTANCEIDCOLUMN
				+ ") VALUES ('" + myObject.getCanonicalName() + "', '" + myObject.id + "')";
		Connection conn = null;
		MyStatement st = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			this.id = st.executeUpdateKey(query);
		} finally {
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
		canonicalClassName = myObject.getCanonicalName();
		myObjectId = myObject.id;
		return this;
	}

	public boolean equals(Anchor a) throws ExceptionCoding {
		if (a.id == 0 || id == 0)
			throw new ExceptionCoding("anchor not loaded");
		return a.id == id;
	}

	private Anchor extractInternal(ResultSet row) throws SQLException {
		myObjectId = row.getInt(INSTANCEIDCOLUMN);
		canonicalClassName = row.getString(CLASSNAMECOLUMN);
		id = row.getInt("id");
		return this;
	}

	public void extractInfo(ResultSet row) throws SQLException {
		id = row.getInt(ANCHORID);
	}

	public void clear() throws Exception {
		id = myObjectId = 0;
		canonicalClassName = null;
	}

	@Override
	public final void newTable(SessionVars sVars) throws Exception {
		String newTab = "CREATE TABLE `" + getMyFileName() + "`(";
		newTab += "`id` int(11) auto_increment";
		newTab += ", `" + INSTANCEIDCOLUMN + "` int(11)";
		newTab += ", `" + CLASSNAMECOLUMN + "` char(50)";
		newTab += ", PRIMARY KEY  (`id`)" + ") ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";

		Connection conn = null;
		MyStatement st = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			st.executeMyUpdate("DROP TABLE IF EXISTS `" + getMyFileName() + "`");
			st.executeMyUpdate(newTab);
		} finally {
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
	}

//	@Override
	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		ret.add(new DoubleString(INSTANCEIDCOLUMN, "" + myObjectId));
		ret.add(new DoubleString(CLASSNAMECOLUMN, "" + canonicalClassName));
		return ret;
	}

	@Override
	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add(", `" + INSTANCEIDCOLUMN + "` int(11)");
		ret.add(", `" + CLASSNAMECOLUMN + "` char(50)");
		return ret;
	}

//	public String getSimpleClassName() {
//		return MethodHandles.lookup().lookupClass().getSimpleName();
//	}

	/**
	 * get an instance of the anchor
	 * 
	 * @return
	 * @throws Exception
	 */
	public MyObject getInstanceOfAnchor() throws Exception {
		find(id);
		MyObject ret = new RunApplication().getOneOfThese(canonicalClassName, sVars);
		ret.find(myObjectId);
		return ret;
	}

	public boolean isLoaded() {
		return id > 0;
	}

	public class AnchorNotFoundException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3475254905884521358L;

		public AnchorNotFoundException(String s) {
			super(s);
		}
	}

	/**
	 * true if the anchor of obj is this. for testing only.
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public boolean sameAnchor(MyObject obj) throws Exception {
		Anchor tmp = obj.getAnchor();
		if (!isLoaded())
			throw new Exception("obj " + obj.getInstanceName() + " has an anchor that is not loaded");
		return tmp.id == id;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public String getCanonicalName() {
		// TODO Auto-generated method stub
		return null;
	}
}
