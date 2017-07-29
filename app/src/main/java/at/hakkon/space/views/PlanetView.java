package at.hakkon.space.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import at.hakkon.space.R;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;

/**
 * Created by Markus on 29.07.2017.
 */

public class PlanetView extends LinearLayout{

	private View view;

	private AbsPlanet planet;

	public PlanetView(Context context, AbsPlanet planet) {
		super(context);

		this.planet = planet;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.view_planet, this, true);

		((TextView)view.findViewById(R.id.tvContent)).setText(planet.getInformationDump());

	}



	public AbsPlanet getPlanet(){
		return planet;
	}


}
