package at.hakkon.space.datamodel.room;

import at.hakkon.space.datamodel.ship.AbsShip;

/**
 * Created by Markus on 29.07.2017.
 */

public class NavigationRoom extends AbsRoom {

	public NavigationRoom(AbsShip ship, int level) {
		super(ship, level);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Navigation;
	}

	@Override
	/**
	 * Percentage for evading attacks
	 */
	public float getEfficency() {
		float value = getEfficency(getLevel());
		return value;
	}

	private float getEfficency(int level) {
		float value = (float) level * 5f / 100f;

		value = Math.min(value, 0.9f);
		return value;
	}

	@Override
	public String getUpgradeInformationText() {
		return "Evasion (" + getEfficency(getLevel()) + ") => " + getEfficency(getLevel()+1);
	}
}
