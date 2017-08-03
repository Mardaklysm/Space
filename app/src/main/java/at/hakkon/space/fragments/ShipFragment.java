package at.hakkon.space.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.EGameOverReason;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.event.RestartGameEvent;
import at.hakkon.space.listener.IShipListener;

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

		view.findViewById(R.id.bRestartGame).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RestartGameEvent restartGameEvent = new RestartGameEvent(1, EGameOverReason.RestartRequested);
				restartGameEvent.execute(getContext());
			}
		});

		return view;
	}


	@Override
	public void shipUpdated(PlayerShip ship) {
		((TextView)view.findViewById(R.id.tvShipInfo)).setText(ship.getInformationDump());
	}
}
