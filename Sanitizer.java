package fih;
import java.util.ArrayList;

public class Sanitizer {
	
	int Combination = 1;
	int min_support_level = Main.min_support_level;
	public long start_time;
	boolean indicator = false;
	public int item_counter = 0;
	// ArrayList of itemsets that became non frequent
	public ArrayList<Itemset> Sen_Itemsets_became_non_frequent = new ArrayList<Itemset>();
	
	public void initiate(int combination){
		this.Combination = combination;
		
		if (this.Combination == 1){
			start_time = System.currentTimeMillis();
			run_combination_1();
		}
		if (this.Combination == 5){
			start_time = System.currentTimeMillis();
			run_combination_5();
		}
		if (this.Combination == 7){
			start_time = System.currentTimeMillis();
			run_combination_7();
		}	
		if (this.Combination == 2){
			start_time = System.currentTimeMillis();
			run_combination_2();
		}
		if (this.Combination == 3){
			start_time = System.currentTimeMillis();
			run_combination_3();
		}
		if (this.Combination == 4){
			start_time = System.currentTimeMillis();
			run_combination_4();
		}
		if (this.Combination == 6){
			start_time = System.currentTimeMillis();
			run_combination_6();
		}
		if (this.Combination == 8){
			start_time = System.currentTimeMillis();
			run_combination_8();
		}
		
		Main.items_removed = item_counter;
	}
	
	private boolean exceeded_permitted_time() {
		long temp = System.currentTimeMillis();
		if ((temp-start_time)>Main.run_time_limit){
			
			System.out.println("Time exceeded limit, " + ((temp-start_time)/1000.0) + " seconds");
			System.out.println("Itemse Removed So Far " + item_counter);
			Main.items_removed = item_counter;
			Main.algorithm_ran = false;
			return true;
		}
		return false;
	}

	private void run_combination_1() {
		Weight_Item_1 item_weight = new Weight_Item_1();
		Weight_Branch_2 branch_weight = new Weight_Branch_2();
		this.Sen_Itemsets_became_non_frequent.clear();
		
		Item IH; 
		Node BH;
		

		
		while (((Main.main_fp_tree.list_of_sensitive_itemsets.size()>0)) && (indicator == false)){
			long temp_time1 = System.currentTimeMillis();
			IH = item_weight.get_IH();
			long temp_time2 = System.currentTimeMillis();
			Main.choosing_item_time = Main.choosing_item_time+temp_time2-temp_time1;
			
			

			
			temp_time1 = System.currentTimeMillis();
			BH = branch_weight.get_BH(IH);
			temp_time2 = System.currentTimeMillis();
			Main.choosing_branch_time = Main.choosing_branch_time+temp_time2-temp_time1;

//			if((IH.get_value() == 38)&&(BH.get_item() == 1994)){
//				System.out.print("Break point reached");
//			}
			System.out.println("Item removed from branch with main node test" +IH.value.intValue()+", "+BH.get_item()+", "+BH.get_main_node().get_item());
			this.Sen_Itemsets_became_non_frequent = Main.main_fp_tree.sanitize_node_in_branch(IH, BH);
			
			System.out.println("Number of sensitive Itemsets that became infrequent" + "  "+ this.Sen_Itemsets_became_non_frequent.size());

			for (Itemset temp_itemset :this.Sen_Itemsets_became_non_frequent){
				if (temp_itemset.items.contains(48) && temp_itemset.items.contains(147)){
					System.out.print("check Here Rami "+temp_itemset.support_level);
				}
				//System.out.println("Itemset affected  "+temp_itemset.get_items());
				//System.out.println("new support level  "+temp_itemset.support_level);
				item_weight.adjust_weights_for_One_Itemset(temp_itemset);
			}
			
			this.Sen_Itemsets_became_non_frequent.clear();
					
			//Check the time if it exceeded the limit, exit if that i sthe case and print a message
			indicator = exceeded_permitted_time();
				
				
			item_counter++;
			
		}
		
		
	}
	
	


