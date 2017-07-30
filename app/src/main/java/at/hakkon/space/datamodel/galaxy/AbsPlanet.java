package at.hakkon.space.datamodel.galaxy;

import at.hakkon.space.datamodel.event.AbsEvent;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsPlanet {

	private Galaxy galaxy;
	private String name;

	private boolean visited;

	public AbsPlanet(Galaxy galaxy, String name){
		this.galaxy =galaxy;
		this.name = name;
	}

	public abstract AbsEvent getEvent();

	public String getInformationDump(){
		String retString = "";

		retString+="Planet (" + name + ")";

		return retString;
	}

	public int getTravelCosts(){
		//TODO: Implement some cool star map
		return 2;
	}

	public String getName() {
		return name;
	}

	public Galaxy getGalaxy(){
		return galaxy;
	}

}
