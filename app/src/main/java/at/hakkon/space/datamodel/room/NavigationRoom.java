package at.hakkon.space.datamodel.room;

/**
 * Created by Markus on 29.07.2017.
 */

public class NavigationRoom extends AbsRoom {

	public NavigationRoom(String name) {
		super(name);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Navigation;
	}
}
