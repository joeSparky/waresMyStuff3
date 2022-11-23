package com.parts.security;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashSet;

import com.db.MyConnection;
import com.db.SessionVars;
import com.db.XML;
import com.errorLogging.Internals;
import com.forms.DispatchRunStuff;
import com.forms.FormsArray;
import com.forms.HandleRequest;
import com.forms.SmartForm;
import com.forms.Utils;
import com.parts.runApps.RunApplication;
//import com.runApps.RunApplication;
import com.security.ExceptionCoding;
import com.security.Permission;
import com.security.Permissions;
import com.security.User;

/**
 * connect the displayed text with the url of the jsp to handle it
 * 
 * @author joe
 * 
 */
public class Dispatch extends SmartForm {
	public Dispatch(SessionVars sVars) {
		super(null, sVars);
		// register with HandleRequest
		if (!sVars.hasApToRun(MYRETURN))
			sVars.setApToRun(MYRETURN, this);
		try {
			DBChanges.doDBChanges(sVars);
		} catch (SQLException e) {
			dbChangeException = e;
		} catch (Exception e) {
			dbChangeException = e;
		}
	}

	Exception dbChangeException = null;

//	private String GOTO = DispatchRunStuff.getMyGoTo(this);
	private String NODE_GOTO = Utils.getNextString();
	private String GOUP = Utils.getNextString();
	private static String MYRETURN = Utils.getNextString();

	// public String DISPATCHID = Utils.getNextString();

	/**
	 * create a form to select the class to run
	 * 
	 * @param sVars
	 * @return
	 * @throws Exception
	 */
	@Override
	public FormsArray getForm(SessionVars sVars) throws Exception {
//		fdfdsVars.setApToRun(GOTO, this);
		FormsArray ret = new FormsArray();
		if (dbChangeException != null) {
			ret.errorToUser(dbChangeException);
			dbChangeException = null;
		}
		if (justDispatched == null) {
			User user = new User(sVars);
			user.find(sVars.getUserNumber());
			ret.rawText(user.firstName + " " + user.lastName + "<br>");
			ret.rawText("Database:" + sVars.xml.readXML(MyConnection.XMLDBNAME) + "<br>");
//			ret.rawText("Error log:" + Internals.getLogFilePathandName() + "<br>");
			ret.rawText(currentNode.buttonName + " menu:<br>");
			for (DispatchRunStuff thisNode : currentNode.children) {
				Permissions p = new Permissions();
				p.add(Permission.ADMINISTRATOR);
				if (thisNode.hasRunnables(p)) {
					ret.submitButton(thisNode.buttonName, NODE_GOTO + " " + thisNode.thisId);
					// ret.radioButton(NODE_GOTO, "" + thisNode.thisId,
					// thisNode.buttonName, true, false);
					// ret.newLine();
				}
			}
			if (!currentNode.equals(mainBranch)) {
				ret.submitButton("Go up one level", GOUP);
				// ret.newLine();
			}
			ret.setReturnTo(this, sVars);
			// ret.hiddenField(FormsArray.HIDDEN, this.toString());
			return ret;
		} else {
			// we just dispatched to a class. use it for the form
			ret.addAll(justDispatched.getForm(sVars));
			justDispatched = null;
			return ret;
		}
	}

	/**
	 * just dispatched a new class. use the just dispatched class for getForm
	 */
	private SmartForm justDispatched = null;

