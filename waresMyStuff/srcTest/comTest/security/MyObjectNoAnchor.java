package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.security.Anchor;

import comTest.utilities.Utilities;

public class MyObjectNoAnchor {
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
	public void testAddMyObject() {
		Level1 levelOne = null;
		try {
			levelOne = new Level1(sVars);
			levelOne.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			new NoAnchor(sVars).add(levelOne);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		
	}

	@Test
	public void testAddAnchor() {
		try {
			new NoAnchor(sVars).add(new Anchor(sVars));
			fail("add(Anchor anchor) did not throw an exception.");
		} catch (Exception e) {
		}
	}

	@Test
	public void testListAllMyObject() {
		Level1 levelOne = null;
		try {
			levelOne = new Level1(sVars);
			levelOne.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			new NoAnchor(sVars).listAll(levelOne);
			fail("did not block listAll(MyObject obj)");
		} catch (Exception e) {
		}
	}
}
