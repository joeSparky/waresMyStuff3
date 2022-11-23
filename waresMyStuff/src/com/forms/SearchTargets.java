package com.forms;

import java.util.ArrayList;

import com.db.SessionVars;
import com.security.Anchor;
import com.security.MyObject;
import com.security.MyObjectsArray;

/**
 * A row of tabs with an individual tab being a SearchTarget.
 * 
 * @author joe
 *
 */
public class SearchTargets extends ArrayList<SearchTarget> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1297625850611117138L;
	protected SessionVars sVars = null;

	public SearchTargets(SessionVars sVars) {
		this.sVars = sVars;
//		this.fmd = fmd;
	}

//	public SearchTargets() {
//	}

//	public void setFormsMatrixDynamic(FormsMatrixDynamic fmd) {
////		this.fmd = fmd;
//		for (fmd.column = 0; fmd.column < size(); fmd.column++)
//			this.get(fmd.column).fmd = fmd;
//	}
//	@Override
//	public boolean add(FilteredList list) {
//		boolean returnFlag = super.add(list);
////		for (int i=0; i<list.size(); i++) {
////			
////		}
//		
//		list.fmd = this.fmd;
//		return returnFlag;
//	}

//	protected FormsMatrixDynamic fmd = null;

	public boolean needsUpdate = false;
//	public FilteredListInterface listInterface = new FilteredList();

	/**
	 * find me in this, return the index or -1 if not found
	 * 
	 * @param me
	 * @return
	 * @throws Exception
	 */
	public int findIndexOfObject(MyObject me) throws Exception {
		for (int i = 0; i < size(); i++)
			if (get(i).obj.getMyFileName().equals(me.getMyFileName()))
				return i;
		return -1;
	}

	public int getLargestRecursionIndex() {
		for (int i = size() - 1; i >= 0; i--)
			if (get(i).obj.isRecursive())
				return i;
		return -1;
	}

	public MyObject getObjectUsingClassName(MyObject lookingFor) throws Exception {
		int index = findIndexOfObject(lookingFor);
		if (index < 0)
			throw new Exception(
					"can not find " + lookingFor.getInstanceName() + " at " + new Exception().getStackTrace()[0]);
		return get(index).obj;
	}

//	public void setListInterface(FilteredListInterface listInterface) {
//		this.listInterface = listInterface;
//	}

	/**
	 * assuming there are 2 instances of the same class, find the one that's not me
	 * 
	 * @param me
	 * @return
	 * @throws Exception
	 */
	public int findOtherObjectUsingClassName(MyObject me) throws Exception {
		for (int i = 0; i < size(); i++)
			if (get(i).obj.getMyFileName().equals(me.getMyFileName()) && get(i).obj.id != me.id)
				return i;
		throw new Exception(me.getMyFileName() + " is not in stack");
	}

	public boolean canHaveChildren(MyObject me) throws Exception {
		return findIndexOfObject(me) < size() - 1;
	}

	public boolean canHaveParent(MyObject me) throws Exception {
		return findIndexOfObject(me) > 0;
	}

	/**
	 * clear everyone below me
	 * 
	 * @param me
	 * @throws Exception
	 */
	public void clearChildren(int myIndex) throws Exception {
//		for (int i = myIndex + 1; i < size(); i++)
//			get(i).obj.clear();
	}

	// public FilterParams params;

	// public FilteredLists results = null;

//	public FilteredList getFilteredList(int index) {
//		return get(index);
//	}

//	public SearchArray getFilteredListIdStrings(int index) {
//		return get(index).searchArray;
//	}

	/**
	 * force a re-evaluation of the
	 * 
	 * @throws Exception
	 */
//	public void updateFilteredList() throws Exception {
//
//		if (listInterface == null)
//			throw new Exception("listInterface not set");
//		if (isEmpty())
//			throw new Exception("empty");
//		// if the top instance is not selected
//		if (!get(0).obj.isLoaded())
//			throw new Exception(get(0).obj.getMyFileName() + " is not selected.");
//
//		initResults();
//
//		// Put the children of the lowest selected instance into targets.
//		// Overridden by location
//		listInterface.setDescendants(this);
//
//		// from the bottom up
//		listInterface.setAncestors(this);
//
//		listInterface.setAll(this);
//
//		listInterface.setOrphans(this);
//
//		listInterface.setOldestInventory(this);
//
//		listInterface.setOldestLinkInventory(this);
//
//		listInterface.applySearch(this);
//
//	}

	/**
	 * get all of the ancestors of EXTERNALRECURSIVE links. The current MyObject
	 * will be the first item in the array. The last item will be the highest
	 * member.
	 * 
	 * @param parentType
	 * @return
	 * @throws Exception
	 */
