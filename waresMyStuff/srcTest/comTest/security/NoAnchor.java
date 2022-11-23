package comTest.security;

import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.forms.Utils;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class NoAnchor extends MyObject {
	public static final String NAME = "NoAnchor";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

	public NoAnchor(SessionVars sVars) throws Exception {
		super(sVars);
//		clear();
		try {
			setInstanceName(NAME+" "+Utils.getNextString());
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
//		if (exhibit instanceof NoAnchor)
//			return id == ((NoAnchor) exhibit).id;
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
		return new NoAnchor(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof NoAnchor;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName();
	}
	@Override
	public boolean hasAnchor() {
		return false;
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
