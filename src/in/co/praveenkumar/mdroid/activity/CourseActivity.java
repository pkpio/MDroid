package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.fragment.CourseFragment;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.view.SlidingTabLayout;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class CourseActivity extends BaseNavigationActivity {

	private ViewPager viewPager;
	private static final String[] TABS = { "MY COURSES", "FAVOURITE COURSES" };
	private StartAppAd startAppAd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * StartAppSDK and StartAppAd Init. SDK init is only required in the
		 * MainActivity - entry activities to the app.
		 */
		StartAppSDK.init(this, Param.STARTAPP_DEV_ID, Param.STARTAPP_APP_ID,
				false);
		startAppAd = new StartAppAd(this);

		setContentView(R.layout.activity_course);
		setUpDrawer();

		// Send a tracker
		((ApplicationClass) getApplication())
				.sendScreen(Param.GA_SCREEN_COURSE);

		getSupportActionBar().setTitle("Moodle Home");
		getSupportActionBar().setIcon(R.drawable.ic_actionbar_icon);

		FragmentPagerAdapter mAdapter = new CourseTabsAdapter(
				getSupportFragmentManager());

		viewPager = (ViewPager) findViewById(R.id.course_pager);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(mAdapter);

		SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(viewPager);
	}

	class CourseTabsAdapter extends FragmentPagerAdapter {
		public CourseTabsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			/*
			 * We use bundle to pass course listing type because, by using other
			 * methods we will lose the listing type information in the fragment
			 * on onResume (this calls empty constructor). For the same reason
			 * interface may not work. Bundles are passed again on onResume
			 */
			switch (position) {
			case 0:
				CourseFragment userCourses = new CourseFragment();

				// Set the listing type to only user courses in bundle.
				Bundle bundle = new Bundle();
				bundle.putInt("coursesType", CourseFragment.TYPE_USER_COURSES);
				userCourses.setArguments(bundle);

				return userCourses;
			case 1:
				CourseFragment favCourses = new CourseFragment();

				// Set the listing type to only user courses in bundle.
				Bundle bundle1 = new Bundle();
				bundle1.putInt("coursesType", CourseFragment.TYPE_FAV_COURSES);
				favCourses.setArguments(bundle1);

				return favCourses;
			}
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TABS[position];
		}

		@Override
		public int getCount() {
			return TABS.length;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		startAppAd.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		startAppAd.onPause();
	}

	@Override
	public void onBackPressed() {
		if (!isProUser() && !Param.hideAdsForSession && Param.STARTAPP_EXIT_ADS) {
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(this);
			long now = System.currentTimeMillis();
			long last = settings.getLong("startapp_last_served", now
					- Param.STARTAPP_INTERSTITIAL_MAX_FREQ);

			if (now - last >= Param.STARTAPP_INTERSTITIAL_MAX_FREQ)
				startAppAd.onBackPressed();
		}
		super.onBackPressed();
	}

}
