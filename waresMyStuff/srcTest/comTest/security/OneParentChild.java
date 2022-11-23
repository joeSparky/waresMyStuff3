package comTest.security;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

/**
 * an MyObjectInstance that can have only one parent (the ID of the parent is in
 * the instance)
 * 
 * @author Joe
 *
 */
public class OneParentChild extends MyObject {

	protected OneParentChild(SessionVars sVars) throws Exception {
		super(sVars);
		new Table().tableCreated(this, sVars);
	}

	public static final String NAME = MethodHandles.lookup().lookupClass()
			.getSimpleName();

	

	@Override
	public String getAName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogicalName() {
		return NAME;
	}

	@Override
	public int hashCodeReminder() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equalsReminder(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MyObject getNew() throws Exception {
		return new OneParentChild(sVars);
	}

	@Override
	public void sanity() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof OneParentChild;
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
