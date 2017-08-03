package at.hakkon.space.datamodel.galaxy;

import java.util.Comparator;

public class PlanetPositionComparator implements Comparator<AbsPlanet> {

	@Override
	public int compare(AbsPlanet arg0, AbsPlanet arg1) {

		if (arg0.getPlanetPosition().getY() < arg1.getPlanetPosition().getY()) {
			return -1;
		} else if (arg0.getPlanetPosition().getY() > arg1.getPlanetPosition().getY()) {
			return 1;
		} else if (arg0.getPlanetPosition().getX() < arg1.getPlanetPosition().getX()) {
			return -1;
		} else if (arg0.getPlanetPosition().getX() > arg1.getPlanetPosition().getX()) {
			return 1;
		} else {
			return 0;
		}

	}

}