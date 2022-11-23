package com.security;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.ResultSet;
import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.MyConnection;
import com.db.MyStatement;
import com.db.SessionVars;
import com.db.Strings;
import com.forms.SearchTarget;

public class ClassAccess extends MyObject {
	public ClassAccess(SessionVars sVars) throws Exception {
		super(sVars);
		this.sVars = sVars;
		new Table().tableCreated(this, sVars);
	}

	public static final String NAME = "ClassAccess";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";
	public static final int NAMELENGTH = 30;
	public static final int NAMEMIN = 5;
	SessionVars sVars = null;

//	enum ACCESSTYPE {
//		NOTINITIALIZED, ACCESS, ACCESSANDCHANGE
//	}

	SearchTarget.EDITSELECTTYPE accessType;

//	public ClassAccess() throws Exception {
//	}

	public void clear() {
		super.clear();
		accessType = SearchTarget.EDITSELECTTYPE.NOTINITIALIZED;
	}

//	public ArrayList<ClassAccess> list(Anchor anchor) throws SQLException, Exception {
//		ArrayList<ClassAccess> roles = new ArrayList<ClassAccess>();
//		for (MyObject obj : listAll(anchor))
//			roles.add((ClassAccess) obj);
//		return roles;
//	}

	public ClassAccess addEditSelectType(Anchor anchor, MyObject obj, SearchTarget.EDITSELECTTYPE editSelectType)
			throws Exception {
		setInstanceName(obj.getLogicalName());
		accessType = editSelectType;
		addToDb(anchor);
		return this;
	}

//	public ClassAccess addAccessAndChange(MyObject obj) throws Exception {
//		setInstanceName(obj.getLogicalName());
//		accessType = SearchTarget.EDITSELECTTYPE.EDITANDSELECT;//SearchTarget.EDITSELECTTYPE.EDITANDSELECT;
//		addToDb();
//		return this;
//	}
//
//	public ClassAccess addAccess(MyObject obj) throws Exception {
//		setInstanceName(obj.getLogicalName());
//		accessType = SearchTarget.EDITSELECTTYPE.SELECT;//SearchTarget.EDITSELECTTYPE.SELECTSearchTarget.EDITSELECTTYPE.SELECTSearchTarget.EDITSELECTTYPE.SELECTSearchTarget.EDITSELECTTYPE.SELECT;
//		addToDb();
//		return this;
//	}

	public boolean canAccessAndChange() {
		return accessType == SearchTarget.EDITSELECTTYPE.EDITANDSELECT;
	}

	public boolean canAccess() {
		return accessType == SearchTarget.EDITSELECTTYPE.SELECT;
	}

	public ClassAccess find(Anchor anchor, String name, SearchTarget.EDITSELECTTYPE accessType) throws Exception {
		String query = "SELECT * FROM " + getMyFileName() + " WHERE name='" + name + "' AND access='"
				+ accessType.ordinal() + "' AND anchorid='" + anchor.id + "'";
		Connection conn = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			rs = st.executeQuery(query);
			if (rs.next()) {
				this.extractInfo(rs);
				return this;
			} else {
				throw new Exception("name:" + name + " not found in " + getMyFileName());
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

	@Override
	public String getAName() {
		return ANAME;
	}

	@Override
	public String getLogicalName() {
		return NAME;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public int hashCodeReminder() {
		return 0;
	}

	public int hashCode() {
		return id;
	}

	@Override
	public boolean equalsReminder(Object obj) {
		return false;
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof ClassAccess;
	}

	@Override
	public MyObject getNew() throws Exception {
		return new ClassAccess(sVars);
	}

//	/**
//	 * set the anchor, name, and access type
//	 * 
//	 * @param anchor
//	 * @param name
//	 * @param accessType
//	 * @return
//	 * @throws Exception
//	 */
//	public ClassAccess setAnchorName(Anchor anchor, String name) throws Exception {
//		setInstanceName(name);
//		anchorId = anchor.id;
//		return this;
//	}

//	public ClassAccess setAccess() {
//		accessType = ACCESSTYPE.ACCESS;
//		return this;
//	}
//
//	public ClassAccess setAccessAndChange() {
//		accessType = SearchTarget.EDITSELECTTYPE.EDITANDSELECT;
//		return this;
//	}

	@Override
	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		ret.add(new DoubleString("access", "" + accessType.ordinal()));
		return ret;
	}

	@Override
	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	@Override
	protected ClassAccess extractInfo(ResultSet row) throws Exception {
		accessType = SearchTarget.EDITSELECTTYPE.values()[row.getInt("access")];
		super.extractInfo(row);
		return this;
	}

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add("`access` int(11)");
		return ret;
	}

//	public ClassAccess sanityClassAccess() throws Exception {
//		sanity();
//		if (accessType == ACCESSTYPE.NOTINITIALIZED)
//			throw new Exception("accessType not set");
//		if (anchorId == 0)
//			throw new Exception("anchor not set");
//		return this;
//	}
	static int goingIn = 0;
	static int goingOut = 0;

	/**
	 * add the instance to the database
	 * 
	 * @throws Exception
	 */
	private ClassAccess addToDb(Anchor anchor) throws Exception {
		goingIn++;
		// see if this is a duplicate of a record already in the database
		ClassAccess ca = null;
		try {
			ca = find(anchor, getInstanceName(), accessType);
			goingOut++;
			return ca;
		} catch (Exception e) {
		}
		goingOut++;
		// add if it wasn't found
		return (ClassAccess) super.add(new Anchor(sVars));
	}

	@Override
	public boolean equals(Object exhibit) {
		if (!(exhibit instanceof ClassAccess))
			return false;
		if (((ClassAccess) exhibit).id == id)
			return true;
		return false;
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		return new MyObjects();
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		return new MyObjects();
	}

}
