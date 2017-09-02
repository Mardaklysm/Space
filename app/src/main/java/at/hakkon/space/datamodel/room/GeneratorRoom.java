package at.hakkon.space.datamodel.room;

import at.hakkon.space.datamodel.ship.AbsShip;

/**
 * Created by Markus on 29.07.2017.
 */

public class GeneratorRoom extends AbsRoom {

	public GeneratorRoom(AbsShip ship, int level) {
		super(ship, level);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Generator;
	}

	@Override
	public double getEfficency() {
		return 3 + getLevel();
	}

	private double getEfficency(int level) {
		return 3 + level+3;
	}

	@Override
	public String getUpgradeInformationText() {
		return "Energy Regeneration: " + getEfficency(getLevel()) + " => " + (getEfficency(getLevel() +1));
	}

	@Override
	protected int getMaxHealthForLevel(int level) {
		return 20 + level * 5;
	}


}
