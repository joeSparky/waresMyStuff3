package com.security;

import java.lang.invoke.MethodHandles;

import com.db.DoubleStrings;
import com.db.SessionVars;

public class Company extends MyObject {
	// public static final String TABLENAME = "company";
	public static final String NAME = "Company";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = "Companies";
//	private static final String DEFAULTCOMPANY = "dCompany";
//	SessionVars sVars = null;
	/**
	 * 
	 */
//	static boolean defaultCompanySetUp = false;

	public Company(SessionVars sVars) throws Exception {
		super(sVars);
		new Table().tableCreated(this, sVars);
	}

	synchronized void init() throws Exception {
	
			String defaultCompanyName = sVars.getDefaultCompanyName();
			Company tmp = new Company(sVars);
			boolean found = false;
			try {
				tmp.find(defaultCompanyName);
				found = true;
			} catch (Exception e) {
			}
			if (!found) {
				tmp.setInstanceName(defaultCompanyName);
				// add a default company to the database
				tmp.add(new Anchor(sVars));
			}
		
	}

//	public Company() throws Exception {
//		new Table().tableCreated(this, sVars);
//	}

//	public Company getDefaultCompany() throws Exception {
//		if (defaultCompany == null) {
//			defaultCompany = new Company(sVars);
//			XML xml = new XML();
////			// return it if it is already in the database
////			try {
//				defaultCompany.find(xml.readXML(DEFAULTCOMPANY));
////				return defaultCompany;
////			} catch (Exception e) {
////				Internals.dumpExceptionExit(e);
////			}
//		}
//		return defaultCompany;
//	}

	@Override
	public void newTableMonitor() throws Exception {
		init();
//		XML xml = new XML();
////		try {
//			defaultCompany = new Company(sVars);
//			defaultCompany.setInstanceName(xml.readXML(DEFAULTCOMPANY));
//			defaultCompany.add(new Anchor(sVars));
////		} catch (Exception e) {
////			Internals.dumpExceptionExit(e);
////		}
	}

//	static Company defaultCompany = null;

	@Override
	public String getAName() {
		return ANAME;
	}

	public Company find(String name) throws Exception {
		return (Company) super.find(name);
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
//	public boolean equals(Object exhibit) {
//		if (exhibit instanceof Company)
//			return id == ((Company) exhibit).id;
//		else
//			Internals.dumpStringExit("invalid class " + exhibit.getClass().getCanonicalName());
//		return false;
//	}
//
//	@Override
//	public void sanity() throws Exception {
//	}

	@Override
	public MyObject getNew() throws Exception {
		return new Company(sVars);
	}

	@Override
	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Company;
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
