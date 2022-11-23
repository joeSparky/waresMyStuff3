package com.forms;

/**
 * this information is created by the form and return to the caller
 * 
 * @author Joe
 *
 */
public class CalledInfo {
	public CalledInfo(SmartForm whoFinished, boolean selectedLastTime, FormsArray.STATUS status, Object object) {
		this.whoFinished = whoFinished;
		this.selectedLastTime = selectedLastTime;
		this.status = status;
		this.object = object;
	}

	/**
	 * the form that just finished
	 */
	public SmartForm whoFinished;
	/**
	 * true when the form was selected (submitted). This form should be
	 * highlighted on the next display.
	 */
	boolean selectedLastTime;
	/**
	 * the status of the form
	 */
	public FormsArray.STATUS status;
	
	/**
	 * the object being edited
	 */
	public Object object;
}
