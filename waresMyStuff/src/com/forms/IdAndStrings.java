package com.forms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.db.MyStatement;
import com.db.SessionVars;
import com.forms.SearchTarget.SEARCHTYPES;
import com.security.MyObject;

public class IdAndStrings extends ArrayList<IdAndString> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8594003232659591066L;
// public for testing
	/**
	 * force the user to indicate that he wants to select an object from a list
	 * before displaying any portion of a list.
	 */
	public String REQUESTALIST = Utils.getNextString();
	/**
	 * comes back as a terminator when a selection was made
	 */
	String SELECTFROMLIST = Utils.getNextString();
	public String NEXT = Utils.getNextString();
	public String PREVIOUS = Utils.getNextString();
	/**
	 * cancel selecting from a list
	 */
	String CANCELLIST = Utils.getNextString();
	public SearchOffset so = null;

	public enum OPERATORINPUT {
		NEXT, PREVIOUS, WANTSALIST, CANCELLIST
	}
	
	SessionVars sVars = null;

	/**
	 * when true, ask the user if he wants a list.
	 */
	public boolean giveMeAListButton = true;

//	public enum DISPLAYSTATE {
//		/**
//		 * we haven't heard from the user yet. waiting for an indication that the user
//		 * wants a list.
//		 */
//		SHOWGIVEMEALISTBUTTON,
//
//		/**
//		 * after the first search, no records were found. Stop asking user for a
//		 * selection using this SearchType
//		 */
//		SHOWCANCELONLYBUTTON,
//		/**
//		 * show next button
//		 */
//		SHOWNEXTBUTTON,
//		/**
//		 * show previous button
//		 */
//		SHOWPREVIOUSBUTTON,
//		/**
//		 * show next and previous button
//		 */
//		SHOWNEXTANDPREVIOUSBUTTON
//	};

	public enum DIRECTION {
		UNKNOWN, FORWARD, REVERSE
	};

	boolean waitingForOperatorInput = true;

	// public for testing
