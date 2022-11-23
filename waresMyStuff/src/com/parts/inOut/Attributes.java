package com.parts.inOut;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.parts.warehouse.Warehouse;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

/**
 * manage the rule
 * 
 * @author joe
 * 
 */
public class Attributes extends MyObject {
	public static final String NAME = "Attributes";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = "Attributes";
//	public FamilyOptions familyOptions = null;
//	private ActiveClass active = new ActiveClass();

	/**
	 * if a sequential marking is required
	 * 
	 * @return
	 */
//	public boolean needsSeqMark() {
//		return familyOptions.needsSeqMark();
//	}

//	public boolean needsInspection() {
//		return familyOptions.needsInspection();
//	}

//	public boolean needsShippingAddress() {
//		return familyOptions.needsShippingAddress();
//	}
//
//	public boolean needsUserLoc() {
//		return familyOptions.needsUserLoc();
//	}

	/**
	 * the name of the family
	 */
	// public String familyName;

	/**
	 * a description of the family
	 */
	// public Description descriptionInstance = new Description();
	/**
	 * the family id
	 */

	// public int id;
	/**
	 * the number of days before expiration to issue an expiration warning
	 */
//	public int daysWarning;
	/**
	 * the number of days before expiration to remove the item from the shelf
	 */
//	public int daysRemove;

	public Attributes(SessionVars sVars) throws Exception {
		super(sVars);
		clear();
		new Table().tableCreated(this, sVars);
	}

//	public int familyReceiveSound;

//	@Override
//	public void clear() {
////		daysWarning = daysRemove = 0;
//		if (familyOptions == null)
//			familyOptions = new FamilyOptions();
//		familyOptions.clear();
////		active.setActive();
////		familyReceiveSound = 0;
//		super.clear();
//	}

//	public void clone(Attributes source) {
//		try {
//			setInstanceName(source.getInstanceName());
//		} catch (Exception e) {
//			Internals.dumpExceptionExit(e);
//		}
//		// try {
//		// descriptionInstance.setDescription(source.descriptionInstance
//		// .getDescription());
//		// } catch (Exception e) {
//		// // should not have been able to set an illegal value of description
//		// Internals.dumpStringExit(e.getLocalizedMessage());
//		// }
//		id = source.id;
//		daysWarning = source.daysWarning;
////		daysRemove = source.daysRemove;
//		familyOptions.clear();
//		for (OptionEnums.OPTIONS opt : source.familyOptions.values()) {
//			this.familyOptions.put(opt);
//		}
////		active = source.active;
//		familyReceiveSound = source.familyReceiveSound;
//	}

	/**
	 * deactivate a rule
	 * 
	 * @throws Exception
	 */
//	public void deactivateFamily() throws Exception {
//		MyStatement st = new MyStatement();
//		String update = "UPDATE " + getMyFileName() + " SET active=0 WHERE id='" + this.id + "'";
//
//		st.executeUpdate(update);
//		st.close();
//		clear();
////		active.setNotActive();
//	}

//	public void findFamilyByName(String familyName, int warehouse) throws SQLException, Exception {
//		MyStatement st = new MyStatement();
//		// con = MyConnection.GetConnection();
//		// st = con.createStatement();
//		String query = "SELECT * FROM " + getMyFileName() + " WHERE name='" + familyName + "' AND  warehouse='"
//				+ warehouse + "' AND active>'0'";
//		ResultSet rs = null;
//		rs = st.executeQuery(query);
//		// st.close();
//		// if the record was found
//		try {
//			if (rs.next()) {
//				this.extractInfo(rs);
//				return;
//			}
//		} catch (SQLException e) {
//			Internals.dumpExceptionExit(e);
//		} finally {
//			st.close();
//		}
//		throw new Exception("rule '" + familyName + "' was not found in warehouse '"
//				+ new Warehouse(sVars).find(warehouse).getInstanceName() + "'");
//	}

