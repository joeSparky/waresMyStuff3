package com.forms;

import com.db.SessionVars;
import com.security.MyObject;

public class AttachToFormPair extends AttachToForm{
	String buttonLinkChild = Utils.getNextString();
	String buttonUnlinkChild = Utils.getNextString();
//	String buttonInventoried = Utils.getNextString();
	protected SearchTarget upper, lower = null;
//	boolean benign = false;

	/**
	 * work with the object at row, column and at row, column+1
	 * @param fmd
	 * @throws Exception
	 */
	public AttachToFormPair(FormsMatrixDynamic fmd, SessionVars sVars, SearchTarget leftTarget, SearchTarget rightTarget)
			throws Exception {
		super(fmd, sVars);
		upper = leftTarget;
		lower = rightTarget;
	}
	
	public void setRowsColumns(SearchTarget leftTarget, SearchTarget rightTarget) {
		upper = leftTarget;
		lower = rightTarget;
	}

//	public AttachToFormPair(SmartForm callback) {
//		super(callback);
//		benign = true;
//	}

	public FormsArray getForm() throws Exception {
		FormsArray ret = new FormsArray();
//		if (benign)
//			return ret;
		ret.addAll(getMyForms(upper, lower));
		return ret;

	}

	FormsArray getMyForms(SearchTarget upper, SearchTarget lower) throws Exception {
		FormsArray ret = new FormsArray();
		if (upper.obj.isLoaded() && lower.obj.isLoaded())
			if (upper.obj.linkToChildExists(lower.obj)) {
				ret.addAll(removeLink(upper.obj, lower.obj));
				ret.addAll(markAsInventoried(upper.obj, lower.obj));
			} else
				ret.addAll(addLink(upper.obj, lower.obj));
		return ret;
	}

	public FormsArray removeLink(MyObject upper, MyObject lower) throws Exception {
		FormsArray ret = new FormsArray();
		ret.submitButton(
				"Remove link between " + upper.getLogicalName() + ":" + upper.getInstanceName() + " and "
						+ lower.getLogicalName() + ":" + lower.getInstanceName(),
				buttonUnlinkChild + "_" + upper.id + "_" + lower.id);

		// let the user know what would happen if he chose the above option
		try {
			upper.deleteTest(lower);
		} catch (Exception e) {
			// let the user know
			ret.rawText(e.getLocalizedMessage());
			ret.newLine();
		}
		return ret;
	}

	/**
	 * allow parts to override
	 * 
	 * @param upper
	 * @param lower
	 * @return
	 * @throws Exception
	 */
	public FormsArray markAsInventoried(MyObject upper, MyObject lower) throws Exception {
		FormsArray ret = new FormsArray();
//		ret.submitButton("Mark the quantity of " + upper.getLogicalName() + ":" + upper.getInstanceName() + " at location "
//				+ lower.getLogicalName() + ":" + lower.getInstanceName() + " as correct", buttonUnlinkChild);
		return ret;
	}

	public FormsArray addLink(MyObject upper, MyObject lower) throws Exception {
		FormsArray ret = new FormsArray();
		ret.submitButton(
				"Create link between " + upper.getLogicalName() + ":" + upper.getInstanceName() + " and "
						+ lower.getLogicalName() + ":" + lower.getInstanceName(),
				buttonLinkChild + "_" + upper.id + "_" + lower.id);

		return ret;
	}

	@Override
	public FormsArray extractParams(SessionVars sVars)
//			, SmartForm callBackVar, SmartForm whoCalled,
//			// used by Part
//			SelectForm form, FormsMatrixDynamic fm) 
					throws Exception {
		FormsArray ret = new FormsArray();
//		if (benign)
//			return ret;
		ret.addAll(extractOneSide(sVars));
//		, callBackVar, whoCalled, form, fm, upper, lower));
//		ret.addAll(extractHandleLinkBelowMeSpare(sVars, callBackVar, whoCalled, lower, form));
		return ret;
	}

	public FormsArray extractOneSide(SessionVars sVars)
//	, SmartForm callBackVar, SmartForm whoCalled,
//			// used by Part
//			SelectForm form, FormsMatrixDynamic fm, MyObject upper, MyObject lower) 
					throws Exception {
		FormsArray ret = new FormsArray();
		if (sVars.hasParameterKey(buttonLinkChild + "_" + upper.obj.id + "_" + lower.obj.id)) {
			upper.obj.addChild(lower.obj);
			fmd.resetAllIdAndStrings();
			throw new EndOfInputRedoQueries(fmd);
		}

		if (sVars.hasParameterKey(buttonUnlinkChild + "_" + upper.obj.id + "_" + lower.obj.id)) {
			upper.obj.deleteLinkUnconditionally(lower.obj);
			fmd.resetAllIdAndStrings();
			throw new EndOfInputRedoQueries(fmd);
		}
		return ret;

	}

//	public FormsArray extractHandleLinkBelowMeSpare(SessionVars sVars, SmartForm callBackVar, SmartForm whoCalled,
//			MyObject lower,
//			// used by Part
//			SelectForm form) throws Exception {
//		return new FormsArray();
//	}

}
