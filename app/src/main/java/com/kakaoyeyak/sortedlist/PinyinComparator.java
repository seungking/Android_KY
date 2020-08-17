package com.kakaoyeyak.sortedlist;

import com.kakaoyeyak.Item_Friend;

import java.util.Comparator;

public class PinyinComparator implements Comparator<Item_Friend> {

	public int compare(Item_Friend o1, Item_Friend o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return -1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return 1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
