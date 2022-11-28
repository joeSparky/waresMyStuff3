package com.forms;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.db.SessionVars;
import com.errorLogging.Internals;

/**
 * general request handler
 * 
 * @author joe
 * 
 */
public class HandleRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String SESSIONATTRIBUTE = "sessionVariables";
	/**
	 * the first time HttpServlet has run
	 */
	boolean firstTime = true;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doBoth(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doBoth(request, response);
	}

	protected void doBoth(HttpServletRequest request, HttpServletResponse response) {
		SessionVars sVars = null;
		response.setContentType("text/html");
		HttpSession session = null;
		/**
		 * if the server restarts during a session, the session remains up after the
		 * restart. However, sVars is returned as null in the getAttribute.
		 */

//		synchronized (this) 
		{
			// get the old session or create a new session if there is no old session
			session = request.getSession(true);

			// get sVars from the session, or a null if sVars has not been saved in the
			// session
			sVars = (SessionVars) session.getAttribute(SESSIONATTRIBUTE);

			if (sVars == null) {
				try {
					sVars = new SessionVars(getServletContext());
				} catch (Exception e) {
					Internals.logStartupError(e);
				}
				// save sVars in the session variables
				session.setAttribute(SESSIONATTRIBUTE, sVars);
			}
		}
		sVars.request = request;
		sVars.response = response;

		if (firstTime) {
			firstTime = false;
			try {
				// checks xml file available, use the test to initialize the login form
				sVars.getLoginForm();
				// checks access to the database
				sVars.connection.getConnection();
			} catch (Exception e) {
				Internals.logStartupError(e);
			}

		}

		// if tVars.context = getServletContext();

		// new FormToRun().whichForm(sVars, newSession);

//		if (sVars.ret == null) {
//			sVars.ret = new FormsArray();
//			sVars.ret.errorToUser("3/11/2022" + FormsArray.lineSeparator);
//		}
//		if (newSession)
//			sVars.ret.errorToUser("new session " + FormsArray.lineSeparator);

//		if (sVars.threadAlreadyRunning) {
////			Internals.logString("thread already running");
//			sVars.ret.errorToUser("Second thread attempted.");
//			sVars.threadAlreadyRunning = false;
//			return;
//		} else
//			sVars.threadAlreadyRunning = true;
		// strip the input parameters
		sVars.extractInputParams(request);

//		Internals.logWithDate();

//		if (Internals.startupError())
//			sVars.ret.errorToUser(Internals.getStartupError());

//		XML xml = null;
//		SmartForm login = null;
		// get the form associated with the input or null
		SmartForm sf = sVars.getApToRun();

//		Exception ex = null;
//		try {
////			xml = 
//			// let parts override
//			login = sVars.getLoginForm();
////			login = xml.getLogin(null, sVars);
//		} catch (Exception e) {
//			// save the exception for later
//			ex = e;
//		}
//		if (ex != null) {
//			// we can't run
//			// bestOutputEffort(login, ex);
//			bestOutputEffort(response);
//			sVars.threadCount--;
//			return;
//		}

		// if the user is not logged in or the input did not have an id for the
		// application to run
		if (!sVars.isLoggedIn() || sf == null) {
			SmartForm login = null;
			try {
				login = sVars.getLoginForm();
			} catch (Exception e) {
				// should have been caught when the servlet first ran
				Internals.logStartupError(e);
			}
			processInput(login, sVars);
			sVars.threadCount--;
			return;

		}

//		try {
//			sf = sVars.getApToRun(returnTo);
//		} catch (Exception e) {
//		}
//		if (sf == null) {
//			backToLogin(sVars.ret, login);
//			sVars.threadAlreadyRunning = false;
//			return;
//		}
//
//		// if not logged in
//		if (!sVars.isLoggedIn()) {
//			processInput(login);
//			sVars.threadAlreadyRunning = false;
//			return;
//		}
//		Internals.logWithDate();

		processInput(sf, sVars);
		sVars.threadCount--;
	}

