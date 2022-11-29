package comTest.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
//import org.xml.sax.SAXException;

import com.db.MyConnection;
import com.db.MyStatement;
import com.db.SessionVars;
import comTest.utilities.Utilities;

public class MyStatementTest
//implements StartOverCallback
{
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		new Utilities().allNewTables(sVars);
	}

	@Test
	public void testExecuteQuery() {
		Connection conn = null;
		MyStatement ms = null;
		try {
			try {
				conn = sVars.connection.getConnection();
				ms = new MyStatement(conn);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
			try {
				ms.executeQuery("askdfj adkfjsasdf kasdf;");
				fail("allowed junk");
			} catch (SQLException e) {
			}
			try {
				ms.executeQuery("show tables;");
			} catch (SQLException e) {
				fail(e.getLocalizedMessage());
			}
		} catch (Exception e) {

		} finally {
			if (ms != null)
				try {
					ms.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
		}
	}

	@Test
	public void testExecuteUpdateKey() {
		try {
			Utilities.createTable("executeMyUpdate", sVars);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// put "name1" into the db, should get id=1 for the first record
		// no leading commas on first field and value
		String fields = "name";
		String values = "'name1'";
		Connection conn = null;
		MyStatement st = null;
		try {
			conn = sVars.connection.getConnection();
			st = new MyStatement(conn);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		int id = 0;

		try {
			id = st.executeUpdateKey("INSERT INTO executeMyUpdate (" + fields + ") VALUES (" + values + ")");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (id != 1)
			fail("id should have been 1, got " + id);

		// put "name2" into db, should get id=2 for the second record
		fields = "name";
		values = "'name2'";

//		try {
//
//			st = new MyStatement();
//		} catch (Exception e) {
//			fail(e.getLocalizedMessage());
//		}
		id = 0;

		try {
			id = st.executeUpdateKey("INSERT INTO executeMyUpdate (" + fields + ") VALUES (" + values + ")");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		if (id != 2)
			fail("id should have been 2, got " + id);

		// tidy up the db
		try {
			st.executeMyUpdate("DROP TABLE IF EXISTS `executeMyUpdate`");
		} catch (SQLException e) {
			fail(e.getLocalizedMessage());
		}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				fail(e.getLocalizedMessage());
			}

	}

	@Test
	public void testExecuteUpdate() {
		final String TABLENAME = "executeReturn";
		Utilities.createTable(TABLENAME, sVars);

		// put in 10 records, delete records 5 through 7, see if 3 rows affected
		// put "name1" into the db, should get id=1 for the first record
		// no leading commas on first field and value
		String fields = "";
		String values = "";
		MyStatement st = null;
		Connection conn = null;
		try {
			try {
				conn = sVars.connection.getConnection();
				st = new MyStatement(conn);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
			for (int i = 0; i < 10; i++) {
				fields = "name";
				values = "'name" + (i + 1) + "'";
				try {
					st.executeMyUpdate("INSERT INTO " + TABLENAME + " (" + fields + ") VALUES (" + values + ")");
				} catch (Exception e) {
					fail(e.getLocalizedMessage());
				}
			}

			// delete 3 rows
			int count = 0;
			try {
				count = st.executeMyUpdate("DELETE from " + TABLENAME + " WHERE (id >= '5') AND (id <= '7')");
			} catch (SQLException e) {
				fail(e.getLocalizedMessage());
			}

			if (count != 3)
				fail("expected count of 3, got " + count);
			// tidy up the db
			try {
				st.executeMyUpdate("DROP TABLE IF EXISTS `" + TABLENAME + "`");
			} catch (SQLException e) {
				fail(e.getLocalizedMessage());
			}
		} finally {
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
		}
	}

	@Test
	public void testTestListTable() {
		String dbName = null;
		try {
			dbName = sVars.xml.readXML(MyConnection.XMLDBNAME);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		final String FIRSTTABLENAME = "executeReturn";
		Connection conn = null;
		MyStatement ms = null;
		try {
			try {
				Utilities.createTable(FIRSTTABLENAME + "1", sVars);
				Utilities.createTable(FIRSTTABLENAME + "2", sVars);
				Utilities.createTable(FIRSTTABLENAME + "3", sVars);
				conn = sVars.connection.getConnection();
				ms = new MyStatement(conn);
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
			Collection<String> titles = new ArrayList<String>();
			ResultSet rs = null;
			try {
				rs = ms.executeQuery(
						"SELECT table_name FROM information_schema.tables where table_schema='" + dbName + "'");
			} catch (SQLException e) {
				fail(e.getLocalizedMessage());
			}
			try {
				while (rs.next()) {
					// String s = rs.getString(1);
					// titles.add(rs.getString(1));
					titles.add(rs.getString("table_name"));
					// System.out.println("string '" + s + "'");
				}
			} catch (SQLException e) {
				fail(e.getLocalizedMessage());
			}
			try {
				ms.close();
			} catch (SQLException e) {
				fail(e.getLocalizedMessage());
			}

			if (titles.size() != 3)
				fail("expected size of 3, got " + titles.size());

			if (!titles.contains((FIRSTTABLENAME + "1")))
//					.toLowerCase()))
				fail("could not find " + FIRSTTABLENAME + "1");
			if (!titles.contains((FIRSTTABLENAME + "2")))
//					.toLowerCase()))
				fail("could not find " + FIRSTTABLENAME + "2");
			if (!titles.contains((FIRSTTABLENAME + "3")))
//					.toLowerCase()))
				fail("could not find " + FIRSTTABLENAME + "3");
		} finally {
			if (ms != null)
				try {
					ms.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}
		}
	}

	/**
	 * see if connections are pooled
	 */
	@Test
	public void testConnectionPool() {
		Set<Integer> connectionSet = new HashSet<Integer>();
		Connection con = null;
		int connections = 100;
		for (int i = 0; i < connections; i++) {

			try {
				con = sVars.connection.getConnection();
			} catch (Exception e1) {
				fail(e1.getLocalizedMessage());
			}

			connectionSet.add(System.identityHashCode(con));

			if (con == null)
				fail("null connection");
			try {
				con.close();
			} catch (SQLException e1) {
				fail(e1.getLocalizedMessage());
			}
		}
	}

	/**
	 * see if connections are pooled
	 */
	@Test
	public void testConnectionPoolC() {
		Set<Connection> connectionSet = new HashSet<Connection>();
		Connection con = null;
		int connections = 100;
		for (int i = 0; i < connections; i++) {

			try {
				con = sVars.connection.getConnection();
			} catch (Exception e1) {
				fail(e1.getLocalizedMessage());
			}

			connectionSet.add(con);

			if (con == null)
				fail("null connection");
			try {
				con.close();
			} catch (SQLException e1) {
				fail(e1.getLocalizedMessage());
			}
		}
		System.out.println("number of connections:" + connectionSet.size());
	}
}
