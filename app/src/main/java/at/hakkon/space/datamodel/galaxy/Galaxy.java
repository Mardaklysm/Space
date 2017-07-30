package at.hakkon.space.datamodel.galaxy;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Markus on 29.07.2017.
 */

public class Galaxy {

	private int level;
	private String name;
	private ArrayList<AbsPlanet> planets = new ArrayList<>();

	public final static int MAX_GALAXY_WIDTH = 4;

	public int getGalaxyDepth(){
		return 10 + level + level/2 ;
	}

	public Galaxy(String name, int level) {
		this.name = name;
		this.level = level;

		createPlanets();
	}

	private void createPlanets() {
		int planetX = 0;
		int planetY = 0;

		Random random = new Random();
		for (int i = 0; i < getGalaxyDepth(); i++) {
			AbsPlanet planet = new GenericPlanet(this, "Generic Planet " + i, new PlanetPosition(planetX, planetY));
			planets.add(planet);
			planetY++;
			planetX = random.nextInt(MAX_GALAXY_WIDTH);
		}

	}

	public ArrayList<AbsPlanet> getReachablePlanets(int position) {
		ArrayList<AbsPlanet> reachablePlanets = new ArrayList<>();
		//TODO: Implement somet cool navigation structure
		//for (Planet planet:planets){
		//}

		for (AbsPlanet planet : planets) {
			reachablePlanets.add(planet);
		}

		return reachablePlanets;
	}

	public ArrayList<AbsPlanet> getReachablePlanets() {
		ArrayList<AbsPlanet> reachablePlanets = new ArrayList<>();

		for (AbsPlanet planet : planets) {
			reachablePlanets.add(planet);
		}

		return reachablePlanets;
	}

	public ArrayList<AbsPlanet> getPlanets() {
		return planets;
	}

	public String getInformationDump() {
		String retString = "";

		retString += "Galaxy (" + name + ")\n";

		for (AbsPlanet planet : planets) {
			retString += planet.getInformationDump() + "\n";
		}

		return retString;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}
}
