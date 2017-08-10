package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;

import at.hakkon.space.datamodel.inventory.Loot;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.room.EmptyRoom;
import at.hakkon.space.datamodel.room.NavigationRoom;
import at.hakkon.space.datamodel.room.WeaponRoom;

/**
 * Created by Markus on 03.08.2017.
 */

public class EnemyShipA extends AbsShip {

	protected final static int START_HEALTH = 25;

//	public EnemyShipA(String name, int level, int health, ArrayList<Person> persons, ArrayList<AbsRoom> rooms) {
//		super(name, level, health, persons, rooms);
//	}

	public EnemyShipA(String name, int level) {
		super(name, level, START_HEALTH * level, getInitPersons(level), getInitRooms(level));

		if (level == 1) {
			addInventory(Weapon.getLaser(1));
		} else if (level >= 2) {
			addInventory(Weapon.getLaser(1));
			addInventory(Weapon.getRocket(1));
		} else if (level >= 4) {
			addInventory(Weapon.getLaser(1));
			addInventory(Weapon.getLaser(1));
			addInventory(Weapon.getRocket(1));
		} else if (level >= 6) {
			addInventory(Weapon.getLaser(2));
			addInventory(Weapon.getRocket(1));
		} else if (level >= 8) {
			addInventory(Weapon.getLaser(3));
			addInventory(Weapon.getRocket(1));
		} else if (level >= 10) {
			addInventory(Weapon.getLaser(2));
			addInventory(Weapon.getLaser(2));
			addInventory(Weapon.getRocket(3));
		}

	}

	@Override
	public EShipType getShipType() {
		return EShipType.Enemy_A;
	}

	private static ArrayList<Person> getInitPersons(int level) {
		ArrayList<Person> persons = new ArrayList<>();

		persons.add(new Person("Random Eenemy I"));

		if (level >= 2) {
			persons.add(new Person("Random Eenemy II"));
		}

		if (level >= 4) {
			persons.add(new Person("Random Eenemy III"));
		}

		if (level >= 6) {
			persons.add(new Person("Random Eenemy IV"));
		}

		if (level >= 8) {
			persons.add(new Person("Random Eenemy V"));
		}

		if (level >= 10) {
			persons.add(new Person("Random Eenemy VI"));
		}

		return persons;
	}

	private static ArrayList<AbsRoom> getInitRooms(int level) {
		ArrayList<AbsRoom> rooms = new ArrayList<>();


		rooms.add(new NavigationRoom("Navigation I"));
		rooms.add(new EmptyRoom("Hall I"));
		rooms.add(new WeaponRoom("Armory I"));

		if (level > 2) {
			rooms.add(new EmptyRoom("Hall II"));
		}

		if (level > 4) {
			rooms.add(new EmptyRoom("Hall III"));
		}

		if (level > 6) {
			rooms.add(new EmptyRoom("Hall IV"));
		}

		if (level > 8) {
			rooms.add(new EmptyRoom("Hall V"));
		}

		if (level > 10) {
			rooms.add(new EmptyRoom("Hall VI"));
		}

		return rooms;
	}

	@Override
	public Loot getLoot() {
		return new Loot(100 * getLevel(), 3 * getLevel(), getInventory());
	}


}