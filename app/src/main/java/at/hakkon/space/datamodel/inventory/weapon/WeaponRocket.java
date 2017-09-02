package at.hakkon.space.datamodel.inventory.weapon;

/**
 * Created by Markus on 02.09.2017.
 */

public class WeaponRocket extends AbsWeapon {

	public WeaponRocket(int level){
		super(level, "Rocket Lv." + level, 2 * level, 3 * level, false, true);
	}

	@Override
	public int getCashValue() {
		return getLevel() * 50;
	}

	@Override
	public EWeaponType getWeaponType() {
		return EWeaponType.Rocket;
	}
}