	@Override
	public FormsArray extractParams(SessionVars sVars) throws Exception {
		justDispatched = null;
		FormsArray ret = new FormsArray();
		if (sVars.getParameterKeys().contains(NODE_GOTO)) {
			int foundID = Integer.parseInt(sVars.getParameterValue(NODE_GOTO));
			for (DispatchRunStuff thisRun : currentNode.children) {
				if (thisRun.thisId == foundID) {
					currentNode = thisRun;
					sVars.buttonName = thisRun.buttonName;
					if (thisRun.runName.isEmpty())
						return ret;
					else
						try {
							justDispatched = new RunApplication().dispatchThis(thisRun.runName, sVars);
						} catch (Exception e) {
							ret.errorToUser(e);
						}
					return ret;
				}
			}
			ret.errorToUser(new Exception("couldn't find " + foundID));
//			Internals.dumpStringContinue("couldn't find " + foundID);
			HandleRequest.backToLogin(sVars);
			return ret;
		}
		if (sVars.getParameterKeys().contains(GOUP)) {
			if (currentNode.parent == null) {
				throw new Exception("asked to go up at top trunk of tree");
//				HandleRequest.backToLogin(sVars);
//				return ret;
			} else {
				currentNode = currentNode.parent;
				return ret;
			}
		}
		for (String thisId : FormsArray.getIdsAndNoValues(sVars.getParameterMap(), NODE_GOTO)) {
			int foundID = Integer.parseInt(thisId);
			for (DispatchRunStuff thisRun : currentNode.children) {
				if (thisRun.thisId == foundID) {
					currentNode = thisRun;
					sVars.buttonName = thisRun.buttonName;
					if (thisRun.runName.isEmpty())
						return ret;
					else {
//						try {
						try {
							justDispatched = new RunApplication().dispatchThis(thisRun.runName, sVars);
						} catch (NoSuchMethodException e) {
							ret.errorToUser(e);
							return ret;
						} catch (SecurityException e) {
							ret.errorToUser(e);
							return ret;
						} catch (InstantiationException e) {
							ret.errorToUser(e);
							return ret;
						} catch (IllegalAccessException e) {
							ret.errorToUser(e);
							return ret;
						} catch (IllegalArgumentException e) {
							ret.errorToUser(e);
							return ret;
						} catch (InvocationTargetException e) {
							for (StackTraceElement ste : e.getStackTrace()) {
								ret.rawText(ste.toString());
								ret.newLine();
							}
							ret.errorToUser(e);
							return ret;
						}
//						} catch (Exception e) {
//							ret.errorToUser(e);
//							return ret;
//						}
						if (justDispatched.getClass().equals(Logout.class)) {
							Login.logOff(sVars);
							// prompt the user to log back in
							justDispatched = new Login(sVars);
						}
						return ret;
					}
				}
			}
			throw new Exception("couldn't find " + foundID + " amoung " + currentNode.children);
//			HandleRequest.backToLogin(sVars);
//			return ret;
		}
		throw new Exception("shouldn't get here");
	}

	DispatchRunStuff currentNode = mainBranch;
	static DispatchRunStuff mainBranch = null;
	static {
		/**
		 * top of the menu
		 */
		mainBranch = null;
		try {
			mainBranch = new DispatchRunStuff("", null, "Main");
		} catch (ExceptionCoding e) {
			Internals.logStartupError(e);
		}
		try {
			mainBranch.addChildren(new DispatchRunStuff("com.parts.forms.MainPartsForm", Permission.USER, "Parts"));
		} catch (ExceptionCoding e) {
			Internals.logStartupError(e);
		}
//		DispatchRunStuff testingBranch = new DispatchRunStuff("", null, "Testing");
//		mainBranch.addChildren(
//				new DispatchRunStuff("com.parts.forms.CSVForm", Permission.USER, "print inventory"));
		try {
			mainBranch
					.addChildren(new DispatchRunStuff("com.parts.security.FormLogout", Permission.MANAGER, "Log Off"));
		} catch (ExceptionCoding e) {
			Internals.logStartupError(e);
		}
//		testingBranch.addChildren(
//				new DispatchRunStuff("com.parts.forms.ImportCDM", Permission.ADMINISTRATOR, "import CDM database"));
//		mainBranch.addChildren(testingBranch);

		try {
			DispatchRunStuff.findDuplicateClassName(mainBranch, new HashSet<String>());
			DispatchRunStuff.findDuplicateButtonName(mainBranch, new HashSet<String>());
		} catch (Exception e) {
			com.errorLogging.Internals.dumpException(e);
		}

	}

	@Override
	public String getReturnTo() {
		return MYRETURN;
	}

	@Override
	public FormsArray processButtons(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
		ret.addAll(extractParams(sVars));
		if (justDispatched != null)
			ret.addAll(justDispatched.getForm(sVars));
		else
			ret.addAll(getForm(sVars));
		return ret;

	}
}
