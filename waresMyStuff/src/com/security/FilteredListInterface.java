package com.security;

import com.forms.SearchTargets;

public interface FilteredListInterface {
	void setDescendants(SearchTargets objs) throws Exception;
	void setOrphans(SearchTargets objs) throws Exception;
	void setAncestors(SearchTargets objs) throws Exception;
	void setAll(SearchTargets objs) throws Exception;
	void selectParentsInterface(SearchTargets objs, int index) throws Exception;
	void applySearch(SearchTargets objs) throws Exception;
	void setOldestInventory(SearchTargets objs) throws Exception;
	void setOldestLinkInventory(SearchTargets objs) throws Exception;
	Anchor getAnchorInterface(SearchTargets objs, MyObject whosAskingForAnchor) throws Exception;
	String isAnchorInstanceSet(SearchTargets objs, MyObject whosAskingForAnchor);
//	String getQuery(SearchTargets targets, int index, SEARCHTYPES type) throws Exception;
}
