package comTest.forms;

import com.forms.IdAndString;
import com.forms.SearchArray;

import junit.framework.TestCase;

public class TestIdAndString extends TestCase {

	public void testSearchList() {
		// no change if no search string for one record
		SearchArray noSearchString = new SearchArray();
		noSearchString.addAll(oneRecord());
		noSearchString.searchList("");
		assertTrue(noSearchString.equals(oneRecord()));

		// nothing found
		SearchArray onlyOneRecord = new SearchArray();
		onlyOneRecord.addAll(oneRecord());
		onlyOneRecord.searchList(NOTINONERECORD);
		assertTrue(onlyOneRecord.isEmpty());
		
		// one record should match
		onlyOneRecord = new SearchArray();
		onlyOneRecord.addAll(oneRecord());
		onlyOneRecord.searchList(ISINONERECORD);
		assertTrue(onlyOneRecord.size()==1);
		// highlighted
		assertTrue(onlyOneRecord.get(0).string.trim().equals(SearchArray.highlight(ISINONERECORD)));
		
		// sorted if no search string. nothing should be highlighted
		SearchArray twoRecords = new SearchArray();
		twoRecords.addAll(twoRecords());
		twoRecords.searchList("");
		assertTrue(twoRecords.get(0).id==twoRecords().get(1).id);
		assertTrue(twoRecords.get(0).string.equals(twoRecords().get(1).string));
		assertTrue(twoRecords.get(1).id==twoRecords().get(0).id);
		assertTrue(twoRecords.get(1).string.equals(twoRecords().get(0).string));
		
		// a string in both records. BOTHRECORDS in each record should be highlighted. should be sorted
		twoRecords = new SearchArray();
		twoRecords.addAll(twoRecords());
		twoRecords.searchList(BOTHRECORDS);
		assertTrue(twoRecords.size()==2);
		// records swapped
		assertTrue(twoRecords.get(0).id==twoRecords().get(1).id);
		assertTrue(twoRecords.get(1).id==twoRecords().get(0).id);
		
		twoRecords = new SearchArray();
		twoRecords.addAll(twoRecords());
		twoRecords.searchList(ONERECORD);
		assertTrue(twoRecords.size()==1);
		// right record made it through
		assertTrue(twoRecords.get(0).id==ONERECORDID);
	}

	private String NOTINONERECORD = "asfa";
	private String ISINONERECORD = "fersure";
	private SearchArray oneRecord() {
		SearchArray ret = new SearchArray();
		IdAndString tmp = new IdAndString();
		tmp.id = 1;
		tmp.string = ISINONERECORD;
		ret.add(tmp);
		return ret;
	}

	private String BOTHRECORDS = "both";
	private String ONERECORD = "zero";
	private int ONERECORDID = 17;
	private SearchArray twoRecords() {
		SearchArray ret = new SearchArray();
		IdAndString tmp = new IdAndString();
		// create an unsorted list (zero should follow two)
		tmp.id = ONERECORDID;
		tmp.string = BOTHRECORDS + " "+ONERECORD;
		ret.add(tmp);
		tmp = new IdAndString();
		tmp.id = 2;
		tmp.string = BOTHRECORDS + " two";
		ret.add(tmp);
		return ret;
	}

}
