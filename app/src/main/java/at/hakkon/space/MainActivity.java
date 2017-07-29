package at.hakkon.space;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import at.hakkon.space.navigation.PagingViewPager;
import at.hakkon.space.navigation.TopLevelFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

	private ApplicationClass appClass = ApplicationClass.getInstance();

	private TextView mTextMessage;

	private TopLevelFragmentPagerAdapter adapter;
	private PagingViewPager viewPager;

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
			= new BottomNavigationView.OnNavigationItemSelectedListener() {

		@Override
		public boolean onNavigationItemSelected(@NonNull MenuItem item) {
			switch (item.getItemId()) {
				case R.id.navigation_home:
					viewPager.setCurrentItem(0, false);
					return true;
				case R.id.navigation_dashboard:
					viewPager.setCurrentItem(1, false);
					return true;
				case R.id.navigation_notifications:
					viewPager.setCurrentItem(2, false);
					return true;
			}
			return false;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		viewPager = (PagingViewPager) findViewById(R.id.pager);

		adapter = new TopLevelFragmentPagerAdapter(getSupportFragmentManager(), 3);

		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(10);
		viewPager.setPagingEnabled(false);

		BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		appClass.initialize();
	}

}
