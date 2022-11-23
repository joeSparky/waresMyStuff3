package com.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.db.DoubleString;
import com.db.DoubleStrings;
//import com.db.MyConnection;
import com.db.MyStatement;
import com.db.SessionVars;
import com.db.Strings;
import com.forms.EndOfInputException;
import com.forms.FormsArray;
import com.forms.FormsMatrixDynamic;
import com.forms.IdAndString;
import com.forms.SearchTarget;
import com.forms.SmartForm;
import com.forms.Utils;

/**
 * common methods for users, roles, and warehouses
 * 
 * @author Joe
 *
 */
public abstract class MyObject implements HasTableInterface {

	protected MyObject(SessionVars sVars) {
//		new Table().tableCreated(this, sVars);
		clear();
		this.sVars = sVars;
	}

	protected SessionVars sVars = null;
	public int id;
	private Name nameInstance = new Name();

	/**
	 * Search string for selection in editor. Not persistent.
	 */
	public String searchString = null;

	/**
	 * a logical name, such as "a User", "a Role", or "an Exhibit"
	 * 
	 * @return
	 */
	public abstract String getAName();

	/**
	 * get the logical name of the class, such as "User", "Exhibit"
	 * 
	 * @return
	 */
	public abstract String getLogicalName();

	public abstract String getMyFileName() throws Exception;

	public final boolean isLoaded() {
		return id > 0;
	}

