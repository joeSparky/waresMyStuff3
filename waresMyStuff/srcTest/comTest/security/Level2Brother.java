package comTest.security;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class Level2Brother extends MyObject {
	public static final String NAME = "Level2";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

	public Level2Brother(SessionVars sVars) throws Exception {
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
//			throw new ExceptionCoding("invalid class "
//					+ myObject.getClass().getCanonicalName());
//		return false;
//	}

	@Override
	public void sanity() throws Exception {
	}

//	@Override
//	public void update() throws Exception {
//		MyStatementStatic.executeMyUpdateStatic("UPDATE " + getMyFileName()
//				+ " SET " + extendUpdateInstanceName() + "'  WHERE " + "id ='"
//				+ id + "'");
//	}

	@Override
	public MyObject getNew() throws Exception {
		return new Level2Brother(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Level2Brother;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName();
	}
	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects ret = new MyObjects();
		ret.add(new Level1(sVars));
		return ret;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		return new MyObjects();
	}

}
