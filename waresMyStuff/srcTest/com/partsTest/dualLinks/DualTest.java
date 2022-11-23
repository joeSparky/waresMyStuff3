package com.partsTest.dualLinks;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.partsTest.Utilities.Utilities;
import com.security.Anchor;
import com.security.Company;

import comTest.security.Level1;
import comTest.security.Level2;

public class DualTest {
	Level1 pe = null;
	Level1 parentOfChildWithIdInside = null;
	Level2 cob = null;
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
//		sVars = new SessionVars();
////		
//		new Utilities().allNewTables(sVars);
	}

	void mySetup() {
		try {
			sVars = new SessionVars();
//			String dbName  = new XML(sVars).readXML(MyConnection.XMLDBNAME);
			new Utilities().allNewTables(sVars);
			Company company = Utilities.getACompany();
			Anchor anchor = company.getAnchor();
			pe = new Level1(sVars);
			parentOfChildWithIdInside = new Level1(sVars);
			cob = new Level2(sVars);
			// create instances
			pe.setInstanceName("external");
			parentOfChildWithIdInside.setInstanceName("internal");
			cob.setInstanceName("child of both");
			pe.add(anchor);
			parentOfChildWithIdInside.add(anchor);
			cob.add(anchor);
			pe.addChild(cob);
			parentOfChildWithIdInside.addChild(cob);
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace()) {
				System.out.println(ste.toString());
			}
			fail(e.getLocalizedMessage());
		}

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	// delete the parent of an externally linked pair. The child should not be
	// deleted.
	public void testDeleteExternal() {
		mySetup();
		try {
			pe.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// verify that child was not deleted
		try {
			cob.find(cob.id);
		} catch (Exception e) {
			fail("child deleted. " + e.getLocalizedMessage());
		}
	}

	@Test
	// create an internal link and delete the parent. The child should not be
	// deleted.
	public void testDeleteInternal() {
		mySetup();
		try {
			parentOfChildWithIdInside.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// verify that child was not deleted
		try {
			cob.find(cob.id);
		} catch (Exception e) {
			fail("child was deleted.");
		}
	}
}
