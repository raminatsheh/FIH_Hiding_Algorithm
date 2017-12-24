/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fih;

import java.util.ArrayList;

/**
 * 
 * @author Rami
 */
public class Node {

	// the FP-Tree that the nodes reports to is recorded
	private FP_Tree fptree;
	// the item that is represented by this node is recorded
	private int item;
	// the support level of the item in this node is recorded
	private int support;
	// the type of th node is recorded, main, intermediate or leaf
	private String type;
	// the level of the node, i.e. how many parents are there
	public int level = 0;
	// the weight that will be used for sanitization
	public double weight = 0;
	// the inherit sensitive itemset flag
	private boolean inherit = false;
	// the level of the node in the FP-Tree is recorded
	// this will help in the sanitization process
	// the number of children for tthis node is recorded, this will help during
	// the split process and to add items to the tree
	private int number_of_children;
	// the parent of the current node is recorded in order to be able to
	// track the the path. There can only be one node as a parent.
	private Node parent;
	// the list of nodes that are children to this node are recorded
	// this will help during the split process and to add items to the tree
	private ArrayList<Node> children = new ArrayList<Node>();
	// this is the main node that this node belongs to, there can only be
	// one main node.
	private Node main_node;
	// list of sensitive itemsets that the path -until this node- supports
	// this matters mainly for leaf nodes, but can be extended to all
	// other nodes.
	private ArrayList<Itemset> sensitive_itemsets = new ArrayList<Itemset>();
	// Support level for each sensitive itemset in the current node
	// the location of the integer in the arraylist corresponds to
	// the sensitive itemset that is located in sensitive_itemsets arraylist
	private ArrayList<Integer> support_sensitive_itemsets = new ArrayList<Integer>();
	
	//Define Itemset to store itemset number for improved reconstruction 
	ArrayList<Integer> temp_arraylist = new ArrayList<Integer>();
	public Itemset improved_reconstruction_itemsets = new Itemset(0, temp_arraylist, "temp");

	// a public function that sets the FP_Tree tha the node belongs to
	// it returns true if the process was asuccessful and false otherwise
	public Boolean set_fptree(FP_Tree new_fptree) {
		this.fptree = new_fptree;
		if (this.fptree == new_fptree) {
			return true;
		} else {
			return false;
		}
	}

	// a public function that returns the value of the item that
	// is represented by this node
	public int get_item() {
		return this.item;
	}

	// a public function that returns the value of the type of the node
	public String get_type() {
		return this.type;
	}

	// apublic function that sets the type of the node
	// it returns true if it is successful and flase otherwise
	public Boolean set_type(String new_type) {
		if (new_type.contains("main")) {
			this.type = "main";
		} else if (new_type.contains("intermediate")) {
			this.type = "intermediate";
		} else if (new_type.contains("leaf")) {
			this.type = "leaf";
		} else if (new_type.contains("old")) {
			this.type = "old";
		} else {
			return false;
		}

		return true;
	}

	// a public function that returns the arraylist "children" that contains
	// the children of this node
	public ArrayList<Node> get_children() {
		return this.children;
	}

	// a public function that returns the support of the item that is
	// represneted
	// by the current node
	public int get_support() {
		return this.support;
	}

	// a public function that sets the support of the item that is represented
	// by this node. Returns true if succesful and false otherwise.
	public Boolean set_support(int new_support) {
		this.support = new_support;
		if (this.support == new_support) {
			return true;
		} else {
			return null;
		}
	}

	// a public function that sets the parent of the node
	public Boolean set_parent(Node new_parent) {
		this.parent = new_parent;
		if (this.parent == new_parent) {
			return true;
		} else {
			return false;
		}
	}

	// a public function that returns the parent node of the current node
	public Node get_parent() {
		return this.parent;
	}

	// a public function that removes a node from the children arraylist
	// returns true if the arralylist contains the node and it was removed
	public Boolean remove_child(Node child) {
		return this.children.remove(child);
	}

	// a public funtion that adds a node to the children arraylist
	public Boolean add_child(Node new_child) {
		this.number_of_children++;
		return this.children.add(new_child);
	}

	// a public function that returns true if the node contains a child that
	// supports the passed item
	public Boolean is_item_a_child(int passed_item) {
		for (Node temp_node : this.children) {
			if (temp_node.item == passed_item) {
				return true;
			}
		}
		return false;
	}

	// Constructor for Node
	public Node(FP_Tree Tree, int new_item, String new_type, int new_support) {

		this.fptree = Tree;
		this.item = new_item;
		this.support = new_support;
		this.type = new_type;
		// for (int i =0; i< Main.Number_of_Sensitive_Itemsets; i++)
		// {
		// this.support_sensitive_itemsets.add(0);
		// }

	}

	// a public function that adds a sensitive itemset to the current node
	// this sensitive itemset is only for the path that the node represents
	public Boolean add_sensitive_itemset(Itemset new_itemset) {
		// check if the sensitive_itemsets arraylist contains the item or not
		if (this.sensitive_itemsets.contains(new_itemset)) {
			// get the index of the sensitive itemset
			// int index_of_new_itemset =
			// this.fptree.list_of_sensitive_itemsets.indexOf(new_itemset);
			int index_of_new_itemset = this.sensitive_itemsets
					.indexOf(new_itemset);
			// increment the value of the support of this itemset
			int temp = this.support_sensitive_itemsets
					.get(index_of_new_itemset);
			Integer temp1 = new Integer(temp + 1);
			this.support_sensitive_itemsets.set(index_of_new_itemset, temp1);
			return true;
		} else {
			this.sensitive_itemsets.add(new_itemset);
			this.support_sensitive_itemsets.add(new Integer(1));
			return false;
		}
	}

