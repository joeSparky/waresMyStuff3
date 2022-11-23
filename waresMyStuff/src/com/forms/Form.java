package com.forms;

public class Form {
	public static final String ONCLICKBUTTON = "onClickButton";
	public enum ButtonType {
		CANCEL, SUBMITBUTTON, EXPAND, DELETE, UPDATE,
		/**
		 * change the default member
		 */
		CHANGEDEFAULTGROUP,
		/**
		 * no barcode on the item, select from a list of families
		 */
		NOBARCODE,
		/**
		 * no barcode on the item, select from a list of categories
		 */
		NOBARCODEUSECAT,
		/**
		 * no barcode on family, select from a list of families
		 */
		NOBARCODEFINDPRODUCT,
		/**
		 * cancel the selected family
		 */
		CANCELPRODUCTSELECTION,
		
		/**
		 * go up one level
		 */
		GOUPONELEVEL,
		/**
		 * search for a member
		 */
		SEARCHGROUPS,
		/**
		 * start over
		 */
		RESET, PREVIEW
	}
}
