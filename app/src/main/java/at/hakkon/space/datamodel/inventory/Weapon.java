package at.hakkon.space.datamodel.inventory;

import at.hakkon.space.datamodel.room.AbsRoom;

/**
 * Created by Markus on 29.07.2017.
 */

public class Weapon implements  IInventoryItem{

	private String name;
	private int health;
	private int damage;

	private int energyCost;

	public Weapon(String name, int health, int damage, int energyCost){
		this.name = name;
		this.health = health;
		this.damage = damage;
		this.energyCost = energyCost;
	}


	@Override
	public String getName() {
		return name;
	}

	public int getHealth() {
		return health;
	}

	public int getDamage() {
		return damage;
	}

	public int getDamage(float modifier) {
		return Math.round(damage * modifier);
	}

	public int updateHalth(int health){
		this.health+=health;
		return this.health;
	}

	public String getInformationDump() {
		String retString = "";

		retString+= name + "(" + health + "HP, DMG: " + damage + ", Cost: " + energyCost + ")";
		return retString;
	}

	public static Weapon getLaser(int level){
		return new Weapon("Laser Lv." + level, 5*level, 2*level, 2*level);
	}

	public static Weapon getRocket(int level){
		return new Weapon("Rocket Lv." + level, 10*level, 4*level, 3*level);
	}

	private AbsRoom target;

	public void setTarget(AbsRoom target){
		this.target = target;
	}

	public AbsRoom getTarget(){
		return target;
	}

	public boolean hasTarget(){
		return target != null;
	}

	public int getEnergyCost() {
		return energyCost;
	}

	private boolean isEquipped;

	public void equip(boolean equip){
		isEquipped = equip;
	}

	public boolean isEquipped(){
		return isEquipped;
	}

	public String getBattleLabel(float damageModifier){
		String retString = "";

		//Costs,Name, Danage
		retString += "(" + getEnergyCost() + ") " + getName() + " " + getDamage(damageModifier) + "Dmg";
		retString += (getTarget() != null ? "\n(" + getTarget().getName() + ")" : "\n ");

		return retString;
	}
}
