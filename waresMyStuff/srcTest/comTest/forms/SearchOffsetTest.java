package comTest.forms;

import static org.junit.Assert.*;

import org.junit.Test;

import com.forms.IdAndStrings;
import com.forms.SearchOffset;

public class SearchOffsetTest {

	@Test
	public void testGetNewOffsetInitialValues() {
		SearchOffset so = new SearchOffset();
		if (so.getNewOffset(IdAndStrings.DIRECTION.FORWARD) != 0)
			fail("expected and offset of zero");
		so = new SearchOffset();
		if (so.getNewOffset(IdAndStrings.DIRECTION.REVERSE) != 0)
			fail("expected and offset of zero");
	}

	@Test
	public void testGetNewOffsetForward() {
		SearchOffset so = new SearchOffset();

		// no records found, expect no change
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 0);
		if (so.getNewOffset(IdAndStrings.DIRECTION.FORWARD) != 0)
			fail("expected an offset of zero");

		// one record found. expect no change as the first and last record will be
		// displayed on this pass. No need to move the offset forward
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 1);
		if (so.getNewOffset(IdAndStrings.DIRECTION.FORWARD) != 0)
			fail("expected an offset of zero");

		// start fresh
		// find DISPLAYSIZE number of records. The offset should move.
		so = new SearchOffset();
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, IdAndStrings.DISPLAYSIZE);
		if (so.getNewOffset(IdAndStrings.DIRECTION.FORWARD) != IdAndStrings.DISPLAYSIZE)
			fail("expected an offset of IdAndStrings.DISPLAYSIZE");
	}

	@Test
	public void testGetNewOffsetReverse() {
		SearchOffset so = new SearchOffset();

		// no records found, expect no change
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 0);
		if (so.getNewOffset(IdAndStrings.DIRECTION.REVERSE) != 0)
			fail("expected an offset of zero");

		// 1 records found in reverse, expect offset of 0 (not go negative)
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 1);
		if (so.getNewOffset(IdAndStrings.DIRECTION.REVERSE) != 0)
			fail("expected an offset of zero");

		// DISPLAYSIZE records found in reverse, expect offset of 0 (not go negative)
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, IdAndStrings.DISPLAYSIZE);
		if (so.getNewOffset(IdAndStrings.DIRECTION.REVERSE) != 0)
			fail("expected an offset of zero");

		// go forward DISPLAYSIZE records
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, IdAndStrings.DISPLAYSIZE);
		// expect a new reverse offset of 0
		if (so.getNewOffset(IdAndStrings.DIRECTION.FORWARD) != IdAndStrings.DISPLAYSIZE)
			fail("expected a forward offset of DISPLAYSIZE");

		// go forward another DISPLAYSIZE records
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, IdAndStrings.DISPLAYSIZE);
		if (so.getNewOffset(IdAndStrings.DIRECTION.FORWARD) != IdAndStrings.DISPLAYSIZE*2)
			fail("expected an offset of DISPLAYSIZE * 2");
		
		// ask for the offset if we were to go backwards
		if (so.getNewOffset(IdAndStrings.DIRECTION.REVERSE) != IdAndStrings.DISPLAYSIZE)
			fail("expected an offset of DISPLAYSIZE");
	}

	@Test
	public void testShowNextButton() {
		SearchOffset so = new SearchOffset();
		if (so.showPreviousButton())
			fail("showPreviousButton not initialized to false");

		///////////////////////////////////////////////////////
		// forward, initial searches (lastOffset == 0)
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 0);
		if (so.showNextButton())
			fail("showNextButton should be false after 0 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 1);
		if (so.showNextButton())
			fail("showNextButton should be false after 1 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, IdAndStrings.DISPLAYSIZE);
		if (!so.showNextButton())
			fail("showNextButton should be true after DISPLAYSIZE records found");

		///////////////////////////////////////////////////////
		// reverse, initial searches (lastOffset == 0)
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 0);
		if (so.showNextButton())
			fail("showNextButton should be false after 0 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 1);
		if (so.showNextButton())
			fail("showNextButton should be false after 1 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, IdAndStrings.DISPLAYSIZE);
		if (!so.showNextButton())
			fail("showNextButton should be true after DISPLAYSIZE records found");

		///////////////////////////////////////////////////////
		// forward, after initial searches (lastOffset > 0)
		// pretend the last search yielded DISPLAYSIZE records
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, IdAndStrings.DISPLAYSIZE);
		// move the lastOffset counter
		so.getNewOffset(IdAndStrings.DIRECTION.FORWARD);

		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 0);
		if (so.showNextButton())
			fail("showNextButton should be false after 0 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 1);
		if (so.showNextButton())
			fail("showNextButton should be false after 1 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, IdAndStrings.DISPLAYSIZE);
		if (!so.showNextButton())
			fail("showNextButton should be true after DISPLAYSIZE records found");

		///////////////////////////////////////////////////////
		// reverse, initial searches (lastOffset == 0)
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 0);
		if (so.showNextButton())
			fail("showNextButton should be false after 0 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 1);
		if (so.showNextButton())
			fail("showNextButton should be false after 1 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, IdAndStrings.DISPLAYSIZE);
		if (!so.showNextButton())
			fail("showNextButton should be true after DISPLAYSIZE records found");

	}

	@Test
	public void testShowPreviousButton() {
		SearchOffset so = new SearchOffset();
		if (so.showPreviousButton())
			fail("showPreviousButton not initialized to false");

		///////////////////////////////////////////////////////
		// forward, initial searches (lastOffset == 0)
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 0);
		if (so.showPreviousButton())
			fail("showPreviousButton should be false after 0 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 1);
		if (so.showPreviousButton())
			fail("showPreviousButton should be false after 1 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, IdAndStrings.DISPLAYSIZE);
		if (so.showPreviousButton())
			fail("showPreviousButton should be false after DISPLAYSIZE records found");

		///////////////////////////////////////////////////////
		// reverse, initial searches (lastOffset == 0)
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 0);
		if (so.showPreviousButton())
			fail("showPreviousButton should be false after 0 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 1);
		if (so.showPreviousButton())
			fail("showPreviousButton should be false after 1 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, IdAndStrings.DISPLAYSIZE);
		if (so.showPreviousButton())
			fail("showPreviousButton should be false when offset is 0");

		///////////////////////////////////////////////////////
		// forward, after initial searches (lastOffset > 0)
		// pretend the last search yielded DISPLAYSIZE records
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, IdAndStrings.DISPLAYSIZE);
		// move the lastOffset counter
		so.getNewOffset(IdAndStrings.DIRECTION.FORWARD);

		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 0);
		if (!so.showPreviousButton())
			fail("showPreviousButton should be true after 0 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, 1);
		if (!so.showPreviousButton())
			fail("showPreviousButton should be true after 1 records found");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.FORWARD, IdAndStrings.DISPLAYSIZE);
		if (!so.showPreviousButton())
			fail("showPreviousButton should be true after DISPLAYSIZE records found");

		///////////////////////////////////////////////////////
		// reverse, initial searches (lastOffset == 0)
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 0);
		if (!so.showPreviousButton())
			fail("showPreviousButton should be true");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, 1);
		if (!so.showPreviousButton())
			fail("showPreviousButton should be true");
		so.storeLastSearchResults(IdAndStrings.DIRECTION.REVERSE, IdAndStrings.DISPLAYSIZE);
		if (!so.showPreviousButton())
			fail("showPreviousButton should be true after DISPLAYSIZE records found");
	}

}
