package com.parts.inOut;

import java.util.HashMap;
import java.util.HashSet;

import com.parts.inOut.OptionEnums.OPTIONS;
import com.security.ExceptionCoding;

public class MustHaves {
	public static void set(OPTIONS first, OPTIONS second) throws ExceptionCoding {
		if (first.getGroup().equals(second.getGroup()))
			throw new ExceptionCoding(first.toString() + " and "
					+ second.toString() + " are both in the "
					+ first.getGroup().toString() + " group.");
		if (first.equals(second))
			throw new ExceptionCoding("duplicate incompatibilities of "
					+ first.toString());
		addSet(first, second);
	}

	private static HashMap<OPTIONS, HashSet<OPTIONS>> mustHaves;

	static {
		mustHaves = new HashMap<OPTIONS, HashSet<OPTIONS>>();
	}

	private static void addSet(OPTIONS first, OPTIONS second) {
		if (mustHaves.containsKey(first)) {
			HashSet<OPTIONS> tmp = mustHaves.get(first);
			tmp.add(second);
			mustHaves.put(first, tmp);
		} else {
			HashSet<OPTIONS> tmp = new HashSet<OPTIONS>();
			tmp.add(second);
			mustHaves.put(first, tmp);
		}
	}

	public static HashSet<OPTIONS> get(OPTIONS option) {
		if (mustHaves.containsKey(option))
			return mustHaves.get(option);
		else
			return new HashSet<OPTIONS>();
	}
}
