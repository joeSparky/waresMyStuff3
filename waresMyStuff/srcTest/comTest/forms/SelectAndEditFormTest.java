package comTest.forms;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.SearchTargets;
import com.forms.SelectAndEditForm;
import com.security.Company;
import com.security.MyLinkObject;
//import com.security.MyObjectsArray;
import com.security.Role;
import com.security.User;

import comTest.utilities.Utilities;

public class SelectAndEditFormTest {
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
	public void testResetForm() {

		FormsMatrixDynamic fmd = null;
		Company company = Utilities.getACompany();
		User user = null;
		Role role = null;
		try {
			fmd = new FormsMatrixDynamic(sVars);
			user = new User(sVars);
			role = new Role(sVars);

		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		SearchTargets objs = new SearchTargets(sVars);
		try {
			objs.add(company);
			objs.add(user);
			objs.add(role);

			new MyLinkObject(company, user, sVars);
			new MyLinkObject(user, role, sVars);
//TODO reinstate?
			// objs.updateFilteredList();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		fmd.add(objs);
		try {
			objs = new SearchTargets(sVars);
			objs.add(company);
			objs.add(user);
			objs.add(role);

//TODO reinstate?
			// objs.updateFilteredList();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

//		Everyone everyone = new Everyone();
//		everyone.put(user);
//		everyone.put(role);
//		everyone.put(company);
		fmd.add(objs);
		SelectAndEditForm sf = null;
		try {
			sf = new SelectAndEditForm(new SessionVars(), fmd);
			sf.getForm(new SessionVars());
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace())
				System.out.println(ste);
			fail(e.getLocalizedMessage());
		}
	}
}
