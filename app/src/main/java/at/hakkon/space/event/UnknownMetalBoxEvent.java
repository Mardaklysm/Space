package at.hakkon.space.event;

import android.content.Context;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.utility.Utility;
import at.hakkon.space.datamodel.ship.Ship;

/**
 * Created by Markus on 30.07.2017.
 */

public class UnknownMetalBoxEvent extends AbsEvent {

	private static CharSequence[] options;

	static {
		options = new CharSequence[3];

		options[0] = ("Teleport it onto the ship");
		options[1] = ("Shot at it with your weapons");
		options[2] = ("Ignore it");
	}

	private boolean canBeExecuted = true;

	public UnknownMetalBoxEvent(int level) {
		super(level);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	public void execute(Context context) {
		if (canBeExecuted) {
			String text = "We have found an unknown metal box floating in space, what do you want to do?";
			Utility.getInstance().showQuestionsDialog(context, text, options, this);
		}
		canBeExecuted = false;
	}

	@Override
	public boolean canBeExecuted() {
		return canBeExecuted;
	}

	@Override
	public void callback(Context context, int hint){
		Ship ship = ApplicationClass.getInstance().getShip();
		if (hint == 0){
			int money = 150;
			int health = 20;
			String text = "As you try to open the box with your laser it explodes damaging your ship for " + health + ".\n At the bright side you could salvage the box for " + money +"€";

			ship.updateMoney(money);
			ship.updateHealth(-health);

			Utility.getInstance().showTextDialog(context,text);
		}else if (hint == 1){
			int money = 100;
			ship.updateMoney(money);
			String text = "As you shoot at it a huge explosion occurs... It was a bomb\n You could salvage the remains of the box for " + money +"€";

			ship.updateMoney(money);
			Utility.getInstance().showTextDialog(context,text);
		}else if (hint == 2){
			String text = "As you pass the box it explodes but you take no damage. Looks like it was a bomb after all.";
			Utility.getInstance().showTextDialog(context,text);
		}
	}
}
