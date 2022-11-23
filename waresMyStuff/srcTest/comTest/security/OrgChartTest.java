package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.Utils;
import com.security.Company;
import com.security.MyLinkObject;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.MyObjectsArray;

import comTest.utilities.Utilities;

public class OrgChartTest {
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
	public void testOrgChartLink() {
		try {
			getInstance1().addChild(getInstance1());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testGetNewChild() {
		MyLinkObject mlo = null;
		try {
			mlo = new MyLinkObject(getInstance1(), getInstance1(), sVars);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		MyObject child = null;
		try {
			child = mlo.getNewChild();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (!(child instanceof Recurse))
			fail("wrong child");
	}

	@Test
	public void testLinkExists() {
		Recurse level1 = getInstance1();
		Recurse level2 = getInstance1();
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
		try {
			if (!level1.linkToChildExists(level2))
				fail("link should not exist");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	// pair of the same class, one made parent, one made child, try to add twice
	public void testAddOrgChartNoParentsNoKidsDoubleAdd() {
		Recurse level1 = getInstance1();
		Recurse level2 = getInstance1();
		// OrgChartLink mlo = null;
		try {
			// mlo = new OrgChartLink(level1, level2);
			level1.addChild(level2);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		try {
			level1.addChild(level2);
			fail("should not allow duplicate link");
		} catch (Exception e1) {
			// fail(e1.getLocalizedMessage());
		}
	}

	@Test
	// moe is a parent of larry is a parent of curly, all stooges
	public void testAddOrgChart3Levels() {
		Recurse moe = getInstance1();
		Recurse larry = getInstance1();
		// OrgChartLink mlo = null;
		try {
			// mlo = new OrgChartLink(moe, larry);
			moe.addChild(larry);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		Recurse curly = getInstance1();
		try {
			// mlo = new OrgChartLink(larry, curly);
			larry.addChild(curly);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
	}

	@Test
	// moe is a parent of larry is a parent of curly, all stooges. try adding
	// curly as a parent of larry
	public void testAddOrgChart3LevelsAddChildAsParent() {
		Recurse moe = getInstance1();
		Recurse larry = getInstance1();
		Recurse curly = getInstance1();
		try {
			moe.addChild(larry);
			larry.addChild(curly);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// try to swap parent/child
		try {
			larry.addChild(moe);
			fail("should not have allowed larry to be a parent of moe");
		} catch (Exception e1) {
			// fail(e1.getLocalizedMessage());
		}

		// try to swap parent/child
		try {
			// mlo = new OrgChartLink(curly, larry);
			curly.addChild(larry);
			fail("should not have allowed curly to be a parent of larry");
		} catch (Exception e1) {
			// fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		//
		// } catch (Exception e) {
		// }
		// try to swap parent/child
		try {
			// mlo = new OrgChartLink(curly, moe);
			curly.addChild(moe);
			fail("should not have allowed curly to be a parent of moe");
		} catch (Exception e1) {
			// fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		//
		// } catch (Exception e) {
		// }
	}

	@Test
	public void testAddOrgChart1Kid2Parents() {
		// child with 2 parents
		Recurse parent1 = getInstance1();
		Recurse child = getInstance1();
		Recurse parent2 = getInstance1();

		// OrgChartLink mlo = null;
		try {
			// mlo = new OrgChartLink(parent1, child);
			parent1.addChild(child);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		// add second parent to child
		try {
			// mlo = new OrgChartLink(parent2, child);
			parent2.addChild(child);
			fail("allowed a child with 2 parents");
		} catch (Exception e1) {
			// fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		//
		// } catch (Exception e) {
		// // fail(e.getLocalizedMessage());
		// }
	}

	@Test
	public void testAddOrgChart1Parent2Kids() {
		// parent with 2 kids, then try swapping a child with the parent
		Recurse parent = getInstance1();
		Recurse child1 = getInstance1();
		// OrgChartLink mlo = null;
		try {
			// mlo = new OrgChartLink(parent, child1);
			parent.addChild(child1);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		Recurse child2 = getInstance1();
		// add second parent to child
		try {
			// mlo = new OrgChartLink(parent, child2);
			parent.addChild(child2);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }

		// try to swap a parent and a child
		try {
			// mlo = new OrgChartLink(child2, parent);
			child2.addChild(parent);
			fail("allowed a parent to be a child of its child");
		} catch (Exception e1) {
			// fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		//
		// } catch (Exception e) {
		// }
	}

	public Recurse getInstance1() {
		Company company = Utilities.getACompany();
		Recurse ret = null;
		try {
			ret = new Recurse(sVars);
			ret.setInstanceName("one name " + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			ret.add(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		return ret;
	}

	Level2 getInstance2() {
		Company company = Utilities.getACompany();
		Level2 ret = null;
		try {
			ret = new Level2(sVars);
			ret.setInstanceName("one name " + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			ret.add(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		return ret;
	}

	@Test
	public void testListParentsOfChild() {
		Recurse parent = getInstance1();
		Recurse child = getInstance1();
		MyObjects parents = null;
		try {
			parents = child.getParents(parent);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!parents.isEmpty())
			fail("found parents of an orphan");

		MyObjects kids = null;
		try {
			kids = parent.getChildren(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!kids.isEmpty())
			fail("found child of a childless parent");

		// add the link
		try {
			// mlo.add(1);
			parent.addChild(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyObject foundParent = null;
		try {
			foundParent = child.getSingleParent(parent);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (foundParent.id != parent.id)
			fail("found wrong parent");
	}

	@Test
	public void testListChildrenOfParent() {
		MyObject parent = getInstance1();
		MyObject child = getInstance1();
		try {
			if (parent.getChildren(child).size() != 0)
				fail("expected size of 0, got:" + parent.getChildren(child).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (child.getParents(child).size() != 0)
				fail("expected size of 0, got:" + child.getParents(child).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// add the link
		try {
			// mlo.add(1);
			parent.addChild(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (parent.getChildren(child).size() != 1)
				fail("expected size of 1, got:" + parent.getChildren(child).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteTest() {
		Recurse level1 = getInstance1();
		Recurse level2 = getInstance1();
		// OrgChartLink mlo = null;
		try {
			// mlo = new OrgChartLink(level1, level2);
			level1.addChild(level2);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		try {
			level1.deleteTest(level2);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteUnconditionally() {
		Recurse level1 = getInstance1();
		Recurse level2 = getInstance1();
		// OrgChartLink mlo = null;
		try {
			// mlo = new OrgChartLink(level1, level2);
			level1.addChild(level2);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		try {
			level1.deleteLinkUnconditionally(level2);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteLinksUnconditionallyChild() {
		MyObject parent = getInstance1();
		MyObject child = getInstance1();
		MyObjectsArray moa = new MyObjectsArray();
		moa.add(parent);
		moa.add(child);

		// OrgChartLink mlo = null;
		try {
			// mlo = new OrgChartLink(level1, level2);
			parent.addChild(child);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		try {
			parent.deleteLinkUnconditionally(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Orgchart grandparent, Recurse parent, Recurse child, delete parent
	 * unconditionally. verify parent deleted. grandparent and child not deleted.
	 */
	@Test
	public void testDeleteUnconditionallyOrgChart3Generations() {
		Company company = Utilities.getACompany();
		KeepDescendants grandparent = null;
		KeepDescendants parent = null;
		KeepDescendants child = null;
		int grandparentId = -1;
		int parentId = -1;
		int childId = -1;
		try {
			grandparent = new KeepDescendants(sVars);
			parent = new KeepDescendants(sVars);
			child = new KeepDescendants(sVars);
			grandparent.setInstanceName("grandparent");
			grandparent.add(company.getAnchor());
			grandparentId = grandparent.id;
			parent.setInstanceName("parent");
			parent.add(company.getAnchor());
			parentId = parent.id;
			child.setInstanceName("child");
			child.add(company.getAnchor());
			childId = child.id;
			grandparent.addChild(parent);
			parent.addChild(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		MyObjectsArray objs = new MyObjectsArray();
		KeepDescendants keepDescendants = null;
		try {
			keepDescendants = new KeepDescendants(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		objs.add(keepDescendants);
		objs.add(keepDescendants);
		try {
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
	public void testOrgChartStackOf5() {
		Recurse topRecurse = getInstance1();
		Recurse middleLevel = getInstance1();
		Recurse bottomOrgChartExtensionParent = getInstance1();
		Recurse bottomRecurse = getInstance1();

		// OrgChartLink mlo = null;
		try {
			// mlo = new OrgChartLink(topRecurse, middleLevel);
			topRecurse.addChild(middleLevel);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }

		// build links for 2 children of middle level
		try {
			// mlo = new OrgChartLink(middleLevel,
			// bottomOrgChartExtensionParent);
			middleLevel.addChild(bottomOrgChartExtensionParent);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		try {
			// mlo = new OrgChartLink(middleLevel, bottomRecurse);
			middleLevel.addChild(bottomRecurse);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }

		// try to sneak in an object that is already in the list
		try {
			// mlo = new OrgChartLink(topRecurse,
			// bottomOrgChartExtensionParent);
			topRecurse.addChild(bottomOrgChartExtensionParent);
			fail("allowed topOrgChartExtensionParent bottomOrgChartExtensionParent");
		} catch (Exception e1) {

		}
		// try {
		// mlo.add(1);
		//
		// } catch (Exception e) {
		// }
		try {
			// mlo = new OrgChartLink(middleLevel,
			// bottomOrgChartExtensionParent);
			middleLevel.addChild(bottomOrgChartExtensionParent);
			fail("allowed middleLevel bottom OrgChartExtensionParent");
		} catch (Exception e1) {
			// fail(e1.getLocalizedMessage());
		}
		// try {
		// mlo.add(1);
		//
		// } catch (Exception e) {
		// }
	}

	/**
	 * create a parent>child relationship, the try to swap positions
	 */
	@Test
	public void testSwapParentChild() {
		Company company = Utilities.getACompany();
		Recurse child = getInstance1();
		try {
			child.setInstanceName("child");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			child.add(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Recurse parent = getInstance1();
		try {
			parent.setInstanceName("parent");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			parent.add(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			parent.addChild(child);
			// new OrgChartLink(parent, child).add(1);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// now try to swap places
		try {
			child.addChild(parent);
			// new OrgChartLink(child, parent).add(1);
			fail("should not allow swap");
		} catch (Exception e) {
		}
	}

	@Test
	public void testIdentical() {
		Company company = Utilities.getACompany();
		Recurse parent = getInstance1();
		try {
			parent.setInstanceName("parent");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			parent.add(company.getAnchor());
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

	// @Test
	// public void testDifferentClasses() {
	// try {
	// new OrgChartLink(getInstance1(), getInstance2());
	// fail("allowed different classes");
	// } catch (Exception e) {
	// }
	// }

	// org chart on top, non-org chart on the bottom, delete the lowest org
	// chart, verify non-org chart pointer updated to 0
	@Test
	public void testBoundary() {
		Company company = Utilities.getACompany();
		Recurse parent = getInstance1();
		Recurse child = getInstance1();
		// OrgChartLink ocl = null;
		try {
			// ocl = new OrgChartLink(parent, child);
			parent.addChild(child);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// try {
		// ocl.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		Recurse l1 = null;

		try {
			l1 = new Recurse(sVars);
			l1.newTable(sVars);
			l1.setInstanceName("test boundary");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			l1.add(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// OrgChartInternals
		// .addInternal(child,l1,
		// OrgChartInternals.VALIDQUANTITY.MUSTBEPOSITIVE,
		// "org chart to level1",
		// OrgChartInternals.LINKTYPE.PARENTIDINCHILD);
		// MyLinkObject mlo = new MyLinkObject(child, l1);
		// try {
		// mlo.add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		// MyObjectsArray objs = new MyObjectsArray();
		// objs.add(parent);
		// objs.add(l1);
		// try {
		// child.deleteUnconditionally(objs);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		// try {
		// l1.find(l1.id);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		// if (l1.parentId != 0)
		// fail("delete unconditionally did not clear parent id in child");
	}
}
