package at.hakkon.space.datamodel.room;

/**
 * Created by Markus on 29.07.2017.
 */

public class NavigationRoom extends AbsRoom {

	public NavigationRoom(int level) {
		super(level);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Navigation;
	}

	@Override
	/**
	 * Percentage for evading attacks
	 */
	public float getEfficency() {
		float value = (float) getLevel() * 5f / 100f;

		value = Math.min(value, 0.9f);
		return value;
	}
}
