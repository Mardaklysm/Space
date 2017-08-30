package at.hakkon.space.datamodel.room;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.ship.AbsShip;
import at.hakkon.space.utility.Utility;

public abstract class AbsRoom {

	private ArrayList<IInventoryItem> items = new ArrayList<>();

	private AbsShip ship;

	private ERoom room;

	private int level = 1;

	private int maxHealth;
	private int health;

	public AbsRoom(AbsShip ship, int level) {
		this.level = level;
		health = getMaxHealthForLevel(level);

		this.ship = ship;
		this.maxHealth = health;
	}

	public AbsShip getShip() {
		return ship;
	}

	public boolean istDestroyed() {
		return health <= 0;
	}

	public void regenerate() {
		int healthRegeneration = getShip().getMechanicRoom() == null ? 0 : (int) getShip().getMechanicRoom().getEffectiveEfficency();
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

	public AttackResult attackWithWeapon(AbsShip attacker, AbsShip defender, Weapon weapon) {
		if (attacker.hits(defender) || weapon.alwaysHits()) {
			int weaponDamage = defender.getWeaponRoom() == null ? 0 : (int) Math.round(weapon.getDamage() * attacker.getWeaponRoom().getEffectiveEfficency());
			//int damageReduction = defender.getMechanicRoom() == null ? 0 : (int) Math.floor(defender.getMechanicRoom().getEffectiveEfficency() / 2);
			int damageReduction = 0;
			int totalDamage = weaponDamage - damageReduction;

			//Update ship health
			defender.updateHealth(-totalDamage);

			//Update Room health
			health = Math.max(health - totalDamage, 0);

			//Handle OneTimeWeapons
			if (weapon.isOneTimeWeapon()) {
				attacker.removeInventory(weapon);
			}

			return new AttackResult(true, Math.max(0, totalDamage));
		} else {
			if (weapon.isOneTimeWeapon()) {
				attacker.removeInventory(weapon);
			}
			return new AttackResult(false, 0);
		}
	}

	public abstract double getEfficency();

	public double getEfficencyInPercent() {
		double p1 = maxHealth / 100f;
		double pOkay = health / p1;
		return pOkay;
	}

	public double getEffectiveEfficency() {

		double efficiency = getEfficency();

		if (efficiency == 0){
			return 0;
		}

		double p1 = maxHealth / 100f;
		double pOkay = health / p1;

		if (pOkay > 99) {
			pOkay = 100;
		}


		double result = efficiency * pOkay / 100;
		result = Utility.roundTwoDecimals(result);
		if (getRoomType() == ERoom.Mechanic) {
			return Math.max(1f, result);
		}

		return result;
	}


	public int getLevel() {
		return level;
	}

	public int getUpgradeCosts() {
		return level * 100;
	}

	public void upgrade() {
		upgrade(true);
	}

	public void upgrade(boolean pay) {
		if (pay) {
			ApplicationClass.getInstance().updateShipMoney(-getUpgradeCosts());
		}

		level++;

		ApplicationClass.getInstance().requestNotifyShipChangedEvent();
	}

	public abstract String getUpgradeInformationText();

	public int getMaxHealth() {
		return maxHealth;
	}

	public void regenerate(boolean full) {
		if (full) {
			health = maxHealth;
		} else {
			regenerate();
		}
	}

	abstract protected int getMaxHealthForLevel(int level);
}