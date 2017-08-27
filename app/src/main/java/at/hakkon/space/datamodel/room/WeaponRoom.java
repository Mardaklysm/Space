package at.hakkon.space.datamodel.room;

/**
 * Created by Markus on 29.07.2017.
 */

public class WeaponRoom extends AbsRoom {

	public WeaponRoom(int level) {
		super(level);
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
