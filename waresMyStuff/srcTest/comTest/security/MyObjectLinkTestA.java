package comTest.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.security.Anchor;

import comTest.utilities.Utilities;

public class MyObjectLinkTestA {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
		new POCWithOnlyOneParent(sVars).newTable(sVars);
		new ChildWithOnlyOneParent(sVars).newTable(sVars);
//		MyLinkInternals.addInternal(new ParentOfChildWithOnlyOneParent(),
//				new ChildWithOnlyOneParent(),
//				"fer shur",
//				MyLinkInternals.LINKTYPE.EXTERNAL, MyLinkInternals.DELETETYPE.KEEPDESCENDANTS);
	}

	@After
	public void tearDown() throws Exception {
	}

	// update a parent / child link that uses parentId
	@Test
	public void testUpdate() {
		Anchor anchor = Utilities.getAnAnchor();
		POCWithOnlyOneParent p = null;
		try {
			p = new POCWithOnlyOneParent(sVars);
			p.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		ChildWithOnlyOneParent c = null;
		try {
			c = new ChildWithOnlyOneParent(sVars);
			c.add(anchor);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
//		MyLinkObject mlo = new MyLinkObject(p, c);
		try {
			p.addChild(c);
//			mlo.add(1);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
