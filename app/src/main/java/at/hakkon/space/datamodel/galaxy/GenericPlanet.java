package at.hakkon.space.datamodel.galaxy;

import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.event.EventGenerator;

/**
 * Created by Markus on 29.07.2017.
 */

public class GenericPlanet extends AbsPlanet {

	private AbsEvent event;

	public GenericPlanet(Galaxy galaxy, String name, PlanetPosition planetPosition) {
		super(galaxy, name, planetPosition);

		event = EventGenerator.getInstance().generateRandomEvent(galaxy.getLevel());
	}

	@Override
	public AbsEvent getEvent() {
		return event;
	}


}
