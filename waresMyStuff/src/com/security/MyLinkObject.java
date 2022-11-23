package com.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.db.DoubleString;

//import org.xml.sax.SAXException;

import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.MyStatement;
import com.db.Strings;

/**
 * common methods for links between objects
 * 
 * @author Joe
 *
 */
public class MyLinkObject implements HasTableInterface {
	
	protected SessionVars sVars = null;
	
	public MyLinkObject(MyObject parent, MyObject child, SessionVars sVars) throws Exception {
		this.sVars = sVars;
		clear();
		this.parent = parent;
		this.child = child;
		// Avoid infinite loop with local getFileName creating a table.
		String ret = (parent.getMyFileName() + CHILDSTRING + child.getMyFileName()).toLowerCase();
		new Table().tableCreated(this, ret, sVars);
	}

	protected MyObject parent;
	protected MyObject child = null;
	public final static String CHILDSTRING = "qxchildqx";

	protected void clear() {
		id = 0;
	}

	public int id;

	public MyObject getNewChild() throws Exception {
		return child.getNew();
	}

	/**
	 * verify that both the parent and child exist.
	 * 
	 * @throws Exception
	 */
	public void sanity() throws Exception {
		// see if the parent exists without changing the parent
		parent.getNew().find(parent.id);
		// see if the child exists without changing the child
		child.getNew().find(child.id);
	}

	public MyLinkObject find() throws Exception {
		if (!parent.isLoaded())
			throw new Exception("parent is not loaded.");
		if (!child.isLoaded())
			throw new Exception("child is not loaded.");
		Connection conn = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			rs = st.executeQueryWithException("select * from " + getMyFileName() + " where parentId='" + parent.id
					+ "' and childId='" + child.id + "'");
			if (rs.next())
				return extractInfo(rs);
			throw new Exception("Parent:" + parent.getInstanceName() + "Child:" + child.getInstanceName()
					+ " not found. " + new Exception().getStackTrace()[0]);
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
	}

