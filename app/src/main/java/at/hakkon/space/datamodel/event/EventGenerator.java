package at.hakkon.space.datamodel.event;

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
		ResourceBonusEvent event = new ResourceBonusEvent(level);
		UnknownMetalBoxEvent unknownMetalBoxEvent = new UnknownMetalBoxEvent(level);

		return unknownMetalBoxEvent;
	}
}
