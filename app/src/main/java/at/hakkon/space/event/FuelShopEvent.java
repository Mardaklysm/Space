package at.hakkon.space.event;

import android.content.Context;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 10.08.2017.
 */

public class FuelShopEvent extends AbsEvent {

	private int exitHint;


	public FuelShopEvent(int level) {
		super(level);
		setCanBeOverwritten(false);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	protected void executeImpl(Context context) {


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

		choices.add("Leave the Store");
		exitHint = choices.size()-1;

		CharSequence[] charSequences = new CharSequence[choices.size()];
		for (int i=0; i<choices.size(); i++){
			charSequences[i] = choices.get(i);
		}

		String text = "Refill station - We pump you up!";
		if (charSequences.length == 0){
			text+="\nLooks like there is nothing we can do for you now!";
		}
		Utility.getInstance().showQuestionsDialog(context, text, charSequences, this);
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		if (hint != exitHint){
			ApplicationClass.getInstance().updateShipMoney(-((hint + 1) * 50));
			ApplicationClass.getInstance().updateFuel((hint + 1) * 5);
			ApplicationClass.getInstance().updateScore(getLevel() * (hint *10));
		}

	}
}
