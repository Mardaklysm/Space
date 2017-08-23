package at.hakkon.space.datamodel.room;

import java.util.ArrayList;

import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.ship.AbsShip;

public abstract class AbsRoom {

	private ArrayList<IInventoryItem> items = new ArrayList<>();

	private ERoom room;

	private int level = 1;

	private int health = 4;

	public AbsRoom(int level) {
		this.level = level;
	}

	public boolean istDestroyed() {
		return health == 0;
	}

	public void updateHealth(int value) {
		health += value;
		health = Math.max(0, health);
	}


	public abstract ERoom getRoomType();

	public void addInventory(IInventoryItem item) {
		if (!items.contains(item)) {
			items.add(item);
		}
	}

	public void removeInventory(IInventoryItem item) {
		items.remove(item);
	}

	public String getName() {
		return getRoomType().name() + " Lv." + level;
	}


	public String getInformationDump() {
		return getName();
	}

	public int getHealth() {
		return health;
	}

	public int attackWithWeapon(AbsShip attacker, AbsShip defender, Weapon weapon) {
		if (attacker.hits(defender)) {
			int weaponDamage = weapon.getDamage(attacker.getWeaponRoomLevel());
			defender.updateHealth(-weaponDamage);
			//health -= weaponDamage;
			return weaponDamage;
		} else {
			return -1;
		}
	}

	public abstract float getEfficency();

	public int getLevel() {
		return level;
	}
}