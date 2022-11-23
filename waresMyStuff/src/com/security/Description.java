package com.security;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.Strings;

/**
 * add a description field to an object. The user can put a string of
 * description into this field and have it associated in the datbase with the
 * object.
 * 
 * @author Joe
 *
 */
public class Description {
	private String description = "";
	public static int DESCRIPTIONLENGTH = 40;

	public void extractInfo(ResultSet row) throws SQLException {
		description = row.getString("description");
	}

	public Strings extendNewTable() {
		return new Strings("`description` CHAR(" + DESCRIPTIONLENGTH + ")");
	}

	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		ret.add(new DoubleString("description",description));
		return ret;
	}

	public DoubleStrings extendUpdate(){
		return extendAdd();
	}

	public void setDescription(String description) throws Exception {
		sanity(description);
		this.description = description;
	}

	private void sanity(String description) throws Exception {
		if (description == null)
			throw new Exception("null description");
		if (description.length() > DESCRIPTIONLENGTH)
			throw new Exception("description is greater than "
					+ DESCRIPTIONLENGTH + " characters");
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return "Description";
	}

//	public FormsArray getDescriptionForm(String boxName, MyObject caller,
//			SmartForm.INSTATE inState) throws Exception {
//		FormsArray ret = new FormsArray();
//		ret.startRow();
//		ret.tableDepth++;
//		ret.textBox(boxName, DESCRIPTIONLENGTH, caller.getLogicalName()
//				+ " Description", description,
//				inState == INSTATE.STANDARDFORMWITHFOCUS,
//				inState == INSTATE.DISPLAYONLY);
//		ret.tableDepth--;
//		ret.endRow();
//		return ret;
//	}

	public boolean extractDescription(SessionVars sVars, String boxName)
			throws Exception {
		// if we have the parameter and something changed
		if (sVars.hasParameterKey(boxName)
				&& !sVars.getParameterValue(boxName).equals(description)) {
			// throw an exception if it's not valid
			sanity(sVars.getParameterValue(boxName));
			// update the field
			description = sVars.getParameterValue(boxName);
			// tell the world
			return true;
		}
		return false;
	}
}
