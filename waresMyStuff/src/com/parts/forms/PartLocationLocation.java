package com.parts.forms;

import com.db.SessionVars;
import com.forms.EndOfInputException;
import com.forms.FormsArray;
import com.forms.Utils;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.security.ExceptionCoding;
import com.security.SelectForm;

public class PartLocationLocation extends SelectForm {

	

	public PartLocationLocation(com.forms.FormsMatrixDynamic fmd, SessionVars sVars) throws Exception {
		super(fmd, sVars);
		if (fmd.column + 3 > fmd.get(fmd.row).size())
			throw new ExceptionCoding("not enough space in the row for Part, Location, Location");
		if (fmd.get(fmd.row).get(fmd.column).obj.getLogicalName().equals(Part.getSimpleClassNameStatic()))
			throw new ExceptionCoding("first object not a part");
		if (fmd.get(fmd.row).get(fmd.column + 1).obj.getLogicalName().equals(Location.getSimpleClassNameStatic()))
			throw new ExceptionCoding("second object not a location");
		if (fmd.get(fmd.row).get(fmd.column + 2).obj.getLogicalName().equals(Location.getSimpleClassNameStatic()))
			throw new ExceptionCoding("third object not a location");

		partRow = fmd.row;
		partColumn = fmd.column;
		part = (Part) fmd.get(partRow).get(partColumn).obj;
		sourceLocation = (Location) fmd.get(partRow).get(partColumn + 1).obj;
		destinationLocation = (Location) fmd.get(partRow).get(partColumn + 2).obj;
	}

	int partRow, partColumn = -1;
	Part part = null;
	Location sourceLocation, destinationLocation = null;
	public final String MOVEITEMBUTTON = Utils.getNextString();
	public final String MOVECONTENTSBUTTON = Utils.getNextString();
	public final String MOVETONEWPARENTBUTTON = Utils.getNextString();

	@Override
	public FormsArray getForm(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();

		super.getForm(sVars);
		if (part.isLoaded() && sourceLocation.isLoaded() && destinationLocation.isLoaded()
				&& part.linkToChildExists(sourceLocation)) {
			ret.submitButton("Move '" + part.getInstanceName() + "' from '" + sourceLocation.getInstanceName()
					+ "' to '" + destinationLocation.getInstanceName() + "'", MOVEITEMBUTTON);
		}
		if (sourceLocation.isLoaded() && destinationLocation.isLoaded()) {
			ret.submitButton("Move the contents of '" + sourceLocation.getInstanceName() + "' to '"
					+ destinationLocation.getInstanceName() + "'", MOVECONTENTSBUTTON);
		}
		if (sourceLocation.isLoaded() && destinationLocation.isLoaded()
				&& !destinationLocation.childExists(sourceLocation)) {
			ret.submitButton("Make '" + sourceLocation.getInstanceName() + "' a child of '"
					+ destinationLocation.getInstanceName() + "'", MOVETONEWPARENTBUTTON);
		}
		return ret;
	}
	@Override
	public FormsArray extractParams(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();

		ret.addAll(super.extractParams(sVars));
		if (sVars.hasParameterKey(MOVEITEMBUTTON)) {
//			ret.addAll(updateTheCaller(sVars, target));
//			MyObject item = objs.get(objs.findIndexOfObject(new Part(sVars))).obj;
//			MyObject sourceLocation = objs.get(objs.findIndexOfObject(new Location(sVars))).obj;
			part.reChild(sourceLocation, destinationLocation);
			throw new EndOfInputException(ret);
		}

		if (sVars.hasParameterKey(MOVECONTENTSBUTTON)) {
//			ret.addAll(updateTheCaller(sVars, target));
//			Location destinationLocation = (Location) target;
//			Location sourceLocation = (Location) objs.get(objs.findIndexOfObject(new Location(sVars))).obj;
			sourceLocation.moveChildrenOfThisParentToNewParent(destinationLocation, new Part(sVars));
//			destinationLocation.moveLocationContents(sourceLocation);
			throw new EndOfInputException(ret);
		}

		if (sVars.hasParameterKey(MOVETONEWPARENTBUTTON)) {
//			ret.addAll(updateTheCaller(sVars, target));
//			Location destinationLocation = (Location) target;
//			MyObject sourceLocation = objs.get(index - 1).obj;
			sourceLocation.moveToNewParentRecursive(destinationLocation);
			throw new EndOfInputException(ret);
		}

		return ret;
}}
