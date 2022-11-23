package com.partsTest.exhibit;

import static org.junit.Assert.fail;

import com.db.SessionVars;
import com.forms.SearchTarget;
import com.forms.SearchTargets;
import com.parts.inOut.Attributes;
import com.parts.inOut.Part;
import com.parts.warehouse.Warehouse;
import com.partsTest.Utilities.Utilities;
import com.security.ExceptionCoding;
import com.security.MyObject;

//import comTest.utilities.Utilities;

public class MyObjectTestExtended extends comTest.security.MyObjectTestA {
	SessionVars sVars = null;

	public void setUpMoa() throws Exception {
		if (sVars==null)sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
		moa = new SearchTargets(sVars);

		try {
			moa.add(new Warehouse(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			moa.add(new Attributes(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			moa.add(new Part(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// moa.add(new Item());
		utilities = new com.partsTest.Utilities.Utilities();

	}

	@Override
	protected boolean handledFullListSize(MyObject mo, int copies) {
		if (mo instanceof Warehouse) {
			// original warehouses
			int properSize = 15;
			try {
				if (mo.listAll().size() != properSize)
					fail("Expected " + properSize + " " + mo.getLogicalName() + ". Got " + mo.listAll().size());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
			// passed
			return true;
		}
		if (mo instanceof Attributes) {
			// original families
			int properSize = 10;
			try {
				if (mo.listAll().size() != properSize)
					// exit on failure
					fail("Expected " + properSize + " " + mo.getLogicalName() + ". Got " + mo.listAll().size());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
			// passed
			return true;
		}
		if (mo instanceof Part) {
			// original families
			int properSize = 5;
			try {
				if (mo.listAll().size() != properSize)
					// exit on failure
					throw new ExceptionCoding(
							"Expected " + properSize + " " + mo.getLogicalName() + ". Got " + mo.listAll().size());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
			// passed
			return true;
		}
		return false;
	}

}
