package at.hakkon.space.event;

import android.content.Context;

import java.util.ArrayList;

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
		String text = "Refill station - We pump you up!";
		CharSequence[] charSequences = new CharSequence[5];

		ArrayList<CharSequence> choices = new ArrayList<>();
		boolean canAfford = getShip().getMoney() >= 50;
		if (!canAfford){
			choices.add("You can't afford any fuel :(");
		}

		int multiplier=1;
		while (multiplier < 8) {
			int costs = multiplier * 50;
			int value = multiplier * 5;
			if (costs <= getShip().getMoney()){
				choices.add("Buy " + value + " fuel for " + costs + "$.");
			}
			multiplier++;
		}

		charSequences = new CharSequence[choices.size()];
		for (int i=0; i<choices.size(); i++){
			charSequences[i] = choices.get(i);
		}
		Utility.getInstance().showQuestionsDialog(context, text, charSequences, this);
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		ApplicationClass.getInstance().updateShipMoney(-((hint + 1) * 50));
		ApplicationClass.getInstance().updateFuel((hint + 1) * 5);
		ApplicationClass.getInstance().updateScore(getLevel() * (hint *10));
	}
}
