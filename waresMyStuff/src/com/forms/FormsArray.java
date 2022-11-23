package com.forms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.db.SessionVars;
import com.errorLogging.Internals;
import com.security.ExceptionCoding;

public class FormsArray {
	public String body;
	/**
	 * name of input element to get focus
	 */
	private String focusTag;
	private String errorToUser, title;
	public int tableDepth;
	// public HashMap<Integer, Integer> cellDepth;
	private static final String NAME = "name";
	private static final String FORMNAME = "formName";
	public static final String HIDDENSUBMIT = "submitHidden";
	public static final String RETURNTO = "returnTo";
	private static final String FORMELEMENT = "<form action=\"handler\" method=\"POST\" name=\"" + FORMNAME + "\" >";
	private static final String ENDFORMELEMENT = "</form>";
	/**
	 * stop processing extraction when notDoneWithExtraction is false
	 */
//	private boolean notDoneWithExtraction = true;
//	public boolean notDoneWithExtraction() {
//		return notDoneWithExtraction;
//	}
//	public void doneWithExtraction() {
//		notDoneWithExtraction = false;
//	}
	/**
	 * the app to run
	 */
	public String returnToString;

//	public String getReturnToString() {
//		return returnToString;
//	}

	/**
	 * the application has handled the <form > element. don't add any others.
	 */
	public boolean formsSet;
	// private boolean selectedLastTime;
	private boolean javaScriptNeeded = false;

	// private boolean needsPreviewButton;

	public FormsArray() {

		status = STATUS.NOTINITIALIZED;
		tableDepth = 0;
		body = errorToUser = title = focusTag = "";
		returnToString = null;
		formsSet = false;
	}

	/**
	 * check to see if a button was selected
	 * 
	 * @param button
	 * @param sVars
	 * @return
	 */
	public static boolean whichButton(Form.ButtonType button, SessionVars sVars) {
		return sVars.keyNameValue(button.toString(), buttonText.get(button));
	}

	/**
	 * inform the caller that something was selected and that control should be
	 * returned to this form.
	 */
	// public void setSelectedLastTime() {
	// if (selectedLastTime)
	// throw new ExceptionCoding("selectedLastTime already set");
	// selectedLastTime = true;
	// }
	/**
	 * return true if the form was selected
	 * 
	 * @return
	 */
	// public boolean selectedLastTime(){
	// return selectedLastTime;
	// }

	/**
	 * if the cancel button was clicked
	 * 
	 * @param sVars
	 * @return
	 */
	public boolean cancelClicked(SessionVars sVars) {
		return sVars.keyNameValue(Form.ButtonType.CANCEL.toString(), buttonText.get(Form.ButtonType.CANCEL));
	}

	public boolean submitClicked(SessionVars sVars) {
		return sVars.keyNameValue(Form.ButtonType.SUBMITBUTTON.toString(),
				buttonText.get(Form.ButtonType.SUBMITBUTTON));
	}

	public boolean resetClicked(SessionVars sVars) {
		return sVars.keyNameValue(Form.ButtonType.RESET.toString(), buttonText.get(Form.ButtonType.RESET));
	}

	public boolean previewClicked(SessionVars sVars) {
		return sVars.keyNameValue(Form.ButtonType.PREVIEW.toString(), buttonText.get(Form.ButtonType.PREVIEW));
	}

	public boolean buttonClicked(SessionVars sVars, String buttonName) {
		return sVars.hasParameterKey(buttonName);
	}

	public enum TEXTTYPE {
		NOTINITIALZIED, TEXT, PASSWORD, TEXTAREA
	};

	/**
	 * text box
	 * 
	 * @param internalName
	 * @param size
	 * @param externalLabel
	 * @param value
	 * @param focus
	 * @param disabled
	 * @return
	 * @throws Exception
	 */
	public void textBox(String internalName, int size, String externalLabel, String value, boolean focus,
			boolean disabled) throws Exception {
		String id = Utils.getNextString();
		textPasswordBox(internalName, size, externalLabel, value, focus, disabled, TEXTTYPE.TEXT, 0, 0, 0, id);
		submitClickWithId(id, internalName, "Enter");
	}

