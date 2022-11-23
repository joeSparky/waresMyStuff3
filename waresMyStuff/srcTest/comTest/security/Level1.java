package comTest.security;

import static org.junit.Assert.fail;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.forms.IdAndString;
import com.security.Anchor;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

import comTest.utilities.Utilities;

public class Level1 extends MyObject {
	public static final String NAME = "Level1";
	public static final String ANAME = "a " + NAME;
	public static final String NAMES = NAME + "s";

	public Level1(SessionVars sVars) throws Exception {
		super(sVars);
		try {
			setInstanceName(NAME + " " + Utilities.getPaddedNextString(6));
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

	@Override
	public boolean equals(Object exhibit) {
		if (exhibit instanceof Level1 && id == ((Level1) exhibit).id)
			return true;
		if (exhibit instanceof IdAndString) {
			return ((IdAndString) exhibit).id == id && ((IdAndString) exhibit).string.equals(this.getInstanceName());
		}
		return false;
	}

	@Override
	public void sanity() throws Exception {
	}

	@Override
	public MyObject getNew() throws Exception {
		return new Level1(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof Level1;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName();
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects ret = new MyObjects();
//		ret.add(new Level1(sVars));
		return ret;
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		return new MyObjects();
	}

	@Override
	public Level1 add(Anchor anchor) throws Exception {
		return (Level1) super.add(anchor);
	}

}