	private void run_combination_5() {
		Weight_Item_1 item_weight = new Weight_Item_1();
		Weight_Branch_1 branch_weight = new Weight_Branch_1();
		this.Sen_Itemsets_became_non_frequent.clear();
		
		Item IH; 
		Node BH;
		
		while ((!Main.main_fp_tree.list_of_sensitive_itemsets.isEmpty()) && (indicator == false)){
			
			long temp_time1 = System.currentTimeMillis();
			BH = branch_weight.get_BH();
			long temp_time2 = System.currentTimeMillis();
			Main.choosing_branch_time = Main.choosing_branch_time+temp_time2-temp_time1;

			
			temp_time1 = System.currentTimeMillis();
			IH = item_weight.get_IH_given_BH(BH);
			temp_time2 = System.currentTimeMillis();
			Main.choosing_item_time = Main.choosing_item_time+temp_time2-temp_time1;


			
			System.out.println("Item removed from branch with main node" +IH.value.intValue()+", "+BH.get_item()+", "+BH.get_main_node().get_item());
			this.Sen_Itemsets_became_non_frequent = Main.main_fp_tree.sanitize_node_in_branch(IH, BH);
			System.out.println("Number of sensitive Itemsets that became infrequent" + "  "+ this.Sen_Itemsets_became_non_frequent.size());

			for (Itemset temp_itemset :this.Sen_Itemsets_became_non_frequent){
				item_weight.adjust_weights_for_One_Itemset(temp_itemset);
			}
			
			this.Sen_Itemsets_became_non_frequent.clear();
					
			
			//Check the time if it exceeded the limit, exit if that i sthe case and print a message
			indicator = exceeded_permitted_time();
			
			
			item_counter++;
		}
		
		
	}

	private void run_combination_7() {
		Weight_Item_1 item_weight = new Weight_Item_1();
		Weight_Branch_3 branch_weight = new Weight_Branch_3();
		this.Sen_Itemsets_became_non_frequent.clear();
		
		Item IH; 
		Node BH;
		
		while ((!Main.main_fp_tree.list_of_sensitive_itemsets.isEmpty()) && (indicator == false)){
			
			long temp_time1 = System.currentTimeMillis();
			BH = branch_weight.get_BH();
			long temp_time2 = System.currentTimeMillis();
			Main.choosing_branch_time = Main.choosing_branch_time+temp_time2-temp_time1;
			
			temp_time1 = System.currentTimeMillis();
			IH = item_weight.get_IH_given_BH(BH);
			temp_time2 = System.currentTimeMillis();
			Main.choosing_item_time = Main.choosing_item_time+temp_time2-temp_time1;
			
			System.out.println("Item removed from branch with main node" +IH.value.intValue()+", "+BH.get_item()+", "+BH.get_main_node().get_item());
			this.Sen_Itemsets_became_non_frequent = Main.main_fp_tree.sanitize_node_in_branch(IH, BH);
			System.out.println("Number of sensitive Itemsets that became infrequent" + "  "+ this.Sen_Itemsets_became_non_frequent.size());

			for (Itemset temp_itemset :this.Sen_Itemsets_became_non_frequent){
				item_weight.adjust_weights_for_One_Itemset(temp_itemset);
			}
			
			this.Sen_Itemsets_became_non_frequent.clear();
					
			
			//Check the time if it exceeded the limit, exit if that i sthe case and print a message
			indicator = exceeded_permitted_time();
			
			item_counter++;
		}
		
		
	}
	
	
	private void run_combination_2() {
		Weight_Item_2 item_weight = new Weight_Item_2();
		Weight_Branch_2 branch_weight = new Weight_Branch_2();
		this.Sen_Itemsets_became_non_frequent.clear();
		
		Item IH; 
		Node BH;
		
		while ((!Main.main_fp_tree.list_of_sensitive_itemsets.isEmpty()) && (indicator == false)){
			

			long temp_time1 = System.currentTimeMillis();
			IH = item_weight.get_IH();
			long temp_time2 = System.currentTimeMillis();
			Main.choosing_item_time = Main.choosing_item_time+temp_time2-temp_time1;
			

			temp_time1 = System.currentTimeMillis();
			BH = branch_weight.get_BH(IH);
			temp_time2 = System.currentTimeMillis();
			Main.choosing_branch_time = Main.choosing_branch_time+temp_time2-temp_time1;
			
			System.out.println("Item removed from branch with main node" +IH.value.intValue()+", "+BH.get_item()+", "+BH.get_main_node().get_item());
			this.Sen_Itemsets_became_non_frequent = Main.main_fp_tree.sanitize_node_in_branch(IH, BH);
			System.out.println("Number of sensitive Itemsets that became infrequent" + "  "+ this.Sen_Itemsets_became_non_frequent.size());

			for (Itemset temp_itemset :this.Sen_Itemsets_became_non_frequent){
				item_weight.adjust_weights_for_One_Itemset(temp_itemset);
			}
			
			this.Sen_Itemsets_became_non_frequent.clear();
					
			item_weight.adjust_count_of_item(IH);
			
			//Check the time if it exceeded the limit, exit if that i sthe case and print a message
			indicator = exceeded_permitted_time();
			
			item_counter++;
			
		}
		
	}
	
