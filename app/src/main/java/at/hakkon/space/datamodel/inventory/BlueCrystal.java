package at.hakkon.space.datamodel.inventory;

/**
 *
 * Shields a room for damage for 3 rounds
 *
 * Created by Markus on 02.09.2017.
 */

public class BlueCrystal extends GenericLoot implements IConsumableItem {

	public BlueCrystal(int value) {
		super("Blue Crystal", value, "Shield room", EGenricLootType.Crystal_Blue);
	}

	@Override
	public EConsumableItem getConsumableItemType() {
		return EConsumableItem.BlueCrystal;
	}
}
