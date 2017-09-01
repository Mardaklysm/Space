package at.hakkon.space.datamodel;

import java.io.Serializable;

import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.datamodel.ship.PlayerShip;

/**
 * Created by Markus on 01.09.2017.
 */

public class SaveFile implements Serializable {

	private PlayerShip playerShip;
	private Galaxy galaxy;

	public SaveFile(PlayerShip playerShip, Galaxy galaxy){
		this.playerShip = playerShip;
		this.galaxy = galaxy;
	}

	public PlayerShip getPlayerShip() {
		return playerShip;
	}

	public Galaxy getGalaxy() {
		return galaxy;
	}
}
