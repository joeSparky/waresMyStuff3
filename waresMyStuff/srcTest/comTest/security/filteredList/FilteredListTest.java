package comTest.security.filteredList;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.forms.FormsMatrixDynamic;
import com.forms.SearchTarget;
import com.forms.SearchTarget.SEARCHTYPES;
import com.forms.SearchTargets;
import com.security.Anchor;
import com.security.MyObject;
import com.security.MyObjectsArray;

import comTest.security.Level1;
import comTest.security.Level2;
import comTest.security.Level3;
import comTest.utilities.Utilities;

public class FilteredListTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * used to build setDescendantsQuery
	 */
	@Test
	public void testSetDescendantsQuery() {
		// create a stack with links between the layers
		Level1 levelOne = null;
		Level2 levelTwo = null;
		Level3 levelThree = null;
		int levelThreeId = -1;

		MyObjectsArray objArray = null;
		try {
			levelOne = new Level1(sVars);
			levelOne.add(new Anchor(sVars));
			levelTwo = new Level2(sVars);
			levelTwo.add(levelOne);
			levelThree = new Level3(sVars);
			levelThree.add(levelOne);
			levelThreeId = levelThree.id;

			objArray = new MyObjectsArray();
			objArray.add(levelOne);
			objArray.add(levelTwo);
			objArray.add(levelThree);

			// create all combinations of links between the layers
			levelOne.addChild(levelTwo);
			levelTwo.addChild(levelThree);

			// clear the bottom portion of the stack
			// levelTwo.clear();
			levelThree.clear();
			// exercise the search string
			levelThree.searchString = "dkkasd fjasd eve";

		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		FormsMatrixDynamic fmd = null;
		// set the focus on the middle tab of the only row

		SearchTargets objs = new SearchTargets(sVars);

		objs.initResults();
//		String ignore = null;
		try {
			fmd = new FormsMatrixDynamic(sVars);
			fmd.row = 0;
			fmd.column = 1;
			objs.add(levelOne, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objs.add(levelTwo, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			objs.add(levelThree, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			fmd.add(objs);
			objs.get(0).setDescendantsQuery(0);
			// orphans need the first tab to be loaded. check before clearing the first tab
			objs.get(0).setOrphanQuery(0);

			// get the ancestors of levelThree into levelOne
			levelOne.clear();
			// let levelTwo be an ancestor of levelThree
			levelTwo.clear();
			levelTwo.searchString = "dkkasd fjasd eve";
			// load up levelThree again so levelTwo can be its ancestor
			levelThree.find(levelThreeId);
			objs.get(0).setAncestorsQuery(0);
			objs.get(0).setDescendantsQuery(0);
			objs.get(0).setInventoryLinkQuery(0);
			objs.get(0).setInventoryQuery(0);
		} catch (Exception e) {
			for (StackTraceElement s : e.getStackTrace()) {
				System.out.println(s);
			}
			fail(e.getLocalizedMessage());
		}
	}

//	@Test
//	public void testSetDescendants() {
//		// create a stack with links between the layers
//		Level1 levelOne = null;
//		Level2 levelTwo = null;
//		Level3 levelThree = null;
//		// remember some ids for later
//		int level2Id = -1;
//		int level3Id = -1;
//
//		MyObjectsArray objArray = null;
//		try {
//			levelOne = new Level1(sVars);
//			levelOne.add(new Anchor(sVars));
//			levelTwo = new Level2(sVars);
//			levelTwo.add(levelOne);
//			level2Id = levelTwo.id;
//			levelThree = new Level3(sVars);
//			levelThree.add(levelOne);
//			level3Id = levelThree.id;
//
//			objArray = new MyObjectsArray();
//			objArray.add(levelOne);
//			objArray.add(levelTwo);
//			objArray.add(levelThree);
//
//			// create all combinations of links between the layers
//			levelOne.addChild(levelTwo);
//			levelTwo.addChild(levelThree);
//
//			// clear the bottom portion of the stack
//			levelTwo.clear();
//			levelThree.clear();
//
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		SearchTargets objs = new SearchTargets(sVars);
//		objs.add(levelOne, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		objs.add(levelTwo, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		objs.add(levelThree, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		objs.initResults();
//
//		// let FilteredList modify objs
//		try {
//			new SearchTarget().setDescendants(objs);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		// loaded layers should be empty
//		if (!objs.get(0).descendantTargets.isEmpty())
//			fail("expected empty descendantTargets, got " + objs.get(0).descendantTargets.size());
//
//		Level2 levelTwoRebuilt = null;
//		Level3 levelThreeRebuilt = null;
//		// rebuild levelTwo and levelThree after the clear
//		try {
//			levelTwoRebuilt = new Level2(sVars);
//			levelTwoRebuilt.find(level2Id);
//			levelThreeRebuilt = new Level3(sVars);
//			levelThreeRebuilt.find(level3Id);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		// levelTwo should be in the middle layer targets
//		if (objs.get(1).descendantTargets.size() != 1)
//			fail("expected size of 1, got " + objs.get(1).descendantTargets.size());
//		if (!objs.get(1).descendantTargets.contains(levelTwoRebuilt))
//			fail("expected contents of levelTwo");
//
//		// levelThree should be in the bottom layer targets
//		if (objs.get(2).descendantTargets.size() != 1)
//			fail("expected size of 1");
//		if (!objs.get(2).descendantTargets.contains(levelThreeRebuilt))
//			fail("expected contents of levelThree");
//	}

//	@Test
//	public void testSetAncestors() {
//		// create a stack with links between the layers
//		Level1 levelOne = null;
//		Level2 levelTwo = null;
//		Level3 levelThree = null;
//		// remember some ids for later
//		int level2Id = -1;
//
//		MyObjectsArray objArray = null;
//		try {
//			levelOne = new Level1(sVars);
//			levelOne.add(new Anchor(sVars));
//			levelTwo = new Level2(sVars);
//			levelTwo.add(levelOne);
//			level2Id = levelTwo.id;
//			levelThree = new Level3(sVars);
//			levelThree.add(levelOne);
//
//			objArray = new MyObjectsArray();
//			objArray.add(levelOne);
//			objArray.add(levelTwo);
//			objArray.add(levelThree);
//
//			// create all combinations of links between the layers
//			levelOne.addChild(levelTwo);
//			levelTwo.addChild(levelThree);
//
//			// clear the middle of the stack
//			levelTwo.clear();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		SearchTargets objs = new SearchTargets(sVars);
//		objs.add(levelOne, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		objs.add(levelTwo, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		objs.add(levelThree, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		objs.initResults();
//
//		// let FilteredList modify objs
//		try {
//			new SearchTarget().setAncestors(objs);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		// loaded layers should be empty
//		if (!objs.get(0).ancestorTargets.isEmpty())
//			fail("expected empty");
//		if (!objs.get(2).ancestorTargets.isEmpty())
//			fail("expected empty");
//
//		Level2 levelTwoRebuilt = null;
//		// rebuild levelTwo after the clear
//		try {
//			levelTwoRebuilt = new Level2(sVars);
//			levelTwoRebuilt.find(level2Id);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		// levelTwo should be in the middle layer targets
//		if (objs.get(1).ancestorTargets.size() != 1)
//			fail("expected size of 1, got " + objs.get(1).ancestorTargets.size());
//		if (!objs.get(1).ancestorTargets.contains(levelTwoRebuilt))
//			fail("expected contents of levelTwo");
//	}

//	@Test
//	public void testSetAncestorsWithOrphan() {
//		// create a stack with links between the layers
//		Level1 levelOne = null;
//		Level2 levelTwo = null;
//		Level3 levelThree = null;
//		Level3 level3Orphan = null;
//		// remember some ids for later
////		int level2Id = -1;
////		int level3OrphanId = -1;
//
//		MyObjectsArray objArray = null;
//		try {
//			levelOne = new Level1(sVars);
//			levelOne.add(new Anchor(sVars));
//			levelTwo = new Level2(sVars);
//			levelTwo.add(levelOne);
////			level2Id = levelTwo.id;
//			levelThree = new Level3(sVars);
//			levelThree.add(levelOne);
//
//			level3Orphan = new Level3(sVars);
//			level3Orphan.add(levelOne);
////			level3OrphanId = level3Orphan.id;
//
//			objArray = new MyObjectsArray();
//			objArray.add(levelOne);
//			objArray.add(levelTwo);
//			objArray.add(levelThree);
//
//			// create all combinations of links between the layers
//			// levelOne.addChild(objArray, levelTwo, 19);
//			// levelTwo.addChild(objArray, levelThree, 9);
//
//			// clear the middle of the stack
//			levelTwo.clear();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		SearchTargets objs = new SearchTargets(sVars);
//		objs.add(levelOne, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		objs.add(levelTwo, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		objs.add(levelThree, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		objs.initResults();
//
//		// let FilteredList modify objs
//		try {
//			new SearchTarget().setAncestors(objs);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//
//		// loaded layers should be in targets
//		if (objs.get(0).ancestorTargets.size() != 1)
//			fail("expected size of 1");
//		if (!objs.get(0).ancestorTargets.contains(levelOne))
//			fail("expected contents of levelOne");
//		if (!objs.get(1).ancestorTargets.isEmpty())
//			fail("expected empty second layer");
//		if (objs.get(2).ancestorTargets.size() != 2)
//			fail("expected size of 2");
//		if (!objs.get(2).ancestorTargets.contains(levelThree))
//			fail("expected contents of levelThree");
//		if (!objs.get(2).ancestorTargets.contains(level3Orphan))
//			fail("expected contents of level3Orphan");
//
////		Level2 levelTwoRebuilt = null;
////		// rebuild levelTwo after the clear
////		try {
////			levelTwoRebuilt = new Level2(sVars);
////			levelTwoRebuilt.find(level2Id);
////		} catch (Exception e) {
////			fail(e.getLocalizedMessage());
////		}
////
////		// levelTwo (the orphan) should be in the middle layer targets
////		if (objs.get(1).ancestorTargets.size() != 1)
////			fail("expected size of 1");
////		if (!objs.get(1).ancestorTargets.contains(levelTwoRebuilt))
////			fail("expected contents of levelTwo");
//	}

	@Test
	public void testSetOrphanQuery() {
		// create a stack with links between the layers
		Level1 levelOne = null;
		Level2 levelTwoOrphan = null;
		Level2 levelTwoFriend = null;
		Level3 levelThreeOrphan = null;
		Level3 levelThreeFriend = null;
		// remember some ids for later
//		int level2Id = -1;
//		int level3Id = -1;

		FormsMatrixDynamic fmd = null;
		SearchTargets objArray = null;
		try {
			fmd = new FormsMatrixDynamic(sVars);
			levelOne = new Level1(sVars);
			levelOne.add(new Anchor(sVars));
			levelTwoOrphan = new Level2(sVars);
			levelTwoOrphan.add(levelOne);
			levelTwoFriend = new Level2(sVars);
			levelTwoFriend.add(levelOne);
//			level2Id = levelTwoOrphan.id;
			levelThreeOrphan = new Level3(sVars);
			levelThreeOrphan.add(levelOne);
			levelThreeFriend = new Level3(sVars);
			levelThreeFriend.add(levelOne);
//			level3Id = levelThreeOrphan.id;

			objArray = new SearchTargets(sVars);
			objArray.add(levelOne);
			objArray.add(levelTwoOrphan);
			objArray.add(levelThreeOrphan);
			fmd.add(objArray);
			// focus on the middle column of the only row
			fmd.row = 0;
			fmd.column = 1;

			// create some children to force the children tables to be created
			levelOne.addChild(levelTwoFriend);
			levelTwoFriend.addChild(levelThreeFriend);

			// clear the bottom portion of the stack
			levelTwoOrphan.clear();
			levelThreeOrphan.clear();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// let FilteredList modify objs
		try {
			fmd.get(0).get(1).setOrphanQuery(0);
		} catch (Exception e) {
			for (StackTraceElement s : e.getStackTrace()) {
				System.out.println(s);
			}
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testFormsMatrixDynamicBySearchTargets() {
		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();

		// extract the forms matrix
//		FormsMatrix formsMatrix = fmd.fm;

		// spin through the searchTargets
		Iterator<SearchTargets> itr = fmd.iterator();
		while (itr.hasNext()) {
			SearchTargets tmp = itr.next();
			// spin through the filtered lists
			Iterator<SearchTarget> fitr = tmp.iterator();
			while (fitr.hasNext()) {
				SearchTarget tmpFilteredList = fitr.next();
				// spin through each query
				for (SEARCHTYPES type : SEARCHTYPES.values()) {
					try {
						System.out.println("type:" + type.toString() + " filteredList:" + tmpFilteredList + "\nquery:"
								+ tmpFilteredList.getQuery(type, 0));
					} catch (Exception e) {
						System.out.print(e.getStackTrace());
						fail(e.getLocalizedMessage());
					}
				}

			}
		}
	}

	@Test
	public void testFormsMatrixDynamicByRowColumn() {
		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();

		// extract the forms matrix
//		FormsMatrix formsMatrix = fmd.fm;

		// spin through the rows
		for (int row = 0; row < fmd.getNumberOfRows(); row++) {
			for (int column = 0; column < fmd.getRow().size(); column++) {
				MyObject obj = fmd.getObject();
				System.out.println("row:" + row + " column:" + column + " obj:" + obj.toString() + " name:"
						+ obj.getInstanceName());
			}
		}
	}
}
