package at.hakkon.space.activity;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.games.Games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.inventory.IConsumableItem;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.inventory.Loot;
import at.hakkon.space.datamodel.inventory.weapon.AbsWeapon;
import at.hakkon.space.datamodel.room.AbsRoom;
import at.hakkon.space.datamodel.room.AttackResult;
import at.hakkon.space.datamodel.ship.AbsShip;
import at.hakkon.space.datamodel.ship.EShipType;
import at.hakkon.space.datamodel.ship.EnemyBossShipA;
import at.hakkon.space.datamodel.ship.EnemyShipA;
import at.hakkon.space.datamodel.ship.EnemyShipB;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.event.battle.UseCrystalEvent;
import at.hakkon.space.event.planet.GoToGalaxyEvent;
import at.hakkon.space.listener.IShipListener;
import at.hakkon.space.utility.Utility;
import at.hakkon.space.views.RoomView;

/**
 * Handles a battle between two ships.
 */
public class BattleActivity extends AppCompatActivity implements IShipListener {

	private static final String TAG = "BattleActivity";
	private ApplicationClass appClass = ApplicationClass.getInstance();

	private AbsShip enemyShip;

	private int energyPlayer = 0;
	private int energyEnemy = 0;

	private int roundNr = 0;

	private HashMap<AbsWeapon, Button> mapWeaponButtons = new HashMap<>();
	private HashMap<AbsWeapon, AbsRoom> mapWeaponRooms = new HashMap<>();

	private int level;

	private static BattleActivity instance;

	private boolean isBossBattle;

	private ArrayList<MessageData> messageDataQueue = new ArrayList<>();


	private HashMap<AbsRoom, RoomView> mapRooms = new HashMap<>();

	private AbsWeapon selectedWeapon;
	private int colorSelected = Color.parseColor("#dbd530");
	private int colorWeaponLocked = Color.parseColor("#42bc31");

	private int colorGreen = Color.parseColor("#52bc35");
	private int colorBlue = Color.parseColor("#0b16f9");
	private int colorRed = Color.parseColor("#ff1e00");//912416"

	private MediaPlayer music;

	private Timer messageAnimationTimer = new Timer();

	private ProgressBar pbHealthPlayer;
	private ProgressBar pbHealthEnemy;
	private ProgressBar pbEnergyPlayer;
	private ProgressBar pbEnergyEnemy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		ApplicationClass.getInstance().updateActiveContext(this);
		setContentView(R.layout.activity_battle);

		//Get parameters;
		int enemyShipTypeOrdinal = getIntent().getIntExtra("enemyShipTypeOrdinal", 0);
		level = getIntent().getIntExtra("level", 1);
		isBossBattle = getIntent().getBooleanExtra("isBossBattle", false);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		ApplicationClass.getInstance().addShipListener(this);

		this.energyPlayer = (int) appClass.getShip().getGeneratorRoom().getEffectiveEfficency();

		//UI
		updateHeader(ApplicationClass.getInstance().getShip());

		initWeaponButtonsListener();

		updateWeaponButtons(ApplicationClass.getInstance().getShip());


		EShipType enemyShipType = EShipType.values()[enemyShipTypeOrdinal];
		initEnemyShip(enemyShipType);


		updateTitle();
		addMessage("You are engaging an enemy ship: " + enemyShipType.name() + " (" + enemyShip.getHealth() + "HP)\n");


		initConfirmButton();

		initPlayerShipRooms();


		roundNr++;
		addMessage("[Round " + roundNr + "]: Make your moves!");

		startAnimationTimerTask(this);


