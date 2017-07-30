package at.hakkon.space.event;

import android.content.Context;

import java.util.ArrayList;

import at.hakkon.space.datamodel.ship.Ship;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsEvent {

	private int level;
	private int resourceBonus;

	private Context context;
	private Ship ship;
	private AbsPlanet planet;

	public AbsEvent(int level){
		this.level = level;
	}

	public ArrayList<String> text = new ArrayList<>();

	abstract public EEventType getEventType();

	public int getLevel() {
		return level;
	}


	public int getResourceBonus() {
		return resourceBonus;
	}

	public void setResourceBonus(int resourceBonus){
		this.resourceBonus = resourceBonus;
	}

	public abstract void execute(Context context);

	public abstract boolean canBeExecuted();

	public abstract void callback(Context context, int hint);

	public void init(Context context, Ship ship, AbsPlanet planet){
		this.context = context;
		this.ship = ship;
		this.planet = planet;
	}

	public Context getContext() {
		return context;
	}

	public Ship getShip() {
		return ship;
	}

	public AbsPlanet getPlanet() {
		return planet;
	}
}
