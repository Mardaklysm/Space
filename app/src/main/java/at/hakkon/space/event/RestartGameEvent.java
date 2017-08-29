package at.hakkon.space.event;

import android.content.Context;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.EGameOverReason;
import at.hakkon.space.utility.Utility;

/**
 * Created by Markus on 03.08.2017.
 */

public class RestartGameEvent extends AbsEvent {

	private EGameOverReason reason;
	public RestartGameEvent(int level, EGameOverReason reason) {
		super(level);
		this.reason = reason;
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Question;
	}

	@Override
	protected void executeImpl(Context context) {
		Utility.getInstance().showYesNoDialog(context, "Game Over: " + reason.name() + "\nDo you want to restart the game (This is kinda rethorical ...) ? ", this);
	}

	@Override
	public void callbackImpl(Context context, int hint) {
		if (reason != EGameOverReason.RestartRequested || hint == 1){
			ApplicationClass.getInstance().restartGame();
		}
	}

	@Override
	public boolean canBeExecuted(){
		return true;
	}
}
