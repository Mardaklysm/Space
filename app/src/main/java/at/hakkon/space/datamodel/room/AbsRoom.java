package at.hakkon.space.datamodel.room;

import java.util.ArrayList;

import at.hakkon.space.datamodel.inventory.IInventoryItem;

public abstract class AbsRoom {

	private String name;

	private ArrayList<IInventoryItem> items = new ArrayList<>();

	private ERoom room;

	private int health = 4;


	public AbsRoom(String name) {
		this.name = name;
	}

	public boolean istDestroyed(){
		return health == 0;
	}

	public void updateHealth(int value){
		health += value;
		health = Math.max(0, health);
	}


	public abstract ERoom getRoomType();

	public void addInventory(IInventoryItem item) {
		if (!items.contains(item)) {
			items.add(item);
		}
	}

	public void removeInventory(IInventoryItem item) {
		items.remove(item);
	}

	public String getName() {
		return name;
	}


	public String getInformationDump() {
		String retString = "";

		retString += "Room [" + getName() + "(Type: " + getRoomType().name() + ")]";

		return retString;
	}

	public int getHealth() {
		return health;
	}
}