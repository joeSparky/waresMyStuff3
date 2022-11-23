package com.parts.exhibit;

import java.util.HashSet;

public class PartNumbers extends HashSet<PartNumber> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PartNumbers justOne(PartNumber vendor) {
		PartNumbers ret = new PartNumbers();
		ret.add(vendor);
		return ret;
	}
	
}