	/**
	 * find an active family using the family ID
	 * 
	 * @throws SQLException
	 * @throws Exception
	 */
	// @Override
	// public Family find(int familyId) throws Exception {
	// MyStatement st = new MyStatement();
	// // con = MyConnection.GetConnection();
	// // st = con.createStatement();
	// String query = "SELECT * FROM " + getSimpleClassName() + " WHERE id='" +
	// familyId
	// + "' AND active>'0'";
	// ResultSet rs = null;
	// rs = st.executeQuery(query);
	// // if the record was found
	// try {
	// if (rs.next()) {
	// this.extractInfo(rs);
	// return this;
	// } else {
	// throw new Exception(NAME + " not found");
	// }
	// } catch (SQLException e) {
	// Internals.dumpSQLExit(e);
	// } finally {
	// st.close();
	// }
	// return this;
	// }

//	public void checkFamilyLoaded(int loadid) throws SQLException, Exception {
//		assert loadid > 0 : "invalid rule id";
//		if (id == 0) {
//			find(loadid);
//		}
//	}

//	public boolean familyLoaded() {
//		if (id > 0)
//			return true;
//		else
//			return false;
//	}

//	@Override
//	public MyObject extractInfo(ResultSet rs) throws Exception {
////		active.extractInfo(rs);
////		this.daysWarning = rs.getInt("dayswarning");
////		this.daysRemove = rs.getInt("daysremove");
////		this.familyReceiveSound = rs.getInt("receiveSound");
//		for (OptionEnums.OPTIONGROUPS s : OptionEnums.OPTIONGROUPS.getFamilyOptionGroups()) {
//			familyOptions.put(s, familyOptions.findEnum(rs.getInt(s.getSqlLabel())));
//		}
//		super.extractInfo(rs);
//		return this;
//	}

