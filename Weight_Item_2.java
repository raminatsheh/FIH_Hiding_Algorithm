package fih;
import java.util.ArrayList;
import java.util.Collections;

public class Weight_Item_2 {
	
	public ArrayList<Item> items_in_sensitive_itemsets = new ArrayList<Item>();
	
	//Preprocess function to populate items and their weights
	private void preprocess_items(){
		
		for(Itemset temp_itemset : Main.main_fp_tree.list_of_sensitive_itemsets){
			for (Integer temp_Integer : temp_itemset.get_items()){
				int temp_counter = 0;
				for (Item temp_item : this.items_in_sensitive_itemsets){
					if (temp_item.get_value().intValue() == temp_Integer.intValue()){
						temp_item.set_count_itemsets(temp_item.get_count_itemsets()+1);
						temp_counter = 1;
					}
				}
				if (temp_counter == 0){
					Item new_item = new Item(temp_Integer);
					new_item.set_count_itemsets(1);
					this.items_in_sensitive_itemsets.add(new_item);
				}
		}
		}
		
		//call find support for each item in the list
		find_count_of_items();
		
		//calculate weights for each item
		for (Item temp_item : this.items_in_sensitive_itemsets){
			temp_item.set_weight(temp_item.get_count()*temp_item.get_count_itemsets());
		}
	
	}
	
	private void find_count_of_items() {
		
		for (Node temp_Node:Main.main_fp_tree.list_of_all_nodes){
			
			for (Item temp_item:this.items_in_sensitive_itemsets){
				if (temp_Node.get_item() == temp_item.get_value().intValue()){
					temp_item.set_count(temp_item.get_count().intValue()+temp_Node.get_support());
				}
			}
		}
	}

	public Item get_IH(){
		Relevant_Items_Comparator temp_comparator = new Relevant_Items_Comparator();
		Collections.sort(this.items_in_sensitive_itemsets, temp_comparator );
		return this.items_in_sensitive_itemsets.get(0);
	}
	
	public Item get_IH_given_BH(Node leaf){
		Relevant_Items_Comparator temp_comparator = new Relevant_Items_Comparator();
		Collections.sort(this.items_in_sensitive_itemsets, temp_comparator );
		for (int i = 0; i < this.items_in_sensitive_itemsets.size(); i++){
			ArrayList<Node> parents_temp = leaf.return_all_parents_till_root(leaf);
			for (Node temp_node : parents_temp){
				if (temp_node.get_item() == this.items_in_sensitive_itemsets.get(i).get_value().intValue()){
					return this.items_in_sensitive_itemsets.get(i);
				}
			}
		}
		
		return null;
	}
	
	//adjust the count of items in databsae after removal
	public void adjust_count_of_item(Item removed_item){
		for (Item temp_item : this.items_in_sensitive_itemsets){
			if (temp_item.get_value().intValue() == removed_item.get_value().intValue()){
				temp_item.set_count(temp_item.get_count()-1);
				temp_item.set_weight(temp_item.get_count()*temp_item.get_count_itemsets());
			}
		}
	}
	
	
	//adjusts the weight for the itemsets that became non sensitive
	
	public void adjust_weights_for_One_Itemset(Itemset passed_itemset){
		
		for (Item temp_item : this.items_in_sensitive_itemsets){
			for (Integer temp_integer : passed_itemset.get_items()){
				if (temp_item.get_value().intValue() == temp_integer.intValue()){
					temp_item.set_weight(temp_item.get_count()*(temp_item.get_count_itemsets()-1));
				}
			}
		}
	}
	
	
 	public Weight_Item_2(){
		this.preprocess_items();
	}
	

}
