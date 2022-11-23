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
public class NoParents extends MyObject {

	public static final String NAME = MethodHandles.lookup().lookupClass().getSimpleName();

	public NoParents(SessionVars sVars) throws Exception {
		super(sVars);
		new Table().tableCreated(this, sVars);
	}

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
	public void sanity() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public MyObject getNew() throws Exception {
		return new NoParents(sVars);
	}

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof NoParents;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName();
	}	@Override
	public MyObjects listParentsClasses() throws Exception {
		return new MyObjects();
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		MyObjects objs = new MyObjects();
		objs.add(new OneParentChild(sVars));
		return objs;
	}

}
