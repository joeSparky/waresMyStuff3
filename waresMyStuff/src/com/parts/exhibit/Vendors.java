package com.parts.exhibit;

import java.util.HashSet;

public class Vendors extends HashSet<Vendor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Vendors justOne(Vendor vendor) {
		Vendors ret = new Vendors();
		ret.add(vendor);
		return ret;
	}
	
}
