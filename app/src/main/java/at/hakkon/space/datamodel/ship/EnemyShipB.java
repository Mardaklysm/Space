package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;
import java.util.Random;

import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Loot;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.room.GeneratorRoom;
import at.hakkon.space.datamodel.room.MechanicRoom;
import at.hakkon.space.datamodel.room.NavigationRoom;
import at.hakkon.space.datamodel.room.WeaponRoom;

/**
 * Created by Markus on 03.08.2017.
 */

public class EnemyShipB extends AbsShip {

	protected final static int START_HEALTH = 20;

//	public EnemyShipA(String name, int level, int health, ArrayList<Person> persons, ArrayList<AbsRoom> rooms) {
//		super(name, level, health, persons, rooms);
//	}

	public EnemyShipB(String name, int level) {
		super(name, level, START_HEALTH + 10 * (level - 1));

		Random random = new Random();

		if (level == 1) {
			addInventory(Weapon.getNuke(1));
		} else if (level == 2) {
			addInventory(Weapon.getNuke(1));
		} else if (level == 3) {
			addInventory(Weapon.getNuke(1));
			addInventory(Weapon.getLaser(1));
		} else if (level == 4) {
			addInventory(Weapon.getNuke(1));
			addInventory(Weapon.getNuke(1));
			addInventory(Weapon.getLaser(2));
		} else if (level >= 6) {
			addInventory(Weapon.getNuke(1));
			addInventory(Weapon.getNuke(2));
			addInventory(Weapon.getLaser(3));
		} else if (level >= 8) {
			addInventory(Weapon.getNuke(2));
			addInventory(Weapon.getNuke(3));
			addInventory(Weapon.getLaser(3));
		} else if (level >= 10) {
			addInventory(Weapon.getNuke(3 + level/10));
			addInventory(Weapon.getNuke(1));
			addInventory(Weapon.getLaser(level/2));
		}

	}

	@Override
	public EShipType getShipType() {
		return EShipType.Enemy_B;
	}


	@Override
	public Loot getLoot() {
		Random random = new Random();
		int randCash = (random.nextInt(150 - 100) + 100) * getLevel();
		int randFuel = random.nextInt(4) + getLevel();

		ArrayList<IInventoryItem> items = new ArrayList<>();
		for (IInventoryItem item : getInventory()) {
			if (random.nextInt(4) == 2) {
				item.equip(false);
				items.add(item);
			}
		}

		return new Loot(randCash, randFuel, items);
	}

	@Override
	protected ArrayList<Person> getInitPersons() {
		ArrayList<Person> persons = new ArrayList<>();

		persons.add(new Person("Random Eenemy I"));

		if (getLevel() >= 2) {
			persons.add(new Person("Random Eenemy II"));
		}

		if (getLevel() >= 4) {
			persons.add(new Person("Random Eenemy III"));
		}

		if (getLevel() >= 6) {
			persons.add(new Person("Random Eenemy IV"));
		}

		if (getLevel() >= 8) {
			persons.add(new Person("Random Eenemy V"));
		}

		if (getLevel() >= 10) {
			persons.add(new Person("Random Eenemy VI"));
		}

		return persons;
	}

	@Override
	protected ArrayList<AbsRoom> getInitRooms() {
		ArrayList<AbsRoom> rooms = new ArrayList<>();

		if (getLevel() <= 1) {
			rooms.add(new NavigationRoom(this, 3));
			rooms.add(new WeaponRoom(this, 2));
			rooms.add(new MechanicRoom(this, 1));
			rooms.add(new GeneratorRoom(this, 3));
		} else if (getLevel() <= 2) {
			rooms.add(new NavigationRoom(this, 4));
			rooms.add(new WeaponRoom(this, 4));
			rooms.add(new MechanicRoom(this, 2));
			rooms.add(new GeneratorRoom(this, 5));
		} else if (getLevel() <= 3) {
			rooms.add(new NavigationRoom(this, 5));
			rooms.add(new WeaponRoom(this, 5));
			rooms.add(new MechanicRoom(this, 3));
			rooms.add(new GeneratorRoom(this, 6));
		} else if (getLevel() <= 4) {
			rooms.add(new NavigationRoom(this, 6));
			rooms.add(new WeaponRoom(this, 8));
			rooms.add(new MechanicRoom(this, 4));
			rooms.add(new GeneratorRoom(this, 7));
		} else if (getLevel() <= 6) {
			rooms.add(new NavigationRoom(this, 8));
			rooms.add(new WeaponRoom(this, 12));
			rooms.add(new MechanicRoom(this, 5));
			rooms.add(new GeneratorRoom(this, 8));
		} else {
			rooms.add(new NavigationRoom(this, 8 + getLevel() / 2));
			rooms.add(new WeaponRoom(this, 10 + getLevel() / 2));
			rooms.add(new MechanicRoom(this, 6 + getLevel() / 2));
			rooms.add(new GeneratorRoom(this, 8 + getLevel() / 2));
		}

		return rooms;
	}


}
