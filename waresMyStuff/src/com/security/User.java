package com.security;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.ResultSet;
import com.db.DoubleString;
import com.db.DoubleStrings;
import com.db.MyStatement;
import com.db.SessionVars;
import com.db.Strings;

public class User extends MyObject {
	/**
	 * 
	 */
	public static final String ALREADYINDB = "The user is already in the database.";
	public static final String NAME = "User";
	public static final String ANAME = "a User";
	public static final String NAMES = "Users";
	public static final int EMAILLENGTH = 40;
	public static final int PHONELENGTH = 15;
	public static final int USERNAMELENGTH = 20;
	public static final int PASSWORDLENGTH = 20;
	public static final int FIRSTNAMELENGTH = 25;
	public static final int LASTNAMELENGTH = 25;
	public static final int MINIMUMLENGTH = 3;
	public String firstName, lastName, email, phone, password;
//	SessionVars sVars = null;
	public static final String DEFAULTUSERWAREHOUSE = "dWarehouse";

//	public User() throws Exception {
//		clear();
//		new Table().tableCreated(this, sVars);
//	}
	
	SessionVars sVars = null;

	public User(SessionVars sVars) throws Exception {
		super(sVars);
		this.sVars = sVars;
//		this.sVars = sVars;
		clear();
		new Table().tableCreated(this, sVars);

	}

	private boolean defaultObj = false;

	public void clear() {
		firstName = lastName = email = phone = password = "";
		super.clear();
	}

	// the user table is built before the user constructor is executed. active has
	// not been initialized when the table is built.
//	private void checkActive() {
//		if (active == null)active = new ActiveClass();
//	}

	public void checkPasswordLength(String str) throws Exception {
		if (str.length() < MINIMUMLENGTH) {
			throw new Exception("The password must be " + MINIMUMLENGTH + " or more characters.");
		}
		if (str.length() > PASSWORDLENGTH) {
			throw new Exception("The password must be " + PASSWORDLENGTH + " or less characters.");
		}
	}

	private void checkNameLength() throws Exception {
		// enforce min and max length
		if (this.firstName.length() < MINIMUMLENGTH) {
			throw new Exception("The first name must be " + MINIMUMLENGTH + " or more characters.");
		}
		if (this.firstName.length() > FIRSTNAMELENGTH) {
			throw new Exception("The first name must be " + FIRSTNAMELENGTH + " or less characters.");
		}
		// enforce min and max length
		if (this.lastName.length() < MINIMUMLENGTH) {
			throw new Exception("The last name must be " + MINIMUMLENGTH + " or more characters.");
		}
		if (this.lastName.length() > LASTNAMELENGTH) {
			throw new Exception("The last name must be " + LASTNAMELENGTH + " or less characters.");
		}
	}

	/**
	 * using the name and password in the instance, login.
	 */
	public void logIn() throws Exception {
		checkPasswordLength(password);
		// check that the user name is in the database
		try {
			validUserName(this.getInstanceName());
		} catch (Exception e) {
			if (!e.getMessage().equals(ALREADYINDB)) {
				// throw all other exceptions back to the caller
				throw new Exception(e.getMessage());
			}
		}
		this.extractInfo(this.getInstanceName());
	}

	public void checkNamePassword() throws Exception {
		checkPasswordLength(password);
	}

	/**
	 * validate a user before entering the user into the system
	 * 
	 * @throws Exception
	 */
	public void sanity() throws Exception {
		checkPasswordLength(password);
		if (!isLoaded())
			validUserName(this.getInstanceName());
		if (!firstName.isEmpty() || !lastName.isEmpty())
			checkNameLength();
	}

	public void logIn(String name, String enteredPassword) throws Exception {
		this.setInstanceName(name);
		this.password = enteredPassword;
		this.logIn();
		if (!enteredPassword.equals(this.password)) {
			throw new Exception("The user name and/or password are not in the database.");
		}
	}

	@Override
	public DoubleStrings extendUpdate() {
		return extendAdd();
	}

