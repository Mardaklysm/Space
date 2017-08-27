package at.hakkon.space.application;

/**
 * Created by Markus on 27.08.2017.
 */

/**
 * AN instance of this class is interested in general game information like the players score. (More to come!)
 */
public interface IMetaDataListener {

	public void scoreChanged(int score);
}
