package com.security;

import java.util.HashSet;

public class Users extends HashSet<User>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6076518475356732170L;
	public Users justOne(User exhibit) {
		Users ret = new Users();
		ret.add(exhibit);
		return ret;
	}
//	/**
//	 * return one of the users in the list
//	 * @return
//	 * @throws Exception 
//	 */
//	public User getOne() throws Exception{
//		if (this.isEmpty()) return new User(sVars);
//		Iterator<User> itr = this.iterator();
//		return itr.next();
//	}
}
