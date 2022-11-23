package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.forms.SearchTargets;
import com.forms.SmartForm;
import com.security.Anchor;
import com.security.Company;
import com.security.MyObject;
//import com.security.SearchTargets;
import com.security.Role;
import com.security.User;

import comTest.forms.BlankForm;
import comTest.utilities.Utilities;

public class MyObjectTest {

	SearchTargets moa = null;

	SmartForm blankForm = new BlankForm();
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
//		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();
		moa = new SearchTargets(sVars);
		moa.add(new User(sVars));
		moa.add(new Role(sVars));
		// moa.add(new Warehouse(sVars));
		blankForm = new BlankForm();

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getCompanyUsingAnchorTest() {
		Company company = Utilities.getACompany();
		MyObject extractedCompany = null;
		try {
			extractedCompany = company.getInstanceOfAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!(extractedCompany instanceof Company))
			fail("did not get back an instanceof Company");
		if (extractedCompany.id != company.id)
			fail("got back " + extractedCompany.id + ", expected " + company.id);
	}

	@Test
	public void getANameTest() {
		for (MyObject mo : moa.getObjects()) {
			if (mo.getAName() == null)
				fail("null getAName");
			if (mo.getAName().isEmpty())
				fail("empty getAName");
			if (mo instanceof User) {
				if (mo.getAName().equals(User.ANAME))
					continue;
				else
					fail("did not get " + User.ANAME);
			}
			if (mo instanceof Role) {
				if (mo.getAName().equals(Role.ANAME))
					continue;
				else
					fail("did not get " + Role.ANAME);
			}
			// if (mo instanceof Warehouse) {
			// if (mo.getAName().equals(Warehouse.ANAME))
			// continue;
			// else
			// fail("did not get " + Warehouse.ANAME);
			// }
			fail("unknown instance of " + mo.getClass().toString());
		}
	}

	@Test
	public void getNameTest() {
		for (MyObject mo : moa.getObjects()) {
			if (mo.getLogicalName() == null)
				fail("null getNAME");
			if (mo.getLogicalName().isEmpty())
				fail("empty getNAME");
			if (mo instanceof User) {
				if (mo.getLogicalName().equals(User.NAME))
					continue;
				else
					fail("did not get " + User.NAME);
			}
			if (mo instanceof Role) {
				if (mo.getLogicalName().equals(Role.NAME))
					continue;
				else
					fail("did not get " + Role.NAME);
			}
			// if (mo instanceof Warehouse) {
			// if (mo.getNAME().equals(Warehouse.NAME))
			// continue;
			// else
			// fail("did not get " + Warehouse.NAME);
			// }
			fail("unknown instance of " + mo.getClass().toString());
		}
	}

