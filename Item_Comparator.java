/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fih;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;


/**
 *
 * @author Rami
 */
public class Item_Comparator implements Comparator {

    public FP_Tree passed_tree = fih.Main.main_fp_tree;
    public int compare(Object o1, Object o2) {


        int Index1 = 0;
        Index1 = passed_tree.Items_In_Tree.indexOf(o1);
        int Index2 = 0;
        Index2 = passed_tree.Items_In_Tree.indexOf(o2);

        int Item1 = 0;
        Item1 = passed_tree.Support_Items_In_Tree.get(Index1).intValue();
        int Item2 = 0;
        Item2 = passed_tree.Support_Items_In_Tree.get(Index2).intValue();

        if (Item1 <  Item2) return 1;
        else if (Item1 > Item2) return -1;
        else if(Item1 == Item2){
        	if (Index1 < Index2) return 1;
        	else if(Index1 > Index2) return -1;
        }
        return 0;

    }

}
