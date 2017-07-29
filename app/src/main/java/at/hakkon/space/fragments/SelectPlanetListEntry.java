package at.hakkon.space.fragments;

import at.hakkon.space.datamodel.galaxy.AbsPlanet;


public class SelectPlanetListEntry {

	private AbsPlanet planet;

	public SelectPlanetListEntry(AbsPlanet planet) {
		this.planet = planet;
	}

	@Override
	public String toString() {
		return planet.getInformationDump();
	}

	public AbsPlanet getPlanet() {
		return planet;
	}
}