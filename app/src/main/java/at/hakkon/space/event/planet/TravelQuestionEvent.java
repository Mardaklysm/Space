package at.hakkon.space.event.planet;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.games.Games;

import at.hakkon.space.achievement.Achievements;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 30.07.2017.
 */

public class TravelQuestionEvent extends AbsEvent {

	private final static String TAG = "TravelQuestionEvent";

	public TravelQuestionEvent(int level) {
		super(level);
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	private boolean moved = false;

	@Override
	protected void executeImpl(Context context) {
		if (!canBeExecuted()){
			Utility.getInstance().showTextDialog(context,"You can not travel there!");
			return;
		}
		int fuelCost = Utility.getTravelCost(getShip(), getPlanet());
		String dialogTitle = "Would you like to travel to " + getPlanet().getName() + "(Fuel Cost: " + fuelCost + ") ?";
		Utility.getInstance().showYesNoDialog(getContext(), dialogTitle, this);
	}

	@Override
	public boolean canBeExecuted() {
		return getShip().canMoveToPlanet(getPlanet());
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		if (hint == 1){
			moved = ApplicationClass.getInstance().moveToPlanet(getPlanet());

			Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_EXPLORER_I, 1);
			Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_EXPLORER_II, 1);
			Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_EXPLORER_III, 1);
			Log.i(TAG, "User decided to move to planet: " + getPlanet().getName());
		}else if (hint == 2){
			Log.i(TAG, "User decided to NOT move to planet: " + getPlanet().getName());
		}
	}
}
