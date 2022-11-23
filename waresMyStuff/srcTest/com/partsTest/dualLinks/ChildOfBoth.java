package com.partsTest.dualLinks;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class ChildOfBoth extends MyObject {
	public static final String NAME = "Level2";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

	public ChildOfBoth(SessionVars sVars) throws Exception {
		super(sVars);
//		clear();
		new Table().tableCreated(this, sVars);
	}

	@Override
	public String getAName() {
		return ANAME;
	}

	@Override
	public String getLogicalName() {
		return NAME;
	}

//	@Override
//	public MyObject find(int id) throws Exception {
//		return extractInfo(findResultSet(id));
//	}

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

//	@Override
//	public boolean equals(Object myObject) {
//		if (myObject instanceof MyObject)
//			return id == ((MyObject) myObject).id;
//		if (myObject instanceof MyLinkObject) {
//			return (myObject.getClass().getName().equals(this.getClass()
//					.getName()));
//		} else
//			throw new Exception("invalid class "
//					+ myObject.getClass().getCanonicalName());
//	}

	@Override
	public void sanity() throws Exception {
	}

	@Override
	public MyObject getNew() throws Exception {
		return new ChildOfBoth(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	static String myName = null;

	public static String getSimpleClassNameStatic() {
		if (myName == null)
			myName = MethodHandles.lookup().lookupClass().getSimpleName();
		return myName;
	}
	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName();
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects ret = new MyObjects();
		ret.add(new ParentExternal(sVars));
		ret.add(new ParentIdInChild(sVars));
		return ret;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
