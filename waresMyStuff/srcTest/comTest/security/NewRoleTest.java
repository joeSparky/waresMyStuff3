package comTest.security;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.forms.SearchTarget;
import com.forms.SearchTarget.EDITSELECTTYPE;
import com.security.Anchor;
import com.security.ClassAccess;
//import com.security.ClassAccess;
import com.security.Company;
import com.security.MyObjects;
import com.security.NewRole;
import com.security.User;

import comTest.utilities.Utilities;

public class NewRoleTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
		anchor = Utilities.getACompany().getAnchor();
	}

	Anchor anchor = null;

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAName() {
		NewRole newRole = null;
		try {
			newRole = new NewRole(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!newRole.getAName().equals("a NewRole"))
			fail("in getAName(), expected NewRole, got " + newRole.getAName());
	}

	@Test
	public void testGetLogicalName() {
		if (!Utilities.getANewRole(anchor).getLogicalName().equals(NewRole.NAME))
			fail("getLogicalName() failed");
	}

//	@Test
//	public void testGetFileName() {
//		if (!Utilities.getANewRole(anchor).getMyFileName().equals("NewRole"))
//			fail("got getFileName of " + Utilities.getANewRole(anchor).getMyFileName());
//	}

	@Test
	public void testHashCode() {
		if (Utilities.getANewRole(anchor).hashCode() == 0)
			fail("no hashCode");
	}

	@Test
	public void testEquals() {
		if (Utilities.getANewRole(anchor).equals(new Object()))
			fail("no equals");
	}

	@Test
	public void testEqualsObject() {
		if (Utilities.getANewRole(anchor).equalsObject(new Object()))
			fail("no equalsObject");
	}

	@Test
	public void testGetNew() {
		try {
			Utilities.getANewRole(anchor).getNew();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDeleteUnconditionallly() {
		NewRole role = null;
		String roleName = null;
		try {
			role = Utilities.getANewRole(anchor);
			roleName = role.getInstanceName();
			role.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			new NewRole(sVars).find(anchor, roleName);
			fail("found role that was removed");
		} catch (Exception e) {
		}
	}

	@Test
	public void testDeleteUnconditionalllyWithClassAccess() {
		NewRole role = null;
		String roleName = null;
		try {
			role = Utilities.getANewRole(anchor);
			role.add(new ClassAccess(sVars).addEditSelectType(anchor, new Level1(sVars), EDITSELECTTYPE.SELECT));
			roleName = role.getInstanceName();
			role.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			new NewRole(sVars).find(anchor, roleName);
			fail("found role that was removed");
		} catch (Exception e) {
		}
	}

	@Test
	public void testDeleteUnconditionalllyWithUser() {
		NewRole role = null;
		String roleName = null;
		try {
			role = Utilities.getANewRole(anchor);
			User user = new Utilities().getAUser(anchor);
			role.add(new ClassAccess(sVars).addEditSelectType(anchor, user, EDITSELECTTYPE.SELECT));
			roleName = role.getInstanceName();
			role.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			new NewRole(sVars).find(anchor, roleName);
			fail("found role that was removed");
		} catch (Exception e) {
		}
	}

	@Test
	public void testList() {
		try {
			Utilities.getANewRole(anchor).list(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testListUsers() {
		try {
			Utilities.getANewRole(anchor).listUsers();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testListUsersUnloadedRole() {
		try {
			new NewRole(sVars).listUsers();
			fail("allowed listUsers on unloaded role");
		} catch (Exception e) {
		}
	}

	@Test
	public void testListClassAccessUnloadedRole() {
		try {
			new NewRole(sVars).listClassAccesses();
			fail("allowed listClassAccesses on unloaded role");
		} catch (Exception e) {
		}
	}

	@Test
	public void testListClassAccessEmpty() {
		NewRole role = Utilities.getANewRole(anchor);
		try {
			if (!role.listClassAccesses().isEmpty())
				fail("not empty");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testListClassAccess() {
		NewRole role = Utilities.getANewRole(anchor);
		ClassAccess classAccess = null;
		try {
			classAccess = new ClassAccess(sVars).addEditSelectType(anchor, new Level1(sVars), EDITSELECTTYPE.SELECT);
			role.add(classAccess);
			if (!role.listClassAccesses().contains(classAccess))
				fail("classAccess not found");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testRemoveUser() {
		NewRole role = null;
		User user = null;
		try {
			// get a new role, add a user, and then remove the role
			role = Utilities.getANewRole(anchor);
			user = new Utilities().getAUser(new Anchor(sVars));
			role.add(user);
			role.remove(user);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// try to find the user
		try {
			if (role.listUsers().containsMyObject(user))
				fail("role still has user");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testRemoveClassAccess() {
		NewRole role = null;
		role = Utilities.getANewRole(anchor);
		ClassAccess classAccess = null;
		try {
			classAccess = new ClassAccess(sVars).addEditSelectType(anchor, new Level1(sVars), SearchTarget.EDITSELECTTYPE.SELECT);
			role.add(classAccess);
			role.remove(classAccess);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (role.listClassAccesses().contains(classAccess))
				fail("still has classAccess");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void testAddUser() throws Exception {
		Company company = Utilities.getACompany();
		NewRole role = null;
		User user = null;
		MyObjects users = null;
		try {
			role = new NewRole(sVars).setAnchorAndName(company.getAnchor(), "ferSure").sanityRole()
					.add(company.getAnchor());
			user = new Utilities().getAUser(company.getAnchor());
			role.add(user);
			users = role.listUsers();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (!users.containsMyObject(user))
			fail("could not find user");
		if (users.size() != 1)
			fail("expected a users size of 1, got " + users.size());

	}

	@Test
	public void testAddUser2() throws Exception {
		Company company = Utilities.getACompany();
		NewRole role = null;
		User user = null;
		MyObjects users = null;
		User secondUser = null;
		try {
			role = new NewRole(sVars).setAnchorAndName(company.getAnchor(), "ferSure").sanityRole()
					.add(company.getAnchor());
			user = new Utilities().getAUser(company.getAnchor());
			role.add(user);
			users = role.listUsers();
			secondUser = new Utilities().getAUser(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (users.containsMyObject(secondUser))
			fail("should not find secondUser");
		if (users.size() != 1)
			fail("expected a users size of 1, got " + users.size());

	}

	@Test
	public void testFindDifferentName() {
		Company company = Utilities.getACompany();
		try {
			new NewRole(sVars).setAnchorAndName(company.getAnchor(), "ferSure").sanityRole().add(company.getAnchor());
			new NewRole(sVars).find(company.getAnchor(), "notferSure");
			fail("found notferSure");
		} catch (Exception e) {
			// should throw an exception for not finding notferSure
		}
	}

	@Test
	public void testFindDifferentAnchor() {
		Company company = Utilities.getACompany();
		Company secondCompany = Utilities.getACompany();
		try {
			new NewRole(sVars).setAnchorAndName(company.getAnchor(), "ferSure").sanityRole().add(company.getAnchor());
			new NewRole(sVars).find(secondCompany.getAnchor(), "ferSure");
			fail("found different anchor");
		} catch (Exception e) {
			// should throw an exception for not finding notferSure
		}
	}

	@Test
	public void testRemoveUserFromList() {
		Company company = Utilities.getACompany();
		NewRole role = null;
		User user = null;
		MyObjects users = null;
		try {
			role = new NewRole(sVars).setAnchorAndName(company.getAnchor(), "ferSure").sanityRole()
					.add(company.getAnchor());
			user = new Utilities().getAUser(company.getAnchor());
			role.add(user);
			role.remove(user);
			users = role.listUsers();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		if (!users.isEmpty())
			fail("expected empty users, got " + users.size());
	}

	/**
	 * list all roles for a given anchor
	 */
	@Test
	public void testListAnchors() {
		Company company = Utilities.getACompany();
		ArrayList<NewRole> roles = null;
		try {
			Anchor anchor = company.getAnchor();
			Utilities.getANewRole(anchor);
			Utilities.getANewRole(anchor);
			Utilities.getANewRole(anchor);
			roles = new NewRole(sVars).list(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (roles.size() != 3)
			fail("expected 3, got " + roles.size());

	}

	@Test
	public void testUserClassAccess() {
		// create a role, remove it, check for remaining items.
		Company company = Utilities.getACompany();
		NewRole newRole = null;
		User firstUser = null;
		User secondUser = null;
		try {
			newRole = new NewRole(sVars).setAnchorAndName(company.getAnchor(), "my favorite role")
					.add(company.getAnchor());
			firstUser = new Utilities().getAUser(company.getAnchor());
			newRole.add(firstUser);
			secondUser = new Utilities().getAUser(company.getAnchor());
			newRole.add(secondUser);
			newRole.add(new ClassAccess(sVars).addEditSelectType(anchor, new Level1(sVars), EDITSELECTTYPE.SELECT));
			newRole.add(new ClassAccess(sVars).addEditSelectType(anchor, new Level2(sVars), EDITSELECTTYPE.EDITANDSELECT));
			newRole.add(new ClassAccess(sVars).addEditSelectType(anchor, new Level3(sVars), EDITSELECTTYPE.SELECT));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		NewRole storedRole = null;
		try {
			storedRole = new NewRole(sVars).find(company.getAnchor(), "my favorite role");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		Level1 levelOne = null;
		Level2 levelTwo = null;
		Level3 levelThree = null;
		try {
			levelOne = new Level1(sVars);
			levelTwo = new Level2(sVars);
			levelThree = new Level3(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// user and access line up
		if (!storedRole.canAccess(firstUser, levelOne))
			fail("firstUser, level1, access");
		if (!storedRole.canAccess(secondUser, levelOne))
			fail("firstUser, level1, access");
		if (!storedRole.canAccessAndChange(firstUser, levelTwo))
			fail("firstUser, level2, accessAndChange");
		if (!storedRole.canAccessAndChange(secondUser, levelTwo))
			fail("firstUser, level2, accessAndChange");
		if (!storedRole.canAccess(firstUser, levelThree))
			fail("firstUser, level3, access");
		if (!storedRole.canAccess(secondUser, levelThree))
			fail("firstUser, level3, access");

		// different user
		try {
			if (storedRole.canAccess(new Utilities().getAUser(company.getAnchor()), new Level1(sVars)))
				fail("new User, level1, access");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// different class
		try {
			if (storedRole.canAccess(firstUser, new Level2Brother(sVars)))
				fail("new User, level1, access");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// different access
		try {
			if (storedRole.canAccessAndChange(firstUser, new Level1(sVars)))
				fail("new User, level1, accessAndChange");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testDuplicateRoles() {
		Company company = Utilities.getACompany();
		try {
			new NewRole(sVars).setAnchorAndName(company.getAnchor(), "ferSure").sanityRole().add(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			new NewRole(sVars).setAnchorAndName(company.getAnchor(), "ferSure").sanityRole().add(company.getAnchor());
			fail("allowed duplicate roles");
		} catch (Exception e) {
		}
	}

}
