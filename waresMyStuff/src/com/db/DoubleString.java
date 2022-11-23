package com.db;
/**
 * tie a database column to a value
 * @author Joe
 *
 */
public class DoubleString {
	public String field;
	public String value;
	/**
	 * database column name, value
	 * @param field
	 * @param value
	 */
	public DoubleString(String field, String value){
		this.field = field;
		this.value = value;
	}
}