	/**
	 * different items can share a user location
	 * 
	 * @return
	 */
//	public boolean differentItemsCanShareUserLocation() {
//		return familyOptions.differentItemsCanShareUserLocation();
//	}

//	public boolean identicalItemsCanShareUserLocation() {
//		return familyOptions.identicalItemsCanShareUserLocation();
//	}
//
//	public boolean familyOptionOn(OptionEnums.OPTIONS check) {
//		return familyOptions.familyOptionOn(check);
//	}
//
//	public Warehouse getWarehouse() throws Exception {
//		MyObject mla = this.getSingleParent(new Warehouse(sVars));
//		Warehouse w = new Warehouse(sVars);
//		return w.find(mla.id);
//	}

//	@Override
//	public void sanity() throws Exception {
//		familyOptions.optionFamilySanity();
//		super.sanity();
//		// if (parentId == 0)
//		// throw new Exception("warehouse not set");
//	}

//	@Override
//	public DoubleStrings extendAdd() {
//		DoubleStrings ret = new DoubleStrings();
////		ret.addAll(active.extendAdd());
//		// ret.addAll(descriptionInstance.extendAdd());
////		ret.add(new DoubleString("dayswarning", "" + daysWarning));
////		ret.add(new DoubleString("daysremove", "" + daysRemove));
////		ret.add(new DoubleString("receiveSound", "" + familyReceiveSound));
//		for (OptionEnums.OPTIONGROUPS s : OptionEnums.OPTIONGROUPS.getFamilyOptionGroups()) {
//			ret.add(new DoubleString(s.getSqlLabel(), "" + familyOptions.get(s).ordinal()));
//		}
//		return ret;
//	}

//	@Override
//	public Strings extendNewTable() {
//		Strings ret = new Strings();
////		ret.addAll(active.extendNewTable());
//		// ret.addAll(descriptionInstance.extendNewTable());
////		ret.add("`warehouse` INT NOT NULL DEFAULT '0'");
////		ret.add("`dayswarning` INT NOT NULL DEFAULT '0'");
////		ret.add("`receiveSound` INT NOT NULL DEFAULT '0'");
////		ret.add("`daysremove` INT NOT NULL DEFAULT '0'");
////		ret.add("`entered` TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
//
//		for (OptionEnums.OPTIONGROUPS s : OptionEnums.OPTIONGROUPS.getFamilyOptionGroups()) {
//			ret.add("`" + s.getSqlLabel() + "` INT NOT NULL DEFAULT '0'");
//		}
//		return ret;
//	}

//	public void deleteFamily() throws Exception {
//		// java.sql.Connection con = null;
//		MyStatement st = new MyStatement();
//		String insert = "DELETE FROM " + getMyFileName() + " WHERE id='" + id + "'";
//
//		int rows = st.executeUpdate(insert);
//		st.close();
//
//		if (rows != 1) {
//			Internals.dumpStringExit("id not found");
//		}
//	}

//	/**
//	 * update a family in the database
//	 * 
//	 * @throws Exception
//	 */
//	@Override
//	public DoubleStrings extendUpdate() {
//		return extendAdd();
//	}

//	public boolean sayPosition() {
//		if (familyOptionOn(OptionEnums.OPTIONS.SAYPOSITION))
//			return true;
//		else
//			return false;
//	}
//
//	public boolean sayCustom() {
//		if (familyOptionOn(OptionEnums.OPTIONS.CUSTOMSOUND))
//			return true;
//		else
//			return false;
//	}

//	private void whyNotWhoSelects(Attributes finalMember) throws Exception {
//		switch (finalMember.familyOptions.get(OPTIONGROUPS.WHOSELECTSTYPE)) {
//		// nothing to do
//		case ORDERER:
//		case PICKER:
//		case SYSTEMFIFO:
//			break;
//		case SYSTEMEXP:
//			// see if all of the barcodes have an expiration date set up
//			whyNotExpiration(finalMember);
//			break;
//		case SYSTEMLOT:
//			whyNotLot(finalMember);
//			break;
//		default:
//			Internals.dumpStringExit(
//					"unknown who selects type of " + familyOptions.get(OPTIONGROUPS.WHOSELECTSTYPE).toString());
//		}
//	}

//	private void whyNotLot(Attributes finalMember) throws Exception {
//		for (Part member : Part.listMembersByFamilyId(finalMember.id)) {
//			if (member.itemBarcodes.isEmpty())
//				throw new Exception(member.getInstanceName() + " needs a barcode. " + member.getInstanceName()
//						+ " has no barcode set up.");
//			for (ItemBarcode ibc : member.itemBarcodes) {
//				if (!ibc.hasLot())
//					throw new Exception("Barcode " + ibc.rawBarcode + " in " + member.getInstanceName()
//							+ " does not have a lot number set up.");
//			}
//		}
//	}

//	private void whyNotExpiration(Attributes finalMember) throws Exception {
//		for (Part member : Part.listMembersByFamilyId(finalMember.id)) {
//			if (member.itemBarcodes.isEmpty())
//				throw new Exception(member.getInstanceName() + " needs a barcode. " + member.getInstanceName()
//						+ " has no barcode set up.");
//			for (ItemBarcode ibc : member.itemBarcodes) {
//				if (!ibc.hasExp())
//					throw new Exception("Barcode " + ibc.rawBarcode + " in " + member.getInstanceName()
//							+ " does not have an expiration date set up.");
//			}
//		}
//	}

//	private void whyNotBarcodeType(Attributes oldMember) throws Exception {
//		switch (oldMember.familyOptions.get(OPTIONGROUPS.DUPEUNIQUENONEBARCODE)) {
//		case UNIQUE:
//			// verify all barcodes are unique
//			whyNotAllUniqueBarcodes(oldMember);
//			break;
//		case DUPE:
//			// nothing needed for allowing duplicates
//			break;
//		case NOBARCODE:
//			// all barcodes will be deleted so nothing to check
//			break;
//		default:
//			Internals.dumpStringExit(
//					"unknown barcode type of " + familyOptions.get(OPTIONGROUPS.DUPEUNIQUENONEBARCODE).toString());
//		}
//	}

//	private void whyNotAllUniqueBarcodes(Attributes oldMember) throws Exception {
//		HashSet<String> barcodes = new HashSet<String>();
//		for (Part member : Part.listMembersByFamilyId(oldMember.id)) {
//			// for (Item item : Item.listBoxesInMember(member.id))
//			{
//				TimeReceived item = null;
//				if (item.barcode.isSet()) {
//					if (!barcodes.add(item.barcode.rawBarcode)) {
//						throw new Exception(item.barcode.rawBarcode + " is a duplicate barcode.");
//					}
//				}
//			}
//		}
//	}

//	protected void whyNotMarking(Attributes oldMember) throws Exception {
//		// if either the old or new options use the common area
//		if ((oldMember.familyOptionOn(OPTIONS.COMMONMARKED) || familyOptionOn(OPTIONS.COMMONMARKED)))
//			throw new Exception("Can not change common marked area with inventory.");
//
//		switch (oldMember.familyOptions.get(OPTIONGROUPS.MARKINGSET)) {
//		case USERUNMARKEDNOTSHARED:
//			switch (familyOptions.get(OPTIONGROUPS.MARKINGSET)) {
//			case USERUNMARKEDIDENTICAL:
//				// OK if no sharing taking place
//				whyNotSharingButIdenticalOK(oldMember);
//				break;
//			case USERMARKED:
//				// going from unmarked to marked
//				// allow if no user specified locations are in use
//				whyNotNewMarkedButOldUnmarked(oldMember);
//				break;
//			case USERUNMARKEDSHAREDDIF:
//				// going from identical to shared.
//				// nothing to do
//				break;
//			default:
//				Internals.dumpStringExit(
//						"unknown family option of " + familyOptions.get(OPTIONGROUPS.MARKINGSET).toString());
//			}
//			break;
//		case USERUNMARKEDIDENTICAL:
//			switch (familyOptions.get(OPTIONGROUPS.MARKINGSET)) {
//			case USERUNMARKEDNOTSHARED:
//				// can't allow any sharing
//				// ok if no sharing taking place
//				whyNotSharingButIdenticalOK(oldMember);
//				break;
//			case USERMARKED:
//				// allow if no user specified locations are in use
//				whyNotNewMarkedButOldUnmarked(oldMember);
//				break;
//			case USERUNMARKEDSHAREDDIF:
//				// nothing to do
//				break;
//			default:
//				Internals.dumpStringExit(
//						"unknown family option of " + familyOptions.get(OPTIONGROUPS.MARKINGSET).toString());
//			}
//			break;
//		case USERMARKED:
//			switch (familyOptions.get(OPTIONGROUPS.MARKINGSET)) {
//			case USERUNMARKEDIDENTICAL:
//			case USERUNMARKEDNOTSHARED:
//			case USERUNMARKEDSHAREDDIF:
//				throw new Exception("Requires a change in the package marking.");
//			default:
//				Internals.dumpStringExit(
//						"unknown family option of " + familyOptions.get(OPTIONGROUPS.MARKINGSET).toString());
//			}
//			break;
//		case USERUNMARKEDSHAREDDIF:
//			switch (familyOptions.get(OPTIONGROUPS.MARKINGSET)) {
//			case USERUNMARKEDIDENTICAL:
//				// allow only if sharing locations with identical items
//				whyNotUserUnmarkedIdentical(oldMember);
//				break;
//			case USERUNMARKEDNOTSHARED:
//				// both are unmarked so not a marking issue.
//				// check if sharing issue.
//				// allow if all of the oldMember items are not sharing a
//				// location, even with the same oldMember
//				whyNotNoSharingIdenticalNotOK(oldMember);
//				break;
//			case USERMARKED:
//				// allow if no user specified locations are in use
//				whyNotNewMarkedButOldUnmarked(oldMember);
//				break;
//			default:
//				Internals.dumpStringExit(
//						"unknown family option of " + familyOptions.get(OPTIONGROUPS.MARKINGSET).toString());
//			}
//			break;
//		case COMMONMARKED:
//			// should have been captured at the begining of the method
//			break;
//		default:
//			Internals.dumpStringExit(
//					"unknown family option of " + oldMember.familyOptions.get(OPTIONGROUPS.MARKINGSET).toString());
//		}
//	}

