package com.forms;

public class EndOfInputException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public EndOfInputException(FormsArray form) {
		super();
		fa = new FormsArray();
		fa = form;
	}
	FormsArray fa = null;
//	public void setForm(FormsArray form) {
//		fa = new FormsArray();
//		fa = form;
//	}
	public FormsArray getForm() {
		return fa;
	}
	
	/**
	 * setup the form to return to the instance that is completing the input processing
	 * @param form
	 * @param sVars
	 * @param oc
	 * @param caller
	 * @throws Exception
	 */
//	public EndOfInputException(FormsArray form, SessionVars sVars, MyObject oc, SmartForm caller) throws Exception {
//		super();
//		fa = new FormsArray();
//		fa = form;		
//		fa.addAll(caller.callBack(sVars, new CalledInfo(caller, true, FormsArray.STATUS.TRYAGAIN, oc)));
//	}

}
