package at.hakkon.space.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.event.TravelQuestionEvent;

/**
 * Created by Markus on 29.07.2017.
 */

public class GalaxyRowView extends LinearLayout{

	private View view;

	private  ArrayList<AbsPlanet> planets;

	public GalaxyRowView(Context context, ArrayList<AbsPlanet> planets) {
		super(context);

		this.planets = planets;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.view_planet_row, this, true);

		boolean posExists0 = false;
		boolean posExists1 = false;
		boolean posExists2 = false;
		boolean posExists3 = false;

		for (AbsPlanet planet: planets){
			int position = planet.getPlanetPosition().getX();

			switch (position){
				case 0: addButtonListener((Button) view.findViewById(R.id.bPos0), planet); posExists0 = true; break;
				case 1: addButtonListener((Button) view.findViewById(R.id.bPos1), planet); posExists1 = true; break;
				case 2: addButtonListener((Button) view.findViewById(R.id.bPos2), planet); posExists2 = true; break;
				case 3: addButtonListener((Button) view.findViewById(R.id.bPos3), planet); posExists3 = true; break;
			}
		}

		if (!posExists0){
			 view.findViewById(R.id.bPos0).setVisibility(INVISIBLE);
		}
		if (!posExists1){
			view.findViewById(R.id.bPos1).setVisibility(INVISIBLE);
		}
		if (!posExists2){
			view.findViewById(R.id.bPos2).setVisibility(INVISIBLE);
		}
		if (!posExists3){
			view.findViewById(R.id.bPos3).setVisibility(INVISIBLE);
		}


	}


	private void addButtonListener(Button button, final AbsPlanet planet){
		button.setText(planet.getName());
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (planet.equals(ApplicationClass.getInstance().getShip().getCurrentPlanet())) {
					return;
				}
				TravelQuestionEvent travelQuestionEvent = new TravelQuestionEvent(1);
				travelQuestionEvent.init(getContext(), ApplicationClass.getInstance().getShip(), planet);
				travelQuestionEvent.execute(getContext());
			}
		});

	}



	public ArrayList<AbsPlanet> getPlanets(){
		return planets;
	}


}
