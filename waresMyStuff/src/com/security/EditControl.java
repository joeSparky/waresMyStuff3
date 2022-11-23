package com.security;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import com.db.DoubleStrings;
import com.db.MyConnection;
import com.db.MyStatement;
import com.db.SessionVars;
import com.db.Strings;

public class EditControl extends MyObject {
	public EditControl(SessionVars sVars) throws Exception {
		super(sVars);
		new Table().tableCreated(this, sVars);
	}

	public static final String NAME = "Edit Control";
	public static final String ANAME = "an " + NAME;
	public static final String NAMES = NAME + "s";
	public static final int CLASSNAMELENGTH = 55;
//	String className;

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
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
		if (exhibit instanceof EditControl) {
			return id == ((EditControl) exhibit).id;
		}
		return false;
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof EditControl;
	}

	@Override
	public MyObject getNew() throws Exception {
		return new EditControl(sVars);
	}

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add("`className` varchar(" + CLASSNAMELENGTH + ")");
		return ret;
	}

//	@Override
//	protected DoubleStrings extendAdd() {
//		DoubleStrings ret = new DoubleStrings();
//		ret.add(new DoubleString("className", className));
//		return ret;
//	}

	@Override
	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	public Collection<String> getEditControls() throws Exception {
		Collection<String> ret = new ArrayList<String>();
		String query = "SELECT * FROM " + getMyFileName();
		Connection conn = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			rs = st.executeQuery(query);
			while (rs.next())
				ret.add(new EditControl(sVars).extractInfo(rs).getInstanceName());
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

	public void delete(MyObject classObject) throws Exception {
		Connection conn = null;
		MyStatement st = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			st.executeMyUpdate("DELETE FROM " + getMyFileName() + " WHERE name='" + classObject.getMyFileName() + "'");
		} finally {
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
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