	/**
	 * throw an exception if the item is sharing a location with a different member.
	 * location sharing with same member items ok
	 * 
	 * @param oldMember
	 * @throws Exception
	 */
//	private void whyNotSharingButIdenticalOK(Attributes oldMember) throws Exception {
//		for (Part member : Part.listMembersByFamilyId(oldMember.id)) {
//			// for every item in this member
//			TimeReceived itemSameMember = null;
//			{
////			for (Item itemSameMember : Item.listBoxesInMember(member.id)) {
//				// for every item at the same location as the member item
//				Location location = itemSameMember.getLocation();
//				MyObjects objs = location.listAllParentsOfType(itemSameMember);
//				for (MyObject obj : objs) {
//					TimeReceived item = (TimeReceived) obj;
//					// if two different items at the same location
//					if (itemSameMember.getMember().id != item.getMember().id)
//						throw new Exception("location shared with " + item.getInstanceName() + " at "
//								+ item.getLocation().dumpPathAsString());
//				}
//			}
//		}
//	}

	/**
	 * throw an exception if a user specified location is in use
	 * 
	 * @param oldMember
	 * @throws Exception
	 */
//	private void whyNotNewMarkedButOldUnmarked(Attributes oldMember) throws Exception {
//		throw new Exception("The old " + Attributes.NAME + " '" + oldMember.getInstanceName()
//				+ "' has unmarked items but the new " + Attributes.NAME
//				+ " requires marked items. Remove all of the items in '" + oldMember.getInstanceName()
//				+ "' from inventory, change this option, and then put the items back "
//				+ "into inventory so they will be marked.");
//	}