//	public void getAncestorsRecursive(MyObjectsArray classes, int startingIndex) throws Exception {
//		// while the current target is loaded and not at the top of the list
//		while (classes.get(startingIndex).isLoaded() && startingIndex > 0) {
//			MyObjects parents = classes.get(startingIndex).getParents(classes.get(startingIndex - 1));
//			if (parents.size() == 1) {
//				// set the parent of the current child
//				classes.get(startingIndex - 1).find(parents.iterator().next().id);
//				getAncestorsRecursive(classes, startingIndex - 1);
//			} else
//				// stop the recursion
//				startingIndex = 0;
//		}
//		return;
//	}

//	public MyObjects getParents(int indexOfChild) throws Exception {
//		if (indexOfChild > 0)
//			return new MyObjects().getParents(get(indexOfChild - 1).obj, get(indexOfChild).obj);
//		else
//			return new MyObjects();
//	}

	/**
	 * initialize the SearchTargets array. Return the index of the lowest selected
	 * object.
	 * 
	 * @param myResults
	 * @return
	 */
	public void initResults() {

		// for each object in MyObjectsArray
		for (int myIndex = 0; myIndex < size(); myIndex++) {
			get(myIndex).clear();
//			if (get(myIndex).obj.isLoaded()) {
			// get(myIndex).descendantTargets.add(IdAndString.toIdAndString(get(myIndex).obj));
//				get(myIndex).ancestorTargets.add(get(myIndex).obj);
			// get(myIndex).allTargets.add(IdAndString.toIdAndString(get(myIndex).obj));
//			}
		}
//		ancestorSource = descendantSource = "";
	}

	/**
	 * get the index of the lowest loaded object. return -1 if none are loaded
	 * 
	 * @return
	 */
	public int getLowestLoadedIndex() {
		for (int myIndex = size() - 1; myIndex >= 0; myIndex--) {
			if (get(myIndex).obj.isLoaded())
				return myIndex;
		}
		return -1;
	}

	/**
	 * find the first loaded object above myLevel
	 * 
	 * @param myLevel
	 * @return
	 */
	public int getLowestLoadedIndexAbove(int myLevel) {
		if (myLevel == 0)
			return -1;
		for (int myIndex = myLevel - 1; myIndex >= 0; myIndex--) {
			if (get(myIndex).obj.isLoaded())
				return myIndex;
		}
		return -1;
	}

	/**
	 * find the first loaded object below myLevel
	 * 
	 * @param myLevel
	 * @return
	 */
	public int getHighestLoadedIndexBelow(int myLevel) {
		if (myLevel >= size() - 1)
			return -1;
		for (int myIndex = myLevel + 1; myIndex < size(); myIndex++) {
			if (get(myIndex).obj.isLoaded())
				return myIndex;
		}
		return -1;
	}

	public MyObjectsArray getObjects() {
		MyObjectsArray objs = new MyObjectsArray();
		for (int i = 0; i < size(); i++)
			objs.add(get(i).obj);
		return objs;
	}

	// legacy adds
	public void add(MyObject obj) throws Exception {
		add(obj, SearchTarget.EDITSELECTTYPE.EDITANDSELECT);
	}

	public void add(MyObject obj, SearchTarget.EDITSELECTTYPE editSelectType) throws Exception {
//		if (fmd == null)
//			throw new Exception("FormsMatrixDynamic fmd is null");
		SearchTarget st = new SearchTarget(obj, sVars);
		st.obj = obj;
		st.editSelectType = editSelectType;
		this.add(st);
	}

//	public void addWithUpdate(MyObject obj, SearchTarget.EDITSELECTTYPE editSelectType) throws Exception {
//		if (fmd == null)
//			throw new Exception("FormsMatrixDynamic fmd is null");
//		FilteredList st = new FilteredList(fmd);
//		st.obj = obj;
//		st.editSelectType = editSelectType;
////		st.updateNeeded = true;
//		this.add(st);
//	}

