package fih;

/**
 *
 * @author Rami
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public final class FP_Tree {

	// number of nodes is recorded for the FP_Tree
	private int number_of_nodes = 1;
	// number of leaves is recorded for the FP_Tree
	private int number_of_leaves = 0;
	// number of items is recorded for the FP_Tree
	private int number_of_items = 0;
	// number of itemsets is recorded for the FP_Tree
	private int number_of_transactions = 0;
	// Add list of sensitive itemsets
	public ArrayList<Itemset> list_of_sensitive_itemsets = new ArrayList<Itemset>();
	// Add list of all nodes
	public ArrayList<Node> list_of_all_nodes = new ArrayList<Node>();
	// Define number of sensitive itemsets
	public int number_of_sensitive_itemsets = 30;
	// main or the root node is defined
	public Node root = new Node(Main.main_fp_tree, 0, "root", 0);
	// ArrayList to conatin all items and their support
	public ArrayList<Integer> Items_In_Tree = new ArrayList<Integer>(100000);
	public ArrayList<Integer> Support_Items_In_Tree = new ArrayList<Integer>(100000);
	// leaf nodes are added to the list below to keep trak of them
	public ArrayList<Node> list_of_leaf_nodes = new ArrayList<Node>();
	
	//Define arraylists to store all levels of nodes
	public ArrayList all_levels = new ArrayList();
	
	// Define max level in FP_Tree
	public int max_level = 0;

	
	// List of sensitive itemsets
	// public ArrayList<Itemset> Sensitive_Itemsets = new ArrayList<Itemset>();
	// a public function that adds an item to the FP_Tree
	// it returns 0 if the item is added to an existing node.
	// it returns 1 if the item is added to a new node
	public int add_itemset(Itemset new_Itemset_added) {

		// increment the number of items
		this.number_of_items = this.number_of_items
				+ new_Itemset_added.items.size();

		// increment the number of itemsets
		this.number_of_transactions = this.number_of_transactions + 1;

		// find sensitive itemsets that are contained within this itemset
		ArrayList<Itemset> current_sen_itemsets = new ArrayList<Itemset>();
		current_sen_itemsets = this.find_sensitive_itemsets(new_Itemset_added);

		// add status int that will track the changes
		int Status = 2;

		Item_Comparator Item_Compare = new Item_Comparator();
		Collections.sort(new_Itemset_added.items, Item_Compare);

		// adding temp node to follow the parent
		Node parent_node = root;
		// figure out if the first item is part of the root
		// loop for each item in the items listed in the new itemset
		for (Integer current_item : new_Itemset_added.items) {

			// set a last item indicator, set to 1 if he last item in the
			// arraylist
			int last_item = 0;
			int temp_index = new_Itemset_added.items.indexOf(current_item);
			if (temp_index == (int) new_Itemset_added.items.size() - 1) {
				last_item = 1;
			}

			// compare the item for each child with the current item
			// increase the support if the node contains the same item and
			// make the child the parent and oncrease the support by 1
			Status = 2;

			// find if the item is already a child or not then decide
			// which function to call
			if (this.is_Item_a_child(current_item, parent_node)) {
				parent_node = increment_item_in_child(current_item,
						parent_node, last_item, current_sen_itemsets);
			} else {
				parent_node = add_new_node_to_child(current_item, parent_node,
						last_item, current_sen_itemsets);
			}

		}
		return Status;
	}

	public Node add_new_node_to_child(Integer current_item, Node parent_node,
			int last_item, ArrayList<Itemset> sen_itemsets) {
		boolean new_node = true;
		if (parent_node == root) {
			Node temp = new Node(Main.main_fp_tree, current_item, "main", 1);
			temp.set_parent(root);
			temp.level = 0;
			parent_node.add_child(temp);
			temp.set_main_node(temp);
			this.number_of_nodes = this.number_of_nodes + 1;
			this.validate_node_for_type(temp, last_item, sen_itemsets, new_node);
			this.list_of_all_nodes.add(temp);
			return temp;
		} else {
			Node temp = new Node(Main.main_fp_tree, current_item,
					"intermediate", 1);
			temp.set_parent(parent_node);
			temp.level = parent_node.level+1;
			if(temp.level > this.max_level) this.max_level = temp.level;
			parent_node.add_child(temp);
			temp.set_parent(parent_node);
			temp.set_main_node(parent_node.get_main_node());
			this.number_of_nodes = this.number_of_nodes + 1;
			this.validate_node_for_type(temp, last_item, sen_itemsets, new_node);
			this.list_of_all_nodes.add(temp);
			return temp;
		}
	}

	public Node increment_item_in_child(Integer current_item, Node parent_node,
			int last_item, ArrayList<Itemset> sen_itemsets) {
		boolean new_node = false;
		Node temp_node = parent_node.get_child_for_item(current_item);
		temp_node.set_support(temp_node.get_support() + 1);
		this.validate_node_for_type(temp_node, last_item, sen_itemsets,
				new_node);
		return temp_node;

	}

	public void validate_node_for_type(Node node, int last_item,
			ArrayList<Itemset> sen_itemsets, boolean new_node) {
		if (node.get_type() == "leaf" && last_item != 1) {
			node.set_type("old");
			this.list_of_leaf_nodes.remove(node);
			this.number_of_leaves--;
		}
		if (node.get_children().isEmpty() && last_item == 1) {
			node.set_type("leaf");
			if (!this.list_of_leaf_nodes.contains(node)) {
				this.list_of_leaf_nodes.add(node);
				this.number_of_leaves++;
			}
			if (new_node) {
				this.add_sen_itemsets_from_nearest_old_leaf(node);
			}
			this.set_sensitive_support_per_node(node, sen_itemsets);
			//this.fix_all_nodes_upwards(node);
		}
		if (!node.get_children().isEmpty() && last_item != 1) {
			if (node.get_type() == "old")
				;
			else {
				node.set_type("intermediate");
			}
		}
		if (!node.get_children().isEmpty() && last_item == 1) {
			node.set_type("old");
			this.propagate_sen_itemset_to_all_below(node, sen_itemsets);
			this.set_sensitive_support_per_node(node, sen_itemsets);
		}
	}

	private void fix_all_nodes_upwards(Node node) {
		ArrayList<Node> parents = node.return_all_parents_till_root(node);
		ArrayList<Integer> items = new ArrayList<Integer>();

		for (Node temp_node : parents) {
			items.add(temp_node.get_item());
		}
		Itemset current_itemset = new Itemset(0, items, "not_sensitive");
		Node parent = node.get_parent();
		ArrayList<Itemset> sen_itemsets = new ArrayList<Itemset>();

		parents.clear();
		parents.add(node);

		while (parent != root) {
			if (parent.get_children().size() > 1) {
				sen_itemsets = this.find_sensitive_itemsets(current_itemset);
				this.propagate_sen_itemset_to_all_below_except_certain_nodes(
						parent, sen_itemsets, parents);
				parents.add(parent);
				sen_itemsets.clear();
			}
			current_itemset.get_items().remove((Integer) parent.get_item());

			parent = parent.get_parent();
		}

	}

	private void propagate_sen_itemset_to_all_below_except_certain_nodes(
			Node node, ArrayList<Itemset> sen_itemsets,
			ArrayList<Node> exclude_nodes) {
		for (Node leaf : node.return_leaves()) {
			if (!exclude_nodes.contains(leaf)) {
				exclude_nodes.add(leaf);
				for (Itemset temp_itemset : sen_itemsets) {
					leaf.add_sensitive_itemset(temp_itemset);
				}
			}
		}
		for (Node old_leaf : node.return_old_leaves()) {
			if (!exclude_nodes.contains(old_leaf)) {
				exclude_nodes.add(old_leaf);
				for (Itemset temp_itemset : sen_itemsets) {
					old_leaf.add_sensitive_itemset(temp_itemset);
				}
			}
		}

	}

	public boolean set_sensitive_support_per_node(Node Leaf_Node,
			ArrayList<Itemset> Sensitive_Itemsets) {

		for (Itemset temp_Itemset : Sensitive_Itemsets) {
			Leaf_Node.add_sensitive_itemset(temp_Itemset);
		}
		return true;
	}

	public void add_sen_itemsets_from_nearest_old_leaf(Node node) {
		Node temp_node = this.find_nearest_old_leaf(node);
		if (temp_node != node) {
			node.inherit_node(temp_node);
		}
	}

	public Node find_nearest_old_leaf(Node node) {

		int temp = 0;
		Node temp_node = node.get_parent();
		while (temp == 0) {
			if (temp_node.get_type() == "old") {
				return temp_node;
			} else {
				if (temp_node == root) {
					temp = 1;
				}
				temp_node = temp_node.get_parent();
			}
		}
		return node;
	}

	public void propagate_sen_itemset_to_all_below(Node node,
			ArrayList<Itemset> sen_itemsets) {
		for (Node leaf : node.return_leaves()) {
			for (Itemset temp_itemset : sen_itemsets) {
				leaf.add_sensitive_itemset(temp_itemset);
			}
		}
		for (Node old_leaf : node.return_old_leaves()) {
			for (Itemset temp_itemset : sen_itemsets) {
				old_leaf.add_sensitive_itemset(temp_itemset);
			}
		}
	}

	// a public function that returns an arraylist of sensitive itemsets
	// from a passed transaction or itemset
	public ArrayList<Itemset> find_sensitive_itemsets(Itemset Passed_Itemset) {

		ArrayList<Itemset> Current_Sen_Itemset = new ArrayList<Itemset>();

		for (Itemset temp_Itemset : this.list_of_sensitive_itemsets) {
			if (Passed_Itemset.Contains(temp_Itemset)) {
				Current_Sen_Itemset.add(temp_Itemset);
			}
		}

		return Current_Sen_Itemset;
	}

	// a public funciton that finds the list of leaf nodes that are connected to
	// a
	// node
	public ArrayList<Node> find_leaf_nodes(Node Last_Node) {

		ArrayList<Node> current_leaf_nodes = new ArrayList<Node>();

		current_leaf_nodes = Last_Node.return_leaves();

		return current_leaf_nodes;

	}

	// a public funciton that finds the list of children and grandchildren nodes
	// that are connected to a node
	public ArrayList<Node> find_all_children_nodes(Node temp_Node) {

		ArrayList<Node> current_all_children_nodes = new ArrayList<Node>();

		current_all_children_nodes = temp_Node.return_all_children_nodes();

		return current_all_children_nodes;

	}

	// a public function that find if the item passed (Integer) is
	// contained within the child of the parent node returns true if that is
	// the case
	public boolean is_Item_a_child(Integer item, Node Parent_Node) {

		for (Node child : Parent_Node.get_children()) {
			if (child.get_item() == item.intValue()) {
				return true;
			}
		}

		return false;

	}

	// a public funciton that sets the number of nodes.
	// returns true if it is successful, false otherwise.
	public Boolean set_number_of_nodes(Integer new_number_of_nodes) {
		this.number_of_nodes = new_number_of_nodes;
		if (this.number_of_nodes == new_number_of_nodes) {
			return true;
		} else {
			return false;
		}
	}

	// a public funciton that sets the number of leaves.
	// returns true if it is successful, false otherwise.
	public Boolean set_number_of_leaves(Integer new_number_of_leaves) {
		this.number_of_leaves = new_number_of_leaves;
		if (this.number_of_leaves == new_number_of_leaves) {
			return true;
		} else {
			return false;
		}
	}

	// a public funciton that sets the number of items.
	// returns true if it is successful, false otherwise.
	public Boolean set_number_of_items(Integer new_number_of_items) {
		this.number_of_items = new_number_of_items;
		if (this.number_of_items == new_number_of_items) {
			return true;
		} else {
			return false;
		}
	}

	// a public funciton that gets the number of nodes.
	// returns true if it is successful, false otherwise.
	public int get_number_of_nodes() {
		return this.number_of_nodes;
	}

	// a public funciton that gets the number of leaves.
	// returns true if it is successful, false otherwise.
	public int get_number_of_leaves() {
		return this.number_of_leaves;
	}

	// a public funciton that sets the number of nodes.
	// returns true if it is successful, false otherwise.
	public int get_number_of_items() {
		return this.number_of_items;
	}

	// a public function that will split the node from the original branch
	// it will return true if the split is successful, false otherwise.
	public Boolean split_branch(Node current_node, Node leaf_node) {
		return null;
	}

	// a public function that will reduce the support level for the node
	// returns true if successful,false otherwise
	public Boolean reduce_support_for_node(Node node, Integer decrement) {
		Integer temp = node.get_support();
		node.set_support(node.get_support() - decrement);
		if (node.get_support() == temp - decrement) {
			return true;
		} else {
			return false;
		}
	}

	// a public funciton that caluclates the weight for the item using weight1
	// method
	public float get_item_weight1(Integer item) {
		return 0;
	}

	// a public funciton that caluclates the weight for the item using weight2
	// method
	public float get_item_weight2(Integer item) {
		return 0;
	}

	// a public funciton that caluclates the weight for the branch using branch1
	// method
	public float get_branch_weight1(Node leaf_node) {
		return 0;
	}

	// a public funciton that caluclates the weight for the branch using branch2
	// method
	public float get_branch_weight2(Node leaf_node) {
		return 0;
	}

	/*
	// a public function that populates the sensitive itemsets
	public void define_sensitive_itemsets() {

		// Define some sensitive itemsets for testing
		ArrayList<Integer> Integer_Arraylist_Sensitive1 = new ArrayList<Integer>();
		ArrayList<Integer> Integer_Arraylist_Sensitive2 = new ArrayList<Integer>();
		ArrayList<Integer> Integer_Arraylist_Sensitive3 = new ArrayList<Integer>();
		ArrayList<Integer> Integer_Arraylist_Sensitive4 = new ArrayList<Integer>();
		ArrayList<Integer> Integer_Arraylist_Sensitive5 = new ArrayList<Integer>();

		// Add Sensitive Itemset to the list
		Integer_Arraylist_Sensitive1.add(1);
		Integer_Arraylist_Sensitive1.add(2);
		Integer_Arraylist_Sensitive1.add(3);
		Integer_Arraylist_Sensitive1.add(4);

		Itemset Temp1 = new Itemset(0, Integer_Arraylist_Sensitive1, "Normal");

		// Add Sensitive Itemset to the list
		Integer_Arraylist_Sensitive2.add(3);
		Integer_Arraylist_Sensitive2.add(5);
		Itemset Temp2 = new Itemset(0, Integer_Arraylist_Sensitive2, "Normal");

		// Add Sensitive Itemset to the list
		Integer_Arraylist_Sensitive3.add(4);
		Integer_Arraylist_Sensitive3.add(5);
		Integer_Arraylist_Sensitive3.add(7);

		Itemset Temp3 = new Itemset(0, Integer_Arraylist_Sensitive3, "Normal");

		// Add Sensitive Itemset to the list
		Integer_Arraylist_Sensitive4.add(5);
		Integer_Arraylist_Sensitive4.add(9);
		Itemset Temp4 = new Itemset(0, Integer_Arraylist_Sensitive4, "Normal");

		// Add Sensitive Itemset to the list
		Integer_Arraylist_Sensitive5.add(8);
		Integer_Arraylist_Sensitive5.add(9);

		Itemset Temp5 = new Itemset(0, Integer_Arraylist_Sensitive5, "Normal");

		this.list_of_sensitive_itemsets.add(Temp1);
		this.list_of_sensitive_itemsets.add(Temp2);
		this.list_of_sensitive_itemsets.add(Temp3);
		this.list_of_sensitive_itemsets.add(Temp4);
		this.list_of_sensitive_itemsets.add(Temp5);
	}
	*/
	
	public void read_sensitive_itemsets(String passed_sensitive_itemsets_path){
		try {
			String Database = passed_sensitive_itemsets_path;
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
				// Create the new itemset
				//Itemset_In = new Itemset(0, items_integer, "Normal");
				Itemset temp_itemset = new Itemset(0, items_integer, "Normal");
				temp_itemset.number_of_items = items_integer.size();
				this.list_of_sensitive_itemsets.add(temp_itemset);
			}
			Database_Reader_Sensitive.close();
			this.number_of_sensitive_itemsets = this.list_of_sensitive_itemsets.size();
		} catch (IOException e) {
			System.err.println("Error: " + e);
		}

	}


	// constructor for FP_Tree
	public FP_Tree(int Number_Of_sensitive_Itemsets, String passed_sensitive_itemsets_path) {
		this.number_of_nodes = 1;
		this.number_of_leaves = 0;
		this.number_of_items = 0;
		this.read_sensitive_itemsets(passed_sensitive_itemsets_path);
		this.number_of_sensitive_itemsets = Number_Of_sensitive_Itemsets;
	}

	public void populate_level_weight_in_leaves() {
		
		for (Node temp_node:this.list_of_leaf_nodes){
			temp_node.level = temp_node.return_all_parents_till_root(temp_node).size();
			if(temp_node.level !=0)	temp_node.weight = (double)temp_node.get_sensitive_itemsets().size()/(double)temp_node.level;
			else{
				temp_node.weight = (double)temp_node.get_sensitive_itemsets().size()/1;
			}
		}
		
	}

	public ArrayList<Itemset> sanitize_node_in_branch(Item IH, Node BH) {
		
		// create ArrayList of integers to store that branch that will be affected
		ArrayList<Integer> current_integer_list = new ArrayList<Integer>();
		
		// start populating the itemset for each node till root
		// after that is done, you need to evaluate the whole branch to populate the 
		// sensitive itemsets properly. For that purpose we need to identify if the
		// branch has split or not
		
		// find all nodes till root
		ArrayList<Node> parents_nodes = BH.return_all_parents_till_root(BH);
		parents_nodes.add(BH);
		
		// store all items in current_integer_list
		get_integers_from_nodes(parents_nodes, current_integer_list);
		
		// create itemset from the integers and get the list of sensitive itemsets included
		// in this itemset
		
		Itemset current_itemset = new Itemset(0,current_integer_list, "temp");
		
		// get sensitive itemsets from both BH and current_itemset
		ArrayList<Itemset> BH_sensitive_itemsets = BH.get_sensitive_itemsets();
		ArrayList<Itemset> branch_sensitive_itemsets = this.find_sensitive_itemsets(current_itemset);
		
		//find sensitive itemsets that will be removed and store them
		ArrayList<Itemset> removed_sensitive_itemsets = new ArrayList<Itemset>();
		
		find_removed_sensitive_itemsets(removed_sensitive_itemsets, branch_sensitive_itemsets, IH);
		
		// start main sanitization procedure
		
		Node temp_node;
		Node new_leaf_node;
		Node current_node = BH;
		
		boolean loop_counter = true;
		boolean new_leaf_exists = false;
		
		while (loop_counter){
			
			if (current_node.get_support() == 1){
				if (current_node.get_type() == "leaf"){
					if (current_node.get_parent() != this.root){
						// if the parent has only one child set it to leaf
						if (current_node.get_parent().get_children().size() > 1){
							new_leaf_exists = false;
						}
						else{
						new_leaf_node = current_node.get_parent();
						new_leaf_node.set_type("leaf");
						new_leaf_exists = true;
						this.list_of_leaf_nodes.add(new_leaf_node);
						this.number_of_leaves++;
						}
					}
					else{
						// if the parent has only one child set it to leaf
						if (current_node.get_parent().get_children().size() > 1){
							new_leaf_exists = false;
						}
						else{
						new_leaf_node = current_node.get_parent();
						new_leaf_node.set_type("leaf");
						new_leaf_exists = true;
						this.list_of_leaf_nodes.add(new_leaf_node);
						this.number_of_leaves++;
						}
						loop_counter = false;
					}
					this.list_of_leaf_nodes.remove(current_node);
					this.number_of_leaves--;
				}
				current_node.get_parent().get_children().remove(current_node);
				this.number_of_nodes--;
				this.list_of_all_nodes.remove(current_node);
				this.number_of_items--;

			}
			else{
				this.reduce_support_for_node(current_node, 1);
				this.number_of_items--;
				if (current_node.get_parent() == this.root){
					loop_counter = false;
				}
			}
			current_node = current_node.get_parent();

			
		}
		
		if (!new_leaf_exists){
			//Arraylist to conatin all the modified itemset in branch_sensitive_itemsets
			ArrayList<Itemset> temp_array = new ArrayList<Itemset>();
			ArrayList<Integer> BH_support_sen_itemsets = BH.get_support_sensitive_itemsets();
			for (Itemset branch_itemset:branch_sensitive_itemsets){
				for(Itemset BH_itemset:BH_sensitive_itemsets){
					if (branch_itemset.Contains(BH_itemset) && (BH_itemset.items.size() == branch_itemset.items.size())){
						int temp_index = BH_sensitive_itemsets.indexOf(BH_itemset);
						Integer temp_support = BH_support_sen_itemsets.get(temp_index);
						if(temp_support.intValue() ==1){
							temp_array.add(BH_itemset);
						}
						else{
							BH_support_sen_itemsets.set(temp_index, (temp_support.intValue()-1));
						}
					}
				}
			}
			
			if (!temp_array.isEmpty()){
				for (Itemset temp_itemset: temp_array){
					int temp_index = BH_sensitive_itemsets.indexOf(temp_itemset);
					if (temp_index == -1){
						temp_index = -1;
					}
					BH_sensitive_itemsets.remove(temp_itemset);
					BH_support_sen_itemsets.remove(temp_index);
				}
			}
		}
		
		//remove item from IH from the new integer list
		ArrayList<Integer> new_integer_list = new ArrayList<Integer>();
		
		for (Integer temp_integer: current_integer_list){
			new_integer_list.add(temp_integer.intValue());
			if (temp_integer.intValue() == IH.value.intValue()){
				new_integer_list.remove(temp_integer);
			}
		}
		
		//create a new itemset after modifcation tobe added to FP-Tree
		Itemset new_itemset = new Itemset(0,new_integer_list, "temp");
		
		// add itemset to FP tree
		this.add_itemset(new_itemset);
		
		// evaluate the sensitive itemsets and determine which one became non frequent
		ArrayList<Itemset> sen_itemset_became_non_frequent = new ArrayList<Itemset>();
		for (Itemset temp1_Itemset:this.list_of_sensitive_itemsets){
			for (Itemset temp2_Itemset:removed_sensitive_itemsets){
				if ((temp1_Itemset.Contains(temp2_Itemset))&&(temp1_Itemset.items.size()==temp2_Itemset.items.size())){
					if (temp1_Itemset.support_level > 1){
						temp1_Itemset.support_level = temp1_Itemset.support_level-1;
					}
					else{
						temp1_Itemset.support_level = 0;
					}
					if (temp1_Itemset.support_level < Main.min_support_level){

						sen_itemset_became_non_frequent.add(temp1_Itemset);
					}
				}
			}
		}
		
		//remove itemsets that are not frequent from list
		for (Itemset temp_itemset:sen_itemset_became_non_frequent){
			this.list_of_sensitive_itemsets.remove(temp_itemset);
			this.number_of_sensitive_itemsets--;
		}

		//print debug info
		System.out.println("======================================================================");
		System.out.println("Item removed "+IH.value.intValue()+" Branch Affected "+ BH.get_item()+" Number of Leaves "+ this.number_of_leaves);
		System.out.println("======================================================================");
		return sen_itemset_became_non_frequent;
		
	}

	private void find_removed_sensitive_itemsets(
			ArrayList<Itemset> removed_sensitive_itemsets,
			ArrayList<Itemset> branch_sensitive_itemsets, Item iH) {
		
			for (Itemset temp_Itemset:branch_sensitive_itemsets){
				if (temp_Itemset.items.contains(iH.value)){
					removed_sensitive_itemsets.add(temp_Itemset);
				}
			}
		
	}

	public void get_integers_from_nodes(
			ArrayList<Node> parents_nodes, ArrayList<Integer> passed_current_integer_list) {

		for (Node temp_node: parents_nodes){
			
			passed_current_integer_list.add(temp_node.get_item());
			
		}
		
	}

	public void add_nodes_to_levels() {
		//Define arraylists to store nodes based on max_level
		for(int i =0; i<this.max_level+1;i++){
			this.all_levels.add(new ArrayList<Node>());
		}
		
		for(Node temp_node:this.list_of_all_nodes){
			temp_node.level = temp_node.return_all_parents_till_root(temp_node).size();
			ArrayList<Node> temp_arraylist = (ArrayList<Node>) this.all_levels.get(temp_node.level);
			temp_arraylist.add(temp_node);
		}
		
		
	}

	
}
