package at.hakkon.space.event;

import android.content.Context;
import android.content.Intent;

import at.hakkon.space.activity.BattleActivity;
import at.hakkon.space.datamodel.ship.EShipType;

/**
 * Created by Markus on 04.08.2017.
 */

public class BattleEvent extends AbsEvent {

	private EShipType shipType;

	public BattleEvent(int level) {
		super(level);
	}

	public BattleEvent(int level, EShipType shipType) {
		super(level);
		this.shipType = shipType;
	}

	@Override
	public EEventType getEventType() {
		return EEventType.Battle;
	}

	@Override
	protected void executeImpl(Context context) {
		if (shipType == null){
			throw new RuntimeException("Enemy cant be null!");
		}

		Intent intent = new Intent(context, BattleActivity.class);
		intent.putExtra("enemyShipTypeOrdinal", shipType.ordinal()); //Optional parameters
		context.startActivity(intent);

	}

	@Override
	public void callbackImpl(Context context, int hint) {

	}
}
