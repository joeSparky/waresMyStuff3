package com.partsTest.exhibit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.parts.inOut.Attributes;
import com.parts.inOut.Part;
import com.partsTest.Utilities.Utilities;
import com.security.Company;
import com.security.MyObjectsArray;

public class MemberTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		if (sVars==null)sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	// add a member with minimal information and then try to find it.
	public void testFindInt() {
		Part member = null;
		Company company = Utilities.getACompany();
		try {member = new Part(sVars);
			member.setInstanceName("new member name");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			member.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			member.add(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		Part findMember = null;
		try {findMember = new Part(sVars);
			findMember.find(member.id);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	// set up a minimal member and then try to delete it
	public void testDeleteMember() {
		Part member = null;
		Company company = Utilities.getACompany();
		try {member = new Part(sVars);
			member.setInstanceName("new member name");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			member.sanity();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			member.add(company.getAnchor());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		MyObjectsArray myObjs = new MyObjectsArray();
		try {
			myObjs.add(new Attributes(sVars));
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}
		myObjs.add(member);
		try {
			member.deleteTest();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		try {
			member.deleteUnconditionally();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

}
