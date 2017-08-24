package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;
import java.util.Random;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Loot;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.room.MachanicRoom;
import at.hakkon.space.datamodel.room.NavigationRoom;
import at.hakkon.space.datamodel.room.WeaponRoom;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsShip {

	private String name;
	private int health;
	private int maxHealth;
	private int level;

	protected ArrayList<Person> persons = new ArrayList<>();
	protected ArrayList<AbsRoom> rooms = new ArrayList<>();
	protected ArrayList<IInventoryItem> inventory = new ArrayList<>();

	public AbsShip(String name, int level, int health, ArrayList<Person> persons, ArrayList<AbsRoom> rooms) {
		this.name = name;
		this.health = health;
		this.maxHealth = health;

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

	public ArrayList<Weapon> getEquippedWeapons() {
		ArrayList<Weapon> weapons = new ArrayList<>();

		for (IInventoryItem item : inventory) {
			if ((item instanceof Weapon )&& (((Weapon)item).isEquipped())) {
				weapons.add((Weapon) item);
			}
		}

		return weapons;
	}


	public void updateHealth(int value) {
		int newHealth = health + value;

		if (newHealth <0){
			newHealth = 0;
		}
		if (newHealth > maxHealth){
			newHealth = maxHealth;
		}
		health = newHealth;

		if (this instanceof PlayerShip){
			ApplicationClass.getInstance().requestNotifyShipChangedEvent();
		}
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

	public int getWeaponRoomLevel() {
		for (AbsRoom room : getRooms()) {
			if (room instanceof WeaponRoom) {
				return room.getLevel();
			}
		}
		return 1;
	}

	public WeaponRoom getWeaponRoom() {
		for (AbsRoom room : getRooms()) {
			if (room instanceof WeaponRoom) {
				return (WeaponRoom) room;
			}
		}
		return null;
	}

	public MachanicRoom getMechanicRoom() {
		for (AbsRoom room : getRooms()) {
			if (room instanceof MachanicRoom) {
				return (MachanicRoom) room;
			}
		}
		return null;
	}

	public NavigationRoom getNavigationRoom() {
		for (AbsRoom room : getRooms()) {
			if (room instanceof NavigationRoom) {
				return (NavigationRoom) room;
			}
		}
		return null;
	}

	public boolean hits(AbsShip defender) {
		float evadeChanche = defender.getNavigationRoom().getEffectiveEfficency();

		Random random = new Random();
		float hitValue =  random.nextFloat();

		return hitValue > evadeChanche;
	}
}
