package at.hakkon.space.datamodel.room;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.ship.AbsShip;

/**
 * Created by Markus on 29.07.2017.
 */

public class MechanicRoom extends AbsRoom {

	public MechanicRoom(AbsShip ship, int level) {
		super(ship, level);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Mechanic;
	}

	@Override
	public float getEfficency() {
		return getLevel() ;
	}

	@Override
	public void upgrade() {
		ApplicationClass.getInstance().getShip().updateMaxHealth(50);
		super.upgrade();
	}

	@Override
	public String getUpgradeInformationText() {
		return "Ship Health: +50\nRoom Repair Rate(" + getEfficency() + ") => " + (getEfficency() +1);
	}


}
