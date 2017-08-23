package at.hakkon.space.datamodel.room;

/**
 * Created by Markus on 29.07.2017.
 */

public class MachanicRoom extends AbsRoom {

	public MachanicRoom(int level) {
		super(level);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Mechanic;
	}

	@Override
	public float getEfficency() {
		return getLevel()*3;
	}

}
