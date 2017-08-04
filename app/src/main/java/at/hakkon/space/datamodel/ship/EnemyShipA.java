package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;

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

	protected final static int START_HEALTH = 250;

	public EnemyShipA(String name, int health, ArrayList<Person> persons, ArrayList<AbsRoom> rooms) {
		super(name, health, persons, rooms);
	}

	public EnemyShipA(String name) {
		super(name, START_HEALTH, getInitPersons(), getInitRooms());

		addInventory(Weapon.getLaser(1));
		addInventory(Weapon.getRocket(1));
	}

	@Override
	public EShipType getShipType() {
		return EShipType.Enemy_A;
	}

	private static ArrayList<Person> getInitPersons() {
		ArrayList<Person> persons = new ArrayList<>();

		persons.add(new Person("Random Eenemy I"));

		return persons;
	}

	private static ArrayList<AbsRoom> getInitRooms() {
		ArrayList<AbsRoom> rooms = new ArrayList<>();
		rooms.add(new NavigationRoom("Cheap Navigation room"));
		rooms.add(new EmptyRoom("Empty Hall I"));
		WeaponRoom weaponRoom = new WeaponRoom("Armory");

		rooms.add(weaponRoom);

		return rooms;
	}



}
