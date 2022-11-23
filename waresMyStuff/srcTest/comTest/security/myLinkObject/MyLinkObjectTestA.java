package comTest.security.myLinkObject;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.db.MyStatement;
import com.db.SessionVars;
import com.forms.Utils;
import com.security.Anchor;
import com.security.Company;
import com.security.ExceptionCoding;
import com.security.MyLinkObject;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.MyObjectsArray;
import comTest.security.KeepDescendants;
import comTest.security.Level1;
import comTest.security.Level2;
import comTest.security.Level2ChildIdInParent;
import comTest.security.Level2Brother;
import comTest.security.Level2ParentInChild;
import comTest.security.Level3;
import comTest.security.Recurse;
import comTest.utilities.Utilities;

public class MyLinkObjectTestA {
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
	public void testExtractAnchor() {
		Anchor anchor = Utilities.getAnAnchor();
		Level1 l1 = null;
		try {
			l1 = Utilities.getLevel1(anchor);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Anchor extractedAnchor = null;
		try {
			extractedAnchor = l1.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// if (!(extractedAnchor instanceof Level1))
		// fail("extractedAnchor an instanceof "
		// + extractedAnchor.getLogicalName());
		try {
			if (!extractedAnchor.equals(l1.getAnchor()))
				fail("wrong anchor");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * internal and external links
	 */
	@Test
	public void testMyLinkObjectInternalExternal() {

		// internal links
		try {
			new MyLinkObject(new Level1(sVars), new Level2ParentInChild(sVars), sVars);
			new MyLinkObject(new Level1(sVars), new Level2Brother(sVars), sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void testGetNewChild() {
		MyLinkObject mlo = null;
		MyObject child = null;
		try {
			mlo = new MyLinkObject(new Level1(sVars), new Level2(sVars), sVars);
			child = mlo.getNewChild();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (!(child instanceof Level2))
			fail("wrong child");
	}

	@Test
	public void testSanity() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Level1 level1 = null;
		try {
			level1 = new Level1(sVars);
			level1.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Level2 level2 = null;
		try {
			level2 = new Level2(sVars);
			level2.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// MyLinkObject mlo = new MyLinkObject(level1, level2);
		try {
			level1.linkSanity(level2);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testLinkExists() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Level1 level1 = null;
		try {
			level1 = new Level1(sVars);
			level1.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Level2 level2 = null;
		try {
			level2 = new Level2(sVars);
			level2.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// MyLinkObject mlo = new MyLinkObject(level1, level2);
		try {
			if (level1.linkToChildExists(level2))
				fail("list should not exist");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			level1.addChild(level2);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testAdd() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		Level1 level1 = null;
		Level2Brother level2 = null;
		try {
			level1 = new Level1(sVars);
			level1.add(anchor);
			level2 = new Level2Brother(sVars);
			level2.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// MyLinkObject mlo = new MyLinkObject(level1, level2);
		try {
			level1.addChild(level2);
			// mlo.add(17);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	// pair of the same class, one made parent, one made child
	public void testAddOrgChartNoParentsNoKidsStandard() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		final String mine = "kdkdf";
		Recurse level1 = null;
		try {
			level1 = new Recurse(sVars);
			level1.setInstanceName(mine + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			level1.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		Level1 level2 = null;
		try {
			level2 = new Level1(sVars);
			level2.setInstanceName(mine + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			level2.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// if (!MyLinkInternals.getLinkType(level1, level2).equals(
		// MyLinkInternals.LINKTYPE.RECURSIVE))
		// fail("need recursive link for testing");

		// OrgChartLink mlo = null;

		try {
			// mlo = new OrgChartLink(level1, level2);
			level1.addChild(level2);
			// mlo.add(17);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	// pair of the same class, one made parent, one made child, try to add twice
	public void testAddOrgChartNoParentsNoKidsDoubleAdd() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		final String mine = "kdkdf";
		Recurse level1 = null;
		try {
			level1 = new Recurse(sVars);
			level1.setInstanceName(mine + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			level1.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		Level1 level2 = null;
		try {
			level2 = new Level1(sVars);
			level2.setInstanceName(mine + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			level2.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// if (!MyLinkInternals.getLinkType(level1, level2).equals(
		// MyLinkInternals.LINKTYPE.RECURSIVE))
		// fail("need recursive link for testing");

		// OrgChartLink mlo = null;

		try {
			// mlo = new OrgChartLink(level1, level2);
			level1.addChild(level2);
			// mlo.add(17);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {// try adding a duplicate link
				// mlo = new OrgChartLink(level1, level2);
			level1.addChild(level2);
			// mlo.add(17);
			fail("should not allow duplicate link");
		} catch (Exception e) {
		}

	}

	@Test
	// moe is a parent of larry is a parent of curly, all stooges
	public void testAddOrgChart3Levels() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		final String mine = "kdkdf";
		Recurse moe = null;
		Recurse larry = null;
		Recurse curly = null;
		try {
			moe = new Recurse(sVars);
			moe.setInstanceName(mine + Utils.getNextString());
			moe.add(anchor);
			larry = new Recurse(sVars);
			larry.setInstanceName(mine + Utils.getNextString());
			larry.add(anchor);
			moe.addChild(larry);
			curly = new Recurse(sVars);
			curly.setInstanceName(mine + Utils.getNextString());
			curly.add(anchor);
			larry.addChild(curly);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	// moe is a parent of larry is a parent of curly, all stooges. try adding
	// curly as a parent of larry
	public void testAddOrgChart3LevelsAddChildAsParent() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		final String mine = "kdkdf";
		Recurse moe = null;
		Recurse larry = null;
		Recurse curly = null;
		try {
			moe = new Recurse(sVars);
			larry = new Recurse(sVars);
			curly = new Recurse(sVars);
			anchor = company.getAnchor();
			moe.setInstanceName(mine + Utils.getNextString());
			moe.add(anchor);
			larry.setInstanceName(mine + Utils.getNextString());
			larry.add(anchor);
			moe.addChild(larry);
			curly.setInstanceName(mine + Utils.getNextString());
			curly.add(anchor);
			larry.addChild(curly);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// try to swap parent/child
		try {
			// mlo = new OrgChartLink(larry, moe);
			larry.addChild(moe);
			// mlo.add(17);
			fail("should not have allowed larry to be a parent of moe");
		} catch (Exception e) {
		}

		// try to swap parent/child
		try {
			curly.addChild(larry);
			fail("should not have allowed curly to be a parent of larry");
		} catch (Exception e) {
		}

		// try to swap parent/child
		try {
			curly.addChild(moe);
			fail("should not have allowed curly to be a parent of moe");
		} catch (Exception e) {
		}
	}

	@Test
	public void testAddOrgChart1Kid2Parents() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		// child with 2 parents
		final String mine = "kdkdf";
		Recurse parent1 = null;
		Recurse child = null;
		Recurse parent2 = null;
		try {
			parent1 = new Recurse(sVars);
			child = new Recurse(sVars);
			parent2 = new Recurse(sVars);
			anchor = company.getAnchor();
			parent1.setInstanceName(mine + Utils.getNextString());
			parent1.add(anchor);
			child.setInstanceName(mine + Utils.getNextString());
			child.add(anchor);
			parent1.addChild(child);
			parent2.setInstanceName(mine + Utils.getNextString());
			parent2.add(anchor);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// add second parent to child
		try {
			parent2.addChild(child);
			fail("second parent allowed");
		} catch (Exception e) {
		}
	}

	@Test
	public void testAddOrgChart1Parent2Kids() {
		// parent with 2 kids, then try swapping a child with the parent
		final String mine = "kdkdf";
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		Recurse parent = null;
		Recurse child1 = null;
		Recurse child2 = null;
		try {
			parent = new Recurse(sVars);
			child1 = new Recurse(sVars);
			child2 = new Recurse(sVars);
			anchor = company.getAnchor();
			parent.setInstanceName(mine + Utils.getNextString());
			parent.add(anchor);
			child1.setInstanceName(mine + Utils.getNextString());
			child1.add(anchor);
			parent.addChild(child1);
			child2.setInstanceName(mine + Utils.getNextString());
			child2.add(anchor);
			parent.addChild(child2);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// try to swap a parent and a child
		try {
			child2.addChild(parent);
			fail("allowed a parent to be a child of its child");
		} catch (Exception e) {
		}
	}

	@Test
	public void testGetParent() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		Level1 level1 = null;
		Level2 level2 = null;
		try {
			level1 = new Level1(sVars);
			level1.add(anchor);
			level2 = new Level2(sVars);
			level2.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		MyObjects parents = null;
		try {
			parents = level2.getParents(level1);

		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!parents.isEmpty())
			fail("parent of orphan found");

		MyObjects children = null;
		try {
			children = level2.getParents(level1);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		if (!children.isEmpty())
			fail("found child of childless parent");
		// add the link
		try {
			level1.addChild(level2);
			// mlo.add(1);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			parents = level2.getParents(level1);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (parents.isEmpty())
			fail("did not find child of parent");
	}

	@Test
	public void testListChildrenOfParent() {
		Anchor anchor = Utilities.getAnAnchor();

		Level1 level1 = null;
		Level2Brother level2 = null;
		try {
			level1 = new Level1(sVars);
			level1.add(anchor);
			level2 = new Level2Brother(sVars);
			level2.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (level1.listChildren(level2).size() != 0)
				fail("expected size of 0, got:" + level1.listChildren(level2).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (level1.listChildren(level2).size() != 0)
				fail("expected size of 0, got:" + level1.listChildren(level2).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// add the link
		try {
			level1.addChild(level2);
			// mlo.add(17);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (level1.listChildren(level2).size() != 1)
				fail("expected size of 1, got:" + level1.listChildren(level2).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteTest() {
		Anchor anchor = Utilities.getAnAnchor();

		Level1 level1 = null;
		Level2Brother level2 = null;

		try {
			level1 = new Level1(sVars);
			level1.add(anchor);
			level2 = new Level2Brother(sVars);
			level2.add(anchor);
			level1.addChild(level2);
			level1.deleteTest(level2);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteUnconditionally() {
		Anchor anchor = Utilities.getAnAnchor();

		Level1 level1 = null;
		Level2 level2 = null;

		try {
			level1 = new Level1(sVars);
			level2 = new Level2(sVars);
			level1.add(anchor);
			level2.add(anchor);
			level1.addChild(level2);
			level1.deleteLinkUnconditionally(level2);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteLinksUnconditionallyChild() {
		Anchor anchor = Utilities.getAnAnchor();
		Level1 level1 = null;
		Level2Brother level2 = null;
		try {
			level1 = new Level1(sVars);
			level2 = new Level2Brother(sVars);
			level1.add(anchor);
			level2.add(anchor);
			level1.addChild(level2);
			level1.deleteLinkUnconditionally(level2);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Level1 parent, Level1 child, delete parent unconditionally. verify parent
	 * deleted. verify child not deleted.
	 */
	@Test
	public void testDeleteUnconditionallyOrgChart() {
		Anchor anchor = Utilities.getAnAnchor();
		int parentId = -1;
		int childId = -1;
		KeepDescendants parent = null;
		KeepDescendants child = null;

		try {
			parent = new KeepDescendants(sVars);
			child = new KeepDescendants(sVars);
			parent.setInstanceName("parent");
			parent.add(anchor);
			parentId = parent.id;
			child.setInstanceName("child");
			child.add(anchor);
			childId = child.id;
			parent.addChild(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		if (!parent.getDeleteType(child).equals(MyObject.DELETETYPE.KEEPDESCENDANTS))
//			fail("need " + MyObject.DELETETYPE.KEEPDESCENDANTS.toString() + " type of classes");
		MyObjectsArray objs = new MyObjectsArray();
		try {
			objs.add(new KeepDescendants(sVars));
			objs.add(new KeepDescendants(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			parent.deleteUnconditionally();
//			parent.deleteUnconditionally(objs);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// see if parent deleted
		try {
			parent.find(parentId);
			fail("parent not deleted.");
		} catch (Exception e) {
		}

		try {
			child.find(childId);
		} catch (Exception e) {
			fail("child deleted.");
		}
	}

	/**
	 * Orgchart grandparent, Level1 parent, Level1 child, delete parent
	 * unconditionally. verify parent deleted. grandparent and child not deleted.
	 */
	@Test
	public void testDeleteUnconditionallyOrgChart3Generations() {
		Anchor anchor = Utilities.getAnAnchor();

		KeepDescendants grandparent = null;
		try {
			grandparent = new KeepDescendants(sVars);
			grandparent.setInstanceName("grandparent");
			grandparent.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {

		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		int grandparentId = grandparent.id;

		KeepDescendants parent = null;
		try {
			parent = new KeepDescendants(sVars);
			parent.setInstanceName("parent");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			parent.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		int parentId = parent.id;

		KeepDescendants child = null;
		try {
			child = new KeepDescendants(sVars);
			child.setInstanceName("child");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			child.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		int childId = child.id;

		try {
			// new OrgChartLink(grandparent, parent).add(1);
			grandparent.addChild(parent);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			// new OrgChartLink(parent, child).add(1);
			parent.addChild(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyObjectsArray objs = new MyObjectsArray();

		try {
			objs.add(new KeepDescendants(sVars));
			objs.add(new KeepDescendants(sVars));
			parent.deleteUnconditionally();
//			parent.deleteUnconditionally(objs);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// see if everything deleted
		try {
			parent.find(parentId);
			fail("parent not deleted.");
		} catch (Exception e) {
		}

		try {
			child.find(childId);
		} catch (Exception e) {
			fail("child deleted.");
		}

		// see if grandparent still there
		try {
			grandparent.find(grandparentId);
		} catch (Exception e) {
			fail("grandparent deleted");
		}
	}

	@Test
	public void testIsLoaded() {
		Anchor anchor = Utilities.getAnAnchor();

		Level1 level1 = null;
		try {
			level1 = new Level1(sVars);
			level1.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Level2Brother level2 = null;
		try {
			level2 = new Level2Brother(sVars);
			level2.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		MyLinkObject mlo = null;
		;
		try {
			mlo = new MyLinkObject(new Level1(sVars), new Level2(sVars), sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (mlo.isLoaded())
			fail("should not be loaded");
		try {
			mlo = new MyLinkObject(level1, level2, sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!mlo.isLoaded())
			fail("should be loaded");
		try {
			level1.addChild(level2);
			// mlo.add(17);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!mlo.isLoaded())
			fail("should be loaded");
		try {
			mlo.find();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!mlo.isLoaded())
			fail("should be loaded");
	}

	@Test
	public void testOrgChartStackOf5() {
		Anchor anchor = Utilities.getAnAnchor();

		final String mine = "kdkdf";
		Recurse topLevel = null;
		Recurse middleLevel = null;
		Recurse bottomLevel1 = null;
		Recurse bottomLevel2 = null;
		try {
			topLevel = new Recurse(sVars);
			topLevel.setInstanceName(mine + Utils.getNextString());
			topLevel.add(anchor);
			middleLevel = new Recurse(sVars);
			middleLevel.setInstanceName(mine + Utils.getNextString());
			middleLevel.add(anchor);
			bottomLevel1 = new Recurse(sVars);
			bottomLevel1.setInstanceName(mine + Utils.getNextString());
			bottomLevel1.add(anchor);
			bottomLevel2 = new Recurse(sVars);
			bottomLevel2.setInstanceName(mine + Utils.getNextString());
			bottomLevel2.add(anchor);
			topLevel.addChild(middleLevel);

			// build links for 2 children of middle level
			middleLevel.addChild(bottomLevel1);
			middleLevel.addChild(bottomLevel2);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// try to sneak in an object that is already in the list
		try {
			topLevel.addChild(bottomLevel1);
			fail("allowed topLevel1 bottomLevel1");
		} catch (Exception e) {
		}

		// add a child that already exists
		try {
			middleLevel.addChild(bottomLevel1);
			fail("allowed middleLevel bottomLevel1");
		} catch (Exception e) {
		}
	}

	@Test
	public void testIdentical() {
		Anchor anchor = Utilities.getAnAnchor();

		Level1 parent = null;
		try {
			parent = new Level1(sVars);
			parent.setInstanceName("parent");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			parent.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			// new OrgChartLink(parent, parent).add(1);
			parent.addChild(parent);
			fail("allowed duplicates to be in a link");
		} catch (Exception e) {
		}
	}

	public MyObject getLevel1() {
		Anchor anchor = Utilities.getAnAnchor();

		Level1 ret = null;
		try {
			ret = new Level1(sVars);
			ret.setInstanceName("Level1 " + Utils.getNextString());
			ret.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		return ret;
	}

	// get all the parents of a child, parentId in child
	// since a child with the parentId stored in the child can only have 1
	// parent, expect getting back the one parent.
	@Test
	public void testListParentsOfChildInternal() {
		Anchor anchor = Utilities.getAnAnchor();
		// create some potential parents
		Utilities.getLevel1(anchor);
		// MyObject parent = Utilities.getLevel1(anchor);
		// create the parent
		MyObject l1 = Utilities.getLevel1(anchor);
		// another potential parent
		Utilities.getLevel1(anchor);
		// the child
		MyObject l2 = Utilities.getLevel2ParentInChild(anchor);
		MyObjects objs = null;
		try {
			// create the link between the parent and child
			new MyLinkObject(l1, l2, sVars).add();
			// get the list
			objs = l2.getParents(new Level1(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (objs.size() != 1)
			fail("expected a size of 1, got size of " + objs.size());
		if (objs.iterator().next().id != l1.id)
			fail("did not get back the correct parent.");
	}

	// get all the parents of a child with external links
	@Test
	public void testListParentsOfChildExternal() {
		Anchor anchor = Utilities.getAnAnchor();
		// create some potential parents
		Utilities.getLevel1(anchor);
		// MyObject parent = Utilities.getLevel1(anchor);

		// create the parent
		MyObject l1First = Utilities.getLevel1(anchor);
		// another potential parent
		MyObject l1Second = Utilities.getLevel1(anchor);
		// the child
		MyObject l2 = Utilities.getLevel2External(anchor);
		MyObjects objs = null;
		try {
			// create the link between two parents and the child
			new MyLinkObject(l1First, l2, sVars).add();
			new MyLinkObject(l1Second, l2, sVars).add();
			// get the list
			objs = l2.getParents(new Level1(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (objs.size() != 2)
			fail("expected a size of 2, got size of " + objs.size());
	}

	@Test
	public void testChildInParentAdd() {
		Anchor anchor = Utilities.getAnAnchor();
		Level2ChildIdInParent levelTwoCinP = null;
		try {
			levelTwoCinP = new Level2ChildIdInParent(sVars);
			levelTwoCinP.setInstanceName("child id in parent");
			// self anchoring
			levelTwoCinP.add(anchor);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Level3 levelThree = null;
		try {
			levelThree = new Level3(sVars);
			levelThree.setInstanceName("child id in parent");
			levelThree.add(anchor);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		try {
			levelTwoCinP.addChild(levelThree);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// get parent of levelThree
		// TODO could have multiple parents
		MyObject parent = null;
		try {
			parent = levelThree.getParents(new Level2ChildIdInParent(sVars)).iterator().next();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (parent.id != levelTwoCinP.id)
			fail("wrong parent");
	}

	@Test
	public void testListChildrenOfParentRecursive() throws Exception {
		Recurse first = null;
		Recurse second = null;
		Recurse third = null;
		try {
			first = new Recurse(sVars);
			second = new Recurse(sVars);
			third = new Recurse(sVars);
			first.add(new Anchor(sVars));
			second.add(first);
			third.add(first);
			first.addChild(second);
			first.addChild(third);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyObjectsArray stack = new MyObjectsArray();
		stack.add(first);
		MyObjects kids = null;
		MyLinkObject mlo = null;
		try {
			mlo = new MyLinkObject(first, first, sVars);
			kids = mlo.listChildrenOfParent();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (kids.size() != 2)
			fail("expected first to have 2 kids, got " + kids.size());
		if (!kids.containsMyObject(second))
			fail("second not in kids");
		if (!kids.containsMyObject(third))
			fail("third not in kids");
	}

//	// create a parent child pair, delete the child, and verify the parent child
//	// link is also deleted
//	@Test
//	public void testDeleteChildCheckLinkUsingTables() {
//		Level1 levelOne = null;
//		Level2 levelTwo = null;
//		Collection<String> linkTables = null;
//		try {
//			levelOne = new Level1(sVars);
//			levelTwo = new Level2(sVars);
//			levelOne.add(new Anchor(sVars));
//			levelTwo.add(levelOne);
//			linkTables = MyLinkObject.listLinkTables();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (!linkTables.isEmpty())
//			fail("should be 0 tables of links, found " + linkTables.size());
//
//		// create a parent child pair
//		try {
//			levelOne.addChild(levelTwo);
//			linkTables = MyLinkObject.listLinkTables();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (linkTables.size() != 1)
//			fail("expected a size of 1, got " + linkTables.size());
//		if (!linkTables.contains(
//				(levelOne.getMyFileName() + MyLinkObject.CHILDSTRING + levelTwo.getMyFileName()).toLowerCase()))
//			fail("did not find "
//					+ (levelOne.getMyFileName() + MyLinkObject.CHILDSTRING + levelTwo.getMyFileName()).toLowerCase());
//
//	}

	// create an instance in two separate stacks, delete the instance in one
	// stack, see if the other stack is damaged
	@Test
	public void testDeleteChildCheckLink() throws ExceptionCoding {
		Level1 levelOneStackOne = null;
		Level1 levelOneStackTwo = null;
		Level2 levelTwoBoth = null;
		Level3 levelThreeStackOne = null;
		Level3 levelThreeStackTwo = null;
		try {
			levelOneStackOne = new Level1(sVars);
			levelOneStackTwo = new Level1(sVars);
			levelTwoBoth = new Level2(sVars);
			levelThreeStackOne = new Level3(sVars);
			levelThreeStackTwo = new Level3(sVars);

			levelOneStackOne.add(new Anchor(sVars));
			levelOneStackTwo.add(new Anchor(sVars));
			levelTwoBoth.add(levelOneStackOne);
			levelThreeStackOne.add(levelTwoBoth);
			levelThreeStackTwo.add(levelTwoBoth);

			// stack one
			levelOneStackOne.addChild(levelTwoBoth);
			levelTwoBoth.addChild(levelThreeStackOne);

			// stack two with common levelTwoBoth
			levelOneStackTwo.addChild(levelTwoBoth);
			levelTwoBoth.addChild(levelThreeStackTwo);

		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyLinkObject mloOneTwoStackOne = null;
		MyLinkObject mloTwoThreeStackOne = null;
		MyLinkObject mloOneTwoStackTwo = null;
		MyLinkObject mloTwoThreeStackTwo = null;
		try {
			mloOneTwoStackOne = new MyLinkObject(levelOneStackOne, levelTwoBoth, sVars);
			mloTwoThreeStackOne = new MyLinkObject(levelTwoBoth, levelThreeStackOne, sVars);
			mloOneTwoStackTwo = new MyLinkObject(levelOneStackTwo, levelTwoBoth, sVars);
			mloTwoThreeStackTwo = new MyLinkObject(levelTwoBoth, levelThreeStackTwo, sVars);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (!mloOneTwoStackOne.linkExists())
			fail("could not create parent child link");
		if (!mloTwoThreeStackOne.linkExists())
			fail("could not create parent child link");
		if (!mloOneTwoStackTwo.linkExists())
			fail("could not create parent child link");
		if (!mloTwoThreeStackTwo.linkExists())
			fail("could not create parent child link");
//		MyObjectsArray objs = new MyObjectsArray();
//		objs.add(levelOneStackOne);
//		objs.add(levelTwoBoth);
//		objs.add(levelThreeStackOne);
		try {
			levelTwoBoth.deleteUnconditionally();
//			levelTwoBoth.deleteUnconditionally(objs);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (mloOneTwoStackTwo.linkExists())
			fail("link should have been deleted");
		if (mloTwoThreeStackTwo.linkExists())
			fail("link should have been deleted");

	}

//	@Test
//	public void testListLinkTables() {
//		Collection<String> results = null;
//		try {
//			results = MyLinkObject.listLinkTables();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (!results.isEmpty())
//			fail("expected empty, got " + results.size());
//
//		Level1 levelOne = null;
//		Level2 levelTwo = null;
//		try {
//			levelOne = new Level1(sVars);
//			levelTwo = new Level2(sVars);
//			levelOne.add(new Anchor(sVars));
//			levelTwo.add(levelOne);
//			levelOne.addChild(levelTwo);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		try {
//			results = MyLinkObject.listLinkTables();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (results.size() != 1)
//			fail("expected a size of 1, got " + results.size());
//
//		try {
//			if (!results.contains(new MyLinkObject(levelOne, levelTwo).getMyFileName()))
//				fail("did not find " + new MyLinkObject(levelOne, levelTwo).getMyFileName());
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//	}

//	@Test
//	public void testListMyChildrensSimpleClassName() {
//		Level1 levelOne = null;
//		Level2 levelTwo = null;
//		try {
//			levelOne = new Level1(sVars);
//			levelTwo = new Level2(sVars);
//			// creates a level1 to level1 orphan table
//			levelOne.add(new Anchor(sVars));
//			levelTwo.add(levelOne);
//			levelOne.addChild(levelTwo);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		Collection<String> results = null;
//		try {
//			results = MyLinkObject.listMyChildrensSimpleClassName(levelOne);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (results.size() != 1)
//			fail("expected a size of 1, got " + results.size());
//		if (!results.contains(levelTwo.getMyFileName().toLowerCase()))
//			try {
//				fail("could not find " + new MyLinkObject(levelOne, levelTwo).getMyFileName());
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
//
//		// verify levelTwo is not listed as a parent
//		try {
//			results = MyLinkObject.listMyChildrensSimpleClassName(levelTwo);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (!results.isEmpty())
//			fail("expected empty, got " + results.size());
//	}

//	@Test
//	public void testListMyParentsSimpleClassName() {
//		Level1 levelOne = null;
//		Level2 levelTwo = null;
//		try {
//			levelOne = new Level1(sVars);
//			levelTwo = new Level2(sVars);
//			// creates a level1_orphan_level1 table
//			levelOne.add(new Anchor(sVars));
//			levelTwo.add(levelOne);
//			levelOne.addChild(levelTwo);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		Collection<String> results = null;
//		try {
//			results = MyLinkObject.listMyParentsSimpleClassName(levelTwo);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (results.size() != 1)
//			fail("expected a size of 1, got " + results.size());
//		if (!results.contains(levelOne.getMyFileName().toLowerCase()))
//			fail("could not find " + levelOne.getMyFileName().toLowerCase());
//
//		// verify levelOne is not listed as a child
//		try {
//			results = MyLinkObject.listMyParentsSimpleClassName(levelOne);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (!results.isEmpty())
//			fail("expected empty, got " + results.size());
//	}

	// create a parent child relationship
	// delete the parent and see if the link is deleted
	@Test
	public void testMakeMeGoAwayParent() {
		Connection conn = null;
		MyStatement ms = null;
		ResultSet rs = null;
		ResultSet nextRs = null;
		Parent parent = null;
		Child child = null;
		boolean foundOne = false;
		try {
			conn = sVars.connection.getConnection();
			ms = new MyStatement(conn);
			parent = new Parent(sVars);
			child = new Child(sVars);
			parent.add(new Anchor(sVars));
			child.add(parent);
			parent.addChild(child);

			rs = ms.executeQuery("select * from " + new MyLinkObject(parent, child, sVars).getMyFileName());
			rs.next();
			if (rs.getInt("childId") != child.id)
				fail("expected levelTwo.id of " + child.id + ", but got " + rs.getInt("childId"));
			if (rs.getInt("parentId") != parent.id)
				fail("expected levelOne.id of " + parent.id + ", but got " + rs.getInt("parentId"));
			if (!rs.isLast())
				fail("got more than 1 record.");
			child.deleteUnconditionally();
			nextRs = ms.executeQuery("select * from " + new MyLinkObject(parent, child, sVars).getMyFileName());
			if (nextRs.next())
				foundOne = true;
		} catch (SQLException e) {
			fail(e.getLocalizedMessage());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (nextRs != null)
				try {
					nextRs.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (ms != null)
				try {
					ms.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
		}
		if (foundOne)
			fail("should not have found one");
	}

}
