package com.security;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.Strings;
import com.forms.FormsArray;

/**
 * add a name field to an object. The user can put a string of name into this
 * field and have it associated in the datbase with the object.
 * 
 * @author Joe
 *
 */
public class Name {
	private String name = "";
	public static int NAMELENGTH = 60;
	public static int MINIMUMLENGTH = 3;

	public void extractInfo(ResultSet row) throws SQLException {
		name = row.getString("name");
	}

	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add("`name` CHAR(" + NAMELENGTH + ")");
		ret.add("UNIQUE KEY `name` (`name`)");
//		ret.add("FULLTEXT(name)");
		return ret;
	}

	public DoubleStrings extendUpdate() {
		DoubleStrings ret = new DoubleStrings();
		ret.add(new DoubleString("name", name));
		return ret;
	}

	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		ret.add(new DoubleString("name", name));
		return ret;
	}

	/**
	 * throw an exception for null, length < MINIMUMLENGTH, or > NAMELENGTH. does
	 * NOT check for duplicates
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void setname(String name) throws Exception {
		sanity(name);
		this.name = name;
	}

	public void clear() {
		name = "";
	}

	/**
	 * throw an exception for null, length < MINIMUMLENGTH, or > NAMELENGTH
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void sanity(String name) throws Exception {
		if (name == null)
			throw new Exception("null name");
		if (name.length() > NAMELENGTH)
			throw new Exception("name is greater than " + NAMELENGTH + " characters");
		if (name.length() < MINIMUMLENGTH)
			throw new Exception("The name '" + name + "' is less than " + MINIMUMLENGTH + " characters");
	}

	public void sanity() throws Exception {
		sanity(name);
	}

	public String getname() {
		return name;
	}

	public String getTitle() {
		return "name";
	}

	public FormsArray getNameForm(String boxName, MyObject caller,
//			SmartForm.INSTATE inState, 
			boolean masterIsLoaded) throws Exception {
		FormsArray ret = new FormsArray();
		ret.startRow();
		ret.tableDepth++;
		if (masterIsLoaded) {
			ret.textBox(boxName, NAMELENGTH, caller.getLogicalName() + " name", name,
					false, false);
		} else {
			// blank name if not loaded
			ret.textBox(boxName, NAMELENGTH, caller.getLogicalName() + " name", "",
					false, false);
		}
		ret.tableDepth--;
		ret.endRow();
		return ret;
	}
	
	public boolean nameChanged(SessionVars sVars, String boxName) throws Exception {
		// if we have the parameter and something changed
		return (sVars.hasParameterKey(boxName) && !sVars.getParameterValue(boxName).equals(name));
	}

	/**
	 * valid new name
	 * 
	 * @param sVars
	 * @param boxName
	 * @return
	 * @throws Exception
	 */
	public boolean extractname(SessionVars sVars, String boxName) throws Exception {
		// if we have the parameter and something changed
		if (nameChanged(sVars, boxName)) {
			// throw an exception if it's not valid
			sanity(sVars.getParameterValue(boxName));
			// update the field
			name = sVars.getParameterValue(boxName).toLowerCase();
			// tell the world
			return true;
		}
		return false;
	}

//	public int compareTo(Name nameInstance) {
//		// TODO Auto-generated method stub
//		throw new ExceptionCoding("not yet");
//		return 0;
//	}
}
