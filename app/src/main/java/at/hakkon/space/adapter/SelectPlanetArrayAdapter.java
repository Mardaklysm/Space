package at.hakkon.space.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import at.hakkon.space.views.PlanetView;


public class SelectPlanetArrayAdapter extends ArrayAdapter<SelectPlanetListEntry> {
	private Context mContext;
	private List<SelectPlanetListEntry> items;

	public SelectPlanetArrayAdapter(Context context, int textViewResourceId, List<SelectPlanetListEntry> list) {
		super(context, textViewResourceId, list);
		mContext = context;
		items = list;
	}

	@Override
	public View getView(final int position, View v, ViewGroup parent) {
		PlanetView planetView = new PlanetView(mContext,items.get(position).getPlanet());
		return planetView;
	}

	public void refreshWidgets(){
		//TODO: Implement
	}

}