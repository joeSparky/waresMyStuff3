package com.security;

public enum Permission {
	NOTINITIALIZED("not initialized"), RUNREPORTS("Access to Reports"), ADMINISTRATOR(
			"Administrator"), USER("User"), MANAGER("Manager");
	private String optionDescription;

	Permission(String descr) {
		this.optionDescription = descr;
	}

	public String optionDescription() {
		return optionDescription;
	}

	public static Permission findEnum(String lookFor) throws Exception {
		for (Permission a : Permission.values()) {
			if ((a.ordinal() + "").equals(lookFor)) {
				return a;
			}
		}
		// should never get here
		throw new Exception(lookFor + " was not found in Permission enum");
	}

	public static Permission findEnum(int lookFor) throws Exception {
		for (Permission a : Permission.values()) {
			if (a.ordinal() == lookFor) {
				return a;
			}
		}
		// should never get here
		throw new Exception(lookFor + " was not found in Permission enum");
	}

	public static Permissions getAll() {
		Permissions ret = new Permissions();
		for (Permission a : Permission.values()) {
			if (a.equals(NOTINITIALIZED))
				continue;
			ret.add(a);
		}
		return ret;
	}

	public static String NAME = "Permission";
	public static String NAMES = "Permissions";
	public static String ANAME = "a +" + NAME;

//	public MyObject toSearch() throws Exception {
//		MyObject m = new Role();
//		m.id = ordinal();
////		try {
//			m.setInstanceName(optionDescription);
////		} catch (Exception e) {
////			Internals.dumpExceptionExit(e);
////		}
//		return m;
//
//	}
}