	/**
	 * throw an exception if the member shares a user location with anything
	 * different
	 * 
	 * @throws Exception
	 */
//	protected void whyNotUserUnmarkedIdentical(Attributes oldMember) throws Exception {
//		for (Part member : Part.listMembersByFamilyId(oldMember.id)) {
//			// for every item in this member
//			TimeReceived itemSameMember = null;
//			{
////			for (Item itemSameMember : Item.listBoxesInMember(member.id)) {
//				Location location = itemSameMember.getLocation();
//				// for every item at the same location as the member item
//				for (MyObject obj : location.listAllParentsOfType(itemSameMember)) {
//					TimeReceived item = (TimeReceived) obj;
//					if (item.getMember().id != member.id)
//						throw new Exception("location shared with " + item.getInstanceName() + " at "
//								+ item.getLocation().dumpPathAsString());
//				}
//			}
//		}
//	}

	/**
	 * throw an exception if any sharing
	 * 
	 * @param oldMember
	 * @throws Exception
	 */
//	protected void whyNotNoSharingIdenticalNotOK(Attributes oldMember) throws Exception {
//		for (Part member : Part.listMembersByFamilyId(oldMember.id)) {
//			// for
//			// every
//			// item
//			// in
//			// this
//			// member
//			TimeReceived itemSameMember = null;
//			{
////			for (Item itemSameMember : Item.listBoxesInMember(member.id)) {
//				// for every item at the same location as the member item
//				Location location = itemSameMember.getLocation();
//				for (MyObject obj : location.listAllParentsOfType(itemSameMember)) {
//					TimeReceived item = (TimeReceived) obj;
//					// if two different items at the same location
//					if (itemSameMember.id != item.id)
//						throw new Exception("location shared with " + item.getInstanceName() + " at "
//								+ item.getLocation().dumpPathAsString());
//				}
//			}
//		}
//	}

//	protected void whyNotRefurbishing(Attributes oldMember) throws Exception {
//		return;
//	}
//
//	protected void whyNotShippingAddress(Attributes oldMember) {
//		// nothing to do
//		switch (oldMember.familyOptions.get(OPTIONGROUPS.SHIPPINGADDRESSSET)) {
//		case SHIPPINGREQUIRED:
//		case SHIPPINGNOTREQUIRED:
//			break;
//		default:
//			Internals.dumpStringExit(
//					"unknown family option of " + familyOptions.get(OPTIONGROUPS.SHIPPINGADDRESSSET).toString());
//		}
//	}

//	protected void whyNotReceiveAudio(Attributes oldMember) {
//		// nothing to do
//		switch (oldMember.familyOptions.get(OPTIONGROUPS.RECEIVEAUDIO)) {
//		case NORECEIVESOUND:
//		case SAYPOSITION:
//		case CUSTOMSOUND:
//			break;
//		default:
//			Internals.dumpStringExit(
//					"unknown family option of " + familyOptions.get(OPTIONGROUPS.RECEIVEAUDIO).toString());
//		}
//	}

	@Override
	public String getAName() {
		return ANAME;
	}

	@Override
	public String getLogicalName() {
		return NAME;
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
	public boolean equalsReminder(Object obj) {
		return false;
	}

	@Override
	public boolean equals(Object exhibit) {
		if (exhibit instanceof Attributes) {
			return id == ((Attributes) exhibit).id;
		}
		return false;
	}

	@Override
	public MyObject getNew() throws Exception {
		return new Attributes(sVars);
	}

//	public static void initLinks() throws Exception {
//
//		if (!MyConnection.tableExists(getSimpleClassNameStatic())) {
//			new Attributes(sVars).newTable(sVars);
//			TableVersion.addNewVersion(getSimpleClassNameStatic(), "first", 1);
//		}
//	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Attributes;
	}

//	public static String getSimpleClassNameStatic() {
//		return MethodHandles.lookup().lookupClass().getSimpleName();
//	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

//	public MyObjects listLegalLocations() throws Exception {
//		return new Location(sVars).listAll(getWarehouse());
//	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Warehouse(sVars));
		return objs;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Part(sVars));
		return objs;
	}
//	@Override
//	public SmartForm getSelector(SmartForm callBack, String keyWordsLabel,
//			String overallLabel, SearchTargets objs, int index) {
//		return new FamilyEdit(callBack, keyWordsLabel, overallLabel, objs,
//				index);
//	}
	public static String getSimpleClassNameStatic() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}
}