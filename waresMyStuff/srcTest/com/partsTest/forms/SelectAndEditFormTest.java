package com.partsTest.forms;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.FormsArray;
import com.forms.FormsMatrixDynamic;
import com.forms.SearchTarget;
import com.forms.SearchTargets;
import com.forms.SelectAndEditForm;
import com.parts.exhibit.Kit;
import com.parts.exhibit.Vendor;
import com.parts.inOut.Attributes;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.parts.warehouse.Warehouse;
import com.partsTest.Utilities.Utilities;
import com.security.Company;

import comTest.security.Level1;

public class SelectAndEditFormTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		if (sVars==null)
		sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testResetForm() {

//		FormsMatrix row = new FormsMatrix();
		FormsMatrixDynamic fmd = null;
		try {
			fmd = new FormsMatrixDynamic(sVars);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		Company company = Utilities.getACompany();
		// exhibit, assembly, item, vendor row
		SearchTargets columns = new SearchTargets(sVars);
		Kit exhibit = null;
		// Assembly assembly = new Assembly();
		Part member = null;
		Vendor vendor = null;
		Warehouse warehouse = null;
		Attributes family = null;
//		TimeReceived item = null;
		Location location = null;
		try {
			exhibit = new Kit(sVars);
			member = new Part(sVars);
			vendor = new Vendor(sVars);
			location = new Location(sVars);
			columns.add(company, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			columns.add(exhibit, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			// columns.add(assembly);
			columns.add(member, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			columns.add(vendor, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

//		columns.setListInterface(new FilteredList());
		fmd.add(columns);

		// assembly, item row
		// columns = new MyObjectsArray();
		// columns.add(assembly);
		// columns.add(member);
		// row.add(columns);

		// warehouse family, member, item
		columns = new SearchTargets(sVars);

		try {
			warehouse = new Warehouse(sVars);
			family = new Attributes(sVars);
			columns.add(company, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			columns.add(warehouse, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			columns.add(family, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			columns.add(member, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
			columns.add(location, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// Member member = new Member();

//		try {
//			item = new TimeReceived();location = new Location(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		columns.add(item, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);

//		columns.setListInterface(new FilteredList());
		fmd.add(columns);

//		Everyone everyone = new Everyone();
//		everyone.put(company);
//		everyone.put(warehouse);
//		everyone.put(family);
//		everyone.put(member);
////		everyone.put(item);
//		everyone.put(location);
		// everyone.put(Company.class, company);
		// everyone.put(Role.class, role);
		// everyone.put(User.class, user);

		SelectAndEditForm sf = null;
		try {
			sf = new SelectAndEditForm(new SessionVars(), fmd);
			sf.getForm(new SessionVars());
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace()) {
				System.out.println(ste.toString());
			}
			fail(e.getLocalizedMessage());
		}
//		try {
//			sf.extractParams(new SessionVars());
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
	}

//	@Test
	public void testResetFormMinimal() {

		FormsMatrixDynamic fmd = null;
		try {
			fmd = new FormsMatrixDynamic(sVars);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		SearchTargets objs = new SearchTargets(sVars);
		// objs.add(new Assembly());
		fmd.add(objs);
		SelectAndEditForm sf = null;
		FormsArray ret = null;
		try {
			sf = new SelectAndEditForm(new SessionVars(), fmd);
			ret = sf.getForm(new SessionVars());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		ret.standardForm();
		// System.out.println(ret.generateHTML());
//		try {
//			sf.extractParams(new SessionVars());
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
	}

	@Test
	public void testNeither() {
		FormsMatrixDynamic fmd = null;
		SearchTargets objs = new SearchTargets(sVars);
		try {
			fmd = new FormsMatrixDynamic(sVars);
			objs.add(new Level1(sVars), SearchTarget.EDITSELECTTYPE.NEITHER);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		fmd.add(objs);
		// set the one and only
		fmd.get(0).get(0).objectSelectedLastTime = true;

		SelectAndEditForm sf = null;
		try {
			sf = new SelectAndEditForm(new SessionVars(), fmd);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		FormsArray ret = null;
		try {
			ret = sf.getForm(new SessionVars());
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace()) {
				System.out.println(ste.toString());
			}
			System.out.println();
			fail(e.getLocalizedMessage());
		}
		if (ret.body.contains("<form action"))
			fail("body should be empty. " + ret.body);
	}

//	@Test
//	public void testSelect() {
//		FormsMatrix row = new FormsMatrix();
//		SearchTargets objs = new SearchTargets(sVars);
//		Level1 levelOne=null;
//		try {
//			levelOne = new Level1(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		objs.add(levelOne, SearchTarget.EDITSELECTTYPE.SELECT);
//		row.add(objs);
//		SelectAndEditForm sf = null;
//		FormsArray ret = null;
//		try {sf = new SelectAndEditForm(new SessionVars(), row, new Everyone());
//			ret = sf.getForm(new SessionVars());
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (!ret.body.contains("Search for a " + levelOne.getInstanceName()))
//			fail("body should be empty. " + ret.body);
//	}

//	@Test
//	public void testEdit() {
//		FormsMatrix row = new FormsMatrix();
//		SearchTargets objs = new SearchTargets(sVars);
//		Level1 levelOne = new Level1(sVars);
//		objs.add(levelOne, SearchTarget.EDITSELECTTYPE.EDIT);
//		row.add(objs);
//		SelectAndEditForm sf = new SelectAndEditForm(new SessionVars(), row,
//				new Everyone());
//		FormsArray ret = null;
//		try {
//			ret = sf.getForm(new SessionVars());
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (!ret.body.contains("Add a new " + levelOne.getInstanceName()))
//			fail("body should be empty. " + ret.body);
//	}

	// create an edit form, extract the named input, create a simulated response
	// to the input for extractParams, and see if the new Level1 was created
//	@Test
//	public void testAddLevel1() {
//		FormsMatrix row = new FormsMatrix();
//		SearchTargets objs = new SearchTargets(sVars);
//		Level2 anchorObject = null;
//		try {anchorObject = new Level2(sVars);
//			anchorObject.add(new Anchor(sVars));
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		Level1 levelOne=null;
//		try {
//			levelOne = new Level1(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		String levelOneClass = levelOne.getFileName();
//		objs.add(anchorObject, SearchTarget.EDITSELECTTYPE.NEITHER);
//		objs.add(levelOne, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
//		row.add(objs);
//		SelectAndEditForm sf=null;
//		try {
//			sf = new SelectAndEditForm(new SessionVars(), row, new Everyone());
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		SessionVars sVars = new SessionVars();
//		FormsArray ret = null;
//		try {
//			ret = sf.getForm(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		if (!ret.body.contains("Add a new " + levelOneClass))
//			fail("body should contain 'Add a new " + levelOneClass + "'. Contains '" + ret.body + "'");
//		// get the name of the text box for adding a new Level1
//		// System.out.println("ret.body " + ret.body);
//		// System.out.println("leveOneClass " + levelOneClass);
//		String lookingFor = "<tr><td>" + levelOneClass + " name</td><td><input type=\"text\" name=";
//		// System.out.println("lookingFor " + lookingFor);
//		int index = ret.body.indexOf(lookingFor);
//		// System.out.println("index: " + index);
//		// if (index >= 0)
//		// for (int i = index + lookingFor.length(); i < ret.body.length(); i++)
//		// System.out.print(ret.body.charAt(i));
//		// System.out.println();
//		String containsString = (String) ret.body.subSequence(index + lookingFor.length(), ret.body.length());
//		String quoted = containsString.split(" ")[0];
//		// System.out.println("containsString " + containsString);
//		// System.out.println("quoted " + quoted);
//		String unquoted = quoted.split("\"")[1];
//		// System.out.println("unquoted " + unquoted);
//		String[] params = { "new level1 name" };
//		sVars.parameterMap.put(unquoted, params);
//
//		// add the new level1 the hard way
//		try {
//			sf.extractParams(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		Level1 newLevelOne = null;
//		try {newLevelOne = new Level1(sVars);
//			newLevelOne.find("new level1 name");
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//	}

	// create a select form with a parent and child, see if the child shows up
	// on the search list
//	@Test
//	public void testSelectLevel1() {
//		FormsMatrix row = new FormsMatrix();
//		SearchTargets objs = new SearchTargets(sVars);
//		Level1 firstLevelOne = null;
//		try {firstLevelOne = new Level1(sVars);
//			firstLevelOne.add(new Anchor(sVars));
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		// top element must be selected for updateFilteredList to work
//		objs.add(firstLevelOne, SearchTarget.EDITSELECTTYPE.NEITHER);
//		Level2 levelTwo = null;
//		// create a child of firstLevelOne
//		try {levelTwo = new Level2(sVars);
//			levelTwo.add(firstLevelOne);
//			firstLevelOne.addChild(levelTwo, 3);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		String levelTwoName = levelTwo.getInstanceName();
//
//		// levelTwo = new Level2(sVars);
//		// try {
//		// levelTwo.add(firstLevelOne);
//		// firstLevelOne.addChild(levelTwo, 6);
//		// } catch (Exception e) {
//		// fail(e.getLocalizedMessage());
//		// }
//		// System.out.println("level two name is " + levelTwoName);
//		// add a non-selected level2
//		try {
//			objs.add(new Level2(sVars), SearchTarget.EDITSELECTTYPE.SELECT);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		// objs.anchorObject = firstLevelOne;
//		try {
//			objs.updateFilteredList();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		row.add(objs);
//		SelectAndEditForm sf=null;
//		try {
//			sf = new SelectAndEditForm(new SessionVars(), row, new Everyone());
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		SessionVars sVars = new SessionVars();
//		FormsArray ret = null;
//		try {
//			ret = sf.getForm(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		// System.out.println("ret.body " + ret.body);
//		// <tr><td><select size="5" name="g55" ><option value="1" >Level2
//		// g7</option></select></td><td><input type="submit" name="g61"
//		String firstLook = "<tr><td><select size=\"" + SelectIdFromList.DISPLAYSIZE + "\" name=";
//		int firstIndex = ret.body.indexOf(firstLook);
//		if (firstIndex < 0)
//			fail("did not find " + firstLook);
//		String valueName = ((String) ret.body.subSequence(firstIndex + firstLook.length(), ret.body.length()))
//				.split("\"")[1];
//		// System.out.println("selectTableName " + selectTableName);
//		String lookingFor = levelTwoName + "</option></select></td><td><input type=\"submit\" name=";
//		// System.out.println("lookingFor " + lookingFor);
//		int index = ret.body.indexOf(lookingFor);
//		// System.out.println("index: " + index);
//		if (index < 0)
//			fail(lookingFor + " not found");
//		String containsString = (String) ret.body.subSequence(index + lookingFor.length(), ret.body.length());
//		// extract the quoted value g55
//
//		String tableName = containsString.split("\"")[1];
//		// System.out.println("unquoted " + unquoted);
//		String[] params = { "" + levelTwo.id };
//		String[] noParams = {};
//		sVars.parameterMap.put(tableName, noParams);
//		sVars.parameterMap.put(valueName, params);
//
//		// add the new level1 the hard way
//		try {
//			sf.extractParams(sVars);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		// see if the levelTwo is selected
//		if (!objs.get(1).obj.isLoaded())
//			fail("not selected");
//	}
}
