package at.hakkon.space.datamodel;

import java.io.Serializable;
import java.util.HashMap;

import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.datamodel.ship.PlayerShip;

/**
 * Created by Markus on 01.09.2017.
 */

public class SaveFile implements Serializable {

	private PlayerShip playerShip;
	private Galaxy galaxy;

	private  HashMap<EFaction, Integer> karmaMap = new HashMap<>();

	public SaveFile(PlayerShip playerShip, Galaxy galaxy, HashMap<EFaction, Integer> karmaMap) {
		this.playerShip = playerShip;
		this.galaxy = galaxy;
		this.karmaMap = karmaMap;
	}

	public PlayerShip getPlayerShip() {
		return playerShip;
	}

	public Galaxy getGalaxy() {
		return galaxy;
	}

	public HashMap<EFaction, Integer> getKarmaMap() {
		return karmaMap;
	}

}
