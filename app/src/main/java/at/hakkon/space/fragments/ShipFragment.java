package at.hakkon.space.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.hakkon.space.ApplicationClass;
import at.hakkon.space.IShipListener;
import at.hakkon.space.R;
import at.hakkon.space.datamodel.Ship;

/**
 * Created by Markus on 05.07.2017.
 */

public class ShipFragment extends Fragment implements IShipListener {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_ship, container, false);

		ApplicationClass.getInstance().addShipListener(this);
		shipUpdated(ApplicationClass.getInstance().getShip());

		return view;
	}


	@Override
	public void shipUpdated(Ship ship) {
		((TextView)view.findViewById(R.id.tvShipInfo)).setText(ship.getInformationDump());
	}
}
