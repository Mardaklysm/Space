package at.hakkon.space.datamodel.rooms;

import java.util.ArrayList;

import at.hakkon.space.datamodel.IInventoryItem;

public abstract class AbsRoom {

	private String name;

	private ArrayList<IInventoryItem> items = new ArrayList<>();

	private ERoom room;

	public AbsRoom(String name) {
		this.name = name;
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

		retString += "Room [" + getName() + "(Type: " + getRoomType().name() + ")]\n";

		return retString;
	}
}