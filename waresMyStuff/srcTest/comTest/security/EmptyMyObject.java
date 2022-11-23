package comTest.security;

import com.db.SessionVars;
import com.security.MyObject;
import com.security.MyObjects;
import com.security.Table;

public class EmptyMyObject extends MyObject{

	public EmptyMyObject(SessionVars sVars) throws Exception {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMyFileName() {
		// TODO Auto-generated method stub
		return null;
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
	public boolean equalsObject(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MyObject getNew() {
		// TODO Auto-generated method stub
		return null;
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