//
//	public String isAnchorInstanceSet(MyObject whosAskingForAnchor) throws Exception {
//		return listInterface.isAnchorInstanceSet(this, whosAskingForAnchor);
//	}

	/**
	 * check from the caller to the top of the array (lowest index) if every
	 * instance is loaded and there's a link between each layer except for the layer
	 * between the caller and the object above it. The part / location pair uses
	 * this to verify that all of links are setup between the company and the
	 * location.
	 * 
	 * @param indexOfCaller
	 * @return
	 * @throws Exception
	 */
	public boolean linkedAndLoadedToTheTop(int indexOfCaller) throws Exception {
		// check for every object is loaded
		for (int column = 0; column < indexOfCaller; column++)
			if (!get(column).obj.isLoaded())
				return false;
		// check for every link but the highest exists
		for (int column = 0; column < indexOfCaller - 1; column++)
			if (!get(column).obj.linkToChildExists(get(column + 1).obj))
				return false;
		return true;
	}

	public Anchor getSearchTargetsAnchor(int whosAskingIndex) throws Exception {
//		return listInterface.getAnchorInterface(this, whosAsking);
//	}
//	public Anchor getAnchorInterface(SearchTargets objs, MyObject whosAsking) throws Exception {
		int lowestLoadedObjectAboveWhosAsking = getLowestLoadedIndexAbove(whosAskingIndex);
		if (lowestLoadedObjectAboveWhosAsking == -1)
			throw new Exception("anchor not found");
		return get(lowestLoadedObjectAboveWhosAsking).obj.getAnchor();
//		MyObject obj = getObjects().get(0);
//		if (!obj.isLoaded())
//			throw new Exception(obj.getMyFileName() + " is not loaded. " + new Exception().getStackTrace()[0]);
//		return obj.getAnchor();
	}

	void setDescendants() throws Exception {
		// if the first entry (company) is not loaded, something is wrong
//		if (!objs.get(0).obj.isLoaded())
//			throw new Exception("first entry not loaded");

		String descendantSource = "";
		// for each column in the row
		for (int myIndex = 0; myIndex < size(); myIndex++) {

			get(myIndex).descendantSource = descendantSource;
			MyObject obj = get(myIndex).obj;
			if (obj.isLoaded()) {
				// update the descendantSource with the latest layer information
				descendantSource = obj.getAName() + ":" + obj.getInstanceName();
				// don't create descendants for a loaded layer
				continue;
			}
		}
	}

//	public String lastJoin(int layer, String linkFileName, String thisLevelFileName) {
//		String ret = "";
//		if (layer + 1 == myColumn)
//			ret += " AND " + linkFileName + ".childId = " + thisLevelFileName + ".id";
//		ret += ")";
//		return ret;
//	}

//	public String setDescendantsQuery() throws Exception {
//		// tables that we've already included in the query
//		Collection<String> tables = new ArrayList<String>();
//for (int tabIndex=0; tabIndex<size(); tabIndex++) {
////		MyObject obj = get(fmd.row).get(fmd.column).obj;
//
//		// the top row can not be descendants (no parents above them)
//		if (fmd.column == 0 ||
//		// or the row is loaded
//				obj.isLoaded()) {
//			// return a query that wont find any records
//			return "";
//		}
//		int lowestSelected = fmd.get(fmd.row).getLowestLoadedIndexAbove(fmd.column);
//		if (lowestSelected == -1)
//			throw new Exception("lowestSelected = -1");
//
//		String thisLevelFileName = obj.getMyFileName();
//		// build a list of link files between the layers
//		ArrayList<String> layers = new ArrayList<String>();
//		for (int layer = lowestSelected; layer < fmd.column; layer++) {
//			layers.add(MyLinkObject.getFileName(fmd.get(fmd.row).get(layer).obj, fmd.get(fmd.row).get(layer + 1).obj));
//		}
//		String query = "";
//		query += "SELECT DISTINCT " + thisLevelFileName + "." + "id, ";
//		query += thisLevelFileName + ".name";
//		query += insertSearchMatch(obj);
//		query += " from ";
//		query += obj.getMyFileName() + " ";
//		tables.add(obj.getMyFileName());
//		// for the first layer, require the parentId of the link file to match the id of
//		// the highest selected object
//		query += "JOIN (" + layers.get(0) + ") ON (" + layers.get(0) + ".parentID="
//				+ fmd.get(fmd.row).get(lowestSelected).obj.id;
//		// if this is the last join of the query, add childId must equal id
//		query += lastJoin(lowestSelected, layers.get(0), thisLevelFileName);
//		// do the remainder of the joins matching the childId of the current level to
//		// the parentId of the next lower level
//		for (int layer = lowestSelected + 1; layer < fmd.column; layer++) {
//			query += "JOIN (" + layers.get(layer) + ") ON (" + layers.get(layer) + ".parentId=" + layers.get(layer - 1)
//					+ ".childId ";
//			query += lastJoin(layer, layers.get(layer), thisLevelFileName);
//		}
//		query += insertScoreThreshold(obj);
//		query += insertOrderByAndLimit(obj, IdAndStrings.DISPLAYSIZE,
//				fmd.getSearchTarget().getIdAndStrings(SEARCHTYPES.DESCENDANTS).getFirstDisplayedRecord());
//		return query;
//	}

