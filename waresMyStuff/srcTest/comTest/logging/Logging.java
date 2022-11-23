package comTest.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

public class Logging {

	private final static Logger LOGGER = Logger.getLogger(Logging.class.getName());
	static {
		Handler fileHandler = null;
		try {
			fileHandler = new FileHandler(Logging.class.getSimpleName());
			LOGGER.addHandler(fileHandler);
			fileHandler.setLevel(Level.ALL);
			LOGGER.setLevel(Level.ALL);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error in FileHandler", e);
		}
	}

//	static {
//		LOGGER.setLevel(Level.ERROR);
//	}
	@Before
	public void setUp() throws Exception {
//		new XML(sVars)
//		new Utilities().allNewTables(sVars);
	}

	@Test
	public void test() {
		LOGGER.warning("a warning message");
	}
	
	// exercise the logging function of Internals
//	@Test
//	public void testInternals() throws ExceptionCoding {
//		throw new ExceptionCoding("dump this string and continue");
//	}

}
