package com.partsTest.exhibit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.parts.exhibit.Vendor;
import com.partsTest.Utilities.Utilities;
import com.security.Company;

public class VendorTest
//implements StartOverCallback 
{
	SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {sVars = new SessionVars();
	if (sVars==null)sVars = new SessionVars();	
//	
		new Utilities().allNewTables(sVars);
//		Internals.setupCallback(this);
	}

	@After
	public void tearDown() throws Exception {
//		Internals.setupCallback(null);
	}

	static final String VENDORNAME = "vendor name";
	static final String VENDORURL = "vendor URL";

	@Test
	public void testAdd() {
//		calledBack = false;
		try {
			standardVendor(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		if (calledBack)
//			fail("add failed");
		if (standardVendorFails())
			fail("standard add fails");
	}

	Vendor vendor = null;

	private void standardVendor(SessionVars sVars) throws Exception {

		Company company = Utilities.getACompany();
//		boolean vendorAlreadyExists = false;
//		try {
		vendor = new Vendor(sVars);
//		vendor.findByName(VENDORNAME);
//		vendorAlreadyExists = true;
//		} catch (Exception e2) {
//		}
//		if (!vendorAlreadyExists) {
//			try {
				vendor.setInstanceName(VENDORNAME);
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
			vendor.url = VENDORURL;
//			try {
				vendor.add(company.getAnchor());
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
//		}
//
//		vendorAlreadyExists = false;
//		try {
//			vendor = new Vendor(SessionVars sVars);
//			vendor.findByName(VENDORNAME);
//			vendorAlreadyExists = true;
//		} catch (Exception e) {
//		}
//		if (!vendorAlreadyExists) {
//			// should pass sanity
//			try {
//				vendor.setInstanceName(VENDORNAME);
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
//			vendor.url = VENDORURL;
//			try {
//				vendor.add(company.getAnchor());
//			} catch (Exception e) {
//				fail("normal vendor add failed");
//			}
//		}
//		if (standardVendorFails())
//			fail("extract info incorrect");
	}

	@Test
	public void testSanity() {

		try {
			standardVendor(sVars);
			vendor.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// null URL
		// vendor.url = null;
		// try {
		// vendor.sanity();
		// fail("missed null URL");
		// } catch (Exception e) {
		// }
		// URL too short
		vendor.url = "a";
		if (vendor.url.length() >= Vendor.URLMINIMUM)
			fail("make test URL shorter");
		try {
			vendor.sanity();
			fail("missed too short of URL");
		} catch (Exception e) {
		}
		// URL too long
		vendor.url = "aadfadsfjkadsf;ljasdfkajsdfasdfasdfklasjdflkajsdfaksjd";
		if (vendor.url.length() < Vendor.URLLENGTH)
			fail("make test URL longer");
		try {
			vendor.sanity();
			fail("missed too long of URL");
		} catch (Exception e) {
		}
	}

	// @Test
	// public void testDelete() {
	// // normal delete
	// standardVendor(sVars);
	// try {
	// vendor.delete();
	// } catch (Exception e) {
	// fail("delete failed");
	// }
	//
	// // delete an vendor that is not in the database
	// standardVendor(sVars);
	// vendor.id = 23232;
	// try {
	// vendor.delete();
	// } catch (Exception e) {
	// fail("should not complain about deleting a record that no longer exists.");
	// }
	// }

	private boolean standardVendorFails() {
		if ((vendor.getInstanceName() == null) || (!vendor.getInstanceName().equals(VENDORNAME)) || (vendor.url == null)
				|| (!vendor.url.equals(VENDORURL)) || (vendor.id < 1))
			return true;
		return false;
	}

	@Test
	public void testFind() {
		try {
			standardVendor(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		int tmpId = vendor.id;

		try {
			vendor = new Vendor(sVars);
			vendor.find(tmpId);
		} catch (Exception e) {
			fail("could not find normal vendor");
		}
		if (standardVendorFails())
			fail("extracted vendor info not correct");
		try {
			vendor.find(23232);
			fail("found a non-existent vendor");
		} catch (Exception e) {
		}
	}

	@Test
	public void testFindByNameAndVendorId() {

		// clear the instance

		try {
			standardVendor(sVars);

			vendor = new Vendor(sVars);
			vendor.find(VENDORNAME);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (standardVendorFails())
			fail("extract info incorrect");
	}

	@Test
	public void testExtractInfo() {

		Vendor tmp = null;
		try {
			standardVendor(sVars);
			tmp = new Vendor(sVars);
			tmp.find(vendor.id);
		} catch (Exception e) {
			fail("could not find standard vendor");
		}
		if (tmp.getInstanceName() == null)
			fail("null vendor name");
		if (!tmp.getInstanceName().equals(VENDORNAME))
			fail("extracted incorrect name");
		if (tmp.url == null)
			fail("null vendor URL");
		if (!tmp.url.equals(VENDORURL))
			fail("extracted incorrect URL");
	}

//	boolean calledBack = false;
//
//	@Override
//	public void callBack() {
//		calledBack = true;
//	}

}