//	public String lastJoinAncestors(String linkFileName, String thisLevelFileName) {
//		return " AND " + linkFileName + ".parentId = " + thisLevelFileName + ".id";
//	}

//	public String setAncestorsQuery() throws Exception {
//		// tables that we've already included in the query
////		Collection<String> tables = new ArrayList<String>();
//
//		MyObject obj = fmd.get(fmd.row).get(fmd.column).obj;
//
//		// if the tab is the last in the row, it can not have any ancestors (no children
//		// below it).
//		if (fmd.column >= fmd.get(fmd.row).size() - 2 ||
//		// or the row is loaded
//				obj.isLoaded()) {
//			// return a query that wont find any records
//			return "";
//		}
//		int highestSelected = fmd.get(fmd.row).getHighestLoadedIndexBelow(fmd.column);
//		if (highestSelected == -1)
//			return "";
//
//		String thisLevelFileName = obj.getMyFileName();
//		// build a list of link files between the layers
//		ArrayList<String> layers = new ArrayList<String>();
//		for (int layer = highestSelected; layer > fmd.column; layer--) {
//			layers.add(MyLinkObject.getFileName(fmd.get(fmd.row).get(layer - 1).obj, fmd.get(fmd.row).get(layer).obj));
//		}
//		String query = "";
//		query += "SELECT DISTINCT " + thisLevelFileName + "." + "id, ";
//		query += thisLevelFileName + ".name";
//		query += insertSearchMatch(obj);
//		query += " from ";
//		query += obj.getMyFileName() + " ";
////		tables.add(obj.getMyFileName());
//		// for the first layer, require the parentId of the link file to match the id of
//		// the highest selected object
//		query += "JOIN (" + layers.get(0) + ") ON (" + layers.get(0) + ".childID="
//				+ fmd.get(fmd.row).get(highestSelected).obj.id;
//		// if this is the last join of the query
//		if (fmd.column + 1 == highestSelected)
//			query += " AND " + layers.get(0) + ".parentId = " + thisLevelFileName + ".id)";
//		else
//			query += ")";
//		// do the remainder of the joins matching the parentId of the current level to
//		// the childId of the next higher level
//		for (int layer = 1; layer < layers.size(); layer++) {
//			query += "JOIN (" + layers.get(layer) + ") ON (" + layers.get(layer) + ".childId=" + layers.get(layer - 1)
//					+ ".parentId ";
//			if (layer == layers.size() - 1)
//				query += " AND " + layers.get(layer) + ".parentId = " + thisLevelFileName + ".id)";
//			else
//				query += ")";
//		}
//		query += insertScoreThreshold(obj);
//		query += insertOrderByAndLimit(obj, IdAndStrings.DISPLAYSIZE,
//				fmd.getSearchTarget().getIdAndStrings(SEARCHTYPES.ANCESTORS).getFirstDisplayedRecord());
//		return query;
//	}

//	/**
//	 * insert match() against() as score
//	 * 
//	 * @return
//	 * @throws Exception
//	 */
//	String insertSearchMatch(MyObject obj) throws Exception {
//		String soFar = "";
//		if (obj.searchString.isBlank())
//			return soFar;
//		soFar += ", match(";
//		soFar += obj.getMyFileName();
//		soFar += ".name) against ('";
//		for (String subString : obj.searchString.split("\\W+")) {
//			soFar += subString + "* ";
//		}
//		soFar += "' in BOOLEAN MODE) AS score ";
//		return soFar;
//	}

	/**
	 * insert HAVING score > 0
	 * 
	 * @param obj
	 * @return
	 */
