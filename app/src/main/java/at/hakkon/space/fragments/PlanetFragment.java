package at.hakkon.space.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.listener.IPlanetVisitListener;
import at.hakkon.space.listener.IShipListener;
import at.hakkon.space.R;
import at.hakkon.space.datamodel.ship.Ship;
import at.hakkon.space.event.AbsEvent;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;

import static android.view.View.GONE;


/**
 * Created by Markus on 05.07.2017.
 */

public class PlanetFragment extends Fragment implements IShipListener, IPlanetVisitListener {

	private View view;

	private AbsPlanet planet;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_planet, container, false);


		ApplicationClass.getInstance().addShipListener(this);
		ApplicationClass.getInstance().addPlanetVisitorListener(this);
		planet = ApplicationClass.getInstance().getShip().getCurrentPlanet();
		planetChanged();

		initExecuteEventButton();

		return view;
	}

	private void initExecuteEventButton() {
		Button button = (Button) view.findViewById(R.id.bExecutePlanetEvent);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				executeEvent(planet);
			}
		});
	}

	private void planetChanged() {
		TextView tvPlanetInfo = (TextView) view.findViewById(R.id.tvPlanetInfo);

		tvPlanetInfo.setText(planet.getInformationDump());
		updateExecuteEventButton(planet.getEvent().canBeExecuted());
	}

	@Override
	public void shipUpdated(Ship ship) {
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

	private void executeEvent(AbsPlanet planet) {
		AbsEvent event = planet.getEvent();

		switch (event.getEventType()) {

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

				if (event.canBeExecuted()) {
					event.execute(getContext());
					updateExecuteEventButton(false);
				}

				break;
			case Question:
				if (event.canBeExecuted()) {
					event.execute(getContext());
					updateExecuteEventButton(false);
				}
				break;
		}
	}

	private void updateExecuteEventButton(boolean enable) {
		view.findViewById(R.id.bExecutePlanetEvent).setVisibility(enable ? View.VISIBLE : GONE);
	}
}
