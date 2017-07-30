package at.hakkon.space.event;

import android.content.Context;

import java.util.Random;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 29.07.2017.
 */

public class ResourceBonusEvent extends AbsEvent {

	public ResourceBonusEvent(int level) {
		super(level);

		Random r = new Random();
		int resourceBonus = r.nextInt(100 * level - 50 * level) + 50 * level;

		setResourceBonus(resourceBonus);
	}

	@Override
	protected void executeImpl(Context context) {
		if (!canBeExecuted()) {
			Utility.getInstance().showTextDialog(context, "There are no minerals left.");
			return;
		}

		Utility.getInstance().showTextDialog(context, "You collected minerals worth " + getResourceBonus() + "€");

		ApplicationClass.getInstance().updateShipMoney(getResourceBonus());
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		//Nothing to do
	}

	@Override
	public EEventType getEventType() {
		return EEventType.ResourceBonus;
	}
}
