package comTest.security.Table;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.security.Table;

import comTest.security.Level1;
import comTest.utilities.Utilities;

public class TableTest {
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
	public void testRemember() {
		try {
			new Level1(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	// second instance should not create new table
	@Test
	public void testRememberTwice() {
		try {
			new Level1(sVars);
			if (new Table().tableCreated(new Level1(sVars), sVars))
				fail("new table created");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
