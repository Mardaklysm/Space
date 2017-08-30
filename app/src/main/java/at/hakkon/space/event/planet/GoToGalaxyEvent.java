package at.hakkon.space.event.planet;

import android.content.Context;
import android.util.Log;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.ship.EShipType;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.event.battle.BattleEvent;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 31.07.2017.
 */

public class GoToGalaxyEvent extends AbsEvent {

	private final static String TAG = "GoToGalaxyEvent";

	public GoToGalaxyEvent(int level) {
		super(level);
		setCanBeOverwritten(false);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	protected void executeImpl(Context context) {
		String dialogTitle = "Congratulations: You reached the end of this Galaxy!\n\nWould you like to fight the boss of this galaxy and travel to the next Galaxy?";
		Utility.getInstance().showYesNoDialog(ApplicationClass.getInstance().getActiveContext(), dialogTitle, this);
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		if (hint == 1){
			BattleEvent event = new BattleEvent(getLevel(), EShipType.Enemy_Boss_A, true);
			event.execute(context);
		}

	}

	public static void move(Context context, int level) {
		int fuelBonus = 5 * level;
		int hullRepair = 20 * level;
		ApplicationClass.getInstance().updateFuel(fuelBonus);
		ApplicationClass.getInstance().updateShipHealth(hullRepair);
		Utility.getInstance().showTextDialog(context, "Welcome to Galaxy #" + level + "\n\nYou find " + fuelBonus + " packaged fuel barrels at your landing coordinates.\n\nThe ship got repaired by " + hullRepair + " points.");
		ApplicationClass.getInstance().moveToNewGalaxy();
		ApplicationClass.getInstance().updateScore(level * 200);

	//	Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_EXPLORER_I, 1);
	//	Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_EXPLORER_II, 1);
	//	Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_EXPLORER_III, 1);

		Log.i(TAG, "User decided to move to a new galaxy");
	}
}
