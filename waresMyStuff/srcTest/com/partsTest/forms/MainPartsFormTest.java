package com.partsTest.forms;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.MyConnection;
import com.db.MyStatement;
import com.db.SessionVars;
import com.db.XML;
import com.forms.EndOfInputRedoQueries;
import com.forms.FormsMatrixDynamic;
import com.forms.IdAndStrings;
import com.forms.SearchTarget;
import com.parts.forms.MainPartsForm;
import com.parts.inOut.Part;
import com.security.Company;
import com.security.MyLinkObject;
import com.security.MyObject;
import com.security.User;

import comTest.utilities.Utilities;

public class MainPartsFormTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		if (sVars==null)sVars = new SessionVars();
//		
		new Utilities().allNewTables(sVars);
		Company company = Utilities.getACompany();
		User user = new Utilities().getAUser(company.getAnchor());
		new MyLinkObject(company, user, sVars).add();
		sVars.userNumber = user.id;
	}

	

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetFormSessionVars() {
		try {
			new MainPartsForm(sVars).getForm(sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testMainPartsFormSessionVars() {
		try {
			new MainPartsForm(sVars);
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace())
				System.out.println(ste.toString());
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testFormsMatrixDynamicByRowColumn() {
		Company company = null;
//		XML xml = null;
		try {
//			xml = new XML(new SessionVars());
//			xml = new XML(new SessionVars(), "cdm2");
			company = new Company(sVars);

			company.find(sVars.xml.getDefaultCompanyName());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		MainPartsForm mpf = null;
		FormsMatrixDynamic fmd = null;
		try {
			mpf = new MainPartsForm(sVars);
			fmd = mpf.fmd;
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}

		fmd.get(0).get(0).obj = company;

		// extract the forms matrix
//		FormsMatrix formsMatrix = fmd.fm;

		// spin through the rows
		for (fmd.row = 0; fmd.row < fmd.getNumberOfRows(); fmd.row++) {
			for (fmd.column = 0; fmd.column < fmd.get(fmd.row).size(); fmd.column++) {
				MyObject obj = fmd.getObject();
				System.out.println("row:" + fmd.row + " column:" + fmd.column + " obj:" + obj.toString() + " name:"
						+ obj.getInstanceName());
			}
		}

//		IdAndStrings idAndStrings = null;
		for (fmd.row = 0; fmd.row < fmd.getNumberOfRows(); fmd.row++) {
			// ignore the company in the first column
			for (fmd.column = 1; fmd.column < fmd.get(fmd.row).size(); fmd.column++) {
				for (SearchTarget.SEARCHTYPES type : SearchTarget.SEARCHTYPES.values()) {
					try {
						System.out.println("row:" + fmd.row + " column:" + fmd.column + " type:" + type.toString());
						System.out.println("forward " + fmd.get(fmd.row).get(fmd.column).getQuery(type, 0));
						System.out.println("reverse " + fmd.get(fmd.row).get(fmd.column).getQuery(type, 0));
						System.out.println();

						IdAndStrings ids = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type);
						if (ids.size() > 0)
//							System.out.print("row:" + fmd.row + " column:" + fmd.column + " type:" + type.toString());
							System.out.println(" size:" + ids.size());
						else
							System.out.println();

//						idAndStrings = new IdAndStrings(fmd, type).doQuery();
					} catch (Exception e) {
						for (StackTraceElement ele : e.getStackTrace()) {
							System.out.println(ele);
						}
//					System.out.print(e.getStackTrace());
						fail(e.getLocalizedMessage());
					}
				}
			}
		}
	}

	@Test
	/**
	 * verify all IdAndStrings.displayState are initialized
	 */
	public void testMainPartsFormStates() {
//		MainPartsForm mpf = null;
//		String currentDbName = null;
		try {
//			currentDbName = sVars.xml.getDefaultDbName();
//				Utilities.getDbName();
			sVars.connection.createBasicDataSource("cdm2");

			SessionVars sVars = new SessionVars();
			FormsMatrixDynamic fmd = null;

			// set the default user id in sVars
			sVars.userNumber = new User(sVars).getDefaultUser().id;

			fmd = new MainPartsForm(sVars).fmd;

			for (fmd.row = 0; fmd.row < fmd.getNumberOfRows(); fmd.row++) {
				// ignore the company in the first column
				for (fmd.column = 1; fmd.column < fmd.get(fmd.row).size(); fmd.column++) {
					for (SearchTarget.SEARCHTYPES type : SearchTarget.SEARCHTYPES.values()) {
						if (!fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).giveMeAListButton)
							fail("row:" + fmd.row + " column:" + fmd.column + " type:" + type.toString()
									+ " giveMeAListButton is false");
					}
				}
			}
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace())
				System.out.println(ste);
			fail(e.getLocalizedMessage());
		}
		try {
			sVars.connection.createBasicDataSource(MyConnection.XMLDBNAME);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

	}

//	@Test
//	/**
//	 * verify all IdAndStrings.displayState are independent
//	 */
//	public void testMainPartsFormWalkingStates() {
////		MainPartsForm mpf = null;
//		String currentDbName = Utilities.getDbName();
//		Utilities.changeDatabase("cdm2");
//
//		SessionVars sVars = new SessionVars();
//		FormsMatrixDynamic fmd = null;
//		try {
//
//			// set the default user id in sVars
//			sVars.userNumber = new User(sVars).getDefaultUser().id;
//
//			fmd = new MainPartsForm(sVars).fmd;
//
//			for (fmd.row = 0; fmd.row < fmd.getNumberOfRows(); fmd.row++) {
//				// ignore the company in the first column
//				for (fmd.column = 1; fmd.column < fmd.get(fmd.row).size(); fmd.column++) {
//					for (SearchTarget.SEARCHTYPES type : SearchTarget.SEARCHTYPES.values()) {
//						if (!fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).giveMeAListButton)
//							fail("row:" + fmd.row + " column:" + fmd.column + " type:" + type.toString()
//									+ " giveMeAListButton is false");
//
//						// simulate the user requested a list of IdAndStrings for this SEARCHTYPE
//						String buttonName = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).REQUESTALIST;
//						// clear all previous buttons we've inserted
//						sVars.parameterMap.clear();
//						sVars.parameterMap.put(buttonName, null);
//						boolean endOfInput = false;
//						try {
//							fmd.get(fmd.row).get(fmd.column).extractParams(sVars);
//						} catch (EndOfInputRedoQueries e) {
//							endOfInput = true;
//						}
//						if (!endOfInput)
//							fail("extractParams failed");
//
//						// check the state of the previous, current, and next IdAndStrings
//						// expected state of previous IdAndStrings
////						IdAndStrings.DISPLAYSTATE expectedState = IdAndStrings.DISPLAYSTATE.SHOWNEXTBUTTON;
//						for (int myRow = 0; myRow < fmd.getNumberOfRows(); myRow++) {
//							// ignore the company in the first column
//							for (int myColumn = 1; myColumn < fmd.get(myRow).size(); myColumn++) {
//								for (SearchTarget.SEARCHTYPES myType : SearchTarget.SEARCHTYPES.values()) {
//									if (!fmd.get(myRow).get(myColumn).getIdAndStrings(myType).displayState
//											.equals(expectedState))
//										fail("myRow:" + myRow + " myColumn:" + myColumn + " myType:" + myType
//												+ " displayState:"
//												+ fmd.get(myRow).get(myColumn).getIdAndStrings(myType).displayState
//												+ " expected:" + expectedState);
//
//									// if the previous IdAndStrings have been checked
//									if (myRow == fmd.row && myColumn == fmd.column && myType.equals(type))
//										// go back to the previous state as the remainder of the displayStates should
//										// not have changed
//										expectedState = IdAndStrings.DISPLAYSTATE.SHOWGIVEMEALISTBUTTON;
//								}
//							}
//
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			for (StackTraceElement ste : e.getStackTrace())
//				System.out.println(ste);
//			fail(e.getLocalizedMessage());
//		} finally {
//			// change the connection database back
//			Utilities.changeDatabase(currentDbName);
//		}
//
//	}

	@Test
	/**
	 * exercise all queries
	 */
	public void testAllQueries() {
//		String currentDbName =null;
		try {
//			currentDbName = sVars.xml.getDefaultDbName();
//		Utilities.changeDatabase(sVars, "cdm2");

		SessionVars sVars = new SessionVars();
		sVars.connection.createBasicDataSource("cdm2");
		FormsMatrixDynamic fmd = null;
		
			// set the default user id in sVars
			sVars.userNumber = new User(sVars).getDefaultUser().id;

			fmd = new MainPartsForm(sVars).fmd;

			for (fmd.row = 0; fmd.row < fmd.getNumberOfRows(); fmd.row++) {
				// ignore the company in the first column
				for (fmd.column = 1; fmd.column < fmd.get(fmd.row).size(); fmd.column++) {
					for (SearchTarget.SEARCHTYPES type : SearchTarget.SEARCHTYPES.values()) {
						{
							// fake the user requesting a query
							String buttonName = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).REQUESTALIST;
							// clear all previous buttons we've inserted
							sVars.parameterMap.clear();
							sVars.parameterMap.put(buttonName, null);

							boolean endOfInput = false;
							try {
								fmd.get(fmd.row).get(fmd.column).extractParams(sVars);
							} catch (EndOfInputRedoQueries e) {
								endOfInput = true;
							}
							if (!endOfInput)
								fail("extractParams failed");
						}
						IdAndStrings ids = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).doQuery();
						if (ids.isEmpty()) {
							System.out.println("empty ids for " + fmd.getObject().getLogicalName() + " type:" + type);
//							System.out.println(" firstDislayedRecord:"
//									+ fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).firstDisplayedRecord);
							continue;
						}
						while (ids.size() == IdAndStrings.DISPLAYSIZE) {
							// ask for more
							System.out.println("full ids for " + fmd.getObject().getLogicalName() + " type:" + type);
//							System.out.println(" firstDislayedRecord:"
//									+ fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).firstDisplayedRecord);
							{
								// fake the user requesting a query
								String buttonName = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).NEXT;
								// clear all previous buttons we've inserted
								sVars.parameterMap.clear();
								sVars.parameterMap.put(buttonName, null);
								boolean endOfInput = false;
								try {
									fmd.get(fmd.row).get(fmd.column).extractParams(sVars);
								} catch (EndOfInputRedoQueries e) {
									endOfInput = true;
								}
								if (!endOfInput)
									fail("extractParams failed");
							}
							ids = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).doQuery();
						}
						if (!ids.isEmpty()) {
							System.out.println(
									ids.size() + " ids for " + fmd.getObject().getLogicalName() + " type:" + type);
//							System.out.println(" firstDislayedRecord:"
//									+ fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).firstDisplayedRecord);
						}

						// all of the empty responses were continued above so at this point, we have
						// non-empty queries.
						// at this point, each remaining IdAndString query responded and should be at
						// its end. unwind them back to the beginning

						// fake a PREVIOUS button

						{
							// fake the user requesting a query
							String buttonName = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).PREVIOUS;
							// clear all previous buttons we've inserted
							sVars.parameterMap.clear();
							sVars.parameterMap.put(buttonName, null);
							boolean endOfInput = false;
							try {
								fmd.get(fmd.row).get(fmd.column).extractParams(sVars);
							} catch (EndOfInputRedoQueries e) {
								endOfInput = true;
							}
							if (!endOfInput)
								fail("extractParams failed");
						}
						do {
							ids = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).doQuery();
							System.out.println("previous " + ids.size() + " ids for " + fmd.getObject().getLogicalName()
									+ " type:" + type);
//							System.out.println(" firstDislayedRecord:"
//									+ fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).firstDisplayedRecord);
							{
								// fake the user requesting a query
								String buttonName = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).PREVIOUS;
								// clear all previous buttons we've inserted
								sVars.parameterMap.clear();
								sVars.parameterMap.put(buttonName, null);
								boolean endOfInput = false;
								try {
									fmd.get(fmd.row).get(fmd.column).extractParams(sVars);
								} catch (EndOfInputRedoQueries e) {
									endOfInput = true;
								}
								if (!endOfInput)
									fail("extractParams failed");
							}
						} while (fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).so.lastOffset > 0);

					}
				}
			}
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace())
				System.out.println(ste);
			fail(e.getLocalizedMessage());
		} 
		try {
			sVars.connection.createBasicDataSource(MyConnection.XMLDBNAME);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	/**
	 * exercise all queries with a part selected
	 */
	public void testAllQueriesPartSelected() {
//		String currentDbName =null;
		try {
//			currentDbName = sVars.xml.getDefaultDbName();
		sVars.connection.createBasicDataSource("cdm2");

		SessionVars sVars = new SessionVars();
		FormsMatrixDynamic fmd = null;
		Part part = null;
		
			// set the default user id in sVars
			sVars.userNumber = new User(sVars).getDefaultUser().id;
			part = new Part(sVars);
			fmd = new MainPartsForm(sVars).fmd;

			// find a part object in the first row
			fmd.row = 0;
			for (fmd.column = 0; fmd.column < fmd.get(0).size(); fmd.column++) {
				if (fmd.get(0).get(fmd.column).obj.getMyFileName().equals(part.getMyFileName())) {

					// get any part in the database
					Connection con = sVars.connection.getConnection();
					MyStatement st = new MyStatement(con);
					ResultSet rs = st.executeQuery("select id from " + part.getMyFileName() + " limit 1");
					int partId = -1;
					if (rs.next()) {
						partId = rs.getInt(1);
					} else {
						rs.close();
						st.close();
						con.close();
						break;
					}
					rs.close();
					st.close();
					con.close();
					part.find(partId);
					break;
				}
			}
//			if (!part.isLoaded())
//				fail("part not found");

			for (fmd.row = 0; fmd.row < fmd.getNumberOfRows(); fmd.row++) {
				// ignore the company in the first column
				for (fmd.column = 1; fmd.column < fmd.get(fmd.row).size(); fmd.column++) {
					for (SearchTarget.SEARCHTYPES type : SearchTarget.SEARCHTYPES.values()) {
						{
							// fake the user requesting a query
							String buttonName = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).REQUESTALIST;
							// clear all previous buttons we've inserted
							sVars.parameterMap.clear();
							sVars.parameterMap.put(buttonName, null);

							boolean endOfInput = false;
							try {
								fmd.get(fmd.row).get(fmd.column).extractParams(sVars);
							} catch (EndOfInputRedoQueries e) {
								endOfInput = true;
							}
							if (!endOfInput)
								fail("extractParams failed");
						}
						IdAndStrings ids = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).doQuery();
						if (ids.isEmpty()) {
							System.out.println("empty ids for " + fmd.getObject().getLogicalName() + " type:" + type);
//							System.out.println(" firstDislayedRecord:"
//									+ fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).firstDisplayedRecord);
							continue;
						}
						while (ids.size() == IdAndStrings.DISPLAYSIZE) {
							// ask for more
							System.out.println("full ids for " + fmd.getObject().getLogicalName() + " type:" + type);
//							System.out.println(" firstDislayedRecord:"
//									+ fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).firstDisplayedRecord);
							{
								// fake the user requesting a query
								String buttonName = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).NEXT;
								// clear all previous buttons we've inserted
								sVars.parameterMap.clear();
								sVars.parameterMap.put(buttonName, null);
								boolean endOfInput = false;
								try {
									fmd.get(fmd.row).get(fmd.column).extractParams(sVars);
								} catch (EndOfInputRedoQueries e) {
									endOfInput = true;
								}
								if (!endOfInput)
									fail("extractParams failed");
							}
							ids = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).doQuery();
						}
						if (!ids.isEmpty()) {
							System.out.println(
									ids.size() + " ids for " + fmd.getObject().getLogicalName() + " type:" + type);
//							System.out.println(" firstDislayedRecord:"
//									+ fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).firstDisplayedRecord);
						}

						// all of the empty responses were continued above so at this point, we have
						// non-empty queries.
						// at this point, each remaining IdAndString query responded and should be at
						// its end. unwind them back to the beginning

						// fake a PREVIOUS button

						{
							// fake the user requesting a query
							String buttonName = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).PREVIOUS;
							// clear all previous buttons we've inserted
							sVars.parameterMap.clear();
							sVars.parameterMap.put(buttonName, null);
							boolean endOfInput = false;
							try {
								fmd.get(fmd.row).get(fmd.column).extractParams(sVars);
							} catch (EndOfInputRedoQueries e) {
								endOfInput = true;
							}
							if (!endOfInput)
								fail("extractParams failed");
						}
						do {
							ids = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).doQuery();
							System.out.println("previous " + ids.size() + " ids for " + fmd.getObject().getLogicalName()
									+ " type:" + type);
//							System.out.println(" firstDislayedRecord:"
//									+ fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).firstDisplayedRecord);
							{
								// fake the user requesting a query
								String buttonName = fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).PREVIOUS;
								// clear all previous buttons we've inserted
								sVars.parameterMap.clear();
								sVars.parameterMap.put(buttonName, null);
								boolean endOfInput = false;
								try {
									fmd.get(fmd.row).get(fmd.column).extractParams(sVars);
								} catch (EndOfInputRedoQueries e) {
									endOfInput = true;
								}
								if (!endOfInput)
									fail("extractParams failed");
							}
						} while (fmd.get(fmd.row).get(fmd.column).getIdAndStrings(type).so.lastOffset > 0);

					}
				}
			}
		} catch (Exception e) {
			for (StackTraceElement ste : e.getStackTrace())
				System.out.println(ste);
			fail(e.getLocalizedMessage());
		}
		try {
			sVars.connection.createBasicDataSource(MyConnection.XMLDBNAME);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
