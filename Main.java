package fih;



//import Apriori;
//import Itemset;




import java.util.ArrayList;
import java.io.*;
import java.util.Collections;

/**
 * 
 * @author Rami
 */
public class Main {

	/**
	 * Defining the main objects that will be used:
	 */

	// Number of Sensitive itemsets
	public static int Number_of_Sensitive_Itemsets = 0;
	
	// Define correlation for items in sensitive itemsets
	
	public static double correlation = 0;
	public static double average_itemset_length = 0;
	
	// Debug the number of lines when creating the list of items
	int Debug__Lines = 0;
	
	// database file path name
	public static String database_file_path = "C:\\Users\\rami\\workspace\\FIH\\retail\\Thirty_sen_itemsets_200\\";
	
	//Path to sensitive itemsets:
	public static String sensitive_itemsets_path = database_file_path + "sensitive_itemsests.txt";

	// the main FP Tree that will be used to populate with the relevant
	// transactions
	public static FP_Tree main_fp_tree;// = new FP_Tree(Number_of_Sensitive_Itemsets, sensitive_itemsets_path);

	// the minimum support level, integer value
	public static int min_support_level = 88;
	
	//Combination
	public static int combination = 1;
	
	// the relative transaction and sanitized veriosn lists
	public static ArrayList<Itemset> sanitized_relative_transactions = new ArrayList<Itemset>();
	public static ArrayList<Itemset> relative_transactions = new ArrayList<Itemset>();
	
	//Define the itemsets that will be stored for improved reconstruction of sanitized transactions
	public static ArrayList<Itemset> sanitized_relative_transactions_improved;

	// list of itemsets in relative data base and sanitized version
	public static ArrayList<Itemset> sanitized_list_of_itemsets = new ArrayList<Itemset>();
	public static ArrayList<Itemset> relative_list_of_itemsets = new ArrayList<Itemset>();

	static ArrayList<Itemset> list_of_items_in_database  = new ArrayList<Itemset>();

	static ArrayList<Itemset> apriori_original_data = new ArrayList<Itemset>();
	public static String log_file_name = "Log_File.csv";

	private static int number_of_itemsets_lost = 0;
	
	// Define timers
	public static long over_all_time;
	public static long building_FP_Tree_time;
	public static long sanitizing_FP_Tree_time;
	public static long choosing_item_time;
	public static long choosing_branch_time;
	public static long temp1_time;
	public static long temp2_time;
	public static long run_time_limit = 72000000; //7200000
	public static int number_of_transactions_modified = 0;

	public static int max_support = 0;
	public static int min_support = 9999999;
	public static double average_support = 0;
	
	public static boolean algorithm_ran = true;
	public static boolean run_apriori_on_FP_Tree = true;
	
	public static int items_removed = 0;

	private static int total_number_of_itemsets =0;


		
	
	
	/*
	// Define some sensitive itemsets for testing
	public ArrayList<Integer> Integer_Arraylist_Sensitive1 = new ArrayList<Integer>();
	public ArrayList<Integer> Integer_Arraylist_Sensitive2 = new ArrayList<Integer>();
	public ArrayList<Integer> Integer_Arraylist_Sensitive3 = new ArrayList<Integer>();
	public ArrayList<Integer> Integer_Arraylist_Sensitive4 = new ArrayList<Integer>();
	public ArrayList<Integer> Integer_Arraylist_Sensitive5 = new ArrayList<Integer>();
	*/ 
	
	// items that are relevant
	//public ArrayList<Integer> list_of_relevant_items = new ArrayList<Integer>();

	// number of lines in the database
	public int Number_Of_Lines = 0;
	// database file name
	// public String database_file_name = "Database.csv";

	public String database_file_name = "current_database.txt";
	
	public String original_FP_Tree = "FP_Tree.txt";
	
	public String sanitized_relative_database = "relevant_transactions_after_sanitization.txt";
	
	public String relative_transactions_database = "relevant_transactions.txt";
	
	



	public int number_of_itemsets = 0;

