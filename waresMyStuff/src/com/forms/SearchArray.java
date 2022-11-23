package com.forms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/**
 * Given a collection of MyObjcts of the same class, build a list of IDAndString for the operator to select from.
 * The list is reduced by a search string.
 * @author Joe
 *
 */
public class SearchArray extends ArrayList<IdAndString> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void searchListReverse() {
		Collections.sort(this, new SearchComparatorReverse());
	}
//	MyObjectsArray moa = null;
//	public SearchArray(MyObjects searchThis) {
//		
//		for (MyObject obj : searchThis)
//			add(IdAndString.toIdAndString(obj));
//	}
	
	/**
	 * apply searchString to SearchArray.
	 * @param searchString
	 */
	public void searchList(String searchString) {
		// if no search string, return the original member list
		if (searchString.isEmpty()) {
			allSorted();
			return;
		}

//		SearchArray tmp = new SearchArray(this);
		// use a set to eliminate duplicate key words
		// Set<String> keyWords = new HashSet<String>();
		searchString = searchString.toLowerCase();

		/**
		 * id to count
		 */
		Map<Integer, Integer> id2count = new HashMap<Integer, Integer>();
		/**
		 * id to string
		 */
		Map<Integer, String> id2string = new HashMap<Integer, String>();

		// build a hash of the IDs and match counts
		// build a hash of the IDs and name for the matches
		for (IdAndString thisElement : this) {
			for (String word : thisElement.string.toLowerCase().split(" ")) {
				for (String searchWord : searchString.split(" ")) {
					if (word.contains(searchWord)) {
						// if the count is already in id2count
						if (id2count.containsKey(thisElement.id)) {
							id2count.put(thisElement.id,
									id2count.get(thisElement.id) + 1);
						} else {
							id2count.put(thisElement.id, 1);
						}
						if (!id2string.containsKey(thisElement.id))
							id2string.put(thisElement.id, thisElement.string);
					}
				}
			}
		}

		// generate a sorted list of unique counts
		Set<Integer> counts = new HashSet<Integer>();
		for (int thisCount : id2count.values()) {
			counts.add(thisCount);
		}
		ArrayList<Integer> sortedCounts = new ArrayList<Integer>();
		for (int thisCount : counts) {
			sortedCounts.add(thisCount);
		}

		Collections.sort(sortedCounts, Collections.reverseOrder());
		
		// build the return list
		this.clear();
		for (int thisCount : sortedCounts) {
			Iterator<Map.Entry<Integer, Integer>> stepper = id2count.entrySet()
					.iterator();
//			SearchArray forEachCount = new SearchArray();
			while (stepper.hasNext()) {
				Map.Entry<Integer, Integer> entry = stepper.next();
				if (entry.getValue() == thisCount) {
					IdAndString stringId = new IdAndString();
					stringId.id = entry.getKey();
					stringId.string = id2string.get(entry.getKey());
					add(stringId);
					stepper.remove();
				}
			}
			// sort the strings for this count
			Collections.sort(this, new SearchComparator());
			// put them onto tmp
//			tmp.addAll(forEachCount);
		}

		// copy the rebuilt array onto this instance
//		this.clear();
//		for (int i = 0; i < tmp.size(); i++) {
//			this.add(i, tmp.get(i));
//		}

		// highlight the matching words
		for (int i = 0; i < size(); i++) {

			String rebuilt = "";
			// for each word in the string
			for (String thisPart : get(i).string.split(" ")) {
				boolean highlightWord = false;
				String lower = thisPart.toLowerCase();
				// for each search word
				for (String thisSearch : searchString.split(" ")) {
					if (lower.contains(thisSearch))
						highlightWord = true;
				}
				if (highlightWord)
					rebuilt += highlight(thisPart) + " ";
				else
					rebuilt += thisPart + " ";
			}
			IdAndString tmpOne = new IdAndString();
			tmpOne.id = get(i).id;
			tmpOne.string = rebuilt;
			this.set(i, tmpOne);
		}
		return;
	}

	public static String highlight(String inWord) {
		return inWord.toUpperCase();
		// return "<b>" + inWord + "</b>";
	}

	private void allSorted() {
		Collections.sort(this, new SearchComparator());
	}

	public boolean equals(SearchArray target) {
		for (int i = 0; i < target.size(); i++) {
			if (target.get(i).id != this.get(i).id)
				return false;
			if (!target.get(i).string.equals(this.get(i).string))
				return false;
		}
		return true;
	}

	public class SearchComparator implements Comparator<IdAndString> {

		@Override
		public int compare(IdAndString arg0, IdAndString arg1) {
			return arg0.string.compareTo(arg1.string);
		}
	}

	public class SearchComparatorReverse implements Comparator<IdAndString> {

		@Override
		public int compare(IdAndString arg0, IdAndString arg1) {
			return arg1.string.compareTo(arg0.string);
		}
	}

	// public void replenish(SearchTarget level, String searchString) {
	// clear();
	// Iterator<MyObject> itr = level.getObjects().iterator();
	// while (itr.hasNext()) {
	// MyObject location = itr.next();
	// IdAndString tmp = new IdAndString();
	// tmp.string = location.getInstanceName();
	// tmp.id = location.id;
	// this.add(tmp);
	// }
	// searchList(searchString);
	// }
}
