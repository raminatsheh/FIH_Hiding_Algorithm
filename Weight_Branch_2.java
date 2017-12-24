package fih;
import java.util.ArrayList;
import java.util.Collections;

public class Weight_Branch_2 {

	public Node get_BH(Item IH){
		
		
		//find all nodes in branch and test if they contain IH and adjust weight

		for (Node leaf_node:Main.main_fp_tree.list_of_leaf_nodes){
			leaf_node.weight = 0;
			ArrayList<Itemset> sensitive_itemsets = leaf_node.get_sensitive_itemsets();

			for(Itemset temp_itemset:sensitive_itemsets){
				boolean status_temp = false;
				for (Integer temp_integer:temp_itemset.get_items()){
					if ((IH.value.intValue() == temp_integer.intValue())&& !status_temp){
						status_temp = true;
						leaf_node.weight = leaf_node.weight+1;
					}
				}
				status_temp = false;
			}
			
			
			
		}
		
		Node_Comparator_Branch_Weight temp_comparator = new Node_Comparator_Branch_Weight(); 
		Collections.sort(Main.main_fp_tree.list_of_leaf_nodes, temp_comparator );
		return Main.main_fp_tree.list_of_leaf_nodes.get(0);
	}
	
	public Weight_Branch_2(){
		Main.main_fp_tree.populate_level_weight_in_leaves();
	}
	
	
}
