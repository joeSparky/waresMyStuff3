package com.forms;

import com.db.SessionVars;
import com.errorLogging.Internals;
import com.security.ExceptionCoding;
import com.security.MyObject;

/**
 * drives the selection and editing forms using an array of MyObjects
 * 
 * @author Joe
 *
 */
public class SelectAndEditForm extends SmartForm {
	public SelectAndEditForm(SessionVars sVars, FormsMatrixDynamic fmd) throws Exception {
		super(fmd, sVars);
//		sVars.setApToRun(RUNME, this);
		resetForm(INSTATE.STANDARDFORM);
	}

//	String RUNME = Utils.getNextString();
	private String HIDDENFIELD = Utils.getNextString();
//	FormsMatrixDynamic fmd = null;
	// new ArrayList<ArrayList<FormsMatrixDynamic>>();

	public void resetForm(INSTATE inState) throws Exception {
		for (fmd.row = 0; fmd.row < fmd.getNumberOfRows(); fmd.row++) {
//			SearchTargets sts = fmd.getRow();
			for (fmd.column = 0; fmd.column < fmd.getRowSize(); fmd.column++) {
				MyObject obj = fmd.getObject();
				fmd.getRow().get(fmd.column).selector = obj.getSelector(fmd, sVars);
//				fmd.getRow().get(fmd.column).selector.setNewState(INSTATE.STANDARDFORM);
//				fmd.getRow().get(column).obj = obj;
				// set objectSelectedLastTime for the second column in the first row (should be
				// a warehouse) if there's more than one row (not in JUnit testing).
				if (fmd.row == 0 && fmd.column == 1 && fmd.getRowSize() > 1)
					fmd.getRow().get(fmd.column).objectSelectedLastTime = true;
//				fmd.get(row).add(fmd);
			}
		}
	}

//	SmartForm getBlankForm(FormsMatrixDynamic fmd) {
//		return new BlankForm(fmd, sVars);
//	}

	String whichOne = "";

	@Override
	public FormsArray extractParams(SessionVars sVars) throws Exception {
		getFormAlreadyDone = false;
		fmd.clearAllObjectSelectedLastTime();
		FormsArray ret = new FormsArray();
		fmd.row = getRow(sVars);
		fmd.column = getColumn(sVars);
		if (fmd.row > fmd.size() - 1) {
			ret.errorToUser("have " + fmd.size() + " rows, received hiddenField row of " + fmd.row);
			throw new EndOfInputException(ret);
		}
		if (fmd.column > fmd.get(fmd.row).size()) {
			ret.errorToUser("have " + fmd.get(fmd.row).size() + " columns in row " + fmd.row
					+ ", received hiddenField column of " + fmd.column);
			throw new EndOfInputException(ret);
		}
		fmd.setObjectSelectedLastTime(true);
		ret.addAll(fmd.getSearchTarget().extractParams(sVars));
//		SmartForm selector = fmd.getSelector();
//		ret.addAll(selector.extractParams(sVars, fmd));
		return ret;
	}

	/**
	 * return true if there is a form after this one in the neighborForms list
	 * 
	 * @param i
	 * @return
	 */
	private boolean hasUpperForm(SearchTargets objs, int i) {
		return i < objs.size() - 1;
	}

	/**
	 * return true if this object (i) and the next object (i+1) in objs are selected
	 * 
	 * @param i
	 * @return
	 */
	private boolean bothSelected(SearchTargets objs, int i) {
		// if there is no next form
		if (!hasUpperForm(objs, i))
			return false;
		if (objs.get(i).obj.isLoaded() && objs.get(i + 1).obj.isLoaded())
			return true;
		return false;
	}

	// private boolean showThisForm(ArrayList<MyObjectsArray> objs, int outer,
	// int inner) {
	// return
	// // there is a form below this form
	// hasUpperForm(objs.get(outer), inner)
	// // and this form should be shown use the form below this one as a
	// // parameter
	// && objs.get(outer).get(inner)
	// .showThisScreen(objs.get(outer).get(inner + 1));
	// }

	int findMe(SearchTargets objs, MyObject me) throws Exception {
		for (int i = 0; i < objs.size(); i++) {
			if (me.equals(objs.get(i).obj))
				return i;
		}
		throw new ExceptionCoding("not found");
//		return 0;
	}

