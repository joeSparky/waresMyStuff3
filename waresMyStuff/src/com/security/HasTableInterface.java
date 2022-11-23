package com.security;

import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.Strings;

public interface HasTableInterface {
	public DoubleStrings extendAdd();
	public DoubleStrings extendUpdate();
	public Strings extendNewTable();
	public String getMyFileName() throws Exception;
	public String getCanonicalName();
	public void newTable(SessionVars sVars) throws Exception;
}
