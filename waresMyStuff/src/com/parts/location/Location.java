package com.parts.location;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.ResultSet;
//import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.MyConnection;
import com.db.MyStatement;
import com.db.SessionVars;
import com.db.Strings;
import com.forms.EndOfInputException;
import com.forms.FormsArray;
import com.forms.FormsMatrixDynamic;
import com.parts.inOut.LocationBarcode;
import com.parts.inOut.Part;
import com.parts.security.PartLink;
import com.parts.warehouse.Warehouse;
import com.security.Anchor;
import com.security.ExceptionCoding;
import com.security.MyLinkObject;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

//import com.security.OrgChart;

/**
 * Assign barcodes representing a position to a textual description. Allow the
 * user to select a location in the warehouse from a list. Allow locations to
 * point to other locations in a hierarchy.
 * 
 * @author joe
 * 
 */
public class Location extends MyObject {

	public static final String NAME = "Location";
	public static final String ANAME = "A " + NAME;
	public static final int NAMELENGTH = 50;
	public static final int NAMELENGTHMINIMUM = 3;
	public static final int MAXINVENTORY = 25;
//	private static final String INVENTORYDATE = "inventoryDate";
	boolean justInventoried = false;
	Date inventoryDate;
	SessionVars sVars = null;

	public Location(SessionVars sVars) throws Exception {
		super(sVars);
		this.sVars = sVars;
		clear();
		new Table().tableCreated(this, sVars);
	}

//	@Override
//	public void sanity() throws Exception {
//		// see if we have a parent warehouse setup
//		// getParent(new Warehouse(sVars));
//		if (locationBarcode.isSet())
//			locationBarcode.barcodeSanity();
//	}

	/**
	 * find the location specified by the barcode
	 * 
	 * @param barcode
	 * @throws Exception
	 */
	public void find(LocationBarcode barcode, int warehouse) throws Exception {
		extract(barcode, warehouse);
	}

	/**
	 * find barcode in the database, extract the database info
	 * 
	 * @param barcode
	 * @throws Exception
	 */
	public void extract(LocationBarcode barcode, int warehouse) throws Exception {
		String uniqueName = "SELECT * FROM " + getMyFileName() + " WHERE barcode = '";
		uniqueName += barcode.rawBarcode + "' AND " + getMyFileName() + ".warehouse='" + warehouse + "'";
		Connection conn = null;
		MyStatement st = null;
		ResultSet rs = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
			rs = st.executeQuery(uniqueName);
			boolean atLeastOne = false;// get the count
			atLeastOne = rs.next();
			if (!atLeastOne) {
				throw new Exception("Location barcode " + barcode.rawBarcode + " not found");
			}
			extractInfo(rs);
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (conn != null)
				conn.close();
		}
	}

//	private void findByBarcode(LocationBarcode barcode, int warehouseId) throws Exception {
//
//		MyStatement st = new MyStatement();
//		String uniqueName = "SELECT * FROM " + getMyFileName() + " WHERE barcode = '";
//		uniqueName += barcode.rawBarcode + "' AND " + getMyFileName() + ".warehouse='" + warehouseId + "'";
//		ResultSet rs = null;
//		try {
//			rs = st.executeQuery(uniqueName);
//			boolean atLeastOne = false;// get the count
//			atLeastOne = rs.next();
//			if (!atLeastOne) {
//				throw new Exception(barcode.rawBarcode + " not found");
//			}
//			extractInfo(rs);
//		} catch (SQLException e) {
//			Internals.dumpSQLExit(e);
//		} finally {
//			st.close();
//		}
//	}

