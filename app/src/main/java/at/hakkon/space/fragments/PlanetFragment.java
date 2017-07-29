package at.hakkon.space.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.hakkon.space.ApplicationClass;
import at.hakkon.space.IPlanetVisitListener;
import at.hakkon.space.IShipListener;
import at.hakkon.space.R;
import at.hakkon.space.datamodel.Ship;
import at.hakkon.space.datamodel.event.AbsEvent;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;


/**
 * Created by Markus on 05.07.2017.
 */

public class PlanetFragment extends Fragment implements IShipListener, IPlanetVisitListener{

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
	public void shipUpdated(Ship ship) {
		if (ship.getCurrentPlanet().equals(planet)){
			return;
		}
		this.planet = ship.getCurrentPlanet();
		planetChanged();
	}

	@Override
	public void planetVisit(AbsPlanet planet) {
		shipUpdated(ApplicationClass.getInstance().getShip());

		handleVisit(planet);
	}

	private void handleVisit(AbsPlanet planet) {
		AbsEvent event = planet.getEvent();

		switch (event.getEventType()){

			case Nothing:
				ApplicationClass.getInstance().toast(getContext(), "Nothing special happened");
				break;
			case Shop:
				//TODO: Implement
				break;
			case Battle:
				break;
			//TODO: Implement
			case ResourceBonus:
				boolean isFirstTimeVisit = planet.getIsFirsTimeVisit();


				if (isFirstTimeVisit){
					ApplicationClass.getInstance().toast(getContext(), "You collected minerals worth " + event.getResourceBonus() + "€");
					event.execute();
				}else{
					ApplicationClass.getInstance().toast(getContext(), "There are no minerals left.");
				}

				break;
		}
	}
}
