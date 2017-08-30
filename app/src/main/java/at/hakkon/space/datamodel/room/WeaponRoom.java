package at.hakkon.space.datamodel.room;

import at.hakkon.space.datamodel.ship.AbsShip;
import at.hakkon.space.utility.Utility;

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
	public double getEfficency() {
		return getEfficency(getLevel());
	}


	private double getEfficency(int level) {
		if (level == 1){
			return 1;
		}

		float value = 1 + (float)(level-1)/10f;

		return Math.max(0, Utility.roundTwoDecimals(value));
	}


	@Override
	public String getUpgradeInformationText() {
		return "Damage: " + (int)(getEfficency(getLevel()))*100 + "% => " + (int)(((getEfficency(getLevel()+1))) * 100)  + "%";
	}

	@Override
	protected int getMaxHealthForLevel(int level) {
		return 20 + level * 10;
	}

}
