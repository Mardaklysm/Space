package at.hakkon.space.datamodel.galaxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import at.hakkon.space.event.planet.FuelShopEvent;
import at.hakkon.space.event.planet.GoToGalaxyEvent;
import at.hakkon.space.event.planet.RepairShopEvent;

/**
 * Created by Markus on 29.07.2017.
 */

public class Galaxy implements Serializable {

	private int level;
	private String name;
	private ArrayList<AbsPlanet> planets = new ArrayList<>();

	private AbsPlanet firstPlanet;

	public final static int MAX_GALAXY_WIDTH = 4;

	public int getGalaxyDepth() {
		return 10 + level + level / 2;
	}

	public Galaxy(String name, int level) {
		this.name = name;
		this.level = level;

		createPlanets();
	}

	//private final static int MAX_STORE_COUNT = 2;

	private void createPlanets() {
		int planetX = 0;
		int planetY = 0;


		Random random = new Random();
		for (int i = 0; i <= getGalaxyDepth(); i++) {

			AbsPlanet planet = new GenericPlanet(this, "Unknown " + i, new PlanetPosition(planetX, planetY));
			planets.add(planet);
			planetY++;
			planetX = random.nextInt(MAX_GALAXY_WIDTH);

			if (i == 0) {
				firstPlanet = planet;
			}
		}

		planets.get(0).revealName();
		planets.get(0).setEvent(null);

		AbsPlanet lastPlanet = planets.get(planets.size() - 1);
		addGoToGalaryEvent(lastPlanet);
		lastPlanet.revealName();



		int shopPlanetIdx = random.nextInt(planets.size()-2) +1;
		planets.get(shopPlanetIdx).setEvent(new FuelShopEvent(level));

		shopPlanetIdx = random.nextInt(planets.size()-2) +1;
		planets.get(shopPlanetIdx).setEvent(new RepairShopEvent(level));

	}

	private void addGoToGalaryEvent(AbsPlanet planet) {
		GoToGalaxyEvent goToGalaxyEvent = new GoToGalaxyEvent(getLevel());

		planet.setEvent(goToGalaxyEvent);
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

	public AbsPlanet getFirstPlanet() {
		return firstPlanet;
	}
}
