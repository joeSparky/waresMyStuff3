package com.forms;

import com.security.ExceptionCoding;

public class TabbedDiv {
	public enum DIVTYPE {
		NOTINITIALIZED, NORMALTAB, STARTNEWROW
	}

	

	public TabbedDiv(String tabLabel, boolean isALink, boolean isHighlighted, String returnTo) {
		clear();
		this.divType = DIVTYPE.NORMALTAB;
		this.uniqueId = Utils.getNextString();
		this.isALink = isALink;
		this.isHighlighted = isHighlighted;
		this.tabLabel = tabLabel;
		form = new FormsArray();
		this.returnTo = returnTo;
	}

	public TabbedDiv(String returnTo) {
		clear();
		this.divType = DIVTYPE.STARTNEWROW;
		form = new FormsArray();
		this.returnTo = returnTo;
	}

	private FormsArray form;
	String uniqueId = null;
	String tabLabel = null;
	boolean isALink = false;
	boolean isHighlighted = false;
	DIVTYPE divType = DIVTYPE.NOTINITIALIZED;
	String returnTo = null;

	private void clear() {
		form = null;
		uniqueId = null;
		isHighlighted = isALink = false;
		tabLabel = null;
		divType = DIVTYPE.NOTINITIALIZED;
		returnTo = null;
	}

	public void sanity() throws Exception {
		switch (divType) {
		case NORMALTAB:
			break;
		case NOTINITIALIZED:
			throw new ExceptionCoding("not initialized");
//			break;
		case STARTNEWROW:
			return;
		}
		if (form == null)
			throw new Exception("form not set");
	}

	public void addForm(FormsArray form, String hiddenFieldName, String hiddenFieldValue) throws Exception {		
		this.form.startForm(returnTo);
		this.form.addAll(form);
		this.form.hiddenField(hiddenFieldName, hiddenFieldValue);
		this.form.endForm();
	}
	public FormsArray getForm(){
		return form;
	}
}
