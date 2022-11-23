package com.parts.inOut;

import java.util.HashMap;
import java.util.List;

import com.parts.inOut.OptionEnums.OPTIONGROUPS;
import com.parts.inOut.OptionEnums.OPTIONS;
import com.security.ExceptionCoding;

/**
 * manage the rule
 * 
 * @author joe
 * 
 */
public class FamilyOptions extends
		HashMap<OptionEnums.OPTIONGROUPS, OptionEnums.OPTIONS> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8524144825169080099L;
	private static boolean firstTime = true;

	public FamilyOptions() {
		if (firstTime) {
			firstTime = false;
			// force an evaluation of the enums
			OPTIONS.COMMONMARKED.equals(OPTIONS.COMMONMARKED);

			// // ignore the NOTUSEDHERE entries
			// if (whichFamilyGroup.size() != OPTIONS.values().length - 9)
			// throw new ExceptionCoding("missing a member "
			// + OPTIONGROUPS.values().length);
		}
		clear();
	}

	public static final String TABLENAME = "product";
	public static final int MODELLENGTH = 40;
	public static final int DESCRIPTIONLENGTH = 40;
	public static final String NAME = "Item Behavior";

	// public OPTIONS put(OPTIONGROUPS s, OPTIONS option) {
	// return put(s, option);
	// }

	// public void putAll(FamilyOptionsMap options) {
	// putAll(options);
	// }

	// public OPTIONS get(OPTIONGROUPS s) {
	// return get(s);
	// }

	/**
	 * if a user location is required
	 * 
	 * @return
	 * @throws ExceptionCoding 
	 */
	public boolean needsUserLoc() throws ExceptionCoding {
		switch (get(OPTIONGROUPS.MARKINGSET)) {

		case COMMONMARKED:
			return false;
		case USERMARKED:
		case USERUNMARKEDSHAREDDIF:
		case USERUNMARKEDNOTSHARED:
		case USERUNMARKEDIDENTICAL:
			return true;
		default:
			throw new ExceptionCoding("unknown family option of "
					+ get(OPTIONGROUPS.MARKINGSET).toString());
		}
//		return false;
	}

	/**
	 * the final item is chosen by the picker
	 * 
	 * @return
	 */
//	public boolean needsPicker() {
//		return containsValue(OPTIONS.PICKER);
//	}

	/**
	 * the final item is chosen by the orderer
	 * 
	 * @return
	 */
//	public boolean needsOrderer() {
//		return containsValue(OPTIONS.ORDERER);
//	}

//	public boolean needsUniqueBarcodes() {
//		return containsValue(OPTIONS.UNIQUE);
//	}

//	public boolean allHaveSerial(Part member) {
//		if (member.itemBarcodes.isEmpty())
//			return false;
//		for (ItemBarcode ibc : member.itemBarcodes) {
//			if (!ibc.hasSerial())
//				return false;
//		}
//		return true;
//	}

//	public boolean allHaveWhole(Part member) {
//		if (member.itemBarcodes.isEmpty())
//			return false;
//		for (ItemBarcode ibc : member.itemBarcodes) {
//			if (!ibc.hasWholeBarcode())
//				return false;
//		}
//		return true;
//	}

//	public boolean allHaveLot(Part member) {
//		if (member.itemBarcodes.isEmpty())
//			return false;
//		for (ItemBarcode ibc : member.itemBarcodes) {
//			if (!ibc.hasLot())
//				return false;
//		}
//		return true;
//	}

//	public boolean noneHaveBarcodes(Part member) {
//		return member.itemBarcodes.isEmpty();
//	}

//	public boolean allHaveExp(Part member) {
//		if (member.itemBarcodes.isEmpty())
//			return false;
//		for (ItemBarcode ibc : member.itemBarcodes) {
//			if (!ibc.hasExp())
//				return false;
//		}
//		return true;
//	}

	/**
	 * the system chooses the final item
	 * 
	 * @return
	 */
//	public boolean needsSystemPick() {
//		switch (get(OPTIONGROUPS.WHOSELECTSTYPE)) {
//		case PICKER:
//		case ORDERER:
//			return false;
//		case SYSTEMEXP:
//		case SYSTEMLOT:
//		case SYSTEMFIFO:
//			return true;
//		default:
//			throw new ExceptionCoding("unknown option of "
//					+ get(OPTIONGROUPS.WHOSELECTSTYPE).toString());
//		}
//		return false;
//	}

	/**
	 * item needs refurbishing after receiving
	 * 
	 * @return
	 */
