package com.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.security.ExceptionCoding;

public class MyStatement
//implements AutoCloseable 
{
	Statement statement = null;
	Connection connection = null;

	public MyStatement(Connection connection) throws Exception {
//		connection = MyConnection.getConnection();
		statement = connection.createStatement();
	}
	public void close() throws SQLException {
//		try {
			statement.close();
//			connection.close();
//		} catch (SQLException e) {
//			Internals.dumpExceptionExit(e);
//		}
	}


	public ResultSet executeQuery(String query) throws SQLException {
//		try {
		return statement.executeQuery(query);
//		} catch (SQLException e) {
//			Internals.dumpSQLExit(e);
//			return null;
//		}
	}

	public int executeMyUpdate(String s) throws SQLException {
		return statement.executeUpdate(s);
	}

	public ResultSet executeQueryWithException(String query) throws Exception {
		Exception e = new Exception();
		try {
			return statement.executeQuery(query);
		} catch (SQLException e1) {
			e = e1;
		}
		throw e;
	}

	public int executeUpdateKey(String execute) throws Exception {
		String eString = "";
		try {
			if (statement.executeUpdate(execute, java.sql.Statement.RETURN_GENERATED_KEYS) == 1) {
				ResultSet rs = statement.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					throw new ExceptionCoding("executeUpdateKey failure");
				}
			}
		} catch (SQLException e) {
			eString = e.getLocalizedMessage();
		}
		if (!eString.isEmpty())
			throw new Exception(eString);
		try {
			statement.close();
		} catch (SQLException e) {
			eString = e.getLocalizedMessage();
		}
		throw new Exception(eString);
	}

//	static final String DRIVER = "com.mysql.cj.jdbc.Driver";
//	static final String DATABASE_URL = "jdbc:mysql://localhost/";
//	public static final String XMLDBNAME = "dbName";
//	static final String MXLDBACCOUNT = "dbAccount";
//	static final String MXLDBPASSWORD = "dbPassword";

//	public java.sql.Statement getStatement() throws Exception {
//		return statement;
////		return datasource.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//	}

	public boolean tableExists(String tableName) throws Exception {
		try
//		(Statement st = datasource.getConnection().createStatement()) 
		{
			statement.executeQuery("SELECT 1 FROM " + tableName + " LIMIT 1");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void wipeDb(String databaseName) throws Exception {
		Strings strs = listAllTables(databaseName);

//		try (Statement st = getStatement()) {
		for (String s : strs) {
			statement.executeUpdate("drop table if exists " + s);
//			}

		}
	}

	public Strings listAllTables(String dbName) throws Exception {
		Strings strs = new Strings();
		try (
//				Statement st = getStatement();
				ResultSet rs = statement.executeQuery(
						"select table_name from information_schema.tables where table_schema ='" + dbName + "'")) {
			while (rs.next()) {
				strs.add(rs.getString("table_name"));
			}
		}
		return strs;
	}
}
