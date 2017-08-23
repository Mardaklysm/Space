package at.hakkon.space.application;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

import at.hakkon.space.activity.MainActivity;
import at.hakkon.space.datamodel.EGameOverReason;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.event.RestartGameEvent;
import at.hakkon.space.listener.IGalaxyListener;
import at.hakkon.space.listener.IPlanetVisitListener;
import at.hakkon.space.listener.IShipListener;
import at.hakkon.space.utility.Utility;

public class ApplicationClass extends android.app.Application {

	private static ApplicationClass instance;

	private ArrayList<IShipListener> shipListeners = new ArrayList<>();
	private ArrayList<IGalaxyListener> galaxyListeners = new ArrayList<>();
	private ArrayList<IPlanetVisitListener> planetVisitListeners = new ArrayList<>();

	private PlayerShip ship;
	private Galaxy galaxy;

	public static ApplicationClass getInstance() {
		if (instance == null) {
			instance = new ApplicationClass();
		}
		return instance;
	}


	private boolean isInitialized = false;

	public void initialize() {
		if (isInitialized) {
			return;
		}

		galaxy = new Galaxy("Starting Galaxy", 1);

		ship = new PlayerShip("Weinreise", 1);
		ship.setStartPlanet(galaxy.getFirstPlanet());
		ship.setCurrentPlanet(galaxy.getFirstPlanet());

		isInitialized = true;

		notifyShipListeners(ship);
		notifyGalaxyListeners(galaxy);
		notifyPlanetVisitListener(ship.getCurrentPlanet());

	}

	public void restartGame(){
		isInitialized = false;

		Intent intent = new Intent(activeContext, MainActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		activeContext.startActivity(intent);

		//notifyShipListeners(ship);
		//notifyGalaxyListeners(galaxy);
		//notifyPlanetVisitListener(ship.getCurrentPlanet());
	}

	public PlayerShip getShip() {
		return ship;
	}


	public boolean moveToPlanet(AbsPlanet planet) {
		boolean moved = getShip().moveToPlanet(planet);

		if (moved) {
			notifyPlanetVisitListener(planet);
			notifyShipListeners(getShip());
		}

		planet.getEvent().execute(getContext());
		return moved;
	}

	public Context getContext(){
		return MainActivity.getInstance();
	}

	public void addShipListener(IShipListener listener) {
		if (!shipListeners.contains(listener)) {
			shipListeners.add(listener);
		}
	}

	public void addGalaxyListener(IGalaxyListener listener) {
		if (!galaxyListeners.contains(listener)) {
			galaxyListeners.add(listener);
		}
	}

	public void addPlanetVisitorListener(IPlanetVisitListener listener) {
		if (!planetVisitListeners.contains(listener)) {
			planetVisitListeners.add(listener);
		}
	}

	public void removeGalaxyListener(IGalaxyListener listener) {
		galaxyListeners.remove(listener);
	}

	public void removeShipListener(IShipListener listener) {
		shipListeners.remove(listener);
	}

	public void removePlanetVisitListener(IPlanetVisitListener listener) {
		planetVisitListeners.remove(listener);
	}

	private void notifyShipListeners(PlayerShip ship) {
		for (IShipListener listener : shipListeners) {
			listener.shipUpdated(ship);
		}
	}

	private final static String TAG = "AppClass";

	private void notifyGalaxyListeners(Galaxy galaxy) {
		for (IGalaxyListener listener : galaxyListeners) {
			listener.galaxyUpdated(galaxy);
		}
	}

	private void notifyPlanetVisitListener(AbsPlanet planet) {
		for (IPlanetVisitListener listener : planetVisitListeners) {
			listener.planetVisit(planet);
		}
	}


	public Galaxy getGalaxy() {
		return galaxy;
	}

	public void visitPlanet(AbsPlanet absPlanet) {
		notifyPlanetVisitListener(absPlanet);

	}

	public void toast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT);
	}

	public void requestNotifyShipChangedEvent(){
		notifyShipListeners(getShip());
	}

	public void updateShipMoney(int value) {
		ship.updateMoney(value);
		notifyShipListeners(ship);
	}

	public void updateShipHealth(int value) {
		ship.updateHealth(value);
		notifyShipListeners(ship);
	}

	public void addShipMember(Person person) {
		ship.addPerson(person);
		notifyShipListeners(ship);
	}

	public void gameOver(EGameOverReason gameOverReason) {
		Utility.getInstance().showTextDialog(activeContext, "Game Over: " + gameOverReason.name());
		RestartGameEvent event = new RestartGameEvent(1, gameOverReason);
		event.execute(activeContext);

	}


	public void moveToNewGalaxy() {
		int galaxyLevel = galaxy.getLevel() +1;
		Galaxy newGalaxy = new Galaxy("Galaxy " + galaxyLevel,galaxyLevel);
		this.galaxy = newGalaxy;
		ship.setCurrentPlanet(galaxy.getFirstPlanet());
		ship.setStartPlanet(galaxy.getFirstPlanet());


		notifyGalaxyListeners(this.galaxy);
	}

	public void updateFuel(int fuel) {
		ship.updateFuel(fuel);
		notifyShipListeners(ship);
	}

	private Context activeContext;

	public void updateActiveContext(Context context){
		activeContext = context;
	}

	public Context getActiveContext(){
		return activeContext;
	}

}