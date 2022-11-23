package com.parts.forms;

import com.db.SessionVars;
import com.forms.AttachToFormPair;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.security.MyObject;

public class SearchTarget extends com.forms.SearchTarget {
	public SearchTarget(MyObject obj, SessionVars sVars) {
		super(obj, sVars);
		this.sVars = sVars;
		// TODO Auto-generated constructor stub
	}

	SessionVars sVars = null;

	@Override
	public String setInventoryLinkQuery(int firstDisplayedRecord) throws Exception {
		if (fmd.get(fmd.row).get(fmd.column).obj instanceof Part
				&& fmd.get(fmd.row).get(fmd.column + 1).obj instanceof Location) {
			return super.setInventoryLinkQuery(firstDisplayedRecord);
		} else
			return "";
	}

	@Override
	protected AttachToFormPair getToTheLeft() throws Exception {
		if (fmd.get(myRow).get(myColumn - 1).obj instanceof Part
				&& fmd.get(myRow).get(myColumn).obj instanceof Location)
			return new AttachToFormPartLocation(fmd, sVars, (SearchTarget) fmd.get(myRow).get(myColumn - 1),
					(SearchTarget) fmd.get(myRow).get(myColumn));
		if (fmd.get(myRow).get(myColumn - 1).obj instanceof Location
				&& fmd.get(myRow).get(myColumn).obj instanceof Location)
			return new AttachToFormLocationLocation(fmd, sVars, (SearchTarget) fmd.get(myRow).get(myColumn - 1),
					(SearchTarget) fmd.get(myRow).get(myColumn));
		return new AttachToFormPair(fmd, sVars, fmd.get(myRow).get(myColumn - 1), fmd.get(myRow).get(myColumn));
	}

	@Override
	protected AttachToFormPair getToTheRight() throws Exception {
		if (fmd.get(myRow).get(myColumn).obj instanceof Part
				&& fmd.get(myRow).get(myColumn + 1).obj instanceof Location)
			return new AttachToFormPartLocation(fmd, sVars, (SearchTarget) fmd.get(myRow).get(myColumn),
					(SearchTarget) fmd.get(myRow).get(myColumn + 1));
		if (fmd.get(myRow).get(myColumn).obj instanceof Location
				&& fmd.get(myRow).get(myColumn + 1).obj instanceof Location)
			return new AttachToFormLocationLocation(fmd, sVars, (SearchTarget) fmd.get(myRow).get(myColumn),
					(SearchTarget) fmd.get(myRow).get(myColumn + 1));
		return new AttachToFormPair(fmd, sVars, fmd.get(myRow).get(myColumn), fmd.get(myRow).get(myColumn + 1));
	}
}
