package com.parts.inOut;

public class LocationBarcode extends Barcode{
	public void setLocationBarcode(String barcode){
		rawBarcode = barcode;
	}
	public void clone(LocationBarcode lbc){
		rawBarcode = lbc.rawBarcode;
	}
}
