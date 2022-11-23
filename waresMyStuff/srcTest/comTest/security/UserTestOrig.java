package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.security.Anchor;
import com.security.MyObjectsArray;
import com.security.User;

import comTest.utilities.Utilities;

public class UserTestOrig {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
//		Permissions.initLinks();
//		Role.initLinks();
//		RoleToPermissions.initLinks();
//		User.initLinks();
//		Company.initLinks();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClear() {
		User user = null;
		try {
			user = new User(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (user.id != 0)
			fail("clear");
		if (!user.firstName.isEmpty())
			fail("clear");
		if (!user.lastName.isEmpty())
			fail("clear");
		if (!user.getInstanceName().isEmpty())
			fail("clear");
		if (!user.password.isEmpty())
			fail("clear");

		user.id = 9449;
		user.firstName = "bob";
		user.lastName = "smith";
		try {
			user.setInstanceName("user");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		user.password = "password";

		try {
			user.clear();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (user.id != 0)
			fail("clear");
		if (!user.firstName.isEmpty())
			fail("clear");
		if (!user.lastName.isEmpty())
			fail("clear");
		if (!user.getInstanceName().isEmpty())
			fail("clear");
		if (!user.password.isEmpty())
			fail("clear");
	}

	@Test
	public void testFindInt() {
		User user = new Utilities().getAUser(Utilities.getAnAnchor());
		User tmp = null;
		try {
			tmp = new User(sVars);
			tmp.find(user.id);
		} catch (Exception e) {
			fail("find");
		}
		if (user.id != tmp.id)
			fail("find");
	}

	@Test
	public void testAdd() {
		User user = new Utilities().getAUser(Utilities.getAnAnchor());
		try {
			new User(sVars).find(user.id);
		} catch (Exception e) {
			fail("add");
		}
	}

	@Test
	public void testSanity() {
		Anchor anchor = Utilities.getAnAnchor();
		User user = new Utilities().getAUser(anchor);
		try {
			user.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		
		//Company company = Utilities.getACompany();
		
		user = Utilities.getUserUser(anchor);
		user.firstName = "asdfkja;sldfkja sdflkjasdflakjsdf asdl;kjfas;dflkjas;dfkjas;dfkja;sldfjas;ldfjas;dfkj";
		try {
			user.sanity();
			fail("first name too long");
		} catch (Exception e) {
		}

		user = Utilities.getAdminUser(anchor);
		user.lastName = "asdfkja;sldfkja sdflkjasdflakjsdf asdl;kjfas;dflkjas;dfkjas;dfkja;sldfjas;ldfjas;dfkj";
		try {
			user.sanity();
			fail("last name too long");
		} catch (Exception e) {
		}

		user = Utilities.getUserUser(anchor);
		user.password = "asdfkja;sldfkja sdflkjasdflakjsdf asdl;kjfas;dflkjas;dfkjas;dfkja;sldfjas;ldfjas;dfkj";
		try {
			user.sanity();
			fail("password too long");
		} catch (Exception e) {
		}

		user = Utilities.getUserUser(anchor);
		user.password = "a";
		try {
			user.sanity();
			fail("password too short");
		} catch (Exception e) {
		}

		user = Utilities.getUserUser(anchor);
		user.clear();
		try {
			user.sanity();
			fail("duplicate name allowed");
		} catch (Exception e) {
		}

	}

	@Test
	public void testDelete() {
		MyObjectsArray objs = new MyObjectsArray();
		User user = null;
		try {
			user = new User(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		objs.add(user);
		Anchor anchor = Utilities.getAnAnchor();
		try {
			new Utilities().getAUser(anchor).deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testList() {
		Anchor anchor = Utilities.getAnAnchor();
		for (int i = 0; i < 33; i++) {
			new Utilities().getAUser(anchor);
		}
		try {
			// default user
			if (new User(sVars).listAll().size() != 34){
				fail("list with size of "+new User(sVars).listAll().size());
			}
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
			
	}

	@Test
	public void testIsLoaded() {
		if (!new Utilities().getAUser(Utilities.getAnAnchor()).isLoaded())
			fail("not loaded");
	}

	@Test
	public void testUpdate() {
		User user = new Utilities().getAUser(Utilities.getAnAnchor());
		int userId = user.id;
		final String COMMON = "asdfasdf";

		// first name
		user.firstName = COMMON;
		try {
			user.update();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			user.find(userId);
		} catch (Exception e1) {
			fail("find failed");
		}
		if (!user.firstName.equals(COMMON))
			fail("first name update failed");

		// last name
		user.lastName = COMMON;
		try {
			user.update();
		} catch (Exception e) {
			fail("update");
		}
		try {
			user.find(userId);
		} catch (Exception e1) {
			fail("find failed");
		}
		if (!user.lastName.equals(COMMON))
			fail("first name update failed");

		// user name
		try {
			user.setInstanceName(COMMON);
		} catch (Exception e2) {
			fail(e2.getLocalizedMessage());
		}
		try {
			user.update();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		
		try {
			user.find(userId);
		} catch (Exception e1) {
			fail("find failed");
		}
		
		if (!user.firstName.equals(COMMON))
			fail("first name update failed");

		// password
		user.password = COMMON;
		try {
			user.update();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			user.find(userId);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!user.password.equals(COMMON))
			fail("first name update failed");

	}
}