	// a public function that reduces the support for a sensitive itemset in the
	// current node
	public Boolean reduce_sensitive_itemset(Itemset new_itemset) {
		// check if the sensitive_itemsets arraylist contains the item or not
		if (this.sensitive_itemsets.contains(new_itemset)) {
			// get the index of the sensitive itemset
			int index_of_new_itemset = this.sensitive_itemsets
					.indexOf(new_itemset);
			// decrement the value of the
			Integer temp = this.support_sensitive_itemsets
					.get(index_of_new_itemset);
			// if support is 1 or below, set it to 0
			if (temp.intValue() <= 1) {
				temp = new Integer(0);
			} else {
				temp = new Integer(temp.intValue() - 1);
			}

			return true;
		} else {
			return false;
		}
	}

	// a public function that returns the arraylist that contains the sensitive
	// itemsets
	public ArrayList<Itemset> get_sensitive_itemsets() {
		return this.sensitive_itemsets;
	}

	// a public function that returns the arraylist that contains the sensitive
	// itemsets
	public ArrayList<Integer> get_support_sensitive_itemsets() {
		return this.support_sensitive_itemsets;
	}

	// a public function that returns the main node of the current node
	public Node get_main_node() {
		return this.main_node;
	}

	// apublic function that sets the main node of the current node
	public Boolean set_main_node(Node new_main_node) {
		this.main_node = new_main_node;
		return true;
	}

	// a public funciton that finds the leaf nodes of this Node
	public ArrayList<Node> return_leaves() {

		ArrayList<Node> leaves = new ArrayList<Node>();
		ArrayList<Node> leaves_temp = new ArrayList<Node>();

		// if this node has no children set leaves to this node and return

		if (this.get_children().isEmpty()) {
			leaves.add(this);
			return leaves;
		} else {
			for (Node child : this.get_children()) {
				leaves_temp.clear();
				leaves_temp = child.return_leaves();
				for (Node temp_leaf : leaves_temp) {
					if (temp_leaf.get_type() == "leaf") {
						leaves.add(temp_leaf);
					}
				}
			}
			return leaves;
		}

	}

	// a public funciton that finds the old leaf nodes of this Node
	public ArrayList<Node> return_old_leaves() {

		ArrayList<Node> old_leaves = new ArrayList<Node>();
		ArrayList<Node> old_leaves_temp = new ArrayList<Node>();

		// if this node has no children set leaves to this node and return

		if (this.get_children().isEmpty()) {
			old_leaves.add(this);
			return old_leaves;
		} else {
			for (Node child : this.get_children()) {
				old_leaves_temp.clear();
				old_leaves_temp = child.return_leaves();
				for (Node temp_leaf : old_leaves_temp) {
					if (temp_leaf.get_type() == "old") {
						old_leaves.add(temp_leaf);
					}
				}
			}
			return old_leaves;
		}

	}

	public ArrayList<Node> return_all_children_nodes() {

		ArrayList<Node> all_children = new ArrayList<Node>();
		ArrayList<Node> all_children_temp = new ArrayList<Node>();

		for (Node child : this.get_children()) {
			all_children_temp.clear();
			all_children_temp = child.return_all_children_nodes();
			for (Node temp_child : all_children_temp) {
				if (!all_children.contains(temp_child)) {
					all_children.add(temp_child);
				}
			}
		}

		return all_children;

	}

	// a public funciton that affects the Node to inherit another node sensitive
	// itemset with the support level
	public void inherit_node(Node node) {

		for (Itemset temp_itemset : node.get_sensitive_itemsets()) {
			int index = node.get_sensitive_itemsets().indexOf(temp_itemset);
			int temp2 = node.get_support_sensitive_itemsets().get(index);

			for (int i = 0; i < temp2; i++) {
				this.add_sensitive_itemset(temp_itemset);
			}

		}
	}

	// a public function that reruns all the parents of the node until the root
	public ArrayList<Node> return_all_parents_till_root(Node node) {
		ArrayList<Node> parents = new ArrayList<Node>();
		Node parent = node.get_parent();
		while (parent != Main.main_fp_tree.root) {
			parents.add(parent);
			parent = parent.get_parent();
		}

		return parents;
	}

	// a public function that returns the child of the passed parent node
	// that matches the passed item
	public Node get_child_for_item(Integer item) {
		for (Node child : this.get_children()) {
			if (child.get_item() == item) {
				return child;
			}
		}
		return null;
	}

	// calculate the total support of children of the node
	public int get_all_children_support() {

		int all_support = 0;
		for (Node child : this.children) {
			all_support = child.get_support() + all_support;
		}
		return all_support;
	}

	public boolean get_inherit() {
		return this.inherit;
	}

	public boolean set_inherit(boolean inherit_value) {
		this.inherit = inherit_value;
		return true;
	}
}
