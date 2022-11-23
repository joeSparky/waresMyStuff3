package comTest.security.MyObject;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.security.Anchor;

import comTest.utilities.Utilities;

public class MyObjectTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDeleteUnconditionally() {
		Anchor anchor = Utilities.getAnAnchor();
		ParentA parent = null;
		ParentA uncle = null;
		ChildA child = null;
		ChildOfChildA childOfChildA = null;
		try {
			parent = new ParentA(sVars);
			uncle = new ParentA(sVars);
			child = new ChildA(sVars);
			childOfChildA = new ChildOfChildA(sVars);
			parent.add(anchor);
			child.add(parent);
			parent.addChild(child);
			uncle.add(anchor);
			uncle.addChild(child);
			// anchor
			childOfChildA.add(child);
			// childhood
			child.addChild(childOfChildA);

			// check links
			if (!parent.listChildren(new ChildA(sVars)).containsMyObject(child)) {
				fail("ChildA is not listed as a child of ParentA");
			}
			if (!uncle.listChildren(new ChildA(sVars)).containsMyObject(child)) {
				fail("ChildA is not listed as a child of UncleA");
			}
			if (!child.listAllParentsOfType(new ParentA(sVars)).containsMyObject(parent)) {
				fail("parent is not listed as a parent of child");
			}
			if (!child.listAllParentsOfType(new ParentA(sVars)).containsMyObject(uncle)) {
				fail("uncle is not listed as a parent of child");
			}
			if (!child.listChildren(new ChildOfChildA(sVars)).containsMyObject(childOfChildA)) {
				fail("childOfChildA is not listed as a child of childA");
			}
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// delete the child, check the links
		try {
			child.deleteUnconditionally();
			if (!parent.listChildren(new ChildA(sVars)).isEmpty())
				fail("list of children of parent not empty");
			if (!uncle.listChildren(new ChildA(sVars)).isEmpty())
				fail("list of children of uncle not empty");
			if (!childOfChildA.listAllParentsOfType(parent).isEmpty())
				fail("list of parents of child not empty");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

	}

}
