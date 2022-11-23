package comTest.security;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.MyObject;

public class RecursiveTwo extends Recurse {
	public RecursiveTwo(SessionVars sVars) throws Exception {
		super(sVars);
	}
	public static final String NAME = "recursiveTwo";
	
	@Override
	public MyObject getNew() throws Exception {
		return new RecursiveTwo(sVars);
	}
	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName();
	}
}
