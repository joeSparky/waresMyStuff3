package com.parts.forms;

import com.db.SessionVars;
import com.forms.AttachToFormPair;
import com.forms.EndOfInputException;
import com.forms.FormsArray;
import com.forms.FormsMatrixDynamic;
import com.forms.Utils;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.parts.security.PartLink;
import com.security.ExceptionCoding;
import com.security.MyLinkObject;

public class AttachToFormPartLocation extends AttachToFormPair {

	String QUANTITY = Utils.getNextString();
	String STOREBUTTON = Utils.getNextString();
	String REMOVEBUTTON = Utils.getNextString();
	String INVENTORYBUTTON = Utils.getNextString();
	String MOVETOSECONDLOCATION = Utils.getNextString();
	SessionVars sVars = null;
//	int locationColumn = -1;
//	Part part = null;
//	Location location = null;

	public AttachToFormPartLocation(FormsMatrixDynamic fmd, SessionVars sVars, SearchTarget partTarget,
			SearchTarget locationTarget) throws Exception {
		super(fmd, sVars, partTarget, locationTarget);
		this.sVars = sVars;
//		this.fmd = fmd;
		/**
		 * part
		 */
		upper = partTarget;
		if (!(upper.obj instanceof Part))
			throw new ExceptionCoding("Part row column is not a part");
		/**
		 * location
		 */
		lower = locationTarget;
		if (!(lower.obj instanceof Location))
			throw new ExceptionCoding("Location row column is not a location");
//		this.locationColumn = locationColumn;
	}

//	FormsMatrixDynamic fmd = null;

	@Override
	public FormsArray getForm() throws Exception {
		FormsArray ret = new FormsArray();
//		ret.addAll(super.getForm());

		if (upper.obj.isLoaded() && lower.obj.isLoaded())
			if (fmd.get(fmd.row).linkedAndLoadedToTheTop(lower.myColumn)) {
				// if there's already a link between the part and the location
				if (upper.obj.childExists(lower.obj)) {
					ret.addAll(removeLink());
					ret.addAll(markAsInventoried());
				} else
					ret.addAll(addLink());
			} else
				ret.addAll(showWhatNeedsToBeDone());
		// offer to move a part from one location to another
		if (upper.obj.isLoaded() && lower.obj.isLoaded()
		// and there's a link between them
				&& upper.obj.linkToChildExists(lower.obj)
				// if there's an object below lower
				&& lower.myColumn < fmd.get(lower.myRow).size() - 1
				// and it's loaded
				&& fmd.get(lower.myRow).get(lower.myColumn + 1).obj.isLoaded()
				// and it's a location
				&& fmd.get(lower.myRow).get(lower.myColumn + 1).obj instanceof Location) {
			ret.submitButton("Move all of '" + upper.obj.getInstanceName() + "' from '" + lower.obj.getInstanceName()
					+ " to " + fmd.get(lower.myRow).get(lower.myColumn + 1).obj.getInstanceName(),
					MOVETOSECONDLOCATION);
		}

		return ret;
	}

	FormsArray showWhatNeedsToBeDone() throws ExceptionCoding, Exception {
		FormsArray ret = new FormsArray();
		// find the unloaded objects above the part
		for (int column = 0; column < upper.myColumn; column++) {
			if (!fmd.get(fmd.row).get(column).obj.isLoaded()) {
				ret.rawText("Please select a " + fmd.get(fmd.row).get(column).obj.getAName());
				return ret;
			}
		}

		for (int column = 0; column < upper.myColumn; column++) {
			if (!new MyLinkObject(fmd.get(fmd.row).get(column).obj, fmd.get(fmd.row).get(column + 1).obj, sVars)
					.linkExists()) {
				ret.rawText("Please create a link between " + fmd.get(fmd.row).get(column).obj.getAName() + " and "
						+ fmd.get(fmd.row).get(column + 1).obj.getAName());
				return ret;
			}
		}
		ret.rawText("should never get to showWhatNeedsToBeDone");
		return ret;
	}

	// the link exists. get the PartLink
	public FormsArray removeLink() throws Exception {
		FormsArray ret = new FormsArray();
		ret.textBox(QUANTITY, 4, "Quantity", "" + inOutQuantity, false, false);
		ret.submitButton("Add '" + upper.obj.getInstanceName() + "' to '" + lower.obj.getInstanceName()
				+ "', current quantity:" + new PartLink(upper.obj, lower.obj, sVars).find().getLinkQuantity(), STOREBUTTON);
		ret.submitButton(
				"Remove '" + upper.obj.getInstanceName() + "' from '" + upper.obj.getInstanceName()
						+ "', current quantity:" + new PartLink(upper.obj, lower.obj, sVars).find().getLinkQuantity(),
				REMOVEBUTTON);
		return ret;
	}

