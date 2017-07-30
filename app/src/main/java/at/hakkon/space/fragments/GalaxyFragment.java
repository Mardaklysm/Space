package at.hakkon.space.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.listener.IGalaxyListener;
import at.hakkon.space.views.GalaxyRowView;

/**
 * Created by Markus on 05.07.2017.
 */

public class GalaxyFragment extends Fragment implements IGalaxyListener {

	private final static String TAG = "MapFragment";

	private View view;

	private Galaxy galaxy;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_galaxy, container, false);

		ApplicationClass.getInstance().addGalaxyListener(this);

		galaxy = ApplicationClass.getInstance().getGalaxy();
		updatePlanets();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void galaxyUpdated(Galaxy galaxy) {
		this.galaxy = galaxy;
		updatePlanets();
	}

	private void updatePlanets() {
		//((TextView) view.findViewById(R.id.tvGalaxyInfo)).setText(galaxy.getName() + " (" + galaxy.getReachablePlanets().size() + " Planets)");

		((LinearLayout) view.findViewById(R.id.llGalaxyContent)).removeAllViews();

		for (int i=0; i< galaxy.getGalaxyDepth(); i++){

			ArrayList<AbsPlanet> rowPlanets = new ArrayList<>();
			for (AbsPlanet planet: galaxy.getPlanets()){
				if (planet.getPlanetPosition().getY() == i){
					rowPlanets.add(planet);
				}
			}

			GalaxyRowView galaxyRowView = new GalaxyRowView(getContext(), rowPlanets);
			LinearLayout parent = (LinearLayout) view.findViewById(R.id.llGalaxyContent);

			parent.addView(galaxyRowView);

		}




	}



}
