package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.EGameOverReason;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.room.EmptyRoom;
import at.hakkon.space.datamodel.room.NavigationRoom;
import at.hakkon.space.datamodel.room.WeaponRoom;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 03.08.2017.
 */

public class PlayerShip extends AbsShip {

	protected final static int START_HEALTH = 1000;

	protected final static int START_FUEL = 50;
	protected final static int START_MONEY = 250;


	private AbsPlanet currentPlanet;

	private int money;
	private int fuel;
	private AbsPlanet startPlanet;

	public PlayerShip(String name) {
		super(name, START_HEALTH, getInitPersons(), getInitRooms());
		this.money = START_MONEY;
		this.fuel = START_FUEL;

		inventory.add(Weapon.getLaser(1));
		inventory.add(Weapon.getLaser(1));
		inventory.add(Weapon.getRocket(1));

	}

	private static ArrayList<Person> getInitPersons() {
		ArrayList<Person> persons = new ArrayList<>();

		persons.add(new Person("Markus"));
		persons.add(new Person("Fabian"));

		return persons;
	}

	private static ArrayList<AbsRoom> getInitRooms() {
		ArrayList<AbsRoom> rooms = new ArrayList<>();
		rooms.add(new NavigationRoom("Cheap Navigation room"));
		rooms.add(new EmptyRoom("Empty Hall I"));
		rooms.add(new EmptyRoom("Empty Hall II"));

		WeaponRoom weaponRoom = new WeaponRoom("Armory");


		rooms.add(weaponRoom);

		return rooms;
	}

	@Override
	public String getInformationDump() {
		String retString = "";

		retString += getName() + " (" + getMoney() + "€)\n";
		retString += "Health: " + getHealth() + "\n";
		retString += "Fuel: " + getFuel() + "\n";

		retString += "\n";

		for (Person person : getPersons()) {
			retString += person.getInformationDump();
			retString += "\n\n";
		}


		for (AbsRoom room : getRooms()) {
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

	public void updateMoney(int resourceBonus) {
		this.money += resourceBonus;
		ApplicationClass.getInstance().requestNotifyShipChangedEvent();
	}

	public int getMoney() {
		return money;
	}

	public boolean moveToPlanet(AbsPlanet planet) {
		if (currentPlanet.equals(planet)) {
			return false;
		}

		if (!canMoveToPlanet(planet)) {
			return false;
		}

		ApplicationClass.getInstance().updateFuel(-(Utility.getTravelCost(this, planet)));

		currentPlanet = planet;
		return true;
	}

	public int updateFuel(int fuel) {
		this.fuel += fuel;

		if (this.fuel <= 0) {
			ApplicationClass.getInstance().gameOver(EGameOverReason.OutOfFuel);
		}

		return fuel;
	}

	public boolean canMoveToPlanet(AbsPlanet planet) {

		int fuelCost = Utility.getTravelCost(this, planet);

		if (fuel - fuelCost < 0) {
			return false;
		}
		return true;
	}

	public AbsPlanet getCurrentPlanet() {
		return currentPlanet;
	}


	public int getFuel() {
		return fuel;
	}


	public void setStartPlanet(AbsPlanet startPlanet) {
		this.startPlanet = startPlanet;
	}

	public void setCurrentPlanet(AbsPlanet currentPlanet) {
		this.currentPlanet = currentPlanet;
	}

	@Override
	public void addInventory(IInventoryItem item) {
		super.addInventory(item);
		ApplicationClass.getInstance().requestNotifyShipChangedEvent();
	}

	@Override
	public boolean removeInventory(IInventoryItem item) {
		boolean result = super.removeInventory(item);
		if (result){
			ApplicationClass.getInstance().requestNotifyShipChangedEvent();
		}

		return result;
	}

	@Override
	public EShipType getShipType() {
		return EShipType.Player_A;
	}

}