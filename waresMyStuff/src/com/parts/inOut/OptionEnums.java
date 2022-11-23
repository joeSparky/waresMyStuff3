package com.parts.inOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.parts.inOut.FamilyOptions.FAMILYORMEMBER;
import com.security.ExceptionCoding;

public class OptionEnums {
	public static HashMap<OPTIONS, String> optDescription;
	static {
		optDescription = new HashMap<OPTIONS, String>();
	}
	public static HashMap<OPTIONS, OPTIONGROUPS> whichFamilyGroup;
	static {
		whichFamilyGroup = new HashMap<OPTIONS, OPTIONGROUPS>();
	}

	public static enum OPTIONGROUPS {
		/**
		 * all possible options related to ordering. there must be at least one
		 * of the options for each rule.
		 */
		MARKINGSET("Marking and storing items.", "marking",
				FAMILYORMEMBER.FAMILY);
		/**
		 * is the item numbered with a position
		 */
//		REFURBISHINGSET("Item refurbishing.", "refurb", FAMILYORMEMBER.FAMILY),
		/**
		 * 
		 */
//		SHIPPINGADDRESSSET("Shipping Address Required", "customer",
//				FAMILYORMEMBER.FAMILY),
		/**
		 * 
		 */
		// PICKCONFIRM("Pick confirmation.", "picking", FAMILYORMEMBER.FAMILY),
//		NOLONGERUSED1("No longer used", "ignore", FAMILYORMEMBER.NOUSED),
		/**
				 * 
				 */
//		RECEIVEAUDIO("Announcements during receiving.", "sounds",
//				FAMILYORMEMBER.FAMILY),

//		NOLONGERUSED("No longer used", "ignore", FAMILYORMEMBER.NOUSED),
		/**
		 * type of bar code
		 */
//		DUPEUNIQUENONEBARCODE("(Unique/Duplicate/No) barcodes", "barcodeType",
//				FAMILYORMEMBER.FAMILY),
		/**
		 * who selects the item
		 */
//		WHOSELECTSTYPE("Who selects the item", "whoSelectsItem",
//				FAMILYORMEMBER.FAMILY),
		/**
		 * barcode sub-fields. If there are multiple matches on a barcode
		 * search, this field is used as a tie-breaker when the item is picked.
		 */
//		NOLONGERUSED2(
//				"(Serial/Tracking/Expiration/Whole/Optional) Picking Barcode",
//				"mandatoryField", FAMILYORMEMBER.NOUSED);

		/**
		 * return all of the options associated with this group
		 * 
		 * @return
		 */
		public HashSet<OPTIONS> getFamilyOptionSubSet() {
			HashSet<OPTIONS> optionSubset = new HashSet<OPTIONS>();
			for (OPTIONS o : OPTIONS.values()) {
				if (whichFamilyGroup.containsKey(o)
						&& whichFamilyGroup.get(o).equals(this))
					optionSubset.add(o);
			}
			return optionSubset;
		}

		public static Set<OPTIONGROUPS> getFamilyOptionGroups() {
			// use either sqlLabels or optDescription
			return sqlLabels.keySet();
		}

		public HashSet<OPTIONS> getOptionSubSet() {
			HashSet<OPTIONS> optionSubset = new HashSet<OPTIONS>();
			for (OPTIONS opt : OPTIONS.values())
				if (whichFamilyGroup.get(opt).equals(this))
					optionSubset.add(opt);
			return optionSubset;
		}

		public String getDescription() {
			return groupDescript.get(this);
		}

		public String getSqlLabel() {
			return sqlLabels.get(this);
		}

		OPTIONGROUPS(String userDescription, String sqlLabel,
				FAMILYORMEMBER form) {
			if (!form.equals(FAMILYORMEMBER.FAMILY))
				return;
			sqlLabels.put(this, sqlLabel);
			groupDescript.put(this, userDescription);
		}