	public MyObject find(String name) throws Exception {
		String query = "SELECT * FROM " + getMyFileName() + " WHERE name='" + name + "'";
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQuery(query);

			if (rs.next()) {
				this.extractInfo(rs);
				return this;
			} else {
				throw new Exception("name:" + name + " not found in " + getMyFileName() + ".<br>"
						+ new Exception().getStackTrace()[0]);
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	public MyObject find(Anchor anchor, String name) throws Exception {
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;

		// if the record was found
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			String query = "SELECT * FROM " + getMyFileName() + " WHERE name='" + name + "' AND anchorid='" + anchor.id
					+ "'";
			rs = st.executeQuery(query);

			if (rs.next()) {
				this.extractInfo(rs);
				return this;
			} else {
				throw new Exception("name:" + name + " not found in " + getMyFileName() + ".<br>"
						+ new Exception().getStackTrace()[0]);
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
//		return this;
	}

	public MyObject find(int id) throws Exception {
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			String query = "SELECT * FROM " + getMyFileName() + " WHERE id='" + id + "'";
			rs = st.executeQuery(query);

			if (rs.next()) {
				this.extractInfo(rs);
				return this;
			} else {
				throw new Exception(
						"id:" + id + " not found in " + getMyFileName() + ".<br>" + new Exception().getStackTrace()[0]);
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	protected MyObject extractInfo(ResultSet row) throws Exception {
		if (hasNameInDb())
			nameInstance.extractInfo(row);
		id = row.getInt("id");
		if (hasAnchor())
			anchorId = row.getInt(ANCHORID);
		return this;
	}

	protected IdAndString extractIdAndString(ResultSet row) throws Exception {
		IdAndString ids = new IdAndString();
		if (hasNameInDb())
			ids.string = row.getString("name");
		ids.id = row.getInt("id");

		return ids;
	}

	/**
	 * @Override public int hashCode() { return id; }
	 */
	public abstract int hashCodeReminder();

	/**
	 * @Override public boolean equals(Object exhibit) { if (exhibit instanceof
	 *           User) { return id == ((User) exhibit).id; } return false; }
	 */
	public abstract boolean equalsReminder(Object obj);

	public abstract boolean equalsObject(Object obj);

	// public abstract void clear();

	/**
	 * clear the MyObject fields
	 * 
	 * @throws Exception
	 */
//	public void clear() throws Exception {
//		clearWithoutRemember();
//		new RememberClass().remember(this);
//	}

	public void clear() {
		id = 0;
		anchorId = 0;
		if (hasNameInDb())
			nameInstance.clear();
		searchString = "";
//		if (!(this instanceof RememberClass))
//			new RememberClass().remember(this);
	}

	/**
	 * throw an exception if the object can not be deleted
	 * 
	 * @throws Exception
	 */
	public final void deleteTestDELETE(MyObjectsArray objs) throws Exception {
		if (this instanceof User && ((User) this).isDefaultUser())
			throw new Exception(
					"Can not delete the default " + User.NAME + ".<br>" + new Exception().getStackTrace()[0]);
		MyObjects kids = listChildren(objs);
		int childCount = kids.size();

		if (childCount > 0) {
			String exString = "This " + this.getLogicalName() + ":" + this.nameInstance.getname() + " has " + childCount
					+ " children.";
			for (MyObject o : kids)
				exString += "<br>" + o.getLogicalName() + ":" + o.nameInstance.getname();
			exString += "<br>" + new Exception().getStackTrace()[0];
			throw new Exception(exString);
		}
	}

	/**
	 * return a list of my children of type child
	 * 
	 * @param child
	 * @return
	 * @throws Exception
	 */
	public MyObjects listChildren(MyObject childType) throws Exception {
		return new MyLinkObject(this, childType, sVars).listChildrenOfParent();
	}

	public void deleteLinkUnconditionally(MyObject child) throws Exception {
//		try {
		new MyLinkObject(this, child, sVars).deleteUnconditionally();
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
	}

	public void deleteLinkUnconditionallyDELETE(MyObjectsArray objs, MyObject child) {
//		try {
//			new MyLinkObject(this, child).deleteUnconditionally(objs);
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
	}

	/**
	 * delete the link(s) to an instances parents delete the link(s) to an instances
	 * children delete the instance
	 * 
	 * @param objs
	 * @throws Exception
	 */
	public void deleteUnconditionally() throws Exception {
		if (this instanceof User && ((User) this).isDefaultUser())
			throw new Exception(
					"Can not delete the default " + User.NAME + ".<br>" + new Exception().getStackTrace()[0]);
		if (!this.isLoaded()) {
			// must have already been deleted
			clear();
			return;
		}

		for (MyObject parent : listParentsClasses())
			new MyLinkObject(parent, this, sVars).deleteLinksUnconditionallyChild();

		for (MyObject thisChildClass : listChildrensClasses())
			new MyLinkObject(this, thisChildClass, sVars).deleteLinksUnconditionallyParent();
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			st.executeMyUpdate("delete from " + getMyFileName() + " where id='" + id + "'");
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		clear();
	}

	public void deleteTest() throws Exception {
		if (this instanceof User && ((User) this).isDefaultUser())
			throw new Exception(
					"Can not delete the default " + User.NAME + ".<br>" + new Exception().getStackTrace()[0]);
		if (!this.isLoaded()) {
			// must have already been deleted
			clear();
			return;
		}

//		for (MyObject parent : listParentsClasses())
//			new MyLinkObject(parent, this).deleteTest();
//		for (MyObject thisChildClass : listChildrensClasses())
//			new MyLinkObject(this, thisChildClass).deleteTest();
	}

	protected void add() throws Exception {
		DoubleStrings adds = new DoubleStrings();
		if (hasNameInDb())
			adds.addAll(nameInstance.extendAdd());
		// from the subclass
		adds.addAll(extendAdd());
		String fields = "";
		String values = "";
		boolean fieldAdded = false;
		// if (adds.isEmpty())
		// Internals.dumpStringExit("no adds");
		for (DoubleString db : adds) {
			if (fieldAdded) {
				fields += ", " + db.field;
				values += ", '" + db.value + "'";
			} else {
				fields += db.field;
				values += "'" + db.value + "'";
				fieldAdded = true;
			}
		}
		if (hasAnchor()) {
			if (fieldAdded) {
				fields += "," + ANCHORID;
				values += ",'" + anchorId + "'";
			} else {
				fields += ANCHORID;
				values += "'" + anchorId + "'";
				fieldAdded = true;
			}
		}
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			this.id = st
					.executeUpdateKey("INSERT INTO " + getMyFileName() + " (" + fields + ") VALUES (" + values + ")");
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	/**
	 * add an instance using myObject as an anchor
	 * 
	 * @param myAnchor
	 * @throws Exception
	 */
	public MyObject add(MyObject myObject) throws Exception {
		if (!myObject.isLoaded())
			throw new Exception(
					myObject.getInstanceName() + " is not loaded.<br>" + new Exception().getStackTrace()[0]);

		if (hasAnchor()) {
			// use myObject as an anchor
			Anchor anchor = new Anchor(sVars);
			anchor.add(myObject);
			// anchor is now set up, add this instance with the anchor, or use null if the
			// instance does not have an anchor
			add(anchor);
		} else
			add();
		return this;
	}

	/**
	 * add an instance using anchor
	 * 
	 * @param myAnchor
	 * @throws Exception
	 */
	public MyObject add(Anchor myAnchor) throws Exception {
		if (!hasAnchor())
			throw new Exception(getMyFileName() + " does not have an anchor.<br>" + new Exception().getStackTrace()[0]);
		anchorId = myAnchor.id;
		try {
			add();
		} catch (Exception e) {
			clear();
			throw e;
		}
		// if this instance is going to anchor itself
		if (!myAnchor.isLoaded()) {
			// put the anchor in the anchor table
			anchorId = myAnchor.add(this).id;
			// save the anchor id in the instance
			update();
		}
		// add to the orphan list
//		for (MyObject parent : this.listParentsClasses())
//			new Orphan(myAnchor, parent, this).add();
		return this;
	}

	/**
	 * fields and values for add
	 * 
	 * @return
	 * @throws Exception
	 */
	public DoubleStrings extendAdd() {
		return new DoubleStrings();
	}

	/**
	 * return true if a link exists between you and your sibling to the right
	 * 
	 * @param you
	 * @param toTheRight
	 * @return
	 * @throws Exception
	 */
	public boolean linkToChildExists(MyObject child) throws Exception {
//		try {
		return new MyLinkObject(this, child, sVars).linkExists();
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
//		return false;
	}

	// default entry point
	public void update() throws Exception {
		update(hasNameInDb());
	}

	// entry point for classes that do not have a nameInstance
	public void update(boolean hasName) throws Exception {
		String insert = "UPDATE " + getMyFileName() + " SET ";
		DoubleStrings updates = new DoubleStrings();
		if (hasName)
			updates.addAll(nameInstance.extendUpdate());
		updates.addAll(extendUpdate());
		// set true when we've added the first field / value pair
		boolean fieldUpdated = false;
		for (DoubleString db : updates) {
			if (fieldUpdated)
				insert += ", ";
			fieldUpdated = true;
			// first field / value pair. no leading comma
			insert += db.field + "='" + db.value + "'";

		}
		if (fieldUpdated)
			insert += ", ";
		fieldUpdated = true;
		if (hasAnchor())
			insert += " " + ANCHORID + "='" + anchorId + "'";

		insert += " WHERE id='" + id + "'";
		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			if (st.executeMyUpdate(insert) != 1)
				throw new Exception(getMyFileName() + " update failed.<br>" + new Exception().getStackTrace()[0]);
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	/**
	 * field1, value1 field2, value2 field3, value3
	 * 
	 * @return
	 */
	public DoubleStrings extendUpdate() {
		return new DoubleStrings();
	}

	/**
	 * get another instance of this object
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract MyObject getNew() throws Exception;

	/**
	 * return a list of objects that are linked with obj and to the right. For
	 * example, listRight(role) would get all warehouses linked to the role.
	 * (Warehouses are right of roles.)
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public MyObjects listChildren(MyObjectsArray stack) throws Exception {
		MyObjects objs = new MyObjects();

		if (isRecursive()) {
			return new MyLinkObject(this, this, sVars).listChildrenOfParent();
		} else {
			int index = stack.findObjectUsingClassName(this);
			// if we're at the bottom of the stack, no children
			if (index >= stack.size() - 1)
				return objs;

			MyObjects kidObjs = null;
//			try {
			kidObjs = new MyLinkObject(this, stack.get(index + 1), sVars).listChildrenOfParent();
//			} catch (Exception e) {
//				Internals.dumpExceptionExit(e);
//			}
			for (MyObject mlo : kidObjs) {
//				try {
				MyObject myObj = stack.get(index + 1).getNew();
				objs.add(myObj.find(mlo.id));
//				} catch (Exception e) {
//					Internals.dumpExceptionExit(e);
//				}
			}
			return objs;
		}
	}

	public final void newTable(SessionVars sVars) throws Exception {
		Strings strings = new Strings();
		if (hasNameInDb())
			strings.addAll(nameInstance.extendNewTable());
		strings.addAll(extendNewTable());
		String newTab = "CREATE TABLE `" + getMyFileName() + "` (" + "`id` int(11) auto_increment";
		for (String s : strings) {
			newTab += ", " + s;
		}
		newTab += ", `" + ANCHORID + "` int(11) DEFAULT 0";
		newTab += ", PRIMARY KEY  (`id`)" + ") ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8";

		Connection co = null;
		MyStatement st = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			st.executeMyUpdate("DROP TABLE IF EXISTS `" + getMyFileName() + "`");
			st.executeMyUpdate(newTab);
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		newTableMonitor();
	}

	// let the instances react to a new table
	public void newTableMonitor() throws Exception {
	}

	public Strings extendNewTable() {
		return new Strings();
	}

	/**
	 * list all instances
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public MyObjects listAll() throws SQLException, Exception {
		MyObjects objs = new MyObjects();
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQuery("SELECT * from " + getMyFileName());
			MyObject tmp;
			while (rs.next()) {
				tmp = getNew();
				objs.add(tmp.extractInfo(rs));
			}
			return objs;
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	/**
	 * list all instances
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public MyObjects listAll(MyObject obj) throws Exception {
		if (!hasAnchor())
			throw new Exception(getMyFileName() + " does not have an anchor.<br>" + new Exception().getStackTrace()[0]);
		if (!obj.isLoaded())
			throw new ExceptionCoding("obj not loaded");
		Anchor anchor = new Anchor(sVars);
		anchor.find(obj);
		MyObjects objs = new MyObjects();
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		MyObject tmp;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQuery("SELECT * from " + getMyFileName() + " where " + ANCHORID + "='" + anchor.id + "'");
			while (rs.next()) {
				// tmp = new MyObjectInstance();
				tmp = getNew();
				objs.add(tmp.extractInfo(rs));
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		return objs;
	}

	public MyObjects listAll(Anchor listAnchor) throws SQLException, Exception {
		MyObjects objs = new MyObjects();
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		MyObject tmp;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQuery(
					"SELECT * from " + getMyFileName() + " where " + ANCHORID + "='" + listAnchor.id + "'");
			while (rs.next()) {
				tmp = getNew();
				objs.add(tmp.extractInfo(rs));
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		return objs;
	}

//	public IdAndStrings listAllIdAndStrings(Anchor listAnchor) throws SQLException, Exception {
//		IdAndStrings objs = new IdAndStrings();
//		Connection co = null;
//		MyStatement st = null;
//		ResultSet rs = null;
//		MyObject tmp;
//		try {
//			co = sVars.connection.getConnection();
//			st = new MyStatement(co);
//			rs = st.executeQuery(
//					"SELECT id, name from " + getMyFileName() + " where " + ANCHORID + "='" + listAnchor.id + "'");
//			while (rs.next()) {
//				tmp = getNew();
//				objs.add(tmp.extractIdAndString(rs));
//			}
//		} finally {
//			if (rs != null)
//				rs.close();
//			if (st != null)
//				st.close();
//			if (co != null)
//				co.close();
//		}
//		return objs;
//	}

	public MyObjects listAllParentsOfType(MyObject parent) throws Exception {
		return new MyLinkObject(parent, this, sVars).listParentsOfChild();
	}

	public boolean equals(MyObject exhibit) throws Exception {
		if (getMyFileName().equals(exhibit.getMyFileName()) && id == exhibit.id)
			return true;
		return false;
	}

	public boolean isSameClass(MyObject exhibit) throws Exception {
		return getMyFileName().equals(exhibit.getMyFileName());
	}

	@Override
	public int hashCode() {
		return id;
	}

	public FormsArray getNameForm(String boxName,
//			SmartForm.INSTATE inState, 
			boolean isLoaded) throws Exception {
		return nameInstance.getNameForm(boxName, this,
//				inState, 
				isLoaded);
	}

	public boolean extractName(SessionVars sVars, String boxName) throws Exception {
		return nameInstance.extractname(sVars, boxName);
	}

	public boolean nameChanged(SessionVars sVars, String boxName) throws Exception {
		return nameInstance.nameChanged(sVars, boxName);
	}

	private static final String ANCHORID = "anchorId";

	/**
	 * record number of anchor in anchor database
	 */
	public int anchorId = 0;

	public void sanity() throws Exception {
		if (hasNameInDb()) {
			if (nameInstance == null)
				throw new Exception(
						"Please set up the " + getLogicalName() + " name.<br>" + new Exception().getStackTrace()[0]);
			nameInstance.sanity();
		}
	}

	public SmartForm getSelector(FormsMatrixDynamic fmd, SessionVars sVars) throws Exception {
		return new SelectForm(fmd, sVars);
	}

	public MyLinkObject getMyLinkObject(MyObject child) throws Exception {
//		try {
		return new MyLinkObject(this, child, sVars);
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
//		return null;
	}

	/**
	 * return the parent of the instance
	 * 
	 * @param parentClass
	 * @return
	 * @throws Exception
	 */
	public MyObjects getParents(MyObject parentClass) throws Exception {
		return new MyLinkObject(parentClass, this, sVars).listParentsOfChild();
	}

	public MyObject getSingleParent(MyObject parentClass) throws Exception {
		MyObjects objs = getParents(parentClass);
		if (objs.size() == 0)
			throw new Exception("Please create a link between " + parentClass.getMyFileName() + " and "
					+ getMyFileName() + ".<br>" + new Exception().getStackTrace()[0]);
		if (objs.size() > 1)
			throw new Exception(objs.size() + " parents for " + this.nameInstance.getname() + " for parent class "
					+ parentClass.getCanonicalName() + ".<br>" + new Exception().getStackTrace()[0]);
		return objs.iterator().next();
	}

	public MyObjects getChildren(MyObject childClass) throws Exception {
		if (!isLoaded())
			throw new Exception(getLogicalName() + " parent is not loaded.<br>" + new Exception().getStackTrace()[0]);
		return new MyLinkObject(this, childClass, sVars).listChildrenOfParent();
	}

	/**
	 * assign a child
	 * 
	 * @param child
	 * @throws Exception
	 */
	public void addChild(MyObject child) throws Exception {
		MyLinkObject mlo = new MyLinkObject(this, child, sVars);
		if (mlo.linkExists())
			throw new Exception("link already exists.<br>" + new Exception().getStackTrace()[0]);
		mlo.add();
	}

	/**
	 * return an exception if deleting the link between this and its parent will
	 * cause problems. nothing is deleted
	 * 
	 * @param parentType
	 * @throws Exception
	 */
	public void testDeleteLinkToParent(MyObject parentType) throws Exception {
		MyLinkObject mlo = new MyLinkObject(parentType, this, sVars);
		mlo.find();
		mlo.deleteTest();
	}

	/**
	 * delete the link between this and its parent.
	 * 
	 * @param parentType
	 * @throws Exception
	 */
	public void deleteLinkToParent(MyObject parentType) throws Exception {
		MyLinkObject mlo = new MyLinkObject(parentType, this, sVars);
		mlo.find();
		mlo.deleteUnconditionally();
	}

	public String getNameOfLink(MyObject child) {
		return getAName() + " to " + child.getAName();
	}

	/**
	 * has a name instance
	 * 
	 * @return
	 */

	public boolean hasNameInDb() {
		return true;
	}

	// allow the instance to tell us that it does not have an anchor. (company does
	// not have an anchor)
	public boolean hasAnchor() {
		return true;
	}

	public String getInstanceName() {
		return nameInstance.getname();
	}

	public String getInstanceName(MyObject parentClass) {
		MyObject singleParent = null;
		try {
			singleParent = getSingleParent(parentClass);
		} catch (Exception e) {
			return "Orphan " + getInstanceName();
		}
		return singleParent.getInstanceName() + " " + getInstanceName();
	}

	public void setInstanceName(String name) throws Exception {
		nameInstance.setname(name);
	}

	public void extractInstanceName(ResultSet rs) throws SQLException {
		nameInstance.extractInfo(rs);
	}

	public DoubleStrings extendUpdateInstanceName() {
		return nameInstance.extendUpdate();
	}

	public void clearInstanceName() {
		nameInstance.clear();
	}

	public void deleteTest(MyObject child) throws Exception {
		new MyLinkObject(this, child, sVars).deleteTest();
	}

	public void linkSanity(MyObject child) throws Exception {
		new MyLinkObject(this, child, sVars).sanity();
	}

	public MyObject getInstanceOfAnchor() throws Exception {
		if (hasAnchor()) {
//			Internals.dumpStringContinue("after hasAnchor");
			return new Anchor(sVars).find(anchorId).getInstanceOfAnchor();
		}
		throw new Exception(getMyFileName() + " does not use anchors.<br>" + new Exception().getStackTrace()[0]);
	}

	/**
	 * return the anchor of an object
	 * 
	 * @return
	 */

	public String getCanonicalName() {
		return getClass().getCanonicalName();
	}

	public Anchor getAnchor() throws Exception {
		if (hasAnchor())
			return new Anchor(sVars).find(anchorId);
		throw new Exception(getMyFileName() + " does not use anchors.<br>" + new Exception().getStackTrace()[0]);
	}

	public boolean isRecursive() {
		return false;
	}

	public void moveChildrenOfThisParentToNewParent(MyObject newParent, MyObject childType) throws Exception {
		if (this.isSameClass(childType)) {
			moveToNewParentRecursive(newParent);
			return;
		}
		if (!this.isLoaded())
			throw new Exception(
					"MyObject:" + this.getInstanceName() + " is not loaded.<br>" + new Exception().getStackTrace()[0]);
		if (!newParent.isLoaded())
			throw new Exception("newParent '" + newParent.getInstanceName() + "' is not loaded.<br>"
					+ new Exception().getStackTrace()[0]);
		for (MyObject thisChild : listChildren(childType)) {
			thisChild.testDeleteLinkToParent(this);
			thisChild.deleteLinkToParent(this);
			newParent.addChild(thisChild);
		}
	}

	/**
	 * this object is linked to other objects of the same class. Delete the link
	 * that points to this object and create a link from newParent to this object.
	 * 
	 * @param newParent
	 * @throws Exception
	 */
	public void moveToNewParentRecursive(MyObject newParent) throws Exception {
		if (!this.isLoaded())
			throw new Exception(
					"MyObject:" + this.getInstanceName() + " is not loaded.<br>" + new Exception().getStackTrace()[0]);
		if (!newParent.isLoaded())
			throw new Exception("newParent '" + newParent.getInstanceName() + "' is not loaded.<br>"
					+ new Exception().getStackTrace()[0]);
		if (!this.isRecursive())
			throw new Exception("MyObject:" + this.getInstanceName() + " is not recursive.<br>"
					+ new Exception().getStackTrace()[0]);
		if (!newParent.isRecursive())
			throw new Exception("newParent '" + newParent.getInstanceName() + "' is not recursive.<br>"
					+ new Exception().getStackTrace()[0]);
		if (!this.getMyFileName().equals(newParent.getMyFileName()))
			throw new Exception("MyObject:" + this.getMyFileName() + " is different than newParent:"
					+ newParent.getMyFileName() + ".<br>" + new Exception().getStackTrace()[0]);
		// source can not be an ancestor of the destination
		if (this.anAncestorOf(newParent))
			throw new Exception(getInstanceName() + " can not be moved to " + newParent.getInstanceName() + ".<br>"
					+ new Exception().getStackTrace()[0]);

		MyLinkObject mlo = new MyLinkObject(this, this, sVars);
		MyObjects objs = mlo.listParentsOfChild();
		switch (objs.size()) {
		case 0:
			break;
		case 1:
			MyObject parent = objs.iterator().next();
			mlo = new MyLinkObject(parent, this, sVars);
			mlo.deleteTest();
			mlo.deleteUnconditionally();
			break;
		default:
			throw new Exception("more than one parent in recursive links.<br>" + new Exception().getStackTrace()[0]);
		}
		newParent.addChild(this);
	}

	public MyObject reChild(MyObject oldChild, MyObject newChild) throws Exception {
		if (!isLoaded())
			throw new Exception("object not loaded.<br>" + new Exception().getStackTrace()[0]);
		if (!oldChild.isLoaded())
			throw new Exception("oldChild not loaded.<br>" + new Exception().getStackTrace()[0]);
		if (!newChild.isLoaded())
			throw new Exception("newChild not loaded.<br>" + new Exception().getStackTrace()[0]);
		MyLinkObject mlo = new MyLinkObject(this, oldChild, sVars);
		mlo.deleteTest();
		mlo.deleteUnconditionally();
		new MyLinkObject(this, newChild, sVars).add();
		return this;
	}

//	/**
//	 * move the parents of this object to be parents of newChild
//	 * 
//	 * @param newParent
//	 * @param childType
//	 * @throws Exception
//	 */
//	public void moveParentsToNewChildDELETE(MyObject newChild, MyObject parentType) throws Exception {
//		for (MyObject parent : listAllParentsOfType(parentType)) {
//			parent.moveToNewChild(this, newChild);
//		}
//	}

	// throw an exception if adding a parent would create a problem
	public void okToAddParent(MyObject newParent) throws Exception {

	}

	/**
	 * for recursive classes, throw an exception if this is found in the ancestral
	 * tree.
	 * 
	 * @param done
	 * @param descendant
	 * @throws Exception
	 */
	private void throwAncestor(boolean done, MyObject descendant) throws Exception {
		if (done)
			return;
		if (this.equals(descendant)) {
			throw new Exception(getInstanceName() + " found" + ".<br>" + new Exception().getStackTrace()[0]);
		}
		MyObjects objs = descendant.getParents(descendant);
		if (objs.isEmpty()) {
			done = true;
			return;
		}
		for (MyObject nextParent : objs) {
			throwAncestor(done, nextParent);
		}
	}

	/**
	 * look for this in the ancestors of descendant
	 * 
	 * @param descendant
	 * @return
	 * @throws Exception
	 */
	public boolean anAncestorOf(MyObject descendant) throws Exception {
		if (!isRecursive())
			throw new Exception("not recursive");
		if (!getMyFileName().equals(descendant.getMyFileName()))
			throw new Exception("not the same class");
		if (this.equals(descendant))
			return false;
		try {
			throwAncestor(false, descendant);
			return false;
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * return a list of classes that could be parents of this instance
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract MyObjects listParentsClasses() throws Exception;

	/**
	 * return a list of classes that could be children of this instance
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract MyObjects listChildrensClasses() throws Exception;

	public boolean childExists(MyObject child) {

		try {
			return new MyLinkObject(this, child, sVars).linkExists();
		} catch (Exception e) {
			return false;
		}

	}

	public boolean isOrphan(MyObject parentType) throws Exception {
		return new MyLinkObject(parentType, this, sVars).isOrphan();
	}

	public final String SEARCHBOX = Utils.getNextString();

	/**
	 * a form for searching for an object by name
	 * 
	 * @return
	 * @throws Exception
	 */
	public FormsArray getSearchForm() throws Exception {
		FormsArray ret = new FormsArray();
		if (isLoaded())
			return ret;
		ret.newLine();
		ret.startRow();
		ret.textBox(SEARCHBOX, SearchTarget.KEYWORDLENGTH, "Search for a " + getLogicalName(), searchString, false,
				false);
		ret.endRow();
		return ret;
	}

	public FormsArray extractSearchFormParams(SessionVars sVars) throws EndOfInputException, Exception {
		FormsArray ret = new FormsArray();
		if (sVars.hasParameterKey(SEARCHBOX) && (!sVars.getParameterValue(SEARCHBOX).equals(searchString))) {
			searchString = sVars.getParameterValue(SEARCHBOX).toLowerCase();
			
//			ret.addAll(callBackVar.callBack(sVars, new CalledInfo(callBackVar, true, FormsArray.STATUS.TRYAGAIN, null)));
			throw new EndOfInputException(ret);
		}
		return ret;
	}

	public final String NAMEBOX = Utils.getNextString();

//	public FormsArray editOrAddForm(
//	// SmartForm.INSTATE inState
//	) throws Exception {
//		FormsArray ret = new FormsArray();
//		ret.newLine();
//		ret.startTable();
//		if (isLoaded())
//			ret.rawText("Edit the name of " + getLogicalName());
//		else
//			ret.rawText("Add a new " + getLogicalName());
//		ret.addAll(getNameForm(NAMEBOX, isLoaded()));
//		ret.endTable();
//		return ret;
//	}

//	public FormsArray extractEditAddFormParams(SessionVars sVars, FormsMatrix fm) throws Exception {
//		FormsArray ret = new FormsArray();
//		if (nameChanged(sVars, NAMEBOX)) {
//			extractName(sVars, NAMEBOX);
//			MyObject tmp = this.getNew();
//			boolean nameFound = false;
//			try {
//				tmp.find(this.getInstanceName());
//				nameFound = true;
//			} catch (Exception e) {
//			}
//			if (nameFound) {
//				ret.errorToUser(this.getInstanceName() + " already exists.");
//				// clear the name from the instance
//				this.clear();
//				throw new EndOfInputException(ret);
//			}
//			ret.addAll(doSanityUpdateAddTryAgain(fm));
//			throw new EndOfInputException(ret);
//		}
//		return ret;
//	}

	public FormsArray doSanityUpdateAddTryAgain(FormsMatrixDynamic fm) throws Exception {
		FormsArray ret = new FormsArray();
		sanity();
		if (isLoaded()) {
			// update the object with the name change
			update();
			// inform the user about the update
			ret.rawText(getLogicalName() + " '" + getInstanceName() + "' updated.<br>");
		} else {
			// new object
			add(fm.getAnchor());
			// inform the user about the new object
			ret.rawText(getLogicalName() + " '" + getInstanceName() + "' added.<br>");
		}
		return ret;
//		throw new EndOfInputException(ret);
	}
//	protected AttachToForm attachToForm = null;

//	// allow the instances to override the parts of forms
//	public AttachToForm getAttachToForms(FormsMatrixDynamic fm) throws Exception {
//		return new AttachToForm(fm);
//	}
//
//	// when the instance is at the top or bottom of the stack, benign method
//	public AttachToForm getAttachToFormsFiller() {
//		return new AttachToForm();
//	}

	// database column name
	public static final String INVENTORYFIELDNAME = "inventoryDate";
	// number of oldest records to present to the user
	public static final int INVENTORYRECORDS = 5;

	// if the object has a date field indicating its last inventory date
	public boolean hasInventoryField() {
		return false;
	}

	public FormsArray markAsInventoried() throws Exception {
		return new FormsArray();
	}

	/**
	 * if this object has an inventory link with a child
	 * 
	 * @param child
	 * @return
	 */
	public boolean hasInventoryLinkWith(MyObject child) {
		return false;
	}
}
