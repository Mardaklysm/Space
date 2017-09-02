package at.hakkon.space.datamodel.inventory.weapon;

/**
 * Created by Markus on 02.09.2017.
 */

public class WeaponLaser extends AbsWeapon {

	public WeaponLaser(int level){
		super(level, "Laser Lv." + level, 4 * level, 3 * level, false, false);
	}

	@Override
	public int getCashValue() {
		return getLevel() * 50;
	}


	@Override
	public EWeaponType getWeaponType() {
		return EWeaponType.Laser;
	}
}