	// main code for
	//@SuppressWarnings("unchecked")
	public void main() {
		

		Main.over_all_time = System.currentTimeMillis();
		// get the list of items and their support
		this.populate_item_ArrayList(Main.database_file_path,
				this.database_file_name);

		// Generate the FP Tree for
		Main.temp1_time = System.currentTimeMillis();
		this.generate_fp_tree(Main.database_file_path, this.database_file_name);
		Main.temp2_time = System.currentTimeMillis();
		Main.building_FP_Tree_time = (long) ((temp2_time-temp1_time)/1000.0);
		Number_of_Sensitive_Itemsets = Main.main_fp_tree.list_of_sensitive_itemsets.size();

		// Sort the items in the leaf ArrayList
		Node_Comparator_Main_Node Node_Compare = new Node_Comparator_Main_Node();
		Collections.sort(main_fp_tree.list_of_leaf_nodes, Node_Compare);
		

		// calculate Correlation
		calculate_correlation_of_sen_itemsets();

		// test sanitize function
		//Item temp = new Item(9);
		//main_fp_tree.sanitize_node_in_branch(temp, main_fp_tree.list_of_leaf_nodes.get(1));
		
		// Create an object of class Sanitizer
		Sanitizer my_sanitizer = new Sanitizer();
		
		//append to main log
		this.append_results_to_log("databse, number of sensitive itemsets, combination, number of itemsets hidden, number of items removed, Overall Time, FP_Tree Generation Time, Choosing Item Time, Choosing Branch Time, Correlation, Average Sensitive Itemset Size, Average Support for Itemsets, Maximum Support for Itemset, Minimum Support for Itemset, Number of transactions modified, Sensitive Itemsets, Number Of Leaves,FP_Tree number of Nodes,  FP_Tree number of items, Number of transactions in FP_Tree ");

		// Call functions to sanitize the FP_Tree
		System.out.println("starting the sanitization process");
		Main.temp1_time = System.currentTimeMillis();
		my_sanitizer.initiate(combination);
		Main.temp2_time = System.currentTimeMillis();
		Main.sanitizing_FP_Tree_time = (long) ((temp2_time-temp1_time)/1000.0);
		
		long end_time = System.currentTimeMillis();
		Main.over_all_time = (long) ((end_time-this.over_all_time)/1000.0);
		
		if (Main.algorithm_ran){
		Main.choosing_branch_time = (long) (Main.choosing_branch_time/1000.0);
		Main.choosing_item_time = (long) (Main.choosing_item_time/1000.0);
		
		
		// Print Original FP_Tree
		//System.out.println("printing the original tree to file");
		//this.print_fp_tree(Main.database_file_path, this.original_FP_Tree);
		
		// Release resources to allow for more memory
		Main.apriori_original_data = null;
		Main.sanitized_list_of_itemsets = null;
		Main.relative_list_of_itemsets = null;
		

			if (Main.run_apriori_on_FP_Tree){
				
				// print relative transaction
				print_transactions(Main.database_file_path, this.relative_transactions_database, Main.relative_transactions);
	
				// reconstruct sanitized relative database
				System.out.println("reconstructing sanitized database from FP-Tree");
				//reconstruct_sanitized_relative_database();
				reconstruct_sanitized_relative_database_improved();
				// print sanitized relative transactions
				//print_transactions(Main.database_file_path, this.sanitized_relative_database, Main.sanitized_relative_transactions);
				print_transactions(Main.database_file_path, this.sanitized_relative_database, Main.sanitized_relative_transactions_improved);
		
			
				//Find number of transactions modified
				
				find_number_of_transactions_modified();
				
				// run apriori on sanitized and original relative databases
				System.out.println("run apriori on both databases (sanitized and non sanitized)");
		
				double support_level_relative_sanitized =  (double)min_support_level/(double)Main.relative_transactions.size();
				try {
					String temp = Main.database_file_path + this.sanitized_relative_database;
					Main.sanitized_list_of_itemsets = this.find_all_itemsets(temp, String.valueOf(support_level_relative_sanitized), false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				double support_level_relative = (double)min_support_level/(double)Main.sanitized_relative_transactions_improved.size();
				try {
					String temp = Main.database_file_path + this.relative_transactions_database;
					Main.relative_list_of_itemsets = this.find_all_itemsets(temp, String.valueOf(support_level_relative), false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// read apriori data from original databse
				
				String apriori_data_path = this.database_file_path+"apriori_data.txt";
				
				try {
					apriori_original_data = this.read_all_itemsets(apriori_data_path);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				// find missing itemsets that were frequent and got lost		
				Adjust_relative_Itemsets();
				
				//find lost frequent itemsets
				find_lost_frequent_itemsets();
				
				// Count how many items were removed
				
				int items_removed = count_items_removed(Main.sanitized_relative_transactions_improved, Main.relative_transactions);
				System.out.println("number of items removed: "+items_removed);
				
				
				System.out.println("Senseitive itemsets path: "+ Main.sensitive_itemsets_path);
				System.out.println("Min Support level: "+ Main.min_support_level);
				System.out.println("Combination: "+ Main.combination);
				System.out.println("Number of nonsensitive frequent itemsets: " + Main.apriori_original_data.size());
				System.out.println("Correlation for sensitive itemsets: "+ correlation);
				
				System.out.println("number of items removed: "+items_removed);
				String temp_output = database_file_path + "," + Main.Number_of_Sensitive_Itemsets + ","+ combination + ","+ Double.toString(Main.number_of_itemsets_lost) +"," + items_removed+"," +Main.over_all_time+","+Main.building_FP_Tree_time+","+Main.choosing_item_time+","+Main.choosing_branch_time+","+Main.correlation+","+Main.average_itemset_length  + ","+Main.average_support+","+Main.max_support+","+Main.min_support+","+Main.number_of_transactions_modified+ ","+Main.sensitive_itemsets_path+","+Main.main_fp_tree.get_number_of_leaves()+","+Main.main_fp_tree.get_number_of_nodes()+","+Main.main_fp_tree.get_number_of_items()+","+Main.relative_transactions.size();                                                                                                                 
				this.append_results_to_log(temp_output);
			}
			else{
				String temp_output = database_file_path + "," + Main.Number_of_Sensitive_Itemsets + ","+ combination + ","+ "empty" +"," + Main.items_removed+"," + "empty" +","+Main.building_FP_Tree_time+","+Main.choosing_item_time+","+Main.choosing_branch_time+","+Main.correlation+","+Main.average_itemset_length + ","+Main.average_support+","+Main.max_support+","+Main.min_support+","+Main.number_of_transactions_modified+Main.sensitive_itemsets_path+Main.main_fp_tree.get_number_of_leaves()+","+Main.main_fp_tree.get_number_of_nodes()+","+Main.main_fp_tree.get_number_of_items()+","+Main.relative_transactions.size();
				this.append_results_to_log(temp_output);
			}
		

		}
		else{
			String temp_output = database_file_path + "," + Main.Number_of_Sensitive_Itemsets + ","+ combination + ","+"Algorithm Timed Out"+ ","+Main.items_removed+"," + "empty" +","+Main.building_FP_Tree_time+","+Main.choosing_item_time+","+Main.choosing_branch_time+","+Main.correlation+","+Main.average_itemset_length + ","+Main.average_support+","+Main.max_support+","+Main.min_support+","+Main.number_of_transactions_modified+Main.sensitive_itemsets_path+Main.main_fp_tree.get_number_of_leaves()+","+Main.main_fp_tree.get_number_of_nodes()+","+Main.main_fp_tree.get_number_of_items()+","+Main.relative_transactions.size();
			this.append_results_to_log(temp_output);
		}
		
	}

	// function that will populate the Items_In_Tree and Support_Items_In_tree
	// it will count the support level for each item


	private void reconstruct_sanitized_relative_database_improved() {
		
		main_fp_tree.add_nodes_to_levels();
		
		//find the number of transactions/itemsets and define the arraylist size
		int number_of_sanized_transactions =0;
		ArrayList<Node> temp_arraylist_0 = (ArrayList<Node>) main_fp_tree.all_levels.get(0);
		for(Node temp_node: temp_arraylist_0){
			number_of_sanized_transactions = temp_node.get_support()+number_of_sanized_transactions;
		}
		
		Main.sanitized_relative_transactions_improved = new ArrayList<Itemset>(number_of_sanized_transactions);
		
		for (int t=0; t<number_of_sanized_transactions;t++){
			ArrayList<Integer> temp_arraylist = new ArrayList<Integer>();
			Main.sanitized_relative_transactions_improved.add(new Itemset(0,temp_arraylist,"temp"));
		}
		
		//add items from main nodes to perspective itemsets and define the itemsets
		int counter =0;
		for(Node temp_node: temp_arraylist_0){
			
			for(int i =0; i<temp_node.get_support();i++){
//				System.out.println("counter value: "+ counter);
//				System.out.println("size of sanitized itemsets: "+ Main.sanitized_relative_transactions_improved.size());
				
				Main.sanitized_relative_transactions_improved.get(counter).items.add(temp_node.get_item());
				temp_node.improved_reconstruction_itemsets.items.add(counter);
//				System.out.println("Node and support: "+ temp_node.get_item()+", " + temp_node.get_support());
				counter++;
			}
			
		}
		
		//start reconstructing the rest of all levels
		for(int i =0; i<main_fp_tree.max_level;i++){
			
			System.out.println("Level Processed, Total number of levels: "+ i+", "+main_fp_tree.max_level);
			ArrayList<Node> temp_arraylist = (ArrayList<Node>) main_fp_tree.all_levels.get(i);
			//System.out.println("max level, size of all levels"+ main_fp_tree.max_level+", "+main_fp_tree.all_levels.size());
			
			for(Node parent_node:temp_arraylist){
				//System.out.println("size of itemsets in node: "+ parent_node.improved_reconstruction_itemsets.items.size());
				//System.out.println("Node and support: "+ parent_node.get_item()+", " + parent_node.get_support());
				
			if(parent_node.get_children().size()>0){

				

				int itemset_number_i = parent_node.improved_reconstruction_itemsets.items.get(0);
				
				for(Node child_node:parent_node.get_children()){
					int child_support = child_node.get_support();
					for(int j=0; j<child_support;j++){
						Main.sanitized_relative_transactions_improved.get(itemset_number_i).items.add(child_node.get_item());
						child_node.improved_reconstruction_itemsets.items.add(itemset_number_i);
						itemset_number_i++;
					}
				}
			}
				
			}
		}
		
		
		
	}

	private void find_number_of_transactions_modified() {
		int[] number_of_occurenece = new int[Main.relative_transactions.size()+1000];
		//int[] number_of_occurenece = new int[1000];
		//int[] number_of_occurenece2 = new int[Main.relative_transactions.size()];
		
		
		for(int i =0; i< Main.relative_transactions.size(); i++){
			int j = Main.relative_transactions.get(i).items.size();
			number_of_occurenece[j]++;
		}
		
		for(int i =0; i< Main.relative_transactions.size(); i++){
			//int j = Main.sanitized_relative_transactions.get(i).items.size();
			int j = Main.sanitized_relative_transactions_improved.get(i).items.size();
			number_of_occurenece[j]--;
		}
		
		
		for(int i =0; i< Main.relative_transactions.size(); i++){
			if(number_of_occurenece[i] > 0){
			Main.number_of_transactions_modified = Main.number_of_transactions_modified+number_of_occurenece[i];
			}
			
		}
		
	}

	private void calculate_correlation_of_sen_itemsets() {

		int current_occurences = 0;
		int size_of_itemsets = 0;
		int support_itemsets = 0;

		
		
		
		ArrayList<Integer> list_of_items_in_sensitive_itemsets = new ArrayList<Integer>();
		
		for(Itemset temp_itemset: Main.main_fp_tree.list_of_sensitive_itemsets){
			size_of_itemsets = size_of_itemsets+temp_itemset.items.size();
			
			average_support = average_support+temp_itemset.support_level;
			
			if(max_support < temp_itemset.support_level) max_support = temp_itemset.support_level;
			if(min_support > temp_itemset.support_level) min_support = temp_itemset.support_level;
			
			
			for(Integer temp_item:temp_itemset.items){
				if (!list_of_items_in_sensitive_itemsets.contains(temp_item)){
					list_of_items_in_sensitive_itemsets.add(temp_item);
				}
			}
			
		}
		average_support = average_support/(double) Main.main_fp_tree.list_of_sensitive_itemsets.size();
		System.out.println("=====================================================");
		System.out.println("Average Support is:" + average_support);
		System.out.println("Max Support is:" + max_support);
		System.out.println("Min Support is:" + min_support);		
		System.out.println("=====================================================");

		ArrayList<Item> list_items = new ArrayList<Item>();
		
		for (Integer temp:list_of_items_in_sensitive_itemsets){
			Item temp_item = new Item(temp);
			list_items.add(temp_item);
		}
		
		for(Itemset temp_itemset: Main.main_fp_tree.list_of_sensitive_itemsets){
			
			for(Integer temp_item:temp_itemset.items){
				
				for(Item temp_item2:list_items){
					if(temp_item.intValue()==temp_item2.get_value()){
						temp_item2.increment_occurences();
					}
				}
				
			}
			
		}
		
		for(Item temp:list_items){
			current_occurences = current_occurences+temp.get_occurences();
		}
		
		Main.correlation = (double) current_occurences / (double) list_items.size();
		Main.average_itemset_length = (double) size_of_itemsets/(double)Main.main_fp_tree.list_of_sensitive_itemsets.size();

		
	}

	private int count_items_removed(
			ArrayList<Itemset> sanitized_transactions,
			ArrayList<Itemset> relative_transactions) {

		int number_of_items_after_sanitization = 0;
		int number_of_items_before_sanitization = 0;
		
		for (Itemset temp: sanitized_transactions){
			number_of_items_after_sanitization = temp.get_items().size()+number_of_items_after_sanitization;
		}
		
		for (Itemset temp2: relative_transactions){
			number_of_items_before_sanitization = temp2.get_items().size()+number_of_items_before_sanitization;
		}
		
		return (number_of_items_before_sanitization-number_of_items_after_sanitization);
	}

	private void find_lost_frequent_itemsets() {

		ArrayList<Itemset> Itemsets_in_sanitized_transactions = new ArrayList<Itemset>();
		for (Itemset transaction:Main.sanitized_relative_transactions_improved){
			
			for (Itemset relative_temp:Main.relative_list_of_itemsets){
				
				if (transaction.Contains(relative_temp)){
					relative_temp.support_level++;
				}
				
			}
			
			
		}
		System.out.println("itemsets that were lost (improved test)");
		
		int counter_lost_frequent_itemset = 0;
		for (Itemset relative_temp:Main.relative_list_of_itemsets){
			
			if(relative_temp.support_level<Main.min_support_level){
				System.out.println(relative_temp.items + " " + relative_temp.support_level);
				counter_lost_frequent_itemset++;
			}
			
		}
		
		System.out.println(" number of itemsets that were lost (improved test): "+(counter_lost_frequent_itemset-Number_of_Sensitive_Itemsets));	
		Main.number_of_itemsets_lost  = counter_lost_frequent_itemset-Number_of_Sensitive_Itemsets;	
	}

	private void Adjust_relative_Itemsets() {

		for (Itemset apriori_temp:Main.apriori_original_data){
			for (Itemset relative_temp: Main.relative_list_of_itemsets){
				if((apriori_temp.Contains(relative_temp)) && (apriori_temp.get_items().size() ==relative_temp.get_items().size())){

					relative_temp.support_level = apriori_temp.support_level-relative_temp.support_level;
					
				}
			}
		}
		System.out.println("end of adjustment");
	}

	private void reconstruct_sanitized_relative_database() {
		
		ArrayList<Node> list_of_processed_nodes = new ArrayList<Node>();

		System.out.println("====================================================================");
		System.out.println("Number of leaves: " + main_fp_tree.get_number_of_leaves());
		System.out.println("====================================================================");

		for (Node temp:Main.main_fp_tree.list_of_leaf_nodes){
			list_of_processed_nodes.add(temp);
		}
		int loop_count = 0;
		while (Main.main_fp_tree.root.get_children().size()>0){
	
			ArrayList<Node> temp_add = new ArrayList<Node>();
			ArrayList<Node> temp_remove = new ArrayList<Node>();

			
			for (Node BH:list_of_processed_nodes){
			
				loop_count++;

				
				// create ArrayList of integers to store that branch that will be affected
				ArrayList<Integer> current_integer_list = new ArrayList<Integer>();
			
				// find all nodes till root
				ArrayList<Node> parents_nodes = BH.return_all_parents_till_root(BH);
				parents_nodes.add(BH);
				
				// store all items in current_integer_list
				Main.main_fp_tree.get_integers_from_nodes(parents_nodes, current_integer_list);
				System.out.println("number of items in current integer list: " +current_integer_list.size());
				
				// create itemset from the integers and get the list of sensitive itemsets included
				// in this itemset
				
				Itemset current_itemset = new Itemset(0,current_integer_list, "temp");
				
				// start main procedure
				
				Node current_node = BH;
				
				boolean loop_counter = true;
				
				int test_counter = 0;
				while (loop_counter){
					test_counter++;
					System.out.println("test counter "+ test_counter);

					if (current_node.get_support() == 1){
						
						if (current_node.get_type() =="leaf"){
							if (current_node.get_parent() != Main.main_fp_tree.root){

								//find out if a new leaf is needed and define it
								if(current_node.get_parent().get_children().size()<2){
									current_node.get_parent().set_type("leaf");
									temp_add.add(current_node.get_parent());
								}
								
								if (temp_add.contains(current_node)){
									temp_add.remove(current_node);
								}

							}
							else{
								loop_counter = false;
							}
							current_node.get_parent().get_children().remove(current_node);
							current_node.set_type("discarded");
							if (!temp_remove.contains(current_node)){
								temp_remove.add(current_node);
							}
						}
						
					}
					else if (current_node.get_support() > 1){
						current_node.set_support(current_node.get_support()-1);
						if (current_node.get_parent() == Main.main_fp_tree.root){
							loop_counter = false;
						}
					}
					if (current_node.get_parent() == Main.main_fp_tree.root){
						
						loop_counter = false;
					}
					current_node = current_node.get_parent();


				}
				
			
				sanitized_relative_transactions.add(current_itemset);

			}

			find_new_leaves(list_of_processed_nodes, temp_add, temp_remove);
			System.out.println("====================================================================");
			System.out.println("Number of new leaves: " + main_fp_tree.get_number_of_leaves());
			System.out.println("====================================================================");
		}
	}

	private void find_new_leaves(ArrayList<Node> list_of_processed_nodes, ArrayList<Node> temp_add, ArrayList<Node> temp_remove) {

		//add leaves if needed provided they are not include
		for(Node temp:temp_add){
			if(!list_of_processed_nodes.contains(temp)){
				list_of_processed_nodes.add(temp);
			}
		}
		//remove nodes that should be removed
		for (Node temp:temp_remove){
			if(list_of_processed_nodes.contains(temp)){
				list_of_processed_nodes.remove(temp);
			}
		}
		
		temp_remove.clear();
		temp_add.clear();
		
	}

	public Boolean populate_item_ArrayList(String passed_database_file_path,
			String passed_database_file_name) {
		try {
			String Database = passed_database_file_path
					+ passed_database_file_name;
			// Start reading the file line by line
			BufferedReader Database_Reader = new BufferedReader(new FileReader(
					Database));
			// Temp string and Itemset
			String transaction_in;
			Itemset Itemset_In;

			// Temp array list to hold items for the itemsets that will be
			// created
			ArrayList<Integer> items_integer = new ArrayList<Integer>();

			// get all the items from string and store them in the arraylist as
			// integers
			while ((transaction_in = Database_Reader.readLine()) != null) {

				this.Debug__Lines++;
				if(main_fp_tree.Items_In_Tree.size() > 499000 || this.Debug__Lines > 10000)
				{
					int i = 1;
					i = i+1;
				}
				String[] items_strings = transaction_in.split(" ");
				for (String item_string : items_strings) {
					items_integer.add(Integer.valueOf(item_string));
				}
				// Creat the new itemset
				Itemset_In = new Itemset(0, items_integer, "Normal");
				// if the itemset contains any sensitive itemsets then add them
				// to the tree
				int status_temp = 0;
				for (Itemset sensitive_item : main_fp_tree.list_of_sensitive_itemsets) {
					if (Itemset_In.Contains(sensitive_item)) {
						status_temp = 1;
						break;
					}
				}

				if (status_temp == 1) {
					for (String item_string : items_strings) {
						if (main_fp_tree.Items_In_Tree.contains(Integer
								.valueOf(item_string))) {
							int temp = main_fp_tree.Items_In_Tree
									.indexOf(Integer.valueOf(item_string));
							Integer temp_Integer = new Integer(
									main_fp_tree.Support_Items_In_Tree
											.get(temp).intValue() + 1);
							main_fp_tree.Support_Items_In_Tree.set(temp,
									temp_Integer);
						} else {
							main_fp_tree.Items_In_Tree.add(Integer
									.valueOf(item_string));
							int temp = main_fp_tree.Items_In_Tree
									.indexOf(Integer.valueOf(item_string));
							main_fp_tree.Support_Items_In_Tree.add(temp, 1);
						}

					}

				}

				items_integer.clear();
			}
			Database_Reader.close();
		} catch (IOException e) {
			System.err.println("Error: " + e);
		}
		return null;
	}

	// function that willprint the FP_Tree and all other lists

	public Boolean print_fp_tree(String path, String file_name) {
		String Output_File = path + file_name;

		BufferedWriter bufferedWriter = null;

		try {

			// Construct the BufferedWriter object
			bufferedWriter = new BufferedWriter(new FileWriter(Output_File));

			// loop for each path and print it to the text file along with
			// the support level, sensitive itemsets and their support for
			// each node

			Node temp_Node = null;
			for (Node leaf_Node : main_fp_tree.list_of_leaf_nodes) {
				temp_Node = leaf_Node;
				while (temp_Node.get_type() != "root") {
					bufferedWriter.write((String.valueOf(temp_Node.get_item()))
							+ " {");
					bufferedWriter
							.write(String.valueOf(temp_Node.get_support()));
					if (temp_Node.get_type().matches("leaf")) {
						bufferedWriter.write("(");
						for (Itemset temp_Itemset : temp_Node
								.get_sensitive_itemsets()) {
							for (Itemset Sen_Itemset : main_fp_tree.list_of_sensitive_itemsets) {

								if (temp_Itemset.Contains(Sen_Itemset)) {
									int temp_index = main_fp_tree.list_of_sensitive_itemsets
											.indexOf(Sen_Itemset) + 1;
									int temp_index2 = temp_Node
											.get_sensitive_itemsets().indexOf(
													temp_Itemset);
									bufferedWriter
											.write(" f"
													+ String.valueOf(temp_index)
													+ "("
													+ String.valueOf(temp_Node
															.get_support_sensitive_itemsets()
															.get(temp_index2))
													+ "); ");
								}
							}
						}
						bufferedWriter.write(")");
					}
					bufferedWriter.write("}; ");
					temp_Node = temp_Node.get_parent();
				}
				bufferedWriter.newLine();
			}
			bufferedWriter.write("number of lines : " + this.Number_Of_Lines);
			bufferedWriter.newLine();
			bufferedWriter.write("number of leaves : "
					+ Main.main_fp_tree.get_number_of_leaves());
			bufferedWriter.newLine();
			bufferedWriter.write("number of nodes : "
					+ Main.main_fp_tree.get_number_of_nodes());
			bufferedWriter.newLine();
			bufferedWriter.write("number of items : "
					+ Main.main_fp_tree.get_number_of_items());
			bufferedWriter.newLine();
			bufferedWriter.write("number of itemsets : "
					+ this.number_of_itemsets);
			
			Main.total_number_of_itemsets = this.number_of_itemsets;
			bufferedWriter.newLine();
						
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the BufferedWriter
			try {
				if (bufferedWriter != null) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}

	
	
	// function that will print the relative transactions into a file
	public Boolean print_transactions(String path, String file_name, ArrayList<Itemset> passed_array_list) {
		String Output_File = path + file_name;

		BufferedWriter bufferedWriter = null;

		try {

			// Construct the BufferedWriter object
			bufferedWriter = new BufferedWriter(new FileWriter(Output_File));

			// loop for each transaction to write to database
			int counter2 = 0;
			int counter =0;
			
			for (Itemset transaction_in:passed_array_list){
				counter2++;
				counter = 0;
				for(Integer temp_integer:transaction_in.get_items()){
					bufferedWriter.write(String.valueOf(temp_integer.intValue()));
					counter++;
					if (counter<transaction_in.items.size()){
						bufferedWriter.write(" ");
					}
				}

				if (counter2 < passed_array_list.size()){
					bufferedWriter.newLine();
				}
			}

						
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the BufferedWriter
			try {
				if (bufferedWriter != null) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}

	
	
	
	// function that will generate the FP_Tree and populate it in main_fp_tree
	// for relevant transactions Returns true if the FP_Tree was generated
	// properly. False otherwise.

	public Boolean generate_fp_tree(String passed_database_file_path,
			String passed_database_file_name) {
		String Database = passed_database_file_path + passed_database_file_name;

		try {

			// Start reading the file line by line
			BufferedReader Database_Reader = new BufferedReader(new FileReader(
					Database));
			// Temp string and Itemset
			String transaction_in;
			Itemset Itemset_In;
			// Temp array list to hold items for the itemsets that will be
			// created
			ArrayList<Integer> items_integer = new ArrayList<Integer>();
			// get all the items from string and store them in the arraylist as
			// integers
			while ((transaction_in = Database_Reader.readLine()) != null) {
				boolean indicator_relative_transaction = false;
				this.Number_Of_Lines++;
				String[] items_strings = transaction_in.split(" ");
				for (String item_string : items_strings) {
					items_integer.add(Integer.valueOf(item_string));
				}
				// Creat the new itemset
				Itemset_In = new Itemset(0, items_integer, "Normal");
				// if the itemset contains any sensitive itemsets then add them
				// to the tree
				for (Itemset sensitive_item : main_fp_tree.list_of_sensitive_itemsets) {
					if (Itemset_In.Contains(sensitive_item)) {
						main_fp_tree.add_itemset(Itemset_In);
						this.number_of_itemsets++;
						// find out all sen itemsets in the transaction and increment them by one
							for (Itemset temp2:Main.main_fp_tree.list_of_sensitive_itemsets){
								if (Itemset_In.Contains(temp2)){
									temp2.support_level++;
								}
							}
						System.out.println(sensitive_item.get_items() +"" + sensitive_item.support_level);
						indicator_relative_transaction = true;
						break;
					}
				}

				if (indicator_relative_transaction){
					ArrayList<Integer> temp = new ArrayList<Integer>();
					for(Integer temp_integer:items_integer){
						temp.add(temp_integer);
					}
					Itemset Itemset_In_new = new Itemset(0, temp, "Normal");
					Main.relative_transactions.add(Itemset_In_new);
					indicator_relative_transaction = false;
				}
				items_integer.clear();

			}
			Database_Reader.close();
		} catch (IOException e) {
			System.err.println("Error: " + e);
		}
		return null;
	}

	// function that will read all apriori data
	private ArrayList<Itemset> read_all_itemsets(String apriori_data_path2) {
		ArrayList<Itemset> temp_itemsets = new ArrayList<Itemset>();
		try {
			String Database = apriori_data_path2;
			// Start reading the file line by line
			BufferedReader Database_Reader_Sensitive = new BufferedReader(new FileReader(
					Database));
			// Temp string and Itemset
			String transaction_in;

			// get all the items from string and store them in the arraylist as
			// integers
			while ((transaction_in = Database_Reader_Sensitive.readLine()) != null) {
				
				ArrayList<Integer> items_integer = new ArrayList<Integer>();
				String[] items_strings = transaction_in.split(" ");
				for (String item_string : items_strings) {
					items_integer.add(Integer.valueOf(item_string));
				}
				
				int last_item_index = items_integer.size()-1;
				int temp_support = items_integer.get(last_item_index);
				items_integer.remove(last_item_index);
				Itemset temp_itemset = new Itemset(temp_support, items_integer, "Normal");
				if (last_item_index == 1){
					Main.list_of_items_in_database.add(temp_itemset);
				}
				else{
					
					// Create the new itemset
					//Itemset_In = new Itemset(0, items_integer, "Normal");
					Itemset temp_itemset2 = new Itemset(temp_support, items_integer, "Normal");
					temp_itemsets.add(temp_itemset2);
				}
			}
			Database_Reader_Sensitive.close();

		} catch (IOException e) {
			System.err.println("Error: " + e);
		}
		
		return temp_itemsets;
	}
	// main algorithm that will minimize the items removed from the FP_Tree
	// will return true if the process was successful, false otherwise.
	public Boolean min_items_removed(FP_Tree fp_tree, Integer min_support_level) {
		return null;
	}

	// A function that will return true if the transaction read contains a
	// sensitive itemset and false otherwise. This will be used in the
	// generaiton
	// of the FP_Tree from relevant transactions.

	public Boolean is_transaction_sensitive(Itemset transaction) {
		for (Itemset temp : main_fp_tree.list_of_sensitive_itemsets) {
			if (transaction.Contains(temp))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {
		if (args.length >=1){
			try {
				Main Main_Obj = new Main();
				Main.combination = Integer.valueOf(args[0]);
				Main.database_file_path = args[1];
				Main.min_support_level = Integer.valueOf(args[2]);
				Main.sensitive_itemsets_path = args[3];
				main_fp_tree = new FP_Tree(Number_of_Sensitive_Itemsets, sensitive_itemsets_path);
				
				Main_Obj.main();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		else{
		    //System.out.println("Please input correct variables for the program to run");
			//Main Main_Obj = new Main();
			//Main_Obj.main(); 
			
			//System.out.println("pls enter the correct input of variables, database name, database path, fp-tree file, minimum support level");
		}	

	}
	private ArrayList<Itemset> find_all_itemsets(String database_path2,
			String Support_Level, boolean first_scan) throws Exception {
		String[] args={database_path2, Support_Level};
		//new Apriori(args, this);
		if (first_scan){
			Apriori my_apriori = new Apriori(args, true, true);
			ArrayList<Itemset> all_itemsets = my_apriori.Apriori_Itemset(args);
			return all_itemsets;
		}
		else
		{
			Apriori my_apriori = new Apriori(args, true, false);
			ArrayList<Itemset> all_itemsets = my_apriori.Apriori_Itemset(args);
			return all_itemsets;
		}

		
		
		
	}
	

	public void append_results_to_log(String string) {
	

		  try{
			  // Create file 
			  FileWriter fstream = new FileWriter(Main.log_file_name,true);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(string);
			  out.newLine();
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
	}

}
