package at.hakkon.space.datamodel.ship;

import java.util.ArrayList;
import java.util.Random;

import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Loot;
import at.hakkon.space.datamodel.inventory.weapon.WeaponLaser;
import at.hakkon.space.datamodel.inventory.weapon.WeaponNuke;
import at.hakkon.space.datamodel.inventory.weapon.WeaponRocket;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.room.GeneratorRoom;
import at.hakkon.space.datamodel.room.MechanicRoom;
import at.hakkon.space.datamodel.room.NavigationRoom;
import at.hakkon.space.datamodel.room.WeaponRoom;

/**
 * Created by Markus on 03.08.2017.
 */

public class EnemyBossShipA extends AbsShip {

	protected final static int START_HEALTH = 0;

	public EnemyBossShipA(String name, int level) {
		super(name, level, START_HEALTH + 100 * level);

		Random random = new Random();

		if (level == 1) {
			addInventory(new WeaponLaser(1));
			addInventory(new WeaponNuke(1));
		} else if (level == 2) {
			addInventory(new WeaponRocket(1));
			addInventory(new WeaponNuke(1));
		} else if (level == 3) {
			addInventory(new WeaponLaser(2));
			addInventory(new WeaponRocket(2));
			addInventory(new WeaponNuke(1));
		} else if (level >= 4) {
			addInventory(new WeaponLaser(2));
			addInventory(new WeaponRocket(3));
			addInventory(new WeaponNuke(2));
		} else if (level >= 6) {
			addInventory(new WeaponLaser(4));
			addInventory(new WeaponLaser(4));
			addInventory(new WeaponRocket(4));
			addInventory(new WeaponNuke(3));
		} else if (level >= 8) {
			addInventory(new WeaponLaser(6));
			addInventory(new WeaponLaser(6));
			addInventory(new WeaponLaser(6));
			addInventory(new WeaponRocket(6));
			addInventory(new WeaponNuke(4));
		} else if (level >= 10) {
			addInventory(new WeaponLaser(4 + level / 2));
			addInventory(new WeaponRocket(4 + level / 2));
			addInventory(new WeaponRocket(4 + level / 2));
			addInventory(new WeaponNuke(Math.min(10,level/2)));
		}

	}

	@Override
	public EShipType getShipType() {
		return EShipType.Enemy_Boss_A;
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

		rooms.add(new NavigationRoom(this, 1 + getLevel()));
		rooms.add(new WeaponRoom(this, 1 + getLevel()));
		rooms.add(new GeneratorRoom(this, 1 + getLevel()));
		rooms.add(new MechanicRoom(this, 1 + getLevel()));

		return rooms;
	}

	@Override
	public Loot getLoot() {
		Random random = new Random();
		int randCash = (random.nextInt(150 - 100) + 100) + getLevel() * 100;

		ArrayList<IInventoryItem> items = new ArrayList<>();



		//float efficiency = getWeaponRoom().getEfficencyInPercent();

		for (IInventoryItem item : getInventory()) {
			if (random.nextInt(4) == 2) {
				item.equip(false);
				items.add(item);
			}
		}

		return new Loot(randCash, 0, items);
	}


}
