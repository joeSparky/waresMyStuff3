package com.parts.warehouse;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.parts.exhibit.Kit;
import com.parts.inOut.Attributes;
import com.security.Table;
import com.security.Company;
import com.security.MyObject;
import com.security.MyObjects;

public class Warehouse extends MyObject {
	// public static final String TABLENAME = "warehouse";
	public static final String NAME = "Warehouse";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";
//	public static final int NAMELENGTH = 30;
//	public static final int NAMEMINIMUM = 5;
//	public static final int LOCATIONLENGTH = 30;
//	public static final int LOCATIONMINIMUM = 5;
//	public static final String DEFAULTUSERWAREHOUSE = "dWarehouse";
	/**
	 * unique barcode sequence for this warehouse
	 */
//	public static final String UNIQUECOLUMNNAME = "uniq";
	/**
	 * unique location barcode sequence for this warehouse
	 */
//	public static final String UNIQUELOCATIONCOLUMNNAME = "luniq";
	/**
	 * last unique item barcode
	 */
//	public int lastUniqueBarcode;
	/**
	 * last unique location barcode
	 */
//	public int lastUniqueLocationBarcode;
	// public int commonAreaLocationId;
//	public Timestamp created;
	/**
	 * address (contact information) of warehouse
	 */
//	public Address address = null;

	// public int getCommonAreaLocationId() {
	// if (commonAreaLocationId < 1) {
	// setCommonArea();
	// }
	// return commonAreaLocationId;
	// }

	public Warehouse(SessionVars sVars) throws Exception {
		super(sVars);
		clear();
		new Table().tableCreated(this, sVars);
	}

	public static boolean needsNewTable = false;

//	public void clear() {
//		lastUniqueBarcode = 0;
//		lastUniqueLocationBarcode = 0;
//		// commonAreaLocationId = 0;
//		created = null;
//		if (address == null) {
//			try {
//				address = new Address();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		super.clear();
//	}

	/**
	 * Add a new warehouse
	 */
//	@Override
//	public void add(Anchor anchor) throws Exception {
//		// force a self-anchor
//		super.add(new Anchor(sVars));
//	}

//	@Override
//	public DoubleStrings extendAdd() {
//		DoubleStrings ret = new DoubleStrings();
//		// ret.add("commonAreaLocationId", "" + commonAreaLocationId);
//		ret.add(UNIQUECOLUMNNAME, "" + lastUniqueBarcode);
//		ret.add(UNIQUELOCATIONCOLUMNNAME, "" + lastUniqueLocationBarcode);
//		ret.add("addressId", "" + address.id);
//		return ret;
//	}

//	@Override
//	public DoubleStrings extendUpdate() {
//		return extendAdd();
//	}

//	@Override
//	protected Warehouse extractInfo(ResultSet rs) throws Exception {
//		extractInstanceName(rs);
//		created = rs.getTimestamp("created");
//		int addressId = rs.getInt("addressId");
//		lastUniqueBarcode = rs.getInt(UNIQUECOLUMNNAME);
//		lastUniqueLocationBarcode = rs.getInt(UNIQUELOCATIONCOLUMNNAME);
//
//		if (addressId > 0)
//			try {
//				address.findById(addressId);
//			} catch (Exception e) {
//				Internals.dumpExceptionExit(e);
//			}
//		else
//			address.clear();
//
//		super.extractInfo(rs);
//		return this;
//	}

//	@Override
//	public Warehouse find(int id) throws Exception {
//		return extractInfo(findResultSet(id));
//	}

	// public boolean isCommonArea(Location location) {
	// return location.id == commonAreaLocationId;
	// }

	/**
	 * setup a common area for this warehouse
	 * 
	 * @param warehouseId
	 */
	// private void setCommonArea() {
	// // Warehouse w = new Warehouse(sVars);
	// // try {
	// // w.find(warehouseId);
	// // } catch (Exception e) {
	// // Internals.dumpExceptionExit(e);
	// // }
	// // get a common area location
	// Location loc = new Location(sVars);
	// // loc.locationBarcode.setLocationBarcode("");
	// // loc.warehouse = warehouseId;
	//
	// try {
	// loc.setInstanceName(getInstanceName());
	// loc.add(getAnchor());
	// } catch (Exception e) {
	// Internals.dumpExceptionExit(e);
	// }
	// commonAreaLocationId = loc.id;
	// try {
	// sanity();
	// } catch (Exception e) {
	// Internals.dumpExceptionExit(e);
	// }
	// }

//	public Strings extendNewTable() {
//		Strings ret = new Strings();
//		ret.add("`" + UNIQUECOLUMNNAME + "` varchar(" + NAMELENGTH
//				+ ") DEFAULT NULL");
//		ret.add("`" + UNIQUELOCATIONCOLUMNNAME + "` varchar(" + NAMELENGTH
//				+ ") DEFAULT NULL");
//		// ret.add("`commonAreaLocationId` int(11) DEFAULT 0 ");
//		ret.add("`addressId` int(11) DEFAULT 0 ");
//		ret.add("`created` timestamp DEFAULT CURRENT_TIMESTAMP");
//		return ret;
//	}

//	public static String getNextItemBarcode(int warehouse) throws Exception {
//		Warehouse myWarehouse = new Warehouse(sVars);
//		myWarehouse.find(warehouse);
//		myWarehouse.lastUniqueBarcode++;
//		try {
//			myWarehouse.sanity();
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
//		return "" + myWarehouse.lastUniqueBarcode;
//	}
//
//	public static String getNextLocationBarcode(int warehouse) throws Exception {
//		Warehouse myWarehouse = new Warehouse(sVars);
//		myWarehouse.find(warehouse);
//		myWarehouse.lastUniqueLocationBarcode++;
//		try {
//			myWarehouse.sanity();
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
//		return "L" + myWarehouse.lastUniqueLocationBarcode;
//	}

	@Override
	public boolean equals(Object exhibit) {
		if (exhibit instanceof Warehouse) {
			return id == ((Warehouse) exhibit).id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String getAName() {
		return ANAME;
	}

	@Override
	public int hashCodeReminder() {
		return 0;
	}

	@Override
	public boolean equalsReminder(Object obj) {
		return false;
	}

	@Override
	public MyObject getNew() throws Exception {
		return new Warehouse(sVars);
	}

	@Override
	public String getLogicalName() {
		return NAME;
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Warehouse;
	}

	static String myName = null;

	public static String getSimpleClassNameStatic() {
		if (myName == null)
			myName = MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
		return myName;
	}

//	// when a warehouse is created or selected, make it an anchor
//	@Override
//	public SmartForm getSelector(SmartForm callBack, String keyWordsLabel,
//			String overallLabel, SearchTargets objs, int index) {
//		return new SelectWarehouse(callBack, keyWordsLabel, overallLabel, objs,
//				index);
//	}

	@Override
	public String getMyFileName() throws Exception {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Company(sVars));
		return objs;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Attributes(sVars));
//		objs.add(new Part(sVars));
		objs.add(new Kit(sVars));
		return objs;
	}	
//	@Override
//	public SmartForm getSelector(SmartForm callBack, String keyWordsLabel, String overallLabel, FormsMatrixDynamic fmd) throws Exception {
//		return getSelector(callBack, keyWordsLabel, overallLabel, fmd);
//	}
}
