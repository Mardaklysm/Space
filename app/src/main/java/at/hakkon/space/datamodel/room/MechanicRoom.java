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
	public double getEfficency() {
		return getEfficency(getLevel());
	}

	public String getName() {
		return "Mechanic";
	}

	private double getEfficency(int level) {
		if (level == 1) {
			return 0;
		}
		return level - 1;
	}

	@Override
	public void upgrade() {
		ApplicationClass.getInstance().getShip().updateMaxHealth(50);
		super.upgrade();
	}

	@Override
	public String getUpgradeInformationText() {
		return "Ship Health: +50\nRoom Repair Rate: " + getEfficency(getLevel()) + " => " + (getEfficency(getLevel() +1));
	}

	@Override
	protected int getMaxHealthForLevel(int level) {
		return 30 + level * 10;
	}


}
