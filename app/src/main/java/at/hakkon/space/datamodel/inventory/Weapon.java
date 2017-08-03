package at.hakkon.space.datamodel.inventory;

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


	public String getName() {
		return name;
	}

	public int getHealth() {
		return health;
	}

	public int getDamage() {
		return damage;
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
		return new Weapon("Laser Lv." + level, 50*level, 15*level, 2*level);
	}

	public static Weapon getRocket(int level){
		return new Weapon("Rocket Lv." + level, 75*level, 40*level, 3*level);
	}
}