//	@SuppressWarnings("unused")
//	private void backToLogin(FormsArray ret, SmartForm login) {
//		// go back to the login screen
//		try {
//			ret.addAll(login.getForm(sVars));
//		} catch (Exception e) {
//			ret.errorToUser(e);
//		}
//		ret.executeForm(sVars, sVars.request, sVars.response, sVars.context);
//		return;
//	}

	private void processInput(SmartForm thisOne, SessionVars sVars) {
		FormsArray ret = new FormsArray();
		if (thisOne == null)
			ret.errorToUser(Internals.dumpExceptionToString(new Exception("null thisOne")));
		if (sVars == null)
			ret.errorToUser(Internals.dumpExceptionToString(new Exception("null sVars")));
		if (sVars.request == null)
			ret.errorToUser(Internals.dumpExceptionToString(new Exception("null sVars.request")));
		if (sVars.response == null)
			ret.errorToUser(Internals.dumpExceptionToString(new Exception("null sVars.response")));
		if (sVars.context == null)
			ret.errorToUser(Internals.dumpExceptionToString(new Exception("null sVars.context")));

		try {
			ret.addAll(thisOne.processButtons(sVars));
		} catch (Exception e) {
			ret.errorToUser(e);
		}
//		ret.errorToUser("inCount:" + inCount + " outCount:" + outCount);

		ret.executeForm(sVars, sVars.request, sVars.response, sVars.context);
	}

//	private void bestOutputEffort(HttpServletResponse response) {
//		bestOutputEffort(response, "");
//	}
//
//	private void bestOutputEffort(HttpServletResponse response, Exception e) {
//		Internals.logStartupError(e);
//		bestOutputEffort(response, "");
//	}

//	private void bestOutputEffort(HttpServletResponse response, String errorString) {
//		FormsArray ret = new FormsArray();
//		// get startup errors
//		ret.errorToUser(Internals.getStartupError());
//		// get the error passed to us
//		if (!errorString.isEmpty())
//			ret.errorToUser(errorString);
////		ret.errorToUser("inCount:" + inCount + " outCount:" + outCount);
//		PrintWriter out = null;
//		try {
//			out = response.getWriter();
//		} catch (IOException e) {
//			// try to dump the exception the next time we run
//			Internals.logStartupError(e);
//		}
//		out.print(ret.generateHTML());
//		out.flush();
//
////		try {
////			blankForm = new BlankForm(new FormsMatrixDynamic(sVars), sVars);
////			blankForm.setErrorToUser(e);
////			blankForm.processButtons(sVars);
////			FormsArray ret = new FormsArray();
////			ret.addAll(blankForm.processButtons(sVars));
////			if (Internals.startupError())
////				ret.errorToUser(Internals.getStartupError());
////			ret.executeForm(sVars, sVars.request, sVars.response, sVars.context);
////		} catch (Exception e1) {
////			// we can't get to the user. try dumping the failure to the log.
////			Internals.logStartupError(e1);
////		}
//	}
//
////	private void failedForm(Exception callerException) {
////		Internals.dumpExceptionContinue(callerException);
////		sVars.request.getSession().invalidate();
////		RequestDispatcher dispatcher = sVars.context.getRequestDispatcher("/index.html");
////		try {
////			dispatcher.forward(sVars.request, sVars.response);
////		} catch (ServletException e) {
////			Internals.dumpExceptionExit(e);
////		} catch (IOException e) {
////			Internals.dumpExceptionExit(e);
////		}
////	}

	public static void backToLogin(SessionVars sVars) throws ServletException, IOException {
		sVars.request.getSession().invalidate();
		RequestDispatcher dispatcher = sVars.context.getRequestDispatcher("/index.html");

		dispatcher.forward(sVars.request, sVars.response);
//		} catch (ServletException e) {
//			Internals.dumpExceptionContinue(e);
//		} catch (IOException e) {
//			Internals.dumpExceptionContinue(e);
//		}
	}

//	public SmartForm getLoginFromXML(SessionVars sVars) throws Exception {
//		return xml.getLogin(sVars);
//	}

}

//TODO need http://localhost:9080/parts/handler, not localhost:9080/parts/handler for form to work
//TOOD sVars is static in HandleRequest