//	String insertScoreThreshold(MyObject obj) {
//		String soFar = "";
//		if (obj.searchString.isBlank())
//			return soFar;
//		soFar += " HAVING score > 0 ";
//		return soFar;
//	}

	/**
	 * insert ORDER BY
	 * 
	 * @param obj
	 * @param skip
	 * @param number
	 * @return
	 */
//	String insertOrderByAndLimit(MyObject obj, int limit, int offset) {
//		String soFar = "";
//		if (!obj.searchString.isBlank())
//			soFar += " ORDER BY score DESC";
//		soFar += " LIMIT " + limit;
//		soFar += " OFFSET " + offset;
//
//		return soFar;
//	}

	/**
	 * set the ancestorTargets for unloaded objects that have a loaded object below
	 * them
	 */
//	void setAncestors() throws Exception {
//		MyObject obj = fmd.get(fmd.row).get(fmd.column).obj;
//		String ancestorSource = "";
//		// from the bottom up
//		for (int index = fmd.getRowSize() - 1; index > 0; index--) {
//			fmd.get(fmd.row).get(fmd.column).ancestorSource = ancestorSource;
//			// if the current layer is loaded
//			if (obj.isLoaded()) {
//				ancestorSource = obj.getAName() + ":" + obj.getInstanceName();
//				// don't bother with ancestors for a loaded object
//				continue;
//			}
//		}
//	}

	/**
	 * create a query string for creating a list of descendants for the index level
	 * 
	 * @param objs
	 * @param index (which level we're working on)
	 * @return
	 * @throws Exception
	 */
//	public String setAncestorsQueryDelete() throws Exception {
//		// tables included in the query so far
//		Collection<String> tables = new ArrayList<String>();
//		MyObject obj = fmd.get(fmd.row).get(fmd.column).obj;
//		String thisLevelFileName = obj.getMyFileName();
//		String query = "";
//		// if the caller wants the ancestors of the highest row
//		if (fmd.column == 0
//				// or the ancestors of a loaded row
//				|| obj.isLoaded()) {
//			// return a query string that will find no records
//			return "";
//		}
//		int highestSelected = fmd.get(fmd.row).getHighestLoadedIndexBelow(fmd.column);
//		// if nobody below me is selected
//		if (highestSelected == -1)
//			return "";
//
//		query += "SELECT DISTINCT " + thisLevelFileName + "." + "id, ";
//		query += thisLevelFileName + ".name";
//		query += insertSearchMatch(obj);
//		query += "from ";
//		query += thisLevelFileName + " ";
//		tables.add(thisLevelFileName);
//		query += "JOIN (";
//		// individual files (not link files)
//		boolean commaNeeded = false;
//		for (int i = highestSelected; i > fmd.column; i--) {
//			if (!tables.contains(fmd.get(fmd.row).get(i).obj.getMyFileName())) {
//				if (commaNeeded)
//					query += ",";
//				query += fmd.get(fmd.row).get(i).obj.getMyFileName();
//				tables.add(fmd.get(fmd.row).get(i).obj.getMyFileName());
//				commaNeeded = true;
//			}
//		}
//
//		// link files
//		for (int i = highestSelected - 1; i >= fmd.column; i--) {
//			if (commaNeeded)
//				query += ",";
//			query += MyLinkObject.getFileName(fmd.get(fmd.row).get(i).obj, fmd.get(fmd.row).get(i + 1).obj);
//		}
//		// end of JOIN
//		query += ")";
//		// start ON
//		query += " ON (";
//		query += fmd.get(fmd.row).get(highestSelected).obj.getMyFileName() + ".id='"
//				+ fmd.get(fmd.row).get(highestSelected).obj.id + "'";
//		for (int i = highestSelected - 1; i >= fmd.column; i--) {
//			query += " AND " + MyLinkObject.getFileName(fmd.get(fmd.row).get(i).obj, fmd.get(fmd.row).get(i + 1).obj)
//					+ ".parentId=";
//			query += fmd.get(fmd.row).get(i).obj.getMyFileName() + ".id";
//			query += " AND " + MyLinkObject.getFileName(fmd.get(fmd.row).get(i).obj, fmd.get(fmd.row).get(i + 1).obj)
//					+ ".childId=";
//			query += fmd.get(fmd.row).get(i + 1).obj.getMyFileName() + ".id";
//		}
//
//		// end of ON
//		query += ")";
//
//		query += insertScoreThreshold(obj);
//		query += insertOrderByAndLimit(obj, IdAndStrings.DISPLAYSIZE,
//				fmd.get(fmd.row).get(fmd.column).getIdAndStrings(SEARCHTYPES.ANCESTORS).getFirstDisplayedRecord());
//		return query;
//	}

	/**
	 * get all objects at the "index" level
	 * 
	 * @param objs
	 * @param index
	 * @return
	 * @throws Exception
	 */
