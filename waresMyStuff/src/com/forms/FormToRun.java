package com.forms;

import com.db.SessionVars;
import com.db.XML;
import com.errorLogging.Internals;

public class FormToRun {
	SmartFormAndFormsArray whichForm(SessionVars sVars, boolean newSession) {

		SmartFormAndFormsArray smartFormAndFormsArray = new SmartFormAndFormsArray();

		// if the XML file is not available
		if (sVars.xml == null) {
			// drop down to the basics to inform the user
			addForm(smartFormAndFormsArray, new BlankForm(null, sVars));
			smartFormAndFormsArray.ret.errorToUser(Internals.getStartupError());
			return smartFormAndFormsArray;
		}
		if (newSession)
			addError(smartFormAndFormsArray, "new session " + FormsArray.lineSeparator);

		// do we have an XML file?

		SmartForm loginForm = null;
		try {
			loginForm = sVars.getLoginForm();
		} catch (Exception e1) {
		}

		// if we can't get a login form
		if (loginForm == null) {
			// drop down to the basics to inform the user
			addForm(smartFormAndFormsArray, new BlankForm(null, sVars));
			String loginFileName = null;
			try {
				loginFileName = sVars.xml.readXML(XML.LOGINPARAMNAME);
			} catch (Exception e) {
			}

			if (loginFileName == null) {
//				new XML().getLogin(sVars)
				addError(smartFormAndFormsArray, "Could not find " + XML.LOGINPARAMNAME + " in ");
			}

//			addError(smartFormAndFormsArray, sVars.xml.readXML(XML.LOGINPARAMNAME)+"login not available");
			return smartFormAndFormsArray;
		}

		sVars.threadCount++;
		if (sVars.threadCount > 1) {
			addForm(smartFormAndFormsArray, loginForm);
			addError(smartFormAndFormsArray, "Thread count is " + sVars.threadCount);
			sVars.threadCount--;
			return smartFormAndFormsArray;
		}

		if (!sVars.isLoggedIn()) {
			addForm(smartFormAndFormsArray, loginForm);
			addError(smartFormAndFormsArray, "Please log in.");
			return smartFormAndFormsArray;
		}

		// at this point, we're logged in
		// get the requested app
		SmartForm sf = sVars.getApToRun();
		if (sf == null) {
			// something is wrong. tell the user and force a login
			addForm(smartFormAndFormsArray, loginForm);
			addError(smartFormAndFormsArray, "null apToRun");
			sVars.logout();
			return smartFormAndFormsArray;
		} else {
			// we're logged in and we have the app to run
			addForm(smartFormAndFormsArray, sf);
			return smartFormAndFormsArray;
		}
	}

	void addError(SmartFormAndFormsArray b, String error) {
		if (b.ret == null)
			b.ret = new FormsArray();
		b.ret.errorToUser(error);
	}

	void addForm(SmartFormAndFormsArray b, SmartForm sf) {
		if (b.sf == null)
			b.sf = sf;
		else
			addError(b, "form already set in 'FormToRun");
	}
}