		public static int getSize(){
			return groupDescript.size();
		}
	}

	
	public static HashMap<OPTIONGROUPS, String> groupDescript;
	static {
		groupDescript = new HashMap<OPTIONGROUPS, String>();
	}
	static HashMap<OPTIONGROUPS, String> sqlLabels;
	static {
		sqlLabels = new HashMap<OPTIONGROUPS, String>();
	}

	public enum OPTIONS {
		/**
		 * 
		 */
//		NOTUSEDHERE("not used", OPTIONGROUPS.NOLONGERUSED),
		/**
		 * member order required. FIFO pick BCDUPE
		 */
//		NOTUSEDHERE5(
//				"The entire barcode identifies the item. Duplicates are allowed. (UPC, FIFO)",
//				OPTIONGROUPS.NOLONGERUSED),
		/**
		 * rule order required. FIFO pick BCUNIQUE
		 */
//		NOTUSEDHERE6(
//				"The entire barcode identifies the item. Each barcode is unique. (FIFO)",
//				OPTIONGROUPS.NOLONGERUSED),
		/**
		 * item order or no order. item pick formerly BCTRACKING
		 */
//		NOTUSEDHERE1(
//				"A portion of the barcode identifies the item such as a FedEx tracking number or serial number. Each barcode is unique.",
//				OPTIONGROUPS.NOLONGERUSED),
		/**
		 * member order required, lot pick formerly BCLOTDUPE
		 */
//		NOTUSEDHERE2(
//				"A portion of the barcode contains a lot number. Duplicates are allowed.",
//				OPTIONGROUPS.NOLONGERUSED),
		/**
		 * member order required, exp pick formerly BCEXPDUPE
		 */
//		NOTUSEDHERE3(
//				"A portion of the barcode contains an expiration date in YYYYMMDD format. Duplicates are allowed.",
//				OPTIONGROUPS.NOLONGERUSED),

		/**
		 * member order, item order, or no order. picker's choice on member
		 * orders. item pick on item order. formerly BCNONE
		 */
//		NOTUSEDHERE4(
//				"There is no barcode on the item. Duplicates are allowed.",
//				OPTIONGROUPS.NOLONGERUSED),

		/**
		 * Items are numbered and stored in the common location. Use with any
		 * barcode and ordering options
		 */
		COMMONMARKED(
				"Items are numbered and stored in the common location. Use with any barcode and ordering options.",
				OPTIONGROUPS.MARKINGSET),
		/**
		 * Items are numbered and stored in a user specified location. Use with
		 * any barcode and ordering option
		 */
		USERMARKED(
				"Items are numbered and stored in a user specified location. Use with any barcode and ordering option.",
				OPTIONGROUPS.MARKINGSET),
		/**
		 * Items are not numbered and are stored in a user specified location.
		 * Different items may share a location with other items. Can not be
		 * used with FIFO, expiration dates, tracking numbers, or lot numbers.
		 */
		USERUNMARKEDSHAREDDIF(
				"Items are not numbered and are stored in a user specified location. Different items may share a location with other items. Can not be used with FIFO, expiration dates, tracking numbers, or lot numbers.",
				OPTIONGROUPS.MARKINGSET),
		/**
		 * Items are not numbered and are stored in a user specified location.
		 * Only one item is stored at a single location. Use for FIFO, serial
		 * numbers, or tracking numbers (one item per location). Use for
		 * multiple items with the same expiration date or lot number.
		 */
		USERUNMARKEDNOTSHARED(
				"Items are not numbered and are stored in a user specified location. Only one item is stored at a single location. Use for FIFO, serial numbers, or tracking numbers (one item per location). Use for multiple items with the same expiration date or lot number.",
				OPTIONGROUPS.MARKINGSET),

