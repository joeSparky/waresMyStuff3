package com.parts.inOut;

import java.lang.invoke.MethodHandles;
import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.Strings;
import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;
import com.parts.exhibit.Kit;
//import com.parts.exhibit.TimeReceived;
import com.parts.exhibit.PartNumber;
import com.parts.forms.PartLocationLocation;
import com.parts.location.Location;
import com.parts.security.PartLink;
import com.security.MyLinkObject;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class Part extends MyObject {

	public static final String NAME = "Part";
	public static final String ANAME = "A Part";
	public int minQuant;

	@Override
	public void clear() {
		minQuant = 0;
		super.clear();
	}

	public Part(SessionVars sVars) throws Exception {
		super(sVars);
		clear();
		new Table().tableCreated(this, sVars);
	}

	public boolean memberLoaded() {
		if (id > 0)
			return true;
		return false;
	}

	public static boolean needsNewTable = false;

	@Override
	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		ret.add(new DoubleString("minQuant", "" + minQuant));
		return ret;
	}

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add("`minQuant` int(11) NOT NULL default '0'");
		return ret;
	}

	@Override
	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	/**
	 * returns a string for sorting the pick order of a package.
	 * 
	 * @return a string for sorting the pick order of a package
	 * @throws Exception
	 */

	@Override
	public String getAName() {
		return Part.ANAME;
	}

	@Override
	public String getLogicalName() {
		return Part.NAME;
	}

	@Override
	public MyObject getNew() throws Exception {
		return new Part(sVars);
	}

	@Override
	public int hashCodeReminder() {
		return 0;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equalsReminder(Object obj) {
		return false;
	}

	@Override
	public boolean equals(Object exhibit) {
		if (exhibit instanceof Part) {
			return id == ((Part) exhibit).id;
		}
		return false;
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Part;
	}

	public static String getSimpleClassNameStatic() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Attributes(sVars));
		objs.add(new Kit(sVars));
		return objs;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Location(sVars));
		objs.add(new PartNumber(sVars));
		return objs;
	}

	public void addChild(MyObject obj, int inOutQuantity) throws Exception {
		PartLink pl = null;
		pl = new PartLink(this, obj, sVars);
		try {
			pl.find();
			throw new Exception(obj.getInstanceName() + " is already a child of " + this.getInstanceName());
		} catch (Exception e) {

		}
		pl.setLinkQuantity(inOutQuantity);
		pl.add();
//		pl.updateSetQuantity(inOutQuantity, false);
	}

	public MyLinkObject getMyLinkObject(MyObject child) throws Exception {
		if (child instanceof Location)
//			try {
			return new PartLink(this, child, sVars);
//			} catch (Exception e) {
//				Internals.dumpExceptionExit(e);
//			}
		else
//			try {
			return new MyLinkObject(this, child, sVars);
//			} catch (Exception e) {
//				Internals.dumpExceptionExit(e);
//			}
//		return null;
	}

//	@Override
//	public AttachToForm getAttachToForms(FormsMatrixDynamic fmd) throws Exception {
//		if (fmd.getObject() instanceof Location) {
//			RowColumn rc = fmd.findInstanceOf(this);
//			return new AttachToFormPartLocation(fmd.get(rc.row).get(rc.column).selector, fmd, rc.row, rc.column,
//					fmd.row, fmd.column);
//		} else
//			return super.getAttachToForms(fmd);
//	}

	@Override
	public boolean hasInventoryLinkWith(MyObject child) {
		return (child instanceof Location);
	}

	@Override
	/**
	 * assign selectors for a part without a following location in fmd, a part with
	 * a following location, or a part followed by 2 locations
	 */
	public SmartForm getSelector(FormsMatrixDynamic fmd, SessionVars sVars) throws Exception {
		// if there are two objects below this one
		if (fmd.column < fmd.get(fmd.row).size() - 2) {
			// if the next two instances are locations
			if (fmd.get(fmd.row).get(fmd.column + 1).obj.isSameClass(new Location(sVars))
					&& fmd.get(fmd.row).get(fmd.column + 2).obj.isSameClass(new Location(sVars))) {
				return new PartLocationLocation(fmd, sVars);
			}
		}
		return super.getSelector(fmd, sVars);
	}
}
