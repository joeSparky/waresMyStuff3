package com.parts.forms;

import com.db.SessionVars;
import com.forms.FormsArray;
import com.forms.FormsMatrixDynamic;
import com.forms.SearchTarget;
//import com.forms.SearchTargets;

import com.forms.SelectAndEditForm;
import com.forms.SmartForm;
import com.forms.Utils;
import com.parts.exhibit.Kit;
import com.parts.exhibit.PartNumber;
import com.parts.exhibit.Vendor;
import com.parts.inOut.Attributes;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.parts.security.PartLink;
import com.parts.warehouse.Warehouse;
import com.parts.warehouse.WarehouseSelectCSV;
import com.security.Anchor;
import com.security.ClassAccess;
import com.security.Company;
import com.security.ExceptionCoding;
import com.security.MyObject;
import com.security.NewRole;
import com.security.User;

public class MainPartsForm extends SmartForm {
	/**
	 * top of forms heap
	 */
//	protected MainPartsForm() {
//		super(null);
//	}

	SelectAndEditForm se = null;
//	FormsMatrix arrayListObjs = new FormsMatrix();
	// create a FormsMatrixDynamic
	
	
	private static String MYRETURN = Utils.getNextString();
	SessionVars sVars = null;

	public MainPartsForm(SessionVars sVars) throws Exception {
		super(new FormsMatrixDynamic(sVars), sVars);
		this.sVars = sVars;
//		fmd = new FormsMatrixDynamic();
//		sVars.setApToRun(MYRETURN, this);

		// create a partLink table so that part->location link is created correctly
		new PartLink(new Part(sVars), new Location(sVars), sVars);

		// get the user information out of the session
		User user = new User(sVars);
//		Internals.dumpStringContinue("new User(sVars)");

		MyObject anchorObj = null;
		NewRole role = null;
//		try {
//		System.out.println("before user.find");
		user.find(sVars.getUserNumber());
//		Internals.dumpStringContinue("after user.find");
		anchorObj = user.getInstanceOfAnchor();
//		Internals.dumpStringContinue("anchor");
		// if we didn't get the company (probably a warehouse), go up one
		// level
		if (!(anchorObj instanceof Company))
			anchorObj = anchorObj.getInstanceOfAnchor();
		role = makeUpARole(user.getAnchor(), user);
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
//		Internals.dumpStringContinue("ok");
		if (!(anchorObj instanceof Company))
			throw new ExceptionCoding("did not find a company anchor");

		/**
		 * use hidden company to filter objects below it. For example, a warehouse is
		 * tied to a company, but we don't want the user changing the company, so hide
		 * it.
		 */
		Company hiddenCompany = (Company) anchorObj;
//		Internals.dumpStringContinue("ok");
		// if this is the default user (has full access to everything)
//		try {
		if (!user.isDefaultUser())
			// must use assigned company
//				try {
			hiddenCompany = (Company) user.getSingleParent(new Company(sVars));
//				} catch (Exception e) {
//					Internals.dumpExceptionExit(e);
//				}
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
//		arrayListObjs = new FormsMatrix();

		// defining stuff, putting stuff into the warehouse, getting stuff out
		// of the warehouse
		Warehouse warehouse = new Warehouse(sVars);

		// warehouse.anchor = true;
		Attributes attributes = new Attributes(sVars);
		Part part = new Part(sVars);
		Location location = new Location(sVars);
		// moving stuff between locations
		Location destinationLocation = new Location(sVars);

		/////////// main row
		SearchTargets objs = new SearchTargets(sVars);
		// force an update of the when selectAndEditForm does a getForm
		objs.add(hiddenCompany, role.getEditSelectType(user, hiddenCompany));
		objs.add(warehouse, role.getEditSelectType(user, warehouse));
		objs.add(attributes, role.getEditSelectType(user, attributes));
		objs.add(part, role.getEditSelectType(user, part));
		// need editAndSelect to create an item time stamp
//		objs.add(item, role.getEditSelectType(user, item));
		objs.add(location, role.getEditSelectType(user, location));
		objs.add(destinationLocation, role.getEditSelectType(user, destinationLocation));
		fmd.add(objs);

//		se = new SelectAndEditForm(sVars, arrayListObjs);

		////////// kit row
//		Kit kit = new Kit(sVars);
//		KitDestination kitDestination = new KitDestination(sVars);
//		Vendor vendor = new Vendor(sVars);
//		PartNumber partNumber = new PartNumber(sVars);
//		objs = new SearchTargets(sVars);
//		objs.add(hiddenCompany, role.getEditSelectType(user, hiddenCompany));
//		objs.add(warehouse, role.getEditSelectType(user, warehouse));
//		objs.add(vendor, role.getEditSelectType(user, vendor));
//		objs.add(partNumber, role.getEditSelectType(user, partNumber));
//		objs.add(part, role.getEditSelectType(user, part));
//		objs.add(kit, role.getEditSelectType(user, kit));
//		objs.add(kitDestination, role.getEditSelectType(user, kitDestination));
//		fmd.add(objs);

		/////////// user and role row
		objs = new SearchTargets(sVars);
		objs.add(hiddenCompany, role.getEditSelectType(user, hiddenCompany));
		objs.add(warehouse, role.getEditSelectType(user, warehouse));
		objs.add(role, role.getEditSelectType(user, role));
		objs.add(user, role.getEditSelectType(user, user));
		fmd.add(objs);

		//////// reports row
		if (user.isDefaultUser()) {
			// add what will only be seen by the default user
			objs = new SearchTargets(sVars);
			objs.add(hiddenCompany, role.getEditSelectType(user, hiddenCompany));
			objs.add(warehouse, role.getEditSelectType(user, warehouse));
			objs.add(user, role.getEditSelectType(user, user));
			fmd.add(objs);
		}
		WarehouseSelectCSV wSelect = new WarehouseSelectCSV(sVars);
		// dump comma separated value inventory
		objs = new SearchTargets(sVars);
		objs.add(hiddenCompany, role.getEditSelectType(user, hiddenCompany));
		objs.add(warehouse, role.getEditSelectType(user, warehouse));
		objs.add(wSelect, role.getEditSelectType(user, warehouse));
		fmd.add(objs);

		// save the form for getForm
		se = new SelectAndEditForm(sVars, fmd);
	}

