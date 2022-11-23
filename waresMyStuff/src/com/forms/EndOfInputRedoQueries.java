package com.forms;

public class EndOfInputRedoQueries extends Exception {
	private static final long serialVersionUID = 1L;

	public EndOfInputRedoQueries(FormsMatrixDynamic fm) {
		super();
		this.fm = fm;
	}
	public FormsMatrixDynamic fm = null;
}
