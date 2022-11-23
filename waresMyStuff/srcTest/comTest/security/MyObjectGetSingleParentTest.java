package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.security.Anchor;
import com.security.MyObject;
import com.security.MyObjectsArray;

import comTest.utilities.Utilities;

public class MyObjectGetSingleParentTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	// this is recursive. this has parent. find parent without looking in the
	// stack.
	@Test
	public void testGetSingleParentMyObjectsArray() {
		Recurse parent = null;
		Recurse child = null;
		try {
			parent = new Recurse(sVars);
			child = new Recurse(sVars);
			parent.add(new Anchor(sVars));
			child.add(parent);
			parent.addChild(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyObject found = null;
		try {
			found = child.getSingleParent(new Recurse(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (!found.equals(parent))
				fail("did not find parent.");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	// this is recursive. this has no recursive parent. look in empty stack.
	// throw an exception for an empty stack
	@Test
	public void testGetSingleParentNoParent() {
		Recurse parent = null;
		Recurse child = null;
		try {
			parent = new Recurse(sVars);
			child = new Recurse(sVars);
			parent.add(new Anchor(sVars));
			child.add(parent);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			child.getSingleParent(new Recurse(sVars));
			fail("allowed looking in an empty stack.");
		} catch (Exception e) {
		}
	}

	// this is recursive. this has no recursive parent. look in stack for loaded
	// parent
	@Test
	public void testGetSingleParentRecursiveOrphan() {
		RecursiveNot parent = null;
		Recurse child = null;
		MyObjectsArray stack = new MyObjectsArray();
		try {
			parent = new RecursiveNot(sVars);
			child = new Recurse(sVars);
			parent.add(new Anchor(sVars));
			child.add(parent);
			parent.addChild(child);
			stack.add(parent);
			stack.add(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyObject found = null;
		try {
			found = child.getSingleParent(new RecursiveNot(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (!found.equals(parent))
				fail("did not find parent.");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
