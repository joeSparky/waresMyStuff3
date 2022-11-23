package com.parts.exhibit;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;
//import com.parts.forms.FormsMatrixDynamic;
import com.parts.inOut.Part;
import com.parts.warehouse.Warehouse;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.SelectForm;
import com.security.Table;

public class Kit extends MyObject {
	public static final String NAME = "Kit";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

	public Kit(SessionVars sVars) throws Exception {
		super(sVars);
		new Table().tableCreated(this, sVars);
	}

	@Override
	public MyObject getNew() throws Exception {
		return new Kit(sVars);
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
	public boolean equalsReminder(Object obj) {
		return false;
	}

	@Override
	public boolean equals(Object exhibit) {
		return exhibit instanceof Kit && id == ((Kit) exhibit).id;
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
	public boolean equalsObject(Object obj) {
		return obj instanceof Kit;
	}

	public static String getSimpleClassNameStatic() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public boolean isRecursive() {
		return true;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Warehouse(sVars));
		return objs;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Part(sVars));
		return objs;
	}
	@Override
	public SmartForm getSelector(FormsMatrixDynamic fmd, SessionVars sVars) throws Exception {
		return new SelectForm(fmd, sVars);
	}
}