	/**
	 * both text and password boxes
	 * 
	 * @param internalName
	 * @param size
	 * @param externalLabel
	 * @param value
	 * @param focus
	 * @param disabled
	 * @param textBox       true if text box, false if password box
	 * @param maxChars      maximum number of characters
	 * @param rows
	 * @param columns
	 * @throws Exception
	 */
	private void textPasswordBox(String internalName, int size, String externalLabel, String value, boolean focus,
			boolean disabled, TEXTTYPE textBox, int maxChars, int rows, int columns, String internalId)
			throws Exception {
		// needsPreviewButton = true;

		if (internalName == null)
			internalName = "";
		if (externalLabel == null)
			externalLabel = "";
		if (internalId == null)
			internalId = "";
		if (value == null)
			value = "";

		// if the box will be in a table
		if (tableDepth > 0) {
			body += "<td>";
			if (!externalLabel.isEmpty())
				// split the label and box into separate columns
				body += externalLabel + "</td><td>";
		} else {
			// precede the text box with the label;
			body += externalLabel;
		}
		switch (textBox) {
		case TEXT:
			body += "<input type=\"text\" ";
			break;
		case PASSWORD:
			body += "<input type=\"password\" ";
			break;
		case TEXTAREA:
			if (maxChars == 0)
				maxChars = 1;
			if (rows == 0)
				rows = 1;
			if (columns == 0)
				columns = 1;
			body += "<textarea maxlength=\"" + maxChars + "\" rows=\"" + rows + "\" cols=\"" + columns + "\" ";
			break;
		default:
			throw new ExceptionCoding("undefined textbox of " + textBox.toString());
		}
		if (!internalName.isEmpty())
			body += "name=\"" + internalName + "\" ";
		if (size > 0)
			body += "size=\"" + size + "\" ";
		if (disabled)
			body += "disabled=\"disabled\" ";
		if (!internalId.isEmpty()) {
			body += "id=\"" + internalId + "\" ";
		}
		switch (textBox) {
		case TEXT:
		case PASSWORD:
			if (!value.isEmpty())
				body += "value=\"" + value + "\" ";
			body += ">";
			break;
		case TEXTAREA:
			body += ">";
			if (!value.isEmpty())
				body += value;
			body += "</textarea>";
			break;
		default:
			throw new ExceptionCoding(textBox.toString());
		}
		if (focus) {
			addFocus(internalName);
		}
		// if (!internalLabel.isEmpty()) {
		// body += "<script>" + "var input = document.getElementById(\""
		// + internalLabel + "\");"
		// + "input.addEventListener(\"keydown\", function(event) {"
		// + "if (event.keyCode === 13) {" + "event.preventDefault();"
		// + "document.getElementById(\"" + internalLabel
		// + "\").click();" + "}});</script>";
		// }
		// if the text box is in a table
		if (tableDepth > 0)
			body += "</td>";

	}

	private void addFocus(String newFocus) throws Exception {
		if (!focusTag.isEmpty() & !newFocus.isEmpty())
			throw new Exception("focus already set");
		focusTag = newFocus;
		body += "<script>document." + FORMNAME + "." + newFocus + ".focus();</script>";
	}

	public void passwordBox(String name, int size, String label, String value, boolean focus) throws Exception {
		textPasswordBox(name, size, label, value, focus, false, TEXTTYPE.PASSWORD, 0, 0, 0, null);
	}

//	public void textAreaBox(String name, int maxChars, String label, String value, boolean focus, boolean disabled,
//			int rows, int columns) throws Exception {
//		textPasswordBox(name, 0, label, value, focus, disabled, TEXTTYPE.TEXTAREA, maxChars, rows, columns, null);
//	}

	public void startMultiSelection(String name, int windowSize) {

		if (tableDepth > 0)
			body += "<td>";
		body += "<select=\"multiple\"";

		if (windowSize > 0)
			body += " size=\"" + windowSize + "\" ";
		if (!name.isEmpty())
			body += "name=\"" + name + "\" ";
		body += ">";
	}

	public void startSingleSelection(String name, int windowSize, boolean onClick) {
		if (tableDepth > 0)
			body += "<td>";
		body += "<select ";
		if (windowSize > 0)
			body += "size=\"" + windowSize + "\" ";
		if (onClick)
			body += onClick();
		if (!name.isEmpty())
			body += "name=\"" + name + "\" ";
		body += ">";

	}

	/**
	 * add a selection to a selection list
	 * 
	 * @param value   labels and values returned upon submission
	 * @param label   shown to the user
	 * @param onClick return immediately
	 */
	public void addSelectionOption(String value, String label) {
		body += "<option ";
		if (!value.isEmpty())
			body += "value=\"" + value + "\" >";
		if (!label.isEmpty())
			body += label;
		body += "</option>";
	}

