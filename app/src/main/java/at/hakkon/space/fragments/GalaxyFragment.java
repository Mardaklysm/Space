package at.hakkon.space.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.adapter.SelectPlanetArrayAdapter;
import at.hakkon.space.adapter.SelectPlanetListEntry;
import at.hakkon.space.listener.IGalaxyListener;
import at.hakkon.space.R;
import at.hakkon.space.event.TravelQuestionEvent;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.galaxy.Galaxy;

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
		((TextView) view.findViewById(R.id.tvGalaxyInfo)).setText(galaxy.getName() + " (" + galaxy.getReachablePlanets().size() + " Planets)");

		ListView listView = (ListView) view.findViewById(R.id.listPlanets);

		ArrayList<SelectPlanetListEntry> entries = new ArrayList<>();

		for (AbsPlanet planet : galaxy.getReachablePlanets()) {
			entries.add(new SelectPlanetListEntry(planet));
		}

		listView.setAdapter(new SelectPlanetArrayAdapter(getContext(), R.layout.default_list_entry, entries));

		listView.setClickable(true);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				ListView listView = (ListView) GalaxyFragment.this.view.findViewById(R.id.listPlanets);
				Object selectedItem = listView.getItemAtPosition(position);

				if (selectedItem != null && (selectedItem instanceof SelectPlanetListEntry)) {
					AbsPlanet selectedPlanet = ((SelectPlanetListEntry) selectedItem).getPlanet();
					if (selectedPlanet.equals(ApplicationClass.getInstance().getShip().getCurrentPlanet())) {
						return;
					}
					TravelQuestionEvent travelQuestionEvent = new TravelQuestionEvent(galaxy.getLevel());
					travelQuestionEvent.init(getContext(), ApplicationClass.getInstance().getShip(), selectedPlanet);
					travelQuestionEvent.execute(getContext());
				}


			}
		});


	}



}
