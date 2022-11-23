package comTest.db;

import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;

import junit.framework.TestCase;

public class TestXML extends TestCase {

	static final String LOGINFORMPATH = "comTest.sessionVars.LoginForm";
	static final String DISPATCHFORM = "comTest.sessionVars.DispatchForm";

	@Test
	public void testGetLoginName() {
		SessionVars sVars = null;
		try {
			sVars = new SessionVars();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		String tmp = null;

		try {
			tmp = sVars.xml.readXML(XML.LOGINPARAMNAME);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!tmp.equals(LOGINFORMPATH))
			fail("got '" + tmp + "', not '" + LOGINFORMPATH + "' in " + XML.XMLFILENAME);

		try {
			tmp = sVars.xml.readXML(XML.DISPATCHPARAMNAME);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (!tmp.equals(DISPATCHFORM))
			fail("got '" + tmp + "' not '" + DISPATCHFORM + "' in " + XML.XMLFILENAME);

		try {
			sVars.xml.getLogin(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
