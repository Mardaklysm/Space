package at.hakkon.space.datamodel.galaxy;

import at.hakkon.space.ApplicationClass;
import at.hakkon.space.datamodel.event.AbsEvent;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsPlanet {

	private Galaxy galaxy;
	private String name;

	private boolean visited;
	private boolean isFirsTimeVisit = true;

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

	public boolean visit(){
		if (visited){
			isFirsTimeVisit = false;
			return false;
		}
		ApplicationClass.getInstance().visitPlanet(this);

		visited = true;
		return true;
	}

	public Galaxy getGalaxy(){
		return galaxy;
	}

	public boolean getIsFirsTimeVisit() {
		return isFirsTimeVisit;
	}

	public void setVisited(boolean visited){
		this.visited = visited;
		this.isFirsTimeVisit = !visited;
	}
}
