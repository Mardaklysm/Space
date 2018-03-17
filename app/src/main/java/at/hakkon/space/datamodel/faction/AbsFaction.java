package at.hakkon.space.datamodel.faction;

import com.google.android.gms.games.Games;

import java.io.Serializable;

import at.hakkon.space.achievement.Achievements;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.EFaction;

/**
 * Created by Markus on 02.09.2017.
 */

public abstract class AbsFaction implements Serializable {

	private int karma;

	public AbsFaction(int karma){
		this.karma = karma;
	}

	public void updateKarma(int value){
		karma+=value;

		if (karma > 0) {
			switch (getFaction()) {
				case Blue:
					Games.Achievements.unlock(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_FIRST_KARMA_PLUS_BLUE);
					break;
				case Red:
					Games.Achievements.unlock(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_FIRST_KARMA_PLUS_RED);
					break;
				case Green:
					Games.Achievements.unlock(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_FIRST_KARMA_PLUS_GREEN);
					break;
				default: throw new RuntimeException("Unknown Faction!");
			}
		}
	}


	public int getKarma() {
		return karma;
	}

	abstract public EFaction getFaction();

	abstract public String getSymbol();
}