//	public boolean needsRefurbishAfterReceive() {
//		return containsValue(OPTIONS.REFURBISH);
//	}

	/**
	 * if the item can support a system generated unique barcode
	 * 
	 * @return
	 */
//	public boolean canHaveAutoGenBarcode() {
//		if (containsValue(OPTIONS.NOBARCODE))
//			return false;
//		if (containsValue(OPTIONS.SYSTEMEXP))
//			return false;
//		if (containsValue(OPTIONS.SYSTEMLOT))
//			return false;
//		return true;
//	}

//	public boolean needsShippingAddress() {
//		return containsValue(OPTIONS.SHIPPINGREQUIRED);
//	}
//
//	public boolean needsInspection() {
//		return containsValue(OPTIONS.REFURBISH);
//	}

	/**
	 * if a sequential marking is required
	 * 
	 * @return
	 */
	public boolean needsSeqMark() {
		return containsValue(OPTIONS.COMMONMARKED)
				|| containsValue(OPTIONS.USERMARKED);
	}

	/**
	 * different items can share a user location
	 * 
	 * @return
	 * @throws ExceptionCoding 
	 */
	public boolean differentItemsCanShareUserLocation() throws ExceptionCoding {

		switch (get(OPTIONGROUPS.MARKINGSET)) {
		case COMMONMARKED:
		case USERUNMARKEDNOTSHARED:
		case USERUNMARKEDIDENTICAL:
			return false;
		case USERMARKED:
		case USERUNMARKEDSHAREDDIF:
			return true;
		default:
			throw new ExceptionCoding("unknown family option of "
					+ get(OPTIONGROUPS.MARKINGSET).toString());
		}
//		return false;
	}

	public boolean identicalItemsCanShareUserLocation() throws ExceptionCoding {
		switch (get(OPTIONGROUPS.MARKINGSET)) {
		case COMMONMARKED:
		case USERUNMARKEDNOTSHARED:
			return false;
		case USERMARKED:
		case USERUNMARKEDSHAREDDIF:
		case USERUNMARKEDIDENTICAL:
			return true;
		default:
			throw new ExceptionCoding("unknown option group of pickset");
		}
		// can't get here
//		return false;
	}

	public boolean familyOptionOn(OPTIONS check) {
		return containsValue(check);
	}

	/**
	 * map an option to its group
	 */
	public static enum FAMILYORMEMBER {
		NOUSED,
		/**
		 * the group of options is at the family level
		 */
		FAMILY
		/**
		 * the group of options is at the group level
		 */
		// MEMBER
	}

	/**
	 * if there can be more than one copy of an item picked or received, such as
	 * duplicate documents.
	 * 
	 * @return
	 * @throws Exception 
	 */
//	public boolean needsQuantity() {
//		// if each item is individually marked
//		if (needsSeqMark())
//			// limit quantities to one
//			return false;
//		if (mustHaveBarcode() && containsValue(OPTIONS.UNIQUE))
//			// limit quantities to one
//			return false;
//		return true;
//	}

	public void setFamilyOptions(List<OPTIONS> list) throws Exception {

		if (list.size() != OPTIONGROUPS.getFamilyOptionGroups().size()) {
			throw new Exception("should have "
					+ OPTIONGROUPS.getFamilyOptionGroups().size() + " "
					+ Attributes.NAME + " options.");
		}
		clear();
		for (OPTIONS a : list) {
			OPTIONGROUPS g = OptionEnums.whichFamilyGroup.get(a);
			if (containsKey(g))
				throw new ExceptionCoding(OptionEnums.groupDescript.get(g)
						+ " is already set");
			put(g, a);
		}
	}

	public void setFamilyOptions(OPTIONS... options) throws Exception {
		if (options.length != OPTIONGROUPS.getFamilyOptionGroups().size()) {
			throw new Exception(
					"invalid option list length");
		}
		for (OPTIONS a : options) {
			OPTIONGROUPS g = OptionEnums.whichFamilyGroup.get(a);
			if (containsKey(g))
				throw new ExceptionCoding(OptionEnums.groupDescript.get(g)
						+ " is already set");
			put(g, a);
		}
	}

