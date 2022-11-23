package com.parts.inOut;

import com.parts.inOut.OptionEnums.OPTIONS;

public class OptionSetPairs {
	public OptionSet first, second;
	/**
	 * option changes
	 */
	public OPTIONS firstChange, secondChange;

	public OptionSetPairs() {
		first = new OptionSet();
		second = new OptionSet();
		firstChange = secondChange = OPTIONS.USERUNMARKEDSHAREDDIF;
	}
}
