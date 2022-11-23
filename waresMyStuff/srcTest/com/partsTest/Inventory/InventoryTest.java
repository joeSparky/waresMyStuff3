package com.partsTest.Inventory;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.parts.security.PartLink;
import com.partsTest.Utilities.Utilities;
import com.security.Anchor;

import comTest.security.Level1;
import comTest.security.Level2;

public class InventoryTest {
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

	@Test
	public void testAddResetTimestamp() {
		PartLink il = null;
		Level1 levelOne = null;
		Level2 levelTwo = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			levelOne = new Level1(sVars);
			levelOne.add(new Anchor(sVars));
			levelTwo = new Level2(sVars);
			levelTwo.add(levelOne);
			il = new PartLink(levelOne, levelTwo, sVars);
			il.setLinkQuantity(10);
			il.add();
			// today's date?
			if (il.getInventoryDate().equals(new Date()))
				fail("expected '" + sdf.format(new Date()) + " but got " + sdf.format(il.getInventoryDate()));
//			// set the internal date to something in the past
//			il.setDate(new Date(48448L*1000));
//			// check for a reset of the time and a quantity of 33
//			il.add(33, true);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// set the internal date to something in the past
		il.setDate(new Date(48448L * 1000));
		// check for a reset of the time and a quantity of 33
		try {
			il.updateAddQuantity(33, true);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		Date inventDate = null;
		try {
			inventDate = il.getInventoryDate();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		String expectedDate = sdf.format(new Date());
		String gotDate = sdf.format(inventDate);

		if (!expectedDate.equals(gotDate))
			fail("expected '" + expectedDate + " but got " + gotDate);
		try {
			if (il.getLinkQuantity() != 43)
				fail("expected quantity of 43, got " + il.getLinkQuantity());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

//	@Test
//	public void testAddQuantityAndDateDate() {
//		PartLink il = null;
//		Level1 levelOne = null;
//		Level2 levelTwo = null;
//		try {
//			levelOne = new Level1(sVars);
//			levelOne.add(new Anchor(sVars));
//			levelTwo = new Level2(sVars);
//			levelTwo.add(levelOne);
//			il = new PartLink(levelOne, levelTwo);
//			il.add(33, Timestamp.valueOf("1973-11-21 01:02:03"));
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (!new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(il.inventoried).equals("1973-11-21 01:02:03"))
//			fail("expected timestamp of 1973-11-21 01:02:03, got " + il.inventoried);
//
//		if (il.getLinkQuantity() != 33)
//			fail("expected quantity of 33, got " + il.getLinkQuantity());
//	}

}
