package com.jamasztal;

/*
 * This class defines an item.
 * 
 * @author Jan Masztal
 */
public class Item {
	private String name;
	private double price;
	private int qty;
	
	/*
	 * Construct an item with Null default values.
	 * 
	 */
	public Item(){
		this(null,0,0);
	}

	/*
	 * Construct an item with specific values.
	 *
	 * @param name The name of an item.
	 * @param price The cost of an item.
	 * @param qty The quantity of an item.
	 */
	public Item(String n, double p, int q){
		name = n;
		price = p;
		qty = q;
	}
	
	/*
	 * Return item name.
	 * 
	 * @return name
	 */
	public String getName(){
		return name;
	}
	
	/*
	 * Return item price.
	 * 
	 * @return price
	 */
	public double getPrice(){
		return price;
	}
	
	/*
	 * Return item quantity.
	 * 
	 * @return qty
	 */
	public double getQty(){
		return qty;
	}
	
	/*
	 * Set item name.
	 * 
	 * @param n
	 */
	public void setName(String n){
		name = n;
	}
	
	/*
	 * Set item price.
	 * 
	 * @param p
	 */
	public void setPrice(double p){
		price = p;
	}
	
	/*
	 * Set item quantity.
	 * 
	 * @param q
	 */
	public void setName(int q){
		qty = q;
	}
	
	/*
	 * Print item object
	 *
	 *@return itemString
	 */
	public String toString(){
		String itemString = "Item Name: " + name + "\n"
							+"Price: " + price + "\n"
							+"Qty: " + qty + "\n";
		return itemString;
	}
}
