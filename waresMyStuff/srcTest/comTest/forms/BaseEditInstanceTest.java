package comTest.forms;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;

import com.db.SessionVars;
import com.forms.BaseEditParams;
import com.forms.SearchTargets;
import com.security.Anchor;
import com.security.Everyone;
import com.security.MyObject;

import comTest.security.Level1;
import comTest.security.Level2;
import comTest.security.Level3;
import comTest.security.Recurse;
import comTest.utilities.Utilities;

public class BaseEditInstanceTest {

	BlankForm callBack = null;
	SessionVars sVars = null;
	Level1 levelOne = null;
	MyObject levelTwo = null;
	Level3 levelThree = null;
	MyObject orphan = null;
	MyObject parentOfLevelTwo = null;

	BaseEditParams p = null;

	@Before
	public void setUp() throws Exception {
	}

	public void standardForm() {
		standardForm(false);
	}

	public void standardForm(boolean recursive) {
		try {
			sVars = new SessionVars();
			sVars.clear();
			new Utilities().allNewTables(sVars);
			callBack = new BlankForm();
			// callBack.callBackVar = new BlankForm();

			levelOne = new Level1(sVars);
			// MyObject levelTwo = null;
			if (recursive) {
				levelTwo = new Recurse(sVars);
				orphan = new Recurse(sVars);
				parentOfLevelTwo = new Recurse(sVars);
			} else {
				levelTwo = new Level2(sVars);
				parentOfLevelTwo = new Level1(sVars);
			}
			levelThree = new Level3(sVars);

			// levelOne anchors itself
			levelOne.add(new Anchor(sVars));
			levelTwo.add(levelOne.getAnchor());
			parentOfLevelTwo.add(levelOne.getAnchor());
			levelThree.add(levelOne.getAnchor());

			levelOne.addChild(levelTwo);
			levelTwo.addChild(levelThree);
			if (orphan != null)
				orphan.add(levelOne.getAnchor());
			parentOfLevelTwo.addChild(levelTwo);

			SearchTargets objs = new SearchTargets(sVars);
			objs.add(levelOne);
			objs.add(levelTwo);
			objs.add(levelThree);
//			objs.setListInterface(new FilteredList());

			Everyone everyone = new Everyone();
			everyone.put(new Level1(sVars));
			everyone.put(new Level2(sVars));
			everyone.put(new Level3(sVars));

			p = new BaseEditParams(callBack, objs, 1, everyone);

			// update the filtered list and see if editors get new info
//			objs.updateFilteredList();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void getFormTest() {
//		standardForm();
//		try {
//			o.getForm(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//	}

//	// select a recursive parent
//	@Test
//	public void parentStringTest() {
//		standardForm(true);
//		// let the editor set the parent string
//		try {
//			o.getForm(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (o.parentString.isEmpty())
//			fail("need a target object with a parent for this test.");
//		sVars.parameterMap.put(o.parentString, new String[0]);
//		try {
//			o.extractParams(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//	}

//	@Test
//	public void testExtractParams() {
//		standardForm();
//		try {
//			o.extractParams(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//	}
}
