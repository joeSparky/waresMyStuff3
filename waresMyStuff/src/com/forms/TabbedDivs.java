package com.forms;

import java.util.ArrayList;

import com.db.SessionVars;
import com.security.ExceptionCoding;

public class TabbedDivs extends ArrayList<TabbedDiv> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1766075114989277098L;

	// set true when we find the first highlighted tab. throws an error on the
	// second
	boolean foundHighlighted;
	SessionVars sVars;
	SmartForm form;
//	String returnTo = null;

	public TabbedDivs(SessionVars sVars, SmartForm form) throws ExceptionCoding {
		foundHighlighted = false;
//		if (returnTo == null || returnTo.isEmpty())
//			throw new ExceptionCoding("empty returnTo");
//		this.returnTo = returnTo;
		this.sVars = sVars;
		this.form = form;
	}

//	@Override
//	public boolean add(TabbedDiv td) {
//		try {
//			td.sanity();
//		} catch (Exception e) {
//			Internals.dumpException(e);
//		}
//		return super.add(td);
//	}

	public FormsArray dumpDivsIntoForm() throws Exception {
		FormsArray ret = new FormsArray();
		ret.setReturnTo(form, sVars);

		// put in all the class divs before the overlay divs
		for (TabbedDiv td : this) {
			switch (td.divType) {
			case NORMALTAB:
				String tmp = "<div class=\"tab";
				if (td.isHighlighted) {
					if (foundHighlighted)
						throw new ExceptionCoding("at least 2 highlighted tabs");
					foundHighlighted = true;
					tmp += " highlight";
				}
				if (td.isALink)
					tmp += " link";
				tmp += "\"  id=\"tabbed" + td.uniqueId + "\" onclick=\"unstack('" + td.uniqueId + "')\">" + td.tabLabel
						+ "</div>";
				ret.rawText(tmp);
				break;
			case NOTINITIALIZED:
				throw new ExceptionCoding("not initialized");
//				break;
			case STARTNEWROW:
				ret.rawText("<div class=\"newLine\"></div>");
				break;

			}

		}
		// put in all the overlays
		for (TabbedDiv td : this) {
			switch (td.divType) {
			case NORMALTAB:
				ret.rawText("<div id=\"overlay" + td.uniqueId + "\" class=\"overlay\">");
				ret.addAll(td.getForm());
				ret.rawText("</div>");
				break;
			case NOTINITIALIZED:
				throw new ExceptionCoding("not initialized");
//				break;
			case STARTNEWROW:
				break;

			}

		}
		ret.setJavaScriptNeeded();
		return ret;
	}

}
