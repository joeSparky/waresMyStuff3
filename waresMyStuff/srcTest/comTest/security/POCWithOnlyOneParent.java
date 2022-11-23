package comTest.security;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class POCWithOnlyOneParent extends MyObject {
	public static final String NAME = "parent";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

	public POCWithOnlyOneParent(SessionVars sVars) throws Exception {
		super(sVars);
		new Table().tableCreated(this, sVars);
	}

	@Override
	public String getAName() {
		return ANAME;
	}

	@Override
	public String getLogicalName() {
		return NAME;
	}

//	@Override
//	public MyObject find(int id) throws Exception {
//		return extractInfo(findResultSet(id));
//	}

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

//	@Override
//	public boolean equals(Object exhibit) {
//		if (exhibit instanceof POCWithOnlyOneParent)
//			return id == ((POCWithOnlyOneParent) exhibit).id;
//		else
//			throw new ExceptionCoding("invalid class "
//					+ exhibit.getClass().getCanonicalName());
//		return false;
//	}

	@Override
	public void sanity() throws Exception {
	}

	// @Override
	// public void update() throws Exception {
	// MyStatement
	// .executeUpdateStatic("UPDATE " + getSimpleClassName()
	// + nameInstance.extendUpdate() + "  WHERE " + "id ='"
	// + id + "'");
	// }

	@Override
	public MyObject getNew() throws Exception {
		return new POCWithOnlyOneParent(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof POCWithOnlyOneParent;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName();
	}
	@Override
	public MyObjects listParentsClasses() throws Exception {
		return new MyObjects();
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		return new MyObjects();
	}

}
