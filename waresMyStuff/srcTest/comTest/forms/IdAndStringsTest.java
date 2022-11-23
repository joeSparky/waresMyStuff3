package comTest.forms;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.EndOfInputException;
import com.forms.EndOfInputRedoQueries;
import com.forms.FormsMatrixDynamic;
import com.forms.IdAndStrings;
import com.forms.SearchTarget;
import com.forms.SmartForm;

import comTest.utilities.Utilities;

public class IdAndStringsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	class DummyForm extends SmartForm {

		protected DummyForm(FormsMatrixDynamic callBack) throws Exception {
			super(callBack, new SessionVars());
		}
	}

//	boolean redoQuery = false;
	boolean calledBack = false;

	@Test
	public void testNextButton() {
		SessionVars sVars = null;
		try {
			sVars = new SessionVars();
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		new Utilities().allNewTables(sVars);
		FormsMatrixDynamic fmd = Utilities.getFormsMatrixDynamic();
		// simulate a SelectFromList button pressed
		String[] tmp = { "", "" };
		IdAndStrings idstr = new IdAndStrings(fmd, SearchTarget.SEARCHTYPES.DESCENDANTS, sVars);
		sVars.parameterMap.put(idstr.REQUESTALIST, tmp);
		boolean redoQueries = false;
		calledBack = false;
		try {
			idstr.extractParams(sVars);
		} catch (EndOfInputException e) {
		} catch (EndOfInputRedoQueries e) {
			redoQueries = true;
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (calledBack)
			fail("got called back");
		if (!redoQueries)
			fail("did not get EndOfInputRedoQueries");

	}
}
