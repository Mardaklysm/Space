package at.hakkon.space.datamodel;

import java.util.ArrayList;

import at.hakkon.space.ApplicationClass;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.datamodel.rooms.AbsRoom;
import at.hakkon.space.datamodel.rooms.EmptyRoom;
import at.hakkon.space.datamodel.rooms.NavigationRoom;
import at.hakkon.space.datamodel.rooms.WeaponRoom;

/**
 * Created by Markus on 29.07.2017.
 */

public class Ship {

	private AbsPlanet startPlanet;
	private AbsPlanet currentPlanet;

	private final static int START_HEALTH = 1000;
	private final static int START_FUEL = 10;
	private final static int START_MONEY = 250;

	private String name;
	private int health;
	private int fuel;
	private int money;

	private ArrayList<Person> persons = new ArrayList<>();
	private ArrayList<AbsRoom> rooms = new ArrayList<>();

	public Ship(String name, int health, int fuel, int money, ArrayList<Person> persons, ArrayList<AbsRoom> rooms, AbsPlanet startPlanet) {
		this.name = name;
		this.health = health;
		this.fuel = fuel;
		this.money = money;

		this.persons = persons;
		this.rooms = rooms;

		this.currentPlanet = startPlanet;
		this.startPlanet = startPlanet;
		startPlanet.setVisited(true);
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
		weaponRoom.addInventory(new Weapon("Simple Laser", 100, 15, 5));
		weaponRoom.addInventory(new Weapon("Simple Rocket", 100, 45, 7));

		rooms.add(weaponRoom);

		Galaxy galaxy = ApplicationClass.getInstance().getGalaxy();
		AbsPlanet startPlanet = galaxy.getReachablePlanets().get(0);
		return new Ship(name, START_HEALTH, START_FUEL, START_MONEY, persons, rooms, startPlanet);
	}

	public String getInformationDump() {
		String retString = "";

		retString += name + " (" + money + "€)\n";
		retString += "Health: " + health + "\n";
		retString += "Fuel: " + fuel + "\n";

		retString += "\n";

		for (Person person : persons) {
			retString += person.getInformationDump();
			retString += "\n";
		}

		retString += "\n";

		for (AbsRoom room : rooms) {
			retString += room.getInformationDump();
			retString += "\n";
		}

		return retString;
	}

	public boolean moveToPlanet(AbsPlanet planet) {
		if (currentPlanet.equals(planet)) {
			return false;
		}

		int fuelCost = planet.getTravelCosts();

		if (fuel - fuelCost < 0) {
			return false;
		}

		fuel = fuel - fuelCost;
		currentPlanet = planet;
		visit(planet);
		return true;
	}

	public AbsPlanet getCurrentPlanet() {
		return currentPlanet;
	}

	public void updateMoney(int resourceBonus) {
		this.money += resourceBonus;
	}


	public boolean visit(AbsPlanet planet) {
		return planet.visit();
	}
}
