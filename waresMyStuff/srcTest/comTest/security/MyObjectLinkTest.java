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

/**
 * test the MyObject instances that can have only one parent (the ID of the
 * parent resides in the child)
 * 
 * @author Joe
 *
 */
public class MyObjectLinkTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
		new NoParents(sVars).newTable(sVars);
		new OneParentChild(sVars).newTable(sVars);
		// MyLinkInternals.newLinkTable(new NoParents(sVars), new OneParentChild(sVars));
	}

	@After
	public void tearDown() throws Exception {
	}

	MyObject noParents = null;
	MyObject oneParent = null;
	MyObjectsArray objs = null;

	public void setup() {
		Anchor anchor = Utilities.getAnAnchor();
		// get a parent that can not have parents
		
		try {noParents = new NoParents(sVars);
			noParents.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			noParents.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		
		// try {
		// oneParent.setParentId( noParents.id);
		// } catch (Exception e1) {
		// fail(e1.getLocalizedMessage());
		// }
		try {// get a child that can have only one parent
		oneParent = new OneParentChild(sVars);
			oneParent.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			oneParent.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		objs = new MyObjectsArray();
		objs.add(noParents);
		objs.add(oneParent);

		// MyLinkInternals.addInternal(noParents, oneParent,
		// "fer shur",
		// MyLinkInternals.LINKTYPE.EXTERNAL,
		// MyLinkInternals.DELETETYPE.DELETEDESCENDANTS);

		try {
			// new MyLinkObject(noParents, oneParent).add(1);
			noParents.addChild(oneParent);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * test adding a parent and a child
	 */
	@Test
	public void testAdd() {
		setup();
	}

	/**
	 * try to delete the parent
	 */
//	@Test
//	public void testDelete() {
//		setup();
//		try {
//			noParents.deleteTest();
//			fail("parent deletion should have failed");
//		} catch (Exception e) {
//		}
//	}
}
