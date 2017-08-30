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

public class EnemyBossShipA extends AbsShip {

	protected final static int START_HEALTH = 5;

	public EnemyBossShipA(String name, int level) {
		super(name, level, START_HEALTH + 3 * level);

		Random random = new Random();

		if (level == 1) {
			addInventory(Weapon.getLaser(1));
			addInventory(Weapon.getNuke(1));
		} else if (level == 2) {
			addInventory(Weapon.getRocket(1));
			addInventory(Weapon.getNuke(1));
			addInventory(Weapon.getNuke(1));
		} else if (level == 3) {
			addInventory(Weapon.getLaser(3));
			addInventory(Weapon.getRocket(3));
			addInventory(Weapon.getNuke(2));

		} else if (level >= 4) {
			addInventory(Weapon.getLaser(3));
			addInventory(Weapon.getLaser(4));
			addInventory(Weapon.getRocket(4));
			addInventory(Weapon.getNuke(2));
		} else if (level >= 6) {
			addInventory(Weapon.getLaser(5));
			addInventory(Weapon.getLaser(5));
			addInventory(Weapon.getRocket(5));
			addInventory(Weapon.getNuke(3));
		} else if (level >= 8) {
			addInventory(Weapon.getLaser(6));
			addInventory(Weapon.getLaser(6));
			addInventory(Weapon.getLaser(6));
			addInventory(Weapon.getRocket(6));
			addInventory(Weapon.getNuke(4));
		} else if (level >= 10) {
			addInventory(Weapon.getLaser(4 + level / 2));
			addInventory(Weapon.getLaser(4 + level / 2));
			addInventory(Weapon.getRocket(4 + level / 2));
			addInventory(Weapon.getRocket(4 + level / 2));
			addInventory(Weapon.getNuke(Math.min(10,level/2)));
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

		rooms.add(new NavigationRoom(this, 2 + getLevel()));
		rooms.add(new WeaponRoom(this, 2 + getLevel()));
		rooms.add(new GeneratorRoom(this, 2 + getLevel()));
		rooms.add(new MechanicRoom(this, 2 + getLevel()));

		return rooms;
	}

	@Override
	public Loot getLoot() {
		Random random = new Random();
		int randCash = (random.nextInt(150 - 100) + 100) * getLevel() * 3;

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
