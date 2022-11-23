package com.db;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.forms.FormsArray;
import com.forms.SmartForm;
import com.security.ExceptionCoding;

public class SessionVars {
	public HashMap<String, String[]> parameterMap;
//	public HttpSession session;
	public HttpServletRequest request;
	public HttpServletResponse response;
	public int userNumber, accessNumber, warehouseNumber;
	public XML xml = null;
	public MyConnection connection = null;
//	, companyNumber;
	/**
	 * login application
	 */
//	public static SmartForm login;
	/**
	 * dispatch application
	 */
//	public static SmartForm dispatch;

	public ServletContext context;

	public String buttonName = null;
	
	/**
	 * while running in the tomcat container
	 * @param context
	 * @throws Exception
	 */
	public SessionVars (ServletContext context) throws Exception {
		this.context = context;
		clear();
	}

	/**
	 * during testing (ServletContext context == null)
	 * @throws Exception
	 */
	public SessionVars() throws Exception {
		clear();
	}

	public void clear() throws Exception {
		synchronized (this) {
			if (xml == null)
				xml = new XML(this);
		}
		synchronized (this) {
			if (connection == null) {
				connection = new MyConnection(this, xml.getDefaultDbName());
			}
		}

		parameterMap = new HashMap<String, String[]>();
//		session = null;
		// loginStr = null;
//		dispatch = null;
		userNumber = accessNumber = warehouseNumber = 0;
//		contextCopy = null;
		buttonName = null;
//		if (xml == null)
//			xml = new XML(this);
	}

	public boolean isLoggedIn() {
		return userNumber>0;
	}
	
	/**
	 * mark the user as logged out
	 */
	public void logout() {
		userNumber=0;
	}

//	public void logIn(User user) throws Exception {
//		userNumber = user.id;
//		// no default company for the default user. (The default user has to
//		// make the companies.)
//		Company company = null;
//		if (!user.isDefaultUser()) {
//			if (user.getInstanceOfAnchor() instanceof Company) {
//				company = (Company) user.getInstanceOfAnchor();
//			} else {
//				MyObject myObject = user.getInstanceOfAnchor();
//				company = (Company) myObject.getInstanceOfAnchor();
//			}
////			companyNumber = company.id;
//		}
//	}

	public void clearLogin() {
		userNumber = 0;
//		companyNumber = 0;
	}

	public int getUserNumber() {
		return userNumber;
	}

	public int getAccessNumber() {
		return accessNumber;
	}

	public int getWarehouseNumber() {
		return warehouseNumber;
	}

	public void setAccessNumber(int accessNumber) {
		this.accessNumber = accessNumber;
	}

	public void setWarehouseNumber(int warehouseNumber) {
		this.warehouseNumber = warehouseNumber;
	}

	public String stripped(String stripThis) {
		stripThis = stripThis.replace("'", "");
		stripThis = stripThis.replace("\"", "");
		stripThis = stripThis.replace("<", "");
		stripThis = stripThis.replace(">", "");
		return stripThis;
	}

	private String[] strippedArray(String[] stripThis) {
		for (int i = 0; i < stripThis.length; i++) {
			stripThis[i] = stripped(stripThis[i]);
		}
		return stripThis;
	}

	/**
	 * strip the input parameters
	 */
	public void extractInputParams(HttpServletRequest request) {
//		@SuppressWarnings("unchecked")
		Map<String, String[]> tmp =
//(HashMap<String, String[]>)
				request.getParameterMap();
		Set<String> keys = tmp.keySet();
		// replace the dirty keys with clean keys
		parameterMap = new HashMap<String, String[]>();
		for (String thisKey : keys) {
			parameterMap.put(stripped(thisKey), strippedArray(tmp.get(thisKey)));
		}
	}

	public HashMap<String, String[]> getParameterMap() {
		return parameterMap;
	}

	/**
	 * clear the parameter map so that there will not be any more values processed
	 */
	public void clearParameterMap() {
		parameterMap.clear();
	}

	public String[] getParameterValues(String str) {
		return parameterMap.get(str);
	}

	/**
	 * return the value of the parameter or a null if the parameter was not set
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String getParameterValue(String str) throws Exception {
		if (parameterMap.containsKey(str))
			return parameterMap.get(str)[0];
		throw new Exception("String " + str + " is not in parameterMap " + parameterMap.toString());

	}

	public boolean hasParameterKey(String str) {
		return parameterMap.keySet().contains(str);
	}

	public String getParamterValueException(String str) throws Exception {
		if (hasParameterKey(str))
			return getParameterValue(str);
		else
			throw new Exception(str + " not found.");

	}

	/**
	 * if a key name and key value are in the input parameters
	 * 
	 * @param keyName
	 * @param keyValue
	 * @return
	 */
	public boolean keyNameValue(String keyName, String keyValue) {
		if (parameterMap.containsKey(keyName) && parameterMap.get(keyName)[0].equals(keyValue))
			return true;
		return false;
	}

	public Set<String> getParameterKeys() {
		return parameterMap.keySet();
	}

	private Map<String, SmartForm> apToRun = new HashMap<String, SmartForm>();

	public void setApToRun(String key, SmartForm form) {
//		if (apToRun == null)
//			apToRun = new HashMap<String, SmartForm>();
		// discard link even if current
		// apToRun.remove(key);
		apToRun.put(key, form);
		// if (apToRun.size()>1){
		// for (String str : apToRun.keySet()) {
		// System.out.println("key:" + str + " app:" + apToRun.get(str));
		// }
		// throw new ExceptionCoding("apToRun bigger than 1");
		// }
		// System.out.println();
	}

	/**
	 * return which instance should run for this form
	 * 
	 * @param key
	 * @return
	 * @throws ExceptionCoding
	 */
	public SmartForm getApToRun() {
		if (parameterMap.containsKey(FormsArray.RETURNTO)) {
			String returnID = parameterMap.get(FormsArray.RETURNTO)[0];
			if (apToRun.containsKey(returnID))
				return apToRun.get(returnID);
		}
		return null;
	}

	public boolean hasApToRun(String key) {
		if (apToRun == null) {
			apToRun = new HashMap<String, SmartForm>();
			return false;
		}
		return apToRun.containsKey(key);
	}

	public LocalDateTime created = LocalDateTime.now();
	/**
	 * detect multiple inputs on a session before the container can respond
	 */
	public int threadCount = 0;

//	public FormsArray ret = null;
	public SmartForm getLoginForm() throws Exception {
//		openXML();
		return xml.getLogin(this);
	}

//	void openXML() throws Exception {
//		if (xml == null)
//			xml = new XML(this);
//	}
	public SmartForm getDispatch() throws Exception {
//		openXML();
		return xml.getDispatch(this);
	}

	public String getSeparator() throws Exception {
//		openXML();
		return xml.getSeparator(this);
	}

	public String getCSVPATH() throws Exception {
//		openXML();
		return xml.getCSVPath();
	}

	public String getDefaultCompanyName() throws Exception {
//		openXML();
		return xml.getDefaultCompanyName();
	}

	public String getDefaultUserName() throws Exception {
//		openXML();
		return xml.getDefaultUserName();
	}

	public String getDefaultUserPassword() throws Exception {
//		openXML();
		return xml.getDefaultUserPassword();
	}
}
