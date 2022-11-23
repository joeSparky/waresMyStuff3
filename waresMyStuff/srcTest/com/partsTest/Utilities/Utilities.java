package com.partsTest.Utilities;

import static org.junit.Assert.fail;

import com.forms.Utils;
import com.parts.exhibit.Kit;
import com.parts.exhibit.Vendor;
import com.parts.inOut.Attributes;
import com.parts.inOut.Part;
import com.parts.location.Location;
import com.parts.warehouse.Warehouse;
import com.security.Anchor;
import com.security.Company;
import com.security.MyObject;

public class Utilities extends comTest.utilities.Utilities {
	private static String UTILITY = "utility";
	public static String ASSEMBLYNAME = UTILITY + "Assembly";
	public static String ITEMNAME = UTILITY + "Item";
	public static String LOCATIONNAME = UTILITY + "Location";
	public static String ASSEMBLYLOCATION = UTILITY + "AssemblyLocation";
	public static String PARTNAME = UTILITY + "Part";
	public static String EXHIBITNAME = UTILITY + "Exhibit";
	public static String EXHIBITLOCATION = UTILITY + "ExhibitLocation";
	public static String VENDORNAME = UTILITY + "VendorName";
	public static String VENDORURL = UTILITY + "VendorURL";
	public static String FIRSTNAME = UTILITY + "firstName";
	public static String LASTNAME = UTILITY + "lastName";
	public static String USERNAME = UTILITY + "userName";
	public static String PASSWORD = UTILITY + "password";
	public static String WAREHOUSE = UTILITY + "warehouse";
	public static String FAMILYNAME = UTILITY + "Family";
	public static String FAMILYDESCRIPTION = UTILITY + "family description";
	public static String MEMBERNAME = UTILITY + "Member";

	public static Company getACompany() {
		Company company = null;
		Anchor anchor = null;
		try {
			company = new Company(sVars);
			anchor = new Anchor(sVars);
			company.setInstanceName(COMPANY + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			company.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (!company.getAnchor().equals(anchor))
				fail("company does not anchor itself");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		return company;
	}

	@Override
	public MyObject getAnObject(MyObject m, Anchor anchor) {
		// if (m instanceof Assembly)
		// return getAnAssembly();
		if (m instanceof Attributes)
			return getAFamily(getAWarehouse(anchor), anchor);
		if (m instanceof Part)
			return getAMember(getAFamily(getAWarehouse(anchor), anchor), anchor);

		if (m instanceof Kit)
			return getAnExhibit(anchor);
		if (m instanceof Vendor)
			return getAVendor(anchor);
		if (m instanceof Warehouse)
			return getAWarehouse(anchor);
//		if (m instanceof TimeReceived)
//			try {
//				return getAnItem(getAMember(getAFamily(getAWarehouse(anchor), anchor), anchor),
//						getLocation(getAWarehouse(anchor).getAnchor()), anchor);
//			} catch (Exception e) {
//				fail(e.getLocalizedMessage());
//			}
		return super.getAnObject(m, anchor);
	}

//	public static TimeReceived getAnItem(Part member, Location location, MyObject myObjectAnchor) {
//		Anchor anchor = null;
//		try {
//			anchor = new Anchor(sVars).find(myObjectAnchor);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		return getAnItem(member, location, anchor);
//	}

//	public static TimeReceived getAnItem(Part member, Location location, Anchor anchor) {
//		if (member.id == 0)
//			fail("member is not loaded");
//
//		TimeReceived item = null;
//
//		// try {
//		// item.setInstanceName(ITEMNAME + Utils.getNextString());
//		// } catch (Exception e) {
//		// fail(e.getLocalizedMessage());
//		// }
//		try {
//			item = new TimeReceived();
//			item.setInstanceName(ITEMNAME + Utils.getNextString());
//			item.add(anchor);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		try {
//			member.addChild(item, 1);
//			item.addChild(location, 1);
//			// item.addAsParent(member);
////			item.addChild(location, 1);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
//		
//		return item;
//	}

	/**
	 * add a generic location in warehouse. Assumes no parent location.
	 * 
	 * @param warehouse
	 * @return
	 */
	public static Location getLocation(Anchor anchor) {
		Location location = null;
		// location.warehouse = warehouse.id;
		try {
			location = new Location(sVars);
			location.setInstanceName(LOCATIONNAME + Utils.getNextString());
			location.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		return location;
	}

//	public static Location getLocation(MyObject myObjectAnchor){
//		Anchor anchor=null;
//		try {
//			anchor = new Anchor(sVars).find(myObjectAnchor);
//		} catch (AnchorNotFoundException e) {
//			fail(e.getLocalizedMessage());
//		}
//		return getLocation(anchor);
//	}

	public static Kit getAnExhibit(Anchor anchor) {
		Kit exhibit = null;
		try {
			exhibit = new Kit(sVars);
			exhibit.setInstanceName(EXHIBITNAME + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// exhibit.location = EXHIBITLOCATION + Utils.getNextString();

		try {
			exhibit.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		// try {
		// new MyLinkObject(exhibit, new Assembly()).add(1);
		// } catch (Exception e) {
		// Internals.dumpExceptionExit(e);
		// }
		return exhibit;
	}

	public static Vendor getAVendor(Anchor anchor) {
		Vendor vendor = null;
		try {
			vendor = new Vendor(sVars);
			vendor.setInstanceName(VENDORNAME + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		vendor.url = VENDORURL + Utils.getNextString();
		try {
			vendor.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		return vendor;
	}

	public static Warehouse getAWarehouse(Anchor myObjectAnchor) {
		Warehouse warehouse = null;
		try {
			warehouse = new Warehouse(sVars);
			warehouse.setInstanceName(WAREHOUSE + Utils.getNextString());
			warehouse.add(myObjectAnchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		return warehouse;
	}

	public static Attributes getAFamily(Warehouse w, Anchor anchor) {
		Attributes family = null;

		try {
			family = new Attributes(sVars);
			family.setInstanceName(FAMILYNAME + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		family.familyOptions.clone(
//				OptionEnums.OPTIONS.COMMONMARKED
////				OptionEnums.OPTIONS.NOREFURBISH,
////				OptionEnums.OPTIONS.SHIPPINGNOTREQUIRED, 
////				OptionEnums.OPTIONS.NORECEIVESOUND, 
////				OptionEnums.OPTIONS.PICKER,
////				OptionEnums.OPTIONS.NOBARCODE
//				);
//		try {
//			family.familyOptions.optionFamilySanity();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
		try {
			family.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			family.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			w.addChild(family);
			// family.addAsParent(w);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		return family;
	}

	/**
	 * get a family where a location can be consolidated with identical members
	 * 
	 * @param w
	 * @return
	 */
	public static Attributes getConsolidateFamily(Warehouse w, Anchor anchor) {
		Attributes family = getAFamily(w, anchor);
//		family.familyOptions.replace(OptionEnums.OPTIONGROUPS.MARKINGSET, OptionEnums.OPTIONS.USERUNMARKEDSHAREDDIF);
		try {
			family.update();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		return family;
	}

	public static Part getAMember(Attributes f, Anchor anchor) {
		Part member = null;
		if (f.id == 0)
			fail("family not set up");
		try {
			member = new Part(sVars);
			member.setInstanceName(MEMBERNAME + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			member.add(anchor);
		} catch (Exception e) {
			fail("could not add " + member.getInstanceName() + " exception:" + e.getLocalizedMessage());
		}
		try {
			f.addChild(member);
			// member.addAsParent(f);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		return member;
	}
}