//	private void findByDescription(String descr, int warehouseId) throws Exception {
//
//		MyStatement st = new MyStatement();
//		String uniqueName = "SELECT * FROM " + getMyFileName() + " WHERE name = '";
//		uniqueName += descr + "' AND " + getMyFileName() + ".warehouse='" + warehouseId + "'";
//		ResultSet rs = st.executeQuery(uniqueName);
//
//		if (rs.next())
//			extractInfo(rs);
//		else {
//			st.close();
//			throw new Exception(descr + " not found");
//		}
//		// if there are more than 1 active locations with the same description
//		if (rs.next())
//			Internals.dumpStringExit("duplicate locations " + descr);
//		st.close();
//	}

	@Override
	public Location extractInfo(ResultSet rs) throws Exception {
		inventoryDate = new Date(0);
		inventoryDate = rs.getDate(MyObject.INVENTORYFIELDNAME);
		super.extractInfo(rs);
		return this;
	}

	public void clear() {
		justInventoried = false;
		inventoryDate = null;
		super.clear();
	}

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
		ret.add("`" + MyObject.INVENTORYFIELDNAME + "` DATE NOT NULL");
		return ret;
	}

	@Override
	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
		inventoryDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String stringDate = formatter.format(inventoryDate);
		ret.add(new DoubleString(MyObject.INVENTORYFIELDNAME, stringDate));
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

	@Override
	public String getAName() {
		return ANAME;
	}

	@Override
	public String getLogicalName() {
		return "Location";
	}

	@Override
	public int hashCodeReminder() {
		return 0;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public Location getNew() throws Exception {
		return new Location(sVars);
	}

//	@Override
//	public SmartForm getSelector(SmartForm callBack, String keyWordsLabel, String overallLabel, SearchTargets objs,
//			int index) throws Exception {
//		// if there's a member in the stack, use the selector that assumes there
//		// will be a member - link that's an item - location triad. Otherwise,
//		// use the regular selectForm
//		if (objs.findIndexOfObject(new Part(sVars)) >= 0) {
//			return new LocationSelect(callBack, keyWordsLabel, overallLabel, objs, index);
//		} else
//			return super.getSelector(callBack, keyWordsLabel, overallLabel, objs, index);
//	}

	public static FormsArray whyCantMove(Location source, Location destination) {
		FormsArray ret = new FormsArray();
		ret.rawText("Go ahead. Make my day");
		return ret;
	}

	static String myName = null;

	public static String getSimpleClassNameStatic() {
		if (myName == null)
			myName = MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
		return myName;
	}

	/**
	 * move an item from the source location to this location
	 * 
	 * @param item
	 * @param sourceLocation
	 * @param moveQuantity
	 * @throws Exception
	 */
	public void moveItem(Part item, Location sourceLocation, int moveQuantity) throws Exception {
		// get the source item

		if (!item.isLoaded())
			throw new Exception(item.getAName() + " is not selected.");
		if (!sourceLocation.isLoaded())
			throw new Exception("source location is not loaded");
		if (!isLoaded())
			throw new Exception("destination location is not loaded");
		PartLink partLink = new PartLink(item, sourceLocation, sVars).find();
		if (partLink.getLinkQuantity() < moveQuantity)
			throw new Exception("moveQuantity must be less than or equal to " + partLink.getLinkQuantity());
		// reduce the quantities of the old location
		partLink.updateAddQuantity(-moveQuantity, false);

		// update the quantities at the new location
		boolean found = false;
		PartLink destination = null;
		try {
			destination = new PartLink(item, this, sVars).find();
			found = true;
		} catch (Exception e) {
		}
		if (found)
			destination.updateAddQuantity(moveQuantity, false);
		else
			destination = new PartLink(item, this, sVars).updateAddQuantity(moveQuantity, false);
	}

	/**
	 * move the contents of the source location to this location
	 * 
	 * @param sourceLocation
	 * @throws Exception
	 */
	public void moveContents(Location sourceLocation) throws Exception {
		if (!isLoaded())
			throw new Exception("destination not loaded.<br>" + new Exception().getStackTrace()[0]);
		if (!sourceLocation.isLoaded())
			throw new Exception("sourceLocation not loaded.<br>" + new Exception().getStackTrace()[0]);
		// for each part at the source location
		for (MyObject movePart : new MyLinkObject(new Part(sVars), sourceLocation, sVars).listParentsOfChild()) {
			// get the total quantity of parts at the old location
			int quantityToMove = new PartLink(movePart, sourceLocation, sVars).find().getLinkQuantity();
			moveItem((Part) movePart, sourceLocation, quantityToMove);
		}
	}

	@Override
	public Location add(Anchor anchor) throws Exception {
		if (!(anchor.getInstanceOfAnchor() instanceof Warehouse))
			throw new Exception("anchor is not a warehouse. " + anchor.getInstanceOfAnchor().getCanonicalName() + " "
					+ new Exception().getStackTrace()[0]);
		super.add(anchor);
		return this;
	}

	@Override
	public boolean isRecursive() {
		return true;
	}

	@Override
	public boolean equalsReminder(Object obj) {
		return false;
	}

	@Override
	public boolean equals(Object exhibit) {
		if (exhibit instanceof Location)
			return id == ((Location) exhibit).id;
		else
//			Internals.dumpStringExit("invalid class " + exhibit.getClass().getCanonicalName());
			return false;
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Location;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public Location add(MyObject obj) throws Exception {
		if (!(obj instanceof Warehouse))
			throw new Exception(
					"non-warehouse anchor " + obj.getCanonicalName() + " " + new Exception().getStackTrace()[0]);
		return (Location) super.add(obj);
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Warehouse(sVars));
		objs.add(new Location(sVars));
		objs.add(new Part(sVars));
		return objs;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Location(sVars));
		return objs;
	}

//	@Override
//	public AttachToForm getAttachToForms(FormsMatrixDynamic fmd) throws Exception {
//		if (fmd.getObject() instanceof Location) {
//			// location to location transactions
//			return new AttachToFormLocationLocation(fmd);
//		} else
//			return super.getAttachToForms(fmd);
//	}

	public void setJustInventoried(boolean inventoried) {
		justInventoried = inventoried;
	}

//	public static String getColumnName() {
//		return INVENTORYDATE;
//	}

	public Date getInventoryDate() throws Exception {
		if (inventoryDate == null)
			throw new Exception("null inventoryDate.");
		return inventoryDate;
	}

	@Override
	public boolean hasInventoryField() {
		return true;
	}

	@Override
	public FormsArray markAsInventoried() throws Exception {
		FormsArray ret = new FormsArray();
		setJustInventoried(true);
		update();
		ret.errorToUser(getInstanceName() + " is marked as being in the correct location.");
		return ret;
	}

	@Override
	public FormsArray doSanityUpdateAddTryAgain(FormsMatrixDynamic fm) throws Exception {
		FormsArray ret = new FormsArray();
		sanity();
		if (isLoaded()) {
			// update the object with the name change
			update();
			// inform the user about the update
			ret.rawText(getLogicalName() + " '" + getInstanceName() + "' updated.<br>");
			throw new EndOfInputException(ret);
		} else {
			// find the warehouse in our SearchTargets
			for (int i = 0; i < fm.get(fm.row).size(); i++) {
				if (fm.get(fm.row).get(i).obj instanceof Warehouse) {
					if (fm.get(fm.row).get(i).obj.isLoaded()) {
						add(fm.get(fm.row).get(i).obj);
						ret.rawText(getLogicalName() + " '" + getInstanceName() + "' added.<br>");
						return ret;
					} else {
						ret.rawText("Please select " + Warehouse.ANAME);
						throw new EndOfInputException(ret);
					}
					
				}
			}
			throw new ExceptionCoding("Warehouse not found");
			
		}
	}
}
