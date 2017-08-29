package at.hakkon.space.datamodel.inventory;

import at.hakkon.space.datamodel.room.AbsRoom;

/**
 * Created by Markus on 29.07.2017.
 */

public class Weapon implements IInventoryItem {

	private String name;
	private int damage;
	private int level;

	private boolean isOneTimeWeapon = false;

	private int energyCost;

	public Weapon(int level, String name, int damage, int energyCost) {
		this.name = name;
		this.damage = damage;
		this.energyCost = energyCost;
		this.level = level;
	}

	public Weapon(int level, String name, int damage, int energyCost, boolean isOneTimeWeapon) {
		this(level, name, damage, energyCost);
		this.isOneTimeWeapon = isOneTimeWeapon;
	}


	@Override
	public String getName() {
		return name;
	}


	public int getDamage() {
		return damage;
	}

	public int getDamage(float modifier) {
		return Math.round(damage * modifier);
	}


	public String getInformationDump() {
		String retString = "";

		retString += name + "( DMG: " + damage + ", Cost: " + energyCost + ")";
		return retString;
	}

	public static Weapon getLaser(int level) {
		return new Weapon(level, "Laser Lv." + level, 2 * level, 3 * level);
	}


	public static Weapon getRocket(int level) {
		return new Weapon(level, "Rocket Lv." + level, 4 * level, 4 * level);
	}

	public static Weapon getNuke(int level) {
		return new Weapon(level, "Nuke Lv." + level, 20 + 25 * level, 10 + 2 * level, true);
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

	public String getBattleLabel(float damageModifier) {
		String retString = "";

		//Costs,Name, Danage
		retString += "(" + getEnergyCost() + ") " + getName() + " x" + getDamage(damageModifier);
		retString += (getTarget() != null ? "\n(" + getTarget().getName() + ")" : "\n ");

		return retString;
	}

	public boolean isOneTimeWeapon() {
		return isOneTimeWeapon;
	}
}
