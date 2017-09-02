package at.hakkon.space.datamodel.inventory.weapon;

import java.io.Serializable;

import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.room.AbsRoom;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsWeapon implements IInventoryItem, Serializable {

	private boolean alwaysHits = false;
	private String name;
	private int damage;
	private int level;

	private boolean isOneTimeWeapon = false;
	private boolean attacksAll = false;

	private int energyCost;


	public AbsWeapon(int level, String name, int damage, int energyCost, boolean isOneTimeWeapon, boolean attacksAll) {
		this.name = name;
		this.damage = damage;
		this.energyCost = energyCost;
		this.level = level;
		this.alwaysHits = false;

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

	public abstract EWeaponType getWeaponType();



	@Override
	public String getDescription() {
		return name;
	}

	public String getBattleLabel(double damageModifier) {
		String retString = "";

		//Costs,Name, Danage
		retString += getName() + "\n(" + getDamage(damageModifier) + ")\nEnergy " + getEnergyCost();
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

	public int getLevel() {
		return level;
	}
}