	public void addMultiSelectionOption(String value, String label, boolean checked) {
		body += "<option ";
		if (!value.isEmpty())
			body += "value=\"" + value + "\" ";
		if (checked)
			body += "selected=\"selected\" ";
		body += ">";
		if (!label.isEmpty())
			body += label;
		body += "</option>";
	}

	public void endSingleSelection() {
		endMultiSelection();
	}

	public void endMultiSelection() {
		body += "</select>";
		if (tableDepth > 0)
			body += "</td>";
	}

	public void startTable() {
		body += "<table cellspacing=\"4\" border=\"3\" >";
		tableDepth++;
		// cellDepth.put(tableDepth, 0);
	}

	public void startInvisibleTable() {
		body += "<table cellspacing=\"4\" border=\"0\" >";
		tableDepth++;
	}

	public void endTable() {
		body += "</table>";
		tableDepth--;
	}

	public void startRow() {
		body += "<tr>";
	}

	public void endRow() {
		body += "</tr>";
	}

	/**
	 * end a cell, start another cell
	 */
	public void endStartCell() {
		body += "</td><td>";
	}

	public void startCell() {
		body += "<td>";
		// cellDepth.put(tableDepth, cellDepth.get(tableDepth)+1);
	}

	public void endCell() {
		body += "</td>";
		// cellDepth.put(tableDepth, cellDepth.get(tableDepth)-1);
	}

	public void startForm(String returnToString) {
		body += FORMELEMENT;
//		addHiddenSubmit();
		// put the return to information in each form
		hiddenField(RETURNTO, returnToString);
		formsSet = true;
	}

	public void addHiddenSubmit() {
		body += "<input type=\"submit\" name=\"" + HIDDENSUBMIT
				+ "\" value=\"Submit\" style=\"position: absolute; left: -9999px; width: 1px; height: 1px;\" tabindex=\"-1\">";
	}

	public void endForm() {
		body += ENDFORMELEMENT;
	}

	public void startDiv() {
		body += "<div>";
	}

	public void endDiv() {
		body += "</div>";
	}

	public void say(String fileName) {
		body += "<EMBED SRC=\"" + fileName + "\" HIDDEN=\"TRUE\" AUTOSTART=\"TRUE\" >";

	}

	public void selectAudioFile(String fileName) {
		body += "<input name=\"" + fileName + "\" type=\"file\" accept=\"audio/*\" >";
	}

	public void startBold() {
		body += "<b>";
	}

	public void endBold() {
		body += "</b>";
	}

	public void title(String value) throws Exception {
		if (!title.isEmpty() && !value.isEmpty())
			throw new Exception("multiple titles");
		if (!value.isEmpty())
			title += value;
	}

	public void rawText(String value) {
		if (tableDepth > 0)
			body += "<td>";
		body += value;
		if (tableDepth > 0)
			body += "</td>";
	}

	public void rawTextInTable(String value) {
		body += "<td>";
		body += value;
		body += "</td>";
	}

	public void rawTextNoTable(String value) {
		body += value;
	}

	public static String lineSeparator = "<br>";

	public void newLine() {
		rawText(lineSeparator);
	}

	public void spanTextColumn(String text, int span) throws Exception {
		if (tableDepth == 0)
			throw new Exception("in table use only");
		body += "<td colspan=\"" + span + "\" >" + text + "</td>";
	}

	public void big(String value) throws Exception {
		if (value.isEmpty())
			throw new Exception("value required");
		body += "<big>" + value + "</big>";
	}

	public void link(String label, String value) {
		body += "<a href=\"" + value + "\" >";
		if (!label.isEmpty())
			body += label;
		body += "</a>";
	}

	public void image(String source, String alt, int width, int height) {
		body += "<img border=\"0\" src=\"" + source + "\" alt=\"" + alt + "\" width=\"" + width + "\"height=\"" + height
				+ "\" >";
	}

	public void validInternalForm() throws Exception {
		if (tableDepth != 0)
			throw new Exception("tableDepth of " + tableDepth);
		if (returnToString == null)
			throw new Exception("returnTo not set");
	}

