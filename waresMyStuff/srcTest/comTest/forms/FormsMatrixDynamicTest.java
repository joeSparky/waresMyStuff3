package comTest.forms;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.EndOfInputRedoQueries;
import com.forms.FormsMatrixDynamic;
import com.forms.IdAndStrings;
import com.forms.SearchTarget;
import com.forms.SearchTargets;
import com.forms.SearchTarget.SEARCHTYPES;
import com.security.Anchor;

import comTest.security.Level1;
import comTest.security.Level2;
import comTest.utilities.Utilities;

public class FormsMatrixDynamicTest {

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
	/**
	 * walk through a list of Level2 using the NEXT button. Verify that each
	 * sub-list is correct.
	 */
	public void nextButtonTest() {
		// set the display size for testing
		IdAndStrings.DISPLAYSIZE = 7;
		// shortcut to the good stuff
		SearchTarget st = null;
		IdAndStrings ids = null;
		ArrayList<String> level2Names = new ArrayList<String>();

		FormsMatrixDynamic fmd = null;
		try {
			fmd = new FormsMatrixDynamic(sVars);
			Level1 level1 = new Level1(sVars);
			level1.add(new Anchor(sVars));
			// add two times DISPLAYSIZE + 3 to get a partial list on the third batch
			for (int i = 0; i < IdAndStrings.DISPLAYSIZE * 2 + 3; i++) {
				Level2 level2 = new Level2(sVars);
				level2.add(level1);
				level2Names.add(level2.getInstanceName());
			}
			SearchTargets sts = new SearchTargets(sVars);
			sts.add(new Level1(sVars));
			sts.add(new Level2(sVars));
			fmd.add(sts);
			fmd.row = 0;
			// point to the Sevel2s
			fmd.column = 1;

			// shortcuts
			st = fmd.get(0).get(1);
			ids = st.getIdAndStrings(SEARCHTYPES.ALL);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		boolean allGood = false;

		///////////////////////////////////////////////////////////////////
		// get the first DISPLAYSIZE Level2s with requesting a list of ALL
		sVars.parameterMap.clear();
		sVars.parameterMap.put(ids.REQUESTALIST, null);
		IdAndStrings results = null;
		try {
			fmd.get(0).get(1).extractParams(sVars);
		} catch (EndOfInputRedoQueries e) {
			allGood = true;
		} catch (Exception e) {

			for (StackTraceElement ste : e.getStackTrace())
				System.out.println(ste);
			fail(e.getLocalizedMessage());
		}
		if (!allGood)
			fail("didn't get " + EndOfInputRedoQueries.class.getCanonicalName());

		try {
			results = ids.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (results.size() != IdAndStrings.DISPLAYSIZE)
			fail("expected size:" + IdAndStrings.DISPLAYSIZE + " got:" + results.size());
		for (int i = 0; i < IdAndStrings.DISPLAYSIZE; i++)
			if (!results.get(i).string.equals(level2Names.get(i)))
				fail("did not find name:" + level2Names.get(i) + " in results");

		/////////////////////////////////////////////////////////////////////////
		// get the second DISPLAYSIZE level2 with a NEXT button
		sVars.parameterMap.clear();
		sVars.parameterMap.put(ids.NEXT, null);
		try {
			fmd.get(0).get(1).extractParams(sVars);
		} catch (EndOfInputRedoQueries e) {
			allGood = true;
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace())
				System.out.println(ste);
			fail(e.getLocalizedMessage());
		}
		if (!allGood)
			fail("didn't get " + EndOfInputRedoQueries.class.getCanonicalName());
		try {
			results = ids.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (results.size() != IdAndStrings.DISPLAYSIZE)
			fail("expected size:" + IdAndStrings.DISPLAYSIZE + " got:" + results.size());
		for (int i = 0; i < IdAndStrings.DISPLAYSIZE; i++)
			if (!results.get(i).string.equals(level2Names.get(i + IdAndStrings.DISPLAYSIZE)))
				fail("did not find name:" + level2Names.get(i + IdAndStrings.DISPLAYSIZE) + " in results");

		/////////////////////////////////////////////////////////////////////////
		// get the third (partial) list with a NEXT button
		sVars.parameterMap.clear();
		sVars.parameterMap.put(ids.NEXT, null);
		try {
			fmd.get(0).get(1).extractParams(sVars);
		} catch (EndOfInputRedoQueries e) {
			allGood = true;
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace())
				System.out.println(ste);
			fail(e.getLocalizedMessage());
		}
		if (!allGood)
			fail("didn't get " + EndOfInputRedoQueries.class.getCanonicalName());
		try {
			results = ids.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (results.size() != 3)
			fail("expected size:3 got:" + results.size());
		for (int i = 0; i < 3; i++)
			if (!results.get(i).string.equals(level2Names.get(i + 2 * IdAndStrings.DISPLAYSIZE)))
				fail("did not find name:" + level2Names.get(i + 2 * IdAndStrings.DISPLAYSIZE) + " in results");
		
		///////////////////////////////////////////////////////////////////////////////
		// get the previous (second) sublist with the PREVIOUS button
		sVars.parameterMap.clear();
		sVars.parameterMap.put(ids.PREVIOUS, null);
		try {
			fmd.get(0).get(1).extractParams(sVars);
		} catch (EndOfInputRedoQueries e) {
			allGood = true;
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace())
				System.out.println(ste);
			fail(e.getLocalizedMessage());
		}
		if (!allGood)
			fail("didn't get " + EndOfInputRedoQueries.class.getCanonicalName());
		try {
			results = ids.doQuery();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		if (results.size() != IdAndStrings.DISPLAYSIZE)
//			fail("expected size:" + IdAndStrings.DISPLAYSIZE + " got:" + results.size());
		for (int i = 0; i < IdAndStrings.DISPLAYSIZE; i++)
			if (!results.get(i).string.equals(level2Names.get(i + IdAndStrings.DISPLAYSIZE)))
				fail("did not find name:" + level2Names.get(i + IdAndStrings.DISPLAYSIZE) + " in results");
		
	}

//	@Test
//	public void testAddSearchTargets() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetObject() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPreviousObjectInRow() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetAnchor() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetRowSize() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetRow() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSearchTarget() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetObjectSelectedLastTime() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testClearAllObjectSelectedLastTime() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSelector() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetObjectAboveMeInRow() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetObjectBelowMeInRow() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testIsObjectBelowMeInRow() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testIsObjectAboveMeInRow() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetNumberOfRows() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetObjectSelectedLastTime() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetEditSelectType() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testOneAndOnlyOneObjectSelectedLastTime() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAboveMeIsParent() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testEditOrAddForm() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSearchForm() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testExtractSearchFormParams() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testExtractEditAddFormParams() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testFindInstanceOf() {
//		fail("Not yet implemented");
//	}

}
