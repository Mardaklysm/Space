package at.hakkon.space.application;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import at.hakkon.space.datamodel.ship.Ship;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.listener.IGalaxyListener;
import at.hakkon.space.listener.IPlanetVisitListener;
import at.hakkon.space.listener.IShipListener;

public class ApplicationClass extends android.app.Application {

	private static ApplicationClass instance;

	private ArrayList<IShipListener> shipListeners = new ArrayList<>();
	private ArrayList<IGalaxyListener> galaxyListeners = new ArrayList<>();
	private ArrayList<IPlanetVisitListener> planetVisitListeners = new ArrayList<>();

	private Ship ship;
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

		ship = Ship.getDefault("Weinreise");

		isInitialized = true;
	}

	public Ship getShip() {
		return ship;
	}


	public boolean moveToPlanet(AbsPlanet planet) {
		boolean moved = getShip().moveToPlanet(planet);

		if (moved) {
			notifyPlanetVisitListener(planet);
			notifyShipListeners(getShip());
		}

		return moved;
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

	private void notifyShipListeners(Ship ship) {
		for (IShipListener listener : shipListeners) {
			listener.shipUpdated(ship);
		}
	}

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
}