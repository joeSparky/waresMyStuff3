package comTest.security;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class ChildWithOnlyOneParent extends MyObject {
	public ChildWithOnlyOneParent(SessionVars sVars) throws Exception {
		super(sVars);
		new Table().tableCreated(this, sVars);
	}
	public static final String NAME = "Child with only one parent";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

//	public ChildWithOnlyOneParent() throws Exception {
//		clear();
//		rememberMe(this);
//	}

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
//	public boolean equals(Object myObject) {
//		if (myObject instanceof MyObject)
//			return id == ((MyObject) myObject).id;
//		if (myObject instanceof MyLinkObject) {
//			return (myObject.getClass().getName().equals(this.getClass()
//					.getName()));
//		} else
//			Internals.dumpStringExit("invalid class "
//					+ myObject.getClass().getCanonicalName());
//		return false;
//	}

	@Override
	public MyObject getNew() throws Exception {
		return new ChildWithOnlyOneParent(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof ChildWithOnlyOneParent;
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
