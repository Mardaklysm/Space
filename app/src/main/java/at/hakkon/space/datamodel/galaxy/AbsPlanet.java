package at.hakkon.space.datamodel.galaxy;

import at.hakkon.space.event.AbsEvent;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsPlanet {

	private AbsEvent event;

	private Galaxy galaxy;
	private String name;
	private PlanetPosition planetPosition;

	public AbsPlanet(Galaxy galaxy, String name, PlanetPosition planetPosition){
		this.galaxy =galaxy;
		this.name = name;
		this.planetPosition = planetPosition;
	}

	public String getInformationDump(){
		String retString = "";

		retString+="Planet (" + name + ")";

		return retString;
	}

	public String getName() {
		return name;
	}

	public Galaxy getGalaxy(){
		return galaxy;
	}

	public PlanetPosition getPlanetPosition() {
		return planetPosition;
	}


	public AbsEvent getEvent() {
		return event;
	}

	public void setEvent(AbsEvent event){
		this.event = event;
	}

	public void revealName(){
		switch (event.getEventType()){

			case Nothing:
				name = "Empty";
				break;
			case Shop:
				name = "Shop";
				break;
			case Battle:
				name = "Empty";
				break;
			case ResourceBonus:
				name = "Empty";
				break;
			case Question:
				name = "Empty";
				break;
			case TravelQuestion:
				break;
			case Upgrade:
				break;
		}
	}
}