	public void executeForm(SessionVars sVars, HttpServletRequest request, HttpServletResponse response,
			ServletContext context) {
		try {
			validInternalForm();
		} catch (Exception e) {
			errorToUser(e);
		}
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// try to dump the exception the next time we run
			Internals.logStartupError(e);
		}
		out.print(generateHTML());
		out.flush();
	}

	public String generateHTML() {
		String returnString = "";
		returnString += "<!DOCTYPE html>";
		returnString += "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">";
		returnString += "<link rel=\"stylesheet\" type=\"text/css\" href=\"wires.css\">";
		returnString += "<style type=\"text/css\">big {font-size:1000%;}p {font-size:50%;}</style>";
		returnString += "<title>" + title + "</title>";
		if (!focusTag.isEmpty()) {
			returnString += "<script language=\"javascript\" type=\"text/javascript\">";
			returnString += "window.document." + FORMNAME + "." + focusTag + ".focus();";
			returnString += "</script>";
		}
		// ret += "<style type=\"text/css\">"
		// +
		// "#hiddenSubmit{background-color:white;color:white;border:white;width:0px;}"
		// + "" + "</style>";
		returnString += "</head>";
		if (javaScriptNeeded)
			returnString += "<body onload=\"unstack(undefined)\">";
		else
			returnString += "<body>";
		if (!formsSet)
			returnString += FORMELEMENT;

		// add a preview button that captures the enter key
		// ret +=
		// "<input type=\"submit\" name=\"submitHidden\" value=\"Submit\"
		// id=\"hiddenSubmit\">";
		// ret += "<div style=\"color:#0000FF\">";
		returnString += errorToUser;
		// ret += "</div>";
		returnString += body;
		if (!formsSet)
			returnString += ENDFORMELEMENT;
		if (javaScriptNeeded)
			returnString += "<script src=\"wares.js\"></script>";
		returnString += "</body>";
		returnString += "</html>";
		return returnString;
	}

	static HashMap<Form.ButtonType, String> buttonText;
	static {
		buttonText = new HashMap<Form.ButtonType, String>();
		buttonText.put(Form.ButtonType.CANCEL, "Cancel");
		buttonText.put(Form.ButtonType.CANCELPRODUCTSELECTION, "Cancel Selected Family");
		buttonText.put(Form.ButtonType.SUBMITBUTTON, "Submit");
		buttonText.put(Form.ButtonType.CHANGEDEFAULTGROUP, "Change the Default Member");
		buttonText.put(Form.ButtonType.NOBARCODEUSECAT, "Select from a list of categories.");
		buttonText.put(Form.ButtonType.NOBARCODEFINDPRODUCT, "No barcode. Search rule names.");
		buttonText.put(Form.ButtonType.UPDATE, "Update");
		buttonText.put(Form.ButtonType.GOUPONELEVEL, "Go up one level");
		buttonText.put(Form.ButtonType.EXPAND, "Go down one level");
		buttonText.put(Form.ButtonType.NOBARCODE, "no bar code");
		buttonText.put(Form.ButtonType.SEARCHGROUPS, "Search for an option");
		buttonText.put(Form.ButtonType.RESET, "Reset");
		buttonText.put(Form.ButtonType.PREVIEW, "Preview");
	}

	public void intToSpeech(int orig_number) {
		int thousands = (int) (orig_number / 1000);
		// discard all but least significant three digits
		int number = orig_number - thousands * 1000;
		int hundreds = (int) (number / 100);
		int tens = (int) ((number - hundreds * 100) / 10);
		int ones = number - 100 * hundreds - 10 * tens;

		switch (ones) {
		case 0:
			say("sszero.wav");
			break;
		case 1:
			say("ssone.wav");
			break;
		case 2:
			say("sstwo.wav");
			break;
		case 3:
			say("ssthree.wav");
			break;
		case 4:
			say("ssfour.wav");
			break;
		case 5:
			say("ssfive.wav");
			break;
		case 6:
			say("sssix.wav");
			break;
		case 7:
			say("ssseven.wav");
			break;
		case 8:
			say("sseight.wav");
			break;
		case 9:
			say("ssnine.wav");
			break;
		}

		switch (tens) {
		case 0:
			say("szero.wav");
			break;
		case 1:
			say("sone.wav");
			break;
		case 2:
			say("stwo.wav");
			break;
		case 3:
			say("sthree.wav");
			break;
		case 4:
			say("sfour.wav");
			break;
		case 5:
			say("sfive.wav");
			break;
		case 6:
			say("ssix.wav");
			break;
		case 7:
			say("sseven.wav");
			break;
		case 8:
			say("seight.wav");
			break;
		case 9:
			say("snine.wav");
			break;
		}

		switch (hundreds) {
		case 0:
			// don't speak leading 0
			break;
		case 1:
			say("one.wav");
			break;
		case 2:
			say("two.wav");
			break;
		case 3:
			say("three.wav");
			break;
		case 4:
			say("four.wav");
			break;
		case 5:
			say("five.wav");
			break;
		case 6:
			say("six.wav");
			break;
		case 7:
			say("seven.wav");
			break;
		case 8:
			say("eight.wav");
			break;
		case 9:
			say("nine.wav");
			break;
		}
	}

	public static void stripOverhead(SessionVars sVars) {
		Collection<String> keySet = sVars.getParameterKeys();
		Iterator<String> stepper = keySet.iterator();
		while (stepper.hasNext()) {
			String thisKey = stepper.next();
			if (thisKey.equals(NAME)) {
				stepper.remove();
				continue;
			}
			if (thisKey.equals(Form.ONCLICKBUTTON)) {
				stepper.remove();
				continue;
			}
			if (thisKey.equals("goto login")) {
				stepper.remove();
				continue;
			}
		}
	}

	public static Collection<String> stripButtons(SessionVars sVars) {
		Collection<String> ret = new ArrayList<String>();
		Collection<String> keySet = sVars.getParameterKeys();
		Iterator<String> stepper = keySet.iterator();
		while (stepper.hasNext()) {
			String thisKey = stepper.next();
			if (thisKey.equals("buttonName")) {
				for (String thisButton : sVars.getParameterValues(thisKey)) {
					ret.add(thisButton);
				}
				stepper.remove();
				continue;
			}
			if (thisKey.equals(Form.ONCLICKBUTTON)) {
				stepper.remove();
				continue;
			}
		}
		return ret;
	}

	/**
	 * strip all parameters from the parameter map
	 * 
	 * @param map
	 * @return
	 */
	public static void stripAll(SessionVars sVars, String[] myParams) {
		Collection<String> keySet = sVars.getParameterKeys();
		Iterator<String> stepper = keySet.iterator();
		while (stepper.hasNext()) {
			String[] splitKey = stepper.next().split(" ");
			for (String thisParam : myParams) {
				// remove "addDescription 6" key when myParams contains
				// "addDescription"
				if (splitKey[0].equals(thisParam)) {
					stepper.remove();
				}
			}
		}
	}

	/**
	 * add an onclick attribute
	 * 
	 * @return
	 */
	public String onClick() {
		return "onclick=\"document." + FORMNAME + ".submit();\" ";
	}

	/**
	 * 
	 * @param returnedKey   returned as a parameter key
	 * @param returnedValue returned as a parameter value
	 * @param label         displayed to the user
	 * @param onClick       return when clicked
	 * @throws Exception
	 */
	public void radioButton(String returnedKey, String returnedValue, String label, boolean onClick, boolean checked)
			throws Exception {
		if (returnedKey == null)
			throw new Exception("null returnedKey");
		if (returnedValue == null)
			throw new Exception("null returnedValue");
		if (label == null)
			throw new Exception("null label");
		// radio button. when clicked, submit also "pressed"
		body += "<input type=\"radio\" ";
		if (!returnedKey.isEmpty())
			body += "name=\"" + returnedKey + "\" ";
		if (!returnedValue.isEmpty())
			body += "value=\"" + returnedValue + "\" ";
		if (checked)
			body += "checked=\"checked\" ";
		if (onClick)
			body += onClick();
		// body += "onclick=\"" + Form.ONCLICKBUTTON.toString()
		// + ".click()\" ";
		body += ">";
		if (!label.isEmpty())
			body += label;
	}

	/**
	 * 
	 * @param returnedKey   returned as a parameter key
	 * @param returnedValue returned as a parameter value
	 * @param label         displayed to the user
	 */
	public void checkBox(String returnedKey, String returnedValue, String label, boolean checked) {
		body += "";
		if (tableDepth > 0) {
			body += "<td>";
		}
		body += "<input type=\"checkbox\" ";
		if (!returnedKey.isEmpty())
			body += "name=\"" + returnedKey + "\" ";
		if (!returnedValue.isEmpty())
			body += "value=\"" + returnedValue + "\" ";
		if (checked)
			body += "checked ";
		body += ">";
		if (!label.isEmpty())
			body += label;
		if (tableDepth > 0)
			body += "</td>";
		else
			body += lineSeparator;
	}

	public void standardFormStart() {
		submitButton();
		resetButton();
		cancelButton();
	}

	public FormsArray submitButton(String label, String name) {

		if (tableDepth > 0)
			body += "<td>";
		else
			body += "<div class=\"looseButton\">";
		if (formsSet)
			body += FORMELEMENT;
		body += "<input type=\"submit\" ";
		if (!name.isEmpty())
			body += "name=\"" + name + "\" ";
		if (!label.isEmpty())
			body += "value=\"" + label + "\" ";
		body += ">";
		if (formsSet)
			body += ENDFORMELEMENT;
		if (tableDepth > 0)
			body += "</td>";
		else
			body += "</div>";
		return this;
	}

	public void submitButtonIgnoreTable(String label, String name) {

		body += "<input type=\"submit\" ";
		if (!name.isEmpty())
			body += "name=\"" + name + "\" ";
		if (!label.isEmpty())
			body += "value=\"" + label + "\" ";
		body += ">";
	}

	/**
	 * Creates a submit button shown to the user as @parem label. Inserts a script
	 * so that when the enter key is pressed while the cursor is in the idToWatch
	 * field, the client will return @param key = @param label in the input.
	 * 
	 * @param idToWatch
	 * @param name
	 * @param value
	 */
	public void submitClickWithId(String idToWatch, String name, String value) {
		String enterId = Utils.getNextString();

//		body += "<style>";
//		body += "input.hidden{";
//		body += "display:none;";
//		body += "}</style>";

		body += "<input type=\"submit\" id=\"" + enterId + "\" ";
		body += "name=\"" + "notUsed" + "\" ";
		body += "value=\"" + value + "\" ";
//		body += "class=\"hidden\"";
		body += ">";

		body += "<script>";
		body += "var input = document.getElementById(\"" + idToWatch + "\");";
		body += "input.addEventListener(\"keyup\",function(event){";
		body += "if (event.keyCode === 13) {";
		body += "event.preventDefault();";
		body += "document.getElementById(\"" + enterId + "\").click();";
		body += "}});</script>";
	}

	/**
	 * add a cancel button
	 * 
	 * @return
	 */
	public void cancelButton() {
		submitButton(buttonText.get(Form.ButtonType.CANCEL), Form.ButtonType.CANCEL.toString());
	}

	public void submitButton() {
		submitButton(buttonText.get(Form.ButtonType.SUBMITBUTTON), Form.ButtonType.SUBMITBUTTON.toString());
	}

	public void resetButton() {
		submitButton(buttonText.get(Form.ButtonType.RESET), Form.ButtonType.RESET.toString());
	}

	public void previewButton() {
		// submitButton(buttonText.get(Form.ButtonType.PREVIEW),
		// Form.ButtonType.PREVIEW.toString());
	}

	public void goUpButton() {
		submitButton(buttonText.get(Form.ButtonType.GOUPONELEVEL), Form.ButtonType.GOUPONELEVEL.toString());
	}

	public void cancelFamilySelectionButton() {
		submitButton(buttonText.get(Form.ButtonType.CANCELPRODUCTSELECTION),
				Form.ButtonType.CANCELPRODUCTSELECTION.toString());
	}

	public void expandButton() {
		submitButton(buttonText.get(Form.ButtonType.EXPAND), Form.ButtonType.EXPAND.toString());
	}

	public boolean hiddenSubmitFound(SessionVars sVars) {
		return sVars.hasParameterKey(HIDDENSUBMIT);
	}

	public void noBarcodeUseCatButton() {
		submitButton(buttonText.get(Form.ButtonType.NOBARCODEUSECAT), Form.ButtonType.NOBARCODEUSECAT.toString());
	}

	public void searchMembersButton() {
		submitButton(buttonText.get(Form.ButtonType.SEARCHGROUPS), Form.ButtonType.SEARCHGROUPS.toString());
	}

	public void changeDefaultMemberButton() {
		submitButton(buttonText.get(Form.ButtonType.CHANGEDEFAULTGROUP), Form.ButtonType.CHANGEDEFAULTGROUP.toString());
	}

	public void noBarcodeButton() {
		submitButton(buttonText.get(Form.ButtonType.NOBARCODE), Form.ButtonType.NOBARCODE.toString());
	}

	/**
	 * return a hashmap of ids and values given the name of the field. For example
	 * <"FIELDNAME 3", "new description"> comes back as <"3", "new description">
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> getIdsAndValues(SessionVars sVars, String lookFor) throws Exception {
		HashMap<String, String> ret = new HashMap<String, String>();
		Collection<String> keySet = sVars.getParameterKeys();
		String[] splitKey;
		String[] values;
		Iterator<String> stepper = keySet.iterator();
		while (stepper.hasNext()) {
			String thisKey = stepper.next();
			splitKey = null;
			values = null;
			splitKey = thisKey.split(" ");
			// if the key is not 2 words separated by a space
			if (splitKey.length != 2)
				continue;
			// if the first word is not what we're looking for
			if (!splitKey[0].equals(lookFor))
				continue;
			values = sVars.getParameterValues(thisKey);
			// pull the found key from the map
			stepper.remove();
			// discard empty parameters
			if (values == null)
				continue;
			if (values.length > 1)
				throw new Exception("for " + lookFor + " multiple values of " + values);
			// if (values[0].length() == 0)
			// continue;
			// save the id and the value
			ret.put(splitKey[1], values[0]);
		}
		return ret;
	}

	/**
	 * returns all keys and values of the keys starting with lookFor. for example,
	 * <lookFor 3 bob, <mary 23>,<39 apple>, <lookFor tom, moe larry curly> comes
	 * back as [3 bob][mary 23],[3 bob][39 apple], [tom, moe larry curly]. if there
	 * are no values associated with the key, the key is not returned
	 * 
	 * @param sVars
	 * @param lookFor
	 * @return
	 */
	public static ArrayList<DoubleStringArray> getMultiIdsAndValues(SessionVars sVars, String lookFor) {
		ArrayList<DoubleStringArray> ret = new ArrayList<DoubleStringArray>();
		for (String thisKey : sVars.getParameterKeys()) {
			String[] splitKey = thisKey.split(" ");
			// if the first word in the key is not what we're looking for
			if (!splitKey[0].equals(lookFor))
				continue;
			String[] values = sVars.getParameterValues(thisKey);
			if (values.length == 0)
				continue;
			if (values[0].length() == 0)
				continue;
			// drop the lookFor at the beginning
			String tmp = "";
			for (int i = 1; i < splitKey.length; i++)
				tmp += splitKey[i] + " ";
			// trim the trailing space
			tmp = tmp.trim();
			splitKey = tmp.split(" ");
			// save the ids and values
			DoubleStringArray thisOne = null;
			// for each value
			for (String thisString : values) {
				thisOne = new DoubleStringArray();
				for (int i = 0; i < splitKey.length; i++)
					thisOne.firstArray.add(splitKey[i]);
				thisOne.secondArray.add(thisString);
			}
			ret.add(thisOne);
		}
		return ret;
	}

	/**
	 * <"lookFor", "Value 7"> comes back as <"Value", "7">
	 * 
	 * @param map
	 * @param lookFor
	 * @return
	 */
	public static HashMap<String, String> getIdsOnValues(SessionVars sVars, String lookFor) {
		HashMap<String, String> ret = new HashMap<String, String>();
		Collection<String> keySet = sVars.getParameterKeys();
		String[] splitValue;
		String[] values;
		Iterator<String> stepper = keySet.iterator();
		while (stepper.hasNext()) {
			String thisKey = stepper.next();
			// if it's not what we're looking for
			if (!thisKey.equals(lookFor))
				continue;
			values = null;
			values = sVars.getParameterValues(thisKey);
			// pull the found key from the map
			stepper.remove();

			// if there's not corresponding value
			if (values == null)
				continue;

			for (String value : values) {
				splitValue = value.split(" ");
				// if the key is not 2 words separated by a space
				if (splitValue.length != 2)
					continue;

				// save the id and the value
				ret.put(splitValue[1], splitValue[0]);
			}
		}
		return ret;
	}

	/**
	 * get the radio keys that have an appended Id. <"RADIO 7", ""> gets returned as
	 * <"7">
	 * 
	 * @param map
	 * @param lookFor
	 * @return
	 */
	public static Collection<String> getIdsAndNoValues(HashMap<String, String[]> map, String lookFor) {
		Collection<String> ret = new ArrayList<String>();
		Collection<String> keySet = map.keySet();
		String[] splitKey;
		Iterator<String> stepper = keySet.iterator();
		while (stepper.hasNext()) {
			String thisKey = stepper.next();
			splitKey = null;
			splitKey = thisKey.split(" ");
			// if the key is not 2 words separated by a space
			if (splitKey.length != 2)
				continue;
			// if the first word is not what we're looking for
			if (!splitKey[0].equals(lookFor))
				continue;
			// pull the found key from the map
			stepper.remove();

			// save the id and the value
			ret.add(splitKey[1]);
		}
		return ret;
	}

	/**
	 * <"lookFor", "moe, larry, curly"> becomes <"moe", "larry", "curly" >
	 * 
	 * @param map
	 * @param lookFor
	 * @return
	 */
	public static Collection<String> getIdsNoValues(SessionVars sVars, String lookFor) {
		Collection<String> ret = new ArrayList<String>();
		if (sVars.getParameterKeys().contains(lookFor)) {
			String[] tmp = sVars.getParameterValues(lookFor);
			for (String thisStr : tmp) {
				ret.add(thisStr);
			}
		}
		// map.remove(lookFor);
		/*
		 * Collection<String> keySet = map.keySet(); Iterator<String> stepper =
		 * keySet.iterator(); while (stepper.hasNext()) { if
		 * (lookFor.equals(stepper.next())) { // save the id and the value
		 * ret.add(map.get(lookFor)[0]); // pull the found key from the map
		 * stepper.remove(); } }
		 */
		return ret;
	}

	public void errorToUser(Exception e) {
		errorToUser += Internals.dumpExceptionToString(e, lineSeparator);
//		StringWriter errors = new StringWriter();
//		e.printStackTrace(new PrintWriter(errors));
//		errorToUser += errors.toString();
//		errorToUser += separator;
	}

