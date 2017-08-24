package at.hakkon.space.datamodel.room;

/**
 * Created by Markus on 24.08.2017.
 */

public class AttackResult {

	private boolean hit;
	private int damage;

	public AttackResult(boolean hit, int damage){
		this.hit = hit;
		this.damage = damage;
	}

	public boolean isHit() {
		return hit;
	}

	public int getDamage() {
		return damage;
	}
}
