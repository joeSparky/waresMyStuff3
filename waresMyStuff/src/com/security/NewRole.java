package com.security;

import java.lang.invoke.MethodHandles;
import java.sql.SQLException;
import java.util.ArrayList;

import com.db.SessionVars;
import com.forms.SearchTarget;

//import com.forms.FilteredList;

public class NewRole extends MyObject {
	public static final String NAME = "NewRole";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";
	public static final int NAMELENGTH = 30;
	public static final int NAMEMIN = 5;
//	SessionVars sVars = null;

//	public enum ACCESSTYPE {
//		NOTINITIALIZED, ACCESS, ACCESSANDCHANGE
//	}
//
//	ACCESSTYPE accessType;
	
	SessionVars sVars = null;

	public NewRole(SessionVars sVars) throws Exception {
		super(sVars);
		this.sVars = sVars;
		new Table().tableCreated(this, sVars);
//		this.sVars = sVars;
	}

	/**
	 * remove a role. first, delete all links with users of the role, then delete
	 * the role
	 * 
	 * @throws Exception
	 */
//	public void remove() throws Exception {
//
////		for (User user : listUsers())
////			new MyLinkObject(this, user).deleteLinksUnconditionallyChild();
////		for (ClassAccess user : listClassAccesses())
////			new MyLinkObject(this, user).deleteLinksUnconditionallyChild();
//		deleteUnconditionally();
//	}

	/*
	 * list all roles for this anchor
	 * 
	 * @param anchor
	 * 
	 * @return
	 * 
	 * @throws SQLException
	 * 
	 * @throws Exception
	 */
	public ArrayList<NewRole> list(Anchor anchor) throws SQLException, Exception {
		ArrayList<NewRole> roles = new ArrayList<NewRole>();
		for (MyObject obj : listAll(anchor))
			roles.add((NewRole) obj);
		return roles;
	}

	public MyObjects listUsers() throws Exception {
		MyLinkObject mlo = new MyLinkObject(this, new User(sVars), sVars);
		MyObjects objs = mlo.listChildrenOfParent();
		MyObjects users = new MyObjects();
		for (MyObject obj : objs) {
			users.add((User) obj);
		}
		return users;
	}

	public ArrayList<ClassAccess> listClassAccesses() throws Exception {
		MyLinkObject mlo = new MyLinkObject(this, new ClassAccess(sVars), sVars);
		MyObjects objs = mlo.listChildrenOfParent();
		ArrayList<ClassAccess> classAccesses = new ArrayList<ClassAccess>();
		for (MyObject obj : objs) {
			classAccesses.add((ClassAccess) obj);
		}
		return classAccesses;
	}

//	public NewRole addChangeability() {
//		accessType = ACCESSTYPE.ACCESSANDCHANGE;
//		return this;
//	}
//
//	public NewRole addExecuteability() {
//		accessType = ACCESSTYPE.ACCESS;
//		return this;
//	}
//
//	public boolean canAccessAndChange() {
//		return accessType == ACCESSTYPE.ACCESSANDCHANGE;
//	}

//	public boolean canAccess() {
//		return accessType == ACCESSTYPE.ACCESS;
//	}

	public NewRole add(User user) throws Exception {
		if (!isLoaded())
			throw new Exception("not loaded");
		MyLinkObject mlo = new MyLinkObject(this, user, sVars);
		if (mlo.linkExists())
			throw new Exception("link already exists");
		mlo.add();
		return this;
	}

	public NewRole add(ClassAccess classAccess) throws Exception {
		if (!isLoaded())
			throw new Exception("not loaded");
		MyLinkObject mlo = new MyLinkObject(this, classAccess, sVars);
		if (mlo.linkExists())
			throw new Exception("link already exists");
		mlo.add();
		return this;
	}

//	public NewRole addClassAndAccess(MyObject obj) throws Exception {
//		if (!isLoaded())
//			throw new Exception("not loaded");
//		return add(new ClassAccess().addAccess(obj));
//	}

//	public NewRole addEditSelectType(Anchor anchor, MyObject obj, SearchTarget.EDITSELECTTYPE editSelectType) throws Exception {
//		if (!isLoaded())
//			throw new Exception("not loaded");
//		return add(new ClassAccess().addEditSelectType(anchor, obj, editSelectType));
//	}

	/**
	 * can the user access the object
	 * 
	 * @param obj
	 * @return
	 */
	public boolean canAccess(User user, MyObject obj) {
		try {
			if (!listUsers().containsMyObject(user))
				return false;
		} catch (Exception e) {
		}
		try {
			for (ClassAccess ca : listClassAccesses()) {
				// the instance name of ClassAccess is the logical name of the obj
				if (ca.getInstanceName().equals(obj.getLogicalName())
						&& ca.accessType == SearchTarget.EDITSELECTTYPE.SELECT)
					return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public SearchTarget.EDITSELECTTYPE getEditSelectType(User user, MyObject obj) {
		try {
			if (!listUsers().containsMyObject(user))
				return SearchTarget.EDITSELECTTYPE.NEITHER;
		} catch (Exception e) {
		}
		// the user is in the role. Check the access
		try {
			for (ClassAccess ca : listClassAccesses()) {
				// the instance name of ClassAccess is the logical name of the obj
				if (ca.getInstanceName().equals(obj.getLogicalName()))
					return ca.accessType;
			}
		} catch (Exception e) {
		}
		return SearchTarget.EDITSELECTTYPE.NEITHER;
	}

	/**
	 * can the user access and change the object
	 * 
	 * @param obj
	 * @return
	 */
	public boolean canAccessAndChange(User user, MyObject obj) {
		try {
			if (!listUsers().containsMyObject(user))
				return false;
		} catch (Exception e) {
		}
		try {
			for (ClassAccess ca : listClassAccesses()) {
				// the instance name of ClassAccess is the logical name of the obj
				if (ca.getInstanceName().equals(obj.getLogicalName())
						&& ca.accessType == SearchTarget.EDITSELECTTYPE.EDITANDSELECT)
					return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public NewRole remove(User user) throws Exception {
		new MyLinkObject(this, user, sVars).deleteLinksUnconditionallyChild();
		return this;
	}

	public NewRole remove(ClassAccess classAccess) throws Exception {
		new MyLinkObject(this, classAccess, sVars).deleteLinksUnconditionallyChild();
		return this;
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
		return obj instanceof NewRole;
	}

	@Override
	public MyObject getNew() throws Exception {
		return new NewRole(sVars);
	}

	public NewRole setAnchorAndName(Anchor anchor, String name) throws Exception {
		setInstanceName(name);
		anchorId = anchor.id;
		return this;
	}

//	@Override
//	public Strings extendNewTable() {
//		Strings ret = new Strings();
//		ret.add("`access` int(11)");
//		return ret;
//	}

	public NewRole sanityRole() throws Exception {
		sanity();
		if (anchorId == 0)
			throw new Exception("anchor not set");
		return this;
	}

	/**
	 * commit the role to the database
	 */
	@Override
	public NewRole add(Anchor myAnchor) throws Exception {
		return (NewRole) super.add(myAnchor);
	}

	@Override
	public NewRole find(Anchor anchor, String name) throws Exception {
		return (NewRole) super.find(anchor, name);
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