//	public boolean sayPosition() {
//		if (familyOptionOn(OPTIONS.SAYPOSITION))
//			return true;
//		else
//			return false;
//	}
//
//	public boolean sayCustom() {
//		if (familyOptionOn(OPTIONS.CUSTOMSOUND))
//			return true;
//		else
//			return false;
//	}

	public OPTIONS findEnum(int enumInt) {
		return OPTIONS.values()[enumInt];
	}

//	public boolean mandatoryExpiration() {
//		return containsValue(OPTIONS.SYSTEMEXP);
//	}
//
//	public boolean mandatoryLot() {
//		return containsValue(OPTIONS.SYSTEMLOT);
//	}
//
//	public boolean mandatoryBarcode() {
//		switch (get(OptionEnums.OPTIONGROUPS.WHOSELECTSTYPE)) {
//		case SYSTEMEXP:
//		case SYSTEMLOT:
//			return true;
//		case PICKER:
//		case ORDERER:
//		case SYSTEMFIFO:
//			return false;
//		default:
//			throw new ExceptionCoding("unknown mandatoryField of "
//					+ get(OptionEnums.OPTIONGROUPS.WHOSELECTSTYPE).toString());
//			return false;
//		}
//	}

//	public boolean needsSystemOrder() {
//		return needsSystemPick();
//	}
//
//	public boolean itemNeedsSortBy() {
//		return !containsValue(OPTIONS.NOBARCODE);
//	}
//
//	public boolean noBarcode() {
//		return containsValue(OPTIONS.NOBARCODE);
//	}
//
//	public boolean duplicateBarcodesAllowed() {
//		return containsValue(OPTIONS.DUPE);
//	}

	/**
	 * if an item has a barcode, it must be unique
	 * 
	 * @return
	 */
//	public boolean uniqueBarcodesRequired() {
//		return containsValue(OPTIONS.UNIQUE);
//	}
//
//	public boolean mustHaveBarcode() {
//		return mandatoryBarcode();
//	}
//
//	public boolean canHaveBarcode() {
//		return !containsValue(OPTIONS.NOBARCODE);
//	}

//	static {
//		Incompatibles.set(OPTIONS.NOBARCODE, OPTIONS.SYSTEMEXP);
//		Incompatibles.set(OPTIONS.NOBARCODE, OPTIONS.SYSTEMLOT);
//
//		Incompatibles.set(OPTIONS.SYSTEMLOT, OPTIONS.USERUNMARKEDSHAREDDIF);
//		Incompatibles.set(OPTIONS.SYSTEMEXP, OPTIONS.USERUNMARKEDSHAREDDIF);
//		Incompatibles.set(OPTIONS.SYSTEMFIFO, OPTIONS.USERUNMARKEDSHAREDDIF);
//
//		Incompatibles.set(OPTIONS.SAYPOSITION, OPTIONS.USERUNMARKEDSHAREDDIF);
//		Incompatibles.set(OPTIONS.SAYPOSITION, OPTIONS.USERUNMARKEDIDENTICAL);
//		Incompatibles.set(OPTIONS.SAYPOSITION, OPTIONS.USERUNMARKEDNOTSHARED);
//	}

	public void optionMemberSanity() throws Exception {

		for (OPTIONS option : values()) {
			for (OPTIONS incompatibleOption : Incompatibles.get(option)) {
				if (values().contains(incompatibleOption))
					incompatible(option, incompatibleOption);
			}
			for (OPTIONS mustHaveOption : MustHaves.get(option)) {
				if (!values().contains(mustHaveOption))
					mustHaveOption(option, mustHaveOption);
			}
		}
		for (OPTIONGROUPS group : OPTIONGROUPS.getFamilyOptionGroups()) {
			// the option that is turned on for this group
			OPTIONS thisOpt = get(group);
			// for each incompatible option with this group
			for (OPTIONS opt : Incompatibles.get(thisOpt)) {
				// is the incompatible option turned on?
				if (containsValue(opt))
					incompatible(thisOpt, opt);
			}
		}

		// see if each option group is set
		for (OPTIONGROUPS a : OPTIONGROUPS.getFamilyOptionGroups()) {
			if (get(a) == null) {
				throw new Exception("\"" + a.getDescription()
						+ "\" is not set.");
			}
		}
		// number of options
		if (size() != OPTIONGROUPS.getSize())
			throw new Exception("invalid number of family options");
		optionsHaveDeadGroup();
		optionFamilySanity();
	}

	public void clone(OptionEnums.OPTIONS... options) throws ExceptionCoding {
		if (options.length != OptionEnums.OPTIONGROUPS.getSize())
			throw new ExceptionCoding("missing option " + options.toString());
		clear();
		for (OptionEnums.OPTIONS opt : options) {
			put(opt);
		}
	}

	public void optionFamilySanity() throws Exception {

		// see if each option group is set
		for (OPTIONGROUPS a : OPTIONGROUPS.getFamilyOptionGroups()) {
			if (get(a) == null) {
				throw new Exception("\"" + a.getDescription()
						+ "\" is not set.");
			}
		}
		for (OPTIONGROUPS group : OPTIONGROUPS.getFamilyOptionGroups()) {
			// the option that is turned on for this group
			OPTIONS thisOpt = get(group);
			// for each incompatible option with this group
			for (OPTIONS opt : Incompatibles.get(thisOpt)) {
				// is the incompatible option turned on?
				if (containsValue(opt))
					incompatible(thisOpt, opt);
			}
		}

		// family updates have have member options set, so more options set than
		// just family
		if (size() < OPTIONGROUPS.getFamilyOptionGroups().size())
			throw new Exception("invalid number of family options");
		optionsHaveDeadGroup();
	}

	private void incompatible(OPTIONS first, OPTIONS second) throws Exception {
		throw new Exception("'" + OptionEnums.optDescription.get(first)
				+ "'<br>is not compatible with<br>'"
				+ OptionEnums.optDescription.get(second) + "'");
	}

	private void mustHaveOption(OPTIONS first, OPTIONS second) throws Exception {
		throw new Exception("'" + OptionEnums.optDescription.get(first)
				+ "'<br>must have<br>'"
				+ OptionEnums.optDescription.get(second) + "'");
	}

