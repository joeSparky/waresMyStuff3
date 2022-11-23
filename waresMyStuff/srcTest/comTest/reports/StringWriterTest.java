package comTest.reports;

import static org.junit.Assert.fail;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.db.SessionVars;
import com.db.XML;
import com.reports.StringWriter;

public class StringWriterTest {
SessionVars sVars = null;
	@Before
	public void setUp() throws Exception {
		sVars = new SessionVars();
	}

	@After
	public void tearDown() throws Exception {
	}

	// ask StringWriter to create a directory
//	@Test
	public void testDirectoryNotFoundException() {
		// delete the csv directory if it exists
		String csvPath = null;
		try {
			csvPath = sVars.xml.readXML(XML.CSVPATH);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
		File directory = new File(csvPath);
		for (String fileName : directory.list()) {
			File fileToBeDeleted = new File(csvPath + "\\" + fileName);
			if (!fileToBeDeleted.delete())
				fail("could not delete " + csvPath + fileName);
		}

		if (directory.exists() && !directory.delete())
			fail("could not delete directory:" + csvPath);

		String writeThis = "first line\nsecond line\n";
		try {
			new StringWriter(new SessionVars()).writeString("myCsvFile.csv", writeThis);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	// ask StringWriter to write a few lines
	@Test
	public void testWriteAFewLines() {
		String writeThis = "first line\nsecond line\n";
		try {
			new StringWriter(sVars).writeString("myCsvFile.csv", writeThis);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}
}
