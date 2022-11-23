package com.forms;

import com.security.MyObject;

public class IdAndString {
	public String string;
	public int id;
	public IdAndString(MyObject o){
		id = o.id;
		string = o.getInstanceName();
	}
	public IdAndString(){		
	}
	public static IdAndString toIdAndString(MyObject obj){
		IdAndString tmp = new IdAndString();
		tmp.id = obj.id;
		tmp.string = obj.getInstanceName();
		return tmp;
	}
	public IdAndString(MyObject o, MyObject parent){
		id = o.id;
		string = o.getInstanceName(parent);
	}
	public boolean equals(Object exhibit) {
		if (exhibit instanceof IdAndString) {
			return ((IdAndString) exhibit).id == id && ((IdAndString) exhibit).string.equals(string);
		}
		return false;
	}
}
