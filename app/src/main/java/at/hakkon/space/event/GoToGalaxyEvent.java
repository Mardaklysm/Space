package at.hakkon.space.event;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.games.Games;

import at.hakkon.space.achievement.Achievements;
import at.hakkon.space.application.ApplicationClass;
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
		String dialogTitle = "Congratulations: You reached the end of this Galaxy!\n\nWould you like to travel to the next Galaxy now?";
		Utility.getInstance().showYesNoDialog(ApplicationClass.getInstance().getActiveContext(), dialogTitle, this);
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		if (hint == 1){
			int fuelBonus = 5 + getLevel();
			ApplicationClass.getInstance().updateFuel(fuelBonus);
			Utility.getInstance().showTextDialog(context,"As you travel to the next Galaxy you can collect " + fuelBonus + " fuel");
			ApplicationClass.getInstance().moveToNewGalaxy();
			ApplicationClass.getInstance().updateScore(getLevel()* 200);

			Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_EXPLORER_I, 1);
			Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_EXPLORER_II, 1);
			Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_EXPLORER_III, 1);
			Log.i(TAG, "User decided to move to a new galaxy");
		}else if (hint == 2){
			Log.i(TAG, "User decided to NOT move to a new galaxy");
		}

	}
}
