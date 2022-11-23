package comTest.security.myLinkObject;

import com.db.SessionVars;
import com.security.MyObjects;

import comTest.security.Level2;

public class Child extends Level2{

	public Child(SessionVars sVars) throws Exception {
		super(sVars);
	}
	@Override
	public MyObjects listParentsClasses() throws Exception {
		MyObjects ret = new MyObjects();
		ret.add(new Parent(sVars));
		return ret;
	}
}
