package com.parts.security;

import com.db.SessionVars;
import com.db.XML;
import com.forms.FormsArray;
import com.forms.SmartForm;
import com.forms.Utils;
import com.security.ExceptionCoding;
import com.security.User;

/**
 * walk the user through the login process.
 * 
 * @author joe
 * 
 */
public class Login extends SmartForm {
	private static final String PASSWORD = Utils.getNextString();
	private static final String USER = Utils.getNextString();
	private static final String LOGIN = Utils.getNextString();
	private static final String MYRETURNTO = Utils.getNextString();
	User user = null;

	// normal operation
	public Login(SessionVars sVars) throws Exception {
		super(null, sVars);
		myState = MYSTATE.NAMEANDPASSWORD;
		resetForm(INSTATE.STANDARDFORM);
		user = new User(sVars);
	}

	private enum MYSTATE {
		NOTINITIALIZED, NAMEANDPASSWORD, LOGGEDIN, TRAPTRASH
	}

	private MYSTATE myState;

	public FormsArray extractParams(SessionVars sVars) throws ExceptionCoding {
		FormsArray ret = new FormsArray();
		switch (myState) {
		case NAMEANDPASSWORD:
			// set the user number if the user name and password in the input are valid.
			// This tells the rest of the system that the user has successfully logged in.
			try {
				sVars.userNumber = user.isValidUser(sVars.getParameterValue(USER),
						sVars.getParameterValue(PASSWORD)).id;
			} catch (Exception e) {
				// ignore the input if it doesn't have the strings we're looking for
				ret.errorToUser(e);
			}
//			SmartForm dispatch = new XML(sVars).getDispatch(sVars);
//			ret.addAll(dispatch.getForm(sVars));
//			ret.status = FormsArray.STATUS.BACKTODISPATCH;
			return ret;
		case LOGGEDIN:
			return ret;
		case TRAPTRASH:
			// trashed input. force the user to start over
			sVars.clearLogin();
			myState = MYSTATE.NAMEANDPASSWORD;
			return ret;
		default:
			throw new ExceptionCoding(myState.toString());
		}
	}

	public void resetForm(INSTATE inState) throws Exception {
//		this.inState = inState;
		if (user == null) {
			user = new User(sVars);
		}
	}

	/**
	 * create form to collect user name and password
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param context
	 * @throws Exception
	 */
	public FormsArray getForm(SessionVars sVars) throws Exception {
		sVars.setApToRun(MYRETURNTO, this);
		if (myState == null)
			myState = MYSTATE.NAMEANDPASSWORD;
		FormsArray ret = new FormsArray();
		if (sVars.isLoggedIn()) {
			SmartForm dispatch = sVars.xml.getDispatch(sVars);
			ret.addAll(dispatch.getForm(sVars));
			return ret;
		}
		switch (myState) {
		case NAMEANDPASSWORD:
			ret.startTable();
			ret.startRow();
			ret.textBox(USER, User.USERNAMELENGTH, "User Name", user.getInstanceName(), true, false);
			ret.endRow();
			ret.startRow();
			ret.passwordBox(PASSWORD, User.PASSWORDLENGTH, "Password", user.password, false);
			ret.endRow();
			ret.endTable();
			ret.submitButton("Log In", LOGIN);
			ret.newLine();
			ret.link("Help (Open in a new window.)<br>", "help.htm");
			ret.newLine();
			ret.setReturnTo(this, sVars);
			break;
		case LOGGEDIN:
			break;
		default:
			throw new Exception(myState.toString() + " is not implemented");
		}
		return ret;
	}

	public static void logOff(SessionVars sVars) {
		sVars.clearLogin();
		;
		sVars.request.getSession().invalidate();
	}

	@Override
	public FormsArray processButtons(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
		ret.addAll(extractParams(sVars));
		if (sVars.isLoggedIn()) {
//			XML xml = 
			SmartForm disp = sVars.xml.getDispatch(sVars);
			ret.addAll(disp.getForm(sVars));
		} else
			ret.addAll(getForm(sVars));
		return ret;

	}

	@Override
	public String getReturnTo() {
		return MYRETURNTO;
	}
}
