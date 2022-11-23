package comTest.security;

import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.forms.Utils;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class Recurse extends MyObject {
	public static final String NAME = "recursive";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

	public Recurse(SessionVars sVars) throws Exception {
		super(sVars);
//		clear();
		new Table().tableCreated(this, sVars);
		try {
			setInstanceName(NAME + " " + Utils.getNextString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
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
//	public boolean equals(Object exhibit) {
//		if (exhibit instanceof Recurse)
//			return id == ((Recurse) exhibit).id;
//		else
//			Internals.dumpStringExit("invalid class "
//					+ exhibit.getClass().getCanonicalName());
//		return false;
//	}

	@Override
	public void sanity() throws Exception {
	}

	@Override
	public MyObject getNew() throws Exception {
		return new Recurse(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Recurse;
	}

	@Override
	public boolean isRecursive() {
		return true;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName();
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new Recurse(sVars));
		return objs;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		return new MyObjects();
	}

}
