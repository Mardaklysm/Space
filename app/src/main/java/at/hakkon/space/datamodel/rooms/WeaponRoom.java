package at.hakkon.space.datamodel.rooms;

import java.util.ArrayList;

import at.hakkon.space.datamodel.IInventoryItem;
import at.hakkon.space.datamodel.Weapon;

/**
 * Created by Markus on 29.07.2017.
 */

public class WeaponRoom extends AbsRoom {

	private ArrayList<Weapon> weapons = new ArrayList<>();

	public WeaponRoom(String name) {
		super(name);
	}

	@Override
	public ERoom getRoomType() {
		return ERoom.Weapons;
	}

	@Override
	public void addInventory(IInventoryItem item){
		super.addInventory(item);

		if (item instanceof Weapon){
			if (!weapons.contains(item)){
				weapons.add((Weapon) item);
			}
		}
	}

	@Override
	public String getInformationDump() {
		String retString = "";

		retString+= super.getInformationDump();

		retString +="Weapons List (" + weapons.size() + ")\n";
		for (Weapon weapon: weapons){
			retString += weapon.getInformationDump();
			retString +="\n";
		}

		return retString;
	}
}
