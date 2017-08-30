package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;
import java.util.Random;

import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Loot;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.room.MechanicRoom;
import at.hakkon.space.datamodel.room.NavigationRoom;
import at.hakkon.space.datamodel.room.GeneratorRoom;
import at.hakkon.space.datamodel.room.WeaponRoom;

/**
 * Created by Markus on 03.08.2017.
 */

public class EnemyShipA extends AbsShip {

	protected final static int START_HEALTH = 25;

	public EnemyShipA(String name, int level) {
		super(name, level, START_HEALTH  + 15 * (level -1));

		Random random = new Random();

		if (level == 1) {
			addInventory(Weapon.getLaser(1));
			if (random.nextBoolean()){
				addInventory(Weapon.getRocket(1));
			}
		}else if (level == 2) {
			addInventory(Weapon.getLaser(2));
			addInventory(Weapon.getRocket(3));
		}else if (level == 3) {
			addInventory(Weapon.getLaser(2));
			addInventory(Weapon.getLaser(2));
			addInventory(Weapon.getRocket(3));

		} else if (level >= 4) {
			addInventory(Weapon.getLaser(3));
			addInventory(Weapon.getLaser(3));
			addInventory(Weapon.getRocket(2));
		} else if (level >= 6) {
			addInventory(Weapon.getLaser(4));
			addInventory(Weapon.getLaser(2));
			addInventory(Weapon.getRocket(4));
		} else if (level >= 8) {
			addInventory(Weapon.getLaser(4));
			addInventory(Weapon.getLaser(4));
			addInventory(Weapon.getLaser(4));
			addInventory(Weapon.getRocket(6));
		} else if (level >= 10) {
			addInventory(Weapon.getLaser( 2 + level/2));
			addInventory(Weapon.getLaser( 2 + level/2));
			addInventory(Weapon.getRocket(2 + level/2));
			addInventory(Weapon.getRocket(2 + level/2));
		}

	}

	@Override
	public EShipType getShipType() {
		return EShipType.Enemy_A;
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
		rooms.add(new NavigationRoom(this, (int) (Math.ceil((float)getLevel()/2))));
		rooms.add(new WeaponRoom(this, (int) Math.ceil((float)getLevel()/2)));
		rooms.add(new GeneratorRoom(this, (int) (Math.ceil((float)getLevel()/2))));
		rooms.add(new MechanicRoom(this, (int) Math.ceil((float)getLevel()/2)));

		return rooms;
	}

	@Override
	public Loot getLoot() {
		Random random = new Random();
		int randCash = (random.nextInt(150 - 100) + 100) * getLevel();
		int randFuel = random.nextInt(4) + getLevel();

		ArrayList<IInventoryItem> items = new ArrayList<>();

		//float efficiency = getWeaponRoom().getEfficencyInPercent();

		for (IInventoryItem item: getInventory()){
			if (random.nextInt(8) == 2){
				item.equip(false);
				items.add(item);
			}
		}

		return new Loot(randCash, randFuel, items);
	}



}
