package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.EGameOverReason;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Loot;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.room.MachanicRoom;
import at.hakkon.space.datamodel.room.NavigationRoom;
import at.hakkon.space.datamodel.room.GeneratorRoom;
import at.hakkon.space.datamodel.room.WeaponRoom;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 03.08.2017.
 */

public class PlayerShip extends AbsShip {

	protected final static int START_HEALTH = 100;

	protected final static int START_FUEL = 300;
	protected final static int START_MONEY = 500;


	private AbsPlanet currentPlanet;

	private int money;
	private int fuel;
	private AbsPlanet startPlanet;

	public PlayerShip(String name, int level) {
		super(name, level, START_HEALTH, getInitPersons(), getInitRooms());
		this.money = START_MONEY;
		this.fuel = START_FUEL;

		Weapon laser1 = Weapon.getLaser(1);
		laser1.equip(true);
		inventory.add(laser1);

		Weapon laser2 = Weapon.getLaser(1);
		laser2.equip(true);
		inventory.add(laser2);

		Weapon rocket1 = Weapon.getRocket(1);
		rocket1.equip(true);
		inventory.add(rocket1);
	}

	private static ArrayList<Person> getInitPersons() {
		ArrayList<Person> persons = new ArrayList<>();

		persons.add(new Person("Markus"));
		persons.add(new Person("Fabian"));

		return persons;
	}

	private static ArrayList<AbsRoom> getInitRooms() {
		ArrayList<AbsRoom> rooms = new ArrayList<>();
		rooms.add(new NavigationRoom(1));
		rooms.add(new MachanicRoom(1));
		rooms.add(new GeneratorRoom(1));
		rooms.add(new WeaponRoom(1));

		return rooms;
	}

	@Override
	public String getInformationDump() {
		String retString = "";

		retString += "Crew Members\n";

		for (Person person : getPersons()) {
			retString += person.getInformationDump();
			retString += "\n\n";
		}

		return retString;
	}


	public String getInformationDump2() {
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
		if (result) {
			ApplicationClass.getInstance().requestNotifyShipChangedEvent();
		}

		return result;
	}

	@Override
	public EShipType getShipType() {
		return EShipType.Player_A;
	}

	@Override
	public Loot getLoot() {
		return new Loot(money, fuel, getInventory());
	}

	@Override
	public void updateHealth(int value) {
		super.updateHealth(value);

		if (getHealth() <= 0) {
			ApplicationClass.getInstance().gameOver(EGameOverReason.OutOfHealth);
		}
	}


}