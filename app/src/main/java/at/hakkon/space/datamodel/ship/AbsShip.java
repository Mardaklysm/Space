package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.EGameOverReason;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsShip {

	private String name;
	private int health;

	protected ArrayList<Person> persons = new ArrayList<>();
	protected ArrayList<AbsRoom> rooms = new ArrayList<>();
	protected ArrayList<IInventoryItem> inventory = new ArrayList<>();

	public AbsShip(String name, int health, ArrayList<Person> persons, ArrayList<AbsRoom> rooms) {
		this.name = name;
		this.health = health;

		this.persons = persons;
		this.rooms = rooms;

	}

	public AbsShip(String name){
		this.name = name;
	}


	public String getInformationDump() {
		String retString = "";

		retString += name + "\n";
		retString += "Health: " + health + "\n";

		retString += "\n";

		for (Person person : persons) {
			retString += person.getInformationDump();
			retString += "\n\n";
		}


		for (AbsRoom room : rooms) {
			retString += room.getInformationDump();
			retString += "\n";
		}

		ArrayList<Weapon> weapons = getWeapons();
		retString += "\nWeapons List (" + weapons.size() + ")\n";
		for (Weapon weapon : weapons) {
			retString += weapon.getInformationDump();
			retString += "\n";
		}

		return retString;
	}




	public void addInventory(IInventoryItem item) {
		if (!inventory.contains(item)) {
			inventory.add(item);
		}
	}

	public boolean removeInventory(IInventoryItem item) {
		return inventory.remove(item);
	}

	public ArrayList<Weapon> getWeapons() {
		ArrayList<Weapon> weapons = new ArrayList<>();

		for (IInventoryItem item : inventory) {
			if (item instanceof Weapon) {
				weapons.add((Weapon) item);
			}
		}

		return weapons;
	}


	public void updateHealth(int value) {
		health += value;
		if (health < 0) {
			ApplicationClass.getInstance().gameOver(EGameOverReason.OutOfHealth);
			Utility.getInstance().showTextDialog(ApplicationClass.getInstance().getBaseContext(), "You are dead!");
		}
		ApplicationClass.getInstance().requestNotifyShipChangedEvent();
	}

	public void addPerson(Person person) {
		if (!persons.contains(person)) {
			persons.add(person);
		}
	}



	public int getHealth() {
		return health;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Person> getPersons() {
		return persons;
	}

	public ArrayList<AbsRoom> getRooms() {
		return rooms;
	}

	public ArrayList<IInventoryItem> getInventory() {
		return inventory;
	}
}
