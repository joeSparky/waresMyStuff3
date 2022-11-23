package com.runApps;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;
import com.security.MyObject;

/**
 * connect the displayed text with the url of the jsp to handle it
 * 
 * @author joe
 * 
 */
public class RunApplication {

	public SmartForm dispatchThis(String className, SessionVars sVars)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ClassNotFoundException {

		Class<?> runClass = null;
//		try {
		runClass = this.getClass().getClassLoader().loadClass(className);
//		} catch (ClassNotFoundException e) {
//			Internals.dumpExceptionExit(e);
//			// during testing, dumpExceptionExit returns
//			return null;
//		}
		SmartForm ret = null;

		// constructor with sVars argument
		Constructor<?> constructor = runClass.getDeclaredConstructor(SessionVars.class);
		ret = (SmartForm) constructor.newInstance(sVars);
		return ret;
	}

	public SmartForm dispatchThis(String className, FormsMatrixDynamic fmd, SessionVars sVars)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ClassNotFoundException {

		Class<?> runClass = null;
		runClass = this.getClass().getClassLoader().loadClass(className);
		SmartForm ret = null;

// constructor with sVars argument
		Constructor<?> constructor = runClass.getDeclaredConstructor(FormsMatrixDynamic.class, SessionVars.class);
		ret = (SmartForm) constructor.newInstance(fmd, sVars);
		return ret;
	}

	private static HashMap<String, Integer> goToHash = new HashMap<String, Integer>();

	public static String getMyGoTo(SmartForm thisClass) {
		String className = thisClass.getClass().getName();
		if (goToHash.containsKey(className))
			return "a" + goToHash.get(className);
		goToHash.put(className, goToHash.size());
		return "a" + (goToHash.size() - 1);
	}
	
	public MyObject getOneOfThese(String className, SessionVars sVars)
			throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, ClassNotFoundException {

		Class<?> runClass = null;
//		try {
		runClass = this.getClass().getClassLoader().loadClass(className);
//		} catch (ClassNotFoundException e) {
//			Internals.dumpExceptionExit(e);
//			// during testing, dumpExceptionExit returns
//			return null;
//		}
		MyObject ret = null;

		// constructor with sVars argument
		Constructor<?> constructor = runClass.getDeclaredConstructor(SessionVars.class);
		ret = (MyObject) constructor.newInstance(sVars);
		return ret;
	}

//	public MyObject dispatchThis(String className) throws NoSuchMethodException, SecurityException,
//			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
//
//		Class<?> runClass = null;
////		try {
//			runClass = this.getClass().getClassLoader().loadClass(className);
////		} catch (ClassNotFoundException e) {
////			Internals.dumpExceptionExit(e);
////			// during testing, dumpExceptionExit returns
////			return null;
////		}
//		MyObject ret = null;
//		Constructor<?> constructor = runClass.getDeclaredConstructor();
//		ret = (MyObject) constructor.newInstance();
//		return ret;
//	}

}
