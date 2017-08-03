package at.hakkon.space.event;

import android.content.Context;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.utility.Utility;
import at.hakkon.space.datamodel.ship.PlayerShip;

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


	public UnknownMetalBoxEvent(int level) {
		super(level);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	protected void executeImpl(Context context) {
		if (canBeExecuted()) {
			String text = "We have found an unknown metal box floating in space, what do you want to do?";
			Utility.getInstance().showQuestionsDialog(context, text, options, this);
		}
	}

	@Override
	public void callbackImpl(Context context, int hint){
		PlayerShip ship = ApplicationClass.getInstance().getShip();
		if (hint == 0){
			int money = 150 * getLevel();
			int health = 20 * getLevel();
			String text = "As you try to carefully open the box with a sledgehammer it explodes damaging your ship for " + health + ".\nAt the bright side you could salvage the box for " + money +"€";

			Utility.getInstance().showTextDialog(context,text);

			ApplicationClass.getInstance().updateShipMoney(money);
			ApplicationClass.getInstance().updateShipHealth(-health);
		}else if (hint == 1){
			int money = 100;
			ship.updateMoney(money);
			String text = "As you shoot at it a huge explosion occurs... It was a bomb\n You could salvage the remains of the box for " + money +"€";

			ApplicationClass.getInstance().updateShipMoney(money);;
			Utility.getInstance().showTextDialog(context,text);
		}else if (hint == 2){
			String text = "As you pass the box it explodes but you take no damage. Looks like it was a bomb after all.";
			Utility.getInstance().showTextDialog(context,text);
		}
	}
}
