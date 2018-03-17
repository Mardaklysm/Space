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

	public String getName() {
		return "Navigation";
	}

	private double getEfficency(int level) {
		double value = (double) level * 5f;

		value = Math.min(value, 75f); //Cap evasion at 75%

		return value;
	}

	@Override
	public String getUpgradeInformationText() {
		return "Evasion: " + (int)(getEfficency(getLevel())) + "%" + " => " + (int)(((getEfficency(getLevel()+1)))) + "%";
	}

	@Override
	protected int getMaxHealthForLevel(int level) {
		return 20 + level * 5;
	}
}
