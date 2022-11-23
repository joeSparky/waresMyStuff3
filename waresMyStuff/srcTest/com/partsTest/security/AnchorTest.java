package com.partsTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.parts.location.Location;
import com.parts.warehouse.Warehouse;
import com.security.Anchor;

import comTest.security.Level1;
import comTest.security.Level2;
import comTest.utilities.Utilities;

public class AnchorTest {
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

	// create a stack with the top selected. add the instance at the bottom of
	// the stack to see if it get an anchor of the top instance
	@Test
	public void testAdd() {
		// create several anchors
		Level2 levelTwo = null;
		try {
			levelTwo = new Level2(sVars);
			levelTwo.add(new Anchor(sVars));
			levelTwo = new Level2(sVars);
			levelTwo.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Level1 levelOne = null;
		// self-anchor the top
		try {
			levelOne = new Level1(sVars);
			levelOne.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		Anchor anchor = null;
		try {
			anchor = new Anchor(sVars);
			anchor.find(levelOne.anchorId);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (anchor.id != levelOne.anchorId)
				fail("anchor.id=" + anchor.id + " levelOne anchor id=" + levelOne.getAnchor().id);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (anchor.id != levelOne.getAnchor().id)
				fail("anchor.id=" + anchor.id + " levelOne anchor id=" + levelOne.getAnchor().id);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void testLocation() {
		Warehouse w = null;
		Location l = null;
		try {
			w = new Warehouse(sVars);
			l = new Location(sVars);
			w.add(new Anchor(sVars));
			l.add(w);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			l = new Location(sVars);
			l.add(new Anchor(sVars));
			fail("added location without a warehouse anchor");
		} catch (Exception e) {
		}

		try {
			l = new Location(sVars);
			l.add(new Level1(sVars));
			fail("added location without a warehouse anchor");
		} catch (Exception e) {
		}
		Level1 levelOne = null;
		try {
			levelOne = new Level1(sVars);
			levelOne.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			l = new Location(sVars);
			l.add(levelOne);
			fail("added location without a warehouse anchor");
		} catch (Exception e) {
		}
	}

}