	private void run_combination_3() {
		Weight_Item_1 item_weight = new Weight_Item_1();
		Weight_Branch_4 branch_weight = new Weight_Branch_4();
		this.Sen_Itemsets_became_non_frequent.clear();
		
		Item IH; 
		Node BH;
		int temp = 0;
		while ((!Main.main_fp_tree.list_of_sensitive_itemsets.isEmpty()) && (indicator == false)){
		
			long temp_time1 = System.currentTimeMillis();
			IH = item_weight.get_IH();
			long temp_time2 = System.currentTimeMillis();
			Main.choosing_item_time = Main.choosing_item_time+temp_time2-temp_time1;

			temp_time1 = System.currentTimeMillis();
			BH = branch_weight.get_BH(IH);
			temp_time2 = System.currentTimeMillis();
			Main.choosing_branch_time = Main.choosing_branch_time+temp_time2-temp_time1;
			
			if((IH.get_value() == 38)&&(BH.get_item() == 10446)){
				temp++;
				if(temp == (152-89)){
					System.out.print("Break point reached");
				}
			}
			
			System.out.println("Item removed from branch with main node" +IH.value.intValue()+", "+BH.get_item()+", "+BH.get_main_node().get_item());
			this.Sen_Itemsets_became_non_frequent = Main.main_fp_tree.sanitize_node_in_branch(IH, BH);
			System.out.println("Number of sensitive Itemsets that became infrequent" + "  "+ this.Sen_Itemsets_became_non_frequent.size());

			for (Itemset temp_itemset :this.Sen_Itemsets_became_non_frequent){
				item_weight.adjust_weights_for_One_Itemset(temp_itemset);
			}
			
			this.Sen_Itemsets_became_non_frequent.clear();
			
			//Check the time if it exceeded the limit, exit if that i sthe case and print a message
			indicator = exceeded_permitted_time();
			
			item_counter++;
			
		}
		
		
	}
	
	private void run_combination_4() {
		Weight_Item_2 item_weight = new Weight_Item_2();
		Weight_Branch_4 branch_weight = new Weight_Branch_4();
		this.Sen_Itemsets_became_non_frequent.clear();
		
		Item IH; 
		Node BH;
		
		while ((!Main.main_fp_tree.list_of_sensitive_itemsets.isEmpty()) && (indicator == false)){
			

			long temp_time1 = System.currentTimeMillis();
			IH = item_weight.get_IH();
			long temp_time2 = System.currentTimeMillis();
			Main.choosing_item_time = Main.choosing_item_time+temp_time2-temp_time1;
			temp_time1 = System.currentTimeMillis();
			BH = branch_weight.get_BH(IH);
			temp_time2 = System.currentTimeMillis();
			Main.choosing_branch_time = Main.choosing_branch_time+temp_time2-temp_time1;

			
			System.out.println("Item removed from branch with main node" +IH.value.intValue()+", "+BH.get_item()+", "+BH.get_main_node().get_item());
			this.Sen_Itemsets_became_non_frequent = Main.main_fp_tree.sanitize_node_in_branch(IH, BH);
			System.out.println("Number of sensitive Itemsets that became infrequent" + "  "+ this.Sen_Itemsets_became_non_frequent.size());

			for (Itemset temp_itemset :this.Sen_Itemsets_became_non_frequent){
				item_weight.adjust_weights_for_One_Itemset(temp_itemset);
			}
			
			this.Sen_Itemsets_became_non_frequent.clear();
					
			item_weight.adjust_count_of_item(IH);
			
			//Check the time if it exceeded the limit, exit if that i sthe case and print a message
			indicator = exceeded_permitted_time();
			
			item_counter++;
		}
		
	}
	
