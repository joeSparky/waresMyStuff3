package comTest.security.myLinkObject;

import com.db.SessionVars;
import com.security.MyObjects;

import comTest.security.Level1;

public class Parent extends Level1{

	public Parent(SessionVars sVars) throws Exception {
		super(sVars);
	}
	@Override
	public MyObjects listChildrensClasses() throws Exception {
		MyObjects ret = new MyObjects();
		ret.add(new Child(sVars));
		return ret;
	}

}
