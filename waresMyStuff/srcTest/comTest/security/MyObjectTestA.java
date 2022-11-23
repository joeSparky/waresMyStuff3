package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.forms.SearchTargets;
import com.security.Company;
import com.security.MyObject;
//import com.security.SearchTargets;
import com.security.Role;
import com.security.User;

import comTest.utilities.Utilities;

public class MyObjectTestA {

	public SearchTargets moa = null;
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		setUpMoa();
		if (utilities == null)
			fail("utilities is null");
	}

	// utilities for generating instances
	protected Utilities utilities = null;

	public void setUpMoa() throws Exception {
		
		new Utilities().allNewTables(sVars);
//		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();
		// Permissions.initLinks();
		// Role.initLinks();
		// RoleToPermissions.initLinks();
		// User.initLinks();
		// Company.initLinks();
		moa = new SearchTargets(sVars);
		try {
			moa.add(new User(sVars));
			moa.add(new Role(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// moa.add(new Warehouse(sVars));
		utilities = new Utilities();
	}

	@After
	public void tearDown() throws Exception {
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
			// fail("unknown instance of " + mo.getClass().toString());
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
			// fail("unknown instance of " + mo.getClass().toString());
		}
	}

	@Test
	public void getFullListTest() {
		Company company = Utilities.getACompany();
		// Anchor anchor = null;
		// try {
		// anchor = new Anchor(sVars).find(company);
		// } catch (AnchorNotFoundException e1) {
		// fail(e1.getLocalizedMessage());
		// }
		int copies = 5;
		for (MyObject mo : moa.getObjects()) {
			// if (mo instanceof User)
			// if (mo.listAll().size() != 1)
			// fail(mo.getClass().toString() + " should have one entry");
			// else
			// continue;
			// if (mo instanceof Role)
			// if (mo.listAll().size() != 1)
			// fail(mo.getClass().toString() + " should have one entry");
			// else
			// continue;
			// all others, create an instance and check this full list size
			// int mySize = utilities.getAnObject(mo).listAll().size();
			try {
				if (mo instanceof User) {
					if (mo.listAll().size() != 1)
						fail("expected default user");
				} else if (!mo.listAll().isEmpty())
					fail(mo.getClass().toString() + " should have one entry. Have " + mo.listAll().size());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
		}
		// add 5 to each
		for (MyObject mo : moa.getObjects()) {
			for (int i = 0; i < copies; i++)
				try {
					utilities.getAnObject(mo, company.getAnchor());
				} catch (Exception e) {
					fail(e.getLocalizedMessage());
				}
		}
		for (MyObject mo : moa.getObjects()) {
			if (handledFullListSize(mo, copies))
				continue;
			// all others, create an instance and check this full list size
			// original, copies more, and then this new one
			try {
				if (mo instanceof User) {
					if (mo.listAll().size() != copies + 1)
						fail(mo.getClass().toString() + " should have " + (copies + 1) + " entries, found "
								+ mo.listAll().size() + " for object " + mo.getLogicalName());
				} else if (mo.listAll().size() != copies)
					fail(mo.getClass().toString() + " should have " + copies + " entries, found " + mo.listAll().size()
							+ " for object " + mo.getLogicalName());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
		}
	}

	protected boolean handledFullListSize(MyObject mo, int copies) {
		return false;
	}

	@Test
	public void findTest() {
		for (MyObject mo : moa.getObjects()) {
			try {
				mo.find(38388);
				fail("should not have found a " + mo.getAName());
			} catch (Exception e) {
			}
		}
	}

	// @Test
	// public void testParentsWarehouse() {
	// // create 1 warehouse, 17 roles, link them, and see if we get back 17
	// // roles when we list the warehouse's parents
	// // Warehouse w = Utilities.getAWarehouse();
	// Role role = new Role();
	// SearchTargets objs = new SearchTargets(sVars);
	// objs.add(role);
	// // objs.add(w);
	//
	// // try {
	// // if (!w.listParents(objs).isEmpty())
	// // fail("not empty");
	// // } catch (Exception e1) {
	// // fail(e1.getLocalizedMessage());
	// // }
	//
	// for (int i = 0; i < 17; i++) {
	// role = new Utilities().loadAnother(role);
	// MyLinkObject rToW = new MyLinkObject(role, w);
	// try {
	// rToW.add(1);
	// } catch (Exception e) {
	// fail(e.getLocalizedMessage());
	// }
	// }
	// try {
	// if (w.listParents(objs).size() != 17)
	// fail("got back " + w.listParents(objs).size());
	// } catch (Exception e) {
	// fail(e.getLocalizedMessage());
	// }
	// }

	// public void testParentsRole() {
	// // create 1 role, 17 users, link them, and see if we get back 17
	// // users when we list the roles's parents
	// Role r = Utilities.getARole();
	// User u = new Utilities().getAUser();
	// SearchTargets objs = new SearchTargets(sVars);
	// objs.add(new User(sVars));
	// objs.add(r);
	//
	// try {
	// MyObject parent = objs.getParent(r);
	// } catch (Exception e) {
	// fail(e.getLocalizedMessage());
	// }
	//
	// try {
	// MyObject parent = objs.getParent(r);
	// } catch (Exception e) {
	// fail(e.getLocalizedMessage());
	// }

	// for (int i = 0; i < 17; i++) {
	// User user = new Utilities().getAUser();
	// try {
	// new MyLinkObject(user,r).add(1);
	// } catch (Exception e) {
	// fail(e.getLocalizedMessage());
	// }
	// // try {
	// // user.addAChild(r);
	// // } catch (Exception e) {
	// // fail(e.getLocalizedMessage());
	// // }
	// }
	// parent = objs.getParent(r);
	// try {
	// if (r.getParent(objs).size() != 17)
	// fail("not empty");
	// } catch (Exception e) {
	// fail(e.getLocalizedMessage());
	// }
	// }

	public void testKidsUsers() {
		// create 1 user, 17 roles, link them, and see if we get back 17
		// roles when we list the user's kids
		Company company = Utilities.getACompany();
		User w = null;
//		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();
		try {
			w = new Utilities().getAUser(company.getAnchor());
		} catch (Exception e2) {
			fail(e2.getLocalizedMessage());
		}
		SearchTargets objs = new SearchTargets(sVars);

		try {
			objs.add(w);
			if (!w.listChildren(objs.getObjects()).isEmpty())
				fail("not empty");
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		for (int i = 0; i < 17; i++) {
			Role role = null;
			try {
				role = Utilities.getARole(company.getAnchor());
			} catch (Exception e1) {
				fail(e1.getLocalizedMessage());
			}
			// RoleToUser rToW = new RoleToUser();
			try {
				// new MyLinkObject(role, w).add(1);
				role.addChild(w);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
			// try {
			// role.addAChild(w);
			// } catch (Exception e) {
			// fail(e.getLocalizedMessage());
			// }
		}
		try {
			if (w.listChildren(objs.getObjects()).size() != 17)
				fail("not empty");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	public void testKidsRoles() {
		Company company = Utilities.getACompany();
		// create 1 role, 17 warehouses, link them, and see if we get back 17
		// warehouses when we list the roles's kids
		Role r = null;
		try {
			r = Utilities.getARole(company.getAnchor());
		} catch (Exception e2) {
			fail(e2.getLocalizedMessage());
		}
		SearchTargets objs = new SearchTargets(sVars);

		try {
			objs.add(r);
			if (!r.listChildren(objs.getObjects()).isEmpty())
				fail("not empty");
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// for (int i = 0; i < 17; i++) {
		// // Warehouse w = Utilities.getAWarehouse();
		// // RoleToWarehouse rToW = new RoleToWarehouse();
		// try {
		// new MyLinkObject(r, w).add(1);
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
		// }
		try {
			if (r.listChildren(objs.getObjects()).size() != 17)
				fail("not empty");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testLinks() {
		Company company = Utilities.getACompany();
		// Warehouse w = Utilities.getAWarehouse();
		Role role = null;
		try {
			role = Utilities.getARole(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		SearchTargets objs = new SearchTargets(sVars);
		try {
			objs.add(role);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// objs.add(w);
		// MyLinkObject mlo = new MyLinkObject(role, w);
		// try {
		// if (!w.listParents(objs).isEmpty())
		// fail("list should be empty");
		// } catch (Exception e1) {
		// fail(e1.getLocalizedMessage());
		// }

		for (int i = 0; i < 17; i++) {
			try {
				role = new Utilities().loadAnother(role, company.getAnchor());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
			// RoleToWarehouse rToW = new RoleToWarehouse();
			// try {
			// new MyLinkObject(role, w).add(1);
			// } catch (Exception e) {
			// fail(e.getLocalizedMessage());
			// }
		}
		// try {
		// if (w.listParents(objs).size() != 17)
		// fail("list should have 17 entries, got "
		// + w.listParents(objs).size());
		// } catch (Exception e) {
		// fail(e.getLocalizedMessage());
		// }
	}

//	@Test
//	public void test() {
//		Company company = Utilities.getACompany();
//		SearchTargets arr = new SearchTargets(sVars);
//		User user = null;
//		try {
//			user = new Utilities().getAUser(company.getAnchor());
//		} catch (Exception e3) {
//			fail(e3.getLocalizedMessage());
//		}
//		Role role = null;
//		try {
//			role = Utilities.getARole(company.getAnchor());
//		} catch (Exception e3) {
//			fail(e3.getLocalizedMessage());
//		}
//		// Warehouse warehouse = Utilities.getAWarehouse();
//
//		// MyLinkObject uToR = new MyLinkObject(user, role);
//		try {
//			user.addChild(role);
//			// uToR.add(1);
//		} catch (Exception e1) {
//			fail(e1.getLocalizedMessage());
//		}
//		arr.add(user);
//		arr.add(role);
//		arr.setListInterface(new FilteredList());
//		// arr.add(warehouse);
//
//		SessionVars sVars = new SessionVars();
//		int index = 0;
//		MyObject parentObj = null;
//		for (MyObject obj : arr.getObjects()) {
//
//			System.out.println(obj.getAName());
//			Everyone everyone = new Everyone();
//			// everyone.put(Warehouse.class, warehouse);
//			// everyone.put(Family.class, family);
//			// everyone.put(Member.class, member);
//			// everyone.put(Item.class, item);
//			// everyone.put(Location.class, location);
//			// everyone.put(Company.class, company);
//			everyone.put(role);
//			everyone.put(user);
//			BaseEditParams params = new BaseEditParams(new BlankForm(), arr,
//					index, everyone);
//			try {
//				obj.getEditor(params).getForm(sVars);
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
//
//			SmartForm sf = null;
//			try {
//				sf = obj.getEditor(params);
//				sf.setNewState(INSTATE.STANDARDFORM);
//				sf.extractParams(sVars);
//			} catch (Exception e2) {
//				fail(e2.getLocalizedMessage());
//			}
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
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
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
//					obj.getSingleParent(parentObj);
//				} catch (Exception e) {
//					fail(e.getLocalizedMessage());
//				}
//			try {
//				obj.listAll();
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
//			parentObj = obj;
//		}
//
//		index++;
//	}

	@Test
	public void CanHaveChildrenTest() {
		Company company = Utilities.getACompany();
		SearchTargets arr = new SearchTargets(sVars);
		User user = null;
		try {
			user = new Utilities().getAUser(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Role role = null;
		try {
			role = Utilities.getARole(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// there is an object (role) in the array after user so user can have a
		// child.
		try {
			arr.add(user);
			arr.add(role);
			if (!arr.canHaveChildren(user))
				fail("should be able to have children");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// there is no object in the array after role so role can not have a
		// child.
		try {
			if (arr.canHaveChildren(role))
				fail("should not be able to have children");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void CanHaveParentTest() {
		Company company = Utilities.getACompany();
		SearchTargets arr = new SearchTargets(sVars);
		User user = null;
		try {
			user = new Utilities().getAUser(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Role role = null;
		try {
			role = Utilities.getARole(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// there is no object in the array before user so user can not have a
		// parent.
		try {
			arr.add(user);
			arr.add(role);
			if (arr.canHaveParent(user))
				fail("should not be able to have a parent");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// there is an object in the array (user) before role so role can have a
		// parent.
		try {
			if (!arr.canHaveParent(role))
				fail("should be able to have a parent");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
