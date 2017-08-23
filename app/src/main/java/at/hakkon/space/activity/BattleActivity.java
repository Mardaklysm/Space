package at.hakkon.space.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Loot;
import at.hakkon.space.datamodel.inventory.Weapon;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.ship.AbsShip;
import at.hakkon.space.datamodel.ship.EShipType;
import at.hakkon.space.datamodel.ship.EnemyShipA;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.listener.IShipListener;
import at.hakkon.space.utility.Utility;
import at.hakkon.space.views.RoomView;

public class BattleActivity extends AppCompatActivity implements IShipListener {

	private static final String TAG = "BattleActivity";
	private ApplicationClass appClass = ApplicationClass.getInstance();

	private AbsShip enemyShip;

	private int energyPlayer = 0;
	private int energyEnemy = 0;

	private int roundNr = 0;

	private HashMap<Weapon, Button> mapWeaponButtons = new HashMap<>();
	private HashMap<Weapon, AbsRoom> mapWeaponRooms = new HashMap<>();

	private int level;

	private static BattleActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ApplicationClass.getInstance().updateActiveContext(this);
		setContentView(R.layout.activity_battle);

		//Get parameters;
		int enemyShipTypeOrdinal = getIntent().getIntExtra("enemyShipTypeOrdinal", 0);
		level = getIntent().getIntExtra("level", 1);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		ApplicationClass.getInstance().addShipListener(this);

		this.energyPlayer = appClass.getShip().getInitialEnergy();

		//UI
		updateHeader(ApplicationClass.getInstance().getShip());

		initWeaponButtonsListener(ApplicationClass.getInstance().getShip());

		updateWeaponButtons(ApplicationClass.getInstance().getShip());


		EShipType enemyShipType = EShipType.values()[enemyShipTypeOrdinal];
		initEnemyShip(enemyShipType);


		updateTitle();
		addMessage("You are engaging an enemy ship: " + enemyShipType.name() + " (" + enemyShip.getHealth() + "HP)\n");


		initConfirmButton();