//	public boolean barcodeReady(ItemBarcodes itemBarcodes) {
//		switch (get(OPTIONGROUPS.WHOSELECTSTYPE)) {
//
//		case SYSTEMEXP:
//			if (itemBarcodes.expirationSetUp())
//				return true;
//			break;
//		case SYSTEMLOT:
//			if (itemBarcodes.lotSetUp())
//				return true;
//			break;
//		default:
//			throw new ExceptionCoding("unknown mandatoryField of "
//					+ get(OPTIONGROUPS.WHOSELECTSTYPE).toString());
//			return false;
//		}
//		return false;
//	}

//	public boolean optionsAreClear() {
//		return isEmpty();
//	}

//	public boolean sytemSelects() {
//		return needsSystemOrder();
//	}

	/**
	 * orderer selects item for picking
	 * 
	 * @return
	 */
//	public boolean ordererPicksItem() {
//		return containsValue(OPTIONS.ORDERER);
//	}

	public OptionEnums.OPTIONS put(OptionEnums.OPTIONS opt) {
		return put(OptionEnums.whichFamilyGroup.get(opt), opt);
	}

	public void optionsHaveDeadGroup() throws Exception {
		for (OPTIONGROUPS group : this.keySet()) {
			if (OptionEnums.groupIsDead(group))
				throw new Exception(group.toString() + " should not be used");
		}
	}

	public OPTIONS[] toArray() {
		OPTIONS[] ret = new OPTIONS[OptionEnums.OPTIONGROUPS.getSize()];
		int i = 0;
		for (OPTIONS opt : values()) {
			ret[i++] = opt;
		}
		return ret;
	}

	// TODO bug When editing a family, the "Edit" submit button should be
	// "Edit / Delete"
	// TODO bug When ordering from inventory, when a member level order is
	// placed, an error "Must be ordered at the rule level." is reported.
	// TODO feature only one person can order a single item
	// TODO feature add pick column to orderCancel
	// TODO bug after selecting the family in "Order, enter tracking number",
	// WMS response with "null". then works ok
	// TODO bug while entering tracking number in
	// "Order, enter tracking number", WMS responds with "invalid rule Option".
	// TODO there is no way to add a barcode in the "Put items in and edit" form
}