	@Override
	public FormsArray getForm(SessionVars sVars) throws Exception {
		return se.getForm(sVars);
	}

	/**
	 * for testing
	 * @return
	 */
//	public FormsMatrixDynamic getFormsMatrixDynamic() {
//		return fmd;
//	}

	@Override
	public FormsArray extractParams(SessionVars sVars) throws Exception {
		return se.extractParams(sVars);
	}

	protected NewRole makeUpARole(Anchor anchor, User user) throws Exception {
		NewRole ret = new NewRole(sVars);
//		boolean found = false;
		try {
			ret.find(anchor, "made up role");
			return ret;
		} catch (Exception e1) {
		}
//		if (found) {
//			// already exists
//			// put a warehouse in the stringToClass array
//			new Warehouse(sVars);
//			ret.deleteUnconditionally();
//		}

		Company company = null;
		MyObject companyObj = null;
//		try {
		companyObj = user.getInstanceOfAnchor();
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
		if (!(companyObj instanceof Company))
			throw new ExceptionCoding("did not find a company anchor");
		company = (Company) companyObj;
//		try {
		ret.setAnchorAndName(company.getAnchor(), "made up role").sanityRole().add(company);
		ret.add(user);
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new Company(sVars), SearchTarget.EDITSELECTTYPE.NEITHER));
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new Warehouse(sVars),
				SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new Attributes(sVars),
				SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new Part(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
//			ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new TimeReceived(), SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new Location(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new Kit(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new Vendor(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new PartNumber(sVars),
				SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new NewRole(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
		ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new User(sVars), SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
//			ret.add(new ClassAccess(sVars).addEditSelectType(anchor, new ImportCDMObject(), SearchTarget.EDITSELECTTYPE.EDITANDSELECT));
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}

		return ret;
	}
	@Override
	public String getReturnTo() {
		return MYRETURN;
	}
}
