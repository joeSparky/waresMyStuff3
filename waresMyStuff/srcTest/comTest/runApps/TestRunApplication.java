package comTest.runApps;

import java.lang.reflect.InvocationTargetException;

import com.db.SessionVars;
import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;
import com.runApps.RunApplication;

import junit.framework.TestCase;

public class TestRunApplication extends TestCase
//implements StartOverCallback 
{

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
//		Internals.setupCallback(null);
		super.tearDown();
	}

	public SessionVars sVars;

	public void testDispatchThis() {
		
		// start a form from a class name
		SmartForm sf = null;
		try {
			new SmartFormInstance(new FormsMatrixDynamic(sVars), sVars);
			sf = new RunApplication().dispatchThis(SmartFormInstance.class.getCanonicalName(), new FormsMatrixDynamic(sVars),
					sVars);
		} catch (NoSuchMethodException e1) {
			fail(e1.getLocalizedMessage());
		} catch (SecurityException e1) {
			fail(e1.getLocalizedMessage());
		} catch (InstantiationException e1) {
			fail(e1.getLocalizedMessage());
		} catch (IllegalAccessException e1) {
			fail(e1.getLocalizedMessage());
		} catch (IllegalArgumentException e1) {
			fail(e1.getLocalizedMessage());
		} catch (InvocationTargetException e1) {
			fail(e1.getLocalizedMessage());
		} catch (ClassNotFoundException e) {
			fail(e.getLocalizedMessage());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			sf.getForm(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// try to start something that does not exist
//		prepCallback();
		boolean foundFailure = false;
		try {
			sf = new RunApplication().dispatchThis("junk", sVars);
			fail("did not catch non-existent class");
		} catch (NoSuchMethodException e) {
			fail(e.getLocalizedMessage());
		} catch (SecurityException e) {
			fail(e.getLocalizedMessage());
		} catch (InstantiationException e) {
			fail(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			fail(e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			fail(e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			fail(e.getLocalizedMessage());
		} catch (ClassNotFoundException e) {
			foundFailure = true;
		}
		if (!foundFailure)
			fail("did not catch ClassNotFoundException");
	}
}

//	public void testMyObjectNoSvars() {
//
//		// try to start something that is does not have a SessionVars argument
//		// constructor
////		prepCallback();
//
//		// working test
//		try {
//			new RunApplication().dispatchThis(comTest.security.EmptyMyObject.class.getCanonicalName());
//			fail("good RunApplication threw an exception");
//		} catch (NoSuchMethodException e) {
//			fail(e.getLocalizedMessage());
//		} catch (SecurityException e) {
//			fail(e.getLocalizedMessage());
//		} catch (InstantiationException e) {
//			fail(e.getLocalizedMessage());
//		} catch (IllegalAccessException e) {
//			fail(e.getLocalizedMessage());
//		} catch (IllegalArgumentException e) {
//			fail(e.getLocalizedMessage());
//		} catch (InvocationTargetException e) {
//			fail(e.getLocalizedMessage());
//		} catch (ClassNotFoundException e) {
//			fail(e.getLocalizedMessage());
//		}
////		if (exceptionThrown)
//
////		prepCallback();
//		// class does not exist test
//		boolean exceptionThrown = false;
//		try {
//			new RunApplication().dispatchThis("junker");
//		} catch (NoSuchMethodException e) {
//			fail(e.getLocalizedMessage());
//		} catch (SecurityException e) {
//			fail(e.getLocalizedMessage());
//		} catch (InstantiationException e) {
//			fail(e.getLocalizedMessage());
//		} catch (IllegalAccessException e) {
//			fail(e.getLocalizedMessage());
//		} catch (IllegalArgumentException e) {
//			fail(e.getLocalizedMessage());
//		} catch (InvocationTargetException e) {
//			fail(e.getLocalizedMessage());
//		} catch (ClassNotFoundException e) {
//			exceptionThrown = true;
//		}
//		if (!exceptionThrown)
//			fail("did not catch ClassNotFoundException");
//	}
//
////	boolean exceptionThrown = false;
////
////	@Override
////	public void callBack() {
////		exceptionThrown = true;
////	}
////
////	private void prepCallback() {
////		exceptionThrown = false;
////		Internals.setupCallback(this);
////	}
//}
