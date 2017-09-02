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
	public double getEfficency() {
		double value = getEfficency(getLevel());
		return value;
	}

	private double getEfficency(int level) {
		double value = (double) level * 5f / 100f;

		value = Math.min(value, 0.75f); //Cap evasion at 75%

		return value;
	}

	@Override
	public String getUpgradeInformationText() {
		return "Evasion: " + (int)(getEfficency(getLevel()))*100 + "%" + " => " + (int)(((getEfficency(getLevel()+1)))*100) + "%";
	}

	@Override
	protected int getMaxHealthForLevel(int level) {
		return 5 + level * 5;
	}
}
