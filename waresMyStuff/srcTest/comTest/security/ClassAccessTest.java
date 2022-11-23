package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.SearchTarget;
import com.security.ClassAccess;
import com.security.Company;

import comTest.utilities.Utilities;

public class ClassAccessTest {
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
	public void testClear() throws Exception {
		new ClassAccess(sVars).clear();
	}

	@Test
	public void testGetAName() throws Exception {
		if (!new ClassAccess(sVars).getAName().equals("a ClassAccess"))
			fail("in getAName(), expected NewRole, got " + new ClassAccess(sVars).getAName());
	}

	@Test
	public void testGetLogicalName() throws Exception {
		if (!new ClassAccess(sVars).getLogicalName().equals(ClassAccess.NAME))
			fail("getLogicalName() failed");
	}
	
	@Test
	public void testDelete() {
		Company company = Utilities.getACompany();
		try {
//			new ClassAccess().addEditSelectType(company.getAnchor(), company, SearchTarget.EDITSELECTTYPE.SELECT).deleteLinkToParent(new NewRole());
			new ClassAccess(sVars).addEditSelectType(company.getAnchor(), company, SearchTarget.EDITSELECTTYPE.SELECT).deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
