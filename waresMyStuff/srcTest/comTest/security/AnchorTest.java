package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.forms.Utils;
import com.security.Anchor;
import com.security.Company;
import com.security.MyObject;

import comTest.utilities.Utilities;

public class AnchorTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		new Utilities().allNewTables(sVars);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * test add(MyObject obj)
	 */
	@Test
	public void testAddAnchorAsObject() {
		Level1 levelOne = null;
		Level2 levelTwo = null;
		Level3 levelThree = null;

		try {
			levelOne = new Level1(sVars);
			levelTwo = new Level2(sVars);
			levelThree = new Level3(sVars);
			levelOne.add(new Anchor(sVars));
			levelTwo.add(levelOne);
			levelThree.add(levelTwo);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Anchor anchor = null;
		MyObject obj = null;
		try {
			anchor = levelOne.getAnchor();
			obj = anchor.getInstanceOfAnchor();

		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (!obj.equals(levelOne))
				fail("levelOne is not self-anchoring");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			anchor = levelTwo.getAnchor();
			obj = anchor.getInstanceOfAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!obj.equals(levelOne))
				fail("levelTwo not anchored by levelOne");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			anchor = levelThree.getAnchor();
			obj = anchor.getInstanceOfAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!obj.equals(levelTwo))
				fail("levelThree not anchored by levelTwo");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	// create a company with an anchor. see if the anchor matches the anchor of
	// the company
	@Test
	public void testAnchorMyObject() {
		Company company = Utilities.getACompany();
		Anchor anchor = null;
		try {
			anchor = company.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (!anchor.sameAnchor(company))
				fail("anchors not the same");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	// create company a with anchor b. create company c with anchor d.
	// verify that the anchor of a is not d.
	// verify that the anchor of c is not b.
	@Test
	public void testAnchorNotSame() {
		Company companyA = Utilities.getACompany();
		Company companyC = Utilities.getACompany();
		Anchor anchorB = null;
		Anchor anchorD = null;
		try {
			anchorB = companyA.getAnchor();
			anchorD = companyC.getAnchor();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (anchorB.sameAnchor(companyC))
				fail("anchors the same");
			if (anchorD.sameAnchor(companyA))
				fail("anchors the same");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testGetInstanceOfAnchor() {
		// create the first Level1 and the first anchor
		Level1 levelOne = null;
		try {
			levelOne = new Level1(sVars);
			levelOne.setInstanceName("anchorTest" + Utils.getNextString());
			levelOne.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (!levelOne.getAnchor().getInstanceOfAnchor().equals(levelOne))
				fail("expected levelOne, got "
						+ levelOne.getAnchor().getInstanceOfAnchor().toString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// create 3 more levelOnes with the same anchor
		for (int i = 0; i < 3; i++) {
			Level1 tmp = null;
			try {
				tmp = new Level1(sVars);
				tmp.setInstanceName("anchorTestLoop" + Utils.getNextString());
				tmp.add(levelOne.getAnchor());
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
		}

		// test the third anchor
		Level1 levelOneAgain = null;
		try {
			levelOneAgain = new Level1(sVars);
			levelOneAgain.setInstanceName("thirdAnchorTest"
					+ Utils.getNextString());
			levelOneAgain.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			if (!levelOneAgain.getAnchor().getInstanceOfAnchor()
					.equals(levelOneAgain))
				fail("expected levelOneAgain, got "
						+ levelOneAgain.getAnchor().getInstanceOfAnchor()
								.toString());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
	@Test
	public void testAdd() {
		try {
			Level1 l = new Level1(sVars);
			l.add(new Anchor(sVars));
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