	public void testParentsRole() {
		// create 1 role, 17 users, link them, and see if we get back 17
		// users when we list the roles's parents
		Company company = Utilities.getACompany();
		Anchor anchor = null;
//		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Role role = Utilities.getARole(anchor);
		User user = new Utilities().getAUser(anchor);
		// MyLinkObject mlo = new MyLinkObject(role, user);
		try {
			role.addChild(user);
			// mlo.add(1);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		MyObject parent = null;
		try {
			parent = user.getSingleParent(new Role(sVars));
			// parent = (Role) new Role().find(user.getParent(new Role()).id);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		if (parent.id != role.id)
			fail("did not find parent");
		SearchTargets objs = new SearchTargets(sVars);

		
		try {objs.add(new Role(sVars));
			objs.add(new User(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// try {
		// if (!r.getParent(objs).isEmpty())
		// fail("not empty");
		// } catch (Exception e1) {
		// fail(e1.getLocalizedMessage());
		// }

		for (int i = 0; i < 17; i++) {
			user = new Utilities().getAUser(anchor);
			// RoleToUser rToW = new RoleToUser();
			try {
				// new MyLinkObject(role, user).add(1);
				role.addChild(user);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
		}

		try {
			if (role.listChildren(objs.getObjects()).size() != 17)
				fail("not empty");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	public void testKidsUsers() {
		// create 1 user, 17 roles, link them, and see if we get back 17
		// roles when we list the user's kids
		Company company = Utilities.getACompany();
//		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		User w = new Utilities().getAUser(anchor);
		SearchTargets objs = new SearchTargets(sVars);
		
		try {
			objs.add(w);
			if (!w.listChildren(objs.getObjects()).isEmpty())
				fail("not empty");
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		for (int i = 0; i < 17; i++) {
			Role role = Utilities.getARole(anchor);
			// RoleToUser rToW = new RoleToUser();
			try {
				// new MyLinkObject(role, w).add(1);
				role.addChild(w);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
		}
		try {
			if (w.listChildren(objs.getObjects()).size() != 17)
				fail("not empty");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

//	@Test
//	public void test() {
//		Company company = Utilities.getACompany();
//		Anchor anchor = null;
//		try {
//			anchor = company.getAnchor();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		SearchTargets arr = new SearchTargets(sVars);
//		User parent = new Utilities().getAUser(anchor);
//		Role child = Utilities.getARole(anchor);
//		arr.add(parent);
//		arr.add(child);
//		arr.setListInterface(new FilteredList());
//		// MyLinkObject parentToChild = new MyLinkObject(parent, child);
//		try {
//			parent.addChild(child);
//			// parentToChild.add(1);
//		} catch (Exception e1) {
//			fail(e1.getLocalizedMessage());
//		}
//
//		// child.getChildEditor(new BlankForm(), arr, 0);
//
//		SessionVars sVars = new SessionVars();
//		SmartForm sf = new BlankForm();
//
//		Everyone everyone = new Everyone();
//		everyone.put(child);
//		everyone.put(parent);
//
//		// MyObject lastObject = null;
//		int index = 0;
//		for (MyObject obj : arr.getObjects()) {
//
//			// verify that the editors are set up
//			SmartForm callback=null;
//			try {
//				callback = obj.getEditor(new BaseEditParams(sf, arr,
//						index, everyone));
//			} catch (Exception e3) {
//				fail(e3.getLocalizedMessage());
//			}
//			try {
//				callback.getForm(sVars);
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
//
//			try {
//				callback.setNewState(INSTATE.STANDARDFORM);
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
//			try {
//				callback.extractParams(sVars);
//			} catch (Exception e2) {
//				fail(e2.getLocalizedMessage());
//			}
//			// if (lastObject != null && arr.canHaveChildren(lastObject)) {
//			// MyLinkObject link = new MyLinkObject(lastObject, obj);
//			// try {
//			// link.add(1);
//			// } catch (Exception e1) {
//			// fail(e1.getLocalizedMessage());
//			// }
//
//			// callback = obj.getChildEditor(callback, arr, 0);
//			// fail("childEditor not set up in obj " +
//			// obj.getInstanceName());
//			// callback.getForm(sVars);
//			// callback.extractParams(sVars);
//			// }
//
//			// verify strings
//			if (obj.getAName() == null || obj.getAName().isEmpty())
//				fail("null or empty");
//			try {
//				obj.setInstanceName("dkdk");
//			} catch (Exception e1) {
//				fail(e1.getLocalizedMessage());
//			}
//			if (obj.getInstanceName() == null)
//				fail("null");
//			if (obj.getInstanceName().isEmpty())
//				fail("empty");
//			if (!obj.getInstanceName().equals("dkdk"))
//				fail("name not set");
//
//			// verify getNew does not return a null
//			MyObject myObj=null;
//			try {
//				myObj = obj.getNew();
//			} catch (Exception e1) {
//				fail(e1.getLocalizedMessage());
//			}
//			if (myObj == null)
//				fail("null myObj");
//			try {
//				myObj.find(393);
//				fail("should not have found target");
//			} catch (Exception e) {
//			}
//
//			if (arr.canHaveChildren(obj))
//				try {
//					obj.listChildren(arr.getObjects());
//				} catch (Exception e) {
//					fail(e.getLocalizedMessage());
//				}
//			if (arr.canHaveParent(obj))
//				try {
//					obj.getParents(arr.get(index - 1).obj);
//				} catch (Exception e) {
//					fail(e.getLocalizedMessage());
//				}
//			try {
//				obj.listAll();
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
//			// lastObject = obj;
//			index++;
//		}
//	}

	// Create two instances with the same anchor. Verify that the second
	// instance get the same anchor as the first.
	@Test
	public void testAddAnchorSelf() {
		Level1 firstLevelOne = null;
		Anchor anchor = null;
		Level1 secondLevelOne = null;
		try {
			firstLevelOne = new Level1(sVars);
			anchor = new Anchor(sVars);
			firstLevelOne.add(anchor);
			secondLevelOne = new Level1(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// Create another levelOne with the same anchor. the anchor of the first
		// levelOne should be the same anchor as the second levelOne.

		
		try {
			secondLevelOne.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!firstLevelOne.getAnchor().equals(secondLevelOne.getAnchor()))
				fail("Anchors do not match.");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
