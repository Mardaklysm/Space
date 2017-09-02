package at.hakkon.space.datamodel.inventory;

/**
 *
 * Heals 50% of a rooms damage
 *
 * Created by Markus on 02.09.2017.
 */

public class RedCrystal extends GenericLoot {

	public RedCrystal(int value) {
		super("Red Crystal", value, "Restore room health", EGenricLootType.Crystal_Red);
	}


}
