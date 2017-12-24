package fih;
import java.util.ArrayList;
import java.util.Collections;

public class Weight_Branch_1 {

	public Node get_BH(){
		Node_Comparator_Sen_Itemsets temp_comparator = new Node_Comparator_Sen_Itemsets(); 
		Collections.sort(Main.main_fp_tree.list_of_leaf_nodes, temp_comparator );
		return Main.main_fp_tree.list_of_leaf_nodes.get(0);
	}
	
	
	
}
