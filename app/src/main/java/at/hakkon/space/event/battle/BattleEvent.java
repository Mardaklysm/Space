package at.hakkon.space.event.battle;

import android.content.Context;
import android.content.Intent;

import at.hakkon.space.activity.BattleActivity2;
import at.hakkon.space.datamodel.ship.EShipType;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EEventType;

/**
 * Created by Markus on 04.08.2017.
 */

public class BattleEvent extends AbsEvent {

	private EShipType shipType;

	private boolean executed = false;

	private boolean isBossBattle;


	public BattleEvent(int level, EShipType shipType, boolean isBossBattle) {
		super(level);
		this.shipType = shipType;
		this.isBossBattle = isBossBattle;
	}

	@Override
	public EEventType getEventType() {
		return isBossBattle ? EEventType.Boss : EEventType.Battle;
	}

	@Override
	protected void executeImpl(Context context) {
		if (shipType == null) {
			throw new RuntimeException("Enemy cant be null!");
		}
		if (!canBeExecuted()) {
			return;
		}
		executed = true;
		Intent intent = new Intent(context, BattleActivity2.class);
		intent.putExtra("enemyShipTypeOrdinal", shipType.ordinal());
		intent.putExtra("level", getLevel());
		intent.putExtra("isBossBattle", isBossBattle);
		context.startActivity(intent);

	}

	@Override
	public void callbackImpl(Context context, int hint) {
	}

	@Override
	public boolean canBeExecuted() {
		return !executed;
	}

	public boolean isBossBattle() {
		return isBossBattle;
	}
}
