package comTest.forms;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.FormsArray;
import com.forms.TabbedDiv;
import com.forms.TabbedDivs;

public class TabbedDivsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStartDiv() {
		TabbedDivs tds=null;
		try {
			tds = new TabbedDivs(new SessionVars(), new TabbedDivsForm(new SessionVars()));
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
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

		td = new TabbedDiv("second tab highlighted, not a link", false, true, "RETURNTOME");
		FormsArray s = new FormsArray();
		s.rawText("second tab form");
		try {
			td.addForm(s, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		td = new TabbedDiv("third tab not highlighted, link", true, false, "RETURNTOME");
		FormsArray t = new FormsArray();
		t.rawText("third tab form");
		try {
			td.addForm(t, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		// start a new row of tabs
		tds.add(new TabbedDiv("RETURNTOME"));

		// td = new TabbedDivs();
		td = new TabbedDiv("first tab, second row", false, false, "RETURNTOME");
		FormsArray fs = new FormsArray();
		fs.rawText("first tab second row form");
		try {
			td.addForm(fs, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		td = new TabbedDiv("second tab, second row", false, false, "RETURNTOME");
		FormsArray ss = new FormsArray();
		ss.rawText("second tab form");
		try {
			td.addForm(ss, "HIDDENSTRINGNAME", "HIDDENSTRINGVALUE");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		tds.add(td);

		td = new TabbedDiv("third tab, third row", false, false, "RETURNTOME");
		FormsArray tt = new FormsArray();
		tt.rawText("third tab form");
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
		System.out.println(ret.body);
	}
}
