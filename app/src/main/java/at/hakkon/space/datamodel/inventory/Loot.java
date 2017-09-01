package at.hakkon.space.datamodel.inventory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Markus on 05.08.2017.
 */

public class Loot implements Serializable{

	private int money = 0;
	private int fuel = 0;

	private ArrayList<IInventoryItem> items = new ArrayList<>();


	public Loot(int money, int fuel, ArrayList<IInventoryItem> items){
		this.money = money;
		this.fuel = fuel;
		this.items = items;
	}

	public void addItem(IInventoryItem item){
		if (!items.contains(item)){
			item.equip(false);
			items.add(item);
		}
	}

	public void removeItem(IInventoryItem item){
		items.remove(item);
	}

	public int updateMoney(int value){
		money += value;
		return money;
	}

	public int updateFuel(int value){
		fuel += value;
		return fuel;
	}

	public int getMoney() {
		return money;
	}

	public int getFuel() {
		return fuel;
	}

	public ArrayList<IInventoryItem> getItems() {
		return items;
	}
}
