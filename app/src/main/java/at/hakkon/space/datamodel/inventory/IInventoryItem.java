package at.hakkon.space.datamodel.inventory;

/**
 * Created by Markus on 29.07.2017.
 */

public interface IInventoryItem {

	String getName();

	boolean isEquipped();

	int getCashValue();

	String getDescription();

	void equip(boolean equip);


}
