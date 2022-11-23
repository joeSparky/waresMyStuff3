package com.parts.forms;

import com.db.SessionVars;
import com.security.MyObject;

/**
 * capture the SearchTarget for inventoryLink so that only the Part/Location pair get queried
 * @author joe
 *
 */
public class SearchTargets extends com.forms.SearchTargets{

//	public SearchTargets(FormsMatrixDynamic fmd) {
//		super(fmd);
//	}

	public SearchTargets(SessionVars sVars) {
		super(sVars);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6982550890557444643L;
	
	// pick up the local SearchTarget
	@Override
	public void add(MyObject obj, SearchTarget.EDITSELECTTYPE editSelectType) throws Exception {
		SearchTarget st = new SearchTarget(obj, sVars);
//		st.obj = obj;
		st.editSelectType = editSelectType;
		this.add(st);
	}
	
//	@Override
//	public String setInventoryLinkQuery() throws Exception {
//		if (fmd.get(fmd.row).get(fmd.column).obj instanceof Part
//				&& fmd.get(fmd.row).get(fmd.column + 1).obj instanceof Location) {
//			return super.setInventoryLinkQuery();
//		} else
//			return "";
//	}
}
