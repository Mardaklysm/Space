package at.hakkon.space.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.games.Games;

import at.hakkon.space.R;
import at.hakkon.space.application.ApplicationClass;
import at.hakkon.space.datamodel.ship.PlayerShip;
import at.hakkon.space.listener.IShipListener;
import at.hakkon.space.navigation.PagingViewPager;
import at.hakkon.space.navigation.TopLevelFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements IShipListener {

	private ApplicationClass appClass = ApplicationClass.getInstance();

	private static MainActivity instance;


	private TopLevelFragmentPagerAdapter adapter;
	private PagingViewPager viewPager;

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_ship:
					viewPager.setCurrentItem(0, false);
					return true;
				case R.id.navigation_map:
					viewPager.setCurrentItem(1, false);
					adapter.getMapFragment().setVisible();
					return true;
				case R.id.navigation_planet:
					viewPager.setCurrentItem(2, false);
					return true;
			}
			return false;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApplicationClass.getInstance().updateActiveContext(this);
		instance = this;
		setContentView(R.layout.activity_main);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		viewPager = (PagingViewPager) findViewById(R.id.pager);

		adapter = new TopLevelFragmentPagerAdapter(getSupportFragmentManager(), 3);

		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(10);
		viewPager.setPagingEnabled(false);

		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


		appClass.addShipListener(this);


		appClass.initialize();

	}

	MediaPlayer music;

	@Override
	public void onResume() {
		super.onResume();

		ApplicationClass.getInstance().updateActiveContext(this);

		Games.setViewForPopups(ApplicationClass.getInstance().getGoogleApiClient(), findViewById(R.id.gps_popup));

		if (ApplicationClass.playMusic) {
			music = MediaPlayer.create(this, R.raw.main_theme);
			music.setLooping(true);
			music.start();
		}

	}

	public static MainActivity getInstance() {
		return instance;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		ApplicationClass.getInstance().removeShipListener(this);
		ApplicationClass.getInstance().removeShipListener(adapter.getPlanetFragment());
		ApplicationClass.getInstance().removeShipListener(adapter.getShipFragment());


		ApplicationClass.getInstance().removeGalaxyListener(adapter.getMapFragment());
	}


	@Override
	public void shipUpdated(PlayerShip ship) {
		updateHeader(ship);
	}

	private void updateHeader(PlayerShip ship) {
		((TextView) findViewById(R.id.tvHeaderCash)).setText("Cash: " + ship.getMoney());
		((TextView) findViewById(R.id.tvHeaderFuel)).setText("Fuel: " + ship.getFuel());
		((TextView) findViewById(R.id.tvHeaderHealth)).setText("Health: " + ship.getHealth());
	}

	@Override
	public void onStop() {
		super.onStop();
		if (ApplicationClass.playMusic) {
			music.stop();
		}

	}
}
