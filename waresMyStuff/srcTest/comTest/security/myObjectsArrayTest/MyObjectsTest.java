package comTest.security.myObjectsArrayTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.security.Anchor;
import com.security.MyObject;
import com.security.MyObjectsArray;

import comTest.security.Recurse;
import comTest.utilities.Utilities;

public class MyObjectsTest {
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
	public void testGetRecursiveParents() throws Exception {
		Recurse recursiveAnchor = null;
		Recurse last = null;
		int towardsTopId = 0;
		int towardsBottomId = 0;
		try {
			recursiveAnchor = new Recurse(sVars);
			recursiveAnchor.add(new Anchor(sVars));
			last = (Recurse) new Recurse(sVars).add(recursiveAnchor);
			recursiveAnchor.addChild(last);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		for (int i = 0; i < 15; i++) {
			Recurse next = null;
			try {
				next = new Recurse(sVars);
				next.add(recursiveAnchor);
				last.addChild(next);
				if (i == 3)
					towardsTopId = next.id;
				if (i == 11)
					towardsBottomId = next.id;
				last = next;
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
		}
		// get an instance of recursive that was towards the bottom of the list
		MyObject startingPoint = null;
		MyObjectsArray stack = new MyObjectsArray();
		try {
			startingPoint = new Recurse(sVars).find(towardsBottomId);
			stack.getRecursiveParents(startingPoint, sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (stack.size() != 13)
			fail("expected a size of 13, got " + stack.size());

		Recurse towardsTop = null;
		try {
			towardsTop = new Recurse(sVars);
			towardsTop.find(towardsTopId);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (!stack.containsMyObject(towardsTop))
			fail("could not find towardsTop in stack");
	}

}
