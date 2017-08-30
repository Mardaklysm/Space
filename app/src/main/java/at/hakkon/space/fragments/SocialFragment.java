package at.hakkon.space.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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


/**
 * Created by Markus on 05.07.2017.
 */

public class SocialFragment extends Fragment {

	private final static String TAG = "PlanetFragment";

	private final static int REQUEST_LEADERBOARD = 100;
	private final static int REQUEST_ACHIEVEMENTS = 101;

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_social, container, false);

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

		try {
			PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
			String version = pInfo.versionName;
			((TextView) view.findViewById(R.id.tvVersionNumber)).setText("Version: " + version);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			((TextView) view.findViewById(R.id.tvVersionNumber)).setText("Version: Not available!");
		}

		return view;
	}

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
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(ApplicationClass.getInstance().getGoogleApiClient(), ApplicationClass.LEADERBOARD_ID), REQUEST_LEADERBOARD);
			}
		});
	}

}
