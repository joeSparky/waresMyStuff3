package com.parts.db;

import com.db.SessionVars;
//import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;
import com.runApps.RunApplication;

public class XML extends com.db.XML{

	public XML(SessionVars sVars) throws Exception {
		super(sVars);
	}
	@Override
	public SmartForm getLogin(SessionVars sVars) throws Exception {
		if (params.get(LOGINPARAMNAME) == null)
			params.put(LOGINPARAMNAME, readXML(LOGINPARAMNAME));
		return new RunApplication().dispatchThis(params.get(LOGINPARAMNAME), sVars);
	}
}
