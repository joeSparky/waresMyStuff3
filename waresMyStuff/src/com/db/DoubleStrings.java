package com.db;

import java.util.ArrayList;

public class DoubleStrings extends ArrayList<DoubleString> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5764099737833728581L;
	public boolean add(String first, String second){
		return add(new DoubleString(first, second));
	}
}
