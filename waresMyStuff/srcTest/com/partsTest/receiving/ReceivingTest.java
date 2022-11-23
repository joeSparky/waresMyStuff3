package com.partsTest.receiving;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.parts.inOut.Attributes;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.parts.security.PartLink;
import com.parts.warehouse.Warehouse;
import com.partsTest.Utilities.Utilities;
import com.security.Anchor;
import com.security.Company;

public class ReceivingTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();

		// System.out.println("database
		// name:"+XML.readStandardXML(MyConnection.XMLDBNAME));
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void initialTest() {
//		// Warehouse warehouse = Utilities.getAWarehouse();
//		// Family family = Utilities.getAFamily(warehouse);
//		// Member member = Utilities.getAMember(family);
//		// Location loc = Utilities.getLocation(warehouse);
////		TimeReceived item = null;
//		// item.parentId = member.id;
//		// item.locationId = loc.id;
//		try {item = new TimeReceived();
//			// item.sanity();
//			item.add(Utilities.getAnAnchor());
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//	}

	@Test
	public void consolidateTest() {
		Company company = Utilities.getACompany();
		Warehouse warehouse = null;
		Anchor warehouseAnchor = null;
		try {
			warehouse = Utilities.getAWarehouse(company.getAnchor());
			warehouseAnchor = new Anchor(sVars).add(warehouse);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Attributes family = null;
		try {
			family = Utilities.getConsolidateFamily(warehouse, warehouseAnchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Part member = null;
		try {
			member = Utilities.getAMember(family, warehouseAnchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		//Anchor a = null;
//		try {
//			a = new Anchor(sVars).add(warehouse);
//		} catch (AnchorNotFoundException e2) {
//			fail(e2.getLocalizedMessage());
//		}
		Location loc = Utilities.getLocation(warehouseAnchor);
//		TimeReceived item = null;
//		try {
//			item = Utilities.getAnItem(member, loc, warehouseAnchor);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
		PartLink pl = null;
		try {
			pl = new PartLink(member, loc, sVars);
			pl.setLinkQuantity(1);
			pl.add();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (pl.getLinkQuantity() != 1)
				fail("expected quantInStock of 1, got " + pl.getLinkQuantity());
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// put another of the same item at the same location.
		try {
			pl.updateAddQuantity(1, false);
//			pl.addQuantityResetDate(1);
//			item.addChild(loc, 1);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (pl.getLinkQuantity() != 2)
				fail("consolidation failure");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	// receive an item into a location. see if the location is recommended.
	@Test
	public void preferredLocationTestA() {
		// put an item into the warehouse
		Company company = Utilities.getACompany();
		Warehouse warehouse = null;
		Anchor warehouseAnchor = null;
		try {
			warehouse = Utilities.getAWarehouse(company.getAnchor());
			warehouseAnchor = new Anchor(sVars).add(warehouse);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Attributes family = null;
		try {
			family = Utilities.getConsolidateFamily(warehouse, warehouseAnchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Part part = null;
		try {
			part = Utilities.getAMember(family, warehouseAnchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Location loc = Utilities.getLocation(warehouseAnchor);
		PartLink pl = null;
		try {
			pl = new PartLink(part, loc, sVars);
			pl.setLinkQuantity(3);
			pl.add();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (pl.getLinkQuantity() != 3)
				fail("expected quantity of 3, got " + pl.getLinkQuantity());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
