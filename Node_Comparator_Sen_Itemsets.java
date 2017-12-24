/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fih;

import java.util.Comparator;

/**
 * 
 * @author Rami
 */
public class Node_Comparator_Sen_Itemsets implements Comparator {

	public int compare(Object o1, Object o2) {

		int Node1Item = ((Node) o1).get_sensitive_itemsets().size();
		int Node2Item = ((Node) o2).get_sensitive_itemsets().size();

		if (Node1Item < Node2Item)
			return 1;
		else if (Node1Item > Node2Item)
			return -1;
		else
			return 0;
	}

}
