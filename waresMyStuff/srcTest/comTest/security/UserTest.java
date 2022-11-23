package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.security.Anchor;
import com.security.Company;
import com.security.MyObjectsArray;
import com.security.User;

import comTest.utilities.Utilities;

public class UserTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
		// Permissions.initLinks();
		// Role.initLinks();
		// RoleToPermissions.initLinks();
		// User.initLinks();
		// Company.initLinks();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClear() {
		User user = null;
		try {
			user = new User(sVars);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
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
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		User user = Utilities.getUserUser(anchor);
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
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		User user = Utilities.getUserUser(anchor);
		try {
			new User(sVars).find(user.id);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testUpdateAfterDelete() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		User user = new Utilities().getAUser(anchor);
		User copy = null;
		try {
			copy = new User(sVars);
			copy.find(user.id);
		} catch (Exception e) {
			fail("find");
		}
		MyObjectsArray objs = new MyObjectsArray();
		User second = null;
		try {
			second = new User(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		objs.add(second);
		try {
			user.deleteUnconditionally();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		try {
			copy.update();
			fail("allowed update on deleted user");
		} catch (Exception e) {
		}
	}

	@Test
	public void testSanity() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		User user = new Utilities().getAUser(anchor);
		try {
			user.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		user.firstName = "asdfkja;sldfkja sdflkjasdflakjsdf asdl;kjfas;dflkjas;dfkjas;dfkja;sldfjas;ldfjas;dfkj";
		try {
			user.sanity();
			fail("first name too long");
		} catch (Exception e) {
		}

		user = Utilities.getUserUser(anchor);
		user.lastName = "asdfkja;sldfkja sdflkjasdflakjsdf asdl;kjfas;dflkjas;dfkjas;dfkja;sldfjas;ldfjas;dfkj";
		try {
			user.sanity();
			fail("last name too long");
		} catch (Exception e) {
		}

		user = Utilities.getUserUser(anchor);
		try {
			user.setInstanceName(
					"asdfkja;sldfkja sdflkjasdflakjsdf asdl;kjfas;dflkjas;dfkjas;dfkja;sldfjas;ldfjas;dfkj");
			fail("allowed an excessive name length.");
		} catch (Exception e1) {
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
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyObjectsArray objs = new MyObjectsArray();

		User user = null;
		try {
			user = new User(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		objs.add(user);
		try {
			Utilities.getUserUser(anchor).deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testList() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		for (int i = 0; i < 33; i++) {
			Utilities.getUserUser(anchor);
		}
		// don't forget default user
		try {
			// default user
			if (new User(sVars).listAll().size() != 34)
				fail("list");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIsLoaded() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!Utilities.getUserUser(anchor).isLoaded())
			fail("not loaded");
	}

	@Test
	public void testUpdate() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		User user = Utilities.getUserUser(anchor);
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
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!user.firstName.equals(COMMON))
			fail("first name update failed");

		// last name
		user.lastName = COMMON;
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
		if (!user.lastName.equals(COMMON))
			fail("last name update failed");

		// don't allow duplicate user names
		User dupe = new Utilities().getAUser(anchor);
		try {
			// set dupe's name to user's name that already exists
			dupe.setInstanceName(user.getInstanceName());
		} catch (Exception e3) {
			fail(e3.getLocalizedMessage());
		}
		try {
			dupe.update();
			fail("allowed a duplicate user name");
		} catch (Exception e3) {
		}

		// get a current copy of user
		try {
			user.find(userId);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

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
			fail("password update failed");

	}

	@Test
	public void testIsValidUserLengths() {
		User user = null;
		try {
			user = new User(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// null name
		try {
			user.isValidUser(null, "nice password");
			fail("allowed a short user name");
		} catch (Exception e) {
		}
		// null password
		try {
			user.isValidUser("nice user name ", null);
			fail("allowed a short password");
		} catch (Exception e) {
		}

		// name too short
		try {
			user.isValidUser("", "nice password");
			fail("allowed a short user name");
		} catch (Exception e) {
		}
		// password too short
		try {
			user.isValidUser("nice user name ", "");
			fail("allowed a short password");
		} catch (Exception e) {
		}

		// name too long
		String longString = "dkfjasdfkjasdfkjaskdfjasdkfjasdfkljas;dfkja;sdlfjkasd;fkjasdfjkasdfhjkasfhdajsfhdkafhsdasjfhdasdjkfhasdfjhasdkjfhasdfhsadlfh";
		try {
			user.isValidUser(longString, "nice password");
			fail("allowed a long user name");
		} catch (Exception e) {
		}
		// password too long
		try {
			user.isValidUser("nice user name ", longString);
			fail("allowed a long password");
		} catch (Exception e) {
		}
	}

	@Test
	public void testNamePasswordInDb() {
		Company company = Utilities.getACompany();
		User user = null;
		try {
			user = new User(sVars);
			user.setInstanceName("goodUserName");
			user.password = "goodPassword";
			user.add(company);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			new User(sVars).isValidUser("goodUserName", "goodPassword");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testNotNamePasswordInDb() {
		Company company = Utilities.getACompany();
		User user = null;
		try {
			user = new User(sVars);
			user.setInstanceName("goodUserName");
			user.password = "goodPassword";
			user.add(company);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			new User(sVars).isValidUser("badUserName", "goodPassword");
			fail("logged in a user with badUserName");
		} catch (Exception e) {
		}
	}

	@Test
	public void testNameNotPasswordInDb() {
		Company company = Utilities.getACompany();
		User user = null;
		try {
			user = new User(sVars);
			user.setInstanceName("goodUserName");
			user.password = "goodPassword";
			user.add(company);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			new User(sVars).isValidUser("goodUserName", "badPassword");
			fail("logged in a user with badPassword");
		} catch (Exception e) {
		}
	}
}
