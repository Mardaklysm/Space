package at.hakkon.space.application;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import at.hakkon.space.R;
import at.hakkon.space.achievement.Achievements;
import at.hakkon.space.activity.MainActivity;
import at.hakkon.space.datamodel.EFaction;
import at.hakkon.space.datamodel.EGameOverReason;
import at.hakkon.space.datamodel.SaveFile;
import at.hakkon.space.datamodel.faction.AbsFaction;
import at.hakkon.space.datamodel.faction.BlueFaction;
import at.hakkon.space.datamodel.faction.GreenFaction;
import at.hakkon.space.datamodel.faction.RedFaction;
import at.hakkon.space.datamodel.galaxy.AbsPlanet;
import at.hakkon.space.datamodel.galaxy.Galaxy;
import at.hakkon.space.datamodel.inventory.IConsumableItem;
import at.hakkon.space.datamodel.inventory.IInventoryItem;
import at.hakkon.space.datamodel.person.Person;
import at.hakkon.space.datamodel.ship.AbsShip;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.event.management.RestartGameEvent;
import at.hakkon.space.listener.IGalaxyListener;
import at.hakkon.space.listener.IPlanetVisitListener;
import at.hakkon.space.listener.IShipListener;
import at.hakkon.space.signin.GoogleSignInActivity;
import at.hakkon.space.utility.Utility;

public class ApplicationClass extends android.app.Application {

	public static final String LEADERBOARD_ID = "CgkI5LP25OEdEAIQEA";
	private static ApplicationClass instance;

	private ArrayList<IShipListener> shipListeners = new ArrayList<>();
	private ArrayList<IGalaxyListener> galaxyListeners = new ArrayList<>();
	private ArrayList<IPlanetVisitListener> planetVisitListeners = new ArrayList<>();
	private ArrayList<IMetaDataListener> metaDataListeners = new ArrayList<>();

	//Save START
	private PlayerShip ship;
	private Galaxy galaxy;
	private int crystalSeriesPosition;
	private HashMap<EFaction, AbsFaction> factionMap = new HashMap<>();
	//Save END

	public static boolean playMusic = true;



	public static ApplicationClass getInstance() {
		if (instance == null) {
			instance = new ApplicationClass();
		}
		return instance;
	}


	private boolean isInitialized = false;

	public void initialize() {
		if (isInitialized) {
			return;
		}

		if (!loadGame()) {
			galaxy = new Galaxy("Starting Galaxy", 1);

			ship = new PlayerShip("Weinreise", 1);
			ship.setStartPlanet(galaxy.getFirstPlanet());
			ship.setCurrentPlanet(galaxy.getFirstPlanet());
			crystalSeriesPosition = 1;
		}


		factionMap.put(EFaction.Green, new GreenFaction(0));
		factionMap.put(EFaction.Red, new RedFaction(0));
		factionMap.put(EFaction.Blue, new BlueFaction(0));

		isInitialized = true;

		notifyShipListeners(ship);
		notifyGalaxyListeners(galaxy);
		notifyPlanetVisitListener(ship.getCurrentPlanet());

		score = 0;
	}

