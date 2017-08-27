package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;
import java.util.Random;

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

	protected final static int START_HEALTH = 60;

//	public EnemyShipA(String name, int level, int health, ArrayList<Person> persons, ArrayList<AbsRoom> rooms) {
//		super(name, level, health, persons, rooms);
//	}

	public EnemyShipB(String name, int level) {
		super(name, level, START_HEALTH * level/2, getInitPersons(level), getInitRooms(level));

		if (level == 1) {
			addInventory(Weapon.getNuke(1));
		} else if (level >= 2) {
			addInventory(Weapon.getNuke(2));
		} else if (level >= 4) {
			addInventory(Weapon.getNuke(2));
		} else if (level >= 6) {
			addInventory(Weapon.getNuke(2));
		} else if (level >= 8) {
			addInventory(Weapon.getNuke(3));
		} else if (level >= 10) {
			addInventory(Weapon.getNuke(4));
		}

	}

	@Override
	public EShipType getShipType() {
		return EShipType.Enemy_B;
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

		/**
		if (true){
			rooms.add(new NavigationRoom(3 + level/2));
			rooms.add(new WeaponRoom(2 + level/2));
			rooms.add(new MachanicRoom(1 + level/2));
			rooms.add(new ShieldRoom(1 + level/2));
		}
		 **/


		if (level <= 2) {
			rooms.add(new NavigationRoom(3));
			rooms.add(new WeaponRoom(2));
			rooms.add(new MechanicRoom(3));
			rooms.add(new GeneratorRoom(2));
		}else if (level <= 3) {
			rooms.add(new NavigationRoom(4));
			rooms.add(new WeaponRoom(5));
			rooms.add(new MechanicRoom(4));
			rooms.add(new GeneratorRoom(4));
		}else if (level <= 4) {
			rooms.add(new NavigationRoom(6));
			rooms.add(new WeaponRoom(8));
			rooms.add(new MechanicRoom(6));
			rooms.add(new GeneratorRoom(6));
		}else if (level <= 6) {
			rooms.add(new NavigationRoom(8));
			rooms.add(new WeaponRoom(12));
			rooms.add(new MechanicRoom(10));
			rooms.add(new GeneratorRoom(8));
		}else {
			rooms.add(new NavigationRoom(8 + level/2));
			rooms.add(new WeaponRoom(10 + level/2));
			rooms.add(new MechanicRoom(8 + level/2));
			rooms.add(new GeneratorRoom(8 + level/2));
		}

		return rooms;
	}

	@Override
	public Loot getLoot() {
		Random random = new Random();
		int randCash = (random.nextInt(150 - 100)+100) * getLevel();
		int randFuel = random.nextInt(4) + getLevel();

		return new Loot(randCash, randFuel, getInventory());
	}


}
