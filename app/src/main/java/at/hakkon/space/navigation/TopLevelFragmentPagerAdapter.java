package at.hakkon.space.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import at.hakkon.space.fragments.GalaxyFragment;
import at.hakkon.space.fragments.SocialFragment;
import at.hakkon.space.fragments.ShipFragment;

public class TopLevelFragmentPagerAdapter extends FragmentStatePagerAdapter {
	int tabCount;

	private ShipFragment tab1;
	private GalaxyFragment tab2;
	private SocialFragment tab3;

	private static TopLevelFragmentPagerAdapter instance;


	public static TopLevelFragmentPagerAdapter getInstance() {
		return instance;
	}

	public TopLevelFragmentPagerAdapter(FragmentManager fm, int tabCount) {
		super(fm);
		this.tabCount = tabCount;
		instance = this;
	}

	@Override
	public Fragment getItem(int position) {

		switch (position) {
			case 0:
				if (tab1 == null) {
					tab1 = new ShipFragment();
				}

				return tab1;
			case 1:
				if (tab2 == null) {
					tab2 = new GalaxyFragment();
				}

				return tab2;
			case 2:
				if (tab3 == null) {
					tab3 = new SocialFragment();
				}
				return tab3;
			default:
				return null;
		}
	}


	public ShipFragment getShipFragment() {
		return tab1;
	}

	public SocialFragment getPlanetFragment() {
		return tab3;
	}

	public GalaxyFragment getMapFragment() {
		return tab2;
	}


	@Override
	public int getCount() {
		return tabCount;
	}

	public void dispose() {

	}
}