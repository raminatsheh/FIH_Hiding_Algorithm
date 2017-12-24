/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fih;

import java.util.ArrayList;

/**
 * 
 * @author Rami
 */
public class Itemset {

	// the number of items within the current itemset is recorded
	public int number_of_items;

	// the support level of this itemset is recorded
	public int support_level;

	// the items that are icnluded within this itemset is recorded
	public ArrayList<Integer> items = new ArrayList<Integer>();

	// the type of this itemset is specified, wither sensitive or not
	private String type;

	public Boolean set_type(String new_type) {
		this.type = new_type;
		if (this.type.contains(new_type))
			return true;
		return false;
	}

	public String get_type() {
		return this.type;
	}

	public Itemset(int new_support_level, ArrayList<Integer> new_items,
			String new_type) {
		this.support_level = new_support_level;
		this.items = new_items;
		this.type = new_type;
	}

	public Boolean Contains(Itemset new_itemset) {
		for (Integer a : new_itemset.items) {
			if (this.items.contains(a))
				;
			else
				return false;
		}
		return true;
	}

	public ArrayList<Integer> get_items() {
		return this.items;

	}

}
