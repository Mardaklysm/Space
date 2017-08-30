package at.hakkon.space.event.planet;

import android.content.Context;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 10.08.2017.
 */

public class RepairShopEvent extends AbsEvent {

	private int exitHint;

	public RepairShopEvent(int level) {
		super(level);
		setCanBeOverwritten(false);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Shop;
	}

	@Override
	protected void executeImpl(Context context) {

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
			int valueBefore= (multiplier -1 ) * 15;
			if (costs <= getShip().getMoney() && getShip().getHealth() + valueBefore < getShip().getMaxHealth()){
				choices.add("Repair " + value + " points for " + costs + "$.");
			}
			multiplier++;
		}

		choices.add("Leave the Store");
		exitHint = choices.size() -1;

		charSequences = new CharSequence[choices.size()];
		for (int i=0; i<choices.size(); i++){
			charSequences[i] = choices.get(i);
		}

		String text = "Repair station - We fix you up!";
		if (charSequences.length == 0){
			text+="\nLooks like there is nothing we can do for you now!";
		}

		Utility.getInstance().showQuestionsDialog(context, text, charSequences, this);

	}

	@Override
	public void callbackImpl(Context context, int hint) {
		if (hint != exitHint){
			ApplicationClass.getInstance().updateShipMoney(-((hint + 1) * 30));
			ApplicationClass.getInstance().updateShipHealth((hint + 1) * 15);

			ApplicationClass.getInstance().updateScore(getLevel()* hint *10);
		}

	}
}
