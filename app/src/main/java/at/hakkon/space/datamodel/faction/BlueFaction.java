package at.hakkon.space.datamodel.faction;

import at.hakkon.space.datamodel.EFaction;

/**
 * Created by Markus on 02.09.2017.
 */

public class BlueFaction extends AbsFaction {

	public BlueFaction(int karma) {
		super(karma);
	}

	@Override
	public EFaction getFaction() {
		return EFaction.Green;
	}

	@Override
	public String getSymbol() {
		return "Fish";
	}
}
