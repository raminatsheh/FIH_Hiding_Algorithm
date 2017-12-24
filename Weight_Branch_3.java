package fih;
import java.util.ArrayList;
import java.util.Collections;

public class Weight_Branch_3 {

	public Node get_BH(){
		Main.main_fp_tree.populate_level_weight_in_leaves();
		Node_Comparator_Branch_Weight temp_comparator = new Node_Comparator_Branch_Weight(); 
		Collections.sort(Main.main_fp_tree.list_of_leaf_nodes, temp_comparator );
		return Main.main_fp_tree.list_of_leaf_nodes.get(0);
	}
	
	
	public Weight_Branch_3(){

	}
	
}
