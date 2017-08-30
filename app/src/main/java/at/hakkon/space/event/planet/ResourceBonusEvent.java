package at.hakkon.space.event.planet;

import android.content.Context;

import java.util.Random;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 29.07.2017.
 */

public class ResourceBonusEvent extends AbsEvent {

	private int resourceBonusFuel;

	private boolean executed = false;

	public ResourceBonusEvent(int level) {
		super(level);

		Random r = new Random();
		int resourceBonusMoney = r.nextInt(100 * level - 50 * level) + 25 * level;
		resourceBonusFuel = r.nextInt(5) + 5;
		ApplicationClass.getInstance().updateScore(resourceBonusMoney / 2);

		setResourceBonus(resourceBonusMoney);
	}

	@Override
	protected void executeImpl(Context context) {
		if (!canBeExecuted()) {
	//		Utility.getInstance().showTextDialog(context, "There are no resources left.");
			return;
		}

		executed = true;

		Random random = new Random();

		if (random.nextBoolean()) {
			//Add minerals
			Utility.getInstance().showTextDialog(context, "You collected minerals worth " + getResourceBonus() + "€ by blowing up a nearby asteroid.");
			ApplicationClass.getInstance().updateShipMoney(getResourceBonus());
		} else {
			//Add fuel
			Utility.getInstance().showTextDialog(context, "You collected " + resourceBonusFuel + " fuel off a nearby asteroid belt.");
			ApplicationClass.getInstance().updateFuel((int) Math.floor(getResourceBonus() / 25));
		}

		ApplicationClass.getInstance().updateScore(getResourceBonus());
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		//Nothing to do
	}

	@Override
	public EEventType getEventType() {
		return EEventType.ResourceBonus;
	}

	@Override
	public boolean canBeExecuted(){
		return !executed;
	}
}
