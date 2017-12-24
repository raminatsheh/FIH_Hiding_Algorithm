package fih;
public class Item {
	
	public Integer value;
	public Integer weight = 0;
	public Integer count = 0;
	public Integer count_itemsets = 0;
	private int occurences =0;
	
	public void set_value(int passed_value){
		this.value = passed_value;
	}
	public Integer get_value(){
		return this.value;
	}
	public Integer get_count(){
		return this.count;
	}
	public void set_count(int passed_count){
		this.count = passed_count;
	}
	public Integer get_count_itemsets(){
		return this.count_itemsets;
	}
	public void set_count_itemsets(int passed_count){
		this.count_itemsets = passed_count;
	}
	public void set_weight(int passed_weight){
		this.weight = passed_weight;
	}
	
	public Integer get_occurences(){
		return this.occurences;
	}
	
	public void increment_occurences(){
		this.occurences ++;
	}
	
	public Integer get_weight(){
		return this.weight;
	}
	public Item(Integer passed_value){
		this.value = passed_value;
	}
}
