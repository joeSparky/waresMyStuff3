package com.parts.exhibit;

import java.util.HashSet;

public class Kits extends HashSet<Kit> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6138984056374905147L;

	public Kits justOne(Kit exhibit) {
		Kits ret = new Kits();
		ret.add(exhibit);
		return ret;
	}
}
