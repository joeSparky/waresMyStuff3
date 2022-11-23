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
import com.security.Company;
import com.security.MyLinkObject;
import com.security.MyObjects;
//import com.security.MyObjectsArray;
import com.security.SelectForm;
import com.security.User;

import comTest.utilities.Utilities;

public class SelectFormTest {
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
	 * create a list of companies that should be available for selection
	 */
	@Test
	public void testGetForm() {
		Company myCompany = Utilities.getACompany();
		Utilities.getACompany();
		Utilities.getACompany();
		Utilities.getACompany();
		Utilities.getACompany();
		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();
		SearchTargets objs = new SearchTargets(sVars);

		try {
			objs.add(myCompany);
			objs.add(new User(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		objs.anchorObject = myCompany;

//		objs.setListInterface(new FilteredList());
		try {
			// create company to user link table
			new MyLinkObject(myCompany, new User(sVars), sVars);
//TODO reinstate?
			// objs.updateFilteredList();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		SelectForm sf = null;
		try {
			sf = new SelectForm(fmd, new SessionVars());
			sf.getForm(new SessionVars());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testGetFilteredListTopSelected() {
		Utilities.getACompany();
		Utilities.getACompany();
		Company selected = Utilities.getACompany();
		Utilities.getACompany();
		Utilities.getACompany();
		FormsMatrixDynamic fmd = null;
		// have the user as the object in focus
		// create 3 users that are children of selected;
		try {
			fmd = new FormsMatrixDynamic(sVars);
			selected.addChild(new Utilities().getAUser(selected.getAnchor()));
			selected.addChild(new Utilities().getAUser(selected.getAnchor()));
			selected.addChild(new Utilities().getAUser(selected.getAnchor()));
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		SearchTargets objs = new SearchTargets(sVars);

		try {
			objs.add(selected);
			objs.add(new User(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		fmd.add(objs);
		fmd.row = 0;
		fmd.column = 1;

		IdAndStrings idAndStrings = null;
		try {
			idAndStrings = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.DESCENDANTS, sVars);
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
		if (idAndStrings == null)
			fail("second.targets null");
//		if (second.descendantTargets.size() != 3)
		if (idAndStrings.size() != 3)
			fail("targets does not have 3 instances");
	}

	@Test
	public void testListAllAnchor() {
		Company yourCompany = Utilities.getACompany();
		Company myCompany = Utilities.getACompany();
		// couple of extra companies
		Utilities.getACompany();
		Utilities.getACompany();
		try {
			if (new Company(sVars).listAll(yourCompany).size() != 1)
				fail("expected a size of 1, got a size of " + new Company(sVars).listAll(yourCompany).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (new Company(sVars).listAll(yourCompany).iterator().next().id != yourCompany.id)
				fail("expected an id of " + yourCompany.id + ", got an id of "
						+ new Company(sVars).listAll(yourCompany).iterator().next().id);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// repeat with myCompany as an anchor
		try {
			if (new Company(sVars).listAll(myCompany).size() != 1)
				fail("expected a size of 1, got a size of " + new Company(sVars).listAll(myCompany).size());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (new Company(sVars).listAll(myCompany).iterator().next().id != myCompany.id)
				fail("expected an id of " + myCompany.id + ", got an id of "
						+ new Company(sVars).listAll(myCompany).iterator().next().id);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	// anchor a couple of users to a company and see if we get them back
	@Test
	public void testListAllAnchorUser() {
		Utilities.getACompany();
		Utilities.getACompany();
		Company myCompany = Utilities.getACompany();
		// couple of extra companies
		Utilities.getACompany();
		Utilities.getACompany();

		User firstMyUser = null;
		User secondMyUser = null;
		try {
			firstMyUser = new Utilities().getAUser(myCompany.getAnchor());
			secondMyUser = new Utilities().getAUser(myCompany.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		MyObjects objs = null;
		try {
			objs = new User(sVars).listAll(myCompany);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (objs.size() != 2)
			fail("expected a size of 2, got " + objs.size());
		if (!objs.contains(firstMyUser))
			fail("objs did not contain firstMyUser");
		if (!objs.contains(secondMyUser))
			fail("objs did not contain secondMyUser");
	}
}