		/**
		 * 
		 */
//		SHIPPINGREQUIRED("A shipping address must be specified when ordering.",
//				OPTIONGROUPS.SHIPPINGADDRESSSET),
		/**
		 * 
		 */
//		SHIPPINGNOTREQUIRED("A shipping address is not required.",
//				OPTIONGROUPS.SHIPPINGADDRESSSET),
		/**
		 * 
		 */
//		REFURBISH("An item must be refurbished before shipping.",
//				OPTIONGROUPS.REFURBISHINGSET),
		/**
		 * 
		 */
//		NOREFURBISH("An item is ready for shipping upon receipt.",
//				OPTIONGROUPS.REFURBISHINGSET),
		/**
		 * 
		 */
//		NOTUSEDHERE10(
//				"The picker must scan the items barcode beforing picking the item.",
//				OPTIONGROUPS.NOLONGERUSED),
		/**
		 * 
		 */
//		NOTUSEDHERE11("No barcode reading is required with picking.",
//				OPTIONGROUPS.NOLONGERUSED),
		/**
		 * no receive sound
		 */
//		NORECEIVESOUND("No sound during receive.", OPTIONGROUPS.RECEIVEAUDIO),
		/**
		 * announce the position
		 */
//		SAYPOSITION("The package position is announced.",
//				OPTIONGROUPS.RECEIVEAUDIO),
		/**
		 * 
		 */
//		CUSTOMSOUND("A custom sound is announced.", OPTIONGROUPS.RECEIVEAUDIO),
		/**
		 * upc without fifo BCNOFIFO
		 */
//		NOTUSEDHERE8(
//				"The entire barcode identifies the item. Duplicates are allowed. (UPC, no FIFO)",
//				OPTIONGROUPS.NOLONGERUSED),
		/**
		 * YYMMDD expiration date format BCEXP6DUPE
		 */
//		NOTUSEDHERE7(
//				"A portion of the barcode contains an expiration date in YYMMDD format. Duplicates are allowed.",
//				OPTIONGROUPS.NOLONGERUSED),
		/**
		 * Identical unmarked items can share a user location. No FIFO among
		 * identical items.
		 */
		USERUNMARKEDIDENTICAL(
				"Identical unmarked items can share a user location. No FIFO among identical items.",
				OPTIONGROUPS.MARKINGSET);

		/**
		 * There is no barcode on the item.
		 */
//		NOBARCODE("There is no barcode on the item.",
//				OPTIONGROUPS.DUPEUNIQUENONEBARCODE),
		/**
		 * The barcode is unique. No two items with the same barcode will be
		 * allowed into the warehouse.
		 */
//		UNIQUE(
//				"The barcode is unique. No two items with the same barcode will be allowed into the warehouse.",
//				OPTIONGROUPS.DUPEUNIQUENONEBARCODE),
		/**
		 * Multiple items with the same barcode can be put into the warehouse.
		 */
//		DUPE(
//				"Multiple items with the same barcode can be put into the warehouse.",
//				OPTIONGROUPS.DUPEUNIQUENONEBARCODE),
		/**
		 * TRACKINGSERIAL The barcode contains a serial number or a tracking
		 * number.
		 */
//		NOTUSEDHERE12(
//				"The barcode contains a serial number or a tracking number.",
//				OPTIONGROUPS.NOLONGERUSED),
		/**
		 * LOT The barcode contains a lot number.
		 */
//		NOTUSEDHERE13("The barcode contains a lot number.",
//				OPTIONGROUPS.NOLONGERUSED2),
		/**
		 * EXPIRATION The barcode contains an expiration date.
		 */
//		NOTUSEDHERE14("The barcode contains an expiration date.",
//				OPTIONGROUPS.NOLONGERUSED2),
		/**
		 * BARCODEOPTIONAL A barcode is optional.
		 */
//		NOTUSEDHERE15("A barcode is optional.", OPTIONGROUPS.NOLONGERUSED2),
//		/**
//		 * WHOLEBARCODE The whole barcode identifies the item. BarcodeType
//		 * UNIQUE and DUPE apply.
//		 */
//		NOTUSEDHERE16("The whole barcode identifies the item.",
//				OPTIONGROUPS.NOLONGERUSED2),
		/**
		 * The person placing the order chooses the item.
		 */
//		ORDERER("The person placing the order chooses the item.",
//				OPTIONGROUPS.WHOSELECTSTYPE),
		/**
		 * picker
		 */
//		PICKER("The person picking the order chooses the item.",
//				OPTIONGROUPS.WHOSELECTSTYPE),
		/**
		 * formerly SYSTEM
		 */
//		NOTUSEDHERE9(
//				"WaresMyStuff selects the item based on lot, exp, or fifo.",
//				OPTIONGROUPS.NOLONGERUSED);
		/**
		 * waresMyStuff
		 */
//		SYSTEMFIFO("WaresMyStuff selects the item based on FIFO. Each item must either be marked or stored individually.",
//				OPTIONGROUPS.WHOSELECTSTYPE),

