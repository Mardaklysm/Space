package at.hakkon.space.event;

import java.util.Random;

import at.hakkon.space.datamodel.ship.EShipType;

/**
 * Created by Markus on 29.07.2017.
 */

public class EventGenerator {

	private static EventGenerator instance;

	public static EventGenerator getInstance(){
		if (instance == null){
			instance = new EventGenerator();
		}

		return instance;
	}

	public AbsEvent generateRandomEvent(int level){

		AbsEvent event = null;
		Random random = new Random();

		int randId = random.nextInt(3);

		switch (randId){
			case 0: event = new ResourceBonusEvent(level); break;
			case 1: event = new UnknownMetalBoxEvent(level); break;
			case 2: event = new FlyingAstronautEvent(level); break;
			default: throw new RuntimeException("balblbalba");
		}

		event = new BattleEvent(level, EShipType.Enemy_A);

		return event;
	}
}
