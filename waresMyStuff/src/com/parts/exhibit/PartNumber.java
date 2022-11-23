package com.parts.exhibit;

import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;

import com.db.SessionVars;
import com.parts.inOut.Part;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class PartNumber extends MyObject {
	public static final String NAME = "Part Number";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";
	public static final int NAMELENGTH = 30;
	public static final int NAMEMINIMUM = 5;

	public PartNumber(SessionVars sVars) throws Exception {
		super(sVars);
		new Table().tableCreated(this, sVars);
	}

	public static boolean needsNewTable = false;

	synchronized void serviceTable() throws Exception {
		if (needsNewTable) {
			needsNewTable = false;
			newTable(sVars);
		}
	}

//	public PartNumber findDELETE(int id) throws Exception {
//		MyStatement st = new MyStatement();
//		ResultSet rs = st.executeQuery("select * from " + getFileName()
//				+ " where id='" + id + "'");
//		// st.close();
//		try {
//			if (rs.next())
//				return extractInfo(rs);
//		} catch (SQLException e) {
//			Internals.dumpSQLExit(e);
//		} finally {
//			try {
//				rs.close();
//				st.close();
//			} catch (SQLException e) {
//				Internals.dumpExceptionExit(e);
//			}
//		}
//		throw new Exception(NAME + " with Id " + id + " not found");
//	}

	/**
	 * find by vendor name
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
//	public PartNumber findByNameDELETE(String name) throws Exception {
//		MyStatement st = new MyStatement();
//		ResultSet rs = st.executeQuery("select * from " + getFileName()
//				+ " where name='" + name + "'");
//		// st.close();
//		try {
//			if (rs.next())
//				return extractInfo(rs);
//		} catch (SQLException e) {
//			Internals.dumpSQLExit(e);
//		} finally {
//			try {
//				rs.close();
//				st.close();
//			} catch (SQLException e) {
//				Internals.dumpExceptionExit(e);
//			}
//		}
//		throw new Exception(NAME + " name of " + name + " not found");
//	}

//	public DoubleStrings extendUpdate() {
//		return extendAdd();
//	}

	@Override
	public PartNumber extractInfo(ResultSet row) throws Exception {
		super.extractInfo(row);
		return this;
	}

	@Override
	public boolean equals(Object assembly) {
		if (assembly instanceof PartNumber) {
			return id == ((PartNumber) assembly).id;
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
		return new PartNumber(sVars);
	}

	@Override
	public String getLogicalName() {
		return NAME;
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof PartNumber;
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
		objs.add(new Part(sVars));
		return objs;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Vendor(sVars));
		return objs;
	}

}