package com.parts.exhibit;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.Table;

public class KitDestination extends Kit{
	public KitDestination(SessionVars sVars) throws Exception {
		super(sVars);
		new Table().tableCreated(this, sVars);
	}

//	public Kit(SessionVars sVars) throws Exception {
//		super(sVars);
//	}

//	@Override
//	public SmartForm getSelector(FormsMatrixDynamic fmd, SessionVars sVars) throws Exception {
//
//		return new KitSelectDestination(fmd, sVars);
//	}
	// fake out filteredList
	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}
	
//	@Override
//	public IdAndStrings listAllIdAndStrings(Anchor listAnchor) throws SQLException, Exception {
//		return new Kit(SessionVars sVars).listAllIdAndStrings(listAnchor);
//	}
}
