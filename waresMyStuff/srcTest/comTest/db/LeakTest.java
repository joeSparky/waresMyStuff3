package comTest.db;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.db.MyStatement;
import com.db.SessionVars;

public class LeakTest {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
	}

//	@After
//	public void tearDown() throws Exception {
//		MyConnection.flush();
//	}

	@Test
	public void testGetStatement() {
		Connection conn = null;
		try {
			conn = sVars.connection.getConnection();

			ArrayList<MyStatement> saveHere = new ArrayList<MyStatement>();
			for (int i = 0; i < 90; i++)
				try {
					saveHere.add(new MyStatement(conn));
				} catch (Exception e) {
					fail("with i:" + i + ", " + e.getLocalizedMessage());
				}
			Iterator<MyStatement> i = saveHere.iterator();
			while (i.hasNext())
				try {
					i.next().close();
				} catch (Exception e) {
					fail("with i:" + i + ", " + e.getLocalizedMessage());
				}

		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					fail(e.getLocalizedMessage());
				}

		}
	}
}
