package comTest.security;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;

import com.db.SessionVars;
import com.db.XML;
import com.security.Anchor;
import com.security.Company;

import comTest.utilities.Utilities;

public class SyncTest2 {
	SessionVars sVars = null;

	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
		
		new Utilities().allNewTables(sVars);
	}

	@After
	public void tearDown() throws Exception {
	}

	static int counter = 0;

	public class HelloRunnable implements Runnable {

		public void tickle() {
			System.out.println("tickle");
			(new Thread(this)).start();
		}

		@Override
		public void run() {
			Company company = Utilities.getACompany();
			Anchor anchor = null;
			try {
				anchor = company.getAnchor();
			} catch (Exception e) {
				fail(e.getLocalizedMessage());
			}
			switch (counter) {
			case 0:
				new Utilities().getAUser(anchor);
				System.out.println("zero");
				break;
			case 1:
				Utilities.getARole(anchor);
				System.out.println("one");
				break;
			case 2:
				System.out.println("two");
				break;
			case 3:
				System.out.println("three");
				break;
			case 4:
				System.out.println("four");
				break;
			default:
				System.out.println("counter of " + counter);
			}
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				Internals.dumpExceptionExit(e);
//			}
			counter++;
		}
	}

	// @Test
	public void testRunnable() {
//		new XML(sVars)
//		User.newTable(sVars);
//		Warehouse.newTable(sVars);
		Thread t1 = new Thread(new HelloRunnable());
		t1.start();
		Thread t2 = new Thread(new HelloRunnable());
		t2.start();
		Thread t3 = new Thread(new HelloRunnable());
		t3.start();
		Thread t4 = new Thread(new HelloRunnable());
		t4.start();
		Thread t5 = new Thread(new HelloRunnable());
		t5.start();

		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// System.out.println("running");
		// for (int i = 0; i < 5; i++) {
		// Thread t = new Thread(new HelloRunnable());
		// t.start();
		// try {
		// t.join();
		// } catch (InterruptedException e1) {
		// Internals.dumpExceptionExit(e1);
		// }
		// }

		// HelloRunnable hr = new HelloRunnable();
		// hr.tickle();
		// try {
		// Thread.sleep(4000);
		// } catch (InterruptedException e) {
		// Internals.dumpExceptionExit(e);
		// }
		// (new Thread(new HelloRunnable())).start();
		// (new Thread(new HelloRunnable())).start();
		// (new Thread(new HelloRunnable())).start();
	}
}
