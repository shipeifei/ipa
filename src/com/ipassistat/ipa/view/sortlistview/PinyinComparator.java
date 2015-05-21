package com.ipassistat.ipa.view.sortlistview;

import java.util.Comparator;

public class PinyinComparator<T> implements Comparator<SortModel<T>> {

	public int compare(SortModel<T> o1, SortModel<T> o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
