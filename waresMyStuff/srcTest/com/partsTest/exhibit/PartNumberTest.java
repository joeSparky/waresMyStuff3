package com.partsTest.exhibit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.parts.exhibit.PartNumber;
import com.partsTest.Utilities.Utilities;
import com.security.Company;

public class PartNumberTest
//implements StartOverCallback 
{
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		if (sVars==null)sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
//		Internals.setupCallback(this);
	}

	@After
	public void tearDown() throws Exception {
//		Internals.setupCallback(null);
	}

	static final String PARTNUMBERNAME = "part number name";
//	static final String VENDORURL = "vendor URL";

	@Test
	public void testAdd() {
		standardPartNumber(sVars);
//		if (calledBack)
//			fail("add failed");
		if (standardPartNumberFails())
			fail("standard add fails");
	}

	PartNumber partNumber = null;

	private void standardPartNumber(SessionVars sVars) {
		
		Company company = Utilities.getACompany();
		boolean vendorAlreadyExists = false;
		try {partNumber = new PartNumber(sVars);
			partNumber.find(PARTNUMBERNAME);
			vendorAlreadyExists = true;
		} catch (Exception e2) {
		}
		if (!vendorAlreadyExists) {
			try {
				partNumber.setInstanceName(PARTNUMBERNAME);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
//			partNumber.url = VENDORURL;
			try {
				partNumber.add(company.getAnchor());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
		}

		
		vendorAlreadyExists = false;
		try {partNumber = new PartNumber(sVars);
			partNumber.find(PARTNUMBERNAME);
			vendorAlreadyExists = true;
		} catch (Exception e) {
		}
		if (!vendorAlreadyExists) {
			// should pass sanity
			try {
				partNumber.setInstanceName(PARTNUMBERNAME);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
//			partNumber.url = VENDORURL;
			try {
				partNumber.add(company.getAnchor());
			} catch (Exception e) {
				fail("normal vendor add failed");
			}
		}
		if (standardPartNumberFails())
			fail("extract info incorrect");
	}

	@Test
	public void testSanity() {
		standardPartNumber(sVars);
		try {
			partNumber.sanity();
		} catch (Exception e) {
			fail("sanity failed");
		}
	}

	// @Test
	// public void testDelete() {
	// // normal delete
	// standardPartNumber(SessionVars sVars);
	// try {
	// vendor.delete();
	// } catch (Exception e) {
	// fail("delete failed");
	// }
	//
	// // delete an vendor that is not in the database
	// standardPartNumber(SessionVars sVars);
	// vendor.id = 23232;
	// try {
	// vendor.delete();
	// } catch (Exception e) {
	// fail("should not complain about deleting a record that no longer exists.");
	// }
	// }

	private boolean standardPartNumberFails() {
		if ((partNumber.getInstanceName() == null) || (!partNumber.getInstanceName().equals(PARTNUMBERNAME))
//				|| (partNumber.url == null) || (!partNumber.url.equals(VENDORURL))
				|| (partNumber.id < 1))
			return true;
		return false;
	}

	@Test
	public void testFind() {
		standardPartNumber(sVars);
		int tmpId = partNumber.id;
		
		try {partNumber = new PartNumber(sVars);
			partNumber.find(tmpId);
		} catch (Exception e) {
			fail("could not find normal vendor");
		}
		if (standardPartNumberFails())
			fail("extracted vendor info not correct");
		try {
			partNumber.find(23232);
			fail("found a non-existent vendor");
		} catch (Exception e) {
		}
	}

	@Test
	public void testFindByNameAndPartNumberId() {
		standardPartNumber(sVars);
		// clear the instance
		
		try {partNumber = new PartNumber(sVars);
			partNumber.find(PARTNUMBERNAME);
		} catch (Exception e) {
			fail("could not find standard vendor");
		}
		if (standardPartNumberFails())
			fail("extract info incorrect");
	}

	@Test
	public void testExtractInfo() {
		standardPartNumber(sVars);
		PartNumber tmp = null;
		try {tmp = new PartNumber(sVars);
			tmp.find(partNumber.id);
		} catch (Exception e) {
			fail("could not find standard vendor");
		}
		if (tmp.getInstanceName() == null)
			fail("null vendor name");
		if (!tmp.getInstanceName().equals(PARTNUMBERNAME))
			fail("extracted incorrect name");
//		if (tmp.url == null)
//			fail("null vendor URL");
//		if (!tmp.url.equals(VENDORURL))
//			fail("extracted incorrect URL");
	}

//	boolean calledBack = false;
//
//	@Override
//	public void callBack() {
//		calledBack = true;
//	}

}
