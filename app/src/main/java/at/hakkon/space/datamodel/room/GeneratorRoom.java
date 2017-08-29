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
	public float getEfficency() {
		return 4 + getLevel();
	}

	@Override
	public String getUpgradeInformationText() {
		return "Energy Regeneration (" + getEfficency() + ") => " + (getEfficency() +1);
	}


}