	public void restartGame() {
		ApplicationClass.getInstance().deleteSaveFile();

		Games.Leaderboards.submitScore(ApplicationClass.getInstance().getGoogleApiClient(), ApplicationClass.LEADERBOARD_ID, score);

		isInitialized = false;
		score = 0;

		Intent intent = new Intent(activeContext, MainActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		activeContext.startActivity(intent);

	}

	public PlayerShip getShip() {
		return ship;
	}


	public boolean moveToPlanet(AbsPlanet planet) {
		boolean moved = getShip().moveToPlanet(planet);
		planet.revealName();
		if (moved) {
			notifyPlanetVisitListener(planet);
			notifyShipListeners(getShip());
		}


		if (planet.getEvent() != null) {
			planet.getEvent().execute(getContext());
			Log.i(TAG, "Executing event for for planet: " + planet.getName());
		} else {
			Log.i(TAG, "No event found for planet: " + planet.getName());
		}
		saveGame();
		return moved;
	}


	public Context getContext() {
		return MainActivity.getInstance();
	}

	public void addShipListener(IShipListener listener) {
		if (!shipListeners.contains(listener)) {
			shipListeners.add(listener);
		}
	}

	public void addGalaxyListener(IGalaxyListener listener) {
		if (!galaxyListeners.contains(listener)) {
			galaxyListeners.add(listener);
		}
	}

	public void addPlanetVisitorListener(IPlanetVisitListener listener) {
		if (!planetVisitListeners.contains(listener)) {
			planetVisitListeners.add(listener);
		}
	}

	public void addMetaDataListener(IMetaDataListener listener) {
		if (!metaDataListeners.contains(listener)) {
			metaDataListeners.add(listener);
		}
	}

	public void removeMetaDataListener(IMetaDataListener listener) {
		metaDataListeners.remove(listener);
	}


	public void removeGalaxyListener(IGalaxyListener listener) {
		galaxyListeners.remove(listener);
	}

	public void removeShipListener(IShipListener listener) {
		shipListeners.remove(listener);
	}

	public void removePlanetVisitListener(IPlanetVisitListener listener) {
		planetVisitListeners.remove(listener);
	}

	private void notifyShipListeners(PlayerShip ship) {
		for (IShipListener listener : shipListeners) {
			listener.shipUpdated(ship);
		}
	}

	private final static String TAG = "AppClass";

	private void notifyGalaxyListeners(Galaxy galaxy) {
		for (IGalaxyListener listener : galaxyListeners) {
			listener.galaxyUpdated(galaxy);
		}
	}

	private void notifyPlanetVisitListener(AbsPlanet planet) {
		for (IPlanetVisitListener listener : planetVisitListeners) {
			listener.planetVisit(planet);
		}
	}

	private void notifyMetaDataListener() {
		for (IMetaDataListener listener : metaDataListeners) {
			listener.scoreChanged(score);
		}
	}

	public Galaxy getGalaxy() {
		return galaxy;
	}

	public void visitPlanet(AbsPlanet absPlanet) {
		notifyPlanetVisitListener(absPlanet);

	}

	public void toast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT);
	}

	public void requestNotifyShipChangedEvent() {
		notifyShipListeners(getShip());
	}

	public void updateShipMoney(int value) {
		ship.updateMoney(value);
		notifyShipListeners(ship);
		if (value > 0) {
			Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_TREASURE_HUNTER_I, value);
			Games.Achievements.increment(ApplicationClass.getInstance().getGoogleApiClient(), Achievements.ID_TREASURE_HUNTER_II, value);
		}

	}

	public void updateShipHealth(int value) {
		ship.updateHealth(value);
		notifyShipListeners(ship);
	}

	public void addShipMember(Person person) {
		ship.addPerson(person);
		notifyShipListeners(ship);
	}

	public void gameOver(EGameOverReason gameOverReason) {
		Utility.getInstance().showTextDialog(activeContext, "Game Over: " + gameOverReason.name());
		RestartGameEvent event = new RestartGameEvent(1, gameOverReason);
		event.execute(activeContext);

	}


	public void moveToNewGalaxy() {
		int galaxyLevel = galaxy.getLevel() + 1;
		Galaxy newGalaxy = new Galaxy("Galaxy " + galaxyLevel, galaxyLevel);
		this.galaxy = newGalaxy;
		ship.setCurrentPlanet(galaxy.getFirstPlanet());
		ship.setStartPlanet(galaxy.getFirstPlanet());

		notifyGalaxyListeners(this.galaxy);

		saveGame();
	}

	public void updateFuel(int fuel) {
		ship.updateFuel(fuel);
		notifyShipListeners(ship);
	}

	private Context activeContext;

	public void updateActiveContext(Context context) {
		activeContext = context;
	}

	public Context getActiveContext() {
		return activeContext;
	}

	public void sellItem(IInventoryItem item) {
		updateShipMoney(item.getCashValue());
		ship.getInventory().remove(item);
		notifyShipListeners(ship);
	}

	private GoogleApiClient googleApiClient;

	public GoogleApiClient getGoogleApiClient() {
		if (googleApiClient == null || !googleApiClient.isConnected()){
			Intent intent = new Intent(activeContext, GoogleSignInActivity.class);
			Log.e(TAG,"Google CLIENT is null or not available (is null==" + (googleApiClient == null) + ") => Trying to reconnect by starting SignInActivity");

			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			activeContext.startActivity(intent);
		}
		return googleApiClient;
	}

	public void setGoogleClient(GoogleApiClient googleApiClient) {
		this.googleApiClient = googleApiClient;
	}

	private int score = 0;

	public int updateScore(int value) {
		score += value;
		Log.i(TAG, "Score is now: " + value);
		notifyMetaDataListener();
		return score;
	}

	public int getScore() {
		return score;
	}

	public static MediaPlayer music;

	public void startMainMusic(Context context) {

		if (music != null) {
			music.stop();
		}
		music = MediaPlayer.create(context, R.raw.main_theme);
		music.setLooping(true);
		music.start();
	}

	public void stopMainMusic() {
		music.stop();
	}


	private final String SAVE_FILE = "space_save_file";

	private void saveGame() {
		Log.i(TAG, "Saving Game!");
		SaveFile saveFile = new SaveFile(ship, galaxy, factionMap, crystalSeriesPosition);

		try {
			FileOutputStream fos = MainActivity.getInstance().openFileOutput(SAVE_FILE, Context.MODE_PRIVATE);

			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(saveFile);
			os.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void incCrystalSeriesPositon(){
		crystalSeriesPosition++;
	}

	/**
	 * @return True if a save game got found and loaded, else False
	 */
	private boolean loadGame() {
		Log.i(TAG, "Loading Save File!");
		FileInputStream fis = null;
		try {
			fis = MainActivity.getInstance().openFileInput(SAVE_FILE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(fis);

			SaveFile saveFile = (SaveFile) is.readObject();

			this.galaxy = saveFile.getGalaxy();
			this.ship = saveFile.getPlayerShip();
			this.factionMap = saveFile.getFactionMap();
			this.crystalSeriesPosition = saveFile.getCrystalSeriesPosition();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public void updateKarma(EFaction faction, int karma) {
		factionMap.get(faction).updateKarma(karma);
	}

	public boolean deleteSaveFile() {

		File file = new File(MainActivity.getInstance().getFilesDir() + "/" + SAVE_FILE);
		boolean deleted = file.delete();
		Log.i(TAG, "Deleted Save File: " + deleted);

		return deleted;
	}

	public AbsFaction getHighestKarmaFaction() {
		AbsFaction faction = null;
		for (Map.Entry<EFaction, AbsFaction> entry : factionMap.entrySet()) {
			if (faction == null || entry.getValue().getKarma() > faction.getKarma()) {
				faction = entry.getValue();
			}
		}
		return faction;
	}

	public AbsFaction getFaction(EFaction faction) {
		return factionMap.get(faction);
	}

	public int getCrystalSeriesPosition() {
		return crystalSeriesPosition;
	}

	public void itemConsumed(AbsShip ship, IConsumableItem item) {
		for (IShipListener listener: shipListeners){
			listener.itemUsed(ship, item);
		}
	}
}