package at.hakkon.space.datamodel.room;

import java.io.Serializable;

/**
 * Created by Markus on 24.08.2017.
 */

public class AttackResult implements Serializable{

	private boolean hit;
	private int damage;
	private boolean shielded;

	public AttackResult(boolean hit, boolean shielded, int damage){
		this.hit = hit;
		this.damage = damage;
		this.shielded = shielded;
	}

	public boolean isHit() {
		return hit;
	}

	public int getDamage() {
		return damage;
	}

	public boolean isShielded() {
		return shielded;
	}
}