	public boolean linkExists() throws ExceptionCoding {
		if (parent == null)
			throw new ExceptionCoding("null parent");
		if (!parent.isLoaded())
			return false;
		if (child == null)
			throw new ExceptionCoding("null child");
		if (!child.isLoaded())
			return false;

		try {
			find();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public MyLinkObject add() throws Exception {

		if (!parent.isLoaded())
			throw new Exception("parent not loaded");
		if (!child.isLoaded())
			throw new Exception("child not loaded");
		if (parent.getClass().equals(child.getClass()) && parent.id == child.id)
			throw new Exception("duplicate objects " + parent.getInstanceName());

		// throw an exception if the link already exists
		if (linkExists())
			throw new Exception(
					"link already exists between " + parent.getInstanceName() + " and " + child.getInstanceName());

		if (isParentAndChildOfSameClass()) {
			if (!child.isRecursive())
				throw new Exception("child is not recursive " + new Exception().getStackTrace()[0]);
			checkRecursiveLineage(parent, child, sVars);
		}

		String fields = "";
		String values = "";
		fields += "parentId,";
		values += "" + parent.id + ",";
		fields += "childid";
		values += "" + child.id;
		for (DoubleString db : extendAdd()) {
			fields += ", " + db.field;
			values += ", '" + db.value + "'";
		}
		Connection conn = null;
		MyStatement st = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			st.executeMyUpdate("INSERT INTO " + getMyFileName() + " (" + fields + ") VALUES (" + values + ")");
		} finally {
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
		return this;
	}

	/**
	 * list all the children of the link (all links that match the parent's id)
	 * 
	 * @param tableName
	 * @param childId
	 * @return
	 * @throws Exception
	 */
	public MyObjects listChildrenOfParent() throws Exception {
		if (!parent.isLoaded())
			throw new Exception("parent is not loaded");
		MyObjects ret = new MyObjects();
		MyLinkObject mlo = new MyLinkObject(parent, child, sVars);
		Connection conn = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			rs = st.executeQuery("select * from " + mlo.getMyFileName() + " where parentId='" + parent.id + "'");
			while (rs.next()) {
				int childId = rs.getInt("childId");
				ret.add(child.getNew().find(childId));
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
		return ret;
	}

	/**
	 * list all the children of the link (all links that match the parent's id)
	 * 
	 * @param tableName
	 * @param childId
	 * @return
	 * @throws Exception
	 */
	public MyObjects listParentsOfChild() throws Exception {
		String exceptionString = null;
		if (!child.isLoaded())
			throw new Exception(child.getInstanceName() + " is not loaded at " + new Exception().getStackTrace()[0]);
		int parentId = -1;
		MyObjects ret = new MyObjects();
		Connection conn = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			rs = st.executeQuery("select * from " + getMyFileName() + " where childId='" + child.id + "'");
			while (rs.next()) {
				parentId = rs.getInt("parentId");
				ret.add(parent.getNew().find(parentId));
			}
		} catch (SQLException e) {
			exceptionString = e.getLocalizedMessage() + " parent:" + parent.getInstanceName() + " parentId:" + parentId
					+ " child:" + child.getInstanceName() + new Exception().getStackTrace()[0];
		} catch (Exception e) {
			exceptionString = e.getLocalizedMessage() + " parent:" + parent.getInstanceName() + " parentId:" + parentId
					+ " child:" + child.getInstanceName() + new Exception().getStackTrace()[0];
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
		if (exceptionString != null)
			throw new Exception(exceptionString);
		return ret;
	}

	/**
	 * return an exception if the link can not be deleted. The only link that can
	 * not be deleted is between the default user and the default role
	 * 
	 * @throws Exception
	 */
	public void deleteTest() throws Exception {
		if (parent instanceof User && parent.id == 1 && child instanceof NewRole && child.id == 1)
			throw new Exception("The link between the default " + User.NAME + " and the default " + NewRole.NAME
					+ " can not be deleted.");
	}

	/**
	 * delete a link unconditionally
	 * 
	 * @throws Exception
	 */
	public void deleteUnconditionally() throws Exception {
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			st.executeMyUpdate("delete from " + getMyFileName() + " where parentId='" + parent.id + "' AND childId='"
					+ child.id + "'");
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		clear();
	}

	/**
	 * delete the links between a child and all of its parents. child must be
	 * loaded. parent is only used in generating a table name
	 * 
	 * @param anchor
	 * @param thisOne
	 * @param anchorId
	 * @throws Exception
	 */
	public void deleteLinksUnconditionallyChild() throws Exception {
		if (!child.isLoaded())
			throw new Exception("child must be loaded");
		MyLinkObject mlo = new MyLinkObject(parent, child, sVars);
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);

			st.executeMyUpdate("delete from " + mlo.getMyFileName() + " where childId='" + child.id + "'");
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	/**
	 * delete the links between a parent and all of its children. parent must be
	 * loaded. child is only used in generating a table name
	 * 
	 * @param anchor
	 * @param thisOne
	 * @param anchorId
	 * @throws Exception
	 */
	public void deleteLinksUnconditionallyParent() throws Exception {
		if (!parent.isLoaded())
			throw new Exception("parent must be loaded");
		MyLinkObject mlo = new MyLinkObject(parent, child,sVars);
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			st.executeMyUpdate("delete from " + mlo.getMyFileName() + " where parentId='" + parent.id + "'");
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	protected MyLinkObject extractInfo(ResultSet row) throws Exception {
		if (row == null) {
			throw new Exception("null row");
		}
//		try {
		child.id = row.getInt("childId");
		parent.id = row.getInt("parentId");
//		} catch (SQLException e) {
//			Internals.dumpSQLExit(e);
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
		return this;
	}

	/**
	 * return the string for the links tab on the main form(s). return null if there
	 * will not be a tab.
	 * 
	 * @return
	 */
	public String getTab() {
		if (parent == null)
			return null;
		if (!parent.isLoaded())
			return null;
		if (child == null)
			return null;
		if (!child.isLoaded())
			return null;

		try {
			find();
			return "change link";
		} catch (Exception e) {
			return "add link";
		}

	}

	// @Override
	public String getMyFileName() throws Exception {
		// let the constructor handle the new Table for parent, child.
		new MyLinkObject(parent, child, sVars);
		return (parent.getMyFileName() + CHILDSTRING + child.getMyFileName()).toLowerCase();
	}

	// @Override
	public void newTable(String tableName) throws Exception {
		String newTab = "CREATE TABLE `" + tableName + "` (";
		newTab += "`id` int(11) auto_increment,";
		newTab += "PRIMARY KEY  (`id`),";
		newTab += "`parentId` int(11),";
		newTab += "`childId` int(11)";
		for (String s : extendNewTable())
			newTab += ", " + s;
		newTab += ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			st.executeMyUpdate("DROP TABLE IF EXISTS `" + tableName + "`");
			st.executeMyUpdate(newTab);
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	// @Override
	protected void createNewTableIfNecessary(String fileName) throws Exception {
		Connection co = null;
		MyStatement s = null;
//		MyStatement s1 = null;
		try {
			co = sVars.connection.getConnection();
			s = new MyStatement(co);
			if (s.tableExists(fileName)) {
				newTable(fileName);
//				try (MyStatement st = new MyStatement(); MyStatement st1 = new MyStatement()) {
				s.executeMyUpdate("CREATE INDEX parentId ON " + fileName + "(parentId)");
				s.executeMyUpdate("CREATE INDEX childId ON " + fileName + "(childId)");
			}
		} finally {
			if (s != null)
				s.close();
			if (co != null)
				co.close();
		}
	}

	/**
	 * throw an exception if the source is an ancestor of the destination
	 * 
	 * @param parent
	 * @param child
	 * @throws Exception
	 */
	public static void checkRecursiveLineage(MyObject parent, MyObject child, SessionVars sVars) throws Exception {
		// let constructor handle new Table
		new MyLinkObject(parent, child, sVars);
		ArrayList<Integer> allLineage = new ArrayList<Integer>();
		// get the ancestors of the destination
		getAncestors(child, allLineage);
		if (!allLineage.isEmpty()) {
			int existingParentId = allLineage.iterator().next();
			MyObject tmpParent = parent.getNew().find(existingParentId);
			throw new Exception(
					"'" + child.getInstanceName() + "' already has ancestor '" + tmpParent.getInstanceName() + "'");
		}
		getAncestors(parent, allLineage);
		getChildrenRecursive(child, allLineage);
		getChildrenRecursive(parent, allLineage);
		if (allLineage.contains(parent.id))
			throw new Exception(
					parent.getInstanceName() + " is already in the " + child.getInstanceName() + " lineage.");

		if (allLineage.contains(child.id))
			throw new Exception(
					child.getInstanceName() + " is already in the " + parent.getInstanceName() + " lineage.");
		return;
	}

	/**
	 * checkLineage internal recursion
	 * 
	 * @param thisObject
	 * @param idsOfAncestors
	 */
	public static void getAncestors(MyObject thisObject, ArrayList<Integer> idsOfAncestors) {
		try {
			// using the first parent of getParents
			MyObject parent = thisObject.getParents(thisObject).iterator().next();
			idsOfAncestors.add(parent.id);
			getAncestors(parent, idsOfAncestors);
		} catch (Exception e) {
		}
	}

	static void getChildrenRecursive(MyObject thisObject, ArrayList<Integer> idsOfAncestors) throws Exception {
//		try {
		for (MyObject obj : thisObject.getChildren(thisObject)) {
			idsOfAncestors.add(obj.id);
			getChildrenRecursive(obj, idsOfAncestors);
		}
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
	}

	static void getMyChildrenRecursive(MyObject thisObject, ArrayList<Integer> idsOfAncestors, MyObject me)
			throws Exception {
		if (thisObject.getMyFileName().equals(me.getMyFileName())) {
			idsOfAncestors.add(thisObject.id);
			return;
		}
//		try {
		for (MyObject obj : thisObject.getChildren(thisObject)) {
			idsOfAncestors.add(obj.id);
			getChildrenRecursive(obj, idsOfAncestors);
		}
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
	}

	boolean isParentAndChildOfSameClass() throws Exception {
		return parent.getMyFileName().equals(child.getMyFileName());
	}

	public boolean isLoaded() {
		if (parent == null || child == null)
			return false;
		return parent.isLoaded() && child.isLoaded();
	}

	@Override
	public void newTable(SessionVars sVars) throws Exception {
		newTable(getMyFileName());

	}

	public boolean isParentChild() throws ExceptionCoding {
		return linkExists();
	}

	@Override
	public DoubleStrings extendAdd() {
		return new DoubleStrings();
	}

	@Override
	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	@Override
	public Strings extendNewTable() {
		return new Strings();
	}

	@Override
	public String getCanonicalName() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getFileName(MyObject parent, MyObject child, SessionVars sVars) throws Exception {
		// let constructor handle new Table for parent, child
		new MyLinkObject(parent, child, sVars);
		return (parent.getMyFileName() + CHILDSTRING + child.getMyFileName()).toLowerCase();
	}

	public static boolean linkExists(MyObject parent, MyObject child, SessionVars sVars) throws Exception {
		// let constructor handle new Table
		new MyLinkObject(parent, child, sVars);
		if (parent == null)
			throw new ExceptionCoding("null parent");
		if (!parent.isLoaded())
			return false;
		if (child == null)
			throw new ExceptionCoding("null child");
		if (!child.isLoaded())
			return false;
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQueryWithException("select * from " + getFileName(parent, child, sVars) + " where parentId='"
					+ parent.id + "' and childId='" + child.id + "'");
			return rs.next();
		} catch (Exception e) {
			return false;
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	public static MyLinkObject find(MyObject parent, MyObject child, SessionVars sVars) throws Exception {
		// let constructor handle new Table
		new MyLinkObject(parent, child, sVars);
		if (!parent.isLoaded())
			throw new Exception("parent is not loaded.");
		if (!child.isLoaded())
			throw new Exception("child is not loaded.");
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQueryWithException("select * from " + getFileName(parent, child, sVars) + " where parentId='"
					+ parent.id + "' and childId='" + child.id + "'");
			if (rs.next())
				return extractInfo(parent, child, rs, sVars);
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		throw new Exception("instance not found");

	}

	protected static MyLinkObject extractInfo(MyObject parent, MyObject child, ResultSet row, SessionVars sVars) throws Exception {
		MyLinkObject mlo = new MyLinkObject(parent, child, sVars);
		if (row == null) {
			throw new ExceptionCoding("null row");
		}
//		try {
		mlo.child.id = row.getInt("childId");
		mlo.parent.id = row.getInt("parentId");
//		} catch (SQLException e) {
//			Internals.dumpSQLExit(e);
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
		return mlo;
	}

	boolean isOrphan() throws Exception {
		if (!child.isLoaded())
			throw new Exception("child is not loaded");
		if (child.isRecursive()) {
			// use the recursive file name
			Connection co = null;
			MyStatement st = null;
			try {
				co = sVars.connection.getConnection();
				st = new MyStatement(co);
				return !(st.executeQuery(
						"SELECT * FROM " + getFileName(child, child, sVars) + " WHERE childId='" + child.id + "' LIMIT 1;")
						.next());
			} catch (Exception e) {
				return false;
			} finally {
				if (st != null)
					st.close();
				if (co != null)
					co.close();
			}
		} else {
			// use this file name
			Connection co = null;
			MyStatement st = null;
			try {
				co = sVars.connection.getConnection();
				st = new MyStatement(co);
				return !(st
						.executeQuery("SELECT * FROM " + getMyFileName() + " WHERE childId='" + child.id + "' LIMIT 1;")
						.next());
			} catch (Exception e) {
				return false;
			} finally {
				if (st != null)
					st.close();
				if (co != null)
					co.close();
			}
		}
	}

	public MyLinkObject update() throws Exception {
		if (!isLoaded())
			throw new Exception("must be loaded before update.<br>" + new Exception().getStackTrace()[0]);
		if (!parent.isLoaded())
			throw new Exception("parent not loaded.<br>" + new Exception().getStackTrace()[0]);
		if (!child.isLoaded())
			throw new Exception("child not loaded.<br>" + new Exception().getStackTrace()[0]);

		String setString = "";
		boolean fieldAdded = false;
		for (DoubleString db : extendUpdate()) {
			if (fieldAdded) {
				setString += ", " + db.field + "= \'" + db.value + "\'";
			} else {
				setString += db.field + "= \'" + db.value + "\'";
				fieldAdded = true;
			}
		}
// if there is nothing to update
		if (setString.isEmpty())
			return this;

		String update = "UPDATE " + getMyFileName();
		update += " SET ";
		update += setString;
		update += " WHERE parentId='" + parent.id + "' AND childId='" + child.id + "'";
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			int count = st.executeMyUpdate(update);
			if (count != 1)
				throw new Exception(getMyFileName() + " update failed with count " + count);
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		return this;
	}
}
