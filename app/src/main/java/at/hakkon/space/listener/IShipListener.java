package at.hakkon.space.listener;

import at.hakkon.space.datamodel.inventory.IConsumableItem;
import at.hakkon.space.datamodel.ship.AbsShip;
import at.hakkon.space.datamodel.ship.PlayerShip;

/**
 * Created by Markus on 29.07.2017.
 */

public interface IShipListener {

	void shipUpdated(PlayerShip ship);

	void itemUsed(AbsShip ship, IConsumableItem item);
}
