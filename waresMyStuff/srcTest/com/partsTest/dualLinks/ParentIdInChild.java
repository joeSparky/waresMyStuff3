package com.partsTest.dualLinks;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class ParentIdInChild extends MyObject {
	public static final String NAME = "parent internal";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

	public ParentIdInChild(SessionVars sVars) throws Exception {
		super(sVars);
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

	@Override
	public boolean equals(Object exhibit) {
		return exhibit instanceof ParentIdInChild 
				&& id == ((ParentIdInChild) exhibit).id;
	}

	@Override
	public void sanity() throws Exception {
	}

	@Override
	public MyObject getNew() throws Exception {
		return new ParentIdInChild(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	static String myFileName = null;
	@Override
	public String getMyFileName() {
		if (myFileName == null)
			myFileName = MethodHandles.lookup().lookupClass().getSimpleName();
		return myFileName;
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		// TODO Auto-generated method stub
		return new MyObjects();
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		// TODO Auto-generated method stub
		return new MyObjects();
	}
}
