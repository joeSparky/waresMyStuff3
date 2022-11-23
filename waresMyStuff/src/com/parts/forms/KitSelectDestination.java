package com.parts.forms;

import com.db.SessionVars;
import com.forms.AttachToFormPair;
import com.forms.FormsMatrixDynamic;

/**
 * move locations, moving items
 * 
 * @author Joe
 *
 */
public class KitSelectDestination extends AttachToFormPair {

	public KitSelectDestination(FormsMatrixDynamic fmd, SessionVars sVars, SearchTarget first, SearchTarget second) throws Exception {
		super(fmd, sVars, first, second);
	}
	
//	@Override
//	protected MyObject findLocationSourceIndex(SearchTargets objs) throws Exception {
//		int tmp = objs.findIndexOfObject(new Kit(SessionVars sVars));
//		if (tmp == -1)
//			throw new Exception(Kit.NAME + " was not found.<br>" + new Exception().getStackTrace()[0]);
//		return objs.get(tmp).obj;
//	}
//
//	@Override
//	protected boolean moveContentsFlag() {
//		return false;
//	}
//	@Override
//	protected boolean moveItemFlag() {return false; }
}
