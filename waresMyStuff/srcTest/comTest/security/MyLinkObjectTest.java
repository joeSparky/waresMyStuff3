package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.Utils;
import com.security.Anchor;
import com.security.MyLinkObject;
import com.security.MyObject;
import com.security.MyObjectsArray;

import comTest.utilities.Utilities;

public class MyLinkObjectTest {

	MyObjectsArray moa = null;
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		new Utilities().allNewTables(sVars);
		loadMoa();
	}

	static int PARENTOBJECTMUSTBEONE = 0;
	static int CHILDOBJECTMUSTBEONE = 1;
	/**
	 * not in link tables
	 */
	static int NOTINTABLES = 2;
	// create a link between two objects, allow them to have a quantity greater
	// than 1
	static int PARENTOBJECTMUSTBEPOSITIVE = 3;
	static int CHILDOBJECTMUSTBEPOSITIVE = 4;
	static int PARENTOFCHILDWITHONLYONEPARENT = 5;
	static int CHILDWITHONLYONEPARENT = 6;

	protected void loadMoa() throws Exception {
		Anchor anchor = Utilities.getAnAnchor();
		moa = new MyObjectsArray();
		// the first (PARENTOFLINK) and second (CHILDOFLINK) entries must both
		// exist and have a link between them
		Level1 levelOne = null;
		// levelOne.newTable(sVars);
		MyObject parentObjectMustBeOne = null;
		try {
			levelOne = new Level1(sVars);
			levelOne.add(anchor);
			parentObjectMustBeOne = Utilities.getLevel1(levelOne.getAnchor());
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		new Level2(sVars).newTable(sVars);
		MyObject childObjectMustBeOne = null;
		try {
			childObjectMustBeOne = Utilities.getLevel2(parentObjectMustBeOne.getAnchor());
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// clear any existing links
		MyLinkObject m = new MyLinkObject(parentObjectMustBeOne, childObjectMustBeOne, sVars);
		m.newTable(m.getMyFileName());
		// try {
		// m.add(1);
		// } catch (Exception e1) {
		// fail(e1.getLocalizedMessage());
		// }
		// create new links
		// MyLinkInternals.addInternal(parentObjectMustBeOne,
		// childObjectMustBeOne,
		// "hufus", MyLinkInternals.LINKTYPE.EXTERNAL,
		// MyLinkInternals.DELETETYPE.KEEPDESCENDANTS);
		// MyLinkObject mlo = new MyLinkObject(parentObjectMustBeOne,
		// childObjectMustBeOne);
		try {
			parentObjectMustBeOne.addChild(childObjectMustBeOne);
			// mlo.add(1);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		moa.add(parentObjectMustBeOne);
		moa.add(childObjectMustBeOne);

		// the third object (NOTINTABLES) must not exist in the tables
		moa.add(new Level1(sVars));

		// the fourth (PARENTOBJECTMUSTBEPOSITIVE) and
		// (CHILDOBJECTMUSTBEPOSITIVE) must have a link that allows a quantity
		// greater than 1
		MyObject parentObjectMustBePositive = null;
		try {
			parentObjectMustBePositive = Utilities.getLevel2(parentObjectMustBeOne.getAnchor());
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		new Level3(sVars).newTable(sVars);
		MyObject childObjectMustBePositive = null;
		try {
			childObjectMustBePositive = Utilities.getLevel3(parentObjectMustBeOne.getAnchor());
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// MyLinkInternals.addInternal(parentObjectMustBePositive,
		// childObjectMustBePositive,
		// "plural",
		// MyLinkInternals.LINKTYPE.EXTERNAL,
		// MyLinkInternals.DELETETYPE.KEEPDESCENDANTS);
		// MyLinkObject plural = new MyLinkObject(parentObjectMustBePositive,
		// childObjectMustBePositive);
		try {
			// quantity of a random positive integer
			// plural.add(35);
			parentObjectMustBePositive.addChild(childObjectMustBePositive);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		moa.add(parentObjectMustBePositive);
		moa.add(childObjectMustBePositive);

		new POCWithOnlyOneParent(sVars).newTable(sVars);
		new ChildWithOnlyOneParent(sVars).newTable(sVars);
		MyObject parentOfChildWithOnlyOneParent = new POCWithOnlyOneParent(sVars);
		try {
			parentOfChildWithOnlyOneParent.setInstanceName("adsfh" + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			parentOfChildWithOnlyOneParent.add(parentObjectMustBeOne.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyObject childWithOnlyOneParent = new ChildWithOnlyOneParent(sVars);
		try {
			childWithOnlyOneParent.setInstanceName("sdjhaas" + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			childWithOnlyOneParent.add(parentObjectMustBeOne.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		moa.add(parentOfChildWithOnlyOneParent);
		moa.add(childWithOnlyOneParent);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMyLinkObject() {
		// constructor
		try {
			new MyLinkObject(moa.get(PARENTOBJECTMUSTBEONE), moa.get(CHILDOBJECTMUSTBEONE), sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testGetTableName() {

		MyLinkObject mlo = null;
		try {
			mlo = new MyLinkObject(moa.get(PARENTOBJECTMUSTBEONE), moa.get(CHILDOBJECTMUSTBEONE), sVars);
			if (mlo.getMyFileName().equals(moa.get(PARENTOBJECTMUSTBEONE).getClass().getCanonicalName() + "To"
					+ moa.get(CHILDOBJECTMUSTBEONE).getClass().getCanonicalName()))
				fail("table name of " + mlo.getMyFileName());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void testGetNewChild() {
		// find an object that can have a child
		MyObject child = null;
		for (MyObject mo : moa)
			try {
				if (moa.canHaveChildren(mo)) {
					child = mo;
					break;
				}
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
		if (child == null)
			fail("no objects can have a child, test not run.");

		MyLinkObject mlo = null;
		try {
			mlo = new MyLinkObject(moa.get(PARENTOBJECTMUSTBEONE), child, sVars);
			mlo.getNewChild();
		} catch (Exception e) {
			fail("could not get a child for object " + child.getClass().getCanonicalName());
		}
	}

	@Test
	public void testSanityChildNotFound() {
		MyObject parent = moa.get(NOTINTABLES);
		MyObject child = moa.get(NOTINTABLES);
		MyLinkObject mlo = null;
		try {
			mlo = new MyLinkObject(parent, child, sVars);
			mlo.sanity();
			fail("sanity passed with child that does not exist");
		} catch (Exception e) {
		}
	}

	@Test
	public void testLinkNotExists() {
		try {
			if (!moa.get(PARENTOBJECTMUSTBEONE).linkToChildExists(moa.get(CHILDOBJECTMUSTBEONE)))
				// if (!new MyLinkObject(moa.get(PARENTOBJECTMUSTBEONE),
				// moa.get(CHILDOBJECTMUSTBEONE)).linkExists())
				fail("link does not exist");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteTest() {
		MyLinkObject mlo = null;
		try {
			mlo = new MyLinkObject(moa.get(PARENTOBJECTMUSTBEONE), moa.get(CHILDOBJECTMUSTBEONE), sVars);
			mlo.find();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			mlo.deleteTest();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteUnconditionally() {
		MyLinkObject mlo = null;
		try {
			mlo = new MyLinkObject(moa.get(PARENTOBJECTMUSTBEONE), moa.get(CHILDOBJECTMUSTBEONE), sVars);
			mlo.find();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			mlo.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIsLoaded() {
		MyLinkObject mlo = null;
		try {
			mlo = new MyLinkObject(moa.get(PARENTOBJECTMUSTBEONE), moa.get(CHILDOBJECTMUSTBEONE), sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!mlo.isLoaded())
			fail("should be loaded");

		// parent not loaded
		try {
			mlo = new MyLinkObject(moa.get(NOTINTABLES), moa.get(PARENTOBJECTMUSTBEONE), sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (mlo.isLoaded())
			fail("parent should not not be loaded");

		// child not loaded
		try {
			mlo = new MyLinkObject(moa.get(PARENTOBJECTMUSTBEONE), moa.get(NOTINTABLES), sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (mlo.isLoaded())
			fail("child should not not be loaded");
	}
}
