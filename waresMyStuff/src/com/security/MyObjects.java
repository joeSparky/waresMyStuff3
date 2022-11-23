package com.security;

import java.util.HashSet;
import java.util.Iterator;

import com.db.SessionVars;

public class MyObjects extends HashSet<MyObject> {

	private static final long serialVersionUID = 3099532864055480496L;
	
	public MyObjects getParents(MyObject parentType, MyObject me, SessionVars sVars) throws Exception {
		return new MyLinkObject(parentType, me, sVars).listParentsOfChild();
	}
	
	public MyObjects getChildren(MyObject me, MyObject childType, SessionVars sVars) throws Exception{
		return new MyLinkObject(me, childType, sVars).listChildrenOfParent();
	}
	
	public boolean containsMyObject(MyObject obj) throws Exception {
		Iterator<MyObject> itr = iterator();
		while (itr.hasNext()) {
			if (itr.next().equals(obj))
				return true;
		}
		return false;
	}
	
}
