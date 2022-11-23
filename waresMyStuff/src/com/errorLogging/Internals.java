package com.errorLogging;

import java.util.ArrayList;

public class Internals {
//	private static Logger logger;
//	static {
//		if (useLogging())
//			logger = Logger.getLogger(Internals.class.getName());
//	}

	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

	public static String getFileName() {
		return Thread.currentThread().getStackTrace()[2].getFileName();
	}

	public static String getClassName() {
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}

	public static String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

//	public static void dumpStringExitDeleteMe(String e) {
//		try {
//			throw (new Exception(e));
//		} catch (Exception s) {
//			dumpException(s);
//		}
//		System.exit(-1);
//	}

//	public static String loggingFile = null;
//	static {
//		loggingFile = System.getProperty("user.dir");
//		loggingFile += System.getProperty("file.separator");
//		loggingFile += "waresMyStuff" + System.getProperty("file.separator");
//		loggingFile += Internals.class.getSimpleName() + ".log";
//	}

//	public final static Logger LOGGER = Logger.getLogger(Internals.class.getName());
//	static {
//		LOGGER.setLevel(Level.ALL);
//		FileHandler fileHandler = null;
//		String s = "";
//		try {
//			s = System.getProperty("user.dir");
//			s += System.getProperty("file.separator");
//			s += "waresMyStuff" + System.getProperty("file.separator");
//			s += Internals.class.getSimpleName() + ".log";

//			fileHandler = new FileHandler(loggingFile);
//			LOGGER.addHandler(fileHandler);
//			fileHandler.setLevel(Level.ALL);
//			LOGGER.setLevel(Level.ALL);
//		} catch (IOException e) {
//			LOGGER.getName();
//			LOGGER.log(Level.SEVERE, "Error in FileHandler "+LOGGER.getName(), e);
//		}
//	}

//	public static File logFile = null;
//	private static BufferedWriter writer = null;
//	static {
//		try {
//			logFile = File.createTempFile("wares", ".log");
//			writer = new BufferedWriter(new FileWriter(logFile));
//			writer.write("start logging\n");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public static String getLogFilePathandName() {
//		return logFile.getAbsolutePath();
//	}
//
//	public static void logWithDate() {
//		try {
//			LocalDateTime ldt = LocalDateTime.now();
//			writer.write(ldt.toString()+" File: " + Thread.currentThread().getStackTrace()[2].getFileName() + " Line:"
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()+"\n");
//			writer.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public static void logString(String string) {
//		try {
//			writer.write(string+"\n");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logWithDate();
//	}

	public static void dumpException(Exception e) {
		for (StackTraceElement ste : e.getStackTrace())
			System.out.println(ste);
		System.out.println(e.getLocalizedMessage());
	}

	public static ArrayList<String> dumpExceptionToString(Exception e) {
		return dumpExceptionToString(e, "");
	}

	public static ArrayList<String> dumpExceptionToString(Exception e, String separator) {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add(e.getMessage() + separator);
		for (StackTraceElement thisRec : e.getStackTrace())
			ret.add(" at " + thisRec.getClassName() + "." + thisRec.getMethodName() + ":" + thisRec.getLineNumber()
					+ separator);
		return ret;
	}

	static ArrayList<String> startupErrorStrings = new ArrayList<String>();
	static ArrayList<Exception> startupExceptions = new ArrayList<Exception>();

	public static void logStartupError(Exception e) {
		startupErrorStrings.addAll(dumpExceptionToString(e, "<br>\n"));
	}

	public static void logStartupError(String s) {
		startupErrorStrings.add(s + "<br>\n");
	}

	public static boolean startupError() {
		return !startupErrorStrings.isEmpty();
	}

	public static ArrayList<String> getStartupError() {
		ArrayList<String> ret = new ArrayList<String>();
		for (String s : startupErrorStrings)
			ret.add(s);
		startupErrorStrings = new ArrayList<String>();
		for (Exception e : startupExceptions)
			ret.addAll(dumpExceptionToString(e));
		startupExceptions = new ArrayList<Exception>();
		return ret;
	}

	// check that the logging file exists.
	// return an emptry string if everything is OK
//	public static String checkLogging(String separator) {
//		// create the logging file
//		dumpException(new Exception("checking logging file '" + loggingFile + "' exists"));
//
//		// see if it worked
//		File f = new File(loggingFile);
//		if (!f.exists())
//			return loggingFile + " does not exist." + separator;
//		if (f.isDirectory()) {
//			return loggingFile + " is a directory and not a file." + separator;
//		}
//
//		// see if the logging directory exists
//
//		return "";
//	}

}
