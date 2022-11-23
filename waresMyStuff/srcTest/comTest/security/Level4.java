package comTest.security;

import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.Anchor;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

import comTest.utilities.Utilities;

public class Level4 extends MyObject {
	public static final String NAME = "Level4";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

	public Level4(SessionVars sVars) throws Exception {
super(sVars);
		try {
			setInstanceName(NAME + " " + Utilities.getPaddedNextString(6));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}new Table().tableCreated(this, sVars);
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

	@Override
	public boolean equals(Object exhibit) {
		return (exhibit instanceof Level4) && id == ((Level4) exhibit).id;
	}

	@Override
	public void sanity() throws Exception {
	}

//	@Override
//	public void update() throws Exception {
//		MyStatement.executeUpdateStatic("UPDATE " + getSimpleClassName()
//				+ " SET " + extendUpdateInstanceName() + "'  WHERE " + "id ='"
//				+ id + "'");
//	}

	@Override
	public MyObject getNew() throws Exception {
		return new Level4(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Level4;
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
	@Override
	public Level4 add(Anchor anchor) throws Exception {
		return (Level4)super.add(anchor);
	}
}
