package at.hakkon.space.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


		return view;
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
		if (!galaxy.getPlanets().contains(planet)){
			planet = galaxy.getFirstPlanet();
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		ApplicationClass.getInstance().removeShipListener(this);
		ApplicationClass.getInstance().removePlanetVisitListener(this);

	}
}
