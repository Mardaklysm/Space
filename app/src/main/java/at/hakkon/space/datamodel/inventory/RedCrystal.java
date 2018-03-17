package at.hakkon.space.datamodel.inventory;

/**
 *
 * Heals the ship and restores room health
 *
 * Created by Markus on 02.09.2017.
 */

public class RedCrystal extends GenericLoot implements IConsumableItem {

	public RedCrystal(int value) {
		super("Red Crystal", value, "Repairs your ship", EGenricLootType.Crystal_Red);
	}

	@Override
	public EConsumableItem getConsumableItemType() {
		return EConsumableItem.RedCrystal;
	}

}
