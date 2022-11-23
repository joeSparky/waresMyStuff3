package comTest.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.db.SessionVars;
import com.db.TableVersion;

public class TableVersionTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
	}

	@Test
	public void testGetLatestVersion() {
		int versionNumber = 0;
		try {
			versionNumber = new TableVersion().getLatestVersion("ahsdkfa", sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (versionNumber != -1)
			fail("did not throw exception on non-existant table name");

		// get latest version of a first record
//		try {
//			com.db.TableVersion.newTable(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		} 
		try {
			new TableVersion().addNewVersion("firstTable", "first reason", 1, sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			versionNumber = new TableVersion().getLatestVersion("firstTable", sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (versionNumber != 1)
			fail("version number of " + versionNumber + ". should have been 1.");

	}

	@Test
	public void testAddNewVersion() {
		int versionNumber = 0;
//		try {
//			com.db.TableVersion.newTable(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		} 
//		calledBack = false;
		try {
			new TableVersion().addNewVersion("newTable", "new reason", 1, sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		if (calledBack)
//			fail("add new version broken");
		try {
			versionNumber = new TableVersion().getLatestVersion("newTable", sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (versionNumber != 1)
			fail("return wrong version number ");

//		calledBack = false;
		try {
			new TableVersion().addNewVersion("newTable", "another reason", 2, sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		if (calledBack)
//			fail("add second version broken");
		try {
			versionNumber = new TableVersion().getLatestVersion("newTable", sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (versionNumber != 2)
			fail("version number should be 2.");
	}

//	boolean calledBack = false;
//
//	@Override
//	public void callBack() {
//		calledBack = true;
//
//	}

}
