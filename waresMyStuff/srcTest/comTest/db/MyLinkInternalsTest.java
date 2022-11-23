package comTest.db;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.security.Company;
import com.security.MyLinkObject;
import com.security.MyObject;

import comTest.utilities.Utilities;

public class MyLinkInternalsTest {

	ArrayList<MyObject> pair;
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		pair = addObjectPair();
	}

	protected ArrayList<MyObject> addObjectPair() {
		ArrayList<MyObject> objs = new ArrayList<MyObject>();
		Company company = Utilities.getACompany();
		// objs.add(new Level1(sVars));
		try {
			objs.add(Utilities.getLevel1(company.getAnchor()));
			objs.add(Utilities.getLevel2(company.getAnchor()));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// objs.add(new Level2(sVars));
		return objs;
	}

	@After
	public void tearDown() throws Exception {
//		MyConnection.flush();
	}

	@Test
	public void testGetFileName() {
		String fName = null;
		try {
			fName = new MyLinkObject(pair.get(0), pair.get(1), sVars).getMyFileName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fName.equals(pair.get(0).getClass().getName() + "To" + pair.get(0).getClass().getName()))
			fail("fail name");
	}
}
