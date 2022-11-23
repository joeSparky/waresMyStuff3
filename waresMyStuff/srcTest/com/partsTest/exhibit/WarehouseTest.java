package com.partsTest.exhibit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.forms.SearchTarget;
import com.forms.SearchTargets;
import com.parts.location.Location;
import com.parts.warehouse.Warehouse;
import com.partsTest.Utilities.Utilities;
import com.security.Anchor;
import com.security.Company;

import comTest.security.Level1;
import comTest.security.Level2;

//import comTest.utilities.Utilities;

public class WarehouseTest {
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
	public void testWarehouse() {
		try {
			Utilities.getAWarehouse(Utilities.getACompany().getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testClear() {
		Warehouse wh = null;
		try {
			wh = Utilities.getAWarehouse(Utilities.getACompany().getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		wh.clear();
		if (wh.id != 0)
			fail("clear");
		if (wh.getInstanceName() != "")
			fail("clear");
	}

	@Test
	public void testFindInt() {
		Warehouse wh = null;
		try {
			wh = Utilities.getAWarehouse(Utilities.getACompany().getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		int id = wh.id;
		String name = wh.getInstanceName();
		wh.clear();
		try {
			wh.find(id);
		} catch (Exception e) {
			fail("find" + e.getLocalizedMessage());
		}
		if (wh.id != id)
			fail("id");
		if (!wh.getInstanceName().equals(name))
			fail("name");
	}

	@Test
	public void testAdd() {
		try {
			Utilities.getAWarehouse(Utilities.getACompany().getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testSanity() {
		Company company = Utilities.getACompany();
		Warehouse wh = null;
		try {
			wh = Utilities.getAWarehouse(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Warehouse dupe = null;
		try {
			dupe = new Warehouse(sVars);
			dupe.setInstanceName(wh.getInstanceName());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			dupe.add(company.getAnchor());
			fail("added duplicate warehouse");
		} catch (Exception e) {
		}
	}

	@Test
	public void testDeleteUnconditionally() {
		Warehouse wh = null;
		try {
			wh = Utilities.getAWarehouse(Utilities.getACompany().getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		SearchTargets objs = new SearchTargets(sVars);

		try {
			objs.add(new Warehouse(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			wh.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testExtractInfo() {
		Warehouse wh = null;
		try {
			wh = Utilities.getAWarehouse(Utilities.getACompany().getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		int id = wh.id;
		String name = wh.getInstanceName();
		wh.clear();
		try {
			wh.find(id);
		} catch (Exception e) {
			fail("find" + e.getLocalizedMessage());
		}
		if (wh.id != id)
			fail("id");
		if (!wh.getInstanceName().equals(name))
			fail("name");
	}

	@Test
	public void testList() {
		Company company = Utilities.getACompany();
		int expected = 37;
		for (int i = 0; i < expected; i++)
			try {
				Utilities.getAWarehouse(company.getAnchor());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}

		// plus default warehouse
		try {
			if (new Warehouse(sVars).listAll().size() != expected)
				fail("expected " + expected + ", got " + new Warehouse(sVars).listAll().size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIsLoaded() {
		Warehouse wh = null;
		try {
			wh = Utilities.getAWarehouse(Utilities.getACompany().getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!wh.isLoaded())
			fail("isLoaded");
	}

	@Test
	public void testUpdate() {
		Warehouse wh = null;
		try {
			wh = Utilities.getAWarehouse(Utilities.getACompany().getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		int id = wh.id;
		String tmp = "akdfsjadsf";
		try {
			wh.setInstanceName(tmp);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			wh.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			wh.update();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		wh.clear();
		try {
			wh.find(id);
		} catch (Exception e) {
			fail("find");
		}
		if (!wh.getInstanceName().equals(tmp))
			fail("update");
	}

	@Test
	public void testGetChildrenNothingLoaded() {
		Warehouse warehouse = null;
		SearchTargets objs = new SearchTargets(sVars);
		try {
			warehouse = new Warehouse(sVars);
			objs.add(warehouse, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		boolean failed = false;
		try {
			warehouse.getChildren(new Level1(sVars));
			failed = true;
		} catch (Exception e) {
		}
		if (failed)
			fail("allowed unloaded warehouse");
	}

	@Test
	public void testGetChildrenLocationNotLoaded() {
		Location location = null;
		SearchTargets objs = new SearchTargets(sVars);
		try {
			location = new Location(sVars);
			objs.add(location, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		boolean failed = false;
		try {
			location.getChildren(new Level2(sVars));
			failed = true;
		} catch (Exception e) {
		}
		if (failed)
			fail("allowed unloaded Level1");
	}

	@Test
	public void testGetChildrenNoWarehouseLoaded() {
		Warehouse warehouse = null;
		Location location = null;
		SearchTargets objs = new SearchTargets(sVars);
		try {
			warehouse = new Warehouse(sVars);
			location = new Location(sVars);
			warehouse.add(new Anchor(sVars));
			objs.add(warehouse, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objs.add(location, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		boolean failed = false;
		try {
			location.getChildren(new Level2(sVars));
			failed = true;
		} catch (Exception e) {
		}
		if (failed)
			fail("allowed unloaded Level1");
	}
}
