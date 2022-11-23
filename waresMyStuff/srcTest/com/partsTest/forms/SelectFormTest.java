package com.partsTest.forms;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.SearchTarget;
import com.forms.SearchTargets;
import com.parts.inOut.Attributes;
import com.parts.inOut.Part;
import com.parts.warehouse.Warehouse;
import com.partsTest.Utilities.Utilities;
import com.security.Company;
import com.security.MyObjects;
import com.security.SelectForm;
import com.security.User;

//import comTest.utilities.Utilities;

public class SelectFormTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		if (sVars == null)
			sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * create a list of companies that should be available for selection
	 */
	@Test
	public void testGetForm() {
		Company myCompany = Utilities.getACompany();
		Warehouse warehouse = null;
		try {
			warehouse = Utilities.getAWarehouse(myCompany.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Attributes family = null;
		try {
			family = Utilities.getAFamily(warehouse, warehouse.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Part member = null;
		try {
			member = Utilities.getAMember(family, warehouse.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		Anchor wa = null;
//		try {
//			wa = new Anchor(sVars).add(warehouse);
//		} catch (Exception e1) {
//			fail(e1.getLocalizedMessage());
//		}
//		Location location = Utilities.getLocation(wa);
//		try {
//			Utilities.getAnItem(member, location, myCompany.getAnchor());
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
		FormsMatrixDynamic fmd = null;
		SearchTargets objs = new SearchTargets(sVars);

		try {
			fmd = new FormsMatrixDynamic(sVars);
			objs.add(myCompany, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objs.add(warehouse, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objs.add(family, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objs.add(member, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		fmd.add(objs);
		fmd.row = 0;
		fmd.column = 3;
		try {
			SelectForm sf = new SelectForm(fmd, new SessionVars());
			sf.getForm(new SessionVars());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	// companies are anchored by themselves.
	// verify that listall(anchor) returns the single company of itself
	@Test
	public void testListAllAnchor() {
		Company yourCompany = Utilities.getACompany();
		Company myCompany = Utilities.getACompany();
		// couple of extra companies
		Utilities.getACompany();
		Utilities.getACompany();
		try {
			if (new Company(sVars).listAll(yourCompany).size() != 1)
				fail("expected a size of 1, got a size of " + new Company(sVars).listAll(yourCompany).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (new Company(sVars).listAll(yourCompany).iterator().next().id != yourCompany.id)
				fail("expected an id of " + yourCompany.id + ", got an id of "
						+ new Company(sVars).listAll(yourCompany).iterator().next().id);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// repeat with myCompany as an anchor
		try {
			if (new Company(sVars).listAll(myCompany).size() != 1)
				fail("expected a size of 1, got a size of " + new Company(sVars).listAll(myCompany).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (new Company(sVars).listAll(myCompany).iterator().next().id != myCompany.id)
				fail("expected an id of " + myCompany.id + ", got an id of "
						+ new Company(sVars).listAll(myCompany).iterator().next().id);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	// anchor a couple of users to a company and see if we get them back
	@Test
	public void testListAllAnchorUser() {
		Utilities.getACompany();
		Utilities.getACompany();
		Company myCompany = Utilities.getACompany();
		// couple of extra companies
		Utilities.getACompany();
		Utilities.getACompany();

		User firstMyUser = null;
		User secondMyUser = null;
		try {
			firstMyUser = new Utilities().getAUser(myCompany.getAnchor());
			secondMyUser = new Utilities().getAUser(myCompany.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		MyObjects objs = null;
		try {
			objs = new User(sVars).listAll(myCompany);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (objs.size() != 2)
			fail("expected a size of 2, got " + objs.size());
		if (!objs.contains(firstMyUser))
			fail("objs did not contain firstMyUser");
		if (!objs.contains(secondMyUser))
			fail("objs did not contain secondMyUser");
	}
}
