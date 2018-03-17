package at.hakkon.space.datamodel;

import java.io.Serializable;
import java.util.HashMap;

import at.hakkon.space.datamodel.faction.AbsFaction;
import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.datamodel.ship.PlayerShip;

/**
 * Created by Markus on 01.09.2017.
 */

public class SaveFile implements Serializable {

	private PlayerShip playerShip;
	private Galaxy galaxy;

	private  HashMap<EFaction, AbsFaction> factionMap = new HashMap<>();

	private int crystalSeriesPosition = 0;

	public SaveFile(PlayerShip playerShip, Galaxy galaxy, HashMap<EFaction, AbsFaction> factionMap, int crystalSeriesPosition) {
		this.playerShip = playerShip;
		this.galaxy = galaxy;
		this.factionMap = factionMap;
		this.crystalSeriesPosition = crystalSeriesPosition;
	}

	public PlayerShip getPlayerShip() {
		return playerShip;
	}

	public Galaxy getGalaxy() {
		return galaxy;
	}

	public HashMap<EFaction, AbsFaction> getFactionMap() {
		return factionMap;
	}

	public int getCrystalSeriesPosition() {
		return crystalSeriesPosition;
	}
}
