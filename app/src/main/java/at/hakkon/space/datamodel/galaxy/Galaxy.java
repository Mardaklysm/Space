package at.hakkon.space.datamodel.galaxy;

import java.util.ArrayList;
import java.util.Random;

import at.hakkon.space.event.GoToGalaxyEvent;
import at.hakkon.space.event.RepairShopEvent;

/**
 * Created by Markus on 29.07.2017.
 */

public class Galaxy {

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

			AbsPlanet planet = new GenericPlanet(this, "Generic Planet " + i, new PlanetPosition(planetX, planetY));
			planets.add(planet);
			planetY++;
			planetX = random.nextInt(MAX_GALAXY_WIDTH);

			if (i == 0) {
				firstPlanet = planet;
			}
		}


		addGoToGalaryEvent(planets.get(planets.size() - 1));

		int shopPlanetIdx = random.nextInt(planets.size()-2) +1;
		planets.get(shopPlanetIdx).setEvent(new RepairShopEvent(level));

//		for (int i=0; i< MAX_STORE_COUNT; i++){
//			boolean foundValidIndex = false;
//			while (!foundValidIndex){
//				int shopPlanetIdx = random.nextInt(planets.size()-1);
//
//				if ( !(planets.get(shopPlanetIdx).getEvent() instanceof RepairShopEvent)){
//					foundValidIndex = true;
//					planets.get(shopPlanetIdx).setEvent(new RepairShopEvent(level));
//				}
//			}
//		}

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