//	public String setAllQuery() throws Exception {
//		MyObject obj = fmd.get(fmd.row).get(fmd.column).obj;
//		if (obj.isLoaded()) {
//			return "";
//		}
//		Anchor allAnchor = fmd.getAnchor();
//		String thisLevelFileName = obj.getMyFileName();
//		String query = "";
//		query += "SELECT " + thisLevelFileName + "." + "id, ";
//		query += thisLevelFileName + ".name";
//		query += insertSearchMatch(obj);
//		query += " from ";
//		query += thisLevelFileName + " ";
//		query += "WHERE (";
//		query += thisLevelFileName + ".anchorId='" + allAnchor.id + "\')";
//
//		query += insertScoreThreshold(obj);
//		query += insertOrderByAndLimit(obj, IdAndStrings.DISPLAYSIZE,
//				fmd.getSearchTarget().getIdAndStrings(SEARCHTYPES.ALL).getFirstDisplayedRecord());
//		return query;
//	}

//	public boolean hasGoodStuff(String lookIn, String lookFor) {
//
//		for (String lookInWord : lookIn.toLowerCase().split(" ")) {
//			for (String lookForWord : lookFor.split(" ")) {
//				if (lookInWord.contains(lookForWord)) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}

	// from the element just above the lowest selected element to the top
//	public void setParents(SearchTargets objs) throws Exception {
//		if (objs.lowestSelectedIndex > 0) {
//			for (int myIndex = objs.lowestSelectedIndex - 1; myIndex >= 0; myIndex--) {
//				SearchTarget thisPass = objs.get(myIndex);
//				if (thisPass.obj.isLoaded())
//					continue;
//				for (MyObject o : objs.get(myIndex + 1).targets) {
//					thisPass.targets.addAll(o.getParents(objs.get(myIndex).obj));
//				}
//			}
//		}
//	}

//	public void selectParentsInterface(SearchTargets objs, int index) throws Exception {
//		if (!objs.get(index).obj.isLoaded())
//			throw new Exception("not loaded");
//		for (int i = index; i > 0; i--) {
//			MyObject aboveMe = objs.get(i - 1).obj;
//			MyObject thatsMe = objs.get(i).obj;
//			MyObjects myParents = thatsMe.getParents(aboveMe);
//			if (myParents.size() == 1)
//				// set the parent
//				aboveMe.find(myParents.iterator().next().id);
//			else
//				break;
//		}
//	}

//	@Override
//	public Anchor getAnchorInterface(SearchTargets objs, MyObject whosAsking) throws Exception {
//		if (!objs.get(0).obj.isLoaded())
//			throw new Exception(
//					objs.get(0).obj.getMyFileName() + " is not loaded. " + new Exception().getStackTrace()[0]);
//		return objs.get(0).obj.getAnchor();
//	}

//	@Override
//	public String isAnchorInstanceSet(SearchTargets objs, MyObject whosAskingForAnchor) {
//		// the subclass is looking for a warehouse
//		return "Should not see this in " + new Exception().getStackTrace()[0];
//	}

//	// return the first selected layer above thisLayer or -1 if there is none
//	public int firstSelectedLayerAbove(SearchTargets objs, int thisLayer) {
//		for (int i = thisLayer - 1; i >= 0; i--) {
//			if (objs.get(i).obj.isLoaded())
//				return i;
//		}
//		return -1;
//	}

//	// return the first selected layer below thisLayer or -1 if there is none
//	public int firstSelectedLayerBelow(SearchTargets objs, int thisLayer) {
//		for (int i = thisLayer + 1; i < objs.size(); i++) {
//			if (objs.get(i).obj.isLoaded())
//				return i;
//		}
//		return -1;
//	}

