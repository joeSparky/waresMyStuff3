package comTest.security;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SyncTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	static void printThis(int n){
		System.out.println(n);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}
		System.out.println(n);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}
		System.out.println(n);
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}
	}
	class M1 extends Thread{
		M1(){
			printThis(10);
		}
		public void run(){
			printThis(1);
		}
	}
	class M2 extends Thread{
		M2(){
			printThis(20);
		}
		public void run(){
			printThis(2);
		}
	}
	class M3 extends Thread{
		M3(){
			printThis(30);
		}
		public void run(){
			printThis(3);
		}
	}
	class M4 extends Thread{
		M4(){
			printThis(40);
		}
		public void run(){
			printThis(4);
		}
	}

	@Test
	public void testAccessIntInt() {
		M1 m1 = new M1();
		M2 m2 = new M2();
		M3 m3 = new M3();
		M4 m4 = new M4();
		m1.start();
		m2.start();
		m3.start();
		m4.start();
	}
	public class HelloRunnable implements Runnable{

		@Override
		public void run() {			
			System.out.println("first HelloRunnable:"+Thread.currentThread().getName());
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				fail(e.getLocalizedMessage());
			}
			System.out.println("second HelloRunnable:"+Thread.currentThread().getName());
		}
		
	}
	@Test
	public void testRunnable(){
		System.out.println("running");
		(new Thread(new HelloRunnable())).start();
		(new Thread(new HelloRunnable())).start();
		(new Thread(new HelloRunnable())).start();
	}
}
