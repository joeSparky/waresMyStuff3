package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.security.Anchor;
import com.security.Company;
import com.security.MyObjects;
import com.security.MyObjectsArray;
import com.security.Role;
import com.security.User;

import comTest.utilities.Utilities;

public class RoleTest {
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

	// @Test
	// public void linkTestRoleWarehouse() {
	// // test linkExists
	// Role role = Utilities.getARole();
	// Warehouse warehouse = Utilities.getAWarehouse();
	// if (role.linkExists(warehouse))
	// fail("link should not exist yet");
	// // RoleToWarehouse rToW = new RoleToWarehouse();
	// try {
	// new MyLinkObject(role, warehouse).add(1);
	// } catch (Exception e) {
	// fail(e.getLocalizedMessage());
	// }
	// if (!role.linkExists(warehouse))
	// fail("link should exist.");
	// }

	@Test
	public void linkTestUserRole() {
		Company company = Utilities.getACompany();
		// test linkExists
		User user=null;
		try {
			user = new Utilities().getAUser(company.getAnchor());
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Role role=null;
		try {
			role = Utilities.getARole(company.getAnchor());
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		try {
			if (user.linkToChildExists(role))
				fail("link should not exist yet");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			// new MyLinkObject(user, role).add(1);
			user.addChild(role);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// try {
		// user.addAChild(role);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		try {
			if (!user.linkToChildExists(role))
				fail("link should exist.");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void linkTestUserRoleAddParent() {
		Company company = Utilities.getACompany();
		Anchor anchor=null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		// test linkExists
		User user = new Utilities().getAUser(anchor);
		Role role = Utilities.getARole(anchor);
		try {
			if (user.linkToChildExists(role))
				fail("link should not exist yet");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			user.addChild(role);
			// new MyLinkObject(user, role).add(1);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// try {
		// role.addAParent(user);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		try {
			if (!user.linkToChildExists(role))
				fail("link should exist.");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testRole() {
		Company company = Utilities.getACompany();
		Anchor anchor=null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Role role = Utilities.getARole(anchor);
		if (role.id < 1)
			fail("id not set");
		if (role.getInstanceName().isEmpty())
			fail("name is not set");

		// see if id is set in a found role
		Role tmp = null;
		try {tmp = new Role(sVars);
			tmp.find(role.id);
		} catch (Exception e) {
			fail("find");
		}
		if (tmp.id < 1)
			fail("id not set");
	}

	@Test
	public void testFindInt() {
		Utilities.getACompany();
		Company secondCompany = Utilities.getACompany();
		Anchor secondAnchor=null;
		try {
			secondAnchor = secondCompany.getAnchor();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Role role = Utilities.getARole(secondAnchor);
		Utilities.getACompany();
		int roleId = role.id;
		
		try {role.clear();
			role.find(roleId);
		} catch (Exception e) {
			fail("find using id");
		}
	}

	@Test
	public void testUpdate() {
		Company company = Utilities.getACompany();
		Anchor anchor=null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e3) {
			fail(e3.getLocalizedMessage());
		}
		Role role = Utilities.getARole(anchor);
		String tmp = role.getInstanceName();
		try {
			role.setInstanceName(role.getInstanceName() + "junk");
		} catch (Exception e2) {
			fail(e2.getLocalizedMessage());
		}
		try {
			role.update();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!role.getInstanceName().equals(tmp + "junk"))
			fail("update role name");

		// try to update a role to a name that already exists
		role = Utilities.getARole(anchor);
		try {
			role.setInstanceName(tmp + "junk");
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		boolean exceptionThrown = false;
		try {
			role.update();
			fail("allowed updating a role to the name of another existing role");
		} catch (Exception e) {
			exceptionThrown = true;
			System.out.println("fyi:" + e.getLocalizedMessage());
		}
		if (!exceptionThrown)
			fail("did not throw exception");
	}

	@Test
	public void testDeleteRole() {
		Company company = Utilities.getACompany();
		Anchor anchor=null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e2) {
			fail(e2.getLocalizedMessage());
		}
		// role with id==1 is the default role. Don't try to delete it.
		Utilities.getARole(anchor);

		// get a role we can delete
		Role role = Utilities.getARole(anchor);
		int roleId = role.id;
		MyObjectsArray objs = new MyObjectsArray();
		objs.add(role);
		try {
			role.deleteUnconditionally();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		boolean exceptionThrown = false;
		try {
			role.find(roleId);
			fail("should not have found deleted role.");
		} catch (Exception e) {
			exceptionThrown = true;
		}
		if (!exceptionThrown)
			fail("did not throw exception");
	}

	@Test
	public void testIsLoaded() {
		Company company = Utilities.getACompany();
		Anchor anchor=null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Role role = Utilities.getARole(anchor);
		if (!role.isLoaded())
			fail("isLoaded");
		try {
			role.clear();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (role.isLoaded())
			fail("isLoaded");
	}

	@Test
	public void testAdd() {
		Company company = Utilities.getACompany();
		Anchor anchor=null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Utilities.getARole(anchor);
	}

	@Test
	public void testList() {
		Company company = Utilities.getACompany();
		Anchor anchor=null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		int expected = 7;
		for (int i = 0; i < expected; i++)
			Utilities.getARole(anchor);
		MyObjects roles=null;
		try {
			roles = new Role(sVars).listAll();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (roles.size() != expected)
			fail("Found " + roles.size() + " " + Role.NAMES
					+ ". Should have been " + (expected));
	}

	@Test
	public void testToSearch() {
		Company company = Utilities.getACompany();
		Anchor anchor=null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		for (int i = 0; i < 37; i++)
			Utilities.getARole(anchor);
		try {
			new Role(sVars).listAll();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

}