//	public void setAll(SearchTargets objs) throws Exception {
//		// if the first entry (company) is not loaded, something is wrong
//		if (!objs.get(0).obj.isLoaded())
//			throw new Exception("first entry not loaded");
//
//		Anchor lowestAnchor = objs.get(0).obj.getAnchor();
//		// if the level below is not loaded, the descendants of this level are added to
//		// descendantTargets of the level below
//		for (int myIndex = 0; myIndex < objs.size(); myIndex++) {
//			// if the object is loaded, update the anchor
//			if (objs.get(myIndex).obj.isLoaded()) {
//				// lowestAnchor = objs.get(myIndex).obj.getAnchor();
//				// if the object at this level is self-anchoring
//				try {
//					// update the lowestAnchor
//					Anchor tmpAnchor = new Anchor(sVars).find(objs.get(myIndex).obj);
//					lowestAnchor = tmpAnchor;
//				} catch (AnchorNotFoundException e) {
//					// otherwise, keep the lowestAnchor
//				}
//			}
////			objs.get(myIndex).allTargets.storeQuery(setAllQuery(objs, myIndex, lowestAnchor.id));
//		}
//	}

//	@Override
//	public void setOrphans(SearchTargets objs) throws Exception {
//		// if the first entry is not loaded, something is wrong
//		if (!objs.get(0).obj.isLoaded())
//			throw new Exception("first entry not loaded");
//
//		Anchor lowestAnchor = objs.get(0).obj.getAnchor();
//		// no orphans on the top layer (need a parent type to be an orphan)
//		for (int myIndex = 1; myIndex < objs.size(); myIndex++) {
//			// if the object is loaded, update the anchor
//			if (objs.get(myIndex).obj.isLoaded()) {
//				// lowestAnchor = objs.get(myIndex).obj.getAnchor();
//				// if the object at this level is self-anchoring
//				try {
//					// update the lowestAnchor
//					Anchor tmpAnchor = new Anchor(sVars).find(objs.get(myIndex).obj);
//					lowestAnchor = tmpAnchor;
//				} catch (AnchorNotFoundException e) {
//					// otherwise, keep the lowestAnchor
//				}
//			}
//
////			objs.get(myIndex).orphanTargets.storeQuery(setOrphanQuery(objs, myIndex, lowestAnchor.id));
//		}
//
//	}

//	public String setOrphanQuery() throws Exception {
//
//		MyObject obj = fmd.get(fmd.row).get(fmd.column).obj;
//		Anchor allAnchor = fmd.getAnchor();
//		String thisLevelFileName = obj.getMyFileName();
//		if (obj.isLoaded() || obj.isRecursive()) {
//			// don't show any orphans if the current layer is loaded
//			return "";
//		}
//		String linkFileName = new MyLinkObject(fmd.get(fmd.row).get(fmd.column - 1).obj, obj).getMyFileName();
//		String query = "";
//		query += "SELECT " + thisLevelFileName + "." + "id, ";
//		query += thisLevelFileName + ".name";
//		query += insertSearchMatch(obj);
//		query += " from ";
//		query += thisLevelFileName + " ";
//		query += " LEFT JOIN ";
//		query += linkFileName;
//		query += " ON ";
//		query += thisLevelFileName + ".id=";
//		query += linkFileName + ".childid";
//
//		query += " WHERE (";
//		query += thisLevelFileName + ".anchorId='" + allAnchor.id + "\'";
//		query += " AND ";
//		query += "childid IS NULL";
//
//		query += ")";
//		query += insertScoreThreshold(obj);
//		query += insertOrderByAndLimit(obj, IdAndStrings.DISPLAYSIZE,
//				fmd.get(fmd.row).get(fmd.column).getIdAndStrings(SEARCHTYPES.ORPHANS).getFirstDisplayedRecord());
////		query += " ORDER BY name;";
//		return query;
//	}

////	@Override
//	public void applySearch(SearchTargets objs) throws Exception {
//		Internals.logWithDate();
//		for (int objectIndex = 0; objectIndex < objs.size(); objectIndex++) {
//			MyObject obj = objs.get(objectIndex).obj;
//			if (obj.searchString != null && !obj.searchString.isEmpty()) {
//				for (SEARCHTYPES s : SEARCHTYPES.values())
//					objs.listInterface.setAll(objs);
//				objs.get(objectIndex).allTargets.sortByCounts(obj.searchString);
//				objs.get(objectIndex).ancestorTargets.sortByCounts(obj.searchString);
//				objs.get(objectIndex).descendantTargets.sortByCounts(obj.searchString);
//				objs.get(objectIndex).orphanTargets.sortByCounts(obj.searchString);
//				objs.get(objectIndex).inventoryTargets.sortByCounts(obj.searchString);
//				objs.get(objectIndex).inventoryLinkTargets.sortByCounts(obj.searchString);
//			}
//		}
//		Internals.logWithDate();
//	}

