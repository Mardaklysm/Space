package at.hakkon.space.event;

import java.util.Random;

import at.hakkon.space.datamodel.ship.EShipType;

/**
 * Created by Markus on 29.07.2017.
 */

public class EventGenerator {

	private static EventGenerator instance;

	public static EventGenerator getInstance() {
		if (instance == null) {
			instance = new EventGenerator();
		}

		return instance;
	}

	private final static int MAX_EVENT_COUNT = 4;

	public AbsEvent generateRandomEvent(int level) {

		AbsEvent event = null;
		Random random = new Random();

		boolean battleEncounter = random.nextInt(2) == 1;


		if (battleEncounter) {
			int randShip = random.nextInt(2);
 			event = createBattleEvent(level, EShipType.values()[randShip]);
		} else {
			int randId = random.nextInt(MAX_EVENT_COUNT);
			switch (randId) {
				case 0:
					event = new ResourceBonusEvent(level);
					break;
				case 1:
					event = new UnknownMetalBoxEvent(level);
					break;
				case 2:
					event = new FlyingAstronautEvent(level);
					break;
				case 3:
					event = new FuelShopEvent(level);
					break;
				default:
					throw new RuntimeException("Invalid Event ID. I dont know any event with this ID: " + randId + ". :( ....");
			}
		}


		return event;
	}

	private AbsEvent createBattleEvent(int level, EShipType shipType) {
		return new BattleEvent(level, shipType);
	}
}
