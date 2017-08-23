package at.hakkon.space.datamodel.room;

import java.util.ArrayList;

import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.ship.AbsShip;

public abstract class AbsRoom {

	private ArrayList<IInventoryItem> items = new ArrayList<>();

	private ERoom room;

	private int level = 1;

	private int maxHealth;
	private int health;
	private int healthRegeneration;

	public AbsRoom(int level) {
		this.level = level;
		health = level *20;
		maxHealth = health;
		healthRegeneration = level;
	}

	public boolean istDestroyed() {
		return health <= 0;
	}

	public void regenerate(){
		health = Math.min(health + healthRegeneration, maxHealth);
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
			int weaponDamage = Math.round (weapon.getDamage() * attacker.getWeaponRoom().getEffectiveEfficency());
			defender.updateHealth(-weaponDamage);
			health = Math.max(health -weaponDamage,0);
			return weaponDamage;
		} else {
			return -1;
		}
	}

	protected abstract float getEfficency();

	public float getEffectiveEfficency(){

		float efficiency = getEfficency();

		float p1 = maxHealth /100f;
		float pOkay = health/p1;
		//float pOkay = 1- pDamaged;



		return efficiency * pOkay/100;
	}




	public int getLevel() {
		return level;
	}

	public int getUpgradeCosts() {
		return level * 100;
	}

	public void upgrade() {

	}
}