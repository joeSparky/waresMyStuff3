package com.parts.security;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.Date;
import java.util.Date;

//import java.sql.Date;

import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.Strings;
import com.security.MyLinkObject;
import com.security.MyObject;

public class PartsInventory extends MyLinkObject {

	public PartsInventory(MyObject parent, MyObject child, SessionVars sVars) throws Exception {
		super(parent, child, sVars);
	}

	private static final String INVENTORYDATE = "inventoryDate";
	boolean justInventoried = false;
	private Date inventoryDate = null;

	public static String getColumnName() {
		return INVENTORYDATE;
	}

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add("`" + INVENTORYDATE + "` DATE NOT NULL");
		ret.addAll(super.extendNewTable());
		return ret;
	}

	@Override
	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		if (justInventoried) {
			inventoryDate = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			String stringDate = formatter.format(inventoryDate);
			ret.add(new DoubleString(INVENTORYDATE, stringDate));
		}
		return ret;
	}

	@Override
	public DoubleStrings extendUpdate() {
		if (justInventoried)
			// insert the current date in the inventory field
			return extendAdd();
		else
			// don't update the inventory field
			return new DoubleStrings();
	}

	protected PartsInventory extractInfo(ResultSet rs) throws Exception {
		inventoryDate = new Date(0);
		inventoryDate = rs.getDate(INVENTORYDATE);
		super.extractInfo(rs);
		return this;
	}

	public void setInventoried(boolean inventoried) {
		justInventoried = inventoried;
	}

	public Date getInventoryDate() throws Exception {
		if (inventoryDate == null)
			throw new Exception("the link between " + super.parent.getInstanceName() + " and "
					+ super.child.getInstanceName() + " has not been inventoried.");
		return inventoryDate;
	}

	/**
	 * for testing only
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		inventoryDate = date;
	}

}
