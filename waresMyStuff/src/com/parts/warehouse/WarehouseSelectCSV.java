package com.parts.warehouse;

import com.db.SessionVars;

public class WarehouseSelectCSV extends Warehouse{

	public WarehouseSelectCSV(SessionVars sVars) throws Exception {
		super(sVars);
	}
	
	public static final String NAME = "Inventory";
//	@Override
//	public SmartForm getSelector(SmartForm callBack, String keyWordsLabel, String overallLabel, FormsMatrixDynamic fmd) throws Exception {
//
//		return new CSVFormSelect(callBack, keyWordsLabel, overallLabel, objs, index);
//	}
	// fake out filteredList
	@Override
	public String getMyFileName() throws Exception {
//		try {
			return new Warehouse(sVars).getMyFileName();
//		} catch (Exception e) {
//			Internals.dumpExceptionContinue(e);
//		}
//		return "";
	}
	@Override
	public String getLogicalName() {
		return NAME;
	}
}
