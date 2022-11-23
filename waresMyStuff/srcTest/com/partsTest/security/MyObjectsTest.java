package com.partsTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.parts.exhibit.Kit;
import com.security.MyObjects;

import comTest.utilities.Utilities;

public class MyObjectsTest {
	SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {sVars = new SessionVars();
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * put two copies of an instance into MyObjects, look for discarding the second
	 * copy
	 */
	@Test
	public void testBlockTwoCopiesWithAdd() {
		Kit levelOne = null;
		MyObjects objs = new MyObjects();
		try {
			levelOne = new Kit(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!objs.isEmpty())
			fail("objs is not empty.");
		objs.add(levelOne);
		if (objs.size() != 1)
			fail("objs should have 1, has " + objs.size());
		objs.add(levelOne);
		if (objs.size() != 1)
			fail("objs should have 1, has " + objs.size());
	}
	
	/**
	 * put two copies of an unloaded exhibit instances into MyObjects
	 * copy
	 */
	@Test
	public void testBlockTwoCopiesWithAddDifferentInstances() {
		Kit firstExhibit = null;
		Kit secondExhibit = null;
		MyObjects objs = new MyObjects();
		try {
			firstExhibit = new Kit(sVars);
			secondExhibit = new Kit(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!objs.isEmpty())
			fail("objs is not empty.");
		objs.add(firstExhibit);
		if (objs.size() != 1)
			fail("objs should have 1, has " + objs.size());
		objs.add(secondExhibit);
		if (objs.size() != 1)
			fail("objs should have 1, has " + objs.size());
	}
	
	
	/**
	 * see if addAll blocks second copy
	 */
	@Test
	public void testBlockTwoCopiesWithAddAll() {
		Kit levelOne = null;
		MyObjects objs = new MyObjects();
		MyObjects source = new MyObjects();
		try {
			levelOne = new Kit(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		source.add(levelOne);
		
		if (!objs.isEmpty())
			fail("objs is not empty.");
		objs.addAll(source);
		if (objs.size() != 1)
			fail("objs should have 1, has " + objs.size());
		objs.addAll(source);
		if (objs.size() != 1)
			fail("objs should have 1, has " + objs.size());
	}
}
