package com.reports;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.db.SessionVars;
import com.security.ExceptionCoding;

public class StringWriter {
	String csvPath = null;

//String fileSeparator = null;
	public StringWriter(SessionVars sVars) throws Exception {
//		XML xml = 
		csvPath = sVars.getCSVPATH() + sVars.getSeparator();
	}

	public void writeString(String fileName, String whatToWrite) throws Exception {
		File directory = new File(csvPath);
		if (!directory.exists() && !directory.mkdir())
			throw new Exception("Could not create the " + csvPath + " directory.");
		File file = new File(csvPath + fileName);
		if (file.exists() && !file.delete())
			throw new Exception("Could not delete " + csvPath + fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw new ExceptionCoding(e.getLocalizedMessage() + " for file " + csvPath + fileName);
		}

//		try (
//		FileOutputStream fout = new FileOutputStream(csvPath + fileName);
		PrintWriter out = new PrintWriter(csvPath + fileName);
//				) 
//		{
			out.print(whatToWrite);
			out.close();
//		} catch (IOException e) {
//			Internals.dumpException(e);
//		}
//	
//		}
	}
}
