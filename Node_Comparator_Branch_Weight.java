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
public class Node_Comparator_Branch_Weight implements Comparator {

	public int compare(Object o1, Object o2) {

		double Node1Item = ((Node) o1).weight;
		double Node2Item = ((Node) o2).weight;

		if (Node1Item < Node2Item)
			return 1;
		else if (Node1Item > Node2Item)
			return -1;
		else
			return 0;
	}

}
