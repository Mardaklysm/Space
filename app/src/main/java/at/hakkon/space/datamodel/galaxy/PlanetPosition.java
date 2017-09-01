package at.hakkon.space.datamodel.galaxy;

import java.io.Serializable;

/**
 * Created by Markus on 30.07.2017.
 */

public class PlanetPosition implements Serializable {

	private int x;
	private int y;

	public PlanetPosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