	/*
	 * check for unique user name. return true if the name is not in the database
	 */
	private void validUserName(String name) throws Exception {
		Connection co = null;
		MyStatement st = null;
		ResultSet rs = null;
		String uniqueName = "SELECT count(name) FROM " + getMyFileName() + " WHERE name = '";
		uniqueName += name + "'";
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQuery(uniqueName);
			rs.next();
			if (rs.getInt(1) == 0) {
				return;
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		throw new Exception(ALREADYINDB);
	}

	public User find(int id) throws Exception {
		return (User) super.find(id);
	}

	/**
	 * find name in the database, copy the database info to myUser
	 * 
	 * @param name
	 */
	public void extractInfo(String name) throws Exception {
		Connection co = null;
		MyStatement st = null;
		String uniqueName = "SELECT * FROM " + getMyFileName() + " WHERE name = '";
		uniqueName += name + "'";
		ResultSet rs = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQuery(uniqueName);
			if (rs.next()) {
				extractInfo(rs);
				if (rs.next())
					throw new Exception(name + " in database more than once");
//				return;
			} else
				throw new Exception(name + " not found");
//		} catch (SQLException e) {
//			Internals.dumpException(e);
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		// see if the parent is set up
		getParents(new Company(sVars));
		return;
	}

	protected User extractInfo(ResultSet rs) throws Exception {
		email = rs.getString("email");
		password = rs.getString("password");
		firstName = rs.getString("firstName");
		lastName = rs.getString("lastName");
		phone = rs.getString("phone");
		defaultObj = rs.getBoolean("defaultObj");
		super.extractInfo(rs);
		return this;
	}

	@Override
	public Strings extendNewTable() {
		Strings ret = new Strings();
//		checkActive();
//		ret.addAll(active.extendNewTable());
		ret.add("`password` varchar(" + PASSWORDLENGTH + ") NOT NULL");
		ret.add("`firstName` varchar(" + FIRSTNAMELENGTH + ")");
		ret.add("`lastName` varchar(" + LASTNAMELENGTH + ")");
		ret.add("`email` varchar(" + EMAILLENGTH + ")");
		ret.add("`phone` varchar(" + PHONELENGTH + ")");
		// ret.add("`currentRoleId` int(11)");
		ret.add("`defaultObj` bool");
		return ret;
	}

	public static int getDefaultWarehouseId() {
		return defaultWarehouseId;
	}

	// private static int defaultUserId = 0;
	private static int defaultWarehouseId = 0;
	static int defaultUserId = 0;

	public boolean isDefaultUser() throws Exception {
		// if we don't know the default user id
		if (defaultUserId == 0) 
			defaultUserId = getDefaultUser().id;
		return defaultUserId == id;
	}

	public User getDefaultUser() throws Exception {
		User defaultUser = new User(sVars);

		// see if there's a default user in the database
		Connection co = null;
		MyStatement st = null;
		String uniqueName = "SELECT * FROM " + getMyFileName() + " WHERE defaultObj = '1'";
		ResultSet rs = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQuery(uniqueName);
			if (rs.next()) {
				defaultUser.extractInfo(rs);
				defaultUserId = defaultUser.id;
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
		return defaultUser;
	}

	/**
	 * Create the default user when a new table is created.
	 * 
	 * @throws Exception
	 */
	@Override
	public void newTableMonitor() throws Exception {
		User defaultUser = null;
//		XML xml = new XML();
//		Exception myException = null;
//		try {
		defaultUser = new User(sVars);
		defaultUser.setInstanceName(sVars.getDefaultUserName());
		defaultUser.password = sVars.getDefaultUserPassword();
		defaultUser.defaultObj = true;
		Company defaultCompany = new Company(sVars);
		defaultCompany.find(sVars.getDefaultCompanyName());
		defaultUser.add(defaultCompany.getAnchor());
//		} catch (Exception e) {
//			myException = e;
//		}
		defaultUserId = defaultUser.id;
	}

	@Override
	public DoubleStrings extendAdd() {
		DoubleStrings ret = new DoubleStrings();
//		checkActive();
//		ret.addAll(active.extendAdd());
		ret.add(new DoubleString("password", password));
		ret.add(new DoubleString("firstName", firstName));
		ret.add(new DoubleString("lastName", lastName));
		ret.add(new DoubleString("email", email));
		ret.add(new DoubleString("phone", phone));
		// ret.add(new DoubleString("currentRoleId", "" + currentRoleId));
		if (defaultObj)
			ret.add(new DoubleString("defaultObj", "1"));
		else
			ret.add(new DoubleString("defaultObj", "0"));
		return ret;
	}

	public String[] getTableNames() {
		return new String[] { getMyFileName() };
	}

	public void purge() throws Exception {
		Connection co = null;
		MyStatement st = null;
		// Exception myException = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			st.executeMyUpdate("DELETE FROM " + getMyFileName());
		} finally {
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}
	}

	public String oneLine() {
		String string = getInstanceName();
		if (!firstName.isEmpty() && !lastName.isEmpty()) {
			if (!string.isEmpty())
				string += ", ";
			string += firstName + " " + lastName;
		}
		return string;
	}

	@Override
	public String getAName() {
		return ANAME;
	}

	// @Override
	// public MyObjects getFullList() {
	// MyObjects ret = new MyObjects();
	// for (MyObject obj:listAll()){
	//
	// }
	// return listAll();
	// }

	@Override
	public int hashCodeReminder() {
		return 0;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equalsReminder(Object obj) {
		return false;
	}

	@Override
	public boolean equals(Object exhibit) {
		if (exhibit instanceof User) {
			return id == ((User) exhibit).id;
		}
		return false;
	}

	/**
	 * shared by the editors
	 */
	// User targetObject = null;

//	@Override
//	public BaseEditForm getEditor(BaseEditParams params) {
//		return new UserEditForm(params);
//
//	}

	@Override
	public MyObject getNew() throws Exception {
		return new User(sVars);
	}

	@Override
	public String getLogicalName() {
		return NAME;
	}

	// /**
	// * assign a role to a user
	// *
	// * @param role
	// * @throws Exception
	// */
	// public void assign(Role role) throws Exception {
	// currentRoleId = role.id;
	// sanity();
	// }

	@Override
	public boolean equalsObject(Object obj) {
		return obj instanceof User;
	}

	@Override
	public String getMyFileName() {
		return MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase();
	}

	@Override
	public MyObjects listParentsClasses() throws Exception {
		return new MyObjects();
	}

	@Override
	public MyObjects listChildrensClasses() throws Exception {
		return new MyObjects();
	}

	public User isValidUser(String name, String password) throws Exception {
		if (name == null)
			throw new Exception("null name.");
		if (password == null)
			throw new Exception("null password.");
		if (name.length() < Name.MINIMUMLENGTH)
			throw new Exception("User name '" + name + "' is less than " + Name.MINIMUMLENGTH + " characters.");
		if (name.length() > USERNAMELENGTH)
			throw new Exception("User name '" + name + "' is greater than " + USERNAMELENGTH + " characters.");
		if (password.length() < Name.MINIMUMLENGTH)
			throw new Exception("Password is less than " + Name.MINIMUMLENGTH + " characters.");
		if (password.length() > PASSWORDLENGTH)
			throw new Exception("Password is greater than " + PASSWORDLENGTH + " characters.");
		if (!findValidUser(name, password)) {
			throw new Exception("Username '" + name + "' and password are not in the database.");
		}
		return (User) find(name);
	}

	/**
	 * return true if the userName and password are in the datbase
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private boolean findValidUser(String userName, String password) throws Exception {
		Connection co = null;
		MyStatement st = null;
		String queryString = "SELECT count(name) FROM " + getMyFileName() + " WHERE name = '";
		queryString += userName + "' AND password='" + password + "'";
		ResultSet rs = null;
		try {
			co = sVars.connection.getConnection();
			st = new MyStatement(co);
			rs = st.executeQuery(queryString);
			rs.next();
			if (rs.getInt(1) == 1) {
				return true;
			}
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (co != null)
				co.close();
		}

		return false;
	}
}
