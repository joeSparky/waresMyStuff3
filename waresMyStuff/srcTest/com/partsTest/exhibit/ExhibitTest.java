package com.partsTest.exhibit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.parts.exhibit.Kit;
import com.parts.inOut.Attributes;
import com.parts.inOut.Part;
import com.parts.warehouse.Warehouse;
import com.partsTest.Utilities.Utilities;
import com.security.Anchor;
import com.security.Company;

public class ExhibitTest {
SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		if (sVars==null)
			sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
//		Internals.setupCallback(this);

	}

	@After
	public void tearDown() throws Exception {
//		Internals.setupCallback(null);
	}

	private static final String ADDEDEXHIBITNAME = "testExhibitName";

	private Kit addExhibit() {
		Kit tmp = null;
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			tmp = new Kit(sVars);
			anchor = company.getAnchor();
			tmp.setInstanceName(ADDEDEXHIBITNAME);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// tmp.location = ADDEDLOCATIONNAME;
		try {
			tmp.add(anchor);
		} catch (Exception e) {
			fail("add failure");
		}
		return tmp;
	}

	@Test
	public void testFindById() {
		try {
			new Kit(sVars).find(-3);
			fail("found an exhibit with an id of -3");
		} catch (Exception e) {
		}

		Kit added = addExhibit();
		if (added.id < 1)
			fail("add failed");

		// try to find the exhibit we just added
		try {
			added.find(added.id);
		} catch (Exception e) {
			fail("could not find added exhibit");
		}
	}

	@Test
	public void testAdd() {
//		calledBack = false;
		addExhibit();
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
//		if (calledBack)
//			fail("could not add");

		// try to add duplicate names
//		calledBack = false;
		Kit first = null;
		try {
			first = new Kit(sVars);
			first.setInstanceName("duplicate");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// first.location = "first location";
		try {
			first.add(anchor);
		} catch (Exception e) {
			fail("add failure");
		}
//		if (calledBack)
//			fail("add failure");

		Kit second = null;
		try {
			second = new Kit(sVars);
			second.setInstanceName("duplicate");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// second.location = "second location";
		try {
			second.add(anchor);
			fail("did not catch duplicate name");
		} catch (Exception e) {
		}
	}

	@Test
	public void testDelete() {
//		calledBack = false;
		Company company = Utilities.getACompany();
		Kit added = addExhibit();
		try {
			added.deleteUnconditionally();
		} catch (Exception e) {
			fail("delete failure");
		}

		Warehouse w = null;
		Anchor anchor = null;
		Kit toBeDeleted = null;
		try {
			toBeDeleted = Utilities.getAnExhibit(company.getAnchor());
			w = Utilities.getAWarehouse(company.getAnchor());
			anchor = w.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Attributes f = Utilities.getAFamily(w, anchor);
		Part assm = Utilities.getAMember(f, anchor);

		// try to delete an exhibit that doesn't exit
		added = addExhibit();
		added.id = -1234;
		try {
			added.deleteUnconditionally();
		} catch (Exception e) {
			fail("should not complain about deleting a record that no longer exists");
		}

		// try to delete an exhibit that has a link to it
//		Exhibit toBeDeleted = Utilities.getAnExhibit();
//		Warehouse w = Utilities.getAWarehouse();
//		Family f = Utilities.getAFamily(w);
//		Member assm = Utilities.getAMember(f);
		try {
//			new MyLinkObject(toBeDeleted, assm).add(1);
			toBeDeleted.addChild(assm);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			toBeDeleted.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

//	boolean calledBack = false;

//	@Override
//	public void callBack() {
//		calledBack = true;
//
//	}
}
