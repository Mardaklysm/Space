package at.hakkon.space.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.event.planet.TravelQuestionEvent;
import at.hakkon.space.listener.IPlanetVisitListener;

/**
 * Created by Markus on 29.07.2017.
 */

public class GalaxyRowView extends LinearLayout implements IPlanetVisitListener {

	private View view;
	private PlayerShip ship;

	private AbsPlanet planet;

	public GalaxyRowView(Context context, PlayerShip ship, AbsPlanet planet) {
		super(context);

		this.planet = planet;
		this.ship = ship;


		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.view_planet_row, this, true);

		ApplicationClass.getInstance().addPlanetVisitorListener(this);

		updateView();

	}

	public void updateView() {

		Button btn = (Button) view.findViewById(R.id.bPos0);

		int position = planet.getPlanetPosition().getX();
		addButtonListener(btn, planet);
		btn.setText(planet.getName());

		if (planet.equals(ship.getCurrentPlanet())) {
			btn.getBackground().setColorFilter(Color.parseColor("#00ff00"), PorterDuff.Mode.DARKEN);
		} else {
			btn.getBackground().setColorFilter(null);
		}

		//Move view based on its position

		//Position is form left to right 0 -> 3

		int margin = (int) getResources().getDimension(R.dimen.planet_button_positioning_margin);
		if (position == 0) {

		} else if (position == 1) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.LEFT;

			params.setMargins(margin, 0, 0, 0);
			btn.setLayoutParams(params);
		} else if (position == 2) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.RIGHT;

			params.setMargins(0, 0, margin, 0);
			btn.setLayoutParams(params);
		} else if (position == 3) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.RIGHT;

			btn.setLayoutParams(params);
		}


	}


	private void addButtonListener(Button button, final AbsPlanet planet) {
		button.setText(planet.getName());
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (planet.equals(ApplicationClass.getInstance().getShip().getCurrentPlanet())) {
					if (planet.getEvent() != null) {
						planet.getEvent().execute(getContext());
					}
				} else {
					TravelQuestionEvent travelQuestionEvent = new TravelQuestionEvent(1);
					travelQuestionEvent.init(getContext(), ApplicationClass.getInstance().getShip(), planet);
					travelQuestionEvent.execute(getContext());
				}

			}
		});

	}


	@Override
	public void planetVisit(AbsPlanet planet) {
		updateView();
	}

}
