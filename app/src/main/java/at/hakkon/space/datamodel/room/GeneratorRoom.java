package at.hakkon.space.datamodel.room;

/**
 * Created by Markus on 29.07.2017.
 */

public class GeneratorRoom extends AbsRoom {

	public GeneratorRoom(int level) {
		super(level);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Generator;
	}

	@Override
	public float getEfficency() {
		return 4 + getLevel();
	}

	@Override
	public String getUpgradeInformationText() {
		return "Energy Regeneration (" + getEfficency() + ") => " + (getEfficency() +1);
	}


}
