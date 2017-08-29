package at.hakkon.space.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.games.Games;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.listener.IGalaxyListener;
import at.hakkon.space.listener.IPlanetVisitListener;
import at.hakkon.space.listener.IShipListener;


/**
 * Created by Markus on 05.07.2017.
 */

public class PlanetFragment extends Fragment implements IShipListener, IPlanetVisitListener, IGalaxyListener {

	private View view;

	private AbsPlanet planet;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_planet, container, false);


		ApplicationClass.getInstance().addShipListener(this);
		ApplicationClass.getInstance().addPlanetVisitorListener(this);
		planet = ApplicationClass.getInstance().getShip().getCurrentPlanet();
		planetChanged();


		initHighscoreButton();
		initAchievementsButton();


		CheckBox cb = (CheckBox) view.findViewById(R.id.cbPlayMusic);
		cb.setChecked(ApplicationClass.playMusic);
		cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				ApplicationClass.playMusic = isChecked;
				if (isChecked) {
					ApplicationClass.getInstance().startMainMusic(getActivity());
				} else {
					ApplicationClass.getInstance().stopMainMusic();
				}

			}
		});
		return view;
	}

	private final static String TAG = "PlanetFragment";

	private final static int REQUEST_LEADERBOARD = 100;
	private final static int REQUEST_ACHIEVEMENTS = 101;


	private void initAchievementsButton() {
		Button button = (Button) view.findViewById(R.id.bShowAchievements);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(Games.Achievements.getAchievementsIntent(ApplicationClass.getInstance().getGoogleApiClient()), REQUEST_ACHIEVEMENTS);
			}
		});
	}

	private void initHighscoreButton() {
		Button button = (Button) view.findViewById(R.id.bShowHighscore);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(ApplicationClass.getInstance().getGoogleApiClient(), getString(R.string.leaderboard_highscore)), REQUEST_LEADERBOARD);
			}
		});
	}


	private void planetChanged() {
		TextView tvPlanetInfo = (TextView) view.findViewById(R.id.tvPlanetInfo);

		tvPlanetInfo.setText(planet.getInformationDump());
	}

	@Override
	public void shipUpdated(PlayerShip ship) {
		if (ship.getCurrentPlanet().equals(planet)) {
			return;
		}
		this.planet = ship.getCurrentPlanet();
		planetChanged();
	}

	@Override
	public void planetVisit(AbsPlanet planet) {
		shipUpdated(ApplicationClass.getInstance().getShip());
	}


	@Override
	public void galaxyUpdated(Galaxy galaxy) {
		if (!galaxy.getPlanets().contains(planet)) {
			planet = galaxy.getFirstPlanet();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ApplicationClass.getInstance().removeShipListener(this);
		ApplicationClass.getInstance().removePlanetVisitListener(this);

	}
}
