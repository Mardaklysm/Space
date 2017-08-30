package at.hakkon.space.datamodel.inventory;

import at.hakkon.space.datamodel.room.AbsRoom;

/**
 * Created by Markus on 29.07.2017.
 */

public class Weapon implements IInventoryItem {

	private boolean alwaysHits = false;
	private String name;
	private int damage;
	private int level;

	private boolean isOneTimeWeapon = false;
	private boolean attacksAll = false;

	private int energyCost;

	public Weapon(int level, String name, int damage, int energyCost) {
		this.name = name;
		this.damage = damage;
		this.energyCost = energyCost;
		this.level = level;
		this.alwaysHits = false;
	}

	public Weapon(int level, String name, int damage, int energyCost, boolean isOneTimeWeapon, boolean attacksAll) {
		this(level, name, damage, energyCost);
		this.isOneTimeWeapon = isOneTimeWeapon;
		this.attacksAll = attacksAll;
	}


	@Override
	public String getName() {
		return name;
	}


	public int getDamage() {
		return damage;
	}

	public int getDamage(double modifier) {
		return (int) Math.round(damage * modifier);
	}


	public String getInformationDump() {
		String retString = "";

		retString += name + "( DMG: " + damage + ", Cost: " + energyCost + ")";
		return retString;
	}

	public static Weapon getLaser(int level) {
		return new Weapon(level, "Laser Lv." + level, 4 * level, 3 * level);
	}


	public static Weapon getRocket(int level) {
		return new Weapon(level, "Rocket Lv." + level, 2 * level, 3 * level, false, true);
	}

	public static Weapon getNuke(int level) {
		Weapon weapon = new Weapon(level, "Nuke Lv." + level, 10 + 5 * level, 10 + 3 * level, true, true);
		weapon.setAlwaysHits(true);
		return weapon;
	}

	public void setAlwaysHits(boolean alwaysHits) {
		this.alwaysHits = alwaysHits;
	}

	private AbsRoom target;

	public void setTarget(AbsRoom target) {
		this.target = target;
	}

	public AbsRoom getTarget() {
		return target;
	}

	public boolean hasTarget() {
		return target != null;
	}

	public int getEnergyCost() {
		return energyCost;
	}

	private boolean isEquipped = false;

	public void equip(boolean equip) {
		isEquipped = equip;
	}

	@Override
	public boolean isEquipped() {
		return isEquipped;
	}

	@Override
	public int getCashValue() {
		return level * 50;
	}

	@Override
	public String getDescription() {
		return name;
	}

	public String getBattleLabel(double damageModifier) {
		String retString = "";

		//Costs,Name, Danage
		retString += "(" + getEnergyCost() + ") " + getName() + " x" + getDamage(damageModifier);
		retString += (getTarget() != null ? "\n(" + getTarget().getName() + ")" : "\n ");

		return retString;
	}

	public boolean isOneTimeWeapon() {
		return isOneTimeWeapon;
	}

	public boolean alwaysHits() {
		return alwaysHits;
	}

	public boolean attacksAll() {
		return attacksAll;
	}
}
