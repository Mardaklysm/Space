package at.hakkon.space.datamodel.rooms;

/**
 * Created by Markus on 29.07.2017.
 */

public class EmptyRoom extends AbsRoom {

	public EmptyRoom(String name) {
		super(name);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Empty;
	}

}
