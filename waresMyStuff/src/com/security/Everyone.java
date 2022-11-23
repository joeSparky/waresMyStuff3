package com.security;

import java.util.HashMap;

public class Everyone extends HashMap<Class<?>, MyObject>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3202714938118924148L;
	
	public void put(MyObject obj){
		super.put(obj.getClass(), obj);
	}
//	@Override
//	public MyObject put(Class<?> c, MyObject o){
//		Internals.dumpStringExit("use put(myObject)");
//		return null;
//	}

}