		/**
		 * selection based on a field such as lot number or serial number
		 */
//		SYSTEMLOT(
//				"WaresMyStuff selects the item based on a field in a barcode such as a lot number or serial number.",
//				OPTIONGROUPS.WHOSELECTSTYPE),
		/**
		 * selection based on expiration date
		 */
//		SYSTEMEXP("WaresMyStuff selects the item based an expiration date in a barcode.",
//				OPTIONGROUPS.WHOSELECTSTYPE);

		OPTIONS(String descr, OPTIONGROUPS group) {
			optDescription.put(this, descr+" ("+this.toString()+")");
			whichFamilyGroup.put(this, group);
		}

		public static OPTIONS findEnum(int ord) throws Exception {
			for (OPTIONS a : OPTIONS.values()) {
				if (a.ordinal() == ord) {
					// switch (a) {
					// case NOTUSEDHERE1:
					// case NOTUSEDHERE2:
					// case NOTUSEDHERE3:
					// case NOTUSEDHERE4:
					// case NOTUSEDHERE5:
					// case NOTUSEDHERE6:
					// case NOTUSEDHERE7:
					// case NOTUSEDHERE8:
					// System.out.println("old enum encountered "
					// + a.toString());
					// }
					return a;
				}
			}
			throw new Exception(
					"could not find enum associated with " + ord);
//			return OPTIONS.USERUNMARKEDSHAREDDIF;
		}

		public static OPTIONS findEnum(String lookFor) throws ExceptionCoding {
			for (OPTIONS a : OPTIONS.values()) {
				if (a.toString().equals(lookFor)) {
					return a;
				}
			}
			// should never get here
			throw new ExceptionCoding(lookFor + " was not found in enum");
		}

		/**
		 * find an enum using the description string
		 * 
		 * @param lookFor
		 * @return
		 * @throws ExceptionCoding 
		 */
		public static OPTIONS findEnumUsingDescription(String lookFor) throws ExceptionCoding {
			for (OPTIONS a : OPTIONS.values()) {
				if (optDescription.get(a).equals(lookFor)) {
					return a;
				}
			}
			// should never get here
			throw new ExceptionCoding(lookFor + " was not found in enum");
		}

		public int getInt() {
			return this.ordinal();
		}

		public OPTIONGROUPS getGroup() {
			return whichFamilyGroup.get(this);
		}

	}

	/**
	 * get the next option in the group or throw an exception if overflow
	 * 
	 * @param current
	 * @return
	 * @throws Exception
	 */
	private static OPTIONS getNext(OPTIONS current) throws Exception {
		boolean found = false;
		OPTIONGROUPS group = current.getGroup();
		for (OPTIONS thisOpt : group.getOptionSubSet()) {
			if (thisOpt.equals(current)) {
				found = true;
			} else {
				if (found)
					return thisOpt;
			}
		}
		throw new Exception("overflow");
	}

