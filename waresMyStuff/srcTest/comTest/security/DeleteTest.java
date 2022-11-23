package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.security.Anchor;
import com.security.MyObjectsArray;

import comTest.utilities.Utilities;

public class DeleteTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		new Utilities().allNewTables(sVars);
		new Level1(sVars).newTable(sVars);
		new Level2(sVars).newTable(sVars);
		new Level3(sVars).newTable(sVars);
		new OneParentChild(sVars).newTable(sVars);
		new ChildWithOnlyOneParent(sVars).newTable(sVars);
		// MyLinkInternals.addInternal(new Level1(sVars), new Level2(sVars),
		// "Level1 to Level2", MyLinkInternals.LINKTYPE.EXTERNAL,
		// MyLinkInternals.DELETETYPE.KEEPDESCENDANTS);
		// MyLinkInternals.addInternal(new Level2(sVars), new Level3(sVars),
		// "Level2 to Level3", MyLinkInternals.LINKTYPE.EXTERNAL,
		// MyLinkInternals.DELETETYPE.KEEPDESCENDANTS);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	// create a minimal object that has external links and then try to delete it
	public void testDeleteOneLevel() {
		// Anchor anchor = Utilities.getAnAnchor();

		Level1 l1 = null;
		try {
			l1 = new Level1(sVars);
			l1.setInstanceName("level1 name");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			l1.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			l1.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyObjectsArray myObjs = new MyObjectsArray();

		try {
			myObjs.add(new Level1(sVars));
			l1.deleteTest();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
//			l1.deleteUnconditionally(myObjs);
			l1.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	// create a minimal object that has external links and then try to delete it
	public void testDeleteMultiLevel() {
		// Anchor anchor = Utilities.getAnAnchor();

		Level2 l2 = null;
		try {
			l2 = new Level2(sVars);
			l2.setInstanceName("level2 name");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			l2.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			l2.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// create the multiple levels
		MyObjectsArray myObjs = new MyObjectsArray();

		try {
			myObjs.add(new Level1(sVars));
			myObjs.add(new Level2(sVars));
			myObjs.add(new Level3(sVars));
			l2.deleteTest();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			l2.deleteUnconditionally();
//			l2.deleteUnconditionally(myObjs);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteWithInternalLinks() throws Exception {
		// Anchor anchor = Utilities.getAnAnchor();
		POCWithOnlyOneParent parent = new POCWithOnlyOneParent(sVars);
		// parent.newTable(sVars);
		try {
			parent.setInstanceName("parent");
			parent.add(new Anchor(sVars));
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		ChildWithOnlyOneParent oneParentChild = new ChildWithOnlyOneParent(sVars);
		try {
			oneParentChild.setInstanceName("oneParentChild name");
			oneParentChild.add(parent.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			parent.addChild(oneParentChild);
			// oneParentChild.addAsParent(parent);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// create the multiple levels
		MyObjectsArray myObjs = new MyObjectsArray();
		myObjs.add(new POCWithOnlyOneParent(sVars));
		myObjs.add(new ChildWithOnlyOneParent(sVars));
		try {
			oneParentChild.deleteTest();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			oneParentChild.deleteUnconditionally();
//			oneParentChild.deleteUnconditionally(myObjs);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
