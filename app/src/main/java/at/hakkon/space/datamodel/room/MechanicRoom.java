package at.hakkon.space.datamodel.room;

import at.hakkon.space.application.ApplicationClass;

/**
 * Created by Markus on 29.07.2017.
 */

public class MechanicRoom extends AbsRoom {

	public MechanicRoom(int level) {
		super(level);
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
		return "Ship Health: +50\nDamage Reduction: (" + getEfficency() + ") => " + (getEfficency() +1) + "\nRoom Repair Rate(" + getEfficency() + ") => " + (getEfficency() +1);
	}


}
