package at.hakkon.space.datamodel.room;

import at.hakkon.space.datamodel.ship.AbsShip;

/**
 * Created by Markus on 29.07.2017.
 */

public class WeaponRoom extends AbsRoom {

	public WeaponRoom(AbsShip ship, int level) {
		super(ship, level);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Weapons;
	}

	@Override
	public float getEfficency() {
		return getEfficency(getLevel());
	}


	private float getEfficency(int level) {
		return 1 + level / 10f;
	}


	@Override
	public String getUpgradeInformationText() {
		return "Weapon Damage (" + getEfficency(getLevel()) + ") => " + getEfficency(getLevel()+1);
	}

}
