package com.partsTest.forms;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.parts.forms.CSVFormSelect;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.parts.security.PartLink;
import com.parts.warehouse.Warehouse;
import com.partsTest.Utilities.Utilities;
import com.security.Company;

public class testCSVForm {
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

//	@Test
	// TODO broken testDumpInventory
	public void testDumpInventory() {
		Company company = Utilities.getACompany();
		Warehouse warehouse = null;
		Part partOne = null;
		Location location = null;

		try {
			warehouse = Utilities.getAWarehouse(company.getAnchor());
			partOne = new Part(sVars);
			partOne.setInstanceName("my part name");
			partOne.add(warehouse);
			location = new Location(sVars);
			location.add(warehouse);
			new PartLink(partOne, location, sVars).add().updateAddQuantity(33, false);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		SessionVars sVars = new SessionVars();
		sVars.userNumber = 8483;
		sVars.warehouseNumber = warehouse.id;
		String results = null;
		try {
			results = new CSVFormSelect(new FormsMatrixDynamic(sVars), new SessionVars()).dumpInventory(location);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!results.contains("my part name, 33"))
			fail("did not find \"my part name, 33\"");
		System.out.println(results);
	}

}
