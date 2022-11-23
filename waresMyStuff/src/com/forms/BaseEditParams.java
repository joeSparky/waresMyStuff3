package com.forms;

import com.security.Everyone;
import com.security.ExceptionCoding;
import com.security.MyObject;

public class BaseEditParams {
	SmartForm callBack;
	SearchTargets objs;
	int myIndex;
	Everyone everyone;

	public BaseEditParams(SmartForm callBack, SearchTargets objs, int myIndex, Everyone everyone) {
		this.callBack = callBack;
		this.objs = objs;
		this.myIndex = myIndex;
		this.everyone = everyone;
	}

	public SmartForm getCallBack() {
		return callBack;
	}

	public SearchTargets getObjs() {
		return objs;
	}

	public int getMyIndex() {
		return myIndex;
	}

	public MyObject get(Class<?> t) throws ExceptionCoding {
		if (everyone.containsKey(t))
			return everyone.get(t);
		throw new ExceptionCoding("asked for class " + t.toString());
	}
}
