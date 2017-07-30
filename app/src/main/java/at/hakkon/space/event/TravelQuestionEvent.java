package at.hakkon.space.event;

import android.content.Context;
import android.util.Log;

import at.hakkon.space.application.ApplicationClass;
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
		String dialogTitle = "Would you like to travel to " + getPlanet().getName() + " ?";
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

			if (moved){
				String text = "You moved to => " + getPlanet().getName() + "\n(" + getShip().getFuel() + " Fuel left)";
				Utility.getInstance().showTextDialog(context,text);
			}
			Log.i(TAG, "User decided to move to planet: " + getPlanet().getName());
		}else if (hint == 2){
			String text = "You decided so stay where it feels safe. This might have been the better decision anyway ...";
			Utility.getInstance().showTextDialog(context,text);
			Log.i(TAG, "User decided to NOT move to planet: " + getPlanet().getName());
		}
	}
}
