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
		return 1 + getLevel() / 10f;
	}

}
