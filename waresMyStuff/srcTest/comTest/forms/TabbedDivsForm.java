package comTest.forms;

import static org.junit.Assert.fail;

import com.db.SessionVars;
import com.forms.FormsArray;
import com.forms.FormsMatrixDynamic;
import com.forms.SmartForm;
import com.forms.TabbedDiv;
import com.forms.TabbedDivs;
import com.forms.Utils;
import com.security.ExceptionCoding;

public class TabbedDivsForm extends SmartForm {

	protected TabbedDivsForm(FormsMatrixDynamic callBack) throws Exception {
		super(callBack, new SessionVars());
	}

	public TabbedDivsForm(SessionVars sVars) throws Exception {
		super(null, new SessionVars());
//		try {
//			resetForm(INSTATE.STANDARDFORM);
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
	}

	@Override
	public FormsArray getForm(SessionVars sVars) throws ExceptionCoding {
//		TabbedDivs tds = new TabbedDivs("RETURNTOME", sVars, this);
		TabbedDivs tds = new TabbedDivs(sVars, this);
		TabbedDiv td = new TabbedDiv("first tab not highlighted, not a link",
				false, false, "RETURNTOME");
		FormsArray f = new FormsArray();
		f.rawText("first tab form");
		try {
			td.addForm(f, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		td = new TabbedDiv("second tab highlighted, not a link", false, true,"RETURNTOME");
		FormsArray s = new FormsArray();
		s.rawText("second tab form");
		try {
			td.addForm(s, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		td = new TabbedDiv("third tab not highlighted, link", true, false,"RETURNTOME");
		FormsArray t = new FormsArray();
		t.rawText("third tab form");
		try {
			td.addForm(t, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		// start a new row of tabs
		tds.add(new TabbedDiv("new row of tabs"));

		// td = new TabbedDivs();
		td = new TabbedDiv("first tab, second row", false, false,"RETURNTOME");
		FormsArray fs = new FormsArray();
		fs.rawText("first tab on second row form");
		try {
			td.addForm(fs, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		td = new TabbedDiv("second tab, second row", false, false,"RETURNTOME");
		FormsArray ss = new FormsArray();
		ss.rawText("second tab form on second row");
		try {
			td.addForm(ss, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		td = new TabbedDiv("third tab, third row", false, false,"RETURNTOME");
		FormsArray tt = new FormsArray();
		tt.rawText("third tab on second row form");
		try {
			td.addForm(tt, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		FormsArray ret = new FormsArray();
		try {
			ret.addAll(tds.dumpDivsIntoForm());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			ret.setReturnTo(this, sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		return ret;
	}
	public static String RETURNTOME = Utils.getNextString();
	@Override
	public String getReturnTo() throws Exception {
		return RETURNTOME;
	}

}
