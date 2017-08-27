package at.hakkon.space.datamodel.room;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
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
		health = level * 40;
		maxHealth = health;
		healthRegeneration = level;
	}

	public boolean istDestroyed() {
		return health <= 0;
	}

	public void regenerate() {
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
		if (attacker.hits(defender)) {
			int weaponDamage = defender.getWeaponRoom() == null ? 0 : Math.round(weapon.getDamage() * attacker.getWeaponRoom().getEffectiveEfficency());
			int damageReduction = defender.getMechanicRoom() == null ? 0 : (int) Math.floor(defender.getMechanicRoom().getEffectiveEfficency());

			int totalDamage = weaponDamage - damageReduction;

			//Update ship health
			defender.updateHealth(-totalDamage);

			//Update Room health
			health = Math.max(health -totalDamage,0);

			return new AttackResult(true,Math.max(0,totalDamage));
		} else {
			return new AttackResult(false,0);
		}
	}

	protected abstract float getEfficency();

	public float getEffectiveEfficency() {

		float efficiency = getEfficency();

		float p1 = maxHealth / 100f;
		float pOkay = health / p1;

		return efficiency * pOkay / 100;
	}


	public int getLevel() {
		return level;
	}

	public int getUpgradeCosts() {
		return level * 100;
	}

	public void upgrade() {
		ApplicationClass.getInstance().updateShipMoney(-getUpgradeCosts());
		level++;

		ApplicationClass.getInstance().requestNotifyShipChangedEvent();
	}

	public abstract String getUpgradeInformationText();

	public int getMaxHealth(){
		return maxHealth;
	}
}