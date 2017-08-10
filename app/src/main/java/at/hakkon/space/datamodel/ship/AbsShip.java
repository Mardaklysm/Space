package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;

import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Loot;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsShip {

	private String name;
	private int health;
	private int level;

	protected ArrayList<Person> persons = new ArrayList<>();
	protected ArrayList<AbsRoom> rooms = new ArrayList<>();
	protected ArrayList<IInventoryItem> inventory = new ArrayList<>();

	public AbsShip(String name, int level, int health, ArrayList<Person> persons, ArrayList<AbsRoom> rooms) {
		this.name = name;
		this.health = health;

		this.persons = persons;
		this.rooms = rooms;
		this.level = level;

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

	public abstract EShipType getShipType();

	public int getInitialEnergy() {
		return 5 + level;
	}

	public abstract Loot getLoot();

	public int getLevel() {
		return level;
	}

	public int getTotalWeaponDamage(){
		int damage = 0;

		for (IInventoryItem item: inventory){
			if (item instanceof  Weapon){
				damage += ((Weapon) item).getDamage();
			}
		}

		return damage;
	}
}