	/**
	 * my child and I are both selected.
	 * 
	 * @param objs
	 * @param me
	 * @return
	 * @throws Exception
	 */
	boolean meAndMyChildAreBothSelected(SearchTargets objs, MyObject me) throws Exception {
		return bothSelected(objs, findMe(objs, me));
	}

	boolean alreadySet = false;

	/**
	 * 
	 * @param outer
	 * @param inner
	 * @param thisPass (assumed to be loaded)
	 * @return
	 * @throws Exception
	 */
	String makeTabName() throws Exception {
		// create a tab name
		String tabName = fmd.getObject().getInstanceName();
		if (fmd.aboveMeIsParent())
			return makeBold(tabName);
		else
			return tabName;
	}

	String makeBold(String s) {
		return "<b>" + s + "</b>";
	}

	@Override
	public FormsArray getForm(SessionVars sVars) throws Exception {
//		Internals.logWithDate();
		FormsArray ret = new FormsArray();
		if (getFormAlreadyDone)
			return ret;
		getFormAlreadyDone = true;
		TabbedDivs tds = new TabbedDivs(sVars, this);
		TabbedDiv td = new TabbedDiv(getReturnTo());
		// see if there are multiple objectSelectedLastTime flags set
		fmd.oneAndOnlyOneObjectSelectedLastTime();

//		Internals.logWithDate();
		for (fmd.row = 0; fmd.row < fmd.getNumberOfRows(); fmd.row++) {
			for (fmd.column = 0; fmd.column < fmd.getRow().size(); fmd.column++) {
				if (fmd.getRow().get(fmd.column).editSelectType.equals(SearchTarget.EDITSELECTTYPE.NEITHER))
					continue;
				if (fmd.getObject().isLoaded()) {
					// put selected instance in the tab
					td = new TabbedDiv(makeTabName(), false, fmd.getObjectSelectedLastTime(), getReturnTo());
				} else {
					// use the general name of the object for the tab
					td = new TabbedDiv(fmd.getObject().getLogicalName(), false, fmd.getObjectSelectedLastTime(),
							getReturnTo());
				}

				// get the forms for selecting and editing.
				switch (fmd.getEditSelectType()) {
				case EDITANDSELECT:
				case SELECT:
//					td.addForm(fmd.getSelector().getForm(sVars), HIDDENFIELD,
//							"outer_" + fmd.row + "_inner_" + fmd.column);
					td.addForm(fmd.getSearchTarget().getForm(sVars), HIDDENFIELD,
							"outer_" + fmd.row + "_inner_" + fmd.column);
					break;
				case NEITHER:
					break;
				case NOTINITIALIZED:
					throw new ExceptionCoding("editSelectType not initialized. outer: " + fmd.row + " inner: "
							+ fmd.column + " name:" + fmd.getObject().getInstanceName());
				}
				td.sanity();
				tds.add(td);
			}
			// start a new row of tabs
			tds.add(new TabbedDiv(getReturnTo()));
		}

//		try {
		ret.addAll(tds.dumpDivsIntoForm());
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}

		return ret;
	}

	/**
	 * the selected form has sent us a callback
	 * 
	 */
//	private FormsArray processSelectForm(SessionVars sVars, FormsMatrixDynamic fmd) throws Exception {
//		FormsArray ret = new FormsArray();
////		FilteredList st = formsMatrix.get(formsMatrix.thisRow).get(formsMatrix.thisColumn);
//		switch (fmd.status) {
//		case BACKTODISPATCH:
//			ret.status = FormsArray.STATUS.BACKTODISPATCH;
//			return ret;
//		case SELECTEDNEW:
////			fmd.getRow().get(fmd.column).selector.setNewState(INSTATE.DISABLED);
//			break;
//
//		case NOTINITIALIZED:
//			throw new ExceptionCoding("unknown status of " + fmd.status);
//		}
//		return ret;
//	}

