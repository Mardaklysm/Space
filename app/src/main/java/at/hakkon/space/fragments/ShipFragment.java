package at.hakkon.space.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.application.IMetaDataListener;
import at.hakkon.space.datamodel.EGameOverReason;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.event.RestartGameEvent;
import at.hakkon.space.event.SellEvent;
import at.hakkon.space.event.UpgradeEvent;
import at.hakkon.space.listener.IShipListener;

/**
 * Created by Markus on 05.07.2017.
 */

public class ShipFragment extends Fragment implements IShipListener, IMetaDataListener {

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
				restartGameEvent.execute(getActivity());
			}
		});

		TextView title = (TextView) view.findViewById(R.id.tvFragmentShipTitle);
		title.setText("Ship: " + ApplicationClass.getInstance().getShip().getName());

		ApplicationClass.getInstance().addMetaDataListener(this);

		return view;
	}


	@Override
	public void shipUpdated(PlayerShip ship) {
		String scoreText = "Score: " + ApplicationClass.getInstance().getScore() + "\n\n";

		((TextView) view.findViewById(R.id.tvShipInfo)).setText(scoreText + ship.getInformationDump());

		refreshRooms(ship);
		refreshWeapons(ship);
		refreshInventory(ship);

	}

	private void refreshRooms(PlayerShip ship) {
		LinearLayout llShipRooms = (LinearLayout) view.findViewById(R.id.llShipRooms);

		llShipRooms.removeAllViews();

		ArrayList<AbsRoom> rooms = ship.getRooms();

		for (final AbsRoom room : rooms) {
			final Button button = new Button(getActivity());
			button.setText(room.getName() + " (" + room.getEffectiveEfficency() + ")");

			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					UpgradeEvent event = new UpgradeEvent(room, 1);
					event.execute(getActivity());
				}
			});

			llShipRooms.addView(button);
		}


	}

	private void refreshWeapons(PlayerShip ship) {
		LinearLayout llShipWeapons = (LinearLayout) view.findViewById(R.id.llShipWeapons);

		llShipWeapons.removeAllViews();

		PlayerShip playerShip = ApplicationClass.getInstance().getShip();

		ArrayList<Weapon> allWeapons = playerShip.getWeapons();
		ArrayList<Weapon> equippedWeapons = playerShip.getEquippedWeapons();

		for (Weapon weapon : equippedWeapons) {
			createWeaponButton(ship, weapon);
		}

		for (Weapon weapon : allWeapons) {
			if (weapon.isEquipped()) {
				continue;
			}
			createWeaponButton(ship, weapon);
		}
	}

	private void refreshInventory(final PlayerShip ship) {
		LinearLayout llShipInventory = (LinearLayout) view.findViewById(R.id.llShipInventory);

		llShipInventory.removeAllViews();


		ArrayList<IInventoryItem> inventory = ship.getInventory();

		for (final IInventoryItem item : inventory) {
			if (!item.isEquipped()) {
				final Button button = new Button(getActivity());
				llShipInventory.addView(button);
				button.setText(item.getName() + " (" + item.getCashValue() + "$)");

				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						SellEvent sellEvent = new SellEvent(item, 1);
						sellEvent.execute(getActivity());
					}
				});
			}
		}
	}

	private int colorSelected = Color.parseColor("#42bc31");
	private final static int MAX_WEAPONS_EQUIP_COUNT = 4;

	private void createWeaponButton(final PlayerShip ship, final Weapon weapon) {
		LinearLayout llShipWeapons = (LinearLayout) view.findViewById(R.id.llShipWeapons);

		final Button button = new Button(getActivity());

		llShipWeapons.addView(button);
		button.setText(weapon.getName());

		if (weapon.isEquipped()) {

			button.getBackground().setColorFilter(colorSelected, PorterDuff.Mode.DARKEN);
		}

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (weapon.isEquipped()) {
					weapon.equip(false);
					button.getBackground().setColorFilter(null);
				} else if ((!weapon.isEquipped()) && (ship.getEquippedWeapons().size() < MAX_WEAPONS_EQUIP_COUNT)) {
					button.getBackground().setColorFilter(colorSelected, PorterDuff.Mode.DARKEN);
					weapon.equip(true);
				}
				refreshInventory(ship);
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ApplicationClass.getInstance().removeShipListener(this);
		ApplicationClass.getInstance().removeMetaDataListener(this);
	}

	@Override
	public void scoreChanged(int score) {
		shipUpdated(ApplicationClass.getInstance().getShip());
	}
}
