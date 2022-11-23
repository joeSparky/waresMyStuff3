package com.forms;

import com.db.SessionVars;
import com.security.MyObject;

public class SelectIdFromList extends SmartForm {
//	private static final int KEYWORDLENGTH = 50;

	/**
	 * select from a list using keywords and a list
	 * 
	 * @param master
	 * @param sVars
	 * @param keywordsLabel
	 * @param overallLabel
	 * @param buttons
	 */
	public SelectIdFromList(FormsMatrixDynamic fmd, SessionVars sVars) {
		super(fmd, sVars);
//		this.keywordsLabel = keywordsLabel;
//		this.overallLabel = overallLabel;
////		searchTarget = mySearchTarget;
//		this.fm = fm;
//		resetForm(INSTATE.STANDARDFORM);
	}

//	FilteredList searchTarget;
//	FormsMatrixDynamic fm;

	// MyObjectsArray objs = null;

//	protected SelectIdFromList() {
//		super(null);
////		clear();
//	}

//	private void clear() {
////		selectLabel = "";
//		keywordsLabel = "";
//	}

//	public void setOveralLabel(String str) {
//		overallLabel = str;
//	}

//	public String SELECT = Utils.getNextString();
	private String SEARCH = Utils.getNextString();
//	/**
//	 * The user wants to select an object. Give the user a menu.
//	 */
//	private String WANTSTOSELECT = Utils.getNextString();
//	/**
//	 * The user wants to see a previous menu.
//	 */
//	private String PREVIOUS = Utils.getNextString();
//	/**
//	 * The user wants to see the next menu.
//	 */
//	private String NEXT = Utils.getNextString();
//	private String keywordsLabel;
//	private String overallLabel = null;

//	boolean wantsToSelect = false;
//	boolean wantsPrevious = false;
//	boolean wantsNext = false;

	/**
	 * the first entry of memberList menu that was displayed to the user
	 */
//	public static int DISPLAYSIZE = 5;

	@Override
	public FormsArray extractParams(SessionVars sVars) throws Exception {
		getFormAlreadyDone = false;
		MyObject obj = fmd.getObject();
		FormsArray ret = new FormsArray();
//		if (isDisabled())
//			return ret;
		if (sVars.hasParameterKey(SEARCH) && (!sVars.getParameterValue(SEARCH).equals(obj.searchString))) {
			obj.searchString = sVars.getParameterValue(SEARCH).toLowerCase();
			fmd.resetAllIdAndStrings();
			throw new EndOfInputRedoQueries(fmd);
		}
		ret.addAll(fmd.getRow().get(fmd.column).extractParams(sVars));
		return ret;
	}

	@Override
	public FormsArray getForm(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
		getFormAlreadyDone = true;
		ret.addAll(fmd.getRow().get(fmd.column).getForm(sVars));
		return ret;
	}

//	private boolean needKeyWordSearchBox(SearchTarget selectIdInfo, String label) {
//		if (!selectIdInfo.obj.searchString.isEmpty())
//			return true;
//		return label != null && (!selectIdInfo.ancestorTargets.isEmpty()) || !selectIdInfo.descendantTargets.isEmpty()
//				|| !selectIdInfo.allTargets.isEmpty();
//	}

//	private FormsArray dumpButtons() {
//		FormsArray ret = new FormsArray();
//		ret.submitButtonIgnoreTable("Select", SELECT);
//		return ret;
//	}

//	@Override
//	public void resetForm(INSTATE inState) {
//		this.inState = inState;
//	}

//	FormsArray dumpSearchForm(IdAndStrings searchArray, String title, SearchTarget selectIdInfo) throws Exception {
//		FormsArray ret = new FormsArray();
//		if (searchArray.isEmpty()) {
//			if (!selectIdInfo.obj.searchString.isEmpty()) {
////				ret.newLine();
//				ret.rawText("With " + keywordsLabel + ", '" + selectIdInfo.obj.searchString + "', not found in " + title
//						+ " " + overallLabel);
//				ret.newLine();
//			}
//			return ret;
//		}
//		ret.startTable();
//		if (overallLabel != null) {
//			ret.startRow();
//			ret.startBold();
//			ret.spanTextColumn(overallLabel + " : " + title, 2);
//			// ret.rawText(selectLabel);
//			ret.endBold();
//			ret.endRow();
//		}
//
//		// if the user has entered any search criteria and nothing was found in
//		// ancestors
//		if (!selectIdInfo.obj.searchString.isEmpty() && searchArray.isEmpty()) {
//			ret.startRow();
//			ret.rawText("Nothing found with " + selectIdInfo.obj.searchString);
//			ret.endRow();
//			ret.endTable();
//			return ret;
//		}
//		ret.startRow();
//
//		if (!searchArray.isEmpty()) {
//			ret.startSingleSelection(SELECT, IdAndStrings.DISPLAYSIZE, false);
//			IdAndString tmp = new IdAndString();
//			Iterator <IdAndString> itr = searchArray.iterator();
//			while (itr.hasNext()) {
//				tmp = itr.next();
//				ret.addSelectionOption("" + tmp.id, tmp.string);
//			}
//			ret.endSingleSelection();
//		}
//		ret.endTable();
//		return ret;
//	}
}
