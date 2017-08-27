package at.hakkon.space.event;

import android.content.Context;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 10.08.2017.
 */

public class RepairShopEvent extends AbsEvent {

	public RepairShopEvent(int level) {
		super(level);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	protected void executeImpl(Context context) {
		String text = "Repair station - We fix you up!";
		CharSequence[] charSequences = new CharSequence[5];

		ArrayList<CharSequence> choices = new ArrayList<>();
		boolean canAfford = getShip().getMoney() >= 20;
		if (!canAfford){
			choices.add("You can't afford any repairs :(");
		}

		int multiplier=1;
		while (multiplier < 8) {
			int costs = multiplier * 30;
			int value = multiplier * 15;
			if (costs <= getShip().getMoney()){
				choices.add("Repair " + value + " points for " + costs + "$.");
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
		ApplicationClass.getInstance().updateShipMoney(-((hint + 1) * 30));
		ApplicationClass.getInstance().updateShipHealth((hint + 1) * 15);

		ApplicationClass.getInstance().updateScore(getLevel()* hint *10);
	}
}