	private static OPTIONS getNext(FamilyOptions current, OPTIONGROUPS group)
			throws Exception {
		boolean found = false;
		// get the current option of the group to be incremented
		OPTIONS incThis = current.get(group);
		// get the next option after this option
		found = false;
		for (OPTIONS nextOpt : orderedOptions.get(group)) {
			if (incThis == nextOpt)
				found = true;
			else if (found)
				return nextOpt;
		}
		throw new Exception("overflow");
	}

	static ArrayList<OPTIONGROUPS> orderedGroups = null;
	static HashMap<OPTIONGROUPS, ArrayList<OPTIONS>> orderedOptions = null;
	static {
		// orderedGroups = new ArrayList<OPTIONGROUPS>();
		// orderedOptions = new HashMap<OPTIONGROUPS, ArrayList<OPTIONS>>();
		// for (OPTIONGROUPS thisGrp : OPTIONGROUPS.values()) {
		// orderedGroups.add(thisGrp);
		// ArrayList<OPTIONS> thisSet = new ArrayList<OPTIONS>();
		// for (OPTIONS thisOpt : thisGrp.getFamilyOptionSubSet()) {
		// thisSet.add(thisOpt);
		// }
		// orderedOptions.put(thisGrp, thisSet);
		// }
	}

	public static FamilyOptions getNextDeleteC(OPTIONGROUPS groupNotToChange,
			FamilyOptions current) throws Exception {

		// save a copy of the original
		FamilyOptions original = new FamilyOptions();
		for (OPTIONS t : current.values()) {
			original.put(t);
		}
		// debugging
		OPTIONS justChanged = OPTIONS.USERUNMARKEDSHAREDDIF;
		// debugging
		ArrayList<OPTIONGROUPS> lastGroups = new ArrayList<OPTIONGROUPS>();

		for (OPTIONGROUPS group : orderedGroups) {
			if (group.equals(groupNotToChange) || groupIsDead(group))
				continue;
			// debugging
			lastGroups.add(group);
			try {
				justChanged = getNext(current, group);
				current.put(justChanged);
			} catch (Exception e) {
				// overflowed the group.
				// reset the "least significant digits"
				for (OPTIONGROUPS thisGrp : lastGroups) {
					current.put(orderedOptions.get(thisGrp).get(0));
				}
				continue;
			}
			return current;
		}
		throw new Exception("exhausted. original options: "
				+ original.toString() + " group not to change: "
				+ groupNotToChange.toString() + " current: "
				+ current.toString() + " just changed: " + justChanged
				+ " last groups: " + lastGroups);
	}

	private static OPTIONGROUPS getNextGroup(OPTIONGROUPS group,
			OPTIONGROUPS dontChange) throws Exception {
		boolean found = false;
		for (OPTIONGROUPS thisGroup : orderedGroups) {
			// skip the dontChange group
			if (thisGroup.equals(dontChange))
				continue;
			if (group.equals(thisGroup)) {
				found = true;
			} else {
				if (found)
					return thisGroup;
			}
		}
		throw new Exception("done");
	}

	public static OPTIONGROUPS groupToChange = null;

	/**
	 * generate a default familyOptions which are synchronized with the
	 * orderedOptions
	 * 
	 * @return
	 */
	public static FamilyOptions generateDefault() {
		setupOrderedGroups();
		FamilyOptions ret = new FamilyOptions();
		for (OPTIONGROUPS thisGrp : orderedGroups) {
			ret.put(orderedOptions.get(thisGrp).get(0));
		}
		return ret;
	}

	private static void setupOrderedGroups() {
		if (orderedGroups == null) {
			orderedGroups = new ArrayList<OPTIONGROUPS>();
			orderedOptions = new HashMap<OPTIONGROUPS, ArrayList<OPTIONS>>();
			for (OPTIONGROUPS thisGrp : OPTIONGROUPS.values()) {
				if (groupIsDead(thisGrp))
					continue;
				orderedGroups.add(thisGrp);
				ArrayList<OPTIONS> thisSet = new ArrayList<OPTIONS>();
				for (OPTIONS thisOpt : thisGrp.getFamilyOptionSubSet()) {
					thisSet.add(thisOpt);
				}
				orderedOptions.put(thisGrp, thisSet);
			}
		}
	}

