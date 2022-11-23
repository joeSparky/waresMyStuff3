package com.forms;

/**
 * manage the mysql offset for searches
 * 
 * @author joe
 *
 */
public class SearchOffset {
	public SearchOffset() {
		clear();
	}

	IdAndStrings.DIRECTION lastDirection;
	public int lastOffset;
	int numberOfRecordsFound;
	boolean showNextButton;
	boolean showPreviousButton;

	public void clear() {
		lastDirection = IdAndStrings.DIRECTION.FORWARD;
		lastOffset = 0;
		numberOfRecordsFound = 0;
		showNextButton = false;
		showPreviousButton = false;
	}

	public void storeLastSearchResults(IdAndStrings.DIRECTION lastDirection, int numberOfRecordsFound) {
		this.lastDirection = lastDirection;
		this.numberOfRecordsFound = numberOfRecordsFound;
		showNextButton = false;
		showPreviousButton = false;
		switch (lastDirection) {
		case FORWARD:
			if (numberOfRecordsFound == IdAndStrings.DISPLAYSIZE)
				showNextButton = true;
			if (lastOffset > 0)
				showPreviousButton = true;
			break;
		case REVERSE:

			if (numberOfRecordsFound == IdAndStrings.DISPLAYSIZE)
				showNextButton = true;
			if (lastOffset > 0)
				showPreviousButton = true;
			break;
		case UNKNOWN:
			break;
		default:
			break;

		}
	}

	public int getNewOffset(IdAndStrings.DIRECTION direction) {
		switch (direction) {
		case FORWARD:
			if (numberOfRecordsFound == IdAndStrings.DISPLAYSIZE)
				lastOffset = lastOffset + numberOfRecordsFound;
			break;
		case REVERSE:
			lastOffset = Math.max(0, lastOffset - IdAndStrings.DISPLAYSIZE);
			break;
		case UNKNOWN:
			break;
		}
		return lastOffset;
	}

	public boolean showNextButton() {
		return showNextButton;
	}

	public boolean showPreviousButton() {
		return showPreviousButton;
	}
}
