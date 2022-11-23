package com.security;

import java.util.HashSet;

/**
 * Set of roles for accessing the warehouse such as picker, administrator, or
 * inventory manager. Each role has a set of permissions, such as
 * pickingWithoutOrders, administor, runningReports
 * 
 * @author Joe
 *
 */
public class Roles extends HashSet<Role> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
