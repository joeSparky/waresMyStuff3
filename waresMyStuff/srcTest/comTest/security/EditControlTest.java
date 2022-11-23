package comTest.security;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.security.EditControl;

import comTest.utilities.Utilities;

public class EditControlTest {
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
	public void testFindString() {
		try {
			new EditControl(sVars).find("some random string");
			fail("find was not blocked");
		} catch (Exception e) {
		}
	}

	

	@Test
	public void testListAll() {
		try {
			if (!new EditControl(sVars).listAll().isEmpty())
				fail("new EditControl not empty.");
		} catch (SQLException e) {
			fail(e.getLocalizedMessage());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

}
