package at.hakkon.space.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.ship.AbsShip;
import at.hakkon.space.datamodel.ship.EShipType;
import at.hakkon.space.datamodel.ship.EnemyShipA;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.listener.IShipListener;
import at.hakkon.space.views.RoomView;

public class BattleActivity extends AppCompatActivity implements IShipListener {

	private ApplicationClass appClass = ApplicationClass.getInstance();

	private AbsShip enemyShip;

	private int energyPlayer = 0;
	private int energyEnemy = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battle);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		ApplicationClass.getInstance().addShipListener(this);

		this.energyPlayer = appClass.getShip().getInitialEnergy();

		//UI
		updateHeader(ApplicationClass.getInstance().getShip());
		updateWeaponButtons(ApplicationClass.getInstance().getShip());

		int enemyShipTypeOrdinal = getIntent().getIntExtra("enemyShipTypeOrdinal", 0);
		EShipType enemyShipType = EShipType.values()[enemyShipTypeOrdinal];
		initEnemyShip(enemyShipType);

		updateTitle("Battle: " + ApplicationClass.getInstance().getShip().getName() + " Vs. " + enemyShip.getName());
		addMessage("You are engaging an enemy ship: " + enemyShipType.name() + " (" + enemyShip.getHealth() + "HP)");


		initConfirmButton();
	}

	private void initConfirmButton() {
		Button button = (Button) findViewById(R.id.bBattleNextRound);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nextRound();
			}
		});
	}

	private void setConfirmButtonEnabled(boolean enabled){
		findViewById(R.id.bBattleNextRound).setEnabled(enabled);
	}

	private void nextRound() {
		//Fire weapons for player

		//Fire weapons for enemy

	}

	private void updateTitle(String text) {
		TextView textView = (TextView) findViewById(R.id.tvBattleTitle);
		textView.setText(text);
	}

	private void addMessage(String message) {
		TextView textView = (TextView) findViewById(R.id.tvBattleLog);
		textView.setText(textView + message + "\n");
	}

	ArrayList<RoomView> roomViews = new ArrayList<>();

	private void initEnemyShip(EShipType enemyShipType) {
		switch (enemyShipType) {

			case Enemy_A:
				enemyShip = new EnemyShipA("Enemy A");
				break;
			case Player_A:
				throw new RuntimeException("Ship cant be Player Ship");
			case Enemy_B:
				throw new RuntimeException("Enemy_B not implemented yet");
		}

		//Init room Widgets
		ArrayList<AbsRoom> rooms = enemyShip.getRooms();

		LinearLayout parent = (LinearLayout) findViewById(R.id.llBattleEnemy);
		for (int i = 0; i < rooms.size(); i++) {
			//AbsRoom room = rooms.get(i);
			RoomView roomView = new RoomView(this, enemyShip, i);
			parent.addView(roomView);
			roomViews.add(roomView);
		}

		energyEnemy = enemyShip.getInitialEnergy();
	}

	private void updateWeaponButtons(PlayerShip ship) {
		PlayerShip playerShip = ApplicationClass.getInstance().getShip();

		ArrayList<Weapon> weapons = playerShip.getWeapons();

		if (weapons.size() > 0) {
			Weapon weapon = playerShip.getWeapons().get(0);
			((Button) findViewById(R.id.bBattleWeapon1)).setText(weapon.getName() + "(" + weapon.getHealth() + ")");
		} else {
			((Button) findViewById(R.id.bBattleWeapon1)).setText("-UNUSED SLOT-");
		}

		if (weapons.size() > 1) {
			Weapon weapon = playerShip.getWeapons().get(0);
			((Button) findViewById(R.id.bBattleWeapon2)).setText(weapon.getName() + "(" + weapon.getHealth() + ")");
		} else {
			((Button) findViewById(R.id.bBattleWeapon1)).setText("-UNUSED SLOT-");
		}

		if (weapons.size() > 2) {
			Weapon weapon = playerShip.getWeapons().get(0);
			((Button) findViewById(R.id.bBattleWeapon3)).setText(weapon.getName() + "(" + weapon.getHealth() + ")");
		} else {
			((Button) findViewById(R.id.bBattleWeapon1)).setText("-UNUSED SLOT-");
		}

		if (weapons.size() > 3) {
			Weapon weapon = playerShip.getWeapons().get(0);
			((Button) findViewById(R.id.bBattleWeapon4)).setText(weapon.getName() + "(" + weapon.getHealth() + ")");
		} else {
			((Button) findViewById(R.id.bBattleWeapon1)).setText("-UNUSED SLOT-");
		}
	}


	@Override
	public void shipUpdated(PlayerShip ship) {
		updateHeader(ship);
		updateWeaponButtons(ship);
	}

	private void updateHeader(PlayerShip ship) {
		((TextView) findViewById(R.id.tvHeaderCash)).setText("Cash: " + ship.getMoney());
		((TextView) findViewById(R.id.tvHeaderFuel)).setText("Fuel: " + ship.getFuel());
		((TextView) findViewById(R.id.tvHeaderHealth)).setText("Health: " + ship.getHealth());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		ApplicationClass.getInstance().removeShipListener(this);
	}
}
