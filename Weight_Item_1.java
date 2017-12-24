package fih;
import java.util.ArrayList;
import java.util.Collections;

public class Weight_Item_1 {
	
	public ArrayList<Item> items_in_sensitive_itemsets = new ArrayList<Item>();
	
	//Preprocess function to populate items and their weights
	private void preprocess_items(){
		
		for(Itemset temp_itemset : Main.main_fp_tree.list_of_sensitive_itemsets){
			for (Integer temp_Integer : temp_itemset.get_items()){
				int temp_counter = 0;
				for (Item temp_item : this.items_in_sensitive_itemsets){
					if (temp_item.get_value().intValue() == temp_Integer.intValue()){
						temp_item.set_weight(temp_item.get_weight()+temp_itemset.number_of_items);
						temp_counter = 1;
					}
				}
				if (temp_counter == 0){
					Item new_item = new Item(temp_Integer);
					new_item.set_weight(temp_itemset.number_of_items);
					this.items_in_sensitive_itemsets.add(new_item);
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
	
	//adjusts the weight for the itemsets that became non sensitive
	
	public void adjust_weights_for_One_Itemset(Itemset passed_itemset){
		for (Item temp_item : this.items_in_sensitive_itemsets){
			for (Integer temp_integer : passed_itemset.get_items()){
				if (temp_item.get_value().intValue() == temp_integer.intValue()){
					temp_item.set_weight(temp_item.get_weight() - passed_itemset.number_of_items);
				}
			}
		}
	}
	
	
 	public Weight_Item_1(){
		this.preprocess_items();
	}
	

}
