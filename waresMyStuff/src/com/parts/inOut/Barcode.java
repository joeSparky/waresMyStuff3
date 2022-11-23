package com.parts.inOut;

public class Barcode {
	/**
	 * raw barcode. used to set the sub-field fields.
	 */
	public String rawBarcode;
	public static final int BARCODELENGTH = 42;
	public static final int BARCODELENGTHMINIMUM = 2;

	public Barcode() {
		clear();
	}
	/**
	 * clear the rawBarcode field
	 */
	public void clear(){
		rawBarcode = "";
	}

	/**
	 * thrown an exception if the barcode length is out of range
	 * 
	 * @param tmpBarcode
	 * @throws Exception
	 */
	public void barcodeSanity() throws Exception {
		if (rawBarcode == null)
			throw new Exception("Please enter a barcode of at least "
					+ Barcode.BARCODELENGTHMINIMUM + " characters.");
		if ((rawBarcode.length() < Barcode.BARCODELENGTHMINIMUM))
			throw new Exception("Please enter a barcode of at least "
					+ Barcode.BARCODELENGTHMINIMUM + " characters.");
		if (rawBarcode.length() > Barcode.BARCODELENGTH)
			throw new Exception("Please enter a barcode of at most "
					+ Barcode.BARCODELENGTH + " characters.");
	}

	public boolean isSet() {
		if (rawBarcode == null)
			return false;
		try {
			barcodeSanity();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean equals(Barcode barcode) {
		return this.rawBarcode.equals(barcode.rawBarcode);
	}
}
