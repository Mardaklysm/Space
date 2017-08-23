package at.hakkon.space.datamodel.room;

/**
 * Created by Markus on 29.07.2017.
 */

public class ShieldRoom extends AbsRoom {

	public ShieldRoom(int level) {
		super(level);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Shield;
	}

	@Override
	public float getEfficency() {
		return getLevel();
	}


}
