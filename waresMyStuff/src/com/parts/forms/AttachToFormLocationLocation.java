package com.parts.forms;

import com.db.SessionVars;
import com.forms.AttachToFormPair;
import com.forms.EndOfInputException;
import com.forms.FormsArray;
import com.forms.FormsMatrixDynamic;
import com.forms.Utils;
import com.parts.location.Location;
import com.security.ExceptionCoding;

public class AttachToFormLocationLocation extends AttachToFormPair {

	String MOVECONTENTSBUTTON = Utils.getNextString();
	String MOVETONEWPARENTBUTTON = Utils.getNextString();

	public AttachToFormLocationLocation(FormsMatrixDynamic fmd, SessionVars sVars, SearchTarget firstTarget,
			SearchTarget secondTarget) throws Exception {
		super(fmd, sVars, firstTarget, secondTarget);
		target = firstTarget;
		if (!(target.obj instanceof Location))
			throw new ExceptionCoding("target is not a location");
		below = secondTarget;
		if (!(below.obj instanceof Location))
			throw new ExceptionCoding("second object is not a location");
	}

	@Override
	public FormsArray getForm() throws Exception {
		FormsArray ret = new FormsArray();
//		ret.addAll(super.getForm());
		if (target.obj.isLoaded() && below.obj.isLoaded()) {
			ret.submitButton(
					"Move the contents of '" + target.obj.getInstanceName() + "' to '" + below.obj.getInstanceName() + "'",
					MOVECONTENTSBUTTON);

			ret.submitButton("Make '" + target.obj.getInstanceName() + "' a child of '" + below.obj.getInstanceName() + "'",
					MOVETONEWPARENTBUTTON);
		}
		return ret;
	}

	@Override
	public FormsArray extractParams(SessionVars sVars) throws Exception {
//	, SmartForm callBackVar, SmartForm whoCalled,
//			MyObject location,
//			// try again
//			SelectForm form) throws Exception {
		FormsArray ret = new FormsArray();

		if (sVars.hasParameterKey(MOVECONTENTSBUTTON)) {
			// move the parts
			((Location) target.obj).moveContents((Location) below.obj);
			// inform the caller
//			ret.addAll(form.updateTheCaller(sVars, sourceLocation));
			throw new EndOfInputException(ret);
		}

		if (sVars.hasParameterKey(MOVETONEWPARENTBUTTON)) {
			target.obj.moveToNewParentRecursive(below.obj);
			// inform the caller
//			ret.addAll(form.updateTheCaller(sVars, sourceLocation));
			throw new EndOfInputException(ret);
		}
		return ret;
	}
}
