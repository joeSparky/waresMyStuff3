package com.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
	public static <T extends Comparable<? super T>> List<T> asSortedList(
			Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		return list;
	}

	private static int nextInt = 0;

	public static int getNextInt() {
		nextInt++;
		if (nextInt < 0)
			nextInt = 0;
		return nextInt;
	}

	// nothing magic about g
	public synchronized static String getNextString() {
		return "g" + getNextInt();
	}
}