	public static void getNextOptionSetDelete(OPTIONGROUPS groupNotToChange,
			FamilyOptions current, boolean startOver) throws ExceptionCoding {

		setupOrderedGroups();
		if (startOver)
			groupToChange = null;

		if (groupToChange == null) {
			// find the first group that does not equal groupNotToChange
			for (OPTIONGROUPS opt : orderedGroups) {
				if (opt.equals(groupNotToChange))
					continue;
				groupToChange = opt;
				break;
			}
			// dumpInternals(current, groupNotToChange,
			// "starting over", groupToChange);
			if (groupToChange == null)
				throw new ExceptionCoding("missed group");
		}
		OPTIONS justChanged = null;
		boolean overflowed;
		do {
			overflowed = false;
			try {
				justChanged = getNext(current, groupToChange);
				current.put(justChanged);
				// dumpInternals(current, groupNotToChange,
				// "next option", groupToChange);
				return;
			} catch (Exception e) {
				overflowed = true;
			}
			if (overflowed) {
				// dumpInternals(current, groupNotToChange, "overflowed",
				// groupToChange);
				// overflowed the group.
				// reset the "least significant digits"
				for (OPTIONGROUPS thisGrp : orderedGroups) {
					if (thisGrp.equals(groupNotToChange))
						continue;// reset the "digit"
					current.put(orderedOptions.get(thisGrp).get(0));
					// if this is the group that overflowed
					if (thisGrp.equals(groupToChange))
						break;
				}
				// dumpInternals(current, groupNotToChange,
				// "lsd reset", groupToChange);
				try {
					groupToChange = getNextGroup(groupToChange,
							groupNotToChange);
					// dumpInternals(current, groupNotToChange,
					// "new groupToChange", groupToChange);
				} catch (Exception e) {
					throw new ExceptionCoding("exhausted");
				}
			}
		} while (overflowed);
	}

	public static FamilyOptions getNextDeleteB(OPTIONGROUPS groupNotToChange,
			FamilyOptions current) throws Exception {
		// save a copy of the original
		FamilyOptions original = new FamilyOptions();
		for (OPTIONS t : current.values()) {
			original.put(t);
		}
		// debugging
		OPTIONS justChanged = OPTIONS.USERUNMARKEDSHAREDDIF;
		// debugging
		ArrayList<OPTIONGROUPS> lastGroups = new ArrayList<OPTIONGROUPS>();

		for (OPTIONGROUPS group : OPTIONGROUPS.values()) {
			if (group.equals(groupNotToChange) || groupIsDead(group))
				continue;
			// debugging
			lastGroups.add(group);
			try {
				justChanged = getNext(current.get(group));
				current.put(justChanged);
			} catch (Exception e) {
				// overflowed the group. try the next group. start by reloading
				// the original
				current.clear();
				for (OPTIONS t : original.values())
					current.put(t);
				// reset the "least significant digits"
				for (OPTIONGROUPS thisGrp : lastGroups) {
					// reload the first member of each group
					for (OPTIONS thisOpt : thisGrp.getFamilyOptionSubSet()) {
						current.put(thisOpt);
						break;
					}
				}
				continue;
			}
			return current;
		}
		throw new Exception("exhausted. original options: "
				+ original.toString() + " group not to change: "
				+ groupNotToChange.toString() + " current: "
				+ current.toString() + " just changed: " + justChanged
				+ " last groups: " + lastGroups);
	}

	public static void dumpInternals(FamilyOptions opts,
			OPTIONGROUPS dontChange, String comment, OPTIONGROUPS incGroup) {
		for (OPTIONGROUPS opt : orderedGroups) {
			System.out.print(" " + opts.get(opt).toString());
		}
		System.out.print(" don't change: " + dontChange.toString());
		if (incGroup == null)
			System.out.print(" inc: null");
		else
			System.out.print(" inc: " + incGroup.toString());
		System.out.println(" " + comment);
	}

