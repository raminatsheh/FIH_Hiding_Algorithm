package fih;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class Relevant_Items_Comparator implements Comparator {
	public int compare(Object o1, Object o2) {

		int Item1 = ((Item) o1).get_weight().intValue();
		int Item2 = ((Item) o2).get_weight().intValue();

		if (Item1 < Item2)
			return 1;
		else if (Item1 > Item2)
			return -1;
		else
			return 0;
	}

}
