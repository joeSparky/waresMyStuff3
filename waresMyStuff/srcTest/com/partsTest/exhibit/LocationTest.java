package com.partsTest.exhibit;

import static org.junit.Assert.fail;

//import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.parts.inOut.Attributes;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.parts.security.PartLink;
import com.parts.warehouse.Warehouse;
import com.partsTest.Utilities.Utilities;
import com.security.Anchor;
import com.security.Company;
import com.security.ExceptionCoding;
import com.security.MyObjects;

public class LocationTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		if (sVars==null)
		sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMoveItemWithUnloadedItem() {
		Company company = Utilities.getACompany();
		Location destination = null;
		Location source = null;
		try {
			// Anchor anchor = company.getAnchor();
			Warehouse w = Utilities.getAWarehouse(company.getAnchor());
			Anchor warehouseAnchor = new Anchor(sVars).add(w);
			destination = Utilities.getLocation(warehouseAnchor);
			source = Utilities.getLocation(warehouseAnchor);
//			Attributes family = Utilities.getAFamily(w, warehouseAnchor);
//			Part member = Utilities.getAMember(family, warehouseAnchor);
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		try {
			destination.moveItem(new Part(sVars), source, 33);
			fail("did not find unloaded item");
		} catch (Exception e) {
		}
	}

	@Test
	public void testMoveItemWithUnloadedDestination() {
		Part item = null;
		Location source = null;
		try {
			Company company = Utilities.getACompany();
			Warehouse w = Utilities.getAWarehouse(company.getAnchor());
			Anchor anchor = w.getAnchor();

			Attributes family = Utilities.getAFamily(w, anchor);
//			Part member = Utilities.getAMember(family, anchor);

			Anchor warehouseAnchor = null;

			warehouseAnchor = new Anchor(sVars).add(w);
			source = Utilities.getLocation(warehouseAnchor);
			item = Utilities.getAMember(family, anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			new Location(sVars).moveItem(item, source, 9);
			fail("did not find unloaded destination");
		} catch (Exception e) {
		}
	}

	@Test
	public void testMoveItemWithUnloadedSource() {
		Location destination = null;
		Warehouse w = null;
		Part member = null;
		try {
			Company company = Utilities.getACompany();
			w = Utilities.getAWarehouse(company.getAnchor());
			Anchor anchor = w.getAnchor();
			destination = new Location(sVars);
//			Location source = null;
			Attributes family = Utilities.getAFamily(w, anchor);
			member = Utilities.getAMember(family, anchor);

//			Anchor warehouseAnchor = new Anchor(sVars).add(w);
//			source = Utilities.getLocation(warehouseAnchor);
//			item = Utilities.getAMember(member, source, anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			destination.moveItem(member, new Location(sVars), 4);
			fail("did not find unloaded source");
		} catch (Exception e) {
		}
	}

	@Test
	public void testMoveItemWithUnlinkedSource() throws ExceptionCoding {
		Part part = null;
		Location source = null;
		Location destination = null;
		try {
			Company company = Utilities.getACompany();
			Warehouse w = Utilities.getAWarehouse(company.getAnchor());
			Anchor anchor = w.getAnchor();
			Attributes family = Utilities.getAFamily(w, anchor);
			part = Utilities.getAMember(family, anchor);
			Anchor warehouseAnchor = new Anchor(sVars).add(w);
			destination = Utilities.getLocation(warehouseAnchor);
			source = Utilities.getLocation(warehouseAnchor);
			part.addChild(source, 4545);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// delete the link
		PartLink mlo = null;
		try {
			mlo = new PartLink(part, source, sVars).find();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!mlo.linkExists())
			fail("link should exist");
		try {
			mlo.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			destination.moveItem(part, source, 1);
			fail("did not find missing item -> source link");
		} catch (Exception e) {
		}
	}

	@Test
	public void testMoveLocationNotLoaded() {
		try {
			new Location(sVars).moveChildrenOfThisParentToNewParent(new Location(sVars), new Location(sVars));
			fail("did not catch unloaded source location");
		} catch (Exception e) {
		}
	}

	// put an item at a location
	@Test
	public void testUtilityLocation() {
		Company company = Utilities.getACompany();
		Anchor companyAnchor = null;
		try {
			companyAnchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Warehouse w = Utilities.getAWarehouse(companyAnchor);
		Anchor warehouseAnchor = null;
		try {
			warehouseAnchor = new Anchor(sVars).add(w);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Location source = null;
		Attributes family = Utilities.getAFamily(w, warehouseAnchor);
		Part member = Utilities.getAMember(family, warehouseAnchor);
		try {
			source = Utilities.getLocation(warehouseAnchor);
			member.addChild(source, 99);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		MyObjects objs = null;
		try {
			objs = source.listAllParentsOfType(member);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (objs.size() != 1)
			fail("expected 1 item in the source location, found " + objs.size());
		if (!objs.contains(member))
			fail("did not find item at source.");
	}

	@Test
	public void testEquals() {
		// Warehouse warehouse = null;
		Location loc = null;
		Anchor warehouseAnchor = null;
		try {
			Company company = Utilities.getACompany();
			Warehouse warehouse = Utilities.getAWarehouse(company.getAnchor());
			warehouseAnchor = new Anchor(sVars).add(warehouse);
			loc = Utilities.getLocation(warehouseAnchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Location secondLocation = null;
		try {
			secondLocation = Utilities.getLocation(warehouseAnchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (!loc.equals(loc))
				fail("loc is not equal to itself");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (loc.equals(secondLocation))
				fail("loc is not secondLocation");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * create two locations in a parent-child configuration. try to delete the
	 * parent. see if deleteTest complains
	 */
	@Test
	public void testDeleteTest() {
		// Anchor anchor = null;
		Warehouse warehouse = null;
		Anchor warehouseAnchor = null;
		Company company = null;
		Location loc = null;
		try {
			company = Utilities.getACompany();
			warehouse = Utilities.getAWarehouse(company.getAnchor());
			warehouseAnchor = new Anchor(sVars).add(warehouse);
			loc = Utilities.getLocation(warehouseAnchor);
			Location secondLocation = Utilities.getLocation(warehouseAnchor);
			company.addChild(warehouse);
			warehouse.addChild(loc);
			loc.addChild(secondLocation);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			loc.deleteTest();
		} catch (Exception e) {
			fail("did not allow the deletion of parent location");
		}
	}
}
