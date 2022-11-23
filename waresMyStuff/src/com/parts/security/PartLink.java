package com.parts.security;

import java.sql.ResultSet;
import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.SessionVars;
import com.db.Strings;
import com.security.MyObject;

/**
 * This class adds inventory date and quantity to the link between parts and
 * locations. This allows more than one part to be stored at a single location.
 * This also allows parts to be parents of locations, as a child (location) can
 * only have one parent (part), with the quantity field providing the number of
 * parts.
 * 
 * @author Joe
 *
 */
public class PartLink extends PartsInventory {

	public PartLink(MyObject part, MyObject location, SessionVars sVars) throws Exception {
		super(part, location, sVars);
		clear();
	}

	private static int MAGICNUMBER = -394;

	protected void clear() {
		super.clear();
	}

//	public Timestamp inventoried = null;
	private int quantity = MAGICNUMBER;

	public void setLinkQuantity(int quant) {
		quantity = quant;
	}
//	public PartLink add(int quantity, boolean inventoried) throws Exception {
//		if (quantity == 0)
//			return this;
//		super.setInventoried(inventoried);
//		this.quantity = quantity;
//		super.add();
//		return this;
//	}

	@Override
	public PartLink add() throws Exception {
//		if (inventoried == null)
//			throw new Exception("inventoried is null.<br>" + new Exception().getStackTrace()[0]);
		if (quantity == MAGICNUMBER)
			throw new Exception("quantity not set.<br>" + new Exception().getStackTrace()[0]);
		// when a new item is added to the warehouse, mark it as inventoried
		super.setInventoried(true);
		super.add();
		return this;
	}

//	public PartLink update(int quantity, Timestamp inventoryDate) throws Exception {
//		super.setDate(inventoryDate);
//		this.quantity = quantity;
//		update();
//		return this;
//	}

//	public PartLink addQuantityResetDate(int quantity) throws Exception {
//		this.inventoried = Timestamp.valueOf("1970-01-01 00:00:00");
//		this.quantity += quantity;
//		if (isLoaded())
//			update();
//		else
//			add();
//		return this;
//	}

//	public PartLink addQuantityWithoutDateChange(int quantity) throws Exception {
//		if (!isLoaded())
//			throw new Exception("partLink not loaded.<br>" + new Exception().getStackTrace()[0]);
//		this.quantity += quantity;
//		if (this.quantity == 0) {
//			deleteTest();
//			deleteUnconditionally();
//		} else
//			update();
//		return this;
//	}

//	public PartLink update(int quantity) throws Exception {
////		this.inventoried = Timestamp.valueOf("1970-01-01 00:00:00");
//		this.quantity = quantity;
//		update();
//		return this;
//	}

	public PartLink updateAddQuantity(int quantity, boolean inventoried) throws Exception {
//		this.inventoried = Timestamp.valueOf("1970-01-01 00:00:00");
		super.setInventoried(inventoried);
		this.quantity += quantity;
		if (this.quantity == 0) {
			deleteTest();
			deleteUnconditionally();
		} else
			update();
		return this;
	}
	
	public PartLink updateSetQuantity(int quantity, boolean inventoried) throws Exception {
//		this.inventoried = Timestamp.valueOf("1970-01-01 00:00:00");
		super.setInventoried(inventoried);
		this.quantity = quantity;
		if (this.quantity == 0) {
			deleteTest();
			deleteUnconditionally();
		} else
			update();
		return this;
	}

	@Override
	protected PartLink extractInfo(ResultSet row) throws Exception {
//		try {
//		inventoried = row.getTimestamp("inventoried");
		quantity = row.getInt("quantity");
//		} catch (SQLException e) {
//			Internals.dumpExceptionExit(e);
//		}
		super.extractInfo(row);
		return this;
	}

	@Override
	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
//		ret.add(new DoubleString("inventoried", inventoried.toString()));
		ret.add(new DoubleString("quantity", "" + quantity));
		ret.addAll(super.extendAdd());
		return ret;
	}

	@Override
	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	@Override
	public Strings extendNewTable() {
		Strings strs = new Strings();
//		strs.add("`inventoried` TIMESTAMP DEFAULT '1970-01-01 00:00:00'");
		strs.add("`quantity` int(11)");
		strs.addAll(super.extendNewTable());
		return strs;
	}

//	public PartLink update() throws Exception {
//		if (!isLoaded())
//			throw new Exception("must be loaded before update.<br>" + new Exception().getStackTrace()[0]);
//		if (!parent.isLoaded())
//			throw new Exception("parent not loaded.<br>" + new Exception().getStackTrace()[0]);
//		if (!child.isLoaded())
//			throw new Exception("child not loaded.<br>" + new Exception().getStackTrace()[0]);
//
//		String setString = "";
//		boolean fieldAdded = false;
//		for (DoubleString db : extendUpdate()) {
//			if (fieldAdded) {
//				setString += ", " + db.field + "= \'" + db.value + "\'";
//			} else {
//				setString += db.field + "= \'" + db.value + "\'";
//				fieldAdded = true;
//			}
//		}
//
//		String update = "UPDATE " + getMyFileName();
//		update += " SET ";
//		update += setString;
//		update += " WHERE parentId='" + parent.id + "' AND childId='" + child.id + "'";
//		Connection co = null;
//		MyStatement st = null;
//		try {
//			co = MyConnection.getConnection();
//			st = new MyStatement(co);
//			if (st.executeMyUpdate(update) != 1)
//				throw new Exception(getMyFileName() + " update failed");
//		} finally {
//			if (st != null)
//				st.close();
//			if (co != null)
//				co.close();
//		}
//		return this;
//	}

	public PartLink find() throws Exception {
		super.find();
		return this;
	}

	public int getLinkQuantity() {
		return quantity;
	}
}