//	@Override
//	public void setOldestInventory(SearchTargets objs) throws Exception {
//		for (int myIndex = 0; myIndex < objs.size(); myIndex++) {
//			MyObject workingObject = objs.get(myIndex).obj;
////			if (workingObject.hasInventoryField())
////				objs.get(myIndex).inventoryTargets.storeQuery(setInventoryQuery(objs.get(myIndex)));
//		}
//	}

	// ret the least recently inventoried objects that have hasInventoryField set
//	public String setInventoryQuery() throws Exception {
//		String query = "";
//		if (!obj.hasInventoryField())
//			return query;
//		query += "SELECT ";
//		query += obj.getMyFileName() + "." + "id, ";
//		query += obj.getMyFileName() + "." + MyObject.INVENTORYFIELDNAME + ", ";
//		query += "concat(name, ' - ', " + MyObject.INVENTORYFIELDNAME + ") as name ";
////		query += insertSearchMatch(obj);
//		query += "from ";
//		query += obj.getMyFileName();
//		query += " order by `" + MyObject.INVENTORYFIELDNAME + "`, `name` ";
////		query += " limit " + MyObject.INVENTORYRECORDS;
//		return query;
//	}

//	@Override
//	public void setOldestLinkInventory(SearchTargets objs) throws Exception {
//		for (int myIndex = 0; myIndex < objs.size(); myIndex++) {
//			MyObject workingObject = objs.get(myIndex).obj;
//			int belowMeIndex = myIndex + 1;
//			// if not at the bottom of objs
//			if (belowMeIndex < objs.size()) {
//				MyObject belowMeObject = objs.get(belowMeIndex).obj;
//				// and i'm not loaded
//				if (!workingObject.isLoaded()) {
//					// and we have an inventory link
////					if (workingObject.hasInventoryLinkWith(belowMeObject)) {
////						objs.get(myIndex).inventoryLinkTargets.storeQuery(setInventoryLinkQuery(workingObject, belowMeObject));
////					}
//				}
//			}
//		}
//	}

	// ret the least recently inventoried objects that have hasInventoryField set
//	public String setInventoryLinkQuery() throws Exception {
//		// if fmd.column is at the end of the list, there can't be an object below it
//		// for
//		// the inventory link
//		if (fmd.column >= fmd.get(fmd.row).size() - 1)
//			return "";
//		MyObject parent = fmd.get(fmd.row).get(fmd.column).obj;
//		MyObject child = fmd.get(fmd.row).get(fmd.column + 1).obj;
//		MyLinkObject mlo = new MyLinkObject(parent, child);
//		String linkFileName = mlo.getMyFileName();
//		String parentFileName = parent.getMyFileName();
////		String childFileName = child.getMyFileName();
//		String query = "";
//		query += "SELECT ";
//		query += parentFileName + "." + "id, ";
//		query += "concat(" + parentFileName + ".name, \" - \", " + linkFileName + ".inventoryDate) as name ";
//		query += "from ";
//		query += linkFileName + " inner join " + parentFileName + " on " + parentFileName + ".id=" + mlo.getMyFileName()
//				+ ".parentId ";
//		if (child.isLoaded())
//			query += " and " + mlo.getMyFileName() + ".childId=" + child.id;
//		query += " order by `" + MyObject.INVENTORYFIELDNAME + "`, `name` ";
//		query += " LIMIT " + MyObject.INVENTORYRECORDS;
//		return query;
//	}

//	public String getQuery(SEARCHTYPES type) throws Exception {
//		switch (type) {
//		case ALL:
//			return setAllQuery();
//		case ANCESTORS:
//			return setAncestorsQuery();
//		case DESCENDANTS:
//			return setDescendantsQuery();
//		case INVENTORY:
//			return setInventoryQuery();
//		case INVENTORYLINKS:
//			return setInventoryLinkQuery();
//		case ORPHANS:
//			return setOrphanQuery();
//		}
//		return "fer shure";
//	}

//	@Override
//	public void setAll(SearchTargets objs) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
}