		initProgressBars();


	}

	private void initProgressBars() {
		//Init Health ProgressBars
		AbsShip playerShip = ApplicationClass.getInstance().getShip();
		pbHealthPlayer = (ProgressBar) findViewById(R.id.pbBattleHealthPlayer);
		pbHealthPlayer.setProgress(playerShip.getHealth() * 100);
		pbHealthPlayer.setMax(playerShip.getMaxHealth() * 100);
		pbHealthPlayer.setProgressTintList(ColorStateList.valueOf(colorGreen));

		pbHealthEnemy = (ProgressBar) findViewById(R.id.pbBattleHealthEnemy);
		pbHealthEnemy.setProgress(enemyShip.getHealth() * 100);
		pbHealthEnemy.setMax(enemyShip.getMaxHealth() * 100);
		pbHealthEnemy.setProgressTintList(ColorStateList.valueOf(colorGreen));

		//Init Energy ProgressBars
		pbEnergyPlayer = (ProgressBar) findViewById(R.id.pbBattleEnergyPlayer);
		pbEnergyPlayer.setProgress(energyPlayer * 100);
		pbEnergyPlayer.setMax(playerShip.getMaxEnergyCost() * 100);
		pbEnergyPlayer.setProgressTintList(ColorStateList.valueOf(colorBlue));

		pbEnergyEnemy = (ProgressBar) findViewById(R.id.pbBattleEnergyEnemy);
		pbEnergyEnemy.setProgress(energyEnemy * 100);

		pbEnergyEnemy.setProgressTintList(ColorStateList.valueOf(colorBlue));

	}

	private void animateProgresssBar(ProgressBar pb, int value) {

		ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", value);
		animation.setDuration(500);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.start();
	}


	private void initPlayerShipRooms() {

		//Init room Widgets
		ArrayList<AbsRoom> rooms = ApplicationClass.getInstance().getShip().getRooms();

		LinearLayout parent = (LinearLayout) findViewById(R.id.llBattlePlayer);
		for (int i = 0; i < rooms.size(); i++) {
			final AbsRoom room = rooms.get(i);
			final RoomView roomView = new RoomView(this, appClass.getShip(), i);
			parent.addView(roomView);
			roomViewsPlayer.add(roomView);
			mapRooms.put(room, roomView);

			roomView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					UseCrystalEvent event = new UseCrystalEvent(room, level);
					event.execute(BattleActivity.this);
				}
			});
		}
	}


	private void initWeaponButtonsListener() {
		((Button) findViewById(R.id.bBattleWeapon1)).setAllCaps(false);
		((Button) findViewById(R.id.bBattleWeapon2)).setAllCaps(false);
		((Button) findViewById(R.id.bBattleWeapon3)).setAllCaps(false);
		((Button) findViewById(R.id.bBattleWeapon4)).setAllCaps(false);
	}

	private void addWeaponSelectionListener(View view, final AbsWeapon weapon) {
		view.setOnClickListener(new View.OnClickListener() {
			@Override
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

	private void nextRound() {

		//Refresh rooms
		for (AbsRoom room : appClass.getShip().getRooms()) {
			room.nextRound();
		}

		for (AbsRoom room : enemyShip.getRooms()) {
			room.nextRound();
		}

		//HANDLE BATTLE
		//Fire weapons for player AT the enemy

		for (Map.Entry<AbsWeapon, AbsRoom> entry : mapWeaponRooms.entrySet()) {
			AbsWeapon weapon = entry.getKey();

			boolean hitsAll = weapon.attacksAll();
			ArrayList<AbsRoom> targets = new ArrayList<>();

			if (hitsAll) {
				targets.addAll(enemyShip.getRooms());
			} else {
				targets.add(entry.getValue());
			}

			int damage = 0;
			int hitCnt = 0;
			for (AbsRoom room : targets) {
				AttackResult attackResult = room.attackWithWeapon(appClass.getShip(), enemyShip, weapon);
				if (attackResult.isHit()) {
					damage = attackResult.getDamage();
					playDamageAnimation(mapRooms.get(room));
				}


				if (hitsAll && attackResult.isHit()) {
					hitCnt++;
				}
				if (!hitsAll) {
					if (attackResult.isShielded()) {
						addActionMessage(new MessageData(1, colorGreen, "Shield"));
						addMessage("Enemy was shielded (" + weapon.getName() + ")");
					} else if (attackResult.isHit()) {
						addActionMessage(new MessageData(1, colorGreen, String.valueOf(damage)));
						addMessage("Enemy took " + damage + " damage (" + weapon.getName() + ")");
					} else {
						addActionMessage(new MessageData(1, colorGreen, "Miss"));
						addMessage("You missed (" + weapon.getName() + ")");
					}
				}

			}

			if (hitsAll) {
				if (hitCnt > 0) {

					String actionMessage = hitCnt + "x" + damage;
					String message = "Enemy took " + actionMessage + " damage (" + weapon.getName() + ")";
					addActionMessage(new MessageData(2, colorGreen, actionMessage));
					addMessage(message);
				}

				for (int i = hitCnt; i < 4; i++) {
					String message = "Enemy evaded (" + weapon.getName() + ")";
					addActionMessage(new MessageData(1, colorGreen, "Miss"));
					addMessage(message);
				}
			}

			//	if (hitsAll){
			//		String message = hitCnt + "x" + damage;
			//		addActionMessage(new MessageData(2, colorRed, String.valueOf(damage)));
			//		addMessage(message);
			//	}else{
			//	for (int i=hitCnt; i<4; i++){
			//		addActionMessage(new MessageData(1, colorRed, "Miss"));
			//	}
			//	}

			//TODO: What is this for?
			entry.getKey().setTarget(null);

		}

		//Attack the Player => Fire weapons for Enemy AT the player
		if (enemyShip.getHealth() > 0) {
			HashMap<AbsWeapon, AbsRoom> map = enemyShip.generateNewAttackMap(appClass.getShip(), energyEnemy);
			for (Map.Entry<AbsWeapon, AbsRoom> entry : map.entrySet()) {
				AbsWeapon weapon = entry.getKey();
				AbsRoom target = entry.getValue();

				if (energyEnemy - weapon.getEnergyCost() >= 0) {
					boolean hitsAll = weapon.attacksAll();
					ArrayList<AbsRoom> targets = new ArrayList<>();
					if (hitsAll) {
						targets.addAll(appClass.getShip().getRooms());
					} else {
						targets.add(target);
					}

					energyEnemy -= weapon.getEnergyCost();

					int hitCnt = 0;
					int damage = 0;
					for (AbsRoom room : targets) {
						AttackResult attackResult = room.attackWithWeapon(enemyShip, appClass.getShip(), weapon);
						if (attackResult.isHit()) {
							damage = attackResult.getDamage();
							if (hitsAll) {
								hitCnt++;
							}

							playDamageAnimation(mapRooms.get(room));

							if (!hitsAll) {
								String message = "You (" + room.getName() + ") took " + damage + " damage (" + weapon.getName() + ")";
								addActionMessage(new MessageData(2, colorRed, String.valueOf(damage)));
								addMessage(message);
							}
						} else {
							if (!hitsAll) {
								String message = "You evaded (" + weapon.getName() + ")";
								addActionMessage(new MessageData(1, colorGreen, "Evasion"));
								addMessage(message);
							}

						}
					}
					if (hitsAll) {
						if (hitCnt > 0) {
							String actionMessage = hitCnt + "x" + damage;
							String message = "You took " + actionMessage + " damage (" + weapon.getName() + ")";
							addActionMessage(new MessageData(2, colorRed, actionMessage));
							addMessage(message);
						}


						for (int i = hitCnt; i < 4; i++) {
							String message = "You evaded (" + weapon.getName() + ")";
							addActionMessage(new MessageData(1, colorGreen, "Evasion"));
							addMessage(message);
						}
					}


				}
			}
		}

		//Update Energy Pool
		energyPlayer += appClass.getShip().getGeneratorRoom().getEffectiveEfficency();
		energyEnemy += enemyShip.getGeneratorRoom().getEffectiveEfficency();


		//Unselect weapons
		//AbsWeapon lastSelectedWeapon = selectedWeapon;

		selectWeapon(null);

		//resetWeaponCommandLists();
		reloadWeapons();

		updateWeaponButtons(appClass.getShip());


		//Check if enemy is dead
		if (enemyShip.getHealth() <= 0) {
			refreshUI();
			battleOver();
			return;
		}

		//Regenerate Rooms
		for (AbsRoom room : ApplicationClass.getInstance().getShip().getRooms()) {
			room.regenerate();
		}
		for (AbsRoom room : enemyShip.getRooms()) {
			room.regenerate();
		}


		//Update message log
		roundNr++;
		addMessage("\n[Round " + roundNr + "]: Make your moves!");



		//update UI
		refreshUI();


	}


	private void playShieldActivatedAnimation(RoomView room) {
		playBlinkAnimation(room, getResources().getColor(R.color.transparent), getResources().getColor(R.color.colorEnergy));
	}

	private void playRepairAnimation(RoomView room) {
		playBlinkAnimation(room, getResources().getColor(R.color.transparent), getResources().getColor(R.color.colorHeal));
	}

	private void playDamageAnimation(RoomView room) {
		playBlinkAnimation(room, getResources().getColor(R.color.transparent), getResources().getColor(R.color.colorRed));
	}

	private void playTargetedAnimation(RoomView room) {
		playBlinkAnimation(room, getResources().getColor(R.color.transparent), getResources().getColor(R.color.colorOrange));
	}

	private void playBlinkAnimation(final RoomView roomView, int colorFrom, int colorTo) {
		ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
		colorAnimation.setDuration(500);
		colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				roomView.getBackground().setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.DARKEN);
			}

		});

		colorAnimation.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				roomView.getBackground().setColorFilter(null);
				roomView.update();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				roomView.getBackground().setColorFilter(null);
				roomView.update();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		colorAnimation.start();
	}

	private void reloadWeapons() {
		ArrayList<AbsWeapon> weaponsForRemoval = new ArrayList<>();

		for (Iterator<Map.Entry<AbsWeapon, AbsRoom>> it = mapWeaponRooms.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<AbsWeapon, AbsRoom> entry = it.next();
			AbsWeapon weapon = entry.getKey();
			AbsRoom room = entry.getValue();

			if (weapon.isOneTimeWeapon()) {
				weaponsForRemoval.add(weapon);
			} else if (room != null) {
				if (!selectTarget(weapon, room, false, false)) {
					weaponsForRemoval.add(weapon);
				}
			}
		}

		for (AbsWeapon weapon : weaponsForRemoval) {
			mapWeaponRooms.remove(weapon);
		}

	}


	private void addActionMessage(MessageData message) {
		messageDataQueue.add(message);
	}

	private void gameCleanup() {
		for (AbsRoom room : ApplicationClass.getInstance().getShip().getRooms()) {
			room.regenerate(true);
		}

		//Unload weapons
		for (AbsWeapon weapon : ApplicationClass.getInstance().getShip().getWeapons()) {
			weapon.setTarget(null);
		}
	}

	private void battleOver() {
		addMessage("Enemy has been destroyed !!!");
		final Loot loot = enemyShip.getLoot();

		String message = "Loot Report\n";
		message += "Fuel: " + loot.getFuel() + "\n";
		message += "Money: " + loot.getMoney() + "\n";

		if (!enemyShip.getLoot().getItems().isEmpty()) {
			message += "\nItems\n";

			for (IInventoryItem item : enemyShip.getLoot().getItems()) {
				message += "-" + item.getName() + "\n";
				appClass.getShip().getInventory().add(item);
			}
		}

		gameCleanup();


		ApplicationClass.getInstance().updateScore(level * 100 + loot.getFuel() + loot.getMoney());


		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		TextView textView = new TextView(this);
		textView.setText(message);

		builder.setCustomTitle(textView);

		CharSequence[] charSequences = new CharSequence[1];
		charSequences[0] = "Okay";


		builder.setItems(charSequences, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				appClass.updateFuel(loot.getFuel());
				appClass.updateShipMoney(loot.getMoney());

				if (isBossBattle) {
					GoToGalaxyEvent.move(MainActivity.getInstance(), level + 1);
					BattleActivity.super.onBackPressed();
				} else {
					BattleActivity.super.onBackPressed();
				}

			}
		}).show();


	}


	private void refreshUI() {
		updateTitle();

		for (RoomView roomView : roomViewsEnemy) {
			roomView.update();
		}

		for (RoomView roomView : roomViewsPlayer) {
			roomView.update();
		}

		PlayerShip ship = ApplicationClass.getInstance().getShip();

		//Refresh health and energy
		animateProgresssBar(pbHealthPlayer, ship.getHealth() * 100);
		pbEnergyPlayer.setMax(ApplicationClass.getInstance().getShip().getMaxEnergyCost() * 100);
		animateProgresssBar(pbEnergyPlayer, energyPlayer * 100);


		animateProgresssBar(pbHealthEnemy, enemyShip.getHealth() * 100);
		pbEnergyEnemy.setMax(enemyShip.getMaxEnergyCost() * 100);
		animateProgresssBar(pbEnergyEnemy, energyEnemy * 100);

		//Update weapon button state
		for (AbsWeapon weapon : ship.getWeapons()) {
			if (weapon != null && mapWeaponButtons.get(weapon) != null){
				if (mapWeaponRooms.containsKey(weapon)){
					mapWeaponButtons.get(weapon).setEnabled(true);
				}else{
					mapWeaponButtons.get(weapon).setEnabled(canAffordWeapon(weapon, energyPlayer));
				}
			}
		}

	}


	private void updateTitle() {
		int healthEnemy = enemyShip.getHealth();
		String enemyName = enemyShip.getName();

		String text = "Engaged Enemy: " + enemyName + " (" + healthEnemy + "HP, " + energyEnemy + " Energy)";
		TextView textView = (TextView) findViewById(R.id.tvBattleTitle);
		textView.setText(text);
	}

	private void addMessage(String message) {
		//	if (true) {
		//		return;
		//	}
		TextView textView = (TextView) findViewById(R.id.tvBattleLog);
		textView.setText(textView.getText() + message + "\n");

		final ScrollView sc = (ScrollView) findViewById(R.id.scBattle);
		sc.post(new Runnable() {
			public void run() {
				sc.fullScroll(View.FOCUS_DOWN);
			}
		});

	}

	ArrayList<RoomView> roomViewsEnemy = new ArrayList<>();
	ArrayList<RoomView> roomViewsPlayer = new ArrayList<>();

	private void initEnemyShip(EShipType enemyShipType) {
		switch (enemyShipType) {

			case Enemy_A:
				enemyShip = new EnemyShipA("Enemy A", level);
				break;
			case Enemy_B:
				enemyShip = new EnemyShipB("Enemy B", level);
				break;
			case Enemy_Boss_A:
				enemyShip = new EnemyBossShipA("Boss A", level);
				break;
			case Player_A:
				throw new RuntimeException("Ship cant be Player Ship");
			default:
				throw new RuntimeException("Unsupported Ship type");
		}

		//Init room Widgets
		ArrayList<AbsRoom> rooms = enemyShip.getRooms();

		LinearLayout parent = (LinearLayout) findViewById(R.id.llBattleEnemy);
		for (int i = 0; i < rooms.size(); i++) {
			final AbsRoom room = rooms.get(i);
			final RoomView roomView = new RoomView(this, enemyShip, i);
			parent.addView(roomView);
			roomViewsEnemy.add(roomView);

			mapRooms.put(room, roomView);


			roomView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (selectedWeapon == null) {
						return;
					}
					if (selectedWeapon.getTarget() != null && selectedWeapon.getTarget().equals(room)) {
						return;
					}
					selectTarget(selectedWeapon, room, true, true);
				}
			});
		}

		energyEnemy = (int) enemyShip.getGeneratorRoom().getEffectiveEfficency();
	}

	private boolean canAffordWeapon(AbsWeapon weapon, int energyCosts) {
		return energyCosts - weapon.getEnergyCost() >= 0;
	}

	private boolean selectTarget(AbsWeapon weapon, AbsRoom target, boolean showDialogs, boolean manualSelection) {


		if (!weapon.hasTarget() && target != null && !canAffordWeapon(weapon, energyPlayer)) {
			if (showDialogs) {
				Utility.getInstance().showTextDialog(this, "Not have enough energy!");
			}
			return false;
		}

		if (target == null){ //Refund
			if (weapon.hasTarget()){
				energyPlayer += weapon.getEnergyCost();
			}
			mapWeaponRooms.remove(weapon);
		}else{ //Target a Room
			mapWeaponRooms.put(weapon, target);
			if (!weapon.hasTarget()){ //Drain energy if no room was selected yet
				energyPlayer += -weapon.getEnergyCost();
			}

		}

		weapon.setTarget(target);

		mapWeaponButtons.get(weapon).getBackground().setColorFilter(target == null ? colorSelected : colorWeaponLocked, PorterDuff.Mode.DARKEN);
		mapWeaponButtons.get(weapon).setText(weapon.getBattleLabel(ApplicationClass.getInstance().getShip().getWeaponRoom().getEffectiveEfficency()));

		if (manualSelection && target != null) {
			if (weapon.attacksAll()) {
				for (AbsRoom room : target.getShip().getRooms()) {
					playTargetedAnimation(mapRooms.get(room));
				}
			} else {
				playTargetedAnimation(mapRooms.get(target));
			}
		}


		refreshUI();

		return true;
	}


	private void selectWeapon(AbsWeapon weapon) {


		for (Map.Entry<AbsWeapon, Button> entry : mapWeaponButtons.entrySet()) {
			if (!entry.getKey().hasTarget()) {
				entry.getValue().getBackground().setColorFilter(null);
			}

		}

		if (weapon != null) {
			mapWeaponButtons.get(weapon).getBackground().setColorFilter(colorSelected, PorterDuff.Mode.DARKEN);
		}

		if (weapon != null){
			selectedWeapon = weapon;
		}

		//Reset target if the weapong ets reselected after already having a target selected previously
		if (weapon != null && weapon.getTarget() != null) {
			selectTarget(weapon, null, true, false);
		}

		refreshUI();
	}

	private void updateWeaponButtons(PlayerShip playerShip) {

		ArrayList<AbsWeapon> weapons = playerShip.getEquippedWeapons();

		findViewById(R.id.bBattleWeapon1).setVisibility(weapons.size() > 0 ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.bBattleWeapon2).setVisibility(weapons.size() > 1 ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.bBattleWeapon3).setVisibility(weapons.size() > 2 ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.bBattleWeapon4).setVisibility(weapons.size() > 3 ? View.VISIBLE : View.INVISIBLE);

		mapWeaponButtons.clear();

		if (weapons.size() > 0) {
			AbsWeapon weapon = playerShip.getEquippedWeapons().get(0);
			((Button) findViewById(R.id.bBattleWeapon1)).setText(weapon.getBattleLabel(playerShip.getWeaponRoom().getEffectiveEfficency()));
			mapWeaponButtons.put(weapon, ((Button) findViewById(R.id.bBattleWeapon1)));
			addWeaponSelectionListener(findViewById(R.id.bBattleWeapon1), weapon);
		}

		if (weapons.size() > 1) {
			AbsWeapon weapon = playerShip.getEquippedWeapons().get(1);
			((Button) findViewById(R.id.bBattleWeapon2)).setText(weapon.getBattleLabel(playerShip.getWeaponRoom().getEffectiveEfficency()));
			mapWeaponButtons.put(weapon, ((Button) findViewById(R.id.bBattleWeapon2)));
			addWeaponSelectionListener(findViewById(R.id.bBattleWeapon2), weapon);
		}

		if (weapons.size() > 2) {
			AbsWeapon weapon = playerShip.getEquippedWeapons().get(2);
			((Button) findViewById(R.id.bBattleWeapon3)).setText(weapon.getBattleLabel(playerShip.getWeaponRoom().getEffectiveEfficency()));
			mapWeaponButtons.put(weapon, ((Button) findViewById(R.id.bBattleWeapon3)));
			addWeaponSelectionListener(findViewById(R.id.bBattleWeapon3), weapon);
		}

		if (weapons.size() > 3) {
			AbsWeapon weapon = playerShip.getEquippedWeapons().get(3);
			((Button) findViewById(R.id.bBattleWeapon4)).setText(weapon.getBattleLabel(playerShip.getWeaponRoom().getEffectiveEfficency()));
			mapWeaponButtons.put(weapon, ((Button) findViewById(R.id.bBattleWeapon4)));
			addWeaponSelectionListener(findViewById(R.id.bBattleWeapon4), weapon);
		}
	}


	@Override
	public void shipUpdated(PlayerShip ship) {
		updateHeader(ship);
		updateWeaponButtons(ship);
		refreshUI();
	}

	@Override
	public void itemUsed(AbsShip ship, IConsumableItem item) {
		switch (item.getConsumableItemType()) {
			case BlueCrystal:
				for (AbsRoom room : ship.getRooms()) {
					playShieldActivatedAnimation(mapRooms.get(room));
				}
				break;
			case RedCrystal:
				for (AbsRoom room : ship.getRooms()) {
					playRepairAnimation(mapRooms.get(room));
				}
				break;
		}

	}

	private void updateHeader(PlayerShip ship) {
		((TextView) findViewById(R.id.tvHeaderCash)).setText("Cash: " + ship.getMoney());
		((TextView) findViewById(R.id.tvHeaderFuel)).setText("Fuel: " + ship.getFuel());
		((TextView) findViewById(R.id.tvHeaderHealth)).setText("Health: " + ship.getHealth());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		messageAnimationTimer.cancel();
		ApplicationClass.getInstance().removeShipListener(this);

		if (ApplicationClass.playMusic) {
			music.stop();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		refreshUI();
		ApplicationClass.getInstance().updateActiveContext(this);
	}

	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_POSITIVE) {
					Random random = new Random();

					if (random.nextBoolean()) {
						Utility.getInstance().showTextDialog(BattleActivity.this, "You couldn't escape!");
						nextRound();
					} else {
						gameCleanup();
						BattleActivity.super.onBackPressed();
					}
				}
			}
		};

		String text = "Do you want to get away from the battle?";

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setMessage(text).setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	public static Context getInstance() {
		return instance;
	}


	@Override
	public void onStart() {
		super.onStart();

		if (ApplicationClass.playMusic) {
			music = MediaPlayer.create(this, R.raw.trainer_battle);
			music.start();
		}

		Games.setViewForPopups(ApplicationClass.getInstance().getGoogleApiClient(), findViewById(R.id.gps_popup));

	}


	private void startAnimationTimerTask(final Context context) {
		messageAnimationTimer = new Timer();
		messageAnimationTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (messageDataQueue.isEmpty()) {
							return;
						}
						MessageData message = messageDataQueue.get(0);
						messageDataQueue.remove(message);
						//Left
						if (message.getPosition() == 1) {

							//Right
						} else {

						}

						final FrameLayout ll = (FrameLayout) findViewById(R.id.llActionContainerTable);

						final TextView tv = new TextView(context);

						tv.setText(message.getText());
						tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
						tv.setTextColor(message.getColor());
						tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.font_size_battle_action_view));
						ll.addView(tv);

						Animation animation = AnimationUtils.loadAnimation(context, R.anim.move01);

						animation.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
								Log.i(TAG, "Text animation started");
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
				});

			}
		}, 0, 200);
	}
}