//	@Override
//	public FormsArray callBack(SessionVars sVars, FormsMatrixDynamic fm) throws Exception {
//		checkRowColumn(sVars, fm);
//		return processSelectForm(sVars, fm);
////		}
//	}

	void checkRowColumn(SessionVars sVars, FormsMatrixDynamic fmd) throws Exception {
		String tmp = sVars.getParameterValue(HIDDENFIELD);
		if (tmp == null)
			throw new ExceptionCoding("hidden field not found");
		String[] split = tmp.split("_");
		int outer = Integer.parseInt(split[1]);
		int inner = Integer.parseInt(split[3]);
		if (fmd.row != outer)
			throw new Exception("outer:" + outer + " thisRow:" + fmd.row);
		if (fmd.column != inner)
			throw new Exception("inner:" + outer + " thisColumn:" + fmd.column);
	}

	int getColumn(SessionVars sVars) throws Exception {
		String tmp = sVars.getParameterValue(HIDDENFIELD);
		if (tmp == null)
			throw new ExceptionCoding("hidden field not found");
		String[] split = tmp.split("_");
		return Integer.parseInt(split[3]);
	}

	int getRow(SessionVars sVars) throws Exception {
		String tmp = sVars.getParameterValue(HIDDENFIELD);
		if (tmp == null)
			throw new ExceptionCoding("hidden field not found");
		String[] split = tmp.split("_");
		return Integer.parseInt(split[1]);
	}

	void checkDynamic(SessionVars sVars) throws Exception {
		int row = getRow(sVars);
		int column = getColumn(sVars);
		if (row > fmd.getNumberOfRows())
			throw new Exception("row of " + row);
		if (row < 0)
			throw new Exception("row of " + row);
		if (column > fmd.get(row).size())
			throw new Exception("column of " + column);
		if (column < 0)
			throw new Exception("column of " + column);
//		fmd.row = row;
//		fmd.column = column;
//		if (fmd.row != row)
//			throw new Exception("stored row of " + fmd.row + " received row of " + row);
//		if (fmd.column != column)
//			throw new Exception("stored column of " + fmd.column + " received column of " + column);
	}

	@Override
	public FormsArray processButtons(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();

		if (ret.cancelClicked(sVars)) {
			// get the dispatch form
			ret.addAll(sVars.getDispatch().getForm(sVars));
			ret.standardForm();
			return ret;
		}

		checkDynamic(sVars);

		boolean returnedWithoutException = false;
		FormsArray retSoFar = null;
		// process the request, report the errors to the user
		try {
			ret.addAll(extractParams(sVars));
			returnedWithoutException = true;
			ret.errorToUser("input not found");
		} catch (EndOfInputException e) {
			// normal end of input processing
			retSoFar = new FormsArray();
			retSoFar = e.getForm();
		} catch (EndOfInputRedoQueries e) {

		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace())
				ret.rawText(ste.toString() + "<br>");
			ret.errorToUser(e);
		}

		if (retSoFar != null)
			ret.addAll(retSoFar);

		// keep the unread returnedWithoutException from causing an
		// unused variable warning. Keep for breakpoint
		if (returnedWithoutException)
			returnedWithoutException = true;

		// if one of the methods that processed the request want to go back to the
		// dispatch screen
		if (ret.status == FormsArray.STATUS.BACKTODISPATCH) {
//			Internals.logWithDate();
			try {
				ret.addAll(sVars.getDispatch().getForm(sVars));
			} catch (Exception e) {
				ret.errorToUser(e);
			}
			ret.standardForm();
			return ret;
		}

		// get the form for the next screen
		try {
//			Internals.logWithDate();
			ret.addAll(getForm(sVars));
//			Internals.logWithDate();
		} catch (Exception e) {
			ret.errorToUser(e);
		}

		// if getForm didn't get far enough to set RETURNTO
		if (ret.returnToString == null) {
//			Internals.logWithDate();
			SmartForm login = null;
			try {
				login = sVars.getLoginForm();
				// go back to the login form
				ret.addAll(login.getForm(sVars));
			} catch (Exception e) {
				// if we can't get a login form, we're broken.
				// try to log the error
				Internals.logStartupError(e);
			}
			// no standard form
			return ret;
		}
		// add the standard ending to the form generated by extractParams
		ret.standardForm();

		try {
			ret.validInternalForm();
		} catch (Exception e) {
			ret.errorToUser(e);
		}
//		Internals.logWithDate();
		return ret;
	}

	private static String MYRETURNTO = Utils.getNextString();

	@Override
	public String getReturnTo() throws Exception {
		return MYRETURNTO;
	}
}