	public FormsArray addLink() throws Exception {
		FormsArray ret = new FormsArray();
		ret.textBox(QUANTITY, 4, "Quantity", "" + inOutQuantity, false, false);
		ret.submitButton("Store '" + upper.obj.getInstanceName() + "' at '" + lower.obj.getInstanceName() + "'",
				STOREBUTTON);
		return ret;
	}

	public FormsArray markAsInventoried() throws Exception {
		FormsArray ret = new FormsArray();

		PartLink pl = new PartLink(upper.obj, lower.obj, sVars);
		pl.find();
		ret.rawText("Last inventoried on " + pl.getInventoryDate());
		ret.newLine();
		ret.submitButton("Mark '" + lower.obj.getInstanceName() + "' as containing " + pl.getLinkQuantity() + " of '"
				+ upper.obj.getInstanceName() + "'", INVENTORYBUTTON);
		return ret;
	}

	int inOutQuantity = 1;

	public FormsArray extractParams(SessionVars sVars
//			, SmartForm callBackVar
//			, SmartForm whoCalled,
//			MyObject objectBelowMe,
	// used by Part
//			SelectForm form
	) throws Exception {
		FormsArray ret = new FormsArray();
		if (sVars.hasParameterKey(QUANTITY)) {
//			ret.addAll(form.tryAgain(sVars, objectBelowMe));
			inOutQuantity = Integer.parseInt(sVars.getParameterValue(QUANTITY));
			if (inOutQuantity < 1) {
				ret.errorToUser("Quantity must be positive.");
				throw new EndOfInputException(ret);
			}
			// keep going
		}
		if (sVars.hasParameterKey(STOREBUTTON)) {
//			ret.addAll(form.tryAgain(sVars, location));
			if (upper.obj.childExists(lower.obj)) {
				new PartLink(upper.obj, lower.obj, sVars).find().updateAddQuantity(inOutQuantity, false);
			} else {
				Part part = (Part) upper.obj;
				part.addChild(lower.obj, inOutQuantity);
			}
			inOutQuantity = 1;
			fmd.resetAllIdAndStrings();
			throw new EndOfInputException(ret);
		}
		if (sVars.hasParameterKey(REMOVEBUTTON)) {
//			ret.addAll(form.tryAgain(sVars, objectBelowMe));
			// update the quantity and throw an error if the resulting quantity is invalid
			PartLink pl = new PartLink(upper.obj, lower.obj, sVars).find();
			int newQuant = pl.getLinkQuantity() - inOutQuantity;
			if (newQuant < 0) {
				ret.errorToUser("The quantity to be removed is larger than the quantity in stock.");
				throw new EndOfInputException(ret);
			}
			if (newQuant == 0) {
				pl.deleteUnconditionally();
			} else {
				// update the quantity, keep the same time
				pl.updateSetQuantity(newQuant, false);
				inOutQuantity = 1;
			}
			fmd.resetAllIdAndStrings();
			throw new EndOfInputException(ret);
		}
		if (sVars.hasParameterKey(INVENTORYBUTTON)) {
//			ret.addAll(form.tryAgain(sVars, objectBelowMe));
			if (upper.obj.childExists(lower.obj)) {
				PartLink pl = new PartLink(upper.obj, lower.obj, sVars);
				pl.find();
				pl.setInventoried(true);
				pl.update();
			} else {
				throw new Exception("PartLink does not exist.");
			}
			throw new EndOfInputException(ret);
		}
		if (sVars.hasParameterKey(MOVETOSECONDLOCATION)) {
			if (upper.obj.childExists(lower.obj)) {
				PartLink oldLink = new PartLink(upper.obj, lower.obj, sVars).find();
				int oldQuantity = oldLink.getLinkQuantity();
				oldLink.deleteUnconditionally();
				// create a link from the part to the second location
				PartLink newLink = new PartLink(upper.obj, fmd.get(lower.myRow).get(lower.myColumn + 1).obj, sVars);
				if (newLink.linkExists())
					newLink.updateAddQuantity(oldQuantity, false);
				else {
					((Part) upper.obj).addChild(fmd.get(lower.myRow).get(lower.myColumn + 1).obj, oldQuantity);
				}
				ret.rawText("All of Part:" + upper.obj.getInstanceName() + " moved from Location:"
						+ lower.obj.getInstanceName() + " to Location:"
						+ fmd.get(lower.myRow).get(lower.myColumn + 1).obj.getInstanceName());
			} else {
				throw new Exception("PartLink does not exist.");
			}
			throw new EndOfInputException(ret);
		}
		return ret;
	}
}
