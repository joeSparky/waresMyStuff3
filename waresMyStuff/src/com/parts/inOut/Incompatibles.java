package com.parts.inOut;

import java.util.HashMap;
import java.util.HashSet;

import com.parts.inOut.OptionEnums.OPTIONS;
import com.security.ExceptionCoding;

public class Incompatibles {
	public static void set(OPTIONS first, OPTIONS second) throws ExceptionCoding {
		if (first.getGroup().equals(second.getGroup()))
			throw new ExceptionCoding(first.toString() + " and "
					+ second.toString() + " are both in the "
					+ first.getGroup().toString() + " group.");
		if (first.equals(second))
			throw new ExceptionCoding("duplicate incompatibilities of "
					+ first.toString());
		addSet(first, second);
		addSet(second, first);
	}

	static HashMap<OPTIONS, HashSet<OPTIONS>> incomps;

	static {
		incomps = new HashMap<OPTIONS, HashSet<OPTIONS>>();
	}

	private static void addSet(OPTIONS first, OPTIONS second) {
		if (incomps.containsKey(first)) {
			HashSet<OPTIONS> tmp = incomps.get(first);
			tmp.add(second);
			incomps.put(first, tmp);
		} else {
			HashSet<OPTIONS> tmp = new HashSet<OPTIONS>();
			tmp.add(second);
			incomps.put(first, tmp);
		}
	}

	public static HashSet<OPTIONS> get(OPTIONS option) {
		if (incomps.containsKey(option))
			return incomps.get(option);
		else
			return new HashSet<OPTIONS>();
	}
}
