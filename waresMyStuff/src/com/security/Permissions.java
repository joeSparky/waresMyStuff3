package com.security;

import java.util.HashSet;

public class Permissions extends HashSet<Permission> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5398505284736316553L;

//	public static SearchTarget toSearch(Permissions exhibits) {
//		SearchTarget s = new SearchTarget();
////		s.targets = new MyObjects();
////		for (Permission p : exhibits)
////			s.targets.add(p.toSearch());
//		return s;
//	}

	public Permissions fromString(String str) throws Exception {
		Permissions permissions = new Permissions();
		if (str.isEmpty())
			return permissions;
		for (String s : str.split("_")) {
//			try {
				permissions.add(Permission.findEnum(s));
//			} catch (Exception e) {
//				Internals.dumpExceptionExit(e);
//			}
		}
		return permissions;
	}

	@Override
	public String toString() {
		String ret = "";
		boolean first = true;
		for (Permission p : this) {
			if (first) {
				ret += "" + p.ordinal();
				first = false;
			} else
				ret += "_" + p.ordinal();
		}
		return ret;
	}

}
