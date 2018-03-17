package at.hakkon.space.datamodel.inventory.weapon;

/**
 * Created by Markus on 02.09.2017.
 */

public class WeaponNuke extends AbsWeapon {

	public WeaponNuke(int level){
		super(level, "Nuke Lv." + level, 5 + 4 * level, 5 + 4 * level, true, true);
	}

	@Override
	public int getCashValue() {
		return getLevel() * 50;
	}

	@Override
	public EWeaponType getWeaponType() {
		return EWeaponType.Nuke;
	}
}
