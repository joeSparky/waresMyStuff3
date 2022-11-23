package com.parts.inOut;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Checksum128 {
	public static int STARTA = 103;
	public static int STARTB = 104;
	public static int STARTC = 105;
	public static int CODEA = 101;
	public static int CODEB = 100;
	public static int CODEC = 99;
	public static int STOP = 12345;

	public ArrayList<Integer> code128(String encodeThis){
		return compress128(encodeSymbols( encode128(encodeThis)));
	}
	/**
	 * compress contiguous ones and zeros into a list of integers.
	 * 11011100 becomes 2, 1, 3, 2
	 * assumes that the string starts with a 1
	 * @param onesAndZeroes
	 * @return
	 */
	public ArrayList<Integer> compress128(String onesAndZeroes){
		ArrayList<Integer> ret = new ArrayList<Integer>();
		CharacterIterator stepper = new StringCharacterIterator(onesAndZeroes);
		int countOnes = 0;
		int countZeroes = 0;
		/**
		 * the last block of ones or zeroes
		 */
		char lastBlock = '1'; // assume the list starts with a 1
		for(char ch=stepper.first(); ch != CharacterIterator.DONE; ch=stepper.next()){
			if (ch=='1'){
				countOnes++;
				// if this is the first 1
				if (lastBlock=='0') {
					// dump out the count of zeroes
					ret.add(countZeroes);
					countZeroes=0;
					lastBlock='1';
				}
			} else {
				countZeroes++;
				if (lastBlock=='1'){
					ret.add(countOnes);
					countOnes = 0;
					lastBlock='0';
				}
			}
		}
		if (lastBlock=='1'){
			ret.add(countOnes);
		} else {
			ret.add(countZeroes);
		}
		return ret;
	}
	/**
	 * append a checksum
	 * 
	 * @param str
	 * @return
	 */
	public void checkSum(ArrayList<Integer> symbols) {
		Iterator<Integer> stepper = symbols.iterator();

		int checksum = 0;
		if (stepper.hasNext()){
			checksum = stepper.next();
		}
		int position = 1;
		while (stepper.hasNext()) {
			checksum += stepper.next()*position;
			position++;
		}
		int remainder = checksum % 103;
		symbols.add(remainder);
		symbols.add(STOP);
	}

	public static int toCode128(Character chr) {
		if (chr < 32) {
			System.out.println("can not convert " + chr + " to code 128");
			return 0;
		}
		if (chr > 127) {
			if (chr < 32) {
				System.out.println("can not convert " + chr + " to code 128");
				return 0;
			}
		}
		return chr - 32;
	}

	private enum STATE {
		NOTINITIALIZED, CODEA, CODEB, CODEC, SECONDOFTWO
	}

	/**
	 * encode a string into code-128
	 * 
	 * @param str
	 * @return
	 */
	public ArrayList<Integer> encode128(String str) {
		STATE myState = STATE.NOTINITIALIZED;
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for (int i = 0; i < str.length(); i++) {
			switch (myState) {
			case NOTINITIALIZED:
				if (Character.isLowerCase(str.charAt(i))) {
					myState = STATE.CODEB;
					ret.add(STARTB);
					ret.add(toCode128(str.charAt(i)));
					break;
				}
				if (nextTwoAreDigits(str, i)) {
					myState = STATE.SECONDOFTWO;
					ret.add(STARTC);
					ret.add(Integer.parseInt("" + str.charAt(i)));
					break;
				}
				myState = STATE.CODEA;
				ret.add(STARTA);
				ret.add(toCode128(str.charAt(i)));
				break;
			case CODEA:
				if (Character.isLowerCase(str.charAt(i))) {
					myState = STATE.CODEB;
					ret.add(CODEB);
					ret.add(toCode128(str.charAt(i)));
					break;
				}
				if (nextTwoAreDigits(str, i)) {
					myState = STATE.SECONDOFTWO;
					ret.add(CODEC);
					ret.add(Integer.parseInt("" + str.charAt(i)));
					break;
				}
				ret.add(toCode128(str.charAt(i)));
				break;
			case CODEB:
				if (Character.isLowerCase(str.charAt(i))) {
					ret.add(toCode128(str.charAt(i)));
					break;
				}
				if (nextTwoAreDigits(str, i)) {
					myState = STATE.SECONDOFTWO;
					ret.add(CODEC);
					ret.add(Integer.parseInt("" + str.charAt(i)));
					break;
				}
				myState = STATE.CODEA;
				ret.add(CODEA);
				ret.add(toCode128(str.charAt(i)));
				break;
			case CODEC:
				if (Character.isLowerCase(str.charAt(i))) {
					myState = STATE.CODEB;
					ret.add(CODEB);
					ret.add(toCode128(str.charAt(i)));
					break;
				}
				if (nextTwoAreDigits(str, i)) {
					myState = STATE.SECONDOFTWO;
					ret.add(Integer.parseInt("" + str.charAt(i)));
					break;
				}
				myState = STATE.CODEA;
				ret.add(CODEA);
				ret.add(toCode128(str.charAt(i)));
				break;
			case SECONDOFTWO:
				// multiply the previous digit by 10
				int prev = ret.get(ret.size() - 1);
				ret.remove(ret.size() - 1);
				ret.add(Integer.parseInt("" + str.charAt(i)) + 10 * prev);
				myState = STATE.CODEC;
				break;
			default:
				System.out.println("undefined state of " + myState.toString());
				System.exit(-1);
			}
		}
		// if there was something to encode
		if (ret.size() > 0) {
			checkSum(ret);
		}
		return ret;
	}

	private boolean nextTwoAreDigits(String str, int index) {
		// if there can't possibly be 2 digits
		if (index >= str.length() - 1) {
			return false;
		}
		if (Character.isDigit(str.charAt(index))
				&& Character.isDigit(str.charAt(index + 1))) {
			return true;
		}
		return false;
	}

	public String encodeSymbols(ArrayList<Integer> myList) {
		String ret = "";
		for (int thisInt : myList) {
			assert encode.containsKey(thisInt) : "" + thisInt
					+ " is not in the encode table";
			ret += encode.get(thisInt);
		}
		// stop bars
		ret += "11";
		return ret;
	}

	private static final HashMap<Integer, String> encode = new HashMap<Integer, String>();
	static {
		encode.put(0, "11011001100");
		encode.put(1, "11001101100");
		encode.put(2, "11001100110");
		encode.put(3, "10010011000");
		encode.put(4, "10010001100");
		encode.put(5, "10001001100");
		encode.put(6, "10011001000");
		encode.put(7, "10011000100");
		encode.put(8, "10001100100");
		encode.put(9, "11001001000");
		encode.put(10, "11001000100");
		encode.put(11, "11000100100");
		encode.put(12, "10110011100");
		encode.put(13, "10011011100");
		encode.put(14, "10011001110");
		encode.put(15, "10111001100");
		encode.put(16, "10011101100");
		encode.put(17, "10011100110");
		encode.put(18, "11001110010");
		encode.put(19, "11001011100");
		encode.put(20, "11001001110");
		encode.put(21, "11011100100");
		encode.put(22, "11001110100");
		encode.put(23, "11101101110");
		encode.put(24, "11101001100");
		encode.put(25, "11100101100");
		encode.put(26, "11100100110");
		encode.put(27, "11101100100");
		encode.put(28, "11100110100");
		encode.put(29, "11100110010");
		encode.put(30, "11011011000");
		encode.put(31, "11011000110");
		encode.put(32, "11000110110");
		encode.put(33, "10100011000");
		encode.put(34, "10001011000");
		encode.put(35, "10001000110");
		encode.put(36, "10110001000");
		encode.put(37, "10001101000");
		encode.put(38, "10001100010");
		encode.put(39, "11010001000");
		encode.put(40, "11000101000");
		encode.put(41, "11000100010");
		encode.put(42, "10110111000");
		encode.put(43, "10110001110");
		encode.put(44, "10001101110");
		encode.put(45, "10111011000");
		encode.put(46, "10111000110");
		encode.put(47, "10001110110");
		encode.put(48, "11101110110");
		encode.put(49, "11010001110");
		encode.put(50, "11000101110");
		encode.put(51, "11011101000");
		encode.put(52, "11011100010");
		encode.put(53, "11011101110");
		encode.put(54, "11101011000");
		encode.put(55, "11101000110");
		encode.put(56, "11100010110");
		encode.put(57, "11101101000");
		encode.put(58, "11101100010");
		encode.put(59, "11100011010");
		encode.put(60, "11101111010");
		encode.put(61, "11001000010");
		encode.put(62, "11110001010");
		encode.put(63, "10100110000");
		encode.put(64, "10100001100");
		encode.put(65, "10010110000");
		encode.put(66, "10010000110");
		encode.put(67, "10000101100");
		encode.put(68, "10000100110");
		encode.put(69, "10110010000");
		encode.put(70, "10110000100");
		encode.put(71, "10011010000");
		encode.put(72, "10011000010");
		encode.put(73, "10000110100");
		encode.put(74, "10000110010");
		encode.put(75, "11000010010");
		encode.put(76, "11001010000");
		encode.put(77, "11110111010");
		encode.put(78, "11000010100");
		encode.put(79, "10001111010");
		encode.put(80, "10100111100");
		encode.put(81, "10010111100");
		encode.put(82, "10010011110");
		encode.put(83, "10111100100");
		encode.put(84, "10011110100");
		encode.put(85, "10011110010");
		encode.put(86, "11110100100");
		encode.put(87, "11110010100");
		encode.put(88, "11110010010");
		encode.put(89, "11011011110");
		encode.put(90, "11011110110");
		encode.put(91, "11110110110");
		encode.put(92, "10101111000");
		encode.put(93, "10100011110");
		encode.put(94, "10001011110");
		encode.put(95, "10111101000");
		encode.put(96, "10111100010");
		encode.put(97, "11110101000");
		encode.put(98, "11110100010");
		encode.put(99, "10111011110");
		encode.put(100, "10111101110");
		encode.put(101, "11101011110");
		encode.put(102, "11110101110");
		encode.put(103, "11010000100");
		encode.put(104, "11010010000");
		encode.put(105, "11010011100");
		encode.put(STOP, "11000111010");

	}

}
