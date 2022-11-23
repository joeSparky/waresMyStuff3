package com.security;

import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.Strings;

/**
 * a role is a collection of class names, the instances of which can be edited
 * by the user. a role also contains an enumeration. One of the enumerations can
 * override the collection of class names. This special enumeration denotes a
 * super user that can edit any class instance. The collection is implemented as
 * soft links between a role and EditControl.
 * 
 * @author joe
 * 
 */
public class Role extends MyObject {
	public static final String NAME = "Role";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";
	public static final int NAMELENGTH = 30;
	public static final int NAMEMIN = 5;

	public enum ROLETYPE {
		NOTINITIALIZED, NORMALUSER, SUPERUSER
	};

	public ROLETYPE roleType = ROLETYPE.NOTINITIALIZED;

	public Role(SessionVars sVars) throws Exception {
		super(sVars);
		clear();
		new Table().tableCreated(this, sVars);
	}

	// public Permissions permissions = null;

	/**
	 * clear the instance
	 * 
	 * @throws Exception
	 */
	public void clear() {
		roleType = ROLETYPE.NOTINITIALIZED;
		super.clear();
	}

	@Override
	public void sanity() throws Exception {
		if (roleType == ROLETYPE.NOTINITIALIZED)
			throw new Exception("roleType not initialized.");
		super.sanity();
	}

	/**
	 * extract the information from the record to the object
	 * 
	 * @param row results set
	 * @throws Exception
	 */
	@Override
	protected Role extractInfo(ResultSet row) throws Exception {
		roleType = ROLETYPE.values()[row.getInt("roleType")];
		super.extractInfo(row);
		return this;
	}

	@Override
	public String getAName() {
		return ANAME;
	}

	@Override
	public int hashCodeReminder() {
		return 0;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equalsReminder(Object obj) {
		return false;
	}

	@Override
	public boolean equals(Object exhibit) {
		if (exhibit instanceof Role) {
			return id == ((Role) exhibit).id;
		}
		return false;
	}

	// Role target = null;

//	@Override
//	public BaseEditForm getEditor(BaseEditParams params) {
//		return new RoleEditForm(params);
//
//	}

	@Override
	public MyObject getNew() throws Exception {
		return new Role(sVars);
	}

	@Override
	public String getLogicalName() {
		return NAME;
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Role;
	}

	@Override
	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		ret.add(new DoubleString("roleType", "" + roleType.ordinal()));
		return ret;
	}

	@Override
	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add("`roleType` int(11) DEFAULT 0");
		return ret;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
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
