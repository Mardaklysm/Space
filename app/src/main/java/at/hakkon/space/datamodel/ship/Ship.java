package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.EGameOverReason;
import at.hakkon.space.utility.Utility;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.room.EmptyRoom;
import at.hakkon.space.datamodel.room.NavigationRoom;
import at.hakkon.space.datamodel.room.WeaponRoom;

/**
 * Created by Markus on 29.07.2017.
 */

public class Ship {

	private AbsPlanet startPlanet;
	private AbsPlanet currentPlanet;

	private final static int START_HEALTH = 1000;
	private final static int START_FUEL = 2;
	private final static int START_MONEY = 250;

	private String name;
	private int health;
	private int fuel;
	private int money;

	private ArrayList<Person> persons = new ArrayList<>();
	private ArrayList<AbsRoom> rooms = new ArrayList<>();
	private ArrayList<IInventoryItem> inventory = new ArrayList<>();

	public Ship(String name, int health, int fuel, int money, ArrayList<Person> persons, ArrayList<AbsRoom> rooms, AbsPlanet startPlanet) {
		this.name = name;
		this.health = health;
		this.fuel = fuel;
		this.money = money;

		this.persons = persons;
		this.rooms = rooms;

		this.currentPlanet = startPlanet;
		this.startPlanet = startPlanet;
	}

	public static Ship getDefault(String name) {
		ArrayList<Person> persons = new ArrayList<>();

		persons.add(new Person("Markus"));
		persons.add(new Person("Fabian"));

		ArrayList<AbsRoom> rooms = new ArrayList<>();
		rooms.add(new NavigationRoom("Cheap Navigation room"));
		rooms.add(new EmptyRoom("Empty Hall I"));
		rooms.add(new EmptyRoom("Empty Hall II"));

		WeaponRoom weaponRoom = new WeaponRoom("Armory");


		rooms.add(weaponRoom);

		Galaxy galaxy = ApplicationClass.getInstance().getGalaxy();
		AbsPlanet startPlanet = galaxy.getReachablePlanets().get(0);
		startPlanet.getEvent().setEnabled(false);

		Ship ship = new Ship(name, START_HEALTH, START_FUEL, START_MONEY, persons, rooms, startPlanet);
		ship.addInventory(new Weapon("Simple Laser", 100, 15, 5));
		ship.addInventory(new Weapon("Simple Rocket", 100, 45, 7));
		return ship;
	}

	public String getInformationDump() {
		String retString = "";

		retString += name + " (" + money + "€)\n";
		retString += "Health: " + health + "\n";
		retString += "Fuel: " + fuel + "\n";

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

	public boolean moveToPlanet(AbsPlanet planet) {
		if (currentPlanet.equals(planet)) {
			return false;
		}

		if (!canMoveToPlanet(planet)) {
			return false;
		}

		updateFuel(-planet.getTravelCosts());

		currentPlanet = planet;
		return true;
	}

	public int updateFuel(int fuel){
		this.fuel += fuel;

		if (fuel <= 0){
			ApplicationClass.getInstance().gameOver(EGameOverReason.OutOfFuel);
		}

		return fuel;
	}

	public boolean canMoveToPlanet(AbsPlanet planet) {
		int fuelCost = planet.getTravelCosts();

		if (fuel - fuelCost < 0) {
			return false;
		}
		return true;
	}

	public AbsPlanet getCurrentPlanet() {
		return currentPlanet;
	}

	public void updateMoney(int resourceBonus) {
		this.money += resourceBonus;
		ApplicationClass.getInstance().requestNotifyShipChangedEvent();
	}

	public int getFuel() {
		return fuel;
	}


	public void addInventory(IInventoryItem item) {
		if (!inventory.contains(item)) {
			inventory.add(item);
		}
		ApplicationClass.getInstance().requestNotifyShipChangedEvent();
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
}
