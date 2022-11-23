package com.partsTest.location;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.parts.location.Location;
import com.parts.warehouse.Warehouse;
import com.security.Anchor;
import com.security.Company;

import comTest.utilities.Utilities;

public class LocationTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void testWhyCantMove() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCanMove() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testMoveItem() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testMoveLocation() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddAnchor() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testEqualsObject() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddMyObject() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testAddWithWarehouseAnchor() {
		Location location = null;
		Company company = Utilities.getACompany();
		Warehouse warehouse = null;
		Anchor companyAnchor = null;
		Anchor warehouseAnchor = null;
		try {
			companyAnchor = company.getAnchor();
			warehouse = new Warehouse(sVars);
			warehouse.add(companyAnchor);
			warehouseAnchor = warehouse.getAnchor();
			location = new Location(sVars);
			location.add(warehouseAnchor);
			fail("allowed non-Warehouse anchor add");
		} catch (Exception e) {
		}
	}

	@Test
	public void testAddWithoutWarehouseAnchor() {
		Location location = null;
		Company asAnchor = Utilities.getACompany();
		try {
			location = new Location(sVars);
			location.add(asAnchor);
			fail("allowed non-Warehouse anchor add");
		} catch (Exception e) {
		}
	}

}
