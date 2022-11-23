package com.parts.inOut;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpirationDate extends Date {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum DATETYPE {
		NOTINITIALIZED, YYMMDD, YYYYMMDD
	}
	private DATETYPE dateType;

	public ExpirationDate() {
		df = null;
	}

	private Date expDate;
	SimpleDateFormat df;

	public void setFormat(DATETYPE dateType) throws Exception {
		switch (dateType) {
		case YYMMDD:
			df = new SimpleDateFormat("yyMMdd");
			break;
		case YYYYMMDD:
			df = new SimpleDateFormat("yyyyMMdd");
			break;
		default:
			throw new Exception("invalid dateType of " + dateType.toString());
		}
		df.setLenient(false);
		this.dateType = dateType;
	}

	public void setExp(String str) throws Exception {
		if (df == null)
			throw new Exception("date format not set");
		try {
			expDate = df.parse(str);
		} catch (ParseException e) {
			switch (dateType) {
			case YYMMDD:
				throw new Exception("Invalid date format of " + str
						+ ". Expecting a YYMMDD date format");
			case YYYYMMDD:
				throw new Exception("Invalid date format of " + str
						+ ". Expecting a YYYYMMDD date format");
			default:
				throw new Exception("invalid dateType of "
						+ dateType.toString());
			}
		}
	}
	public Date get(){
		return expDate;
	}
}
