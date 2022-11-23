package com.partsTest.exhibit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.parts.inOut.Attributes;
import com.partsTest.Utilities.Utilities;
import com.security.Company;

public class FamilyTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		if (sVars==null)sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClear() {
		Attributes f = null;
		try {
			f = new Attributes(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		f.clear();
	}

	@Test
	public void testAdd() {
		Company company = Utilities.getACompany();
		try {
			Utilities.getAFamily(Utilities.getAWarehouse(company.getAnchor()), company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testSanity() {
		Company company = Utilities.getACompany();
		try {
			Utilities.getAFamily(Utilities.getAWarehouse(company.getAnchor()), company.getAnchor()).sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdate() {
		Company company = Utilities.getACompany();
		Attributes f = null;
		try {
			f = Utilities.getAFamily(Utilities.getAWarehouse(company.getAnchor()), company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			f.setInstanceName(f.getInstanceName() + "dka");
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		try {
			f.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			f.update();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// flip an option
//		if (f.familyOptionOn(OptionEnums.OPTIONS.NORECEIVESOUND)) {
//			f.familyOptions.remove(OptionEnums.OPTIONS.NORECEIVESOUND);
//			f.familyOptions.put(OptionEnums.OPTIONS.SAYPOSITION);
//		} else {
//			f.familyOptions.remove(OptionEnums.OPTIONS.SAYPOSITION);
//			f.familyOptions.put(OptionEnums.OPTIONS.NORECEIVESOUND);
//		}
		try {
			f.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			f.update();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

}
