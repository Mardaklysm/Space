package at.hakkon.space.datamodel.event;

import java.util.Random;

import at.hakkon.space.ApplicationClass;

/**
 * Created by Markus on 29.07.2017.
 */

public class ResourceBonusEvent extends AbsEvent {

	public ResourceBonusEvent(int level, EEventType eventType) {
		super(level, eventType);

		Random r = new Random();
		int resourceBonus = r.nextInt(100*level - 50*level) + 50*level;

		setResourceBonus(resourceBonus);
	}

	@Override
	public void execute() {
		ApplicationClass.getInstance().getShip().updateMoney(getResourceBonus());
	}
}