	private void run_combination_6() {
		Weight_Item_2 item_weight = new Weight_Item_2();
		Weight_Branch_1 branch_weight = new Weight_Branch_1();
		this.Sen_Itemsets_became_non_frequent.clear();
		
		Item IH; 
		Node BH;
		
		while ((!Main.main_fp_tree.list_of_sensitive_itemsets.isEmpty()) && (indicator == false)){
			
			long temp_time1 = System.currentTimeMillis();
			BH = branch_weight.get_BH();
			long temp_time2 = System.currentTimeMillis();
			Main.choosing_branch_time = Main.choosing_branch_time+temp_time2-temp_time1;
			
			temp_time1 = System.currentTimeMillis();
			IH = item_weight.get_IH_given_BH(BH);
			temp_time2 = System.currentTimeMillis();
			Main.choosing_item_time = Main.choosing_item_time+temp_time2-temp_time1;

			
			System.out.println("Item removed from branch with main node" +IH.value.intValue()+", "+BH.get_item()+", "+BH.get_main_node().get_item());
			this.Sen_Itemsets_became_non_frequent = Main.main_fp_tree.sanitize_node_in_branch(IH, BH);
			System.out.println("Number of sensitive Itemsets that became infrequent" + "  "+ this.Sen_Itemsets_became_non_frequent.size());

			for (Itemset temp_itemset :this.Sen_Itemsets_became_non_frequent){
				item_weight.adjust_weights_for_One_Itemset(temp_itemset);
			}
			
			this.Sen_Itemsets_became_non_frequent.clear();
					
			item_weight.adjust_count_of_item(IH);
			
			//Check the time if it exceeded the limit, exit if that i sthe case and print a message
			indicator = exceeded_permitted_time();
			
			item_counter++;
		}
		
	}
	
	
	private void run_combination_8() {
		Weight_Item_2 item_weight = new Weight_Item_2();
		Weight_Branch_3 branch_weight = new Weight_Branch_3();
		this.Sen_Itemsets_became_non_frequent.clear();
		
		Item IH; 
		Node BH;
		
		while ((!Main.main_fp_tree.list_of_sensitive_itemsets.isEmpty()) && (indicator == false)){
			
			long temp_time1 = System.currentTimeMillis();
			BH = branch_weight.get_BH();
			long temp_time2 = System.currentTimeMillis();
			Main.choosing_branch_time = Main.choosing_branch_time+temp_time2-temp_time1;
			
			temp_time1 = System.currentTimeMillis();
			IH = item_weight.get_IH_given_BH(BH);
			temp_time2 = System.currentTimeMillis();
			Main.choosing_item_time = Main.choosing_item_time+temp_time2-temp_time1;

			
			System.out.println("Item removed from branch with main node" +IH.value.intValue()+", "+BH.get_item()+", "+BH.get_main_node().get_item());
			this.Sen_Itemsets_became_non_frequent = Main.main_fp_tree.sanitize_node_in_branch(IH, BH);
			System.out.println("Number of sensitive Itemsets that became infrequent" + "  "+ this.Sen_Itemsets_became_non_frequent.size());

			for (Itemset temp_itemset :this.Sen_Itemsets_became_non_frequent){
				item_weight.adjust_weights_for_One_Itemset(temp_itemset);
			}
			
			this.Sen_Itemsets_became_non_frequent.clear();
					
			item_weight.adjust_count_of_item(IH);
			
			//Check the time if it exceeded the limit, exit if that i sthe case and print a message
			indicator = exceeded_permitted_time();
			
			item_counter++;
		}
		
	}
}
