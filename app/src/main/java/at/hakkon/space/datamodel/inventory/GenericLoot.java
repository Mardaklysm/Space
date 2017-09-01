package at.hakkon.space.datamodel.inventory;

import java.io.Serializable;

/**
 * Created by Markus on 28.08.2017.
 */

public class GenericLoot implements IInventoryItem, Serializable {

	private String name;
	private int value;
	private String description;

	private boolean isEquipped;

	private EGenricLootType genricLootType = EGenricLootType.Generic;

	public GenericLoot(String name, int value, String description, EGenricLootType genricLootType) {
		this.name = name;
		this.value = value;
		this.description = description;
		this.genricLootType = genricLootType;
	}

	public GenericLoot(String name, int value, String description) {
		this.name = name;
		this.value = value;
		this.description = description;
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isEquipped() {
		return isEquipped;
	}

	@Override
	public int getCashValue() {
		return value;
	}

	@Override
	public String getDescription() {
		return name;
	}

	public EGenricLootType getType() {
		return genricLootType;
	}

	@Override
	public void equip(boolean equip){
		isEquipped = equip;
	}
}
