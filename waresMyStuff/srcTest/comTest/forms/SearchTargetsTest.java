package comTest.forms;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.forms.FormsMatrixDynamic;
import com.forms.IdAndStrings;
import com.forms.SearchTarget;
import com.forms.SearchTargets;
import com.security.Anchor;
import com.security.MyLinkObject;

import comTest.security.Level1;
import comTest.security.Level2;
import comTest.utilities.Utilities;

public class SearchTargetsTest {
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
	public void searchStringTest() {
		// create a bunch of levelTwos, set a search string, and see if the
		// updated list only contains what should pass the search test.

//		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();

		// make the display size a non-issue
		IdAndStrings.DISPLAYSIZE = 99999;

		// populate the database
		Level1 anchor = null;
		try {
			anchor = new Level1(sVars);
			anchor.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// each nameInstance with nothing in common
		Level2 levelTwo = null;
		for (int i = 0; i < 10; i++)
			try {
				levelTwo = new Level2(sVars);
				levelTwo.add(anchor);
				anchor.addChild(levelTwo);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}

		// add 5 bubbas
		for (int i = 0; i < 5; i++)
			try {
				levelTwo = new Level2(sVars);
				levelTwo.setInstanceName(levelTwo.getInstanceName() + " bubba");
				levelTwo.add(anchor);
				anchor.addChild(levelTwo);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}

		// create a single row, 2 tab FormsMatrixDynamic
		SearchTargets searchTargets = new SearchTargets(sVars);
		SearchTarget filteredList = new SearchTarget(anchor, sVars);
//		filteredList.obj = anchor;
		searchTargets.add(filteredList);

		// unselected Level2
		Level2 level2 = null;
		try {
			level2 = new Level2(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		filteredList = new SearchTarget(level2, sVars);
//		filteredList.obj = level2;
		searchTargets.add(filteredList);
		FormsMatrixDynamic formsMatrixDynamic = null;
		try {
			formsMatrixDynamic = new FormsMatrixDynamic(sVars);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
//		FormsMatrix formsMatrix = new FormsMatrix();
		formsMatrixDynamic.add(searchTargets);
//		FormsMatrixDynamic formsMatrixDynamic = new FormsMatrixDynamic(formsMatrix);
		// pass the structure to the lower levels
//		formsMatrix.setFormsMatrixDynamic(formsMatrixDynamic);
		// point to the unloaded level2
		formsMatrixDynamic.column = 1;
		formsMatrixDynamic.row = 0;

		IdAndStrings idAndStrings = null;
		try {
			idAndStrings = new IdAndStrings(formsMatrixDynamic, SearchTarget.SEARCHTYPES.DESCENDANTS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
			idAndStrings.doQuery();
		} catch (Exception e) {
			for (StackTraceElement ele : e.getStackTrace()) {
				System.out.println(ele);
			}
//			System.out.print(e.getStackTrace());
			fail(e.getLocalizedMessage());
		}

		if (idAndStrings.size() != 15)
			fail("expected size of 15, got " + idAndStrings.size());

		// make changes
		level2.searchString = "bubba gump";
		idAndStrings.clear();
//		idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//		idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//		idAndStrings.firstDisplayedRecord = -1;

		// should have 5 bubbas
		try {
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (idAndStrings.size() != 5)
			fail("expected size of 5, got " + idAndStrings.size());

	}

	@Test
	public void searchStringWordBreaksTest() {
		// create 10 levelTwos, 5 levelTwos with "bubba", and 7 levelTwos with
		// "bubba gump".
		// look for "gum bubba", should 5 of the "bubba"s and 7 of the
		// "bubba gump"s.
		Level1 levelOne = null;

		FormsMatrixDynamic fmd =null; 

		try {
			fmd = new FormsMatrixDynamic(sVars);
			levelOne = new Level1(sVars);
			levelOne.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// each nameInstance with nothing in common
		Level2 levelTwo = null;
		for (int i = 0; i < 10; i++)
			try {
				levelTwo = new Level2(sVars);
				levelTwo.add(levelOne);
				// mess with quantity just for fun
				levelOne.addChild(levelTwo);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}

		// add 5 bubbas
		for (int i = 0; i < 5; i++)
			try {
				levelTwo = new Level2(sVars);
				levelTwo.setInstanceName(levelTwo.getInstanceName() + " bubba");
				levelTwo.add(levelOne);
				levelOne.addChild(levelTwo);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}

		// add 7 bubba gumps
		for (int i = 0; i < 7; i++)
			try {
				levelTwo = new Level2(sVars);
				levelTwo.setInstanceName(levelTwo.getInstanceName() + " bubba gump");
				levelTwo.add(levelOne);
				// mess with quantity just for fun
				levelOne.addChild(levelTwo);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
//		FormsMatrixDynamic fmd = new FormsMatrixDynamic();

		SearchTargets objs = new SearchTargets(sVars);

		// unselected level2
		try {
			objs.add(levelOne);
			objs.add(new Level2(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		fmd.add(objs);

// point to the Level2
		fmd.row = 0;
		fmd.column = 1;
		// take a snapshot of what we have so far
		// search string not set to find the bubbas
		IdAndStrings idAndStrings = null;
		try {
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.DESCENDANTS, sVars);
			// make the display size a non-issue
			IdAndStrings.DISPLAYSIZE = 99999;
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace()) {
				System.out.println(ste);
			}
			fail(e.getLocalizedMessage());
		}
//		SearchTarget levelTwoFirst = objs.getFilteredList(1);
		if (idAndStrings.size() != 10 + 5 + 7)
			fail("expected size of 10 + 5 + 7, got " + idAndStrings.size());

		// look for gum bub. should pick up 5 bubba gumps
		try {
			objs.get(1).obj.searchString = "gum bub";
//			objs.updateFilteredList();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// should have 5 "bubba"s and 7 "bubba gump"s
		try {
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.DESCENDANTS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (idAndStrings.size() != 12)
			fail("expected size of 12, got " + idAndStrings.size());

	}

	@Test
	public void testOrphan() {
		FormsMatrixDynamic fmd =null; 
		Level1 levelOne = null;
		Level2 levelTwo = null;
		try {
			fmd = new FormsMatrixDynamic(sVars);
			levelOne = new Level1(sVars);
			levelTwo = new Level2(sVars);
			levelOne.add(new Anchor(sVars));
			// create an orphan
			levelTwo.add(levelOne);

			// create a level1qxchildqxlevel2 table
			new MyLinkObject(levelOne, levelTwo, sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		SearchTargets objs = new SearchTargets(sVars);

		// unselected level2
		try {
			objs.add(levelOne);
			objs.add(new Level2(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		fmd.add(objs);
		fmd.row = 0;
		fmd.column = 1;
//		objs.anchorObject = levelOne;
//		try {
//			objs.updateFilteredList();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		SearchTarget levelTwoFirst = objs.getFilteredList(1);
		IdAndStrings idAndStrings = null;
		try {
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.ORPHANS, sVars);
//			idAndStrings.displayState = IdAndStrings.DISPLAYSTATE.ATBEGINNINGWITHOUTASEARCH;
//			idAndStrings.direction = IdAndStrings.DIRECTION.FORWARD;
//			idAndStrings.firstDisplayedRecord = -1;
			idAndStrings.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!idAndStrings.contains(levelTwo))
			fail("targets does not contain " + levelTwo.getInstanceName());
	}
}
