package com.parts.exhibit;

import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.Strings;
import com.security.ExceptionCoding;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class Vendor extends MyObject {
	public static final String NAME = "Vendor";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";
	public static final int NAMELENGTH = 30;
	public static final int NAMEMINIMUM = 5;
	public static final int URLLENGTH = 30;
	public static final int URLMINIMUM = 5;
	/*
	 * empty or (greater than URLMINIMUM and less than URLLENGTH)
	 */
	public String url;

	public Vendor(SessionVars sVars) throws Exception {
		super(sVars);
		clear();
		new Table().tableCreated(this, sVars);
	}

	public static boolean needsNewTable = false;

	synchronized void serviceTable() throws Exception {
		if (needsNewTable) {
			needsNewTable = false;
			newTable(sVars);
		}
	}

	public void sanity() throws Exception {
		if (url == null)
			throw new ExceptionCoding("null url");

		if (!url.isEmpty()) {
			if (url.length() < URLMINIMUM)
				throw new Exception(NAME + " URL '" + url + "' is less than " + URLMINIMUM + " characters");
			if (url.length() > URLLENGTH)
				throw new Exception(NAME + " URL '" + url + "' is greater than " + URLLENGTH + " characters.");
		}
	}

	public void clear() {
		url = "";
		super.clear();
	}

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add("`url` varchar(" + URLLENGTH + ") NOT NULL");
		return ret;
	}

	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		ret.add(new DoubleString("url", url));
		return ret;
	}

	// /**
	// * if there are no links to this vendor, delete a vendor
	// *
	// * @throws Exception
	// */
	// public void delete() throws Exception {
	// boolean okToDelete = false;
	// try {
	// deleteTest();
	// okToDelete = true;
	// } catch (Exception e) {
	// }
	// if (okToDelete)
	// deleteUnconditionally();
	// }

	// public void deleteUnconditionally() {
	// for (PartAndVendor pandv : PartAndVendor.list(this)) {
	// try {
	// pandv.delete();
	// } catch (Exception e) {
	// Internals.dumpExceptionExit(e);
	// }
	// }
	// MyStatement st = new MyStatement();
	// st.executeUpdate("delete from " + getSimpleClassName() + " where id='"
	// + id + "'");
	// clear();
	// }

	public Vendor find(int id) throws Exception {
		return (Vendor) super.find(id);
	}

	/**
	 * find by vendor name
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Vendor find(String name) throws Exception {
		return (Vendor)super.find(name);
	}

	@Override
	public Vendor extractInfo(ResultSet row) throws Exception {
		if (row == null)
			throw new Exception("null row");
		url = row.getString("url");
		super.extractInfo(row);
		return this;
	}

	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	@Override
	public boolean equals(Object assembly) {
		if (assembly instanceof Vendor) {
			return id == ((Vendor) assembly).id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id;
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
	public boolean equalsReminder(Object obj) {
		return false;
	}

	@Override
	public MyObject getNew() throws Exception {
		return new Vendor(sVars);
	}

	@Override
	public String getLogicalName() {
		return NAME;
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Vendor;
	}

	public static String getSimpleClassNameStatic() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new PartNumber(sVars));
		return objs;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		MyObjects objs = new MyObjects();
		return objs;
	}
}