package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.dialog.RateDialog;
import in.co.praveenkumar.mdroid.fragment.CourseFragment;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.view.SlidingTabLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class CourseActivity extends BaseNavigationActivity {
	final int DIALOG_FREQ = 4;
	private ViewPager viewPager;
	private static final String[] TABS = { "MY COURSES", "FAVOURITE COURSES" };
	RateDialog mRateDialog;
	SharedPreferences mSharedPrefs;
	SharedPreferences.Editor mSharedPrefseditor;
	int dialogCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		// Dialog related work
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mSharedPrefseditor = mSharedPrefs.edit();
		dialogCount = mSharedPrefs.getInt("dialogCount", 0);
		mSharedPrefseditor.putInt("dialogCount", dialogCount + 1);
		mSharedPrefseditor.commit();

		if ((dialogCount + 2) % DIALOG_FREQ == 1
				&& !mSharedPrefs.getBoolean("isRated", false)) {
			mRateDialog = new RateDialog(this, new DialogActionListener());
			mRateDialog.show();
		}
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

	public class DialogActionListener {
		public final static int CANCEL = 1;
		public final static int RATE = 2;

		public void doAction(int action) {

			if (action == CANCEL) {
				if (mRateDialog != null)
					mRateDialog.dismiss();
			}

			if (action == RATE) {
				if (mRateDialog != null)
					mRateDialog.dismiss();
				final String appPackageName = getPackageName();
				try {
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://play.google.com/store/apps/details?id="
									+ appPackageName)));
				}
				mSharedPrefseditor.putBoolean("isRated", true);
				mSharedPrefseditor.commit();
			}
		}

	}

}
