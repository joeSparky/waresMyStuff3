package comTest.security;

import java.lang.invoke.MethodHandles;

import com.db.SessionVars;
import com.security.MyObject;

public class RecursiveNot extends Recurse{
	public RecursiveNot(SessionVars sVars) throws Exception {
		super(sVars);
	}
	public static final String NAME = "recursiveNot";
	@Override
	public boolean isRecursive(){
		return false;
	}
	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName();
	}
//	public RecursiveNot(){
////		super();
//		rememberMe(this);
//	}
	@Override
	public MyObject getNew() throws Exception{
		return new RecursiveNot(sVars);
	}
}
