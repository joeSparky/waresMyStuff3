package com.forms;

import com.db.SessionVars;

public class BlankForm extends SmartForm {

	protected BlankForm(FormsMatrixDynamic fmd, SessionVars sVars) {
		super(fmd, sVars);
	}
//	public BlankForm(){
//		super(null);
//	}

	@Override
	public String getReturnTo() throws Exception {
		// point to the login form
//		SmartForm sf = sVars.getLogin();
		return sVars.getLoginForm().getReturnTo();
	}

	Exception e = null;

	public void setErrorToUser(Exception e) {
		this.e = e;
	}

	@Override
	public FormsArray getForm(SessionVars sVars) {
		FormsArray ret = new FormsArray();
		if (e != null) {
			ret.errorToUser(e);
			this.e = null;
		}
		return ret;
	}
}
