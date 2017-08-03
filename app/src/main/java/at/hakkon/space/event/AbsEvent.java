package at.hakkon.space.event;

import android.content.Context;

import java.util.ArrayList;

import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;

/**
 * Created by Markus on 29.07.2017.
 */

public abstract class AbsEvent {

	private boolean enabled = true;
	private boolean executed = false;

	private int level;
	private int resourceBonus;

	private Context context;
	private PlayerShip ship;
	private AbsPlanet planet;

	public AbsEvent(int level){
		this.level = level;
	}

	public ArrayList<String> text = new ArrayList<>();


	public int getLevel() {
		return level;
	}


	public int getResourceBonus() {
		return resourceBonus;
	}

	public void setResourceBonus(int resourceBonus){
		this.resourceBonus = resourceBonus;
	}

	abstract public EEventType getEventType();

	public void execute(Context context){//TODO: Do we need a booelan return value here??
		if (enabled){
			executeImpl(context);
		}
	}

	protected abstract void executeImpl(Context context);

	public boolean canBeExecuted(){
		return enabled && !executed;
	}

	public void callback(Context context, int hint){
		callbackImpl(context, hint);
		executed = true;
	}

	public abstract void callbackImpl(Context context, int hint);

	public void init(Context context, PlayerShip ship, AbsPlanet planet){
		this.context = context;
		this.ship = ship;
		this.planet = planet;
	}

	public Context getContext() {
		return context;
	}

	public PlayerShip getShip() {
		return ship;
	}

	public AbsPlanet getPlanet() {
		return planet;
	}

	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
}
