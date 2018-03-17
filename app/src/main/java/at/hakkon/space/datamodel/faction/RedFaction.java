package at.hakkon.space.datamodel.faction;

import at.hakkon.space.datamodel.EFaction;

/**
 * Created by Markus on 02.09.2017.
 */

public class RedFaction extends AbsFaction {

	public RedFaction(int karma) {
		super(karma);
	}

	@Override
	public EFaction getFaction() {
		return EFaction.Red;
	}

	@Override
	public String getSymbol() {
		return "Bird";
	}
}