//	public void errorToUser(Throwable e) {
////		errorToUser += e.getMessage() + separator;
//		StringWriter errors = new StringWriter();
//		e.printStackTrace(new PrintWriter(errors));
//		String[] es = errors.toString().split("\r\n\tat ");
//		errorToUser += es[0] + lineSeparator;
//		errorToUser += es[1] + lineSeparator;
//		errorToUser += es[3] + lineSeparator;
//		errorToUser += es[4] + lineSeparator;
//	}

	public void errorToUser(String e) {
		errorToUser += e;
	}
	
	public void errorToUser(ArrayList<String>errors) {
		for (String s : errors)
			errorToUser += s;
	}

	public enum STATUS {
		/**
		 * initial state
		 */
		NOTINITIALIZED,

		/**
		 * clean up form and return to dispatch
		 */
		BACKTODISPATCH, SELECTEDNEW
	}

	public STATUS status;

	/**
	 * append formToBeAppended to an existing form
	 * 
	 * @param formToBeAppended
	 * @return
	 * @throws Exception
	 */
	public FormsArray addAll(FormsArray formToBeAppended) throws Exception {
		if (formToBeAppended == null)
			throw new ExceptionCoding("null ret");
		body += formToBeAppended.body;
		if (!formToBeAppended.status.equals(STATUS.NOTINITIALIZED))
			status = formToBeAppended.status;
		// newForm = ret.newForm;
		if (!formToBeAppended.focusTag.isEmpty())
			addFocus(formToBeAppended.focusTag);
		tableDepth += formToBeAppended.tableDepth;
		errorToUser += formToBeAppended.errorToUser;
		if (!title.isEmpty() && formToBeAppended.title.isEmpty())
			throw new Exception("multiple titles detected");
		title += formToBeAppended.title;
		if (formToBeAppended.returnToString != null && returnToString != null)
			throw new Exception("multiple returnToSet, " + formToBeAppended.returnToString + ", " + returnToString);

		if (formToBeAppended.returnToString != null)
			returnToString = formToBeAppended.returnToString;
		formsSet = formsSet || formToBeAppended.formsSet;
		if (formToBeAppended.javaScriptNeeded)
			javaScriptNeeded = true;
		return this;
	}

	/**
	 * standard calling sequence for a single form
	 *
	 */
	public void standardForm() {
		// add a cancel button
		// newLine();
		cancelButton();
		return;
	}

	public void hiddenField(String name, String value) {
		body += "<input type=\"hidden\" ";
		if (!name.isEmpty())
			body += "name=\"" + name + "\" ";
		if (!value.isEmpty())
			body += "value=\"" + value + "\" ";
		body += ">";
	}

	public void setReturnTo(SmartForm form, SessionVars sVars) throws Exception {
		if (returnToString != null)
			throw new ExceptionCoding("returnTo is already set");
		if (form.getReturnTo() == null)
			throw new ExceptionCoding(form.toString() + " has not overridden getReturnToString");
		// save for use in the form element
		returnToString = form.getReturnTo();
		sVars.setApToRun(returnToString, form);
//		if (value == null || value.isEmpty())
//			throw new ExceptionCoding("empty value");
//		else
		hiddenField(RETURNTO, returnToString);
	}

	public void setJavaScriptNeeded() {
		javaScriptNeeded = true;
	}
}
