package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;

import comTest.utilities.Utilities;

public class OrphanTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddOrphan() {
		Level2 levelTwo = null;
		try {
			levelTwo = new Level2(sVars);
			levelTwo.add(Utilities.getAnAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * create an orphan, assign the orphan to a parent, verify the orphan is list is
	 * empty.
	 */
	@Test
	public void testAddWasOrphan() {
		// add an orphan, verify orphan list
		Level2 orphan = null;
		Level1 parent = null;
		// Orphan orphan = null;
		try {
			orphan = new Level2(sVars);
			parent = new Level1(sVars);
			orphan.add(Utilities.getAnAnchor());
			parent.add(orphan.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		
		try {
			if (!orphan.isOrphan(parent))
				fail("orphan is not an orphan");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		
		// assign the orphan and see if orphan list is empty
		try {
			parent.addChild(orphan);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (orphan.isOrphan(parent))
				fail("orphan is still an orphan");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}	
}
