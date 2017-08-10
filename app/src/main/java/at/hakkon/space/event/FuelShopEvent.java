package at.hakkon.space.event;

import android.content.Context;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 10.08.2017.
 */

public class FuelShopEvent extends AbsEvent {
	public FuelShopEvent(int level) {
		super(level);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	protected void executeImpl(Context context) {
		String text = "You found a refill station. Lucky you!";
		CharSequence[] charSequences = new CharSequence[5];

		for (int i=0; i<charSequences.length; i++){
			int multiplier = i+1;
			charSequences[i] = "Buy " + (multiplier* 10) + " fuel for " + (multiplier * 50) + " Cash.";
		}

		Utility.getInstance().showQuestionsDialog(context,text, charSequences, this);

	}

	@Override
	public void callbackImpl(Context context, int hint) {

		ApplicationClass.getInstance().getShip().updateFuel(hint * 10);
		ApplicationClass.getInstance().getShip().updateMoney(-(hint * 50));

		if (hint == 1){

		}
	}
}