		roundNr++;
		addMessage("[Round " + roundNr + "]: Make your moves!");
	}


	private void initWeaponButtonsListener(PlayerShip playerShip) {
		ArrayList<Weapon> weapons = playerShip.getEquippedWeapons();

		if (weapons.size() > 0) {
			final Weapon weapon = playerShip.getEquippedWeapons().get(0);
			addWeaponSelectionListener(findViewById(R.id.bBattleWeapon1), weapon);
		} else {
			findViewById(R.id.bBattleWeapon1).setVisibility(View.INVISIBLE);
		}

		if (weapons.size() > 1) {
			Weapon weapon = playerShip.getEquippedWeapons().get(1);
			addWeaponSelectionListener(findViewById(R.id.bBattleWeapon2), weapon);
		} else {
			findViewById(R.id.bBattleWeapon2).setVisibility(View.INVISIBLE);
		}

		if (weapons.size() > 2) {
			Weapon weapon = playerShip.getEquippedWeapons().get(2);
			addWeaponSelectionListener(findViewById(R.id.bBattleWeapon3), weapon);
		} else {
			findViewById(R.id.bBattleWeapon3).setVisibility(View.INVISIBLE);
		}

		if (weapons.size() > 3) {
			Weapon weapon = playerShip.getEquippedWeapons().get(3);
			addWeaponSelectionListener(findViewById(R.id.bBattleWeapon4), weapon);
		} else {
			findViewById(R.id.bBattleWeapon4).setVisibility(View.INVISIBLE);
		}

		((Button) findViewById(R.id.bBattleWeapon1)).setAllCaps(false);
		((Button) findViewById(R.id.bBattleWeapon2)).setAllCaps(false);
		((Button) findViewById(R.id.bBattleWeapon3)).setAllCaps(false);
		((Button) findViewById(R.id.bBattleWeapon4)).setAllCaps(false);

	}

	private void addWeaponSelectionListener(View view, final Weapon weapon) {
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			//TODO UNSELECT THE SELECTED AND SLEECT THE CURRENT ONE USABILITY!!!!!!!!!!!!!!!!
			public void onClick(View v) {
				selectWeapon(weapon);

			}
		});

		mapWeaponButtons.put(weapon, ((Button) view));
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

	private void setConfirmButtonEnabled(boolean enabled) {
		findViewById(R.id.bBattleNextRound).setEnabled(enabled);
	}


	private void nextRound() {
		//HANDLE BATTLE
		//Fire weapons for player AT the enemy
		for (Map.Entry<Weapon, AbsRoom> entry : mapWeaponRooms.entrySet()) {
			if (entry.getKey().hasTarget()) {
				int damage = entry.getValue().attackWithWeapon(appClass.getShip(), enemyShip, entry.getKey());

				if (damage > 0) {
					String message = "Enemy (" + entry.getValue().getName() + ") took " + damage + " damage (" + entry.getKey().getName() + ")";
					playActionAnimation(1, colorGreen, String.valueOf(damage));
					addMessage(message);
				} else {
					String message = "Enemy (" + entry.getValue().getName() + ") evaded (" + entry.getKey().getName() + ")";
					playActionAnimation(1, colorGreen, "Miss");
					addMessage(message);
				}
			}
			entry.getKey().setTarget(null);
		}


		//Fire weapons for Enemy AT the player
		for (Weapon weapon : enemyShip.getWeapons()) {
			if (energyEnemy - weapon.getEnergyCost() > 0) {

				//Calculate room target
				int rooms = appClass.getShip().getRooms().size();

				Random random = new Random();
				int rndIdx = random.nextInt(rooms);
				AbsRoom target = appClass.getShip().getRooms().get(rndIdx);

				int damage = target.attackWithWeapon(enemyShip, appClass.getShip(), weapon);
				energyEnemy -= weapon.getEnergyCost();

				if (damage >= 0) {
					String message = "You " + target.getName() + " took " + damage + " damage (" + weapon.getName() + ")";
					playActionAnimation(2, colorRed, String.valueOf(damage));
					addMessage(message);
				} else {
					String message = "You evaded (" + weapon.getName() + ")";
					playActionAnimation(1, colorRed, "Miss");
					addMessage(message);
				}
			}
		}

		//PPREPARE NEXT ROUND
		//resetUI
		selectWeapon(null);
		resetWeaponCommandLists();
		updateWeaponButtons(appClass.getShip());


		//Check if enemy is dead
		if (enemyShip.getHealth() <= 0) {
			refreshUI();
			battleOver();
			return;
		}

		//Regenerate Rooms
		for (AbsRoom room: ApplicationClass.getInstance().getShip().getRooms()){
			room.regenerate();
		}
		for (AbsRoom room: enemyShip.getRooms()){
			room.regenerate();
		}

		//AddEnergy
		energyPlayer += appClass.getShip().getInitialEnergy();
		energyEnemy += enemyShip.getInitialEnergy();

		//Update message log
		roundNr++;
		addMessage("\n[Round " + roundNr + "]: Make your moves!");

		//update UI
		refreshUI();


	}

	private void playActionAnimation(int position, int color, String text) {

		//Left
		if (position == 1){

		//Right
		}else{

		}

		final LinearLayout ll = (LinearLayout) findViewById(R.id.llSpacer);

		final TextView tv = new TextView(this);

		tv.setText(text);
		tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
		tv.setTextColor(color);
		ll.addView(tv);

		Animation animation = AnimationUtils.loadAnimation(this, R.anim.move01);

		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				Log.i(TAG, "Damage animation started");
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ll.removeView(tv);
				Log.i(TAG, "Damage animation ended");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				Log.i(TAG, "Damage animation repeated");
			}
		});


		tv.startAnimation(animation);


	}

	private void battleOver() {
		addMessage("Enemy has been destroyed !!!");
		final Loot loot = enemyShip.getLoot();

		String message = "Loot Report\n";
		message += "Fuel: " + loot.getFuel() + "\n";
		message += "Money: " + loot.getMoney() + "\n";

		message += "\nItems\n";

		for (IInventoryItem item : enemyShip.getLoot().getItems()) {
			message += "-" + item.getName() + "\n";
			appClass.getShip().getInventory().add(item);
		}


		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		TextView textView = new TextView(this);
		textView.setText(message);

		builder.setCustomTitle(textView);

		CharSequence[] charSequences = new CharSequence[1];
		charSequences[0] = "Okay";

		builder.setItems(charSequences, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				appClass.getShip().updateFuel(loot.getFuel());
				appClass.getShip().updateMoney(loot.getMoney());
				BattleActivity.super.onBackPressed();
				//event.callback(this, which);
			}
		}).show();

	}

	private void refreshUI() {

		updateTitle();
		energyChanged();

		for (RoomView roomView: roomViews){
			roomView.update();
		}
	}

	private void energyChanged() {
		String text = "Weapon Energy: " + energyPlayer;
		TextView textView = (TextView) findViewById(R.id.tvWeaponTitle);
		textView.setText(text);
	}

	private void updateTitle() {
		int healthEnemy = enemyShip.getHealth();
		String enemyName = enemyShip.getName();

		String text = "Engaged Enemy: " + enemyName + " (" + healthEnemy + "HP, " + energyEnemy + " Energy)";
		TextView textView = (TextView) findViewById(R.id.tvBattleTitle);
		textView.setText(text);
	}

	private void addMessage(String message) {
		TextView textView = (TextView) findViewById(R.id.tvBattleLog);
		textView.setText(textView.getText() + message + "\n");

		final ScrollView sc = (ScrollView) findViewById(R.id.scBattle);
		sc.post(new Runnable() {
			public void run() {
				sc.fullScroll(View.FOCUS_DOWN);
			}
		});

	}

	ArrayList<RoomView> roomViews = new ArrayList<>();

	private void initEnemyShip(EShipType enemyShipType) {
		switch (enemyShipType) {

			case Enemy_A:
				enemyShip = new EnemyShipA("Enemy A", level);
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
			final AbsRoom room = rooms.get(i);
			final RoomView roomView = new RoomView(this, enemyShip, i);
			parent.addView(roomView);
			roomViews.add(roomView);


			roomView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (selectedWeapon == null) {
						return;
					}
					if (selectedWeapon.getTarget() != null && selectedWeapon.getTarget().equals(room)) {
						return;
					}
					selectTarget(selectedWeapon, room);
				}
			});
		}

		energyEnemy = enemyShip.getInitialEnergy();
	}

	private void selectTarget(Weapon weapon, AbsRoom target) {

		boolean canAffordToTarget = energyPlayer - weapon.getEnergyCost() >= 0;

		if (target != null && !canAffordToTarget) {
			Utility.getInstance().showTextDialog(this, "Not have enough energy!");
			return;
		}
		//Refund
		if (weapon.getTarget() != null && target == null) {
			mapWeaponRooms.remove(weapon);
			energyPlayer += weapon.getEnergyCost();//CHarge
		} else if (weapon.getTarget() == null && target != null) {
			if (canAffordToTarget) {
				mapWeaponRooms.put(weapon, target);
				energyPlayer += -weapon.getEnergyCost();
			}
		}

		selectedWeapon.setTarget(target);

		mapWeaponButtons.get(weapon).getBackground().setColorFilter(target == null ? colorSelected : colorWeaponLocked, PorterDuff.Mode.DARKEN);
		mapWeaponButtons.get(weapon).setText(weapon.getBattleLabel(ApplicationClass.getInstance().getShip().getWeaponRoom().getEffectiveEfficency()));


		refreshUI();
	}

	private Weapon selectedWeapon;
	private int colorSelected = Color.parseColor("#dbd530");
	private int colorRoomLocked = Color.parseColor("#42bc31");
	private int colorWeaponLocked = Color.parseColor("#42bc31");

	private int colorGreen = Color.parseColor("#52bc35");
	private int colorRed = Color.parseColor("#912416");

	private void selectWeapon(Weapon weapon) {


		for (Map.Entry<Weapon, Button> entry : mapWeaponButtons.entrySet()) {
			if (!entry.getKey().hasTarget()) {
				entry.getValue().getBackground().setColorFilter(null);
			}

		}

		if (weapon != null) {
			mapWeaponButtons.get(weapon).getBackground().setColorFilter(colorSelected, PorterDuff.Mode.DARKEN);
		}

		selectedWeapon = weapon;

		//Reset target if the weapong ets reselected after already having a target selected previously
		if (weapon != null && weapon.getTarget() != null) {
			selectTarget(weapon, null);
		}

		refreshUI();
	}


	private void resetWeaponCommandLists() {
		for (Map.Entry<Weapon, Button> entry : mapWeaponButtons.entrySet()) {
			Weapon key = entry.getKey();
			Button button = entry.getValue();
			button.getBackground().setColorFilter(null);
		}

		mapWeaponRooms.clear();
		mapWeaponButtons.clear();
	}

	private void updateWeaponButtons(PlayerShip playerShip) {

		ArrayList<Weapon> weapons = playerShip.getEquippedWeapons();

		if (weapons.size() > 0) {
			Weapon weapon = playerShip.getEquippedWeapons().get(0);
			((Button) findViewById(R.id.bBattleWeapon1)).setText(weapon.getBattleLabel(playerShip.getWeaponRoom().getEfficency()));
			mapWeaponButtons.put(weapon, ((Button) findViewById(R.id.bBattleWeapon1)));
		}

		if (weapons.size() > 1) {
			Weapon weapon = playerShip.getEquippedWeapons().get(1);
			((Button) findViewById(R.id.bBattleWeapon2)).setText(weapon.getBattleLabel(playerShip.getWeaponRoom().getEfficency()));
			mapWeaponButtons.put(weapon, ((Button) findViewById(R.id.bBattleWeapon2)));
		}

		if (weapons.size() > 2) {
			Weapon weapon = playerShip.getEquippedWeapons().get(2);
			((Button) findViewById(R.id.bBattleWeapon3)).setText(weapon.getBattleLabel(playerShip.getWeaponRoom().getEfficency()));
			mapWeaponButtons.put(weapon, ((Button) findViewById(R.id.bBattleWeapon3)));
		}

		if (weapons.size() > 3) {
			Weapon weapon = playerShip.getEquippedWeapons().get(3);
			((Button) findViewById(R.id.bBattleWeapon4)).setText(weapon.getBattleLabel(playerShip.getWeaponRoom().getEfficency()));
			mapWeaponButtons.put(weapon, ((Button) findViewById(R.id.bBattleWeapon4)));
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

	@Override
	public void onResume(){
		super.onResume();

		ApplicationClass.getInstance().updateActiveContext(this);
	}

	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_POSITIVE) {
					BattleActivity.super.onBackPressed();
				}
			}
		};

		String text = "Do you want to get away from the battle?";

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(text).setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	public static Context getInstance() {
		return instance;
	}
}
