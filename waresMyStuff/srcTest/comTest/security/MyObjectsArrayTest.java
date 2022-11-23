package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.IdAndString;
import com.forms.IdAndStrings;
import com.forms.SearchTarget;
import com.forms.SearchTargets;
import com.security.Anchor;
import com.security.Company;
import com.security.MyLinkObject;
import com.security.MyObject;

import comTest.utilities.Utilities;

public class MyObjectsArrayTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

//	// get parent, basic operation
//	@Test
//	public void testGetParent() {
//		Anchor anchor = Utilities.getAnAnchor();
//		FormsMatrixDynamic fmd = new FormsMatrixDynamic(sVars);
//		Level1 l1 = null;
//		Level2 l2 = null;
//		Level3 l3 = null;
//		try {
//			l1 = new Level1(sVars);
//			l2 = new Level2(sVars);
//			l3 = new Level3(sVars);
//			l1.add(anchor);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		try {
//			l2.add(anchor);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		try {
//			l1.addChild(l2);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		try {
//			l3.add(anchor);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		SearchTargets objs = new SearchTargets(sVars);
//		// find l2's parent
//		MyObjects l2Parents = null;
//		try {
//			objs.add(l1, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//			objs.add(l2, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//			objs.add(l3, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//fmd.add(objs);
//			l2Parents = objs.getParents(1);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (l2Parents.size() != 1)
//			fail("multiple parents");
//		MyObject l2Parent = l2Parents.iterator().next();
//		try {
//			if (!l2Parent.getMyFileName().equals(l1.getMyFileName()))
//				fail("expected l2Parent.getFileName of " + l1.getMyFileName() + ", got " + l2Parent.id);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (l2Parent.id != l1.id)
//			try {
//				fail("expected l2Parent.id of " + l1.id + ", got " + l2Parent.getMyFileName());
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
//	}

	// create a family tree. select the lowest member. see if the correct family
	// member shows up at the desired level
	@Test
	public void testGetFilteredListLowestSelected() {
		// family tree from level1 to level3, select level3, see if level1
		// returned
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		FormsMatrixDynamic fmd = null;
		try {
			fmd = new FormsMatrixDynamic(sVars);
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Level1 levelOne = null;
		Level2 levelTwo = null;
		Level3 levelThree = null;
		try {
			levelOne = new Level1(sVars);
			levelTwo = new Level2(sVars);
			levelThree = new Level3(sVars);
			levelOne.add(anchor);
			levelTwo.add(anchor);
			levelThree.add(anchor);
			company.addChild(levelOne);
			levelOne.addChild(levelTwo);
			levelTwo.addChild(levelThree);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		SearchTargets objArr = new SearchTargets(sVars);

		try {
			objArr.add(company, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objArr.add(new Level1(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objArr.add(new Level2(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objArr.add(levelThree, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		fmd.add(objArr);
		fmd.row = 0;
		fmd.column = 1;
//		SearchTarget top = objArr.getFilteredList(0);
		IdAndStrings idAndStrings = null;
		try {
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.ANCESTORS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (idAndStrings.size() != 1)
			fail("targets not size of 1");
		if (!idAndStrings.contains(levelOne))
			fail("targets does not contain levelOne");

	}

	// create a family tree. select the youngest descendant. ask for its ancestors at
	// different levels
	@Test
	public void testGetFilteredAncestors() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		FormsMatrixDynamic fmd =null;
		try {fmd=new FormsMatrixDynamic(sVars);
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// family tree from level1 to level3, select level1, see if level3
		// returned
		Level1 levelOne = null;
		Level2 levelTwo = null;
		Level3 levelThree = null;
		// l1.isAnchor = true;
		try {
			levelOne = new Level1(sVars);
			levelTwo = new Level2(sVars);
			levelThree = new Level3(sVars);
			levelOne.add(anchor);
			company.addChild(levelOne);
			levelTwo.add(anchor);
			levelOne.addChild(levelTwo);
			levelThree.add(anchor);
			levelTwo.addChild(levelThree);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		SearchTargets objArr = new SearchTargets(sVars);

		try {
			objArr.add(company, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objArr.add(new Level1(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			// unselected level2
			objArr.add(new Level2(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			// selected level3
			objArr.add(levelThree, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		fmd.add(objArr);
		fmd.row = 0;
		// looking for the ancestors of level3 at the level2 level
		fmd.column = 2;

//		SearchTarget second = objArr.getFilteredList(1);
		IdAndStrings idAndStrings = null;
		try {
//			idAndStrings = new IdAndStrings(objArr, 1, SearchTarget.SEARCHTYPES.DESCENDANTS).doQuery();
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.ANCESTORS, sVars);
			
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			for (StackTraceElement sts : e.getStackTrace())
				System.out.println(sts);
			fail(e.getLocalizedMessage());
		}
		// if (second.selectedObject != null)
		// fail("second selectedObject not null");
		if (idAndStrings == null)
			fail("targets null in second");
		if (!idAndStrings.contains(levelTwo))
			fail("targets does not contain " + levelTwo.getInstanceName());

		// go up one level
		fmd.column = 1;
		// fail("third selectedObject not null");
		try {
//			idAndStrings = new IdAndStrings(objArr, 2, SearchTarget.SEARCHTYPES.DESCENDANTS).doQuery();
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.ANCESTORS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;			
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			for (StackTraceElement sts : e.getStackTrace())
				System.out.println(sts);
			fail(e.getLocalizedMessage());
		}
//		if (idAndStrings == null)
//			fail("targets null in third");
		if (!idAndStrings.contains(levelOne))
			fail("targets does not contain " + levelOne.getInstanceName());

//		fmd.column = 0;
//		try {
////			idAndStrings = new IdAndStrings(objArr, 3, SearchTarget.SEARCHTYPES.DESCENDANTS).doQuery();
//			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.ANCESTORS).doQuery();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (idAndStrings == null)
//			fail("targets null in fourth");
//		if (!idAndStrings.contains(company))
//			fail("targets does not contain " + company.getInstanceName());
	}

	// get an empty list when the top object is loaded but has no children
	// object
	@Test
	public void testGetFilteredListTopLoadedCurrentLoaded() {
		Company company = Utilities.getACompany();
		FormsMatrixDynamic fmd =null; 
		// Anchor anchor = null;
		// Level1 levelOne = new Level1(sVars);
		// Level2 levelTwo = new Level2(sVars);

		// try {
		// anchor = company.getAnchor();
		// levelOne.add(anchor);
		// levelTwo.add(anchor);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }

		SearchTargets objs = new SearchTargets(sVars);

		try {
			fmd=new FormsMatrixDynamic(sVars);
			objs.add(company, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objs.add(new Level1(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objs.add(new Level2(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			// create a table
			new MyLinkObject(new Level1(sVars), new Level2(sVars), sVars);
			new MyLinkObject(company, new Level1(sVars), sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		fmd.add(objs);
		fmd.row = 0;
		fmd.column = 2;
		IdAndStrings idAndStrings = null;
		try {
//			idAndStrings = new IdAndStrings(objs, 1, SearchTarget.SEARCHTYPES.DESCENDANTS).doQuery();
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.DESCENDANTS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// if (second.selectedObject != null)
		// fail("second selectedObject set");
		if (!idAndStrings.isEmpty())
			fail("second targets not empty");
//		SearchTarget third = objs.getFilteredList(1);
		try {
//			idAndStrings = new IdAndStrings(objs, 1, SearchTarget.SEARCHTYPES.DESCENDANTS).doQuery();
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.DESCENDANTS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// if (third.selectedObject != null)
		// fail("third selectedObject set");
		if (!idAndStrings.isEmpty())
			fail("third targets not empty");
	}

	// create a family tree with the top selected. Put in 4 objects
	// that use the top object as an anchor. Put in 4 objects
	// that do NOT use the top as an anchor.
	// ask for a filtered list and see if the 4 anchored objects
	// are returned.
	@Test
	public void testGetFilteredListAnchors() {
		FormsMatrixDynamic fmd = null;
		Level1 levelOneIsAnchor = null;
		Level1 levelOneIsNotAnchor = null;
		try {fmd=new FormsMatrixDynamic(sVars);
			levelOneIsAnchor = new Level1(sVars);
			levelOneIsNotAnchor = new Level1(sVars);
			levelOneIsAnchor.add(new Anchor(sVars));
			levelOneIsNotAnchor.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// 4 level2s that are anchored to l1Anchor
		for (int i = 0; i < 4; i++)
			try {
				Level2 levelTwoWithAnchor = new Level2(sVars);
				levelTwoWithAnchor.add(levelOneIsAnchor.getAnchor());
				levelOneIsAnchor.addChild(levelTwoWithAnchor);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
		// 4 level2s that are not anchored to l1Anchor
		for (int i = 0; i < 4; i++)
			try {
				Level2 levelTwoNotAnchor = new Level2(sVars);
				levelTwoNotAnchor.add(levelOneIsNotAnchor.getAnchor());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}

		SearchTargets objs = new SearchTargets(sVars);

		try {
			objs.add(levelOneIsAnchor, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objs.add(new Level2(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			fmd.add(objs);
			fmd.row = 0;
			fmd.column = 1;
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		objs.anchorObject = levelOneIsAnchor;
//		objs.setListInterface(new FilteredList());
//		try {
//			// TODO reinstate?
//			// objs.updateFilteredList();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}

		// SearchTarget first = objs.getFilteredList(0);
		// if (first.selectedObject == null)
		// fail("first not selected");
		// if (!first.selectedObject.equals(levelOneIsAnchor))
		// fail("first not levelOneAnchor");
//		SearchTarget second = objs.getFilteredList(1);
		IdAndStrings idAndStrings = null;
		try {
//			idAndStrings = new IdAndStrings(objs, 1, SearchTarget.SEARCHTYPES.DESCENDANTS).doQuery();
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.DESCENDANTS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (idAndStrings == null)
			fail("no targets");
		if (idAndStrings.size() != 4)
			fail("expected 4 targets, got " + idAndStrings.size());
	}

	// create a family tree. select the highest element in the tree. see if the
	// correct
	// family shows up at the desired level
	@Test
	public void testGetFilteredListHighestSelectedUseClass() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		FormsMatrixDynamic fmd =null; 
		// family tree from level1 to level3, select level1, see if level3
		// returned
		Level1 levelOne = null;
		Level2 levelTwo = null;
		Level3 levelThree = null;
		try {fmd=new FormsMatrixDynamic(sVars);
			levelOne = new Level1(sVars);
			levelTwo = new Level2(sVars);
			levelThree = new Level3(sVars);
			anchor = company.getAnchor();
			levelOne.add(anchor);
			levelTwo.add(anchor);
			levelThree.add(anchor);
			company.addChild(levelOne);
			levelOne.addChild(levelTwo);
			levelTwo.addChild(levelThree);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		SearchTargets objArr = new SearchTargets(sVars);

		try {
			objArr.add(company);

			// unselected level1
			objArr.add(new Level1(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			// unselected level2
			objArr.add(new Level2(sVars));
			// unselected level3
			objArr.add(new Level3(sVars));
			fmd.add(objArr);
			fmd.row = 0;
			fmd.column = 3;
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
////		objArr.anchorObject = company;
////		objArr.setListInterface(new FilteredList());
//		try {
//			// TODO reinstate?
//			// objArr.updateFilteredList();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}

		// MyObjects objs = null;
//		IdAndString obj = null;
		// first item on the list is selected and should be found
		// SearchTarget first = objArr.getFilteredList(0);
		// if (first.selectedObject == null)
		// fail("top.selectedObject is null");
		// if (!first.selectedObject.equals(company))
		// fail("top selectedObject is not anchor");

//		SearchTarget second = objArr.getFilteredList(1);
		// if (second.selectedObject != null)
		// fail("second selectedObject not null");

		IdAndStrings idAndStrings = null;
		try {
//			idAndStrings = new IdAndStrings(objArr, 1, SearchTarget.SEARCHTYPES.DESCENDANTS).doQuery();
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.DESCENDANTS, sVars);
			
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (idAndStrings.isEmpty())
			fail("targets empty in second");
		// if (!(second.useClass instanceof Level1))
		// fail("useClass is not an instance of level1");
		// if (!second.useAnchor.equals(anchor))
		// fail("does not have correct anchor");
		// objs = second.targets;
		if (idAndStrings.size() != 1)
			fail("expected size of 1, got " + idAndStrings.size());
//		obj = second.descendantTargets.iterator().next();
//		if (!IdAndString.toIdAndString(levelOne).equals(obj))
		if (!IdAndString.toIdAndString(levelThree).equals(idAndStrings.get(0)))
			// (obj instanceof Level1))
			fail("not an instance of level3");
	}

	boolean isTheSame(MyObject o, IdAndString idAndString) {
		if (idAndString.string == null || idAndString.string.isEmpty())
			fail("empty name");
		return idAndString.id == o.id && idAndString.string.equals(o.getInstanceName());
	}

	@Test
	public void testOrphan() {
		FormsMatrixDynamic fmd =null; 
		// create an orphan. see if it shows up in orphans collection
		Level1 levelOne = null;
		Level2 orphan = null;
		try {
			fmd=new FormsMatrixDynamic(sVars);
			levelOne = new Level1(sVars);
			orphan = new Level2(sVars);
			levelOne.add(new Anchor(sVars));
			orphan.add(levelOne.getAnchor());
			new MyLinkObject(levelOne, orphan, sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		SearchTargets objArr = new SearchTargets(sVars);

		try {
			objArr.add(levelOne); // selected
			objArr.add(new Level2(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		fmd.add(objArr);
		fmd.row = 0;
		fmd.column = 1;
		// loaded layers should have 0 descendants
//		SearchTarget first = objArr.get(0);
		IdAndStrings idAndStrings = null;
		try {
//			idAndStrings = new IdAndStrings(objArr, 0, SearchTarget.SEARCHTYPES.ORPHANS).doQuery();
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.ORPHANS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (idAndStrings.size() != 1)
			fail("expected 1 orphan, got " + idAndStrings.size());
		if (!idAndStrings.contains(orphan))
			fail("did not find orphan in allTargets collection.");
	}

	@Test
	public void testaddOrphans() {
		FormsMatrixDynamic fmd =null; 
		// create an orphan. see if it shows up in allTargets collection
		Level1 levelOne = null;
		Level2 orphan = null;
		try {
			fmd = new FormsMatrixDynamic(sVars);
			levelOne = new Level1(sVars);
			orphan = new Level2(sVars);
			levelOne.add(new Anchor(sVars));
			orphan.add(levelOne.getAnchor());

			// create a level1qxchildqxlevel2 table
			new MyLinkObject(levelOne, orphan, sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		SearchTargets objArr = new SearchTargets(sVars);

		try {
			objArr.add(levelOne); // selected
			objArr.add(new Level2(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		fmd.add(objArr);
		fmd.row = 0;
		fmd.column = 1;

		// loaded levels should be empty
		IdAndStrings idAndStrings = null;
		try {
//			idAndStrings = new IdAndStrings(objArr, 0, SearchTarget.SEARCHTYPES.ORPHANS).doQuery();
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.ORPHANS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

//		if (!idAndStrings.isEmpty())
//			fail("expected empty, found " + idAndStrings.size());
//		// look for the orphans at the second level
////		SearchTarget second = objArr.get(1);
//		try {
////			idAndStrings = new IdAndStrings(objArr, 1, SearchTarget.SEARCHTYPES.ORPHANS).doQuery();
//			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.ORPHANS).doQuery();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
		if (idAndStrings.size() != 1)
			fail("expected 1 orphan, got " + idAndStrings.size());
		if (!idAndStrings.contains(orphan))
			fail("did not find orphan in allTargets collection.");
	}
}
