package com.forms;

import com.db.SessionVars;

public class AttachToForm extends SmartForm{
	String buttonLinkChild = Utils.getNextString();
	String buttonUnlinkChild = Utils.getNextString();
//	String buttonInventoried = Utils.getNextString();
	protected SearchTarget above, target, below = null;
//	boolean benign = false;

	public AttachToForm(FormsMatrixDynamic fm, SessionVars sVars) throws Exception {
		super(fm, sVars);
		target = fm.get(fmd.row).get(fmd.column);
		if (fm.isObjectAboveMeInRow())
			above = fm.get(fmd.row).get(fmd.column-1);
		if (fm.isObjectBelowMeInRow())
			below = fm.get(fmd.row).get(fmd.column+1);
	}

//	public AttachToForm() {
//		benign = true;
//	}

	public FormsArray getForm() throws Exception {
		FormsArray ret = new FormsArray();
//		if (benign)
//			return ret;
		if (above != null)
			ret.addAll(getMyForms(above, target));
		if (below != null)
			ret.addAll(getMyForms(target, below));
		return ret;

	}

	FormsArray getMyForms(SearchTarget upper, SearchTarget lower) throws Exception {
		FormsArray ret = new FormsArray();
		if (upper.obj.isLoaded() && lower.obj.isLoaded())
			if (upper.obj.linkToChildExists(lower.obj)) {
				ret.addAll(removeLink(upper, lower));
				ret.addAll(markAsInventoried(upper, lower));
			} else
				ret.addAll(addLink(upper, lower));
		return ret;
	}

	public FormsArray removeLink(SearchTarget upper, SearchTarget lower) throws Exception {
		FormsArray ret = new FormsArray();
		ret.submitButton(
				"Remove link between " + upper.obj.getLogicalName() + ":" + upper.obj.getInstanceName() + " and "
						+ lower.obj.getLogicalName() + ":" + lower.obj.getInstanceName(),
				buttonUnlinkChild + "_" + upper.obj.id + "_" + lower.obj.id);

		// let the user know what would happen if he chose the above option
		try {
			upper.obj.deleteTest(lower.obj);
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
	public FormsArray markAsInventoried(SearchTarget upper, SearchTarget lower) throws Exception {
		FormsArray ret = new FormsArray();
//		ret.submitButton("Mark the quantity of " + upper.obj.getLogicalName() + ":" + upper.obj.getInstanceName() + " at location "
//				+ lower.obj.getLogicalName() + ":" + lower.obj.getInstanceName() + " as correct", buttonUnlinkChild);
		return ret;
	}

	public FormsArray addLink(SearchTarget upper, SearchTarget lower) throws Exception {
		FormsArray ret = new FormsArray();
		ret.submitButton(
				"Create link between " + upper.obj.getLogicalName() + ":" + upper.obj.getInstanceName() + " and "
						+ lower.obj.getLogicalName() + ":" + lower.obj.getInstanceName(),
				buttonLinkChild + "_" + upper.obj.id + "_" + lower.obj.id);

		return ret;
	}

	@Override
	public FormsArray extractParams(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
//		if (benign)
//			return ret;
		if (above != null)
			ret.addAll(extractOneSide(sVars, above, target));
//			, callBackVar, whoCalled, form, fm, above, target));
		if (below != null)
			ret.addAll(extractOneSide(sVars, target, below));
//		, callBackVar, whoCalled, form, fm, target, below));
//		ret.addAll(extractHandleLinkBelowMeSpare(sVars, callBackVar, whoCalled, target, form));
		return ret;
	}

	public FormsArray extractOneSide(SessionVars sVars,
//	, SmartForm callBackVar, SmartForm whoCalled,
//			// used by Part
//			SelectForm form, FormsMatrixDynamic fm, 
	SearchTarget upper, SearchTarget lower) 
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
