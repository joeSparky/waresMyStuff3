package comTest.forms;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.forms.FormsArray;

public class FormsArrayTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTextBox() {
		FormsArray ret = new FormsArray();
		try {
			ret.textBox("internal textbox name", 55, "external textbox label", "my textbox value", false, false);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		System.out.println("body: " + ret.body);
	}

	@Test
	public void testSubmitClickWithId() {
		FormsArray ret = new FormsArray();
		ret.submitClickWithId("idToWatch", "myFavoriteKey", "myFavoriteValue");
		System.out.println("body: " + ret.body);
	}

//	@Test
//	public void testTextBoxWithClick() {
//		FormsArray ret = new FormsArray();
//		ret.textBox("myFavoriteInternalName", 33, "myFavoriteExternalLabel", "myFavoriteValue", false, false);
//		fail(ret.body);
//	}
}