//	public DISPLAYSTATE displayState = DISPLAYSTATE.SHOWGIVEMEALISTBUTTON;
	/**
	 * direction of the last submit key. NEXT -> forward, PREVIOUS -> reverse
	 */
	public DIRECTION direction = DIRECTION.FORWARD;

	/**
	 * The number of records to display with each request.
	 */
	public static int DISPLAYSIZE = 12;

	SEARCHTYPES searchType;
	FormsMatrixDynamic fm = null;

	public IdAndStrings(FormsMatrixDynamic fm, SearchTarget.SEARCHTYPES type, SessionVars sVars) {
		searchType = type;
		this.fm = fm;
		this.sVars = sVars;
		clear();
	}

	@Override
	public void clear() {
		giveMeAListButton = true;
		waitingForOperatorInput = true;
		direction = DIRECTION.FORWARD;
		if (so == null)
			so = new SearchOffset();
		else
			so.clear();
		super.clear();
	}

	public boolean contains(IdAndString idAndString) {
		for (int i = 0; i < size(); i++) {
			if (get(i).id == idAndString.id && get(i).string.equals(idAndString.string))
				return true;
		}
		return false;
	}

	public boolean contains(MyObject obj) {
		for (int i = 0; i < size(); i++) {
			if (get(i).id == obj.id && get(i).string.equals(obj.getInstanceName()))
				return true;
		}
		return false;
	}

	public IdAndStrings doQuery() throws Exception {
		super.clear();
		String myQuery = fm.get(fm.row).get(fm.column).getQuery(searchType, so.getNewOffset(direction));
		if (myQuery.isEmpty())
			return this;
		Connection conn = null;
		MyStatement st = null;
		ResultSet rs = null;
		int recordsFound = 0;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			rs = st.executeQuery(myQuery);
			IdAndString tmp;
			while (rs.next()) {
				tmp = new IdAndString();
				tmp.string = rs.getString("name");
				tmp.id = rs.getInt("id");
				add(tmp);
				recordsFound++;
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
		so.storeLastSearchResults(direction, recordsFound);
		return this;
	}

	public void sortByCountsDelete(String searchString) {
		/**
		 * id of the object, count of the hits in name from searchString
		 */
		HashMap<Integer, Integer> idToCounts = new HashMap<Integer, Integer>();
		/**
		 * id of the object, name of the object, used to rebuid idToString after sorting
		 */
		HashMap<Integer, String> idToName = new HashMap<Integer, String>();
		for (String thisWord : searchString.split(" ")) {
			for (IdAndString idAndString : this) {
				if (idAndString.string.contains(thisWord)) {
					if (!idToName.containsKey(idAndString.id))
						idToName.put(idAndString.id, idAndString.string);
					if (!idToCounts.containsKey(idAndString.id))
						idToCounts.put(idAndString.id, 1);
					else
						idToCounts.put(idAndString.id, idToCounts.get(idAndString.id) + 1);
				}
			}
		}

		// build a list of ids for each count
		HashMap<Integer, ArrayList<Integer>> countsToIds = new HashMap<Integer, ArrayList<Integer>>();
		for (HashMap.Entry<Integer, Integer> entry : idToCounts.entrySet()) {
			// getValue from idToCounts is the count of ids. use as a key for countsToIds
			if (countsToIds.containsKey(entry.getValue())) {
				// append this id to the array of ids for this key
				countsToIds.get(entry.getValue()).add(entry.getKey());
			} else {
				// add a new array with this id to the hashmap
				ArrayList<Integer> ids = new ArrayList<Integer>();
				ids.add(entry.getKey());
				countsToIds.put(entry.getValue(), ids);
			}
		}
		// rebuild
		clear();

		// unique counts
		Set<Integer> uniqueCounts = new HashSet<Integer>();
		for (int count : idToCounts.values()) {
			uniqueCounts.add(count);
		}

		// unique and sorted counts
		ArrayList<Integer> uniqueCountsSorted = new ArrayList<Integer>();
		uniqueCounts.forEach((count) -> {
			uniqueCountsSorted.add(count);
		});
		Collections.sort(uniqueCountsSorted);

		for (int countIndex = uniqueCountsSorted.size() - 1; countIndex >= 0; countIndex--) {
			int count = uniqueCountsSorted.get(countIndex);
			// for each id for this count
			for (int id : countsToIds.get(count)) {
				IdAndString tmp = new IdAndString();
				tmp.id = id;
				tmp.string = idToName.get(id);
				add(tmp);
			}
		}
	}

	public FormsArray extractParams(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
		if (sVars.hasParameterKey(CANCELLIST)) {
			processOperatorInput(OPERATORINPUT.CANCELLIST);
			throw new EndOfInputRedoQueries(fm);
		}
		if (sVars.hasParameterKey(NEXT)) {
			processOperatorInput(OPERATORINPUT.NEXT);
			throw new EndOfInputRedoQueries(fm);
		}
		if (sVars.hasParameterKey(PREVIOUS)) {
			processOperatorInput(OPERATORINPUT.PREVIOUS);
			throw new EndOfInputRedoQueries(fm);
		}
		if (sVars.hasParameterKey(REQUESTALIST)) {
			processOperatorInput(OPERATORINPUT.WANTSALIST);
			throw new EndOfInputRedoQueries(fm);
		}
		if (sVars.getParameterKeys().contains(SELECTFROMLIST)) {
			int id = -1;
			try {
				id = Integer.parseInt(sVars.getParameterValue(SELECTFROMLIST));
			} catch (Exception e) {
				ret.errorToUser("Please make a selection before clicking the Select button");
				throw new EndOfInputException(ret);
			}
			MyObject obj = fm.getObject();
			obj.find(id);
			fm.resetAllIdAndStrings();
			throw new EndOfInputRedoQueries(fm);
		}

		return ret;
	}

	public FormsArray getForm(SessionVars sVars) throws Exception {
		FormsArray ret = new FormsArray();
		if (fm.getObject().isLoaded())
			return ret;
		// if the object does not have an inventory field
		if (searchType == SearchTarget.SEARCHTYPES.INVENTORY && !fm.getObject().hasInventoryField())
			return ret;
		if (giveMeAListButton) {
			ret.submitButton("Select from " + SearchTarget.getIdAndStringLabels(searchType) + " list", REQUESTALIST);
			return ret;
		}
		doQuery();
		if (isEmpty()) {
			if (!fm.getObject().searchString.isEmpty()) {
				ret.rawText("Nothing found in " + SearchTarget.getIdAndStringLabels(searchType) + " list with "
						+ fm.getObject().searchString);
				ret.newLine();
			}
		} else {
			ret.startTable();
			ret.startRow();
			ret.startBold();
			ret.spanTextColumn(SearchTarget.getIdAndStringLabels(searchType) + " list", 2);
			ret.endBold();
			ret.endRow();
			ret.startRow();
			ret.startSingleSelection(SELECTFROMLIST, Math.min(DISPLAYSIZE, this.size()), false);
			IdAndString tmp = new IdAndString();
			Iterator<IdAndString> itr = iterator();
			while (itr.hasNext()) {
				tmp = itr.next();
				ret.addSelectionOption("" + tmp.id, tmp.string);
			}
			ret.endSingleSelection();
			ret.endTable();
			ret.submitButton("Submit selected object from " + SearchTarget.getIdAndStringLabels(searchType),
					SELECTFROMLIST);
			if (so.showNextButton())
				ret.addAll(nextButton());
			if (so.showPreviousButton())
				ret.addAll(previousButton());
			if (!giveMeAListButton)
				ret.addAll(cancelListButton());

//			switch (displayState) {
//			case SHOWNEXTBUTTON:
//				ret.addAll(nextButton());
//				ret.addAll(cancelListButton());
//				break;
//			case SHOWPREVIOUSBUTTON:
//				ret.addAll(previousButton());
//				ret.addAll(cancelListButton());
//				break;
//			case SHOWNEXTANDPREVIOUSBUTTON:
//				ret.addAll(previousButton());
//				ret.addAll(nextButton());
//				ret.addAll(cancelListButton());
//				break;
//			case SHOWGIVEMEALISTBUTTON:
//				// done earlier in getForm
//				break;
//			case SHOWCANCELONLYBUTTON:
//				ret.addAll(cancelListButton());
//				break;
//			}
		}
		return ret;
	}

	FormsArray nextButton() {
		FormsArray ret = new FormsArray();
		ret.submitButton("Next " + DISPLAYSIZE + " objects from " + SearchTarget.getIdAndStringLabels(searchType),
				NEXT);
		return ret;
	}

	FormsArray previousButton() {
		FormsArray ret = new FormsArray();
		ret.submitButton("Previous " + DISPLAYSIZE + " objects from " + SearchTarget.getIdAndStringLabels(searchType),
				PREVIOUS);
		return ret;
	}

	FormsArray cancelListButton() {
		FormsArray ret = new FormsArray();
		ret.submitButton("Cancel selecting objects from " + SearchTarget.getIdAndStringLabels(searchType), CANCELLIST);
		return ret;
	}

	public void processOperatorInput(OPERATORINPUT input) {
		waitingForOperatorInput = false;
		switch (input) {
		case CANCELLIST:
			giveMeAListButton = true;
			break;
		case NEXT:
			// no NEXT button is offered to the user if the records have been exhausted
			direction = DIRECTION.FORWARD;
			break;
		case PREVIOUS:
			direction = DIRECTION.REVERSE;
			break;
		case WANTSALIST:
			giveMeAListButton = false;
			break;
		}

//			break;
//		}
		waitingForOperatorInput = true;
	}
}