	public enum OPTIONSTATE {
		NOTINITIALIZED,
		/**
		 * reset the current FamilyOptions, return the next FamilyOptions
		 */
		STARTOVER,
		/**
		 * return the next FamilyOptions without resetting
		 */
		KEEPGOING
	}

	public static void getNextOptionSet(OPTIONGROUPS groupNotToChange,
			FamilyOptions current, OPTIONSTATE startOver) throws Exception {

		setupOrderedGroups();
		switch (startOver) {
		case NOTINITIALIZED:
			throw new ExceptionCoding("not initialized");
		case STARTOVER:
			resetGroupToChange(groupNotToChange, current);
			break;
		case KEEPGOING:
			break;
		// case RECURSION:
		// throw new ExceptionCoding("not implemented");
		default:
			throw new ExceptionCoding("missing OPTIONSTATE");
		}

		OPTIONS justChanged = null;
		boolean overflowed;
		do {
			overflowed = false;
			try {
				justChanged = getNext(current, groupToChange);
				current.put(justChanged);
				// dumpInternals(current, groupNotToChange,
				// "next option", groupToChange);
				return;
			} catch (Exception e) {
				overflowed = true;
			}
			if (overflowed) {
				// dumpInternals(current, groupNotToChange, "overflowed",
				// groupToChange);
				// overflowed the group.
				// reset the "least significant digits"
				for (OPTIONGROUPS thisGrp : orderedGroups) {
					if (thisGrp.equals(groupNotToChange))
						continue;// reset the "digit"
					current.put(orderedOptions.get(thisGrp).get(0));
					// if this is the group that overflowed
					if (thisGrp.equals(groupToChange))
						break;
				}
				// dumpInternals(current, groupNotToChange, "lsd reset",
				// groupToChange);
				try {
					groupToChange = getNextGroup(groupToChange,
							groupNotToChange);
					// dumpInternals(current, groupNotToChange,
					// "new groupToChange", groupToChange);
				} catch (Exception e) {
					if (outOfGroups(groupToChange, groupNotToChange)) {
						// dumpInternals(current, groupNotToChange, "exhausted",
						// groupToChange);
						throw new Exception(e);
					}
				}
				// dumpInternals(current, groupNotToChange, "before recur",
				// groupToChange);
				getNextOptionSet(groupNotToChange, current,
						OPTIONSTATE.KEEPGOING);
				// dumpInternals(current, groupNotToChange, "after Recur",
				// groupToChange);
				resetGroupToChange(groupNotToChange, current);
				// dumpInternals(current, groupNotToChange,
				// "after resetting groupToChange", groupToChange);
				return;
			}
		} while (overflowed);
	}

	private static void resetGroupToChange(OPTIONGROUPS groupNotToChange,
			FamilyOptions current) throws ExceptionCoding {
		for (OPTIONGROUPS opt : orderedGroups) {
			if (opt.equals(groupNotToChange))
				continue;
			groupToChange = opt;
			break;
		}
		// dumpInternals(current, groupNotToChange, "resetting groupToChange",
		// groupToChange);
		if (groupToChange == null)
			throw new ExceptionCoding("missed group");
	}

	private static boolean outOfGroups(OPTIONGROUPS group,
			OPTIONGROUPS dontChange) {
		boolean found = false;
		for (OPTIONGROUPS thisGroup : orderedGroups) {
			// skip the dontChange group
			if (thisGroup.equals(dontChange))
				continue;
			if (group.equals(thisGroup)) {
				found = true;
			} else {
				if (found)
					return false;
			}
		}
		return true;
	}

	public static boolean groupIsDead(OPTIONGROUPS group) {
		
			return false;

		
	}
}
